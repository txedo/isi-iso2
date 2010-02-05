package pruebas;

import java.util.Date;
import java.util.Random;
import javax.swing.JTextField;
import org.uispec4j.Button;
import org.uispec4j.TextBox;
import org.uispec4j.Panel;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;
import com.toedter.calendar.JDateChooser;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Validacion;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;
import presentacion.JPBeneficiarioRegistrar;

/**
 * Pruebas del panel de registro de beneficiarios.
 */
public class PruebasJPBeneficiarioRegistrar extends org.uispec4j.UISpecTestCase {
	
	private ControladorCliente controlador;
	private JPBeneficiarioRegistrar panel;
	private Panel pnlPanel;
	private TextBox txtNIF;
	private TextBox txtNSS;
	private TextBox txtNombre;
	private TextBox txtApellidos;
	private TextBox txtDomicilio;
	private TextBox txtFechaNacimiento;
	private TextBox txtNumero;
	private TextBox txtPiso;
	private TextBox txtPuerta;
	private TextBox txtCorreo;
	private TextBox txtTelefonoFijo;
	private TextBox txtTelefonoMovil;
	private Button btnCrear;
	private Button btnRestablecer;
	private JTextField jtxtNIF;
	private JTextField jtxtNSS;
	private JTextField jtxtNombre;
	private JTextField jtxtApellidos;
	private JTextField jtxtDomicilio;
	private JTextField jtxtFechaNacimiento;
	private JTextField jtxtNumero;
	private JTextField jtxtPiso;
	private JTextField jtxtPuerta;
	private JTextField jtxtCorreo;
	private JTextField jtxtTelefonoFijo;
	private JTextField jtxtTelefonoMovil;
	private Window winPrincipal;
	
