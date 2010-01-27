package excepciones;

public class FechaNacimientoIncorrectaException extends Exception {

	private static final long serialVersionUID = -5493992740330258198L;

	public FechaNacimientoIncorrectaException() {
		super("La fecha de nacimiento no es v�lida.");
	}
	
	public FechaNacimientoIncorrectaException(String message) {
		super(message);
	}

}
