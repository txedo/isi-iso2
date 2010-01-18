package dominio;

import dominio.control.ControladorPrincipal;

/**
 * Clase principal del servidor de respaldo.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorPrincipal cont;
		
		// Mostramos la única ventana del servidor
		cont = new ControladorPrincipal();
		cont.mostrarVentana();	
	}

}
