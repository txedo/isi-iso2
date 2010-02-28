package pruebas;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.uispec4j.Button;
import org.uispec4j.ComboBox;
import org.uispec4j.TextBox;
import org.uispec4j.Panel;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;
import com.toedter.calendar.JDateChooser;
import comunicaciones.ConfiguracionCliente;

import dominio.conocimiento.Beneficiario;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.CodigoPostalIncorrectoException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.LocalidadIncorrectaException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.NumeroDomicilioIncorrectoException;
import excepciones.PisoDomicilioIncorrectoException;
import excepciones.ProvinciaIncorrectaException;
import excepciones.PuertaDomicilioIncorrectoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;
import presentacion.JPBeneficiarioRegistrar;

/**
 * Pruebas del panel de registro de beneficiarios.
 */
public class PruebasJPBeneficiarioRegistrar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantes {
	
	private String nif;
	private boolean beneficiarioCreado = false;
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
	private TextBox txtLocalidad;
	private TextBox txtProvincia;
	private TextBox txtCP;
	private TextBox txtTelefonoFijo;
	private TextBox txtTelefonoMovil;
	private ComboBox cmbCentros;
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
	private JTextField jtxtLocalidad;
	private JTextField jtxtProvincia;
	private JTextField jtxtCP;
	private JTextField jtxtCorreo;
	private JTextField jtxtTelefonoFijo;
	private JTextField jtxtTelefonoMovil;
	private JComboBox jcmbCentros;
	private Window winPrincipal;
	
