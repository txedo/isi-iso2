package pruebas;

import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JFLogin;
import dominio.control.ControladorCliente;

public class PruebasJFLogin extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas {

	private boolean sesionIniciada;
	private ControladorCliente controlador;
	private JFLogin frmLogin;

	private Frame frmPrincipal;
	private Panel pnlPanel;
	private Panel pnlDatosUsuario;
	private TextBox txtUsuario;
	private PasswordField txtPassword;
	private Panel pnlDatosServidor;
	private TextBox txtDireccionServidor;
	private TextBox txtPuertoServidor;
	private Button btnConectar;
	private Button btnAvanzado;
	private Window winLogin;
	private Window winPrincipal;

	private JPanel jpnlDatosUsuario;
	private JTextField jtxtUsuario;
	private JPasswordField jtxtPassword;
	private JPanel jpnlDatosServidor;
	private JTextField jtxtDireccionServidor;
	private JTextField jtxtPuertoServidor;
	
	protected void setUp () {
		try {
			// Establecemos conexión con el servidor front-end
			controlador = new ControladorCliente();
			winLogin = WindowInterceptor.run(new Trigger() {
				public void run() {
					controlador.identificarse();
				}
			});
			pnlPanel = winLogin.getPanel("pnlPanel");
			pnlDatosUsuario = pnlPanel.getPanel("pnlDatosUsuario");
			txtUsuario = pnlDatosUsuario.getTextBox("txtUsuario");
			txtPassword = pnlDatosUsuario.getPasswordField("txtPassword");
			pnlDatosServidor = pnlPanel.getPanel("pnlDatosServidor");
			txtDireccionServidor = pnlDatosServidor.getTextBox("txtDireccionServidor");
			txtPuertoServidor = pnlDatosServidor.getTextBox("txtPuertoServidor");
			btnConectar = pnlPanel.getButton("btnConectar");
			btnAvanzado = pnlPanel.getButton("btnAvanzado");
			
			jpnlDatosUsuario = (JPanel)pnlDatosUsuario.getAwtComponent();
			jtxtUsuario = (JTextField)txtUsuario.getAwtComponent();
			jtxtPassword = (JPasswordField)txtPassword.getAwtComponent();
			jpnlDatosServidor = (JPanel)pnlDatosServidor.getAwtComponent();
			jtxtDireccionServidor = (JTextField)txtDireccionServidor.getAwtComponent();
			jtxtPuertoServidor = (JTextField)txtPuertoServidor.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown () {
		try {
			if (sesionIniciada) controlador.cerrarSesion();
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	// Loguea un usuario con la IP y el puerto del servidor por defecto
	public void testLoginUsuarioDefault () {
		txtUsuario.setText(usuarioAdmin);
		txtPassword.setPassword(passwordAdmin);
		winPrincipal = WindowInterceptor.run(btnConectar.triggerClick());
		winPrincipal.assertTitleEquals("SSCA - Unidad de Citación");
		sesionIniciada = true;
	}
}
