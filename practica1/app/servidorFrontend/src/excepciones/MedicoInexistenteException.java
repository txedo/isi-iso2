package excepciones;

/**
 * Excepci�n lanzada cuando se intentan recuperar los datos de un
 * m�dico no registrado en el sistema.
 */
public class MedicoInexistenteException extends Exception {

	private static final long serialVersionUID = 96373096628265776L;

	public MedicoInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
