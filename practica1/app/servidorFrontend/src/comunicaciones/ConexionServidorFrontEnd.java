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
import java.util.Vector;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.control.GestorSesiones;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

// Esta clase actua como proxy de la clase ServidorFrontend, que es la fachada del servidor front-end
public class ConexionServidorFrontEnd extends UnicastRemoteObject implements IServidorFrontend{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7735848879217866237L;
	private ServidorFrontend fachada;

	public ConexionServidorFrontEnd() throws RemoteException {
		super();
		fachada = new ServidorFrontend();
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
		return fachada.identificar(login, password);
	}
	
	public void registrar(ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception {
		fachada.registrar(cliente, idSesion);
	}
	
	public void liberar(long idSesion) throws RemoteException, Exception {
		fachada.liberar(idSesion);
	}

	// -----------------------------------
	// Métodos del Gestor de Beneficiarios
	// -----------------------------------
	
	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return fachada.getBeneficiario(idSesion, dni);
	}
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return fachada.getBeneficiarioPorNSS(idSesion, nss);
	}

	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		fachada.crear(idSesion, beneficiario);
	}

	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		fachada.modificar(idSesion, beneficiario);
	}
	
	// --------------------------------------
	// Métodos del Gestor de Usuarios/Médicos
	// --------------------------------------
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		return fachada.getMedico(idSesion, dni);
	}
	
	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		fachada.crear(idSesion, medico);
	}
	
	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		fachada.modificar(idSesion, medico);
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		fachada.eliminar(idSesion, medico);
	}
	
	// ---------------------------
	// Métodos del Gestor de Citas
	// ---------------------------
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception {
		return fachada.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
	}
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, VolanteNoValidoException, SQLException, Exception {
		return fachada.pedirCita(idSesion, beneficiario, idVolante, fechaYHora, duracion);
	}
	
	public Vector<Cita> getCitas(long idSesion, String dni) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		return fachada.getCitas(idSesion, dni);
	}
	
	public void anularCita(long idSesion, Cita cita) throws RemoteException, CitaNoValidaException, SQLException, Exception {
		fachada.anularCita(idSesion, cita);
	}
	
	public long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception {
		return fachada.emitirVolante(idSesion, beneficiario, emisor, destino);
	}
		
	// ------------------------------
	// Métodos del Gestor de Mensajes
	// ------------------------------
	
	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, SesionInvalidaException {
		GestorConexionesLog.ponerMensaje("Usuario '" + GestorSesiones.getSesion(idSesion).getUsuario().getLogin() + "' ejecuta la operación " + codigoMensaje);
		return fachada.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
	}

	// Métodos aún no implementados

	/*

	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException {
		GestorMedicos.modificarCalendario(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
	}
	*/
}
