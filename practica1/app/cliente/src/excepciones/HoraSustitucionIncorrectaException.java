package excepciones;

public class HoraSustitucionIncorrectaException extends Exception {

	private static final long serialVersionUID = 4458443234066061738L;

	public HoraSustitucionIncorrectaException() {
		super("La hora inicial y final de la sustituci�n no es v�lida.");
	}
	
	public HoraSustitucionIncorrectaException(String mensaje) {
		super(mensaje);
	}
	
}
