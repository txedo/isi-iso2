package excepciones;

/**
 * Excepción lanzada por la capa de persistencia cuando se intentan
 * recuperar los datos de un usuario no registrado en el sistema.
 */
public class UsuarioIncorrectoException extends Exception {

	private static final long serialVersionUID = 758959073762644522L;

	public UsuarioIncorrectoException(String mensaje) {
		super(mensaje);
	}

}
