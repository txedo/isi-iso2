package excepciones;

public class FechaCitaIncorrectaException extends Exception {

	private static final long serialVersionUID = 4458443234066061738L;

	public FechaCitaIncorrectaException() {
		super("La fecha de la cita no es válida.");
	}
	
	public FechaCitaIncorrectaException(String mensaje) {
		super(mensaje);
	}
}
