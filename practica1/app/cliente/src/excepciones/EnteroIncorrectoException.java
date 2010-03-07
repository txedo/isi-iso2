package excepciones;

/**
 * Excepción interna lanzada cuando un campo numérico tiene caracteres no
 * válidos o contiene un número negativo.
 */
public class EnteroIncorrectoException extends Exception {

	private static final long serialVersionUID = -4573938396507767050L;

	public EnteroIncorrectoException () {
		super("El número debe ser un entero positivo.");
	}
	
	public EnteroIncorrectoException(String message) {
		super(message);
	}
	
}
