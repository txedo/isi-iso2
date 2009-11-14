package dominio;

import javax.swing.JOptionPane;
import dominio.ControladorLogin;

public class Main {

	public static void main (String args[]) {
		ControladorLogin cont;
		ISesion sesion;
		
		// Nos identificamos en el sistema
		cont = new ControladorLogin();
		cont.identificarse();
		sesion = cont.getSesion();
		JOptionPane.showMessageDialog(null, "Sesi�n iniciada correctamente:\nId: " + String.valueOf(sesion.getId()) + "\nRol: " + String.valueOf(sesion.getRol()));
	}
	
}
