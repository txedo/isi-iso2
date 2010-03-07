package excepciones;

/**
 * Excepci�n lanzada al seleccionar una fecha de nacimiento no v�lida.
 */
public class FechaNacimientoIncorrectaException extends Exception {

	private static final long serialVersionUID = -5493992740330258198L;

	public FechaNacimientoIncorrectaException() {
		super("La fecha de nacimiento no es v�lida.");
	}
	
	public FechaNacimientoIncorrectaException(String message) {
		super(message);
	}

}
