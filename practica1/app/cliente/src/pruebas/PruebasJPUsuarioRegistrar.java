package pruebas;

import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.ComboBox;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPUsuarioRegistrar;
import presentacion.auxiliar.Validacion;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;

public class PruebasJPUsuarioRegistrar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas {
			
		private ControladorCliente controlador;
		private JPUsuarioRegistrar panel;
		private Panel pnlPanel;
		private TextBox txtNIF;
		private TextBox txtNombre;
		private TextBox txtApellidos;
		private TextBox txtLogin;
		private PasswordField txtPassword;
		private PasswordField txtPasswordConf;
		private TextBox txtCorreoElectronico;
		private TextBox txtTelefonoFijo;
		private TextBox txtTelefonoMovil;
		private ListBox lstTipoUsuario;
		private ListBox lstTipoMedico;
		private ComboBox cmbEspecialidad;
		private Button btnCalendario;
		private Button btnCrearUsuario;
		private Button btnRestablecer;
		private JTextField jtxtNIF;
		private JTextField jtxtNombre;
		private JTextField jtxtApellidos;
		private JTextField jtxtLogin;
		private JPasswordField jtxtPassword;
		private JPasswordField jtxtPasswordConf;
		private JTextField jtxtCorreoElectronico;
		private JTextField jtxtTelefonoFijo;
		private JTextField jtxtTelefonoMovil;
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
				txtNIF = pnlPanel.getTextBox("txtNIF");
				txtNombre = pnlPanel.getTextBox("txtNombre");
				txtApellidos = pnlPanel.getTextBox("txtApellidos");
				txtLogin = pnlPanel.getTextBox("txtLogin");
				txtPassword = pnlPanel.getPasswordField("txtPassword");
				txtPasswordConf = pnlPanel.getPasswordField("txtPasswordConf");
				txtCorreoElectronico = pnlPanel.getTextBox("txtCorreoElectronico");
				txtTelefonoFijo = pnlPanel.getTextBox("txtTelefonoFijo");
				txtTelefonoMovil = pnlPanel.getTextBox("txtTelefonoMovil");
				lstTipoUsuario = pnlPanel.getListBox("lstTipoUsuario");
				lstTipoMedico = pnlPanel.getListBox("lstTipoMedico");
				cmbEspecialidad = pnlPanel.getComboBox("cmbEspecialidad");
				btnCalendario = pnlPanel.getButton("btnCalendario");
				btnCrearUsuario = pnlPanel.getButton("btnCrearUsuario");
				btnRestablecer = pnlPanel.getButton("btnRestablecer");
				
				jtxtNIF = (JTextField)txtNIF.getAwtComponent();
				jtxtNombre = (JTextField)txtNombre.getAwtComponent();
				jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
				jtxtLogin = (JTextField)txtLogin.getAwtComponent();
				jtxtPassword = (JPasswordField)txtPassword.getAwtComponent();
				jtxtPasswordConf = (JPasswordField)txtPasswordConf.getAwtComponent();
				jtxtCorreoElectronico = (JTextField)txtCorreoElectronico.getAwtComponent();
				jtxtTelefonoFijo = (JTextField)txtTelefonoFijo.getAwtComponent();
				jtxtTelefonoMovil = (JTextField)txtTelefonoMovil.getAwtComponent();
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

