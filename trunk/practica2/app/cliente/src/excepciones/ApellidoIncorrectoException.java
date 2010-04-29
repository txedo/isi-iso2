package excepciones;

/**
 * Excepción lanzada al introducir unos apellidos con un formato incorrecto.
 */
public class ApellidoIncorrectoException extends Exception {

	private static final long serialVersionUID = -6784531025406118455L;

	public ApellidoIncorrectoException() {
		super("Los apellidos sólo pueden contener caracteres alfabéticos, espacios y guiones");
	}
	
	public ApellidoIncorrectoException(String message) {
		super(message);
	}

}
