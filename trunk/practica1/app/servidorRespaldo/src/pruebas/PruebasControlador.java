package pruebas;

import java.rmi.RemoteException;
import org.uispec4j.Trigger;
import org.uispec4j.interception.WindowInterceptor;

import comunicaciones.ConfiguracionRespaldo;

import dominio.control.ControladorRespaldo;
import junit.framework.TestCase;

/**
 * Pruebas del controlador principal del servidor de respaldo.
 */
public class PruebasControlador extends TestCase {

	private ControladorRespaldo controlador;
	
	public void setUp() {
		try {
			// Inicializamos el controlador y la ventana de estado
			controlador = new ControladorRespaldo();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		// No se necesita código de finalización 
	}
	
	/** Pruebas del controlador */
	public void testControlador() {
		ConfiguracionRespaldo configuracion = null;
		
		try {
			// Mostramos y ocultamos la ventana principal
			WindowInterceptor.run(new Trigger() {
				public void run() {
					controlador.mostrarVentana();
				}
			});
			assertTrue(controlador.getVentana().isVisible());
			controlador.ocultarVentana();
			assertFalse(controlador.getVentana().isVisible());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Activamos el servidor varias veces para ver si no hay fallos
			configuracion = new ConfiguracionRespaldo("127.0.0.1", 3306, 1098);
			controlador.iniciarServidorRespaldo(configuracion);
			controlador.iniciarServidorRespaldo(configuracion);
			assertTrue(controlador.isServidorActivo());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos activar el servidor en un puerto diferente
			configuracion = new ConfiguracionRespaldo("127.0.0.1", 3306, 1298);
			controlador.iniciarServidorRespaldo(configuracion);
			fail("Se esperaba una RemoteException");
		} catch(RemoteException e) {
		} catch(Exception e) {
			fail("Se esperaba una RemoteException");
		}
		
		try {
			// Desactivamos el servidor dos veces para ver si no hay fallos
			configuracion = new ConfiguracionRespaldo("127.0.0.1", 3306, 1098);
			controlador.detenerServidorRespaldo(configuracion);
			controlador.detenerServidorRespaldo(configuracion);
			assertFalse(controlador.isServidorActivo());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
