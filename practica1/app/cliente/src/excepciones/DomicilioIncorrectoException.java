package excepciones;

public class DomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = -3140385747445013562L;

	public DomicilioIncorrectoException () {
		super("El domicilio s�lo puede contener car�cteres alfanum�ricos.");
	}
	
	public DomicilioIncorrectoException (String message) {
		super(message);
	}
}
