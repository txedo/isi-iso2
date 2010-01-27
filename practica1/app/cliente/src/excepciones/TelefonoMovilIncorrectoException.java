package excepciones;

public class TelefonoMovilIncorrectoException extends Exception {

	private static final long serialVersionUID = 4665823293646637955L;

	public TelefonoMovilIncorrectoException () {
		super("El teléfono móvil deben ser 9 dígitos sin separadores y debe comenzar por 6.");
	}
	
	public TelefonoMovilIncorrectoException (String message) {
		super(message);
	}
}
