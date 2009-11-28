package excepciones;

public class SesionNoIniciadaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3223159360978439291L;

	public SesionNoIniciadaException (String mensaje) {
		super(mensaje);
	}
}
