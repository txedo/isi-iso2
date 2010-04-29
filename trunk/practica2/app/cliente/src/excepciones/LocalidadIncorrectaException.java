package excepciones;

/**
 * Excepci�n lanzada al introducir una localidad de un domicilio con
 * un formato incorrecto. 
 */
public class LocalidadIncorrectaException extends Exception{

	private static final long serialVersionUID = 8874972970802361794L;

	public LocalidadIncorrectaException() {
		super("La localidad s�lo puede contener car�cteres alfab�ticos, espacios y guiones.");
	}

	public LocalidadIncorrectaException(String message) {
		super(message);
	}

}
