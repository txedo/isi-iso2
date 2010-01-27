package excepciones;

public class NumeroDomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = 6544931523790937143L;

	public NumeroDomicilioIncorrectoException () {
		super("El número del domicilio debe ser un número entero.");
	}
}
