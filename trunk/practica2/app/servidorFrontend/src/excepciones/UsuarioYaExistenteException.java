package excepciones;

/**
 * Excepción lanzada cuando se intenta crear un nuevo usuario con un
 * NIF que ya tiene otra persona registrada en el sistema.
 */
public class UsuarioYaExistenteException extends Exception {

	private static final long serialVersionUID = -4667296215935691238L;

	public UsuarioYaExistenteException(String mensaje) {
		super(mensaje);
	}
	
}
