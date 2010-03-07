package excepciones;

/**
 * Excepción lanzada al introducir un nombre con un formato incorrecto. 
 */
public class NombreIncorrectoException extends Exception {

	private static final long serialVersionUID = -8853095774781902819L;
	
	public NombreIncorrectoException () {
		super("El nombre sólo puede contener carácteres alfabéticos, espacios y guiones");
	}

}
