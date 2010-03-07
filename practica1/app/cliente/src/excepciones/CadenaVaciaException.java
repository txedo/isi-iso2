package excepciones;

/**
 * Excepción interna lanzada cuando un campo obligatorio no tiene valor.
 */
public class CadenaVaciaException extends Exception {

	private static final long serialVersionUID = 3916299026782481780L;

	public CadenaVaciaException() {
		super("La cadena no puede ser nula.");
	}
	
	public CadenaVaciaException(String message) {
		super(message);
	}
}
