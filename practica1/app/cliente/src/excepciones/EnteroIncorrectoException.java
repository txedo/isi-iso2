package excepciones;

public class EnteroIncorrectoException extends Exception {

	private static final long serialVersionUID = -4573938396507767050L;

	public EnteroIncorrectoException () {
		super("El número debe ser un entero positivo.");
	}
	
	public EnteroIncorrectoException(String message) {
		super(message);
	}
}
