package excepciones;

public class NumeroDomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = 6544931523790937143L;

	public NumeroDomicilioIncorrectoException () {
		super("El n�mero del domicilio debe ser un n�mero entero.");
	}
}
