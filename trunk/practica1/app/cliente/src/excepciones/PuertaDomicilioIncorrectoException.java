package excepciones;

public class PuertaDomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = 7168436723629077338L;

	public PuertaDomicilioIncorrectoException() {
		super("");
	}
	
	public PuertaDomicilioIncorrectoException(String mensaje) {
		super(mensaje);
	}
	
}
