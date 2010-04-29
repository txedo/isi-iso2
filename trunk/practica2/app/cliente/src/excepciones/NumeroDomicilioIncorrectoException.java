package excepciones;

/**
 * Excepci�n lanzada al introducir un n�mero de domicilio con un
 * formato incorrecto. 
 */
public class NumeroDomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = 6544931523790937143L;

	public NumeroDomicilioIncorrectoException () {
		super("El n�mero del domicilio debe ser un n�mero entero, acabado, opcionalmente, en una letra.");
	}
	
	public NumeroDomicilioIncorrectoException(String mensaje) {
		super(mensaje);
	}
	
}
