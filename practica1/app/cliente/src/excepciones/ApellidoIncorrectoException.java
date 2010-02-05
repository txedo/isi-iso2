package excepciones;

public class ApellidoIncorrectoException extends Exception {

	private static final long serialVersionUID = -6784531025406118455L;

	public ApellidoIncorrectoException() {
		super("Los apellidos sólo pueden contener caracteres alfabéticos y espacios.");
	}
	
	public ApellidoIncorrectoException(String message) {
		super(message);
	}

}
