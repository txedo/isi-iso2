package dominio;

import dominio.control.ControladorFrontend;

/**
 * Clase principal del servidor frontend.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorFrontend cont;
		
		// Mostramos la única ventana del servidor
		cont = new ControladorFrontend();
		cont.mostrarVentana();
	}
	
}
