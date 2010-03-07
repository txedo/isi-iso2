package excepciones;

/**
 * Excepción lanzada al introducir un número de domicilio con un
 * formato incorrecto. 
 */
public class NumeroDomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = 6544931523790937143L;

	public NumeroDomicilioIncorrectoException () {
		super("El número del domicilio debe ser un número entero, acabado, opcionalmente, en una letra.");
	}
	
	public NumeroDomicilioIncorrectoException(String mensaje) {
		super(mensaje);
	}
	
}
