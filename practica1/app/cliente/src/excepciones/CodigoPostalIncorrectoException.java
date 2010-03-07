package excepciones;

/**
 * Excepción lanzada al introducir un código postal que no tenga 5 dígitos.
 */
public class CodigoPostalIncorrectoException extends Exception {
	
	private static final long serialVersionUID = 8970392685053485470L;

	public CodigoPostalIncorrectoException() {
		super("El cósigo postal debe tener 5 dígitos.");
	}
	
}
