package dominio;

/**
 * Clase principal del servidor de respaldo.
 */
public class Main {
	
	public static void main(String[] args) {
		Controlador cont;
		
		// Mostramos la única ventana del servidor
		cont = new Controlador();
		cont.mostrarVentana();	
	}

}
