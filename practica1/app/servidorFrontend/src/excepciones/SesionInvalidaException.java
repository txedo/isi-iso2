package excepciones;

public class SesionInvalidaException extends Exception {
	
	private static final long serialVersionUID = 8719135723033800765L;

	public SesionInvalidaException(String mensaje) {
		super(mensaje);
	}
	
}
