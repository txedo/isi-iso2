package excepciones;

/**
 * Excepción lanzada al introducir una dirección IP con un formato
 * inválido o fuera del rango 0.0.0.0 - 255.255.255.255.
 */
public class IPInvalidaException extends Exception {

	private static final long serialVersionUID = -1166896376103239365L;

	public IPInvalidaException() {
		super("El formato de la dirección IP no es válido.");
	}
	
	public IPInvalidaException(String message) {
		super(message);
	}
	
}