		/** Pruebas con datos no válidos */
		public void testDatosInvalidos() {
			String[] invalidos;
			
			try {
				// Ponemos un NIF incorrecto y comprobamos que el campo del
				// NIF se selecciona por tener un formato inválido
				txtNIF.setText("11223344");
				btnCrearUsuario.click();
				assertEquals(jtxtNIF.getSelectedText(), txtNIF.getText());
				txtNIF.setText("11223344B");
				// Ponemos un nombre incorrecto y comprobamos que se selecciona
				txtNombre.setText("Pedro$");
				btnCrearUsuario.click();
				assertEquals(jtxtNombre.getSelectedText(), txtNombre.getText());
				txtNombre.setText("Pedro");
				// Ponemos unos apellidos incorrectos y comprobamos que se seleccionan
				txtApellidos.setText("---");
				btnCrearUsuario.click();
				assertEquals(jtxtApellidos.getSelectedText(), txtApellidos.getText());
				txtApellidos.setText("Jiménez Serrano");
				// Ponemos un nombre de usuario incorrecto y comprobamos que se selecciona
				txtLogin.setText("admin- ");
				btnCrearUsuario.click();
				assertEquals(jtxtLogin.getSelectedText(), txtLogin.getText());
				txtLogin.setText("admin87");
				// Ponemos una contraseña invalida y comprobamos que se selecciona
				//txtPassword.setPassword("123456");
				//btnCrearUsuario.click();
				// TODO: ¿como comprobar que un campo de contraseña está seleccionado?
				//assertEquals(new String(jtxtPassword.getSelectedText(), txtPassword.getText());
				txtPassword.setPassword("12345678");
				// Comprobamos que al no coincidir las contraseñas, se selecciona la primera contraseña
				//txtPasswordConf.setPassword("123456");
				//btnCrearUsuario.click();
				//assertEquals(new String(jtxtPassword.getSelectedText(), txtPassword.getText());
				txtPasswordConf.setPassword("12345678");
				// Probamos un e-ail invalido y comprobamos que se selecciona (este campo es opcional)
				txtCorreoElectronico .setText("pjs80@gmail");
				btnCrearUsuario.click();
				assertTrue(jtxtCorreoElectronico.getSelectionStart() == 0 && jtxtCorreoElectronico.getSelectionEnd() == txtCorreoElectronico.getText().length());
				txtCorreoElectronico.setText("pjs80@gmail.com");
				// Ponemos un teléfono fijo incorrecto y comprobamos que se selecciona
				txtTelefonoFijo.setText("926 147 130");
				btnCrearUsuario.click();
				assertTrue(jtxtTelefonoFijo.getSelectionStart() == 0 && jtxtTelefonoFijo.getSelectionEnd() == txtTelefonoFijo.getText().length());
				txtTelefonoFijo.setText("926147130");
				// Ponemos un teléfono móvil incorrecto y comprobamos que se selecciona
				txtTelefonoMovil.setText("61011122");
				btnCrearUsuario.click();
				assertTrue(jtxtTelefonoMovil.getSelectionStart() == 0 && jtxtTelefonoMovil.getSelectionEnd() == txtTelefonoMovil.getText().length());
				txtTelefonoMovil.setText("626405060");
				// Ponemos un tipo de usuario inválido y comprobamos que se produce un error
				lstTipoUsuario.selectIndex(-1);
				btnCrearUsuario.click();
				assertTrue(txtNIF.getText().length() != 0);
				lstTipoUsuario.selectIndex(2);
				assertTrue(lstTipoMedico.isVisible().isTrue());
				// Seleccionamos un tipo de medico inválido y comprobamos que se produce un error
				lstTipoMedico.selectIndex(-1);
				btnCrearUsuario.click();
				assertTrue(txtNIF.getText().length() != 0);
				lstTipoMedico.selectIndex(2);
				// Seleccionamos una especialidad invalida y se comprueba que se produce un error
				assertTrue(cmbEspecialidad.isVisible().isTrue());
				jcmbEspecialidad.grabFocus();
				jcmbEspecialidad.setSelectedIndex(-1);
				assertTrue(txtNIF.getText().length() != 0);
				// TODO: ¿como probar el botón del calendario?
			} catch(Exception e) {
				fail(e.toString());
			}
		}

		/** Pruebas con datos válidos para registrar un administrador **/
		@SuppressWarnings("deprecation")
		public void testDatosValidosAdministrador() {
			String nif = "";
			String login = "";
			Random r = new Random();
			
			try {
				do {
					// Creamos un usuario administrador con todos los datos válidos
					nif = generarNIF();
					login = "usuario" + String.valueOf(r.nextInt(100));
					txtNIF.setText(nif);
					txtNombre.setText("Pedro");
					txtApellidos.setText("Jiménez Serrano");
					txtLogin.setText(login);
					txtPassword.setPassword("administrador");
					txtPasswordConf.setPassword("administrador");
					txtCorreoElectronico.setText("pjs80@gmail.com");
					txtTelefonoFijo.setText("926147130");
					txtTelefonoMovil.setText("626405060");
					lstTipoUsuario.selectIndex(0);
					assertTrue(!lstTipoMedico.isVisible().isTrue());
					assertTrue(!btnCalendario.isVisible().isTrue());
					assertTrue(!cmbEspecialidad.isVisible().isTrue());
					btnCrearUsuario.click();
				// Si no se han quedado los campos vacios, es porque ya existia el login
				} while (!txtNIF.getText().equals(""));
				comprobarCamposVacios();
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Intentamos crear un usuario con el mismo NIF
				// y comprobamos que los campos no se han borrado
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				lstTipoUsuario.selectIndex(0);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				btnCrearUsuario.click();
				assertEquals(txtNIF.getText(), nif);
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Intentamos crear un usuario con el mismo login
				// y comprobamos que los campos no se han borrado
				nif = generarNIF();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				lstTipoUsuario.selectIndex(0);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				btnCrearUsuario.click();
				assertEquals(txtNIF.getText(), nif);
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Creamos un usuario administrador con datos válidos omitiendo
				// el correo electrónico y los teléfonos
				do {
					nif = generarNIF();
					login = "usuario" + String.valueOf(r.nextInt(100));
					txtNIF.setText(nif);
					txtNombre.setText("Pedro  ");
					txtApellidos.setText("  Jiménez Serrano");
					txtLogin.setText(login);
					txtPassword.setPassword("administrador");
					txtPasswordConf.setPassword("administrador");
					txtCorreoElectronico.setText(" ");
					txtTelefonoFijo.setText(" ");
					txtTelefonoMovil.setText("  ");
					lstTipoUsuario.selectIndex(0);
					assertTrue(!lstTipoMedico.isVisible().isTrue());
					assertTrue(!btnCalendario.isVisible().isTrue());
					assertTrue(!cmbEspecialidad.isVisible().isTrue());
					btnCrearUsuario.click();
				} while (!txtNIF.getText().equals(""));
					comprobarCamposVacios();
			} catch(Exception e) {
				fail(e.toString());
			}
		}
		
