package excepciones;

/**
 * Excepción lanzada al introducir una provincia con un formato incorrecto. 
 */
public class ProvinciaIncorrectaException extends Exception{

	private static final long serialVersionUID = -564150211980968071L;

	public ProvinciaIncorrectaException() {
		super("La provincia sólo puede contener carácteres alfabéticos, espacios y guiones");
	}
	
	public ProvinciaIncorrectaException(String message) {
		super(message);
	}
	
}
