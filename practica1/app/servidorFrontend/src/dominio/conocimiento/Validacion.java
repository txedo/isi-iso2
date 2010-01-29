package dominio.conocimiento;

import java.util.regex.Pattern;
import excepciones.CadenaVaciaException;
import excepciones.IPInvalidaException;
import excepciones.PuertoInvalidoException;

/**
 * Clase estática que contiene métodos para comprobar la validez
 * de los campos de las ventanas.
 */
public class Validacion {
	
	public static final int PUERTO_MINIMO = 1;
	public static final int PUERTO_MAXIMO = 65535;
	
	public static void comprobarDireccionIP(String ip) throws IPInvalidaException, CadenaVaciaException {
		Pattern patronIP;
		
		// Creamos un patrón que define las IPs válidas
		patronIP = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + 
				"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
		
		if(ip.equals("")) {
			throw new CadenaVaciaException("La dirección IP no puede ser nula.");
		} else if(!patronIP.matcher(ip).matches()) {
			throw new IPInvalidaException();
		}
	}
	
	public static void comprobarPuerto(int puerto) throws PuertoInvalidoException {
		if(puerto < PUERTO_MINIMO || puerto > PUERTO_MAXIMO) {
			throw new PuertoInvalidoException();
		}
	}
	
}
