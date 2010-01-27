package excepciones;

public class PisoDomicilioIncorrectoException extends Exception {

	private static final long serialVersionUID = -4590787997375568298L;

	public PisoDomicilioIncorrectoException() {
		super("El piso del domicilio debe ser un número entero.");
	}
}
