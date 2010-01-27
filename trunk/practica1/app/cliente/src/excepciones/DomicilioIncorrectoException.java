package excepciones;

public class DomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = -3140385747445013562L;

	public DomicilioIncorrectoException () {
		super("El domicilio sólo puede contener carácteres alfanuméricos.");
	}
	
	public DomicilioIncorrectoException (String message) {
		super(message);
	}
}
