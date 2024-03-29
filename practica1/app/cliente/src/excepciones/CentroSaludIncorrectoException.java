package excepciones;

/**
 * Excepci�n lanzada al seleccionar un centro de salud inv�lido.
 */
public class CentroSaludIncorrectoException extends Exception {

	private static final long serialVersionUID = 3698362510608641580L;

	public CentroSaludIncorrectoException(String mensaje) {
		super(mensaje);
	}

	public CentroSaludIncorrectoException() {
		super("El centro de salud seleccionado no es v�lido.");
	}
		
}
