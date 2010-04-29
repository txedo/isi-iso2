package dominio;

import dominio.control.ControladorCliente;

/**
 * Clase principal del cliente.
 */
public class Main {

	public static void main(String args[]) {
		ControladorCliente cont;
		
		// Mostramos la ventana de identificación
		cont = new ControladorCliente();
		cont.identificarse();
	}
	
}
