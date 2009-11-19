package dominio;

/**
 * Método principal del servidor frontend.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorPresentacion cont;
		
		// Mostramos la única ventana del servidor
		cont = new ControladorPresentacion();
		cont.iniciar();
	}
	
}
