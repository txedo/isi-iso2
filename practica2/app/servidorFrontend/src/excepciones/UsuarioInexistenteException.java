package excepciones;

/**
 * Excepción lanzada cuando se intentan recuperar los datos de un
 * usuario no registrado en el sistema.
 */
public class UsuarioInexistenteException extends Exception {

	private static final long serialVersionUID = -7987661376143785530L;

	public UsuarioInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
