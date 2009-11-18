package dominio;

/**
 * Método principal del servidor frontend.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorPresentacion cont;
		
		cont = new ControladorPresentacion();
		cont.iniciar();
	}
	
}
