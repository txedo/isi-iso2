package pruebas;

import org.uispec4j.Button;
import org.uispec4j.MenuItem;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import comunicaciones.ConfiguracionFrontend;
import comunicaciones.UtilidadesComunicaciones;
import dominio.control.ControladorFrontend;
import presentacion.JFServidorFrontend;

/**
 * Pruebas de la ventana principal del servidor front-end.
 */
public class PruebasJFServidorFrontend extends org.uispec4j.UISpecTestCase {

	private JFServidorFrontend ventana;
	private ControladorFrontend controlador;
	private Window winVentana;
	private Button btnConectar;
	private Button btnDesconectar;
	private Button btnSalir;
	private TextBox lblBarraEstado;
	private TextBox lblConfigBD;
	private MenuItem mniConectar;
	private MenuItem mniDesconectar;
	private MenuItem mniConfigurar;
	private MenuItem mniAcercaDe;
	private MenuItem mniSalir;

	public void setUp() {
		try {
			// Creamos el controlador y con �l la ventana de estado
			controlador = new ControladorFrontend();
			ventana = controlador.getVentana();
			// Obtenemos los componentes de la ventana
			winVentana = new Window(ventana);
			btnConectar = winVentana.getButton("btnConectar");
			btnDesconectar = winVentana.getButton("btnDesconectar");
			btnSalir = winVentana.getButton("btnSalir");
			lblBarraEstado = winVentana.getTextBox("lblBarraEstado");
			lblConfigBD = winVentana.getTextBox("lblConfigBD");
			mniConectar = winVentana.getMenuBar().getMenu("Archivo").getSubMenu("Conectar");
			mniDesconectar = winVentana.getMenuBar().getMenu("Archivo").getSubMenu("Desconectar");
			mniSalir = winVentana.getMenuBar().getMenu("Archivo").getSubMenu("Salir");
			mniAcercaDe = winVentana.getMenuBar().getMenu("Ayuda").getSubMenu("Acerca de...");
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
			puerto = (new ConfiguracionFrontend()).getPuertoFrontend();
			// Comprobamos que el servidor est� desactivado
			assertEquals(lblBarraEstado.getText(), "Servidor desconectado (puerto " + String.valueOf(puerto) + ").");
			assertFalse(controlador.isServidorActivo());
			assertTrue(btnConectar.isEnabled());
			assertTrue(mniConectar.isEnabled());
			assertTrue(mniConfigurar.isEnabled());
			assertFalse(btnDesconectar.isEnabled());
			assertFalse(mniDesconectar.isEnabled());
			// Activamos el servidor
			btnConectar.click();
			assertEquals(lblBarraEstado.getText(), "Servidor preparado en " + UtilidadesComunicaciones.obtenerIPHost() + " (puerto " + String.valueOf(puerto) + ").");
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
			// Activamos el servidor por men�
			mniConectar.click();
			assertTrue(controlador.isServidorActivo());
			assertFalse(btnConectar.isEnabled());
			assertFalse(mniConectar.isEnabled());
			assertFalse(mniConfigurar.isEnabled());
			assertTrue(btnDesconectar.isEnabled());
			assertTrue(mniDesconectar.isEnabled());
			// Desactivamos el servidor por men�
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

		try {
			// Activamos el servidor e intentamos salir pero
			// cancelamos el cuadro de di�logo de confirmaci�n
			mniConectar.click();
			WindowInterceptor.init(btnSalir.triggerClick())
		    .process(new WindowHandler() {
		    	public Trigger process(Window window) {
		    		return window.getButton("No").triggerClick();
		    	}
		    }).run();
			// Comprobamos que el servidor sigue activo
			assertTrue(controlador.isServidorActivo());
			// Intentamos salir por men�
			WindowInterceptor.init(mniSalir.triggerClick())
		    .process(new WindowHandler() {
		    	public Trigger process(Window window) {
		    		return window.getButton("No").triggerClick();
		    	}
		    }).run();
			// Comprobamos que el servidor sigue activo
			assertTrue(controlador.isServidorActivo());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la ventana de configuraci�n */
	public void testConfigurar() {
		Window dialogo;
		String ip;
		
		try {
			// Abrimos la ventana de configuraci�n y cambiamos un puerto
			dialogo = WindowInterceptor.run(mniConfigurar.triggerClick());
			dialogo.getTextBox("txtPuertoBDPrincipal").setText("8888");
			dialogo.getButton("btnAceptar").click();
			// Comprobamos que el cambio se ha reflejado en la ventana
			ip = (new ConfiguracionFrontend()).getIPBDPrincipal();
			assertEquals(lblConfigBD.getText(), "BD Principal: IP " + ip + ", puerto 8888");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la ventana 'Acerca de' */
	public void testAcercaDe() {
		Window dialogo;
		
		try {
			// Abrimos la ventana de 'Acerca de' y la cerramos
			dialogo = WindowInterceptor.run(mniAcercaDe.triggerClick());
			dialogo.getButton("btnAceptar").click();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
