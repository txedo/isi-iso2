package excepciones;

public class NombreIncorrectoException extends Exception {

	private static final long serialVersionUID = -8853095774781902819L;
	
	public NombreIncorrectoException () {
		super("El nombre s�lo puede contener car�cteres alfab�ticos y espacios.");
	}

}
