package dominio;

import java.rmi.Naming;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import comunicaciones.IServidorFrontend;
import excepciones.UsuarioIncorrectoException;
import presentacion.JFLogin;

public class Controlador {

	private IServidorFrontend servidor;
	private ISesion x;
	private JFLogin l;
	
	public ISesion identificarse() {
		l = new JFLogin();
		l.setControlador(this);
		l.setModal(true);
		l.setVisible(true);
		return x;
	}
	
	public void setServidor(IServidorFrontend servidor) {
		this.servidor = servidor;
	}
	
	public void iniciarSesion(String login, String password) {
		try {
			servidor = (IServidorFrontend)Naming.lookup("rmi://127.0.0.1:2995/servidorfrontend");
			x = (ISesion)servidor.identificar(login, password);
			l.setVisible(false);
			l.dispose();
		} catch(UsuarioIncorrectoException e) {
			JOptionPane.showMessageDialog(l, "Error: " + e.toString());
		} catch(SQLException e) {
			JOptionPane.showMessageDialog(l, "Error: " + e.toString());
		} catch(Exception e) {
			JOptionPane.showMessageDialog(l, "Error: " + e.toString());
		}
	}
	
}
