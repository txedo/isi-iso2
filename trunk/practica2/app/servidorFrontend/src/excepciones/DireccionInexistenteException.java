package excepciones;

/**
 * Excepción lanzada cuando se intentan recuperar los datos de una
 * dirección de beneficiario no registrada.
 */
public class DireccionInexistenteException extends Exception {

	private static final long serialVersionUID = -1389384239724340389L;

	public DireccionInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
