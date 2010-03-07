package excepciones;

/**
 * Excepci�n lanzada cuando se intentan recuperar los datos de una
 * direcci�n de beneficiario no registrada.
 */
public class DireccionInexistenteException extends Exception {

	private static final long serialVersionUID = -1389384239724340389L;

	public DireccionInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
