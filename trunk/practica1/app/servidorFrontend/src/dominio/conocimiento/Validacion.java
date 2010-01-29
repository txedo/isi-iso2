package dominio.conocimiento;

import java.util.regex.Pattern;
import excepciones.CadenaVaciaException;
import excepciones.IPInvalidaException;
import excepciones.PuertoInvalidoException;

/**
 * Clase est�tica que contiene m�todos para comprobar la validez
 * de los campos de las ventanas.
 */
public class Validacion {
	
	public static final int PUERTO_MINIMO = 1;
	public static final int PUERTO_MAXIMO = 65535;
	
	public static void comprobarDireccionIP(String ip) throws IPInvalidaException, CadenaVaciaException {
		Pattern patronIP;
		
		// Creamos un patr�n que define las IPs v�lidas
		patronIP = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + 
				"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
		
		if(ip.equals("")) {
			throw new CadenaVaciaException("La direcci�n IP no puede ser nula.");
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
