package excepciones;

public class UsuarioInexistenteException extends Exception {

	private static final long serialVersionUID = -7987661376143785530L;

	public UsuarioInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
