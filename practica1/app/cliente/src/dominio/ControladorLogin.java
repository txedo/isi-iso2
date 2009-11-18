package dominio;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import comunicaciones.IServidorFrontend;
import comunicaciones.ProxyServidorFrontend;
import excepciones.UsuarioIncorrectoException;
import presentacion.JFLogin;

/**
 * Controlador que gestiona la identificación de los usuarios. 
 */
public class ControladorLogin {

	private IServidorFrontend servidor;
	private ISesion sesion;
	private JFLogin ventana;
	
	public void identificarse() {
		// Creamos la ventana de login y la mostramos
		ventana = new JFLogin();
		ventana.setControlador(this);
		ventana.setModal(true);
		ventana.setVisible(true);
	}
	
	public ISesion getSesion() {
		return sesion;
	}	
	
	public IServidorFrontend getServidor() {
		return servidor;
	}
	
	public void iniciarSesion(String login, String password) {
		try {
			// Intentamos conectarnos con el servidor frontend
			servidor = new ProxyServidorFrontend();
			((ProxyServidorFrontend)servidor).conectar("127.0.0.1");
			// Nos identificamos en el servidor
			sesion = (ISesion)servidor.identificar(login, password);
			// Ocultamos la ventana de login
			ventana.setVisible(false);
			ventana.dispose();
		} catch(UsuarioIncorrectoException e) {
			JOptionPane.showMessageDialog(ventana, "Error en la identificación del usuario:\n" + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(ventana, "Error en la conexión con el servidor:\n" + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(ventana, "Error en la conexión con el servidor:\n" + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(ventana, "Error en la conexión con el servidor:\n" + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(ventana, "Error en el sistema:\n" + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(ventana, "Error general:\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
