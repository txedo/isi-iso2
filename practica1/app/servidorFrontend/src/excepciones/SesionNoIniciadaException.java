package excepciones;

/**
 * Excepción lanzada cuando un usuario que no ha iniciado sesión intenta
 * registrarse en el servidor.
 */
public class SesionNoIniciadaException extends Exception{

	private static final long serialVersionUID = 3223159360978439291L;

	public SesionNoIniciadaException (String mensaje) {
		super(mensaje);
	}
	
}
