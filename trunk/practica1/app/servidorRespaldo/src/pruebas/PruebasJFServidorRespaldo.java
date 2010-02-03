package pruebas;

import org.uispec4j.Button;
import org.uispec4j.MenuItem;
import org.uispec4j.TextBox;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;
import dominio.conocimiento.ConfiguracionRespaldo;
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
	private TextBox lblBarraEstado;
	private TextBox lblConfigBD;
	private MenuItem mniConectar;
	private MenuItem mniDesconectar;
	private MenuItem mniConfigurar;

	public void setUp() {
		try {
			// Creamos el controlador y con él la ventana de estado
			controlador = new ControladorRespaldo();
			ventana = controlador.getVentana();
			// Obtenemos los componentes de la ventana
			winVentana = new Window(ventana);
			btnConectar = winVentana.getButton("btnConectar");
			btnDesconectar = winVentana.getButton("btnDesconectar");
			lblBarraEstado = winVentana.getTextBox("lblBarraEstado");
			lblConfigBD = winVentana.getTextBox("lblConfigBD");
			mniConectar = winVentana.getMenuBar().getMenu("Archivo").getSubMenu("Conectar");
			mniDesconectar = winVentana.getMenuBar().getMenu("Archivo").getSubMenu("Desconectar");
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
	
	/** Pruebas de las acciones de conectar y desconectar */
	public void testConectarDesconectar() {
		int puerto = 0;
		
		try {
			// Obtenemos el puerto de escucha predeterminado
			puerto = (new ConfiguracionRespaldo()).getPuertoRespaldo();
			// Comprobamos que el servidor está desactivado
			assertEquals(lblBarraEstado.getText(), "Servidor desconectado (puerto " + String.valueOf(puerto) + ").");
			assertFalse(controlador.isServidorActivo());
			assertTrue(btnConectar.isEnabled());
			assertTrue(mniConectar.isEnabled());
			assertTrue(mniConfigurar.isEnabled());
			assertFalse(btnDesconectar.isEnabled());
			assertFalse(mniDesconectar.isEnabled());
			// Activamos el servidor
			btnConectar.click();
			assertEquals(lblBarraEstado.getText(), "Servidor preparado (puerto " + String.valueOf(puerto) + ").");
			assertTrue(controlador.isServidorActivo());
			assertFalse(btnConectar.isEnabled());
			assertFalse(mniConectar.isEnabled());
			assertFalse(mniConfigurar.isEnabled());
			assertTrue(btnDesconectar.isEnabled());
			assertTrue(mniDesconectar.isEnabled());
			// Desactivamos el servidor
			btnDesconectar.click();
			assertEquals(lblBarraEstado.getText(), "Servidor desconectado (puerto " + String.valueOf(puerto) + ").");
			assertFalse(controlador.isServidorActivo());
			assertTrue(btnConectar.isEnabled());
			assertTrue(mniConectar.isEnabled());
			assertTrue(mniConfigurar.isEnabled());
			assertFalse(btnDesconectar.isEnabled());
			assertFalse(mniDesconectar.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Activamos el servidor por menú
			mniConectar.click();
			assertTrue(controlador.isServidorActivo());
			assertFalse(btnConectar.isEnabled());
			assertFalse(mniConectar.isEnabled());
			assertFalse(mniConfigurar.isEnabled());
			assertTrue(btnDesconectar.isEnabled());
			assertTrue(mniDesconectar.isEnabled());
			// Desactivamos el servidor por menú
			mniDesconectar.click();
			assertFalse(controlador.isServidorActivo());
			assertTrue(btnConectar.isEnabled());
			assertTrue(mniConectar.isEnabled());
			assertTrue(mniConfigurar.isEnabled());
			assertFalse(btnDesconectar.isEnabled());
			assertFalse(mniDesconectar.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la ventana de configuración */
	public void testConfigurar() {
		Window dialogo;
		String ip;
		
		try {
			// Abrimos la ventana de configuración y cambiamos un puerto
			dialogo = WindowInterceptor.run(mniConfigurar.triggerClick());
			dialogo.getTextBox("txtPuertoBDRespaldo").setText("8888");
			dialogo.getButton("btnAceptar").click();
			// Comprobamos que el cambio se ha reflejado en la ventana
			ip = (new ConfiguracionRespaldo()).getIPBDRespaldo();
			assertEquals(lblConfigBD.getText(), "BD Respaldo: IP " + ip + ", puerto 8888");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
