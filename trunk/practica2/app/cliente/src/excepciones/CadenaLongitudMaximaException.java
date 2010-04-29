package excepciones;

import presentacion.auxiliar.Validacion;

/**
 * Excepci�n interna lanzada cuando un campo (por ejemplo, una ciudad o
 * un nombre) tiene m�s de 255 caracteres.
 */
public class CadenaLongitudMaximaException extends Exception {

	private static final long serialVersionUID = -2788372165137489221L;

	public CadenaLongitudMaximaException() {
		super("La cadena supera la longitud m�xima permitida de " + Validacion.MAX_LONGITUD_CAMPOS + " caracteres.");
	}
	
}
