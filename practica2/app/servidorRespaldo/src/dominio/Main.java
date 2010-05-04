package dominio;

import dominio.control.ControladorRespaldo;

/**
 * Clase principal del servidor de respaldo.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorRespaldo cont;
		
		// Mostramos la única ventana del servidor
		cont = new ControladorRespaldo();
		cont.mostrarVentana();	
	}

}
