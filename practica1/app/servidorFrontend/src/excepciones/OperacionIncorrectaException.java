package excepciones;

public class OperacionIncorrectaException extends Exception {

	private static final long serialVersionUID = 2316421371945471526L;

	public OperacionIncorrectaException (String mensaje) {
		super(mensaje);
	}
}
