package dominio;

import dominio.control.ControladorPrincipal;

/**
 * Clase principal del cliente.
 */
public class Main {

	public static void main(String args[]) {
		ControladorPrincipal cont;
		
		// Mostramos la ventana de identificaci�n
		cont = new ControladorPrincipal();
		cont.identificarse();
	}
	
}
