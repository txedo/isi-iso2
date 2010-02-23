package pruebas;

import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.ComboBox;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPUsuarioConsultar;
import presentacion.auxiliar.Validacion;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;

public class PruebasJPusuarioConsultar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas {

	private ControladorCliente controlador;
	private JPUsuarioConsultar panel;
	private Panel pnlPanel;
	private TextBox txtNIFBuscado;
	private TextBox txtNIF;
	private TextBox txtNombre;
	private TextBox txtApellidos;
	private TextBox txtLogin;
	private PasswordField txtPassword;
	private PasswordField txtPasswordConf;
	private TextBox txtCorreoElectronico;
	private TextBox txtTelefonoFijo;
	private TextBox txtTelefonoMovil;
	private ComboBox cmbRol;
	private TextBox txtCentro;
	private TextBox txtEspecialidad;
	private Button btnCalendario;
	private Button btnGuardar;
	private Button btnEliminar;
	private Button btnBuscar;
	private CheckBox chkEditar;
	private JTextField jtxtNIFBuscado;
	private JTextField jtxtNIF;
	private JTextField jtxtNombre;
	private JTextField jtxtApellidos;
	private JTextField jtxtLogin;
	private JPasswordField jtxtPassword;
	private JPasswordField jtxtPasswordConf;
	private JTextField jtxtCorreoElectronico;
	private JTextField jtxtTelefonoFijo;
	private JTextField jtxtTelefonoMovil;
	private JComboBox jcmbRol;
	private JTextField jtxtCentro;
	private JTextField jtxtEspecialidad;
	private Window winPrincipal;
	
	private Administrador admin;
	private Citador citador;
	private Medico cabecera, especialista, pediatra;
	private TipoMedico tPediatra, tCabecera, tEspecialista;
	private CentroSalud centro;
	private PeriodoTrabajo periodo1, periodo2;
	
	protected void setUp() {
		try {
			// Establecemos conexión con el servidor front-end
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
			// Creamos e insertamos usuarios de prueba, para poder consultarlos
			admin = new Administrador(generarNIF(), "as110", Encriptacion.encriptarPasswordSHA1("admin"), "administrador", "administra", "", "", "");
			citador = new Citador(generarNIF(), "citador121", Encriptacion.encriptarPasswordSHA1("citador"), "citador", "cita", "adad@gmail.com", "912312312", "612131212");
			tCabecera = new Cabecera();
			tEspecialista = new Especialista("Neurologia");
			tPediatra = new Pediatra();
			centro = new CentroSalud("Centro Provincial", "Calle Ninguna, s/n");
			cabecera = new Medico(generarNIF(), "medCabecera", Encriptacion.encriptarPasswordSHA1("medCabecera"), "Eduardo", "P. C.", "", "", "", tPediatra);
			pediatra = new Medico(generarNIF(), "medPediatra", Encriptacion.encriptarPasswordSHA1("medPediatra"), "Carmen", "G. G.", "carmen@gmail.com", "", "", tCabecera);
			especialista = new Medico(generarNIF(), "medEspecialista", Encriptacion.encriptarPasswordSHA1("medEspecialista"), "Juan", "P. F.", "asdsad@yahoo.es", "987654321", "678901234", tEspecialista);
			cabecera.setCentroSalud(centro);
			pediatra.setCentroSalud(centro);
			especialista.setCentroSalud(centro);
			citador.setCentroSalud(centro);
			admin.setCentroSalud(centro);
			periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Miercoles);
			cabecera.getCalendario().add(periodo1);
			pediatra.getCalendario().add(periodo1);
			especialista.getCalendario().add(periodo1);
			
			controlador.crearUsuario(admin);
			controlador.crearUsuario(citador);
			controlador.crearMedico(cabecera);
			controlador.crearMedico(pediatra);
			controlador.crearMedico(especialista);
			
			// Creamos el panel
			panel = new JPUsuarioConsultar(controlador.getVentanaPrincipal(), controlador);
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panel);
			txtNIFBuscado = pnlPanel.getTextBox("txtNIFBuscado");
			txtNIF = pnlPanel.getTextBox("txtNIF");
			txtNombre = pnlPanel.getTextBox("txtNombre");
			txtApellidos = pnlPanel.getTextBox("txtApellidos");
			txtLogin = pnlPanel.getTextBox("txtLogin");
			txtPassword = pnlPanel.getPasswordField("txtPassword");
			txtPasswordConf = pnlPanel.getPasswordField("txtPasswordConf");
			txtCorreoElectronico = pnlPanel.getTextBox("txtCorreoElectronico");
			txtTelefonoFijo = pnlPanel.getTextBox("txtTelefonoFijo");
			txtTelefonoMovil = pnlPanel.getTextBox("txtTelefonoMovil");
			cmbRol = pnlPanel.getComboBox("cmbRol");
			txtCentro = pnlPanel.getTextBox("txtCentro");
			txtEspecialidad = pnlPanel.getTextBox("txtEspecialidad");
			btnCalendario = pnlPanel.getButton("btnCalendario");
			btnGuardar = pnlPanel.getButton("btnGuardar");
			btnEliminar = pnlPanel.getButton("btnEliminar");
			btnBuscar = pnlPanel.getButton("btnbuscar");
			chkEditar = pnlPanel.getCheckBox("chkEditar");
			
