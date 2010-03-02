package pruebas;

import java.awt.Component;
import java.util.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.ComboBox;
import org.uispec4j.Panel;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JFCalendarioLaboral;
import presentacion.JPUsuarioConsultar;

import comunicaciones.ConfiguracionCliente;

import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.LoginIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;
import excepciones.UsuarioYaExistenteException;

public class PruebasJPUsuarioConsultar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {

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
	private JLabel jlblHoras;
	private Window winPrincipal;
	private JFCalendarioLaboral frameCalendario;
	
	private String textoHoras, login;
	private boolean eliminadoAdmin, eliminadoMedico;
	
	private Administrador admin;
	private Citador citador;
	private Medico cabecera, especialista, pediatra;
	private TipoMedico tPediatra, tCabecera, tEspecialista;
	private PeriodoTrabajo periodo1;
	
	protected void setUp() {
		boolean valido = true;
		eliminadoAdmin = false;
		eliminadoMedico = false;
		
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
			// Creamos e insertamos usuarios de prueba, para poder consultarlos
			login = UtilidadesPruebas.generarLogin();
			admin = new Administrador(UtilidadesPruebas.generarNIF(), login, login, "administrador", "administra", "", "", "");
			login = UtilidadesPruebas.generarLogin();
			citador = new Citador(UtilidadesPruebas.generarNIF(), login, login, "citador", "cita", "adad@gmail.com", "912312312", "612131212");
			tCabecera = new Cabecera();
			tEspecialista = new Especialista("Neurologia");
			tPediatra = new Pediatra();
			login = UtilidadesPruebas.generarLogin();
			cabecera = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Eduardo", "PC", "", "", "", tCabecera);
			login = UtilidadesPruebas.generarLogin();
			pediatra = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Carmen", "GG", "carmen@gmail.com", "", "", tPediatra);
			login = UtilidadesPruebas.generarLogin();
			especialista = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Juan", "PF", "asdsad@yahoo.es", "987654321", "678901234", tEspecialista);
			periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Miercoles);
			cabecera.getCalendario().add(periodo1);
			pediatra.getCalendario().add(periodo1);
			especialista.getCalendario().add(periodo1);
			
			// Mientras existan los usuarios, se genera otro login y otro NIF
			do {
				try {
					controlador.crearUsuario(admin);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					admin.setNif(UtilidadesPruebas.generarNIF());
					login = UtilidadesPruebas.generarLogin();
					admin.setLogin(login);
					admin.setPassword(login);
					valido = false;
				}
			}while(!valido);
			do {
				try {
					controlador.crearUsuario(citador);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					citador.setNif(UtilidadesPruebas.generarNIF());
					login = UtilidadesPruebas.generarLogin();
					citador.setLogin(login);
					citador.setPassword(login);
					valido = false;
				}
			}while(!valido);
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
			
			// Consultamos el médico, porque el centro de salud que realmente se le asigna
			// se hace de manera aleatoria
			cabecera = controlador.consultarMedico(cabecera.getNif());
			do {
				try {
					controlador.crearUsuario(pediatra);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					pediatra.setNif(UtilidadesPruebas.generarNIF());
					login = UtilidadesPruebas.generarLogin();
					pediatra.setLogin(login);
					pediatra.setPassword(login);
					valido = false;
				}
			}while(!valido);
			do {
				try {
					controlador.crearUsuario(especialista);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					admin.setNif(UtilidadesPruebas.generarNIF());
					login = UtilidadesPruebas.generarLogin();
					especialista.setLogin(login);
					especialista.setPassword(login);
					valido = false;
				}
			}while(!valido);
			
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
			
			// Buscamos en el panel la label de las horas seleccionadas en el calendario
			for (Component c : panel.getComponents()) {
				if (c instanceof JLabel && c.getName() != null
						&& c.getName().equals("lblHorasSemanales"))
					jlblHoras = (JLabel) c;
			}
			textoHoras = jlblHoras.getText();
			
