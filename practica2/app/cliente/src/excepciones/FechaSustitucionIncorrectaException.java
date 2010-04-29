package excepciones;

/**
 * Excepci�n lanzada al seleccionar una fecha no v�lida para una sustituci�n.
 */
public class FechaSustitucionIncorrectaException extends Exception {

	private static final long serialVersionUID = 4458443234066061738L;

	public FechaSustitucionIncorrectaException() {
		super("La fecha de la sustituci�n no es v�lida.");
	}
	
	public FechaSustitucionIncorrectaException(String mensaje) {
		super(mensaje);
	}
	
}
