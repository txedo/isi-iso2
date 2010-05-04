package dominio;

import dominio.control.ControladorRespaldo;

/**
 * Clase principal del servidor de respaldo.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorRespaldo cont;
		
		// Mostramos la �nica ventana del servidor
		cont = new ControladorRespaldo();
		cont.mostrarVentana();	
	}

}
