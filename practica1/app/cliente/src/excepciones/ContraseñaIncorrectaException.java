package excepciones;

public class Contrase�aIncorrectaException extends Exception {

	private static final long serialVersionUID = -6560620333476127287L;

	public Contrase�aIncorrectaException (String mensaje) {
		super(mensaje);
	}
}
