package excepciones;

/**
 * Excepci�n interna lanzada cuando un campo num�rico tiene caracteres no
 * v�lidos o contiene un n�mero negativo.
 */
public class EnteroIncorrectoException extends Exception {

	private static final long serialVersionUID = -4573938396507767050L;

	public EnteroIncorrectoException () {
		super("El n�mero debe ser un entero positivo.");
	}
	
	public EnteroIncorrectoException(String message) {
		super(message);
	}
	
}
