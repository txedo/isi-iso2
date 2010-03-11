package pruebas;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;
import dominio.control.ControladorCliente;

/**
 * Pruebas de la ventana de identificación de usuarios.
 */
public class PruebasJFLogin extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {

	private boolean sesionIniciada;
	private ControladorCliente controlador;

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
		String [] login = { "sdkjafdskjdska", "eoisek27" };
		String [] password = login.clone();
		String mensaje;
		
		for (int i = 0; i < login.length; i++) {
			for (int j = 0; j < password.length; j++) {
				txtUsuario.setText(login[i]);
				txtPassword.setPassword(password[j]);
				// Pulsamos el botón Conectar y capturamos el texto de la ventana de error
				mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnConectar, OK_OPTION);
				String esperado = "El nombre de usuario o contraseña introducidos no son válidos.";
				assertEquals(esperado, mensaje);
			}
		}
	}
	
	// Direccion IP del servidor inválida
	public void testIPInvalida () {
		String [] direcciones = { "", " ", "aaaa", "-441", "213", "0.0.0", "127.0.0.0.0" };
		String mensaje;
		
		txtUsuario.setText(USUARIO_ADMIN);
		txtPassword.setPassword(PASSWORD_ADMIN);
		// Abrimos las opciones avanzadas
		btnAvanzado.click();
		for (int i = 0; i < direcciones.length; i++) {
			// Ponemos una IP inválida
			txtDireccionServidor.setText(direcciones[i]);
			// Pulsamos el botón Conectar y capturamos el texto de la ventana de error
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnConectar, OK_OPTION);
			String esperado = "La dirección IP del servidor tiene un formato incorrecto.";
			assertEquals(esperado, mensaje);
		}
	}
	
	// Puerto inválido
	public void testPuertoInvalido () {
		String [] puertos = { "", " ", "aaaa", "-441", "0.0.0", "127.0.0.0.0" };
		String mensaje;
		
		txtUsuario.setText(USUARIO_ADMIN);
		txtPassword.setPassword(PASSWORD_ADMIN);
		// Abrimos las opciones avanzadas
		btnAvanzado.click();
		for (int i = 0; i < puertos.length; i++) {
			// Ponemos un puerto inválido
			txtPuertoServidor.setText(puertos[i]);
			// Pulsamos el botón Conectar y capturamos el texto de la ventana de error
			mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnConectar, OK_OPTION);
			String esperado = "El puerto del servidor tiene un formato incorrecto.";
			assertEquals(esperado, mensaje);
		}
	}
	
	// Servidor no alcanzable
	public void testServidorInalcanzable () {
		String mensaje;
		
		txtUsuario.setText(USUARIO_ADMIN);
		txtPassword.setPassword(PASSWORD_ADMIN);
		// Abrimos las opciones avanzadas
		btnAvanzado.click();
		// Ponemos una IP en la que no hay un servidor a la escucha
		txtDireccionServidor.setText("1.0.0.1");
		// Pulsamos el botón Conectar y capturamos el texto de la ventana de error
		mensaje = UtilidadesPruebas.obtenerTextoDialogo(btnConectar, OK_OPTION);
		String esperado = "No se puede conectar con el servidor front-end (IP " + txtDireccionServidor.getText() + ", puerto " + txtPuertoServidor.getText() + ").";
		assertEquals(esperado, mensaje);
	}
	
	// Loguea un usuario con la IP y el puerto del servidor por defecto
	@SuppressWarnings("deprecation")
	public void testLoginUsuarioCorrecto () {
		txtUsuario.setText(USUARIO_ADMIN);
		txtPassword.setPassword(PASSWORD_ADMIN);
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