		/** Pruebas con datos válidos para registrar un citador **/
		@SuppressWarnings("deprecation")
		public void testDatosValidosCitador() {
			String nif = "";
			String login = "";
			Random r = new Random();
			
			try {
				do {
					// Creamos un usuario citador con todos los datos válidos
					nif = generarNIF();
					login = "usuario" + String.valueOf(r.nextInt(100));
					txtNIF.setText(nif);
					txtNombre.setText("Pedro");
					txtApellidos.setText("Jiménez Serrano");
					txtLogin.setText(login);
					txtPassword.setPassword("administrador");
					txtPasswordConf.setPassword("administrador");
					txtCorreoElectronico.setText("pjs80@gmail.com");
					txtTelefonoFijo.setText("926147130");
					txtTelefonoMovil.setText("626405060");
					lstTipoUsuario.selectIndex(1);
					assertTrue(!lstTipoMedico.isVisible().isTrue());
					assertTrue(!btnCalendario.isVisible().isTrue());
					assertTrue(!cmbEspecialidad.isVisible().isTrue());
					btnCrearUsuario.click();
				// Si no se han quedado los campos vacios, es porque ya existia el login
				} while (!txtNIF.getText().equals(""));
				comprobarCamposVacios();
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Intentamos crear un usuario con el mismo NIF
				// y comprobamos que los campos no se han borrado
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				lstTipoUsuario.selectIndex(1);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				btnCrearUsuario.click();
				assertEquals(txtNIF.getText(), nif);
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Intentamos crear un usuario con el mismo login
				// y comprobamos que los campos no se han borrado
				nif = generarNIF();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				lstTipoUsuario.selectIndex(1);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				btnCrearUsuario.click();
				assertEquals(txtNIF.getText(), nif);
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Creamos un usuario citador con datos válidos omitiendo
				// el correo electrónico y los teléfonos
				do { 
					nif = generarNIF();
					login = "usuario" + String.valueOf(r.nextInt(100));
					txtNIF.setText(nif);
					txtNombre.setText("Pedro  ");
					txtApellidos.setText("  Jiménez Serrano");
					txtLogin.setText(login);
					txtPassword.setPassword("administrador");
					txtPasswordConf.setPassword("administrador");
					txtCorreoElectronico.setText(" ");
					txtTelefonoFijo.setText(" ");
					txtTelefonoMovil.setText("  ");
					lstTipoUsuario.selectIndex(1);
					assertTrue(!lstTipoMedico.isVisible().isTrue());
					assertTrue(!btnCalendario.isVisible().isTrue());
					assertTrue(!cmbEspecialidad.isVisible().isTrue());
					btnCrearUsuario.click();
				} while (!txtNIF.getText().equals(""));
					comprobarCamposVacios();
			} catch(Exception e) {
				fail(e.toString());
			}
		}
		
