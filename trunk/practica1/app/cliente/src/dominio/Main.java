package dominio;

import javax.swing.JOptionPane;
import dominio.ControladorLogin;

/**
 * M�todo principal del cliente.
 */
public class Main {

	public static void main(String args[]) {
		ControladorLogin cont;
		ISesion sesion;
		
		// Mostramos la ventana de identificaci�n
		cont = new ControladorLogin();
		cont.identificarse();
		sesion = cont.getSesion();
		if(sesion != null) {
			JOptionPane.showMessageDialog(null, "Sesi�n iniciada correctamente:\nId: " + String.valueOf(sesion.getId()) + "\nRol: " + String.valueOf(sesion.getRol()));
		}
	}
	
}
