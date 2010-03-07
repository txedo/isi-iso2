package excepciones;

/**
 * Excepción lanzada al introducir un nombre de usuario con un
 * formato incorrecto. 
 */
public class LoginIncorrectoException extends Exception {

	private static final long serialVersionUID = -2028475544381714224L;

	public LoginIncorrectoException() {
		super("El nombre de usuario sólo puede contener caracteres alfanuméricos.");
	}
	
	public LoginIncorrectoException(String mensaje) {
		super(mensaje);
	}
	
}
