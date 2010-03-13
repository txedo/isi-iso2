package pruebas;

import org.uispec4j.Button;
import org.uispec4j.MenuItem;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import comunicaciones.ConfiguracionCliente;

import presentacion.JFAcercaDeCliente;
import presentacion.JFPrincipal;
import dominio.control.ControladorCliente;


import junit.framework.TestCase;


public class PruebasJFAcercaDe extends TestCase implements IDatosConexionPruebas {

	private ControladorCliente controlador;
	private Window winVentana;
	private MenuItem mniAcercaDe;
	private JFAcercaDeCliente frmAcercaDe;
	private Panel pnlPanel;
	private Button btnCerrar;
	
	public void setUp() {
		try {
			frmAcercaDe = new JFAcercaDeCliente();
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(frmAcercaDe);
			btnCerrar = pnlPanel.getButton("btnAceptar");
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		try {
			// Liberamos los recursos usados por la ventana
			winVentana.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la ventana 'Acerca de' */
	public void testAcercaDe() {
		try {			
			Window win;
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						// Abrimos la ventana de 'Acerca de' y la cerramos
						frmAcercaDe.setVisible(true);
						// TODO: no se cierra la ventana al pulsar el botón
						btnCerrar.click();
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
}
