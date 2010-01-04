package excepciones;

public class BeneficiarioYaExistenteException extends Exception {

	private static final long serialVersionUID = 3667210159504703742L;

	public BeneficiarioYaExistenteException(String mensaje) {
		super(mensaje);
	}

}
