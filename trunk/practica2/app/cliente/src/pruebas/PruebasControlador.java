package pruebas;

import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.WindowInterceptor;


import dominio.Main;

public class PruebasControlador extends UISpecTestCase implements IDatosConexionPruebas {
	
	/** Pruebas de la clase Main */
	public void testMain() {
		Window ventana;
		
		try {
			// Comprobamos que el método Main muestra la ventana principal del servidor
			ventana = WindowInterceptor.run(new Trigger() {
				public void run() {
					Main.main(new String[] {});
				}
			});
			assertEquals(ventana.getTitle(), "Inicio de sesión");
			ventana.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
}
