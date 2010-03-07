package excepciones;

/**
 * Excepción lanzada al introducir un identificador de volante con un formato
 * incorrecto.
 */
public class IdVolanteIncorrectoException extends Exception {

	private static final long serialVersionUID = -2759433347450120076L;

	public IdVolanteIncorrectoException(String mensaje) {
		super(mensaje);
	}
	
}
