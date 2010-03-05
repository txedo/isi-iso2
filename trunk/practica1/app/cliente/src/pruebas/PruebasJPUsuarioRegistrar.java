package pruebas;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import org.uispec4j.Button;
import org.uispec4j.ComboBox;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import presentacion.JFCalendarioLaboral;
import presentacion.JPUsuarioRegistrar;
import comunicaciones.ConfiguracionCliente;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.ContraseñaIncorrectaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.LoginIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;

public class PruebasJPUsuarioRegistrar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {

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
	private JPasswordField jtxtPassword;
	private JPasswordField jtxtPasswordConf;
	private JList jlstTipoUsuario;
	private JList jlstTipoMedico;
	private JLabel jlblHoras;
	private JComboBox jcmbEspecialidad;
	private Window winPrincipal;

	private JFCalendarioLaboral frameCalendario = null;

	private String textoHoras;

	// Guardamos en una lista los nifs de los usuarios que vamos creando 
	// para despues eliminarlos y no alterar la base de datos
	private ArrayList<String> usuariosCreados = new ArrayList<String>();

	protected void setUp() {
		try {
			// Establecemos conexión con el servidor front-end
			controlador = new ControladorCliente();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IPServidorFrontend, puertoServidorFrontend), usuarioAdmin, passwordAdmin);
					} catch (Exception e) {
						fail(e.toString());
					}
				}
			});
			// Obtenemos el panel
			Panel p1 = winPrincipal.getPanel("jPanelGestionarUsuarios");
			panel = (JPUsuarioRegistrar) p1.getPanel("jPanelRegistrar").getAwtContainer();
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

			jtxtPassword = (JPasswordField) txtPassword.getAwtComponent();
			jtxtPasswordConf = (JPasswordField) txtPasswordConf.getAwtComponent();
			jlstTipoUsuario = (JList) lstTipoUsuario.getAwtComponent();
			jlstTipoMedico = (JList) lstTipoMedico.getAwtComponent();
			jcmbEspecialidad = (JComboBox) cmbEspecialidad.getAwtComponent();
			// Buscamos en el panel la label de las horas seleccionadas en el calendario
			for (Component c : panel.getComponents()) {
				if (c instanceof JLabel && c.getName() != null && c.getName().equals("lblHorasSemanales"))
					jlblHoras = (JLabel) c;
			}
			textoHoras = jlblHoras.getText();
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	protected void tearDown() {
		Usuario u = null;
		try {
			// Si se ha creado algún usuario, los eliminamos
			if (usuariosCreados.size() > 0) {				
				for (String nif : usuariosCreados) {
					u = controlador.consultarUsuario(nif);
					if (u instanceof Medico) {
						controlador.eliminarMedico((Medico) u);
					} else {
						controlador.eliminarUsuario(u);
					}
				}
			}
			// Cerramos la sesión y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
		} catch (Exception e) {
			fail(e.toString());
		}
		winPrincipal.dispose();
	}

	/** Pruebas con datos no válidos */
	public void testDatosInvalidos() {
		try {
			// Ponemos un NIF incorrecto y comprobamos que el campo del
			// NIF se selecciona por tener un formato inválido
			txtNIF.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), new NIFIncorrectoException().getMessage());
			txtNIF.setText("11223344B");
			// Ponemos un nombre incorrecto y comprobamos que se selecciona
			txtNombre.setText("Pedro$");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), new NombreIncorrectoException().getMessage());
			txtNombre.setText("Pedro");
			// Ponemos unos apellidos incorrectos y comprobamos que se seleccionan
			txtApellidos.setText("---");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), new ApellidoIncorrectoException().getMessage());
			txtApellidos.setText("Jiménez Serrano");
			// Ponemos un nombre de usuario incorrecto y comprobamos que se selecciona
			txtLogin.setText("admin- ");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), new LoginIncorrectoException().getMessage());
			txtLogin.setText("admin87");
			// Ponemos una contraseña invalida y comprobamos que se selecciona
			txtPassword.setPassword("123456");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), new ContraseñaIncorrectaException().getMessage());
			txtPassword.setPassword("12345678");
			// Comprobamos que al no coincidir las contraseñas, se selecciona la primera contraseña
			txtPasswordConf.setPassword("123456");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), "Las contraseñas no coinciden.");
			txtPasswordConf.setPassword("12345678");
			// Probamos un e-ail invalido y comprobamos que se selecciona (este campo es opcional)
			txtCorreoElectronico.setText("pjs80@gmail");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), new CorreoElectronicoIncorrectoException().getMessage());
			txtCorreoElectronico.setText("pjs80@gmail.com");
			// Ponemos un teléfono fijo incorrecto y comprobamos que se selecciona
			txtTelefonoFijo.setText("926 147 130");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), new TelefonoFijoIncorrectoException().getMessage());
			txtTelefonoFijo.setText("926147130");
			// Ponemos un teléfono móvil incorrecto y comprobamos que se selecciona
			txtTelefonoMovil.setText("61011122");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), new TelefonoMovilIncorrectoException().getMessage());
			txtTelefonoMovil.setText("626405060");
			// Ponemos un tipo de usuario inválido y comprobamos que se produce un error
			jlstTipoUsuario.grabFocus();
			jlstTipoUsuario.setSelectedIndex(-1);
			btnCrearUsuario.click();
			assertTrue(txtNIF.getText().length() != 0);
			// Seleccionamos un usuario médico
			jlstTipoUsuario.grabFocus();
			jlstTipoUsuario.setSelectedIndex(2);
			assertTrue(lstTipoMedico.isVisible().isTrue());
			assertTrue(btnCalendario.isVisible().isTrue());
			// Seleccionamos un tipo de medico inválido y comprobamos que se produce un error
			jlstTipoMedico.grabFocus();
			jlstTipoMedico.clearSelection();
			btnCrearUsuario.click();
			assertTrue(txtNIF.getText().length() != 0);
			jlstTipoMedico.grabFocus();
			jlstTipoMedico.setSelectedIndex(2);
			// Seleccionamos una especialidad invalida y se comprueba que se produce un error
			assertTrue(cmbEspecialidad.isVisible().isTrue());
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedIndex(-1);
			assertTrue(txtNIF.getText().length() != 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	/** Pruebas con datos válidos para registrar un administrador **/
	@SuppressWarnings("deprecation")
	public void testDatosValidosAdministrador() {
		String nif = "", otronif;
		String login = "";

		try {
			do {
				// Creamos un usuario administrador con todos los datos válidos
				nif = UtilidadesPruebas.generarNIF();
				login = UtilidadesPruebas.generarLogin();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				jlstTipoUsuario.grabFocus();
				jlstTipoUsuario.setSelectedIndex(0);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION).contains("El usuario ha sido dado de alta en el sistema "));
				// Si no se han quedado los campos vacios, es porque ya existia el login
			} while (!txtNIF.getText().equals(""));
			usuariosCreados.add(nif);
		} catch (Exception e) {
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
			jlstTipoUsuario.grabFocus();
			jlstTipoUsuario.setSelectedIndex(0);
			assertTrue(!lstTipoMedico.isVisible().isTrue());
			assertTrue(!btnCalendario.isVisible().isTrue());
			assertTrue(!cmbEspecialidad.isVisible().isTrue());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), "Ya existe una persona en el sistema registrada con el NIF " + nif + ".");
			assertEquals(txtNIF.getText(), nif);
		} catch (Exception e) {
			fail(e.toString());
		}

		try {
			// Intentamos crear un usuario con el mismo login
			// y comprobamos que los campos no se han borrado
			otronif = UtilidadesPruebas.generarNIF();
			txtNIF.setText(otronif);
			txtNombre.setText("Pedro");
			txtApellidos.setText("Jiménez Serrano");
			txtLogin.setText(login);
			txtPassword.setPassword("administrador");
			txtPasswordConf.setPassword("administrador");
			txtCorreoElectronico.setText("pjs80@gmail.com");
			txtTelefonoFijo.setText("926147130");
			txtTelefonoMovil.setText("626405060");
			jlstTipoUsuario.grabFocus();
			jlstTipoUsuario.setSelectedIndex(0);
			assertTrue(!lstTipoMedico.isVisible().isTrue());
			assertTrue(!btnCalendario.isVisible().isTrue());
			assertTrue(!cmbEspecialidad.isVisible().isTrue());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), "El login " + login + " ya existe en el sistema para otro usuario y no se puede utilizar de nuevo.");
			assertEquals(txtNIF.getText(), otronif);
		} catch (Exception e) {
			fail(e.toString());
		}

		try {
			// Creamos un usuario administrador con datos válidos omitiendo
			// el correo electrónico y los teléfonos
			do {
				nif = UtilidadesPruebas.generarNIF();
				login = UtilidadesPruebas.generarLogin();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro  ");
				txtApellidos.setText("  Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText(" ");
				txtTelefonoFijo.setText(" ");
				txtTelefonoMovil.setText("  ");
				jlstTipoUsuario.grabFocus();
				jlstTipoUsuario.setSelectedIndex(0);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION).contains("El usuario ha sido dado de alta en el sistema "));
			} while (!txtNIF.getText().equals(""));
			usuariosCreados.add(nif);
		} catch (Exception e) {
			fail(e.toString());
		}

	}

	/** Pruebas con datos válidos para registrar un citador **/
	@SuppressWarnings("deprecation")
	public void testDatosValidosCitador() {
		String nif = "", otronif;
		String login = "";

		try {
			do {
				// Creamos un usuario citador con todos los datos válidos
				nif = UtilidadesPruebas.generarNIF();
				login = UtilidadesPruebas.generarLogin();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				jlstTipoUsuario.grabFocus();
				jlstTipoUsuario.setSelectedIndex(1);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION).contains("El usuario ha sido dado de alta en el sistema "));
				// Si no se han quedado los campos vacios, es porque ya existia el login
			} while (!txtNIF.getText().equals(""));
			usuariosCreados.add(nif);
		} catch (Exception e) {
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
			jlstTipoUsuario.grabFocus();
			jlstTipoUsuario.setSelectedIndex(1);
			assertTrue(!lstTipoMedico.isVisible().isTrue());
			assertTrue(!btnCalendario.isVisible().isTrue());
			assertTrue(!cmbEspecialidad.isVisible().isTrue());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), "Ya existe una persona en el sistema registrada con el NIF " + nif + ".");
			assertEquals(txtNIF.getText(), nif);
		} catch (Exception e) {
			fail(e.toString());
		}

		try {
			// Intentamos crear un usuario con el mismo login
			// y comprobamos que los campos no se han borrado
			otronif = UtilidadesPruebas.generarNIF();
			txtNIF.setText(otronif);
			txtNombre.setText("Pedro");
			txtApellidos.setText("Jiménez Serrano");
			txtLogin.setText(login);
			txtPassword.setPassword("administrador");
			txtPasswordConf.setPassword("administrador");
			txtCorreoElectronico.setText("pjs80@gmail.com");
			txtTelefonoFijo.setText("926147130");
			txtTelefonoMovil.setText("626405060");
			jlstTipoUsuario.grabFocus();
			jlstTipoUsuario.setSelectedIndex(1);
			assertTrue(!lstTipoMedico.isVisible().isTrue());
			assertTrue(!btnCalendario.isVisible().isTrue());
			assertTrue(!cmbEspecialidad.isVisible().isTrue());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), "El login " + login + " ya existe en el sistema para otro usuario y no se puede utilizar de nuevo.");
			assertEquals(txtNIF.getText(), otronif);
		} catch (Exception e) {
			fail(e.toString());
		}

		try {
			// Creamos un usuario citador con datos válidos omitiendo
			// el correo electrónico y los teléfonos
			do {
				nif = UtilidadesPruebas.generarNIF();
				login = UtilidadesPruebas.generarLogin();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro  ");
				txtApellidos.setText("  Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText(" ");
				txtTelefonoFijo.setText(" ");
				txtTelefonoMovil.setText("  ");
				jlstTipoUsuario.grabFocus();
				jlstTipoUsuario.setSelectedIndex(1);
				assertTrue(!lstTipoMedico.isVisible().isTrue());
				assertTrue(!btnCalendario.isVisible().isTrue());
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION).contains("El usuario ha sido dado de alta en el sistema "));
			} while (!txtNIF.getText().equals(""));
			usuariosCreados.add(nif);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con datos válidos para registrar un medico **/
	@SuppressWarnings("deprecation")
	public void testDatosValidosMedico() {
		String nif = "", otronif;
		String login = "";
		final Vector<PeriodoTrabajo> periodos = new Vector<PeriodoTrabajo>();
		periodos.add(new PeriodoTrabajo(9, 10, DiaSemana.Lunes));

		try {
			do {
				// Creamos un usuario medico con todos los datos válidos (sin calendario)
				nif = UtilidadesPruebas.generarNIF();
				login = UtilidadesPruebas.generarLogin();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro");
				txtApellidos.setText("Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText("pjs80@gmail.com");
				txtTelefonoFijo.setText("926147130");
				txtTelefonoMovil.setText("626405060");
				jlstTipoUsuario.grabFocus();
				jlstTipoUsuario.setSelectedIndex(2);
				assertTrue(lstTipoMedico.isVisible().isTrue());
				assertTrue(btnCalendario.isVisible().isTrue());
				jlstTipoMedico.grabFocus();
				jlstTipoMedico.setSelectedIndex(0);
				assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION).contains("El usuario ha sido dado de alta en el sistema "));
				// Si no se han quedado los campos vacios, es porque ya existia el login
			} while (!txtNIF.getText().equals(""));			
			usuariosCreados.add(nif);
		} catch (Exception e) {
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
			jlstTipoUsuario.grabFocus();
			jlstTipoUsuario.setSelectedIndex(2);
			assertTrue(lstTipoMedico.isVisible().isTrue());
			assertTrue(btnCalendario.isVisible().isTrue());
			jlstTipoMedico.grabFocus();
			jlstTipoMedico.setSelectedIndex(0);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), "Ya existe una persona en el sistema registrada con el NIF " + nif + ".");
			assertEquals(txtNIF.getText(), nif);
		} catch (Exception e) {
			fail(e.toString());
		}

		try {
			// Intentamos crear un usuario con el mismo login
			// y comprobamos que los campos no se han borrado
			otronif = UtilidadesPruebas.generarNIF();
			txtNIF.setText(otronif);
			txtNombre.setText("Pedro");
			txtApellidos.setText("Jiménez Serrano");
			txtLogin.setText(login);
			txtPassword.setPassword("administrador");
			txtPasswordConf.setPassword("administrador");
			txtCorreoElectronico.setText("pjs80@gmail.com");
			txtTelefonoFijo.setText("926147130");
			txtTelefonoMovil.setText("626405060");
			jlstTipoUsuario.grabFocus();
			jlstTipoUsuario.setSelectedIndex(2);
			assertTrue(lstTipoMedico.isVisible().isTrue());
			assertTrue(btnCalendario.isVisible().isTrue());
			jlstTipoMedico.grabFocus();
			jlstTipoMedico.setSelectedIndex(0);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION), "El login " + login + " ya existe en el sistema para otro usuario y no se puede utilizar de nuevo.");
			assertEquals(txtNIF.getText(), otronif);
		} catch (Exception e) {
			fail(e.toString());
		}

		try {
			// Creamos un usuario medico con datos válidos omitiendo
			// el correo electrónico y los teléfonos
			do {
				nif = UtilidadesPruebas.generarNIF();
				login = UtilidadesPruebas.generarLogin();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro  ");
				txtApellidos.setText("  Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText(" ");
				txtTelefonoFijo.setText(" ");
				txtTelefonoMovil.setText("  ");
				jlstTipoUsuario.grabFocus();
				jlstTipoUsuario.setSelectedIndex(2);
				assertTrue(lstTipoMedico.isVisible().isTrue());
				assertTrue(btnCalendario.isVisible().isTrue());
				jlstTipoMedico.grabFocus();
				jlstTipoMedico.setSelectedIndex(0);
				assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION).contains("El usuario ha sido dado de alta en el sistema "));
			} while (!txtNIF.getText().equals(""));
			usuariosCreados.add(nif);
		} catch (Exception e) {
			fail(e.toString());
		}

		try {
			// Creamos un usuario medico especialista con datos válidos (con calendario)
			do {
				nif = UtilidadesPruebas.generarNIF();
				login = UtilidadesPruebas.generarLogin();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro  ");
				txtApellidos.setText("  Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText(" ");
				txtTelefonoFijo.setText(" ");
				txtTelefonoMovil.setText("  ");
				jlstTipoUsuario.grabFocus();
				jlstTipoUsuario.setSelectedIndex(2);
				assertTrue(lstTipoMedico.isVisible().isTrue());
				assertTrue(btnCalendario.isVisible().isTrue());
				jlstTipoMedico.grabFocus();
				jlstTipoMedico.setSelectedIndex(2);
				assertTrue(cmbEspecialidad.isVisible().isTrue());
				jcmbEspecialidad.grabFocus();
				jcmbEspecialidad.setSelectedIndex(0);
				// Interceptamos el frame para configurar el calendario
				WindowInterceptor.init(btnCalendario.triggerClick()).process(
						new WindowHandler() {
							public Trigger process(Window window) {
								frameCalendario = (JFCalendarioLaboral) window.getAwtComponent();
								frameCalendario.setPeriodosTrabajo(periodos);
								return window.getButton("btnAceptar").triggerClick();
							}
						}).run();
				// El texto de la label se ha debido actualizar con el numero de horas introducido
				assertTrue(!textoHoras.equals(jlblHoras.getText()));
				assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION).contains("El usuario ha sido dado de alta en el sistema "));
			} while (!txtNIF.getText().equals(""));
			usuariosCreados.add(nif);
		} catch (Exception e) {
			fail(e.toString());
		}
		try {
			// Creamos un usuario medico pediatra con datos válidos (con calendario de más de una hora)
			do {
				nif = UtilidadesPruebas.generarNIF();
				login = UtilidadesPruebas.generarLogin();
				txtNIF.setText(nif);
				txtNombre.setText("Pedro  ");
				txtApellidos.setText("  Jiménez Serrano");
				txtLogin.setText(login);
				txtPassword.setPassword("administrador");
				txtPasswordConf.setPassword("administrador");
				txtCorreoElectronico.setText(" ");
				txtTelefonoFijo.setText(" ");
				txtTelefonoMovil.setText("  ");
				jlstTipoUsuario.grabFocus();
				jlstTipoUsuario.setSelectedIndex(2);
				assertTrue(lstTipoMedico.isVisible().isTrue());
				assertTrue(btnCalendario.isVisible().isTrue());
				jlstTipoMedico.grabFocus();
				jlstTipoMedico.setSelectedIndex(1);
				assertTrue(!cmbEspecialidad.isVisible().isTrue());
				// Interceptamos el frame para configurar el calendario
				textoHoras = jlblHoras.getText();
				periodos.add(new PeriodoTrabajo(11, 12, DiaSemana.Martes));
				WindowInterceptor.init(btnCalendario.triggerClick()).process(
						new WindowHandler() {
							public Trigger process(Window window) {
								frameCalendario = (JFCalendarioLaboral) window.getAwtComponent();
								frameCalendario.setPeriodosTrabajo(periodos);
								return window.getButton("btnAceptar").triggerClick();
							}
						}).run();
				// El texto de la label se ha debido actualizar con el numero de horas introducido
				assertTrue(!textoHoras.equals(jlblHoras.getText()));
				assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnCrearUsuario, OK_OPTION).contains("El usuario ha sido dado de alta en el sistema "));
			} while (!txtNIF.getText().equals(""));
			usuariosCreados.add(nif);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de restablecimiento de los datos */
	public void testRestablecer() {
		String nif;

		try {
			// Ponemos datos válidos pero borramos todos los
			// campos pulsando el botón Restablecer 
			nif = UtilidadesPruebas.generarNIF();
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
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	private void comprobarCamposVacios() {
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
		assertTrue(txtApellidos.getText().equals(""));
		assertTrue(txtLogin.getText().equals(""));
		assertTrue(new String(jtxtPassword.getPassword()).equals(""));
		assertTrue(new String(jtxtPasswordConf.getPassword()).equals(""));
		assertTrue(txtCorreoElectronico.getText().equals(""));
		assertTrue(txtTelefonoFijo.getText().equals(""));
		assertTrue(txtTelefonoMovil.getText().equals(""));
		assertTrue(lstTipoUsuario.selectionIsEmpty().isTrue());
		assertTrue(lstTipoMedico.selectionIsEmpty());
		assertTrue(!lstTipoMedico.isVisible().isTrue());
		assertTrue(!btnCalendario.isVisible().isTrue());
		assertTrue(!cmbEspecialidad.isVisible().isTrue());
	}

}
