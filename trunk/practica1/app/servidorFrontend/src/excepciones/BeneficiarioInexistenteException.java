package excepciones;

public class BeneficiarioInexistenteException extends Exception {

	private static final long serialVersionUID = -6210791188147652076L;

	public BeneficiarioInexistenteException() {
		super("El beneficiario no se encuentra dado de alta en el sistema.");
	}
	public BeneficiarioInexistenteException(String mensaje) {
		super(mensaje);
	}
}
