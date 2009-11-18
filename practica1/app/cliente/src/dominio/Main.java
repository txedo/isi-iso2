package dominio;

import javax.swing.JOptionPane;
import dominio.ControladorLogin;

/**
 * Método principal del cliente.
 */
public class Main {

	public static void main(String args[]) {
		ControladorLogin cont;
		ISesion sesion;
		
		// Mostramos la ventana de identificación
		cont = new ControladorLogin();
		cont.identificarse();
		sesion = cont.getSesion();
		if(sesion != null) {
			JOptionPane.showMessageDialog(null, "Sesión iniciada correctamente:\nId: " + String.valueOf(sesion.getId()) + "\nRol: " + String.valueOf(sesion.getRol()));
		}
	}
	
}
