package excepciones;

/**
 * Excepci�n lanzada al introducir un c�digo postal que no tenga 5 d�gitos.
 */
public class CodigoPostalIncorrectoException extends Exception {
	
	private static final long serialVersionUID = 8970392685053485470L;

	public CodigoPostalIncorrectoException() {
		super("El c�sigo postal debe tener 5 d�gitos.");
	}
	
}
