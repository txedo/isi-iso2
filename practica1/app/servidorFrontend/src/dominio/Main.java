package dominio;

import dominio.control.ControladorPrincipal;

/**
 * Clase principal del servidor frontend.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorPrincipal cont;
		
		// Mostramos la �nica ventana del servidor
		cont = new ControladorPrincipal();
		cont.mostrarVentana();
	}
	
}
