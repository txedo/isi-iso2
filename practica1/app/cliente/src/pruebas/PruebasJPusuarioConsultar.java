package pruebas;

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

import presentacion.JPUsuarioRegistrar;
import dominio.control.ControladorCliente;

public class PruebasJPusuarioConsultar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas {

	private ControladorCliente controlador;
	private JPUsuarioRegistrar panel;
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
	private ListBox lstTipoUsuario;
	private ListBox lstTipoMedico;
	private ComboBox cmbEspecialidad;
	private Button btnCalendario;
	private Button btnGuardar;
	private Button btnEliminar;
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
	private JList jlstTipoUsuario;
	private JList jlstTipoMedico;
	private JComboBox jcmbEspecialidad;
	private Window winPrincipal;
	
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
			// Creamos el panel
			panel = new JPUsuarioRegistrar(controlador.getVentanaPrincipal(), controlador);
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
			lstTipoUsuario = pnlPanel.getListBox("lstTipoUsuario");
			lstTipoMedico = pnlPanel.getListBox("lstTipoMedico");
			cmbEspecialidad = pnlPanel.getComboBox("cmbEspecialidad");
			btnCalendario = pnlPanel.getButton("btnCalendario");
			btnGuardar = pnlPanel.getButton("btnGuardar");
			btnEliminar = pnlPanel.getButton("btnEliminar");
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
			jlstTipoUsuario = (JList)lstTipoUsuario.getAwtComponent();
			jlstTipoMedico = (JList)lstTipoMedico.getAwtComponent();
			jcmbEspecialidad = (JComboBox)cmbEspecialidad.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesión y la ventana del controlador
			controlador.cerrarSesion();
			winPrincipal.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
