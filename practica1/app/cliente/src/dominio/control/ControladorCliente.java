package dominio.control;

import java.rmi.RemoteException;
import java.sql.SQLException;

import comunicaciones.ConexionCliente;
import comunicaciones.ICliente;
import comunicaciones.IServidorFrontend;
import comunicaciones.ProxyServidorFrontend;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.OperacionesAuxiliares;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.MedicoInexistenteException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import presentacion.JFLogin;
import presentacion.JFPrincipal;

/**
 * Controlador principal de la funcionalidad de los clientes. 
 */
public class ControladorCliente implements OperacionesAuxiliares {

	private ProxyServidorFrontend servidor;
	private ConexionCliente cliente;
	private ISesion sesion;
	private JFLogin ventana;
	private JFPrincipal ventanaPrincipal;
	
	public ControladorCliente() {
		servidor = null;
	}
	
	public void identificarse() {
		// Creamos la ventana de login y la mostramos
		ventana = new JFLogin();
		ventana.setControlador(this);
		//ventana.setModal(true);
		ventana.setVisible(true);
	}
	
	public ISesion getSesion() {
		return sesion;
	}	
	
	public IServidorFrontend getServidor() {
		return servidor;
	}
	
	public void iniciarSesion(String direccionIP, String login, String password) throws SQLException, UsuarioIncorrectoException, Exception {
		// Intentamos conectarnos con el servidor frontend
		if(servidor == null) {
			servidor = new ProxyServidorFrontend();
		}
		servidor.conectar(direccionIP);
		// Nos identificamos en el servidor
		sesion = (ISesion)servidor.identificar(login, password);
		// Una vez que el cliente se ha identificado correctamente, registramos el cliente en el servidor
		cliente = new ConexionCliente();
		cliente.activar();
		servidor.registrar((ICliente)cliente, sesion.getId());
		// Ocultamos la ventana de login
		ventana.setVisible(false);
		ventana.dispose();
		ventanaPrincipal = new JFPrincipal();
		ventanaPrincipal.setControlador(this);
		ventanaPrincipal.iniciar();
		ventanaPrincipal.setVisible(true);
	}
	
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
	
	public Object operacionesDisponibles () throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), OPERACIONES_DISPONIBLES, null);
	}
	
	public Object crearUsuario (Usuario usu) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), CREAR_USUARIO, usu);
	}
	
	public Object consultarUsuario (String dni) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), CONSULTAR_USUARIO, dni);
	}
	
	public Object modificarUsuario (Usuario usu) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), MODIFICAR_USUARIO, usu);
	}
	
	public Object eliminarUsuario (Usuario usu) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), ELIMINAR_USUARIO, usu);
	}

	public Medico consultarMedico(String dni) throws RemoteException, MedicoInexistenteException, Exception {
		return servidor.getMedico(sesion.getId(), dni);
	}
	
	public Object obtenerHorasMedico (String dniMedico) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), CONSULTAR_CITAS_MEDICO, dniMedico);
	}
	
	public Object obtenerMedicos (String tipo) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(sesion.getId(), OBTENER_MEDICOS_TIPO, tipo);
	}
	
	public long emitirVolante (Beneficiario bene, Medico emisor, Medico receptor) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception { 
		return servidor.emitirVolante(sesion.getId(), bene, emisor, receptor);
	}
}
