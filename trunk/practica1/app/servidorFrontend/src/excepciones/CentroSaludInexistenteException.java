package excepciones;

public class CentroSaludInexistenteException extends Exception {

	private static final long serialVersionUID = 1470651576890527816L;

	public CentroSaludInexistenteException(String mensaje) {
		super(mensaje);
	}
	
}
