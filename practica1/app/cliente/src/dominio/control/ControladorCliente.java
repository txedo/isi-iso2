package dominio.control;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import presentacion.JFLogin;
import presentacion.JFPrincipal;
import comunicaciones.RemotoCliente;
import comunicaciones.ICliente;
import comunicaciones.IServidorFrontend;
import comunicaciones.ProxyServidorFrontend;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Controlador principal de la funcionalidad de los clientes. 
 */
public class ControladorCliente {

	private ProxyServidorFrontend servidor;
	private RemotoCliente cliente;
	private ISesion sesion;
	private JFLogin ventanaLogin;
	private JFPrincipal ventanaPrincipal;
	private String usuarioAutenticado;
	
	public ControladorCliente() {
		servidor = null;
	}
	
	public JFPrincipal getVentanaPrincipal() {
		return ventanaPrincipal;
	}
	
	public void cerrarControlador () throws RemoteException, MalformedURLException, NotBoundException {
		if (cliente != null) {
			cliente.desactivar();
		}
		if (ventanaLogin != null) {
			ventanaLogin.setVisible(false);
			ventanaLogin.dispose();
		}
		if (ventanaPrincipal != null) {
			ventanaPrincipal.setVisible(false);
			ventanaPrincipal.dispose();
		}
		System.exit(0);
	}
	
	public void identificarse() {
		if (ventanaLogin != null) {
			ventanaLogin.setVisible(false);
			ventanaLogin.dispose();
		}
		if (ventanaPrincipal != null) {
			ventanaLogin.setVisible(false);
			ventanaPrincipal.dispose();
		}
		// Creamos la ventana de login y la mostramos
		ventanaLogin = new JFLogin();
		ventanaLogin.setControlador(this);
		ventanaLogin.setVisible(true);
	}
	
	public ISesion getSesion() {
		return sesion;
	}	
	
	public IServidorFrontend getServidor() {
		return servidor;
	}
	
	public String getUsuarioAutenticado () {
		return usuarioAutenticado;
	}
	
	public int getPuertoEscucha () {
		int puerto = ICliente.PUERTO_INICIAL_CLIENTE;
		try {
			puerto = cliente.getPuerto();
		} catch (RemoteException e) {
		}
		return puerto;
	}
	
	public void iniciarSesion(String direccionIP, int puerto, String login, String password) throws SQLException, UsuarioIncorrectoException, Exception {
		// Intentamos conectarnos con el servidor frontend
		if(servidor == null) {
			servidor = new ProxyServidorFrontend();
		}
		servidor.conectar(direccionIP, puerto);
		// Nos identificamos en el servidor
		sesion = (ISesion)servidor.identificar(login, password);
		usuarioAutenticado = login;
		// Una vez que el cliente se ha identificado correctamente, registramos el cliente en el servidor
		cliente = RemotoCliente.getCliente();
		cliente.activar();
		servidor.registrar((ICliente)cliente, sesion.getId());
		// Ocultamos la ventana de login
		if(ventanaLogin != null) {
			ventanaLogin.setVisible(false);
			ventanaLogin.dispose();
			ventanaLogin = null;
		}
		if(ventanaPrincipal != null) {
			ventanaPrincipal.dispose();
		}
		ventanaPrincipal = new JFPrincipal(this);
		ventanaPrincipal.iniciar();
		ventanaPrincipal.setVisible(true);
	}
	
	// Métodos con las operación del servidor
	
	public void cerrarSesion() throws RemoteException, Exception {
		servidor.liberar(sesion.getId());
	}
	
	public void crearBeneficiario(Beneficiario bene) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		servidor.crear(sesion.getId(), bene);
	}
	
	public Beneficiario getBeneficiario(String nif) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiario(sesion.getId(), nif);
	}
	
	public Beneficiario getBeneficiarioPorNSS(String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiarioPorNSS(sesion.getId(), nss);
	}
	
	public void modificarBeneficiario(Beneficiario bene) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		servidor.modificar(sesion.getId(), bene);
	}
	
	public Object operacionesDisponibles() throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.OPERACIONES_DISPONIBLES, null);
	}
	
	public Object crearUsuario(Usuario usu) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usu);
	}
	
	public Object consultarUsuario(String dni) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, dni);
	}
	
	public Object modificarUsuario(Usuario usu) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, usu);
	}
	
	public Object eliminarUsuario(Usuario usu) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, usu);
	}

	public Medico consultarMedico(String dni) throws RemoteException, MedicoInexistenteException, Exception {
		return servidor.getMedico(sesion.getId(), dni);
	}
	
	public Object obtenerHorasMedico (String dniMedico) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, dniMedico);
	}
	
	public Object obtenerMedicos (String tipo) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, tipo);
	}
	
	public void modificarMedico(Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.modificar(sesion.getId(), medico);
	}
	
	public long emitirVolante (Beneficiario bene, Medico emisor, Medico receptor) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception { 
		return servidor.emitirVolante(sesion.getId(), bene, emisor, receptor);
	}
	
	public Cita pedirCita(Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception { 
		return servidor.pedirCita(sesion.getId(), beneficiario, idMedico, fechaYHora, duracion);
	}
	
	 public Cita pedirCita(Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, VolanteNoValidoException, SQLException, Exception {
		 return servidor.pedirCita(sesion.getId(), beneficiario, idVolante, fechaYHora, duracion);
	 }
	 
	 public void anularCita (Cita cita) throws RemoteException, CitaNoValidaException, SQLException, Exception {
		 servidor.anularCita(sesion.getId(), cita);
	 }
	 
	 public Vector<Cita> obtenerCitas(String dni) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		 return servidor.getCitas(sesion.getId(), dni);
	 }
}