			jtxtNIFBuscado = (JTextField)txtNIFBuscado.getAwtComponent();
			jtxtNIF = (JTextField)txtNIF.getAwtComponent();
			jtxtNombre = (JTextField)txtNombre.getAwtComponent();
			jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
			jtxtLogin = (JTextField)txtLogin.getAwtComponent();
			jtxtPassword = (JPasswordField)txtPassword.getAwtComponent();
			jtxtPasswordConf = (JPasswordField)txtPasswordConf.getAwtComponent();
			jtxtCorreoElectronico = (JTextField)txtCorreoElectronico.getAwtComponent();
			jtxtTelefonoFijo = (JTextField)txtTelefonoFijo.getAwtComponent();
			jtxtTelefonoMovil = (JTextField)txtTelefonoMovil.getAwtComponent();
			jcmbRol = (JComboBox)cmbRol.getAwtComponent();
			jtxtCentro = (JTextField)txtCentro.getAwtComponent();
			jtxtEspecialidad = (JTextField)txtEspecialidad.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Eliminamos objetos de prueba
			controlador.eliminarUsuario(admin);
			controlador.eliminarUsuario(citador);
			controlador.eliminarMedico(cabecera);
			controlador.eliminarMedico(pediatra);
			controlador.eliminarMedico(especialista);
			// Cerramos la sesión y la ventana del controlador
			controlador.cerrarSesion();
			winPrincipal.dispose();
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/** Pruebas con datos no válidos */
	public void testDatosInvalidos() {			
		try {
			// Ponemos un NIF nulo
			txtNIFBuscado.setText("");
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inválido
			txtNIFBuscado.setText("111111");
			btnBuscar.click();
			assertEquals(jtxtNIFBuscado.getSelectedText(), txtNIFBuscado.getText());
			// Probamos con un NIF que no esté dado de alta en el sistema
			txtNIFBuscado.setText("00000000a");
			btnBuscar.click();
			assertEquals(jtxtNIFBuscado.getSelectedText(), txtNIFBuscado.getText());
			// Ponemos un NIF correcto
			txtNIFBuscado.setText(admin.getDni());
			btnBuscar.click();
			assertTrue(txtNIFBuscado.getText().equals(""));
			assertTrue(txtNIF.getText().equals(admin.getDni()));
			// Intentamos modificar el usuario
			assertFalse(chkEditar.isSelected());
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Los campos NIF y centro no son editables
			assertTrue(!txtNIF.isEditable().isTrue());
			assertTrue(!txtCentro.isEditable().isTrue());
			// Escribimos un nombre incorrecto y comprobamos que se selecciona
			// Ponemos un nombre incorrecto y comprobamos que se selecciona
			txtNombre.setText("Pedro$");
			btnGuardar.click();
			assertEquals(jtxtNombre.getSelectedText(), txtNombre.getText());
			txtNombre.setText("Pedro");
			// Ponemos unos apellidos incorrectos y comprobamos que se seleccionan
			txtApellidos.setText("---");
			btnGuardar.click();
			assertEquals(jtxtApellidos.getSelectedText(), txtApellidos.getText());
			txtApellidos.setText("Jiménez Serrano");
			// Ponemos un nombre de usuario incorrecto y comprobamos que se selecciona
			txtLogin.setText("admin- ");
			btnGuardar.click();
			assertEquals(jtxtLogin.getSelectedText(), txtLogin.getText());
			txtLogin.setText("admin87");
			// Ponemos una contraseña invalida y comprobamos que se selecciona
			/* 
			// TODO: revisar porque no coinciden los campos de las contraseñas
			jtxtPassword.setText("123456");
			btnGuardar.click();
			assertEquals(jtxtPassword.getSelectedText(), new String(jtxtPassword.getPassword()));
			jtxtPassword.setText("12345678");
			// Comprobamos que al no coincidir las contraseñas, se selecciona la primera contraseña
			jtxtPasswordConf.setText("123456");
			btnGuardar.click();
			assertEquals(jtxtPassword.getSelectedText(), new String(jtxtPassword.getPassword()));*/
			jtxtPassword.setText("12345678");
			jtxtPasswordConf.setText("12345678");
			// Probamos un e-mail invalido y comprobamos que se selecciona (este campo es opcional)
			txtCorreoElectronico .setText("pjs80@gmail");
			btnGuardar.click();
			assertTrue(jtxtCorreoElectronico.getSelectionStart() == 0 && jtxtCorreoElectronico.getSelectionEnd() == txtCorreoElectronico.getText().length());
			txtCorreoElectronico.setText("pjs80@gmail.com");
			// Ponemos un teléfono fijo incorrecto y comprobamos que se selecciona
			txtTelefonoFijo.setText("926 147 130");
			btnGuardar.click();
			assertTrue(jtxtTelefonoFijo.getSelectionStart() == 0 && jtxtTelefonoFijo.getSelectionEnd() == txtTelefonoFijo.getText().length());
			txtTelefonoFijo.setText("926147130");
			// Ponemos un teléfono móvil incorrecto y comprobamos que se selecciona
			txtTelefonoMovil.setText("61011122");
			btnGuardar.click();
			assertTrue(jtxtTelefonoMovil.getSelectionStart() == 0 && jtxtTelefonoMovil.getSelectionEnd() == txtTelefonoMovil.getText().length());
			txtTelefonoMovil.setText("626405060");
			// Como se ha buscado un administrador, intentamos cambiar su rol a uno no válido
			jcmbRol.grabFocus();
			jcmbRol.setSelectedIndex(-1);
			btnGuardar.click();
			assertTrue(txtNIF.getText().length()!=0);
		} catch(Exception e) {
			fail(e.toString());
		}
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

}
