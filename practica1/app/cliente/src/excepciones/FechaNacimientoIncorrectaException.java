package excepciones;

/**
 * Excepción lanzada al seleccionar una fecha de nacimiento no válida.
 */
public class FechaNacimientoIncorrectaException extends Exception {

	private static final long serialVersionUID = -5493992740330258198L;

	public FechaNacimientoIncorrectaException() {
		super("La fecha de nacimiento no es válida.");
	}
	
	public FechaNacimientoIncorrectaException(String message) {
		super(message);
	}

}