		/** Pruebas con datos válidos para registrar un medico **/
		@SuppressWarnings("deprecation")
		public void testDatosValidosMedico() {
			String nif = "";
			String login = "";
			Random r = new Random();
			
			try {
				do {
					// Creamos un usuario administrador con todos los datos válidos
					nif = generarNIF();
					login = "usuario" + String.valueOf(r.nextInt(100));
					txtNIF.setText(nif);
					txtNombre.setText("Pedro");
					txtApellidos.setText("Jiménez Serrano");
					txtLogin.setText(login);
					txtPassword.setPassword("administrador");
					txtPasswordConf.setPassword("administrador");
					txtCorreoElectronico.setText("pjs80@gmail.com");
					txtTelefonoFijo.setText("926147130");
					txtTelefonoMovil.setText("626405060");
					lstTipoUsuario.selectIndex(2);
					assertTrue(lstTipoMedico.isVisible().isTrue());
					assertTrue(btnCalendario.isVisible().isTrue());
					lstTipoMedico.selectIndex(0);
					// TODO: ¿como probar el boton del calendario?
					btnCrearUsuario.click();
				// Si no se han quedado los campos vacios, es porque ya existia el login
				} while (!txtNIF.getText().equals(""));
				comprobarCamposVacios();
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Intentamos crear un usuario con el mismo NIF
				// y comprobamos que los campos no se han borrado
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText("admin7");
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				lstTipoUsuario.selectIndex(2);
				assertTrue(lstTipoMedico.isVisible().isTrue());
				assertTrue(btnCalendario.isVisible().isTrue());
				lstTipoMedico.selectIndex(0);
				btnCrearUsuario.click();
				assertEquals(txtNIF.getText(), nif);
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Intentamos crear un usuario con el mismo login
				// y comprobamos que los campos no se han borrado
				nif = generarNIF();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText("admin17");
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				lstTipoUsuario.selectIndex(2);
				assertTrue(lstTipoMedico.isVisible().isTrue());
				assertTrue(btnCalendario.isVisible().isTrue());
				lstTipoMedico.selectIndex(0);
				btnCrearUsuario.click();
				assertEquals(txtNIF.getText(), nif);
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Creamos un usuario medico con datos válidos omitiendo
				// el correo electrónico y los teléfonos
				do { 
					nif = generarNIF();
					login = "usuario" + String.valueOf(r.nextInt(100));
					txtNIF.setText(nif);
					txtNombre.setText("Pedro  ");
					txtApellidos.setText("  Jiménez Serrano");
					txtLogin.setText(login);
					txtPassword.setPassword("administrador");
					txtPasswordConf.setPassword("administrador");
					txtCorreoElectronico.setText(" ");
					txtTelefonoFijo.setText(" ");
					txtTelefonoMovil.setText("  ");
					lstTipoUsuario.selectIndex(2);
					assertTrue(lstTipoMedico.isVisible().isTrue());
					assertTrue(btnCalendario.isVisible().isTrue());
					lstTipoMedico.selectIndex(0);
					// TODO: ¿como probar el boton del calendario?
					btnCrearUsuario.click();
				} while (!txtNIF.getText().equals(""));
					comprobarCamposVacios();
			} catch(Exception e) {
				fail(e.toString());
			}
			
			try {
				// Creamos un usuario medico especialista con datos válidos
				do {
					nif = generarNIF();
					login = "usuario" + String.valueOf(r.nextInt(100));
					txtNIF.setText(nif);
					txtNombre.setText("Pedro  ");
					txtApellidos.setText("  Jiménez Serrano");
					txtLogin.setText(login);
					txtPassword.setPassword("administrador");
					txtPasswordConf.setPassword("administrador");
					txtCorreoElectronico.setText(" ");
					txtTelefonoFijo.setText(" ");
					txtTelefonoMovil.setText("  ");
					lstTipoUsuario.selectIndex(2);
					assertTrue(lstTipoMedico.isVisible().isTrue());
					assertTrue(btnCalendario.isVisible().isTrue());
					lstTipoMedico.selectIndex(2);
					assertTrue(cmbEspecialidad.isVisible().isTrue());
					jcmbEspecialidad.grabFocus();
					jcmbEspecialidad.setSelectedIndex(0);
					// TODO: ¿como probar el boton del calendario?
					btnCrearUsuario.click();
				} while (!txtNIF.getText().equals(""));
					comprobarCamposVacios();
			} catch(Exception e) {
				fail(e.toString());
			}
		}
			
		
		/** Pruebas de restablecimiento de los datos */
		public void testRestablecer() {
			String nif;
			
			try {
				// Ponemos datos válidos pero borramos todos los
				// campos pulsando el botón Restablecer 
				nif = generarNIF();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText("admin87");
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				lstTipoUsuario.selectIndex(0);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				btnRestablecer.click();
				comprobarCamposVacios();
			} catch(Exception e) {
				fail(e.toString());
			}
		}
		
		private void comprobarCamposVacios() {
			assertTrue(txtNIF.getText().equals(""));
			assertTrue(txtNombre.getText().equals(""));
			assertTrue(txtApellidos.getText().equals(""));
			assertTrue(txtLogin.getText().equals(""));
			assertTrue(txtCorreoElectronico.getText().equals(""));
			assertTrue(txtTelefonoFijo.getText().equals(""));
			assertTrue(txtTelefonoMovil.getText().equals(""));
			assertTrue(lstTipoUsuario.selectionIsEmpty().isTrue());
			assertTrue(lstTipoMedico.selectionIsEmpty());
			assertTrue(!lstTipoMedico.isVisible().isTrue());
			assertTrue(!btnCalendario.isVisible().isTrue());
			assertTrue(!cmbEspecialidad.isVisible().isTrue());
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
