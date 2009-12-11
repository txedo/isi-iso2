package excepciones;

public class UsuarioYaExistenteException extends Exception {

	private static final long serialVersionUID = -4667296215935691238L;

	public UsuarioYaExistenteException(String mensaje) {
		super(mensaje);
	}
	
}
