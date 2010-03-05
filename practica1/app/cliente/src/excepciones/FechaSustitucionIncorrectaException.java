package excepciones;

public class FechaSustitucionIncorrectaException extends Exception {

	private static final long serialVersionUID = 4458443234066061738L;

	public FechaSustitucionIncorrectaException() {
		super("La fecha de la sustitución no es válida.");
	}
	
	public FechaSustitucionIncorrectaException(String mensaje) {
		super(mensaje);
	}
}
