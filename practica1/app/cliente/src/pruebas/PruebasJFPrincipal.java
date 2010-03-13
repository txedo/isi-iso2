package pruebas;

import org.uispec4j.Button;
import org.uispec4j.MenuItem;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import comunicaciones.ConfiguracionCliente;

import dominio.control.ControladorCliente;

public class PruebasJFPrincipal extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {
	private Window winPrincipal;
	private Window win;
	private ControladorCliente controlador;
	private MenuItem mniAcercaDe;
	private MenuItem mniSalir;
	private Button btnCerrarSesion;
	private Button btnCerrarAplicacion;
	private boolean terminado;
	
	public void setUp() {
		try {
			terminado = false;
			controlador = new ControladorCliente();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IP_SERVIDOR_FRONTEND, PUERTO_SERVIDOR_FRONTEND), USUARIO_ADMIN, PASSWORD_ADMIN);
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			// Obtenemos los componentes del panel
			win = new Window(controlador.getVentanaPrincipal());
			mniSalir = win.getMenuBar().getMenu("Archivo").getSubMenu("Salir");
			mniAcercaDe = win.getMenuBar().getMenu("Ayuda").getSubMenu("Acerca de...");
			btnCerrarSesion = win.getButton("btnCerrarSesion");
			btnCerrarAplicacion = win.getButton("btnCerrarAplicacion");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		try {
			if (!terminado) {
				controlador.getVentanaPrincipal().dispose();
				controlador.cerrarSesion();
				winPrincipal.dispose();
			}
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
			assertTrue(dialogo.isVisible());
			dialogo.getButton("btnAceptar").click();
			assertFalse(dialogo.isVisible());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testSalir() {
		// Intentamos salir por menú
		WindowInterceptor.init(mniSalir.triggerClick())
	    .process(new WindowHandler() {
	    	public Trigger process(Window window) {
	    		return window.getButton(NO_OPTION).triggerClick();
	    	}
	    }).run();
		assertTrue(controlador.isRegistrado());
	}
	
	public void testCerrarSesion () {
		WindowInterceptor.init(btnCerrarSesion.triggerClick())
	    .process(new WindowHandler() {
	    	public Trigger process(Window window) {
	    		return window.getButton(NO_OPTION).triggerClick();
	    	}
	    }).run();
		assertTrue(controlador.isRegistrado());
	}
	
	public void testCerrarAplicacion () {
		WindowInterceptor.init(btnCerrarAplicacion.triggerClick())
	    .process(new WindowHandler() {
	    	public Trigger process(Window window) {
	    		return window.getButton(NO_OPTION).triggerClick();
	    	}
	    }).run();
		assertTrue(controlador.isRegistrado());
	}
	
}
