package pruebas;

import org.uispec4j.Button;
import org.uispec4j.MenuItem;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import dominio.control.ControladorRespaldo;
import presentacion.JFServidorRespaldo;

/**
 * Pruebas de la ventana principal del servidor de respaldo.
 */
public class PruebasJFServidorRespaldo extends org.uispec4j.UISpecTestCase {

	private JFServidorRespaldo ventana;
	private ControladorRespaldo controlador;
	private Window winVentana;
	private Button btnConectar;
	private Button btnDesconectar;
	private TextBox lblServidor;
	private MenuItem mniConfigurar;

	public void setUp() {
		try {
			controlador = new ControladorRespaldo();
			ventana = controlador.getVentana();
			winVentana = new Window(ventana);
			btnConectar = winVentana.getButton("btnConectar");
			btnDesconectar = winVentana.getButton("btnDesconectar");
			lblServidor = winVentana.getTextBox("lblBarraEstado");
			mniConfigurar = winVentana.getMenuBar().getMenu("Opciones").getSubMenu("Configurar...");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		try {
			// Liberamos los recursos usados por la ventana
			ventana.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testVentana() {
		try {
			// Activamos el servidor
			assertEquals(lblServidor.getText(), "Servidor desconectado.");
			btnConectar.click();
			assertEquals(lblServidor.getText(), "Servidor preparado.");
			// Desactivamos el servidor
			btnDesconectar.click();
			assertEquals(lblServidor.getText(), "Servidor desconectado.");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testMenus() {
		Window dialogo;
		
		try {
			// Abrimos la ventana de configuración
			dialogo = WindowInterceptor.run(new Trigger() {
				public void run() {
					mniConfigurar.click();
				}
			});
			dialogo.getButton("btnAceptar").click();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
