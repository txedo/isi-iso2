package excepciones;

public class MedicoYaExistenteException extends Exception {

	private static final long serialVersionUID = 1260058175372089209L;

	public MedicoYaExistenteException(String mensaje) {
		super(mensaje);
	}
	
}
