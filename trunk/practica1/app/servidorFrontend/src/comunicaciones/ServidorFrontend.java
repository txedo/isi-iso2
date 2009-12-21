package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Hashtable;
import dominio.Beneficiario;
import dominio.ControladorPresentacion;
import dominio.GestorBeneficiarios;
import dominio.GestorMensajes;
import dominio.GestorSesiones;
import dominio.GestorUsuarios;
import dominio.ISesion;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import dominio.Medico;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Fachada del servidor frontend utilizada por el cliente que da acceso a
 * las clases gestoras que implementan la funcionalidad del sistema.
 */
public class ServidorFrontend extends UnicastRemoteObject implements IServidorFrontend {
	
	private static final long serialVersionUID = 7497297325860652078L;
	
	private ControladorPresentacion controlador;
	private Hashtable<Long, ICliente> clientesEscuchando = new Hashtable<Long, ICliente>();
	
	public ServidorFrontend() throws RemoteException {
		super();
		// El constructor de 'UnicastRemoteObject' exporta automáticamente
		// este objeto; aquí cancelamos la exportación porque ya llamamos
		// manualmente a 'exportObject' en el método 'conectar'
		unexportObject(this, false);
		LocateRegistry.createRegistry(PUERTO_SERVIDOR);
	}
	
	public void setControlador(ControladorPresentacion controlador) {
		this.controlador = controlador;
	}
	
