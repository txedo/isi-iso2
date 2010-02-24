package pruebas;

import java.awt.Frame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JFLogin;
import dominio.control.ControladorCliente;
import excepciones.LoginIncorrectoException;

public class PruebasJFLogin extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantes {

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
	
	// Intenta loguear un usuario incorrecto con la IP y el puerto del servidor por defecto
	public void testLoginUsuarioIncorrecto () {
		String [] login = { "", " ", "sdkjafdskjdsañfka" };
		String [] password = login.clone();
		
		for (int i = 0; i < login.length; i++) {
			for (int j = 0; j < password.length; j++) {
				txtUsuario.setText(login[i]);
				txtPassword.setPassword(password[j]);
				/*
				WindowInterceptor.init(btnConectar.triggerClick()).process(new WindowHandler() {
					public Trigger process (Window window) {
						// TODO Comprobar que es el error de LoginIncorrectoException
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				*/
				btnConectar.click();
				// TODO interceptar la excepcion
				// Comprobamos que no ha cambiado nada
				assertEquals(txtUsuario.getText(), login[i]);
				assertTrue(txtPassword.passwordEquals(password[j]));
			}
		}
	}
	
	// Direccion IP del servidor inválida
	public void testIPInvalida () {
		String [] direcciones = { "", " ", "aaaa", "-441", "213", "0.0.0", "127.0.0.0.0" };
		txtUsuario.setText(usuarioAdmin);
		txtPassword.setPassword(passwordAdmin);
		// Abrimos las opciones avanzadas
		btnAvanzado.click();
		for (int i = 0; i < direcciones.length; i++) {
			// Ponemos una IP inválida
			txtDireccionServidor.setText(direcciones[i]);
			// Intentamos conectar
			btnConectar.click();
			// TODO interceptar la excepcion
			// Comprobamos que no ha cambiado nada
			assertEquals(txtUsuario.getText(), usuarioAdmin);
			assertTrue(txtPassword.passwordEquals(passwordAdmin));
		}
	}
	
	// Puerto inválido
	public void testPuertoInvalido () {
		String [] puertos = { "", " ", "aaaa", "-441", "213", "0.0.0", "127.0.0.0.0" };
		txtUsuario.setText(usuarioAdmin);
		txtPassword.setPassword(passwordAdmin);
		// Abrimos las opciones avanzadas
		btnAvanzado.click();
		for (int i = 0; i < puertos.length; i++) {
			// Ponemos un puerto inválido
			txtPuertoServidor.setText("-5");
			// Intentamos conectar
			btnConectar.click();
			// TODO interceptar la excepcion
			// Comprobamos que no ha cambiado nada
			assertEquals(txtUsuario.getText(), usuarioAdmin);
			assertTrue(txtPassword.passwordEquals(passwordAdmin));
		}
	}
	
	// Servidor no alcanzable
	public void testServidorInalcanzable () {
		txtUsuario.setText(usuarioAdmin);
		txtPassword.setPassword(passwordAdmin);
		// Abrimos las opciones avanzadas
		btnAvanzado.click();
		// Ponemos una IP en la que no hay un servidor a la escucha
		txtDireccionServidor.setText("1.0.0.1");
		// Intentamos conectar
		WindowInterceptor.init(btnConectar.triggerClick()).process(new WindowHandler() {
			public Trigger process (Window window) {
				// TODO Comprobar que es el error de LoginIncorrectoException
				return window.getButton(OK_OPTION).triggerClick();
			}
		}).run();
		// TODO interceptar la excepcion
		// Comprobamos que no ha cambiado nada
		assertEquals(txtUsuario.getText(), usuarioAdmin);
		assertTrue(txtPassword.passwordEquals(passwordAdmin));
	}
	
	// Loguea un usuario con la IP y el puerto del servidor por defecto
	public void testLoginUsuarioCorrecto () {
		txtUsuario.setText(usuarioAdmin);
		txtPassword.setPassword(passwordAdmin);
		winPrincipal = WindowInterceptor.run(btnConectar.triggerClick());
		winPrincipal.assertTitleEquals("SSCA - Unidad de Citación");
		sesionIniciada = true;
	}
	
	public void testOpcionesAvanzadas () {
		// Comprobamos que las opciones avanzadas están ocultas y deshabilitadas
		assertFalse(pnlDatosServidor.isVisible());
		assertFalse(txtDireccionServidor.isEnabled());
		assertFalse(txtDireccionServidor.isEditable());
		assertFalse(txtDireccionServidor.isVisible());
		assertFalse(txtPuertoServidor.isEnabled());
		assertFalse(txtPuertoServidor.isEditable());
		assertFalse(txtPuertoServidor.isVisible());
		// Mostramos las opciones avanzadas
		btnAvanzado.click();
		// Comprobamos que los datos del servidor son visibles y están habilitadas
		assertTrue(pnlDatosServidor.isVisible());
		assertTrue(txtDireccionServidor.isEnabled());
		assertTrue(txtDireccionServidor.isEditable());
		assertTrue(txtDireccionServidor.isVisible());
		assertTrue(txtPuertoServidor.isEnabled());
		assertTrue(txtPuertoServidor.isEditable());
		assertTrue(txtPuertoServidor.isVisible());
		// Ocultamos de nuevo las opciones avanzadas
		btnAvanzado.click();
		// Comprobamos que las opciones avanzadas están ocultas y deshabilitadas
		assertFalse(pnlDatosServidor.isVisible());
		assertFalse(txtDireccionServidor.isEnabled());
		assertFalse(txtDireccionServidor.isEditable());
		assertFalse(txtDireccionServidor.isVisible());
		assertFalse(txtPuertoServidor.isEnabled());
		assertFalse(txtPuertoServidor.isEditable());
		assertFalse(txtPuertoServidor.isVisible());
	}
}
