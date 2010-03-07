package excepciones;

/**
 * Excepción lanzada al introducir un NSS con un formato incorrecto. 
 */
public class NSSIncorrectoException extends Exception {

	private static final long serialVersionUID = -4814368959418702471L;
	
	public NSSIncorrectoException () {
		super("El NSS (Número de Seguridad Social) debe contener 12 dígitos.");
	}
	
}
