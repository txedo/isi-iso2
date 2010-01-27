package excepciones;

public class CorreoElectronicoIncorrectoException extends Exception {

	private static final long serialVersionUID = 5200892765737178507L;

	public CorreoElectronicoIncorrectoException () {
		super("El formato del correo electr�nico es incorrecto.");
	}
	
	public CorreoElectronicoIncorrectoException (String message) {
		super(message);
	}
}
