package excepciones;

/**
 * Excepción lanzada cuando se intenta crear un nuevo beneficiario con
 * un NIF o un NSS que ya tiene otra persona registrada en el sistema.
 */
public class BeneficiarioYaExistenteException extends Exception {

	private static final long serialVersionUID = 3667210159504703742L;

	public BeneficiarioYaExistenteException(String mensaje) {
		super(mensaje);
	}

}
