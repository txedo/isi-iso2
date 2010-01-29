package pruebas;

import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.TextBox;
import org.uispec4j.Window;

import dominio.conocimiento.ConfiguracionFrontend;
import presentacion.JFConfigFrontend;

/**
 * Pruebas de la ventana de configuración del servidor frontend. 
 */
public class PruebasJFConfigFrontend extends org.uispec4j.UISpecTestCase {

	private JFConfigFrontend ventana;
	private Window winVentana;
	private Button btnAceptar;
	private Button btnCancelar;
	private TextBox txtIPBDPrincipal;
	private TextBox txtPuertoBDPrincipal;
	private TextBox txtIPRespaldo;
	private TextBox txtPuertoRespaldo;
	private TextBox txtPuertoFrontend;
	private CheckBox chkRespaldo;
	
	public void setUp() {
		try {
			// Creamos la ventana de configuración
			ventana = new JFConfigFrontend();
			// Obtenemos los componentes de la ventana
			winVentana = new Window(ventana);
			btnAceptar = winVentana.getButton("btnAceptar");
			btnCancelar = winVentana.getButton("btnCancelar");
			txtIPBDPrincipal = winVentana.getTextBox("txtIPBDPrincipal");
			txtPuertoBDPrincipal = winVentana.getTextBox("txtPuertoBDPrincipal");
			txtIPRespaldo = winVentana.getTextBox("txtIPRespaldo");
			txtPuertoRespaldo = winVentana.getTextBox("txtPuertoRespaldo");
			chkRespaldo = winVentana.getCheckBox("chkRespaldo");
			txtPuertoFrontend = winVentana.getTextBox("txtPuertoFrontend");
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
	
	/** Pruebas con configuraciones no válidas */
	public void testConfiguracionesInvalidas() {
		ConfiguracionFrontend configOriginal;

		try {
			// Obtenemos la configuración por defecto
			configOriginal = new ConfiguracionFrontend();
			// Ponemos IPs de BDs incorrectas para ver que no se aceptan
			txtIPBDPrincipal.setText("300.0.0.300");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			txtIPBDPrincipal.setText("");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			// Ponemos puertos de BD incorrectos para ver que no se aceptan
			txtIPBDPrincipal.setText("127.0.0.1");
			txtPuertoBDPrincipal.setText("1234567");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			txtPuertoBDPrincipal.setText("abcdefg");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			// Ponemos IPs de servidor incorrectas para ver que no se aceptan
			txtPuertoBDPrincipal.setText("3306");
			chkRespaldo.select();
			txtIPRespaldo.setText("127.0.0.1w");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			txtIPRespaldo.setText("");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			// Ponemos puertos de servidor incorrectos para ver que no se aceptan
			txtIPRespaldo.setText("127.0.0.1");
			txtPuertoRespaldo.setText("-900");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			txtPuertoRespaldo.setText("¿¿??");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			// Ponemos puertos de escucha inválidos para ver que no se aceptan
			txtPuertoRespaldo.setText("1098");
			txtPuertoFrontend.setText("001000100");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			txtPuertoFrontend.setText("5,29");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas con configuraciones válidas */
	public void testConfiguracionesValidas() {
		ConfiguracionFrontend configOriginal;
		
		try {
			// Obtenemos la configuración por defecto
			configOriginal = new ConfiguracionFrontend();
			// Ponemos valores válidos pero cancelamos la ventana
			txtIPBDPrincipal.setText("192.168.240.120");
			txtPuertoBDPrincipal.setText("8001");
			chkRespaldo.select();
			txtIPRespaldo.setText("172.20.0.1");
			txtPuertoRespaldo.setText("12819");
			txtPuertoFrontend.setText("4500");
			btnCancelar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			// Ponemos valores inválidos para el servidor de
			// respaldo pero lo deshabilitamos y aceptamos la ventana
			txtIPBDPrincipal.setText("10.1.2.3");
			txtPuertoBDPrincipal.setText("1829");
			txtIPRespaldo.setText("ninguna");
			txtPuertoRespaldo.setText("ninguno");
			chkRespaldo.unselect();
			txtPuertoFrontend.setText("8001");
			btnAceptar.click();
			assertEquals(new ConfiguracionFrontend("10.1.2.3", 1829, 8001), ventana.getConfiguracion());
			// Ponemos valores válidos y aceptamos la ventana
			txtIPBDPrincipal.setText("10.1.2.3");
			txtPuertoBDPrincipal.setText("1829");
			chkRespaldo.select();
			txtIPRespaldo.setText("192.168.1.100");
			txtPuertoRespaldo.setText("7001");
			txtPuertoFrontend.setText("8001");
			btnAceptar.click();
			assertEquals(new ConfiguracionFrontend("10.1.2.3", 1829, "192.168.1.100", 7001, 8001), ventana.getConfiguracion());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
