package excepciones;

public class VolanteNoValidoException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3983954310982104431L;

	public VolanteNoValidoException(String mensaje){
		super(mensaje);
	}

}