    public void conectar(String ip) throws MalformedURLException, RemoteException {
        exportObject(this, PUERTO_SERVIDOR);
        try {
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_SERVIDOR) + "/" + NOMBRE_SERVIDOR, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_SERVIDOR) + "/" + NOMBRE_SERVIDOR, this);
        }
    }
    
    public void desconectar(String ip) throws RemoteException, MalformedURLException, NotBoundException {
        unexportObject(this, false);
    	Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_SERVIDOR) + "/" + NOMBRE_SERVIDOR);
    }
	
    // --- Métodos del Gestor de Sesiones ---
    
	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ISesion sesion;
		
		try {
			sesion = GestorSesiones.identificar(login, password);
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' autenticado.");
		} catch(RemoteException re) {
			controlador.getObservador().actualizarVentanas(re.getMessage());
			throw re;
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			controlador.getObservador().actualizarVentanas(uie.getMessage());
			throw uie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
		
		return sesion;
	}
	
	public void registrar(ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception {
		try {
			if (GestorSesiones.getSesion(idSesion)==null)
				throw new SesionNoIniciadaException("No se puede registrar en el servidor el cliente con la sesion "+idSesion+" porque no existe esa sesion");
			else {
				clientesEscuchando.put(idSesion, cliente);
				controlador.getObservador().actualizarVentanas("Usuario ICliente.cliente.getLogin() a la escucha.");
			}
		} catch (SesionNoIniciadaException snie) {
			controlador.getObservador().actualizarVentanas(snie.getMessage());
			throw snie;
		} catch (Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
	}
	
	public void liberar(long idSesion) throws RemoteException, Exception {
		try {
			if (GestorSesiones.getSesion(idSesion)==null)
				throw new Exception("No se puede liberar en el servidor el cliente con la sesion "+idSesion+" porque no existe esa sesion");
			else {
				clientesEscuchando.remove(idSesion);
				GestorSesiones.liberar(idSesion);
				controlador.getObservador().actualizarVentanas("Usuario '" + GestorSesiones.getSesion(idSesion).getUsuario().getLogin() + "' desconectado.");
			}
		} catch (Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
	}
	
	// --- Métodos del Gestor de Beneficiarios ---
	
	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		String login;
		
		try { 
			beneficiario = GestorBeneficiarios.getBeneficiario(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' consulta el beneficiario con NIF " + dni);
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			controlador.getObservador().actualizarVentanas("Error al obtener el medico asociado al beneficiario.\n "+uie.getMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			controlador.getObservador().actualizarVentanas("Error al obtener el centro de salud del medico asociado al beneficiario.\n "+csie.getMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			controlador.getObservador().actualizarVentanas(bie.getMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			controlador.getObservador().actualizarVentanas(oie.getMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			controlador.getObservador().actualizarVentanas(sie.getMessage());
			throw sie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
		
		return beneficiario;
	}
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		String login;
		
		try {
			beneficiario = GestorBeneficiarios.getBeneficiarioPorNSS(idSesion, nss);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' consulta el beneficiario con NSS " + nss);
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			controlador.getObservador().actualizarVentanas("Error al obtener el medico asociado al beneficiario.\n "+uie.getMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			controlador.getObservador().actualizarVentanas("Error al obtener el centro de salud del medico asociado al beneficiario.\n "+csie.getMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			controlador.getObservador().actualizarVentanas(bie.getMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			controlador.getObservador().actualizarVentanas(oie.getMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			controlador.getObservador().actualizarVentanas(sie.getMessage());
			throw sie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
		
		return beneficiario;
	}

	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		String login;
		
		try {
			GestorBeneficiarios.crear(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' crea el beneficiario con NIF " + beneficiario.getNif());
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			controlador.getObservador().actualizarVentanas("Error al obtener el medico asociado al beneficiario.\n "+uie.getMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			controlador.getObservador().actualizarVentanas("Error al obtener el centro de salud del medico asociado al beneficiario.\n "+csie.getMessage());
			throw csie;
		} catch(BeneficiarioYaExistenteException bye) {
			controlador.getObservador().actualizarVentanas(bye.getMessage());
			throw bye;
		} catch(OperacionIncorrectaException oie) {
			controlador.getObservador().actualizarVentanas(oie.getMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			controlador.getObservador().actualizarVentanas(sie.getMessage());
			throw sie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
	}

	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		String login;
		
		try {
			GestorBeneficiarios.modificar(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' modifica el beneficiario con NIF " + beneficiario.getNif());
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			controlador.getObservador().actualizarVentanas("Error al obtener el medico asociado al beneficiario.\n "+uie.getMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			controlador.getObservador().actualizarVentanas("Error al obtener el centro de salud del medico asociado al beneficiario.\n "+csie.getMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			controlador.getObservador().actualizarVentanas(bie.getMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			controlador.getObservador().actualizarVentanas(oie.getMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			controlador.getObservador().actualizarVentanas(sie.getMessage());
			throw sie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
	}
	
	// --- Métodos del Gestor de Usuarios/Médicos ---
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		Medico medico;
		String login;
		
		try {
			medico = GestorUsuarios.getMedico(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' consulta el médico con DNI " + dni);
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			controlador.getObservador().actualizarVentanas(mie.getMessage());
			throw mie;
		} catch(SesionInvalidaException sie) {
			controlador.getObservador().actualizarVentanas(sie.getMessage());
			throw sie;
		} catch(OperacionIncorrectaException oie) {
			controlador.getObservador().actualizarVentanas(oie.getMessage());
			throw oie;
		} catch(CentroSaludIncorrectoException csie) {
			controlador.getObservador().actualizarVentanas(csie.getMessage());
			throw csie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;			
		}
		
		return medico;
	}
	
	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		String login;
		
		try {
			GestorUsuarios.crearMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' crea el médico con DNI " + medico.getDni());
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(MedicoYaExistenteException myee) {
			controlador.getObservador().actualizarVentanas(myee.getMessage());
			throw myee;
		} catch(SesionInvalidaException sie) {
			controlador.getObservador().actualizarVentanas(sie.getMessage());
			throw sie;
		} catch(OperacionIncorrectaException oie) {
			controlador.getObservador().actualizarVentanas(oie.getMessage());
			throw oie;
		} catch(CentroSaludIncorrectoException csie) {
			controlador.getObservador().actualizarVentanas(csie.getMessage());
			throw csie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
	}
	
	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			GestorUsuarios.modificarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' modifica el médico con DNI " + medico.getDni());
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			controlador.getObservador().actualizarVentanas(mie.getMessage());
			throw mie;
		} catch(SesionInvalidaException sie) {
			controlador.getObservador().actualizarVentanas(sie.getMessage());
			throw sie;
		} catch(OperacionIncorrectaException oie) {
			controlador.getObservador().actualizarVentanas(oie.getMessage());
			throw oie;
		} catch(CentroSaludIncorrectoException csie) {
			controlador.getObservador().actualizarVentanas(csie.getMessage());
			throw csie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			GestorUsuarios.eliminarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' elimina el médico con DNI " + medico.getDni());
		} catch(SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			controlador.getObservador().actualizarVentanas(mie.getMessage());
			throw mie;
		} catch(SesionInvalidaException sie) {
			controlador.getObservador().actualizarVentanas(sie.getMessage());
			throw sie;
		} catch(OperacionIncorrectaException oie) {
			controlador.getObservador().actualizarVentanas(oie.getMessage());
			throw oie;
		} catch(CentroSaludIncorrectoException csie) {
			controlador.getObservador().actualizarVentanas(csie.getMessage());
			throw csie;
		} catch(Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
	}
	
	/*
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException {
		return GestorCitas.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
	}
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException {
		return GestorCitas.pedirCita(idSesion, beneficiario, idVolante, fechaYHora, duracion);
	}
	
	public Vector<Cita> getCitas(long idSesion, String dni) throws RemoteException {
		return GestorCitas.getCitas(idSesion, dni);
	}
	
	public void anularCita(long idSesion, Cita cita) throws RemoteException {
		GestorCitas.anularCita(idSesion, cita);
	}
	
	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException {
		GestorBeneficiarios.crear(idSesion, beneficiario);
	}
	
	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException {
		GestorBeneficiarios.modificar(idSesion, beneficiario);
	}
	*/

	/*
	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException {
		GestorMedicos.modificarCalendario(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
	}
	*/
	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, SesionInvalidaException {
		controlador.getObservador().actualizarVentanas("Usuario '" + GestorSesiones.getSesion(idSesion).getUsuario().getLogin() + "' ejecuta la operación " + codigoMensaje);
		return GestorMensajes.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
	}
	/*
	public long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException {
		return GestorMedicos.emitirVolante(idSesion, beneficiario, emisor, destino);
	} */
}
