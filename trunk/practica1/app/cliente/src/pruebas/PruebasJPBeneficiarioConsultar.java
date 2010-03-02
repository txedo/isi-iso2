package pruebas;

import java.util.Date;
import java.util.Vector;

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

import com.toedter.calendar.JDateChooser;
import comunicaciones.ConfiguracionCliente;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CodigoPostalIncorrectoException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.FechaNacimientoIncorrectaException;
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
import excepciones.UsuarioYaExistenteException;

public class PruebasJPBeneficiarioConsultar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {
	
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
	private TextBox txtMedicoAsignado;
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
	
	private boolean eliminadoBeneficiario, eliminadoMedico, valido;
	private TipoMedico tCabecera;
	private Medico cabecera;
	private String login;
	private PeriodoTrabajo periodo1;
	
	private Vector<Medico> medicosEliminados;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		medicosEliminados = new Vector<Medico>();
		eliminadoBeneficiario = false;
		eliminadoMedico = false;
		valido = false;
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
			
			// Creamos un médico de cabecera 
			tCabecera = new Cabecera();
			login = UtilidadesPruebas.generarLogin();
			cabecera = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Eduardo", "PC", "", "", "", tCabecera);
			periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Miercoles);
			cabecera.getCalendario().add(periodo1);		
			// Mientras exista el usuario, se genera otro login y otro NIF
			do {
				try {
					controlador.crearUsuario(cabecera);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					cabecera.setNif(UtilidadesPruebas.generarNIF());
					login = UtilidadesPruebas.generarLogin();
					cabecera.setLogin(login);
					cabecera.setPassword(login);
					valido = false;
				}
			}while(!valido);
			
			// Consultamos el médico de nuevo, porque el centro de salud que realmente se le asigna
			// se hace de manera aleatoria
			cabecera = controlador.consultarMedico(cabecera.getNif());

			// Creamos el beneficiario en el mismo centro que el médico, para que no se le asigne otro diferente 
			beneficiarioPrueba = new Beneficiario ();
			beneficiarioPrueba.setNif(UtilidadesPruebas.generarNIF());
			beneficiarioPrueba.setNss(UtilidadesPruebas.generarNSS());
			beneficiarioPrueba.setNombre("beneficiario");
			beneficiarioPrueba.setApellidos("de prueba");
			beneficiarioPrueba.setCorreo(" ");
			beneficiarioPrueba.setTelefono(" ");
			beneficiarioPrueba.setMovil(" ");
			beneficiarioPrueba.setFechaNacimiento(new Date("01/01/1980"));
			beneficiarioPrueba.setDireccion(new Direccion("lagasca", "", "", "", "Madrid", "Madrid", 28000));
			beneficiarioPrueba.setCentroSalud(cabecera.getCentroSalud());
			beneficiarioPrueba.setMedicoAsignado(cabecera);
			// Mientras exista el beneficiario, se genera otro NIF y otro NSS
			do {
				try {					
					controlador.crearBeneficiario(beneficiarioPrueba);
					valido = true;
				} catch (BeneficiarioYaExistenteException e) {
					beneficiarioPrueba.setNif(UtilidadesPruebas.generarNIF());					
					beneficiarioPrueba.setNss(UtilidadesPruebas.generarNSS());
					valido = false;
				}
			}while(!valido);
			
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
			txtMedicoAsignado = pnlPanel.getTextBox("txtMedicoAsignado");
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
			if (!eliminadoBeneficiario) controlador.eliminarBeneficiario(beneficiarioPrueba);
			for (Medico m : medicosEliminados) {
				controlador.crearMedico(m);
			}
			if (!eliminadoMedico) controlador.eliminarMedico(cabecera);
			// Cerramos la sesión y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
		} catch(Exception e) {
			fail(e.toString());
		}
		winPrincipal.dispose();
	}
	
	/** Pruebas con datos no válidos */
	public void testDatosInvalidos() {
		String[] invalidos;
		
		try {
			// Buscamos un beneficiario por su NIF
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			// Inicialmente probamos con un NIF nulo
			txtIdentificacion.setText("");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Debe introducir un NIF o un NSS.");
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inválido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// Probamos con un NIF que no esté dado de alta en el sistema
			txtIdentificacion.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "El beneficiario con NIF 00000000A no se encuentra dado de alta en el sistema.");
			// Buscamos un beneficiario por su NSS
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			// Ponemos un NSS incorrecto y comprobamos que el campo del
			// identificacion se selecciona por tener un formato inválido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NSSIncorrectoException().getMessage());
			// Probamos con un NSS que no esté dado de alta en el sistema
			txtIdentificacion.setText("000000000000");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "El beneficiario con NSS 000000000000 no se encuentra dado de alta en el sistema.");
			// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			// Para saber que se ha encontrado con éxito, comprobamos que los campos txtNIF y txtNSS no son vacios
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
			assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
			// A continuación se pasará a tratar de editar el beneficiario
			assertFalse(chkEditar.isSelected());
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Los campos NIF y NSS no son editables
			// Escribimos un nombre incorrecto y comprobamos que se selecciona
			txtNombre.setText("12341234");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new NombreIncorrectoException().getMessage());
			txtNombre.setText("Jose Manuel");
			// Escribimos un apellido incorrecto y comprobamos que se selecciona
			txtApellidos.setText("12341234");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new ApellidoIncorrectoException().getMessage());
			txtApellidos.setText("López García");
			// Ponemos fechas de nacimientos incorrectas y comprobamos que
			// se seleccionan (la validación de las fechas la hace en gran
			// medida el control JDateChooser en lugar de la clase
			// Validacion, por eso aquí la comprobación es más exhaustiva)
			invalidos = new String[] { "  ", "31/02/1980", "10/14/1980", "27/02/2020", "a/b/c", "01-01-1980" };
			for(String fecha : invalidos) {
				jtxtFechaNacimiento.setSelectionEnd(0);
				txtFechaNacimiento.setText(fecha);
				// Como hay varias fechas inválidas, el mensaje de excepción no siempre es el mismo
				btnGuardar.click();
				assertEquals(jtxtFechaNacimiento.getSelectedText(), txtFechaNacimiento.getText());
			}
			txtFechaNacimiento.setText("01/01/1980");
			// Ponemos un domicilio incorrecto y comprobamos que se selecciona
			txtDomicilio.setText("*");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new DomicilioIncorrectoException().getMessage());
			txtDomicilio.setText("C/Cervantes");
			// Ponemos un número incorrecto y comprobamos que se selecciona
			txtNumero.setText("10 A");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new NumeroDomicilioIncorrectoException().getMessage());
			txtNumero.setText("38");
			// Ponemos un piso incorrecto y comprobamos que se selecciona
			txtPiso.setText("4ºF");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new PisoDomicilioIncorrectoException().getMessage());
			txtPiso.setText("4");
			// Ponemos una puerta incorrecta y comprobamos que se selecciona
			txtPuerta.setText("5");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new PuertaDomicilioIncorrectoException().getMessage());
			txtPuerta.setText("F");
			// Ponemos una localidad incorrecta y comprobamos que se selecciona
			txtLocalidad.setText("<ninguna>");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new LocalidadIncorrectaException().getMessage());
			txtLocalidad.setText("Ciudad Real");
			// Ponemos un código postal incorrecto y comprobamos que se selecciona
			txtCP.setText("130000");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new CodigoPostalIncorrectoException().getMessage());
			txtCP.setText("12345");
			// Ponemos una provincia incorrecta y comprobamos que se selecciona
			txtProvincia.setText("900");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new ProvinciaIncorrectaException().getMessage());
			txtProvincia.setText("Ciudad Real");
			// Ponemos un correo incorrecto y comprobamos que se selecciona
			txtCorreo.setText("pjs80@gmail");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new CorreoElectronicoIncorrectoException().getMessage());
			txtCorreo.setText("pjs80@gmail.com");
			// Ponemos un teléfono fijo incorrecto y comprobamos que se selecciona
			txtTelefonoFijo.setText("926 147 130");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new TelefonoFijoIncorrectoException().getMessage());
			txtTelefonoFijo.setText("926147130");
			// Ponemos un teléfono móvil incorrecto y comprobamos que se selecciona
			txtTelefonoMovil.setText("61011122");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new TelefonoMovilIncorrectoException().getMessage());
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
		// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
		// Para saber que se ha encontrado con éxito, comprobamos que los campos txtNIF y txtNSS no son vacios
		assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
		assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
	}
	
	public void testBuscarBeneficiarioPorNSS () {
		// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(1);
		txtIdentificacion.setText(beneficiarioPrueba.getNss());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
		// Para saber que se ha encontrado con éxito, comprobamos que los campos txtNIF y txtNSS no son vacios
		assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
		assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
	}
	
	public void testBuscarBeneficiarioSinMedicoAsignado () {
		try {
			// Borramos todos los médicos de cabecera para que no se le asigne ninguno
			medicosEliminados = controlador.obtenerMedicosTipo(tCabecera);
			for (Medico m : medicosEliminados) {
				controlador.eliminarMedico(m);
			}
			eliminadoMedico = true;			
			// Consultamos el beneficiario
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			txtIdentificacion.setText(beneficiarioPrueba.getNss());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Para saber que se ha encontrado con éxito, comprobamos que los campos txtNIF y txtNSS no son vacios
			assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
			assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
			// En el textbox de médico, no debe aparecer ninguno
			assertEquals(txtMedicoAsignado.getText(), "(ninguno)");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testActualizarBeneficiario () {
		String nuevoNombre = "nombre nuevo del beneficiario";
		try {
			// Buscamos beneficiarioPrueba por su NIF
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Para saber que se ha encontrado con éxito, comprobamos que los campos txtNIF y txtNSS no son vacios
			assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
			assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el beneficiario
			assertFalse(chkEditar.isSelected());
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Editamos el nombre del beneficiario
			txtNombre.setText(nuevoNombre);
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Guardamos el beneficiario
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), "El beneficiario ha sido modificado correctamente.");
			// Comprobamos que el beneficiario ha sido modificado correctamente
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
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
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Para saber que se ha encontrado con éxito, comprobamos que los campos txtNIF y txtNSS no son vacios
			assertEquals(jtxtNIF.getText(), beneficiarioPrueba.getNif());
			assertEquals(jtxtNSS.getText(), beneficiarioPrueba.getNss());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Eliminar no está habilitado
			assertFalse(btnEliminar.isEnabled());
			// A continuación se pasará a tratar de editar el beneficiario
			assertFalse(chkEditar.isSelected());
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Eliminar ahora sí está habilitado
			assertTrue(btnEliminar.isEnabled());
			// Eliminamos el beneficiario confirmando la operación en el diálogo de confirmación
			assertEquals(UtilidadesPruebas.obtenerTextoSegundoDialogo(btnEliminar, YES_OPTION, OK_OPTION), "El beneficiario ha sido eliminado correctamente.");
			eliminadoBeneficiario = true;
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
	
}
