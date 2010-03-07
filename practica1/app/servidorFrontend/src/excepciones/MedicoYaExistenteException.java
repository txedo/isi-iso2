package excepciones;

/**
 * Excepción lanzada cuando se intenta crear un nuevo médico con un
 * NIF que ya tiene otra persona registrada en el sistema.
 */
public class MedicoYaExistenteException extends Exception {

	private static final long serialVersionUID = 1260058175372089209L;

	public MedicoYaExistenteException(String mensaje) {
		super(mensaje);
	}
	
}
