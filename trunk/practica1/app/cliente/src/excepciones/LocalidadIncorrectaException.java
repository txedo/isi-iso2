package excepciones;

public class LocalidadIncorrectaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874972970802361794L;

	public LocalidadIncorrectaException () {
		super("La ciudad s�lo puede contener car�cteres alfab�ticos, espacios y guiones.");
	}
}
