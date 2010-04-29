package excepciones;

/**
 * Excepción lanzada al introducir una puerta de domicilio con un
 * formato incorrecto. 
 */
public class PuertaDomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = 7168436723629077338L;

	public PuertaDomicilioIncorrectoException() {
		super("La puerta del domicilio debe ser un carácter alfabético.");
	}
	
	public PuertaDomicilioIncorrectoException(String mensaje) {
		super(mensaje);
	}
	
}