	protected void setUp() {
		try {
			// Establecemos conexión con el servidor front-end
			controlador = new ControladorCliente();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IPServidorFrontend, puertoServidorFrontend), usuarioAdmin, passwordAdmin);
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			// Creamos el panel
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
			txtLocalidad = pnlPanel.getTextBox("txtLocalidad");
			txtCP = pnlPanel.getTextBox("txtCP");
			txtProvincia = pnlPanel.getTextBox("txtProvincia");
			txtCorreo = pnlPanel.getTextBox("txtCorreo");
			txtTelefonoFijo = pnlPanel.getTextBox("txtTelefonoFijo");
			txtTelefonoMovil = pnlPanel.getTextBox("txtTelefonoMovil");
			cmbCentros = pnlPanel.getComboBox("cmbCentros");
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
			jtxtLocalidad = (JTextField)txtLocalidad.getAwtComponent();
			jtxtCP = (JTextField)txtCP.getAwtComponent();
			jtxtProvincia = (JTextField)txtProvincia.getAwtComponent();
			jtxtCorreo = (JTextField)txtCorreo.getAwtComponent();
			jtxtTelefonoFijo = (JTextField)txtTelefonoFijo.getAwtComponent();
			jtxtTelefonoMovil = (JTextField)txtTelefonoMovil.getAwtComponent();
			jcmbCentros = (JComboBox)cmbCentros.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			if (beneficiarioCreado) {
				Beneficiario beneficiario = controlador.consultarBeneficiarioPorNIF(nif);
				controlador.eliminarBeneficiario(beneficiario);
			}
			// Cerramos la sesión y la ventana del controlador
			controlador.cerrarSesion();
			winPrincipal.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con datos no válidos */
	public void testDatosInvalidos() {
		String[] invalidos;
		String mensaje;
		
		try {
			// Ponemos un NIF incorrecto y comprobamos que el campo del
			// NIF se selecciona por tener un formato inválido
			txtNIF.setText("11223344");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new NIFIncorrectoException().getMessage(), mensaje);
			txtNIF.setText("11223344B");
			// Ponemos un NSS incorrecto y comprobamos el mensaje de error
			txtNSS.setText("1234567890ABCD");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new NSSIncorrectoException().getMessage(), mensaje);
			txtNSS.setText("112233445566");
			// Ponemos un nombre incorrecto y comprobamos el mensaje de error
			txtNombre.setText("Pedro$");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new NombreIncorrectoException().getMessage(), mensaje);
			txtNombre.setText("Pedro");
			// Ponemos unos apellidos incorrectos y el mensaje de error
			txtApellidos.setText("---");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new ApellidoIncorrectoException().getMessage(), mensaje);
			txtApellidos.setText("Jiménez Serrano");
			// Ponemos fechas de nacimientos incorrectas y comprobamos que
			// se seleccionan (la validación de las fechas la hace en gran
			// medida el control JDateChooser en lugar de la clase
			// Validacion, por eso aquí la comprobación es más exhaustiva)
			invalidos = new String[] { "  ", "31/02/1980", "10/14/1980", "27/02/2020", "a/b/c", "01-01-1980" };
			for(String fecha : invalidos) {
				jtxtFechaNacimiento.setSelectionEnd(0);
				txtFechaNacimiento.setText(fecha);
				btnCrear.click();
				assertEquals(jtxtFechaNacimiento.getSelectedText(), txtFechaNacimiento.getText());
			}
			txtFechaNacimiento.setText("01/01/1980");
			// Ponemos un domicilio incorrecto y comprobamos que se selecciona
			txtDomicilio.setText("*");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new DomicilioIncorrectoException().getMessage(), mensaje);
			txtDomicilio.setText("C/Cervantes");
			// Ponemos un número incorrecto y comprobamos el mensaje de error
			txtNumero.setText("10 A");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new NumeroDomicilioIncorrectoException().getMessage(), mensaje);
			txtNumero.setText("38");
			// Ponemos un piso incorrecto y comprobamos el mensaje de error
			txtPiso.setText("4ºF");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new PisoDomicilioIncorrectoException().getMessage(), mensaje);
			txtPiso.setText("4");
			// Ponemos una puerta incorrecta y comprobamos el mensaje de error
			txtPuerta.setText("5");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new PuertaDomicilioIncorrectoException().getMessage(), mensaje);
			txtPuerta.setText("F");
			// Ponemos una localidad incorrecta y comprobamos el mensaje de error
			txtLocalidad.setText("<ninguna>");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new LocalidadIncorrectaException().getMessage(), mensaje);
			txtLocalidad.setText("Ciudad Real");
			// Ponemos un código postal incorrecto y comprobamos el mensaje de error
			txtCP.setText("130000");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new CodigoPostalIncorrectoException().getMessage(), mensaje);
			txtCP.setText("12345");
			// Ponemos una provincia incorrecta y comprobamos el mensaje de error
			txtProvincia.setText("900");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new ProvinciaIncorrectaException().getMessage(), mensaje);
			txtProvincia.setText("Ciudad Real");
			// Ponemos un correo incorrecto y comprobamos el mensaje de error
			txtCorreo.setText("pjs80@gmail");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new CorreoElectronicoIncorrectoException().getMessage(), mensaje);
			txtCorreo.setText("pjs80@gmail.com");
			// Ponemos un teléfono fijo incorrecto y comprobamos el mensaje de error
			txtTelefonoFijo.setText("926 147 130");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new TelefonoFijoIncorrectoException().getMessage(), mensaje);
			txtTelefonoFijo.setText("926147130");
			// Ponemos un teléfono móvil incorrecto y comprobamos el mensaje de error
			txtTelefonoMovil.setText("61011122");
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new TelefonoMovilIncorrectoException().getMessage(), mensaje);
			txtTelefonoMovil.setText("626405060");
			// Ponemos un centro de salud incorrecto y comprobamos que se produce un error
			jcmbCentros.grabFocus();
			jcmbCentros.setSelectedIndex(-1);
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION);
			assertEquals(new CentroSaludIncorrectoException().getMessage(), mensaje);
			jcmbCentros.setSelectedIndex(0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con datos válidos */
	@SuppressWarnings("deprecation")
	public void testCrearBeneficiarioValido1() {
		String nss = "";
		String otroNif;
		String otroNss;
		
		try {
			do {
				// Creamos un beneficiario con todos los datos válidos
				nif = UtilidadesPruebas.generarNIF();
				nss = UtilidadesPruebas.generarNSS();
				txtNIF.setText(nif);
				txtNSS.setText(nss);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtFechaNacimiento.setText("01/01/1980");
				txtDomicilio.setText("C/Cervantes");
				txtNumero.setText("38");
				txtPiso.setText("4");
				txtPuerta.setText("F");
				txtLocalidad.setText("Ciudad Real");
				txtCP.setText("13001");
				txtProvincia.setText("Ciudad Real");
				txtCorreo.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				jcmbCentros.setSelectedIndex(0);
				btnCrear.click();
				// Si no se han quedado los campos vacios, es porque ya existia el login
			} while (!txtNIF.getText().equals(""));
			// Aqui es mejor hacer la comprobacion de esta manera, porque el mensaje que se muestra en 
			// el diálogo de confirmación varia según se pueda asignar o no médico al beneficiario
			comprobarCamposVacios();
			beneficiarioCreado = true;
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un beneficiario con el mismo NIF
			// y comprobamos que los campos no se han borrado
			otroNss = UtilidadesPruebas.generarNSS();
			txtNIF.setText(nif);
			txtNSS.setText(otroNss);
			txtNombre.setText("Pedro");
			txtApellidos.setText("Jiménez Serrano");
			txtFechaNacimiento.setText("01/01/1980");
			txtDomicilio.setText("C/Cervantes");
			txtNumero.setText("38");
			txtPiso.setText("4");
			txtPuerta.setText("F");
			txtLocalidad.setText("Ciudad Real");
			txtCP.setText("13001");
			txtProvincia.setText("Ciudad Real");
			txtCorreo.setText("pjs80@gmail.com");
			txtTelefonoFijo.setText("926147130");
			txtTelefonoMovil.setText("626405060");
			jcmbCentros.setSelectedIndex(0);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION), "Ya existe una persona en el sistema registrada con el NIF " + nif + ".");
			assertEquals(txtNIF.getText(), nif);
			// Intentamos crear un beneficiario con el mismo NSS
			// y comprobamos que los campos no se han borrado
			otroNif = UtilidadesPruebas.generarNIF();
			txtNIF.setText(otroNif);
			txtNSS.setText(nss);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrear, OK_OPTION),  "El beneficiario con el NSS " + nss + " ya se encuentra dado de alta en el sistema y no se puede registrar de nuevo.");
			assertEquals(txtNSS.getText(), nss);			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testCrearBeneficiarioValido2() {
		String nss = "";
		
		try {
			do {
				// Creamos un beneficiario con datos válidos omitiendo
				// el correo electrónico y los teléfonos
				nif = UtilidadesPruebas.generarNIF();
				nss = UtilidadesPruebas.generarNSS();
				txtNIF.setText(nif);
				txtNSS.setText(nss);
				txtNombre.setText("Pedro  ");
				txtApellidos.setText("  Jiménez Serrano");
				txtFechaNacimiento.setText("01/01/1980");
				txtDomicilio.setText("C/Cervantes");
				txtNumero.setText("38");
				txtPiso.setText("4");
				txtPuerta.setText("F");
				txtLocalidad.setText("Ciudad Real");
				txtCP.setText("13001");
				txtProvincia.setText("Ciudad Real");
				txtCorreo.setText("");
				txtTelefonoFijo.setText(" ");
				txtTelefonoMovil.setText("  ");
				jcmbCentros.setSelectedIndex(0);
				btnCrear.click();
			} while (!txtNIF.getText().equals(""));
			// Aqui es mejor hacer la comprobacion de esta manera, porque el mensaje que se muestra en 
			// el diálogo de confirmación varia según se pueda asignar o no médico al beneficiario
			comprobarCamposVacios();
			beneficiarioCreado = true;
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testCrearBeneficiarioValido3() {
		String nss = "";
		
		try {
			do {
				// Creamos un beneficiario con datos válidos omitiendo
				// el número, el piso y la puerta del domicilio
				nif = UtilidadesPruebas.generarNIF();
				nss = UtilidadesPruebas.generarNSS();
				txtNIF.setText(nif);
				txtNSS.setText(nss);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtFechaNacimiento.setText("01/01/1980");
				txtDomicilio.setText("  C/Cervantes");
				txtNumero.setText("");
				txtPiso.setText(" ");
				txtPuerta.setText("  ");
				txtLocalidad.setText("Ciudad Real  ");
				txtCP.setText("13001");
				txtProvincia.setText("Ciudad Real");
				txtCorreo.setText("pjs80@gmail.com  ");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				jcmbCentros.setSelectedIndex(0);
				btnCrear.click();
			} while (!txtNIF.getText().equals(""));
			// Aqui es mejor hacer la comprobacion de esta manera, porque el mensaje que se muestra en 
			// el diálogo de confirmación varia según se pueda asignar o no médico al beneficiario
			comprobarCamposVacios();
			beneficiarioCreado = true;
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de restablecimiento de los datos */
	public void testRestablecer() {
		String nss;
		
		try {
			// Ponemos datos válidos pero borramos todos los
			// campos pulsando el botón Restablecer 
			nif = UtilidadesPruebas.generarNIF();
			nss = UtilidadesPruebas.generarNSS();
			txtNIF.setText(nif);
			txtNSS.setText(nss);
			txtNombre.setText("Pedro");
			txtApellidos.setText("Jiménez Serrano");
			txtFechaNacimiento.setText("01/01/1980");
			txtDomicilio.setText("C/Cervantes");
			txtNumero.setText("38");
			txtPiso.setText("4");
			txtPuerta.setText("F");
			txtLocalidad.setText("Ciudad Real");
			txtCP.setText("13001");
			txtProvincia.setText("Ciudad Real");
			txtCorreo.setText("pjs80@gmail.com");
			txtTelefonoFijo.setText("926147130");
			txtTelefonoMovil.setText("626405060");
			jcmbCentros.setSelectedIndex(0);
			btnRestablecer.click();
			comprobarCamposVacios();
			beneficiarioCreado = false;
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
		assertTrue(txtLocalidad.getText().equals(""));
		assertTrue(txtCP.getText().equals(""));
		assertTrue(txtProvincia.getText().equals(""));
		assertTrue(txtCorreo.getText().equals(""));
		assertTrue(txtTelefonoFijo.getText().equals(""));
		assertTrue(txtTelefonoMovil.getText().equals(""));
		assertTrue(jcmbCentros.getSelectedIndex() == -1);
	}
	

	
}