			Thread.sleep(50);
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Eliminamos objetos de prueba
			if (!eliminadoAdmin) controlador.eliminarUsuario(admin);
			controlador.eliminarUsuario(citador);
			if (!eliminadoMedico) controlador.eliminarMedico(cabecera);
			controlador.eliminarMedico(pediatra);
			controlador.eliminarMedico(especialista);
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
		try {
			// Ponemos un NIF nulo
			txtNIFBuscado.setText("");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inválido
			txtNIFBuscado.setText("111111");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// Probamos con un NIF que no esté dado de alta en el sistema
			txtNIFBuscado.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "No existe ningún usuario con el NIF introducido.");
			// Ponemos un NIF correcto
			txtNIFBuscado.setText(admin.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			// Intentamos modificar el usuario
			assertFalse(chkEditar.isSelected());
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Los campos NIF y centro no son editables
			assertFalse(txtNIF.isEditable());
			assertFalse(txtCentro.isEditable());
			// Escribimos un nombre incorrecto y comprobamos que se selecciona
			txtNombre.setText("Pedro$");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new NombreIncorrectoException().getMessage());
			txtNombre.setText("Pedro");
			// Ponemos unos apellidos incorrectos y comprobamos que se seleccionan
			txtApellidos.setText("---");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new ApellidoIncorrectoException().getMessage());
			txtApellidos.setText("Jiménez Serrano");
			// Ponemos un nombre de usuario incorrecto y comprobamos que se selecciona
			txtLogin.setText("admin- ");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new LoginIncorrectoException().getMessage());
			txtLogin.setText("admin87");
			// Ponemos una contraseña invalida y comprobamos que se selecciona
			// TODO: sigue fallando la contraseña. Ahora me dice que se almacena correctamente
			/* jtxtPassword.grabFocus();
			jtxtPassword.setText("123456");			
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new ContraseñaIncorrectaException().getMessage());
			jtxtPassword.setText("12345678");
			// Comprobamos que al no coincidir las contraseñas, se selecciona la primera contraseña
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new ContraseñaIncorrectaException().getMessage());*/
			jtxtPassword.setText("12345678");
			jtxtPasswordConf.setText("12345678");
			// Probamos un e-mail invalido y comprobamos que se selecciona (este campo es opcional)
			txtCorreoElectronico.setText("pjs80@gmail");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new CorreoElectronicoIncorrectoException().getMessage());
			txtCorreoElectronico.setText("pjs80@gmail.com");
			// Ponemos un teléfono fijo incorrecto y comprobamos que se selecciona
			txtTelefonoFijo.setText("926 147 130");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new TelefonoFijoIncorrectoException().getMessage());
			txtTelefonoFijo.setText("926147130");
			// Ponemos un teléfono móvil incorrecto y comprobamos que se selecciona
			txtTelefonoMovil.setText("61011122");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), new TelefonoMovilIncorrectoException().getMessage());
			txtTelefonoMovil.setText("626405060");
			// Como se ha buscado un administrador, intentamos cambiar su rol a uno no válido
			jcmbRol.grabFocus();
			jcmbRol.setSelectedIndex(-1);
			btnGuardar.click();
			assertTrue(txtNIF.getText().length()!=0);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testConsultarAdministrador() {
		txtNIFBuscado.setText(admin.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
		assertEquals(txtNIF.getText(), admin.getNif());
		assertEquals(jcmbRol.getSelectedItem().toString(), "Administrador");
		assertFalse(btnCalendario.isVisible());
	}
	
	public void testConsultarCitador() {
		txtNIFBuscado.setText(citador.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
		assertEquals(txtNIF.getText(), citador.getNif());
		assertEquals(jcmbRol.getSelectedItem().toString(), "Citador");
		assertFalse(txtEspecialidad.isVisible());
		assertFalse(btnCalendario.isVisible());
	}
	
	public void testConsultarMedico() {
		txtNIFBuscado.setText(cabecera.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
		assertEquals(txtNIF.getText(), cabecera.getNif());
		assertEquals(jcmbRol.getSelectedItem().toString(), "Medico (Cabecera)");
		assertFalse(txtEspecialidad.isVisible());
		assertTrue(btnCalendario.isVisible());
	}

	public void testConsultarEspecialista() {
		txtNIFBuscado.setText(especialista.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
		assertEquals(txtNIF.getText(), especialista.getNif());
		assertEquals(jcmbRol.getSelectedItem().toString(), "Medico (Especialista)");
		assertTrue(txtEspecialidad.isVisible());
		assertTrue(btnCalendario.isVisible());
	}
	
	public void testActualizarAdministrador() {
		String nuevoNombre = "nombre nuevo del administrador";
		
		// Actualizamos el nombre del administrador
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(admin.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), admin.getNif());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el administrador			
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Editamos el nombre del administrador
			txtNombre.setText(nuevoNombre);
			// Guardamos el administrador
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), "El usuario ha sido modificado correctamente.");
			// Comprobamos que el administrador ha sido modificado correctamente
			txtNIFBuscado.setText(admin.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), admin.getNif());
			// Comprobamos que en el textbox del nombre sale nuevoNombre
			assertEquals(nuevoNombre, txtNombre.getText());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		// Cambiamos el rol del administrador a citador
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(admin.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), admin.getNif());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el administrador
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Editamos el rol del administrador
			jcmbRol.grabFocus();
			jcmbRol.setSelectedIndex(1);
			// Guardamos el administrador
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), "El usuario ha sido modificado correctamente.");
			// Comprobamos que el administrador ha sido modificado correctamente
			txtNIFBuscado.setText(admin.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), admin.getNif());
			assertEquals(jcmbRol.getSelectedItem().toString(), "Citador");
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// TODO: Pruebas para actualizar la contraseña
	}
	
	public void testActualizarCitador() {
		String nuevoCorreo = "correoNuevo@gmail.com";
		
		// Actualizamos el correo del citador
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(citador.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), citador.getNif());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el citador
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Editamos el correo del citador
			txtCorreoElectronico.setText(nuevoCorreo);
			// Guardamos el citador
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), "El usuario ha sido modificado correctamente.");
			// Comprobamos que el citador ha sido modificado correctamente
			txtNIFBuscado.setText(citador.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), citador.getNif());			
			assertEquals(nuevoCorreo, txtCorreoElectronico.getText());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Cambiamos el rol del citador a administrador
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(citador.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), citador.getNif());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el citador
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Editamos el rol del citador
			jcmbRol.grabFocus();
			jcmbRol.setSelectedIndex(0);
			// Guardamos el citador
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), "El usuario ha sido modificado correctamente.");
			// Comprobamos que el citador ha sido modificado correctamente
			txtNIFBuscado.setText(citador.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), citador.getNif());
			assertEquals(jcmbRol.getSelectedItem().toString(), "Administrador");
		} catch(Exception e) {
			fail(e.toString());
		}
		
	}
	
	public void testActualizarMedico1() {
		String nuevoLogin = cabecera.getLogin() + "Cambiado";
		String nuevoTelefono = "918213122";
		final Vector<PeriodoTrabajo> periodos = new Vector<PeriodoTrabajo>();
		periodos.add(periodo1);
		periodos.add(new PeriodoTrabajo(9, 10, DiaSemana.Lunes));
		
		// Actualizamos el login del medico
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el médico
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Editamos el login del médico
			txtLogin.setText(nuevoLogin);
			// Guardamos el medico
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), "El usuario ha sido modificado correctamente.");
			// Comprobamos que el medico ha sido modificado correctamente
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());			
			assertEquals(nuevoLogin, txtLogin.getText());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Modificamos el telefono de un especialista
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(especialista.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), especialista.getNif());
			assertTrue(txtEspecialidad.isVisible().isTrue());
			assertEquals(((Especialista)especialista.getTipoMedico()).getEspecialidad(), txtEspecialidad.getText());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el médico
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Editamos el teléfono del médico
			txtTelefonoFijo.setText(nuevoTelefono);
			// Guardamos el medico
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), "El usuario ha sido modificado correctamente.");
			// Comprobamos que el medico ha sido modificado correctamente
			txtNIFBuscado.setText(especialista.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), especialista.getNif());			
			assertEquals(nuevoTelefono, txtTelefonoFijo.getText());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Modificamos el calendario del médico
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			// Comprobamos que, inicialmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el citador
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Editamos el calendario del médico
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
			// Guardamos el médico
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar, OK_OPTION), "El usuario ha sido modificado correctamente.");
			// Comprobamos que el médico ha sido modificado correctamente
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			assertEquals(jlblHoras.getText(), "7 horas semanales");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testActualizarMedico2() {
		Cita c; 
		Beneficiario beneficiarioPrueba;
		final Vector<PeriodoTrabajo> periodos = new Vector<PeriodoTrabajo>();
		periodos.add(new PeriodoTrabajo(10, 11, DiaSemana.Lunes ));
		
		try {
			// Modificamos el calendario del médico, afectando a la cita que tenia registrada
			
			// Creamos un beneficiario de prueba
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
			controlador.crearBeneficiario(beneficiarioPrueba);
			
			// Creamos una cita de prueba		
			c=controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010 - 1900, 3, 14, 10, 15), dominio.conocimiento.IConstantes.DURACION_CITA);
		
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			// Comprobamos que, inicialmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertFalse(btnGuardar.isEnabled());
			// A continuación se pasará a tratar de editar el citador
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled());
			// Editamos el calendario del médico
			// Interceptamos el frame para configurar el calendario
			WindowInterceptor.init(btnCalendario.triggerClick()).process(
					new WindowHandler() {
						public Trigger process(Window window) {
							// Limpiamos el calendario y ponemos la nueva hora
							window.getButton("btnRestablecerTodo").click();
							frameCalendario = (JFCalendarioLaboral) window.getAwtComponent();
							frameCalendario.setPeriodosTrabajo(periodos);
							return window.getButton("btnAceptar").triggerClick();
						}
					}).run();
			// El texto de la label se ha debido actualizar con el numero de horas introducido
			assertTrue(jlblHoras.getText().equals("1 hora semanal"));
			// Actualizamos el médico
			WindowInterceptor.init(btnGuardar.triggerClick()).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Confirmamos que eliminamos la cita
					return window.getButton(YES_OPTION).triggerClick();
				}
				// Aviso de modificación correcta
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Aviso de las citas anuladas
						return window.getButton(OK_OPTION).triggerClick();
					}
				// Aviso de las citas eliminadas
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Aviso de las citas anuladas
						return window.getButton(CLOSED_OPTION).triggerClick();
					}
			}).run();
			// Comprobamos que el médico ha sido modificado correctamente
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertTrue(jlblHoras.getText().equals("1 hora semanal"));
			assertEquals(txtNIF.getText(), cabecera.getNif());
			controlador.eliminarBeneficiario(beneficiarioPrueba);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testEliminarAdministrador () {
		eliminadoAdmin = false;
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(admin.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), admin.getNif());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Eliminar no está habilitado
			assertFalse(btnEliminar.isEnabled());
			// A continuación se pasará a tratar de eliminar el médico
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Eliminar ahora sí está habilitado
			assertTrue(btnEliminar.isEnabled());
			// Eliminamos el administrador confirmando la operación en el diálogo de confirmación
			WindowInterceptor.init(btnEliminar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			// Comprobamos que el usuario ha sido eliminado correctamente
			// Buscamos el usuario
			txtNIFBuscado.setText(admin.getNif());
			btnBuscar.click();
			WindowInterceptor.init(btnBuscar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					assertTrue(window.titleContains("Error"));
					return window.getButton(OK_OPTION).triggerClick();
				}
				
			}).run();
			// Como no se encuentra al usuario, el boton Editar tiene que estar deshabilitado
			assertFalse(chkEditar.isEnabled());
			eliminadoAdmin = true;
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	// Pruebas para eliminar un médico que no tiene citas ni beneficiarios asignados
	public void testEliminarMedico1() {
		eliminadoMedico = false;
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			// Comprobamos que, inicialmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Eliminar no está habilitado
			assertFalse(btnEliminar.isEnabled());
			// A continuación se pasará a tratar de eliminar el médico
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Eliminar ahora sí está habilitado
			assertTrue(btnEliminar.isEnabled());
			// Eliminamos el medico confirmando la operación en el diálogo de confirmación
			WindowInterceptor.init(btnEliminar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			// Comprobamos que el usuario ha sido eliminado correctamente
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getNif());
			btnBuscar.click();
			WindowInterceptor.init(btnBuscar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					assertTrue(window.titleContains("Error"));
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Como no se encuentra al usuario, el boton Editar tiene que estar deshabilitado
			assertFalse(chkEditar.isEnabled());
			eliminadoMedico = true;
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	// Pruebas para eliminar un médico que tiene citas y beneficiarios asignados
	public void testEliminarMedico2() {
		eliminadoMedico = false;
		Beneficiario beneficiarioPrueba;
		
		try {
			
			// Creamos un beneficiario de prueba
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
			controlador.crearBeneficiario(beneficiarioPrueba);
			
			// Creamos una cita de prueba		
			controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010 - 1900, 3, 14, 10, 15), dominio.conocimiento.IConstantes.DURACION_CITA);

			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			// Comprobamos que, inicialmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled());
			assertFalse(chkEditar.isSelected());
			// Comprobamos que, inicialmente, el botón Eliminar no está habilitado
			assertFalse(btnEliminar.isEnabled());
			// A continuación se pasará a tratar de eliminar el médico
			chkEditar.select();
			assertTrue(chkEditar.isSelected());
			// Comprobamos que el boton de Eliminar ahora sí está habilitado
			assertTrue(btnEliminar.isEnabled());
			// Capturamos los mensajes de confirmacion
			// y las ventanas de aviso
			WindowInterceptor.init(btnEliminar.triggerClick()).process(new WindowHandler() {
				public Trigger process(Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Aviso de los beneficiarios que cambian de médico
					return window.getButton(CLOSED_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Aviso de las citas anuladas
					return window.getButton(CLOSED_OPTION).triggerClick();
				}
			}).run();
			// Comprobamos que el usuario ha sido eliminado correctamente
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getNif());
			btnBuscar.click();
			WindowInterceptor.init(btnBuscar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					assertTrue(window.titleContains("Error"));
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Como no se encuentra al usuario, el boton Editar tiene que estar deshabilitado
			assertFalse(chkEditar.isEnabled());
			eliminadoMedico = true;
			controlador.eliminarBeneficiario(beneficiarioPrueba);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

}

