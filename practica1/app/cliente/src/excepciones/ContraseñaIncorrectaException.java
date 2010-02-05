package excepciones;

/**
 * Excepci�n lanzada al introducir una contrase�a con una longitud
 * demasiado corta o con caracteres no permitidos.
 */
public class Contrase�aIncorrectaException extends Exception {

	private static final long serialVersionUID = -6560620333476127287L;

	public Contrase�aIncorrectaException() {
		super("La contrase�a debe ser alfnum�rica y tener como m�nimo 8 caracteres.");
	}
	
	public Contrase�aIncorrectaException(String mensaje) {
		super(mensaje);
	}
	
}
