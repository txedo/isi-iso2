package excepciones;

import presentacion.auxiliar.Validacion;

/**
 * Excepci�n interna lanzada cuando un campo num�rico tiene m�s de 15
 * caracteres.
 */
public class EnteroLongitudMaximaException extends Exception {

	private static final long serialVersionUID = 5734496450550138365L;

	public EnteroLongitudMaximaException() {
		super("El n�mero supera la longitud m�xima permitida de " + Validacion.MAX_LONGITUD_CAMPOS + " caracteres.");
	}
	
}
