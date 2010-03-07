package excepciones;

/**
 * Excepción lanzada cuando un usuario intenta realizar una operación
 * para la que no tiene permisos suficientes.
 */
public class OperacionIncorrectaException extends Exception {

	private static final long serialVersionUID = 2316421371945471526L;

	public OperacionIncorrectaException (String mensaje) {
		super(mensaje);
	}
	
}
