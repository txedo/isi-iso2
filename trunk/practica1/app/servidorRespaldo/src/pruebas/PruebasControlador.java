package pruebas;

import dominio.control.ControladorRespaldo;
import junit.framework.TestCase;

/**
 * Pruebas del controlador principal del servidor de respaldo.
 */
public class PruebasControlador extends TestCase {

	protected void setUp() {
		// No se necesita código de inicialización
	}
	
	protected void tearDown() {
		// No se necesita código de finalización 
	}
	
	public void testConectarDesconectar() {
		ControladorRespaldo controlador = null;
		
		try {
			// Inicializamos el controlador y la ventana de estado
			controlador = new ControladorRespaldo();
			controlador.mostrarVentana();
			controlador.ocultarVentana();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Activamos el servidor varias veces para ver si no hay fallos
			controlador.iniciarServidorRespaldo("127.0.0.1");
			controlador.iniciarServidorRespaldo("127.0.0.1");
			assertTrue(controlador.getServidorActivo());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Desactivamos el servidor dos veces para ver si no hay fallos
			controlador.detenerServidorRespaldo();
			controlador.detenerServidorRespaldo();
			assertFalse(controlador.getServidorActivo());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
