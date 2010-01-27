package excepciones;

public class FormatoFechaIncorrectoException extends Exception {

	private static final long serialVersionUID = 5844092287426882038L;

	public FormatoFechaIncorrectoException() {
		super("El formato de la fecha no es válido. Debe seguir el formato dd/MM/yyyy.");
	}
	
	public FormatoFechaIncorrectoException(String message) {
		super(message);
	}

}
