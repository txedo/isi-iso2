package excepciones;

public class CiudadIncorrectaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874972970802361794L;

	public CiudadIncorrectaException () {
		super("La ciudad sólo puede contener carácteres alfabéticos y espacios.");
	}
}
