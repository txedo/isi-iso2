package excepciones;

/**
 * Excepci�n lanzada al introducir una fecha con un formato incorrecto.
 */
public class FormatoFechaIncorrectoException extends Exception {

	private static final long serialVersionUID = 5844092287426882038L;

	public FormatoFechaIncorrectoException() {
		super("El formato de la fecha no es v�lido. Debe tener el formato dd/MM/yyyy.");
	}
	
	public FormatoFechaIncorrectoException(String message) {
		super(message);
	}

}
