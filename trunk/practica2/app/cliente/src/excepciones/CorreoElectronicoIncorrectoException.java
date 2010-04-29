package excepciones;

/**
 * Excepción lanzada al introducir un correo electrónico con un formato
 * incorrecto.
 */
public class CorreoElectronicoIncorrectoException extends Exception {

	private static final long serialVersionUID = 5200892765737178507L;

	public CorreoElectronicoIncorrectoException() {
		super("El formato del correo electrónico es incorrecto.");
	}
	
	public CorreoElectronicoIncorrectoException(String message) {
		super(message);
	}
}
