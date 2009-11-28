package excepciones;

public class MedicoInexistenteException extends Exception {

	private static final long serialVersionUID = 96373096628265776L;

	public MedicoInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
