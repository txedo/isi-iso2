package excepciones;

public class HoraSustitucionIncorrectaException extends Exception {

	private static final long serialVersionUID = 4458443234066061738L;

	public HoraSustitucionIncorrectaException() {
		super("La hora inicial y final de la sustitución no es válida.");
	}
	
	public HoraSustitucionIncorrectaException(String mensaje) {
		super(mensaje);
	}
	
}
