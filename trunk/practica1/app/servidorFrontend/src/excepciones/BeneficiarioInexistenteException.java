package excepciones;

/**
 * Excepción lanzada cuando se intentan recuperar los datos de un
 * beneficiario no registrado en el sistema.
 */
public class BeneficiarioInexistenteException extends Exception {

	private static final long serialVersionUID = -6210791188147652076L;

	public BeneficiarioInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
