package excepciones;

public class NSSIncorrectoException extends Exception {

	private static final long serialVersionUID = -4814368959418702471L;
	
	public NSSIncorrectoException () {
		super("El NSS (Numero de Seguridad Social) debe contener 12 dígitos.");
	}
	
}
