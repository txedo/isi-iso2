package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.control.GestorBeneficiarios;
import dominio.control.GestorCitas;
import dominio.control.GestorMensajes;
import dominio.control.GestorSesiones;
import dominio.control.GestorUsuarios;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Fachada del servidor frontend utilizada por el cliente que da acceso a
 * las clases gestoras que implementan la funcionalidad del sistema.
 */
public class ServidorFrontend extends UnicastRemoteObject implements IServidorFrontend {
	
	private static final long serialVersionUID = 7497297325860652078L;
	
	private Hashtable<Long, ICliente> clientesEscuchando = new Hashtable<Long, ICliente>();
	
	public ServidorFrontend() throws RemoteException {
		super();
		// El constructor de 'UnicastRemoteObject' exporta automáticamente
		// este objeto; aquí cancelamos la exportación porque ya llamamos
		// manualmente a 'exportObject' en el método 'conectar'
		unexportObject(this, false);
		LocateRegistry.createRegistry(PUERTO_SERVIDOR);
	}
	
    public void activar(String ip) throws MalformedURLException, RemoteException {
        exportObject(this, PUERTO_SERVIDOR);
        try {
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_SERVIDOR) + "/" + NOMBRE_SERVIDOR, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_SERVIDOR) + "/" + NOMBRE_SERVIDOR, this);
        }
    }
    
    public void desactivar(String ip) throws RemoteException, MalformedURLException, NotBoundException {
        unexportObject(this, false);
    	Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_SERVIDOR) + "/" + NOMBRE_SERVIDOR);
    }
	
    // ------------------------------
    // Métodos del Gestor de Sesiones
    // ------------------------------
    
	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ISesion sesion;
		
		try {
			// Nos identificamos en el sistema con el login y la password
			sesion = GestorSesiones.identificar(login, password);
			GestorConexionesLog.ponerMensaje("Usuario '" + login + "' autenticado.");
		} catch(RemoteException re) {
			GestorConexionesLog.ponerMensaje("Error de conexión al autenticar el usuario '" + login + "': " + re.getLocalizedMessage());
			throw re;
		} catch(SQLException se) {
			GestorConexionesLog.ponerMensaje("Error SQL al autenticar el usuario '" + login + "': " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			GestorConexionesLog.ponerMensaje("Error en el nombre de usuario o la contraseña al autenticar el usuario '" + login + "': " + uie.getLocalizedMessage());
			throw uie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al autenticar un usuario: " + e.toString());
			throw e;
		}
		
		return sesion;
	}
	
	public void registrar(ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception {
		try {
			if (GestorSesiones.getSesion(idSesion)==null)
				throw new SesionNoIniciadaException("No se puede registrar en el servidor el cliente con la sesion "+idSesion+" porque no existe esa sesion");
			else {
				String url;
				url = "rmi://" + cliente.getDireccionIP() + ":" + String.valueOf(cliente.getPuerto()) + "/" + ICliente.NOMBRE_CLIENTE;
		        ICliente terminal = (ICliente)Naming.lookup(url);
		        clientesEscuchando.put(idSesion, terminal);
				GestorConexionesLog.ponerMensaje("Nuevo cliente a la escucha. ID de sesion " + idSesion);
			}
		} catch (SesionNoIniciadaException snie) {
			GestorConexionesLog.ponerMensaje(snie.getMessage());
			throw snie;
		} catch (Exception e) {
			GestorConexionesLog.ponerMensaje(e.getMessage());
			throw e;
		}
	}
	
	public void liberar(long idSesion) throws RemoteException, Exception {
		try {
			ISesion sesion = GestorSesiones.getSesion(idSesion);
			if (sesion == null) {
				throw new Exception("No se puede liberar en el servidor el cliente con la sesion "+idSesion+" porque no existe esa sesion");
			}
			else {
				ICliente cliente = clientesEscuchando.get(idSesion);
				cliente.desactivar();
				clientesEscuchando.remove(idSesion);
				String mensajeLog = "Usuario '" + GestorSesiones.getSesion(idSesion).getUsuario().getLogin() + "' desconectado.";
				GestorSesiones.liberar(idSesion);
				GestorConexionesLog.ponerMensaje(mensajeLog);
			}
		} catch (Exception e) {
			GestorConexionesLog.ponerMensaje(e.getMessage());
			throw e;
		}
	}

	// -----------------------------------
	// Métodos del Gestor de Beneficiarios
	// -----------------------------------
	
	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		String login;
		
		try {
			// Obtenemos el beneficiario con el NIF indicado
			beneficiario = GestorBeneficiarios.getBeneficiario(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Consultado el beneficiario con NIF " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al consultar el beneficiario con NIF " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el médico asociado al beneficiario con NIF " + dni + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico asociado al beneficiario con NIF " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al consultar un beneficiario inexistente con NIF " + dni + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de consulta del beneficiario con NIF " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para consultar el beneficiario con NIF " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al consultar un beneficiario por NIF: " + e.toString());
			throw e;
		}
		
		return beneficiario;
	}
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		String login;
		
		try {
			// Obtenemos el beneficiario con el NSS indicado
			beneficiario = GestorBeneficiarios.getBeneficiarioPorNSS(idSesion, nss);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Consultado el beneficiario con NSS " + nss + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al consultar el beneficiario con NSS " + nss + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el médico asociado al beneficiario con NSS " + nss + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico asociado al beneficiario con NSS " + nss + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al consultar un beneficiario inexistente con NSS " + nss + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de consulta del beneficiario con NSS " + nss + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para consultar el beneficiario con NSS " + nss + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al consultar un beneficiario por NSS: " + e.toString());
			throw e;
		}
		
		return beneficiario;
	}

	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		String login;
		
		try {
			// Añadimos un nuevo beneficiario al sistema
			GestorBeneficiarios.crear(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Creado el beneficiario con NIF " + beneficiario.getNif() + " y NSS " + beneficiario.getNss() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al crear el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el médico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a crear: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a crear: " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioYaExistenteException byee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al crear un beneficiario con NIF " + beneficiario.getNif() + " que ya existe: " + byee.getLocalizedMessage());
			throw byee;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de creación de un beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para crear un beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al crear un beneficiario: " + e.toString());
			throw e;
		}
	}

	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		String login;
		
		try {
			// Modificamos un beneficiario existente del sistema
			GestorBeneficiarios.modificar(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Modificado el beneficiario con NIF " + beneficiario.getNif() + " y NSS " + beneficiario.getNss() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al modificar el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el médico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a modificar: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a modificar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al modificar un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de modificación del beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para modificar el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al modificar un beneficiario: " + e.toString());
			throw e;
		}
	}
	
	// --------------------------------------
	// Métodos del Gestor de Usuarios/Médicos
	// --------------------------------------
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		Medico medico;
		String login;
		
		try {
			// Obtenemos el médico con el DNI indicado
			medico = GestorUsuarios.getMedico(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Consultado el médico con DNI " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al consultar el médico con DNI " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al consultar un médico inexistente con DNI " + dni + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico con DNI " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de consulta del médico con DNI " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para consultar el médico con DNI " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al consultar un médico: " + e.toString());
			throw e;
		}
		
		return medico;
	}
	
	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		String login;
		
		try {
			// Añadimos un nuevo médico al sistema
			GestorUsuarios.crearMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Creado el médico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al crear el médico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoYaExistenteException myee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al crear un médico con DNI " + medico.getDni() + " que ya existe: " + myee.getLocalizedMessage());
			throw myee;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico con DNI " + medico.getDni() + " que se iba a crear: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de creación de un médico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para crear el médico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al crear un médico: " + e.toString());
			throw e;
		}
	}
	
	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Modificamos un médico existente del sistema
			GestorUsuarios.modificarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Modificado el médico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al modificar el médico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al modificar un médico inexistente con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico con DNI " + medico.getDni() + " que se iba a modificar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de modificación del médico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para modificar el médico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al modificar un médico: " + e.toString());
			throw e;
		}
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Eliminamos un médico del sistema
			GestorUsuarios.eliminarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Eliminado el médico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al eliminar el médico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al eliminar un médico inexistente con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico con DNI " + medico.getDni() + " que se iba a eliminar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de eliminación del médico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para eliminar el médico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al eliminar un médico: " + e.toString());
			throw e;
		}
	}
	
	// ---------------------------
	// Métodos del Gestor de Citas
	// ---------------------------
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception {
		Cita cita;
		String login;
		
		try {
			// Pedimos una cita para un beneficiario y un médico dados
			cita = GestorCitas.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Solicitada una cita para el médico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al pedir una cita para el médico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al pedir una cita para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al pedir una cita para un médico inexistente con DNI " + idMedico + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el médico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + csie.getLocalizedMessage());
			throw csie;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al pedir una cita para el médico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + " a una fecha y hora no válidas: " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de petición de una cita para el médico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para pedir una cita para el médico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al solicitar una cita: " + e.toString());
			throw e;
		}
		
		return cita;
	}
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, VolanteNoValidoException, SQLException, Exception {
		Cita cita;
		String login;
		
		try {
			// Pedimos una cita para un beneficiario a partir de un volante
			cita = GestorCitas.pedirCita(idSesion, beneficiario, idVolante, fechaYHora, duracion);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Solicitada una cita para el el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al pedir una cita para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el médico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + csie.getLocalizedMessage());
			throw csie;
		} catch(VolanteNoValidoException vnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir de un volante con el id inexistente o inválido " + idVolante + ": " + vnve.getLocalizedMessage());
			throw vnve;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + " a una fecha y hora no válidas: " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de petición de una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para pedir una cita para y el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al solicitar una cita a partir de un volante: " + e.toString());
			throw e;
		}
		
		return cita;
	}
	
	public Vector<Cita> getCitas(long idSesion, String dni) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		Vector<Cita> citas;
		String login;
		
		try {
			// Obtenemos las citas que tiene un beneficiario
			citas = GestorCitas.getCitas(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Consultadas las citas del beneficiario con NIF " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al consultar las citas del beneficiario con NIF " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al consultar las citas de un beneficiario inexistente con NIF " + dni + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el médico asociado al beneficiario con NIF " + dni + " para el que se iban a consultar las citas: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico asociado al beneficiario con NIF " + dni + " para el que se iban a consultar las citas: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de consulta de las citas del beneficiario con NIF " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para consultar las citas del beneficiario con NIF " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al consultar las citas de un beneficiario: " + e.toString());
			throw e;
		}
		
		return citas;
	}
	
	public void anularCita(long idSesion, Cita cita) throws RemoteException, CitaNoValidaException, SQLException, Exception {
		String login;
		
		try {
			// Eliminamos una cita existente
			GestorCitas.anularCita(idSesion, cita);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Anulada una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al anular una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(CitaNoValidaException cnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al anular una cita inexistente del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + cnve.getLocalizedMessage());
			throw cnve;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de anulación de una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para anular una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al anular una cita de un beneficiario: " + e.toString());
			throw e;
		}
	}
	
	public long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception {
		long idVolante;
		String login;
		
		try {
			// Solicitamos un volante para un cierto beneficiario y médico
			idVolante = GestorCitas.emitirVolante(idSesion, beneficiario, emisor, destino);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Emitido un volante para el médico con DNI " + destino.getDni() + " y el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error SQL al emitir un volante para el médico con DNI " + destino.getDni() + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al solicitar un volante para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al solicitar un volante para un médico inexistente con DNI " + emisor.getDni() + " o " + destino.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(VolanteNoValidoException vnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al solicitar un volante para un médico no especialista con DNI " + destino.getDni() + ": " + vnve.getLocalizedMessage());
			throw vnve;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el médico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a emitir un volante: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al recuperar el centro de salud del médico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a emitir un volante: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, "Error al realizar una operación no permitida de emisión de un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje("Error al comprobar la sesión con id " + idSesion + " para emitir un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje("Error inesperado al emitir un volante: " + e.toString());
			throw e;
		}
		
		return idVolante;
	}
		
	// ------------------------------
	// Métodos del Gestor de Mensajes
	// ------------------------------
	
	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, SesionInvalidaException {
		GestorConexionesLog.ponerMensaje("Usuario '" + GestorSesiones.getSesion(idSesion).getUsuario().getLogin() + "' ejecuta la operación " + codigoMensaje);
		return GestorMensajes.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
	}

	// Métodos aún no implementados

	/*

	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException {
		GestorMedicos.modificarCalendario(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
	}
	*/

}
