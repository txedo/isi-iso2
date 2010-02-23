package pruebas;

import java.util.Date;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.ComboBox;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPBeneficiarioConsultar;
import presentacion.auxiliar.Validacion;

import com.toedter.calendar.JDateChooser;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Direccion;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;

public class PruebasJPBeneficiarioConsultar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantes {
	
	boolean eliminado;
	private Beneficiario beneficiarioPrueba;
	private ControladorCliente controlador;
	private JPBeneficiarioConsultar panel;
	private Panel pnlPanel;
	private ComboBox cmbIdentificacion;
	private TextBox txtIdentificacion;
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
	private Button btnBuscar;
	private Button btnGuardar;
	private Button btnEliminar;
	private CheckBox chkEditar;
	private JComboBox jcmbIdentificacion;
	private JTextField jtxtIdentificacion;
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
	private JCheckBox jchkEditar;
	private Window winPrincipal;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		try {
			// Establecemos conexi�n con el servidor front-end
			controlador = new ControladorCliente();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(IPServidorFrontend, puertoServidorFrontend, usuarioAdmin, passwordAdmin);
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			// Creamos un beneficiario de prueba
			beneficiarioPrueba = new Beneficiario ();
			beneficiarioPrueba.setNif(generarNIF());
			beneficiarioPrueba.setNss(generarNSS());
			beneficiarioPrueba.setNombre("beneficiario");
			beneficiarioPrueba.setApellidos("de prueba");
			beneficiarioPrueba.setFechaNacimiento(new Date("01/01/1980"));
			beneficiarioPrueba.setDireccion(new Direccion("lagasca", "", "", "", "Madrid", "Madrid", 28000));
			beneficiarioPrueba.setCentroSalud(controlador.consultarCentros().firstElement());
			controlador.crearBeneficiario(beneficiarioPrueba);
			eliminado = false;
			// Creamos el panel
			panel = new JPBeneficiarioConsultar(controlador.getVentanaPrincipal(), controlador);
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panel);
			cmbIdentificacion = pnlPanel.getComboBox("cmbIdentificacion");
			txtIdentificacion = pnlPanel.getTextBox("txtIdentificacion");
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
			btnBuscar = pnlPanel.getButton("btnBuscar");
			btnGuardar = pnlPanel.getButton("btnGuardar");
			btnEliminar = pnlPanel.getButton("btnEliminar");
			chkEditar = pnlPanel.getCheckBox("chkEditar");
			jcmbIdentificacion = (JComboBox)cmbIdentificacion.getAwtComponent();
			jtxtIdentificacion = (JTextField)txtIdentificacion.getAwtComponent();
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
			jchkEditar = (JCheckBox)chkEditar.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			if (!eliminado) controlador.eliminarBeneficiario(beneficiarioPrueba);
			// Cerramos la sesi�n y la ventana del controlador
			controlador.cerrarSesion();
		} catch(Exception e) {
			fail(e.toString());
		}
		winPrincipal.dispose();
	}
	
	/** Pruebas con datos no v�lidos */
	public void testDatosInvalidos() {
		String[] invalidos;
		
		try {
			// Buscamos un beneficiario por su NIF
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			// Inicialmente probamos con un NIF nulo
			txtIdentificacion.setText("");
			btnBuscar.click();
			assertEquals(txtNIF.getText(), "");
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inv�lido
			txtIdentificacion.setText("11223344");
			btnBuscar.click();
			assertEquals(jtxtIdentificacion.getSelectedText(), txtIdentificacion.getText());
			// Probamos con un NIF que no est� dado de alta en el sistema
			txtIdentificacion.setText("00000000a");
			btnBuscar.click();
			assertEquals(jtxtIdentificacion.getSelectedText(), txtIdentificacion.getText());
			// Buscamos un beneficiario por su NSS
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			// Ponemos un NSS incorrecto y comprobamos que el campo del
			// identificacion se selecciona por tener un formato inv�lido
			txtIdentificacion.setText("11223344");
			btnBuscar.click();
			assertEquals(jtxtIdentificacion.getSelectedText(), txtIdentificacion.getText());
			// Probamos con un NSS que no est� dado de alta en el sistema
			txtIdentificacion.setText("000000000000");
			btnBuscar.click();
			assertEquals(jtxtIdentificacion.getSelectedText(), txtIdentificacion.getText());
			// Probamos con el NIF de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			btnBuscar.click();
			// Para saber que se ha encontrado con �xito, comprobamos que los campos txtNIF y txtNSS no son vacios
			assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
			assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
			// A continuaci�n se pasar� a tratar de editar el beneficiario
			assertFalse(chkEditar.isSelected());
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Los campos NIF y NSS no son editables
			// Escribimos un nombre incorrecto y comprobamos que se selecciona
			txtNombre.setText("12341234");
			btnGuardar.click();
			assertEquals(jtxtNombre.getSelectedText(), txtNombre.getText());
			txtNombre.setText("Jose Manuel");
			// Escribimos un apellido incorrecto y comprobamos que se selecciona
			txtApellidos.setText("12341234");
			btnGuardar.click();
			assertEquals(jtxtApellidos.getSelectedText(), txtApellidos.getText());
			txtApellidos.setText("L�pez Garc�a");
			// Ponemos fechas de nacimientos incorrectas y comprobamos que
			// se seleccionan (la validaci�n de las fechas la hace en gran
			// medida el control JDateChooser en lugar de la clase
			// Validacion, por eso aqu� la comprobaci�n es m�s exhaustiva)
			invalidos = new String[] { "  ", "31/02/1980", "10/14/1980", "27/02/2020", "a/b/c", "01-01-1980" };
			for(String fecha : invalidos) {
				jtxtFechaNacimiento.setSelectionEnd(0);
				txtFechaNacimiento.setText(fecha);
				btnGuardar.click();
				assertEquals(jtxtFechaNacimiento.getSelectedText(), txtFechaNacimiento.getText());
			}
			txtFechaNacimiento.setText("01/01/1980");
			// Ponemos un domicilio incorrecto y comprobamos que se selecciona
			txtDomicilio.setText("*");
			btnGuardar.click();
			assertEquals(jtxtDomicilio.getSelectedText(), txtDomicilio.getText());
			txtDomicilio.setText("C/Cervantes");
			// Ponemos un n�mero incorrecto y comprobamos que se selecciona
			txtNumero.setText("10 A");
			btnGuardar.click();
			assertEquals(jtxtNumero.getSelectedText(), txtNumero.getText());
			txtNumero.setText("38");
			// Ponemos un piso incorrecto y comprobamos que se selecciona
			txtPiso.setText("4�F");
			btnGuardar.click();
			assertEquals(jtxtPiso.getSelectedText(), txtPiso.getText());
			txtPiso.setText("4");
			// Ponemos una puerta incorrecta y comprobamos que se selecciona
			txtPuerta.setText("5");
			btnGuardar.click();
			assertEquals(jtxtPuerta.getSelectedText(), txtPuerta.getText());
			txtPuerta.setText("F");
			// Ponemos una localidad incorrecta y comprobamos que se selecciona
			txtLocalidad.setText("<ninguna>");
			btnGuardar.click();
			assertEquals(jtxtLocalidad.getSelectedText(), txtLocalidad.getText());
			txtLocalidad.setText("Ciudad Real");
			// Ponemos un c�digo postal incorrecto y comprobamos que se selecciona
			txtCP.setText("130000");
			btnGuardar.click();
			assertEquals(jtxtCP.getSelectedText(), txtCP.getText());
			txtCP.setText("12345");
			// Ponemos una provincia incorrecta y comprobamos que se selecciona
			txtProvincia.setText("900");
			btnGuardar.click();
			assertEquals(jtxtProvincia.getSelectedText(), txtProvincia.getText());
			txtProvincia.setText("Ciudad Real");
			// Ponemos un correo incorrecto y comprobamos que se selecciona
			txtCorreo.setText("pjs80@gmail");
			btnGuardar.click();
			assertTrue(jtxtCorreo.getSelectionStart() == 0 && jtxtCorreo.getSelectionEnd() == txtCorreo.getText().length());
			txtCorreo.setText("pjs80@gmail.com");
			// Ponemos un tel�fono fijo incorrecto y comprobamos que se selecciona
			txtTelefonoFijo.setText("926 147 130");
			btnGuardar.click();
			assertTrue(jtxtTelefonoFijo.getSelectionStart() == 0 && jtxtTelefonoFijo.getSelectionEnd() == txtTelefonoFijo.getText().length());
			txtTelefonoFijo.setText("926147130");
			// Ponemos un tel�fono m�vil incorrecto y comprobamos que se selecciona
			txtTelefonoMovil.setText("61011122");
			btnGuardar.click();
			assertTrue(jtxtTelefonoMovil.getSelectionStart() == 0 && jtxtTelefonoMovil.getSelectionEnd() == txtTelefonoMovil.getText().length());
			txtTelefonoMovil.setText("626405060");
			// Ponemos un centro de salud incorrecto y comprobamos que se produce un error
			jcmbCentros.grabFocus();
			jcmbCentros.setSelectedIndex(-1);
			btnGuardar.click();
			assertTrue(cmbCentros.selectionEquals(""));
			jcmbCentros.setSelectedIndex(0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testBuscarBeneficiarioPorNIF () {
		// Probamos con el NIF de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		btnBuscar.click();
		// Para saber que se ha encontrado con �xito, comprobamos que los campos txtNIF y txtNSS no son vacios
		assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
		assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
	}
	
	public void testBuscarBeneficiarioPorNSS () {
		// Probamos con el NIF de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(1);
		txtIdentificacion.setText(beneficiarioPrueba.getNss());
		btnBuscar.click();
		// Para saber que se ha encontrado con �xito, comprobamos que los campos txtNIF y txtNSS no son vacios
		assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
		assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
	}
	
	public void testActualizarBeneficiario () {
		String nuevoNombre = "nombre nuevo del beneficiario";
		try {
			// Buscamos beneficiarioPrueba por su NIF
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			btnBuscar.click();
			// Para saber que se ha encontrado con �xito, comprobamos que los campos txtNIF y txtNSS no son vacios
			assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
			assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
			// Comprobamos que, inicalmente, el checkbox Editar est� habilitado y no est� seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el bot�n Guardar no est� habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuaci�n se pasar� a tratar de editar el beneficiario
			assertFalse(chkEditar.isSelected());
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Editamos el nombre del beneficiario
			txtNombre.setText(nuevoNombre);
			// Comprobamos que el boton de Guardar ahora s� est� habilitado
			assertTrue(btnGuardar.isEnabled());
			// Guardamos el beneficiario
			btnGuardar.click();
			comprobarCamposVacios();
			// Comprobamos que el beneficiario ha sido modificado correctamente
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			btnBuscar.click();
			// Comprobamos que en el textbox del nombre sale nuevoNombre
			assertEquals(nuevoNombre, txtNombre.getText());
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	public void testEliminarBeneficiario () {
		try {
			// Buscamos beneficiarioPrueba por su NIF
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			btnBuscar.click();
			// Para saber que se ha encontrado con �xito, comprobamos que los campos txtNIF y txtNSS no son vacios
			assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
			assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
			// Comprobamos que, inicalmente, el checkbox Editar est� habilitado y no est� seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el bot�n Eliminar no est� habilitado
			assertFalse(btnEliminar.isEnabled());
			// A continuaci�n se pasar� a tratar de editar el beneficiario
			assertFalse(chkEditar.isSelected());
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Eliminar ahora s� est� habilitado
			assertTrue(btnEliminar.isEnabled());
			// Eliminamos el beneficiario confirmando la operaci�n en el di�logo de confirmaci�n
			WindowInterceptor.init(btnEliminar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			eliminado = true;
			comprobarCamposVacios();
			// Comprobamos que el beneficiario ha sido eliminado correctamente
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			WindowInterceptor.init(btnBuscar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					assertTrue(window.titleContains("Error"));
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Como no se encuentra al usuario, el boton Editar tiene que estar deshabilitado
			assertFalse(chkEditar.isEnabled());
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
					controlador.consultarBeneficiarioPorNIF(nif);
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