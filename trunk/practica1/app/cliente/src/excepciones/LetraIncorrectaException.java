package excepciones;

public class LetraIncorrectaException extends Exception {

	private static final long serialVersionUID = -7484685419011399714L;

	public LetraIncorrectaException () {
		super("La letra del domicilio debe ser un carácter alfabético.");
	}
}
