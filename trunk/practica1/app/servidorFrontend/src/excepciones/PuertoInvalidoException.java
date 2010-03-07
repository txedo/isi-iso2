package excepciones;

import presentacion.auxiliar.Validacion;

/**
 * Excepción lanzada al introducir una dirección IP con un formato
 * inválido o fuera del rango 0.0.0.0 - 255.255.255.255.
 */
public class PuertoInvalidoException extends Exception {

	private static final long serialVersionUID = 2579891266483892738L;

	public PuertoInvalidoException() {
		super("El puerto debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
	}
	
	public PuertoInvalidoException(String message) {
		super(message);
	}
	
}
