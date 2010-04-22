package excepciones;

/**
 * Excepci�n lanzada cuando se intenta pedir una cita o asignar una
 * sustituci�n para una fecha no permitida (por ejemplo, anterior al
 * d�a actual o en una hora en la que el m�dico no trabaja).
 */
public class FechaNoValidaException extends Exception {
	
	private static final long serialVersionUID = 737603991947608394L;

	public FechaNoValidaException(String mensaje) {
		super(mensaje);
	}

}
