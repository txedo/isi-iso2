package excepciones;

public class UsuarioIncorrectoException extends Exception {

	private static final long serialVersionUID = 758959073762644522L;

	public UsuarioIncorrectoException () {
		super("Usuario o contraseña incorrecto.");
	}
	public UsuarioIncorrectoException(String mensaje) {
		super(mensaje);
	}

}
