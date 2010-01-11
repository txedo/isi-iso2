package dominio;

import java.rmi.RemoteException;
import java.sql.SQLException;

import comunicaciones.ICliente;
import comunicaciones.Cliente;
import comunicaciones.IServidorFrontend;
import comunicaciones.ProxyServidorFrontend;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.OperacionesAuxiliares;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.MedicoInexistenteException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import presentacion.JFLogin;
import presentacion.JFPrincipal;

/**
 * Controlador que gestiona la identificación de los usuarios. 
 */
public class ControladorLogin implements OperacionesAuxiliares {

	private ProxyServidorFrontend servidor;
	private Cliente cliente;
	private ISesion sesion;
	private JFLogin ventana;
	private JFPrincipal ventanaPrincipal;
	
	public ControladorLogin() {
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
		cliente = new Cliente();
		cliente.conectar();
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
	
	public Object operacionesDisponibles () throws RemoteException, SesionInvalidaException {
		return servidor.mensajeAuxiliar(sesion.getId(), OPERACIONES_DISPONIBLES, null);
	}

	public Medico consultarMedico(String dni) throws RemoteException, MedicoInexistenteException, Exception {
		return servidor.getMedico(sesion.getId(), dni);
	}
}
