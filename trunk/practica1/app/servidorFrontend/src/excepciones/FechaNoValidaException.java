package excepciones;

/**
 * Excepción lanzada cuando se intenta pedir una cita o asignar una
 * sustitución para una fecha no permitida (por ejemplo, anterior al
 * día actual o en una hora en la que el médico no trabaja).
 */
public class FechaNoValidaException extends Exception {
	
	private static final long serialVersionUID = 737603991947608394L;

	public FechaNoValidaException(String mensaje) {
		super(mensaje);
	}

}
