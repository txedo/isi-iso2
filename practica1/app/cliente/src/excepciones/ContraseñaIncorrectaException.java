package excepciones;

/**
 * Excepción lanzada al introducir una contraseña con una longitud
 * demasiado corta o con caracteres no permitidos.
 */
public class ContraseñaIncorrectaException extends Exception {

	private static final long serialVersionUID = -6560620333476127287L;

	public ContraseñaIncorrectaException() {
		super("La contraseña debe ser alfnumérica y tener como mínimo 8 caracteres.");
	}
	
	public ContraseñaIncorrectaException(String mensaje) {
		super(mensaje);
	}
	
}
