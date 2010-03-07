package excepciones;

/**
 * Excepción lanzada al intentar crear un usuario sin seleccionar su tipo.
 */
public class UsuarioNoSeleccionadoException extends Exception {

	private static final long serialVersionUID = 8060300622529824816L;

	public UsuarioNoSeleccionadoException(String mensaje) {
		super(mensaje);
	}
	
}
