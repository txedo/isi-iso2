package pruebas;

import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.TextBox;
import org.uispec4j.Window;

import dominio.conocimiento.ConfiguracionRespaldo;
import presentacion.JFConfigRespaldo;

/**
 * Pruebas de la ventana de configuración del servidor de respaldo. 
 */
public class PruebasJFConfigRespaldo extends org.uispec4j.UISpecTestCase {

	private JFConfigRespaldo ventana;
	private Window winVentana;
	private Button btnAceptar;
	private Button btnCancelar;
	private TextBox txtIPBDRespaldo;
	private TextBox txtPuertoBDRespaldo;
	private TextBox txtPuertoRespaldo;
	private JTextField jtxtIPBDRespaldo;
	private JTextField jtxtPuertoBDRespaldo;
	private JTextField jtxtPuertoRespaldo;
	
	public void setUp() {
		try {
			// Creamos la ventana de configuración
			ventana = new JFConfigRespaldo();
			// Obtenemos los componentes de la ventana
			winVentana = new Window(ventana);
			btnAceptar = winVentana.getButton("btnAceptar");
			btnCancelar = winVentana.getButton("btnCancelar");
			txtIPBDRespaldo = winVentana.getTextBox("txtIPBDRespaldo");
			txtPuertoBDRespaldo = winVentana.getTextBox("txtPuertoBDRespaldo");
			txtPuertoRespaldo = winVentana.getTextBox("txtPuertoRespaldo");
			jtxtIPBDRespaldo = (JTextField)txtIPBDRespaldo.getAwtComponent();
			jtxtPuertoBDRespaldo = (JTextField)txtPuertoBDRespaldo.getAwtComponent();
			jtxtPuertoRespaldo = (JTextField)txtPuertoRespaldo.getAwtComponent();
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
		ConfiguracionRespaldo configOriginal;

		try {
			// Obtenemos la configuración por defecto
			configOriginal = new ConfiguracionRespaldo();
			// Ponemos una IP de BD incorrecta para ver que no se acepta
			txtIPBDRespaldo.setText("300.0.0.300");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			assertTrue(jtxtIPBDRespaldo.getSelectionStart() == 0 && jtxtIPBDRespaldo.getSelectionEnd() == "300.0.0.300".length());
			txtIPBDRespaldo.setText("127.0.0.1");
			// Ponemos un puerto de BD incorrecto para ver que no se acepta
			txtPuertoBDRespaldo.setText("1234567");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			assertTrue(jtxtPuertoBDRespaldo.getSelectionStart() == 0 && jtxtPuertoBDRespaldo.getSelectionEnd() == "1234567".length());
			txtPuertoBDRespaldo.setText("1990");
			// Ponemos un puerto de escucha inválido para ver que no se acepta
			txtPuertoRespaldo.setText("abcd");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			assertTrue(jtxtPuertoRespaldo.getSelectionStart() == 0 && jtxtPuertoRespaldo.getSelectionEnd() == "abcd".length());
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con configuraciones válidas */
	public void testConfiguracionesValidas() {
		ConfiguracionRespaldo configOriginal;
		
		try {
			// Obtenemos la configuración por defecto
			configOriginal = new ConfiguracionRespaldo();
			// Ponemos valores válidos pero cancelamos la ventana
			txtIPBDRespaldo.setText("10.0.0.4");
			txtPuertoBDRespaldo.setText("5785");
			txtPuertoRespaldo.setText("10183");
			btnCancelar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			// Ponemos valores válidos y aceptamos la ventana
			txtIPBDRespaldo.setText("192.168.0.89");
			txtPuertoBDRespaldo.setText("1239");
			txtPuertoRespaldo.setText("6710");
			btnAceptar.click();
			assertEquals(new ConfiguracionRespaldo("192.168.0.89", 1239, 6710), ventana.getConfiguracion());
			// Ponemos valores válidos con espacios y aceptamos la ventana
			txtIPBDRespaldo.setText("192.168.3.45  ");
			txtPuertoBDRespaldo.setText("  9000");
			txtPuertoRespaldo.setText("21890  ");
			btnAceptar.click();
			assertEquals(new ConfiguracionRespaldo("192.168.3.45", 9000, 21890), ventana.getConfiguracion());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
