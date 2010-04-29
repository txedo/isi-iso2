package excepciones;

/**
 * Excepción lanzada al introducir un número de teléfono fijo con
 * un formato incorrecto. 
 */
public class TelefonoFijoIncorrectoException extends Exception {

	private static final long serialVersionUID = -5220929421406336619L;

	public TelefonoFijoIncorrectoException() {
		super("El teléfono fijo deben ser 9 digitos sin separadores y debe comenzar por 9.");
	}
	
	public TelefonoFijoIncorrectoException(String message) {
		super(message);
	}
}
