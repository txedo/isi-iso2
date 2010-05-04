package pruebas;

import presentacion.auxiliar.Validacion;
import excepciones.IPInvalidaException;
import excepciones.PuertoInvalidoException;
import junit.framework.TestCase;

/**
 * Pruebas de la clase encargada de comprobar la validez de los
 * campos introducidos por el usuario.
 */
public class PruebasValidacion extends TestCase {
	
	public void setUp() {
		// No se necesita c�digo de inicializaci�n
	}
	
	public void tearDown() {
		// No se necesita c�digo de finalizaci�n 
	}
	
	/** Pruebas de direcciones IP */
	public void testDireccionesIP() {
		String[] invalidos, validos;
		
		try {
			// Probamos IPs incorrectas
			invalidos = new String[] { "", "  ", "abc", "1234", "300.0.0.300", "128.0.0.256", "128.0.0.-1", "127.0.0.1  ", "  127.0.0.1" };
			for(String ip : invalidos) {
				try {
					Validacion.comprobarDireccionIP(ip);
					fail("La direcci�n IP '" + ip + "' deber�a ser inv�lida.");
				} catch(IPInvalidaException e) {
				}
			}
			// Probamos IPs correctas
			validos = new String[] { "127.0.0.1", "34.98.240.10", "0.0.0.0", "255.255.255.255" };
			for(String ip : validos) {
				try {
					Validacion.comprobarDireccionIP(ip);
				} catch(IPInvalidaException e) {
					fail("La direcci�n IP '" + ip + "' deber�a ser v�lida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de puertos */
	public void testPuertos() {
		String[] invalidos, validos;
		
		try {
			// Probamos IPs incorrectas
			invalidos = new String[] { "", "  ", "-1", "0", "abcd", "65536", "400x", "3,45", "1.000", "100000" };
			for(String puerto : invalidos) {
				try {
					Validacion.comprobarPuerto(puerto);
					fail("El puerto '" + puerto + "' deber�a ser inv�lido.");
				} catch(PuertoInvalidoException e) {
				}
			}
			// Probamos IPs correctas
			validos = new String[] { "1", "65535", "100", "1000", "00001000" };
			for(String puerto : validos) {
				try {
					Validacion.comprobarPuerto(puerto);
				} catch(PuertoInvalidoException e) {
					fail("El puerto '" + puerto + "' deber�a ser v�lido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
