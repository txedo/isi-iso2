package excepciones;

/**
 * Excepción interna lanzada cuando un campo (por ejemplo, una ciudad o
 * un nombre) tiene caracteres inválidos.
 */
public class CadenaIncorrectaException extends Exception {

	private static final long serialVersionUID = -2788372165137489221L;

	public CadenaIncorrectaException() {
		super("La cadena tiene caracteres inválidos.");
	}
	
}
