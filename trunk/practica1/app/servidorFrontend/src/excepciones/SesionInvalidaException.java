package excepciones;

/**
 * Excepci�n lanzada cuando un usuario que no ha iniciado sesi�n intenta
 * acceder al servidor front-end.
 */
public class SesionInvalidaException extends Exception {
	
	private static final long serialVersionUID = 8719135723033800765L;

	public SesionInvalidaException(String mensaje) {
		super(mensaje);
	}
	
}
