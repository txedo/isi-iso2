package excepciones;

import presentacion.auxiliar.Validacion;

/**
 * Excepción interna lanzada cuando un campo numérico tiene más de 15
 * caracteres.
 */
public class EnteroLongitudMaximaException extends Exception {

	private static final long serialVersionUID = 5734496450550138365L;

	public EnteroLongitudMaximaException() {
		super("El número supera la longitud máxima permitida de " + Validacion.MAX_LONGITUD_CAMPOS + " caracteres.");
	}
	
}
