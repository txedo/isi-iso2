package excepciones;

public class NIFIncorrectoException extends Exception {

	private static final long serialVersionUID = 5189345327715115283L;
	
	public NIFIncorrectoException () {
		super("El NIF debe ser el número de DNI (incluyendo el 0) y la letra sin guión.");
	}
	
}
