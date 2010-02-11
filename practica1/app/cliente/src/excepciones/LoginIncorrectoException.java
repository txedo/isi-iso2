package excepciones;

public class LoginIncorrectoException extends Exception {

	private static final long serialVersionUID = -2028475544381714224L;

	public LoginIncorrectoException() {
		super("El nombre de usuario tiene un formato incorrecto.");
	}
	
	public LoginIncorrectoException(String mensaje) {
		super(mensaje);
	}
	
}