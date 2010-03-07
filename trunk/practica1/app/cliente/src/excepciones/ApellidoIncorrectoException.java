package excepciones;

/**
 * Excepci�n lanzada al introducir unos apellidos con un formato incorrecto.
 */
public class ApellidoIncorrectoException extends Exception {

	private static final long serialVersionUID = -6784531025406118455L;

	public ApellidoIncorrectoException() {
		super("Los apellidos s�lo pueden contener caracteres alfab�ticos, espacios y guiones");
	}
	
	public ApellidoIncorrectoException(String message) {
		super(message);
	}

}
