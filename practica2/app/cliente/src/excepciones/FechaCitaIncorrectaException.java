package excepciones;

/**
 * Excepci�n lanzada al seleccionar una fecha no v�lida para una cita.
 */
public class FechaCitaIncorrectaException extends Exception {

	private static final long serialVersionUID = 4458443234066061738L;

	public FechaCitaIncorrectaException() {
		super("La fecha de la cita no es v�lida.");
	}
	
	public FechaCitaIncorrectaException(String mensaje) {
		super(mensaje);
	}
}
