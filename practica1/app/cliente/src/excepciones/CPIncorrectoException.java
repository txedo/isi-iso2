package excepciones;

public class CPIncorrectoException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8970392685053485470L;

	public CPIncorrectoException() {
		super("El cósigo postal debe tener 5 dígitos.");
	}
	
}
