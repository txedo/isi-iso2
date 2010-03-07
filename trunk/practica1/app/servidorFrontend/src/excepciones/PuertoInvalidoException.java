package excepciones;

import presentacion.auxiliar.Validacion;

/**
 * Excepci�n lanzada al introducir una direcci�n IP con un formato
 * inv�lido o fuera del rango 0.0.0.0 - 255.255.255.255.
 */
public class PuertoInvalidoException extends Exception {

	private static final long serialVersionUID = 2579891266483892738L;

	public PuertoInvalidoException() {
		super("El puerto debe ser un n�mero entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
	}
	
	public PuertoInvalidoException(String message) {
		super(message);
	}
	
}
