package excepciones;

public class IPInvalidaException extends Exception {

	private static final long serialVersionUID = -1166896376103239365L;

	public IPInvalidaException() {
		super("El formato de la direcci�n IP no es v�lido.");
	}
	
	public IPInvalidaException(String message) {
		super(message);
	}
	
}
