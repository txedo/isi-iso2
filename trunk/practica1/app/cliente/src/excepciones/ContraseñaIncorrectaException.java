package excepciones;

public class ContraseñaIncorrectaException extends Exception {

	private static final long serialVersionUID = -6560620333476127287L;

	public ContraseñaIncorrectaException (String mensaje) {
		super(mensaje);
	}
}
