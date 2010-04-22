package excepciones;

/**
 * Excepción lanzada cuando se intenta emitir o utilizar un volante
 * no válido (por ejemplo, porque el médico receptor no es especialista).
 */
public class VolanteNoValidoException extends Exception {

	private static final long serialVersionUID = -3983954310982104431L;

	public VolanteNoValidoException(String mensaje){
		super(mensaje);
	}

}
