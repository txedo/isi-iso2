package dominio;

/**
 * Clase principal del servidor frontend.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorPresentacion cont;
		
		// Mostramos la �nica ventana del servidor
		cont = new ControladorPresentacion();
		cont.mostrarVentana();
	}
	
}
