package excepciones;

/**
 * Excepción lanzada cuando se intentan recuperar los datos de un
 * centro de salud no registrado en el sistema.
 */
public class CentroSaludInexistenteException extends Exception {

	private static final long serialVersionUID = 1470651576890527816L;

	public CentroSaludInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
