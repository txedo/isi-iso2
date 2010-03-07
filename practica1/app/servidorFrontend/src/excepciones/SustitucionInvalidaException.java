package excepciones;

/**
 * Excepci�n lanzada cuando se intenta buscar o asignar un sustituto
 * para un d�a en el que el m�dico no puede ser sustituido o no 
 * puede hacer la sustituci�n.
 */
public class SustitucionInvalidaException extends Exception {

	private static final long serialVersionUID = -8858489323231471145L;

	public SustitucionInvalidaException(String mensaje) {
		super(mensaje);
	}
	
}
