package excepciones;

public class CitaNoValidaException extends Exception {

	private static final long serialVersionUID = -1905894381276083689L;

	public CitaNoValidaException(String mensaje) {
		super(mensaje);
	}
	
}
