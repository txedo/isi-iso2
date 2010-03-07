package excepciones;

/**
 * Excepción lanzada al introducir una localidad de un domicilio con
 * un formato incorrecto. 
 */
public class LocalidadIncorrectaException extends Exception{

	private static final long serialVersionUID = 8874972970802361794L;

	public LocalidadIncorrectaException () {
		super("La ciudad sólo puede contener carácteres alfabéticos, espacios y guiones.");
	}
	
}
