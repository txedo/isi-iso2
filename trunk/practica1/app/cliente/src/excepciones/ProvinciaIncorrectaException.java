package excepciones;

public class ProvinciaIncorrectaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -564150211980968071L;

	public ProvinciaIncorrectaException () {
		super("La provincia s�lo puede contener car�cteres alfab�ticos y espacios.");
	}
}

