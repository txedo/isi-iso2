package excepciones;

/**
 * Excepción lanzada cuando se intenta buscar o asignar un sustituto
 * para un día en el que el médico no puede ser sustituido o no 
 * puede hacer la sustitución.
 */
public class SustitucionInvalidaException extends Exception {

	private static final long serialVersionUID = -8858489323231471145L;

	public SustitucionInvalidaException(String mensaje) {
		super(mensaje);
	}
	
}