	public void setUp() {
		try {
			// Creamos el panel
			controlador = new ControladorCliente();
			panel = new JPBeneficiarioRegistrar(controlador.getVentanaPrincipal(), controlador);
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panel);
			txtNIF = pnlPanel.getTextBox("txtNIF");
			txtNSS = pnlPanel.getTextBox("txtNSS");
			txtNombre = pnlPanel.getTextBox("txtNombre");
			txtApellidos = pnlPanel.getTextBox("txtApellidos");
			txtFechaNacimiento = new TextBox((JTextField)((JDateChooser)pnlPanel.getSwingComponents(JDateChooser.class)[0]).getDateEditor());
			txtDomicilio = pnlPanel.getTextBox("txtDomicilio");
			txtNumero = pnlPanel.getTextBox("txtNumero");
			txtPiso = pnlPanel.getTextBox("txtPiso");
			txtPuerta = pnlPanel.getTextBox("txtPuerta");
			txtCorreo = pnlPanel.getTextBox("txtCorreo");
			txtTelefonoFijo = pnlPanel.getTextBox("txtTelefonoFijo");
			txtTelefonoMovil = pnlPanel.getTextBox("txtTelefonoMovil");
			btnCrear = pnlPanel.getButton("btnCrear");
			btnRestablecer = pnlPanel.getButton("btnRestablecer");
			jtxtNIF = (JTextField)txtNIF.getAwtComponent();
			jtxtNSS = (JTextField)txtNSS.getAwtComponent();
			jtxtNombre = (JTextField)txtNombre.getAwtComponent();
			jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
			jtxtFechaNacimiento = (JTextField)txtFechaNacimiento.getAwtComponent();
			jtxtDomicilio = (JTextField)txtDomicilio.getAwtComponent();
			jtxtNumero = (JTextField)txtNumero.getAwtComponent();
			jtxtPiso = (JTextField)txtPiso.getAwtComponent();
			jtxtPuerta = (JTextField)txtPuerta.getAwtComponent();
			jtxtCorreo = (JTextField)txtCorreo.getAwtComponent();
			jtxtTelefonoFijo = (JTextField)txtTelefonoFijo.getAwtComponent();
			jtxtTelefonoMovil = (JTextField)txtTelefonoMovil.getAwtComponent();
			// Establecemos conexión con el servidor front-end
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion("127.0.0.1", 2995, "admin", "nimda");
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		try {
			// Cerramos la ventana del controlador
			winPrincipal.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con datos no válidos */
	public void testDatosInvalidos() {
		String[] invalidos;
		
		try {
			// Ponemos un NIF incorrecto y comprobamos que el campo del
			// NIF se selecciona por tener un formato inválido
			txtNIF.setText("11223344");
			btnCrear.click();
			assertTrue(jtxtNIF.getSelectionStart() == 0 && jtxtNIF.getSelectionEnd() == "12345678".length());
			txtNIF.setText("11223344B");
			// Ponemos un NSS incorrecto y comprobamos que se selecciona
			txtNSS.setText("1234567890ABCD");
			btnCrear.click();
			assertTrue(jtxtNSS.getSelectionStart() == 0 && jtxtNSS.getSelectionEnd() == "1234567890ABCD".length());
			txtNSS.setText("112233445566");
			// Ponemos un nombre incorrecto y comprobamos que se selecciona
			txtNombre.setText("Pedro$");
			btnCrear.click();
			assertTrue(jtxtNombre.getSelectionStart() == 0 && jtxtNombre.getSelectionEnd() == "Pedro$".length());
			txtNombre.setText("Pedro");
			// Ponemos unos apellidos incorrectos y comprobamos que se seleccionan
			txtApellidos.setText("---");
			btnCrear.click();
			assertTrue(jtxtApellidos.getSelectionStart() == 0 && jtxtApellidos.getSelectionEnd() == "---".length());
			txtApellidos.setText("Jiménez Serrano");
			// Ponemos fechas de nacimientos incorrectas y comprobamos que
			// se seleccionan (la validación de las fechas la hace en gran
			// medida el control JDateChooser en lugar de la clase
			// Validacion, por eso aquí la comprobación es más exhaustiva)
			invalidos = new String[] { "", "  ", "31/02/1980", "10/14/1980", "27/02/2020", "a/b/c", "01-01-1980" };
			for(String fecha : invalidos) {
				jtxtFechaNacimiento.setSelectionEnd(0);
				txtFechaNacimiento.setText(fecha);
				btnCrear.click();
				assertTrue(jtxtFechaNacimiento.getSelectionStart() == 0 && jtxtFechaNacimiento.getSelectionEnd() == fecha.length());
			}
			txtFechaNacimiento.setText("01/01/1980");
			// Ponemos un domicilio incorrecto y comprobamos que se selecciona
			txtDomicilio.setText("*");
			btnCrear.click();
			assertTrue(jtxtDomicilio.getSelectionStart() == 0 && jtxtDomicilio.getSelectionEnd() == "*".length());
			txtDomicilio.setText("C/Cervantes");
			// Ponemos un número incorrecto y comprobamos que se selecciona
			txtNumero.setText("10 A");
			btnCrear.click();
			assertTrue(jtxtNumero.getSelectionStart() == 0 && jtxtNumero.getSelectionEnd() == "10 A".length());
			txtNumero.setText("38");
			// Ponemos un piso incorrecto y comprobamos que se selecciona
			txtPiso.setText("4ºF");
			btnCrear.click();
			assertTrue(jtxtPiso.getSelectionStart() == 0 && jtxtPiso.getSelectionEnd() == "4ºF".length());
			txtPiso.setText("4");
			// Ponemos una letra incorrecta y comprobamos que se selecciona
			txtPuerta.setText("5");
			btnCrear.click();
			assertTrue(jtxtPuerta.getSelectionStart() == 0 && jtxtPuerta.getSelectionEnd() == "5".length());
			txtPuerta.setText("F");
			// Ponemos un correo incorrecto y comprobamos que se selecciona
			txtCorreo.setText("pjs80@gmail");
			btnCrear.click();
			assertTrue(jtxtCorreo.getSelectionStart() == 0 && jtxtCorreo.getSelectionEnd() == "pjs80@gmail".length());
			txtCorreo.setText("pjs80@gmail.com");
			// Ponemos un teléfono fijo incorrecto y comprobamos que se selecciona
			txtTelefonoFijo.setText("926 147 130");
			btnCrear.click();
			assertTrue(jtxtTelefonoFijo.getSelectionStart() == 0 && jtxtTelefonoFijo.getSelectionEnd() == "926 147 130".length());
			txtTelefonoFijo.setText("926147130");
			// Ponemos un teléfono fijo incorrecto y comprobamos que se selecciona
			txtTelefonoMovil.setText("61011122");
			btnCrear.click();
			assertTrue(jtxtTelefonoMovil.getSelectionStart() == 0 && jtxtTelefonoMovil.getSelectionEnd() == "61011122".length());
			txtTelefonoMovil.setText("626405060");
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con datos válidos */
	@SuppressWarnings("deprecation")
	public void testDatosValidos() {
		Beneficiario beneLeido, beneCreado;
		String nif = "", nss = "", otroNif, otroNss;
		
		try {
			// Creamos un beneficiario con datos válidos
			nif = generarNIF();
			nss = generarNSS();
			txtNIF.setText(nif);
			txtNSS.setText(nss);
			txtNombre.setText("Pedro");
			txtApellidos.setText("Jiménez Serrano");
			txtFechaNacimiento.setText("01/01/1980");
			txtDomicilio.setText("C/Cervantes");
			txtNumero.setText("38");
			txtPiso.setText("4");
			txtPuerta.setText("F");
			txtCorreo.setText("pjs80@gmail.com");
			txtTelefonoFijo.setText("926147130");
			txtTelefonoMovil.setText("626405060");
			btnCrear.click();
			comprobarCamposVacios();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que el beneficiario se ha creado correctamente
			beneLeido = controlador.consultarBeneficiario(nif);
			beneCreado = new Beneficiario(nif, nss, "Pedro", "Jiménez Serrano", new Date(1980 - 1900, 0, 1), "C/Cervantes, nº 38, 4º F", "pjs80@gmail.com", 926147130, 626405060);
			assertEquals(beneLeido, beneCreado);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un beneficiario con el mismo NIF
			otroNss = generarNSS();
			txtNIF.setText(nif);
			txtNSS.setText(otroNss);
			txtNombre.setText("Pedro");
			txtApellidos.setText("Jiménez Serrano");
			txtFechaNacimiento.setText("01/01/1980");
			txtDomicilio.setText("C/Cervantes");
			txtNumero.setText("38");
			txtPiso.setText("4");
			txtPuerta.setText("F");
			txtCorreo.setText("pjs80@gmail.com");
			txtTelefonoFijo.setText("926147130");
			txtTelefonoMovil.setText("626405060");
			btnCrear.click();
			assertEquals(txtNIF.getText(), nif);
			// Intentamos crear un beneficiario con el mismo NSS
			otroNif = generarNIF();
			txtNIF.setText(otroNif);
			txtNSS.setText(nss);
			btnCrear.click();
			assertEquals(txtNSS.getText(), nss);			
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// TODO: faltan comprobaciones con campos opcionales
		// (correo electrónico, puerta...)
	}
	
	/** Pruebas de restablecimiento de los datos */
	public void testRestablecer() {
		String nif, nss;
		
		try {
			// Ponemos datos válidos pero borramos todos los
			// campos pulsando el botón Restablecer 
			nif = generarNIF();
			nss = generarNSS();
			txtNIF.setText(nif);
			txtNSS.setText(nss);
			txtNombre.setText("Pedro");
			txtApellidos.setText("Jiménez Serrano");
			txtFechaNacimiento.setText("01/01/1980");
			txtDomicilio.setText("C/Cervantes");
			txtNumero.setText("38");
			txtPiso.setText("4");
			txtPuerta.setText("F");
			txtCorreo.setText("pjs80@gmail.com");
			txtTelefonoFijo.setText("926147130");
			txtTelefonoMovil.setText("626405060");
			btnRestablecer.click();
			comprobarCamposVacios();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	private void comprobarCamposVacios() {
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNSS.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
		assertTrue(txtApellidos.getText().equals(""));
		assertTrue(txtFechaNacimiento.getText().equals(""));
		assertTrue(txtDomicilio.getText().equals(""));
		assertTrue(txtNumero.getText().equals(""));
		assertTrue(txtPiso.getText().equals(""));
		assertTrue(txtPuerta.getText().equals(""));
		assertTrue(txtCorreo.getText().equals(""));
		assertTrue(txtTelefonoFijo.getText().equals(""));
		assertTrue(txtTelefonoMovil.getText().equals(""));
	}
	
	private String generarNIF() {
		Random r;
		String nif;
		boolean existe;
		int i;
		
		nif = "";
		r = new Random();
		try {
			do {
				// Generamos un NIF aleatorio
				nif = "";
				for(i = 0; i < Validacion.NIF_LONGITUD - 1; i++) {
					nif = nif + String.valueOf(r.nextInt(10));
				}
				nif = nif + "X";
				// Comprobamos si ya hay un beneficiario con ese NIF
				try {
					controlador.consultarBeneficiario(nif);
					existe = true;
				} catch(BeneficiarioInexistenteException e) {
					existe = false;
				}
			} while(existe);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		return nif;
	}
	
	private String generarNSS() {
		Random r;
		String nss;
		boolean existe;
		int i;
		
		nss = "";
		r = new Random();
		try {
			do {
				// Generamos un NSS aleatorio
				nss = "";
				for(i = 0; i < Validacion.NSS_LONGITUD; i++) {
					nss = nss + String.valueOf(r.nextInt(10));
				}
				// Comprobamos si ya hay un beneficiario con ese NSS
				try {
					controlador.consultarBeneficiarioPorNSS(nss);
					existe = true;
				} catch(BeneficiarioInexistenteException e) {
					existe = false;
				}
			} while(existe);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		return nss;
	}
	
}
