package excepciones;

/**
 * Excepción lanzada cuando se intentan recuperar los datos de una
 * cita que no existe en el sistema.
 */
public class CitaNoValidaException extends Exception {

	private static final long serialVersionUID = -1905894381276083689L;

	public CitaNoValidaException(String mensaje) {
		super(mensaje);
	}
	
}
