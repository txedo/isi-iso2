package excepciones;

/**
 * Excepción lanzada al introducir unas horas no válidas para una sustitución
 * (por ejemplo, porque la hora inicial es mayor que la final).
 */
public class HoraSustitucionIncorrectaException extends Exception {

	private static final long serialVersionUID = 4458443234066061738L;

	public HoraSustitucionIncorrectaException() {
		super("La hora inicial y final de la sustitución no es válida.");
	}
	
	public HoraSustitucionIncorrectaException(String mensaje) {
		super(mensaje);
	}
	
}
