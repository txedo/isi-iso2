package pruebas;

import org.uispec4j.Button;
import org.uispec4j.TextBox;
import org.uispec4j.Window;
import dominio.conocimiento.ConfiguracionRespaldo;
import presentacion.JFConfiguracion;

/**
 * Pruebas de la ventana de configuración del servidor de respaldo. 
 */
public class PruebasJFConfiguracion extends org.uispec4j.UISpecTestCase {

	private JFConfiguracion ventana;
	private Window winVentana;
	private Button btnAceptar;
	private Button btnCancelar;
	private TextBox txtIPBDRespaldo;
	private TextBox txtPuertoBDRespaldo;
	private TextBox txtPuertoRespaldo;
	
	public void setUp() {
		try {
			// Creamos la ventana de configuración
			ventana = new JFConfiguracion();
			// Obtenemos los componentes de la ventana
			winVentana = new Window(ventana);
			btnAceptar = winVentana.getButton("btnAceptar");
			btnCancelar = winVentana.getButton("btnCancelar");
			txtIPBDRespaldo = winVentana.getTextBox("txtIPBDRespaldo");
			txtPuertoBDRespaldo = winVentana.getTextBox("txtPuertoBDRespaldo");
			txtPuertoRespaldo = winVentana.getTextBox("txtPuertoRespaldo");
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
		ConfiguracionRespaldo configOriginal;

		try {
			// Obtenemos la configuración por defecto
			configOriginal = new ConfiguracionRespaldo();
			// Ponemos una IP de BD incorrecta para ver que no se acepta
			txtIPBDRespaldo.setText("300.0.0.300");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			// Ponemos puertos de BD incorrecto para ver que no se aceptan
			txtIPBDRespaldo.setText("127.0.0.1");
			txtPuertoBDRespaldo.setText("1234567");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			txtPuertoBDRespaldo.setText("abcdefg");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			// Ponemos puertos de escucha para ver que no se aceptan
			txtPuertoBDRespaldo.setText("1990");
			txtPuertoRespaldo.setText("1234567");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
			txtPuertoRespaldo.setText("abcdefg");
			btnAceptar.click();
			assertEquals(configOriginal, ventana.getConfiguracion());
		} catch(Exception e) {
			fail(e.toString());
		}

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
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
