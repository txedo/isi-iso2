package pruebas;

import java.awt.Component;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
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
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import comunicaciones.ConfiguracionCliente;

import presentacion.JFCalendarioLaboral;
import presentacion.JPUsuarioConsultar;
import presentacion.auxiliar.Validacion;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CadenaVaciaException;
import excepciones.ContraseñaIncorrectaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.LoginIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

public class PruebasJPUsuarioConsultar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas,IConstantes {

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
	
	private String textoHoras;
	private boolean eliminadoAdmin, eliminadoMedico;
	
	private Administrador admin;
	private Citador citador;
	private Medico cabecera, especialista, pediatra;
	private TipoMedico tPediatra, tCabecera, tEspecialista;
	private CentroSalud centro;
	private PeriodoTrabajo periodo1;
	
	protected void setUp() {
		boolean valido = true;
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
			admin = new Administrador(generarNIF(), UtilidadesPruebas.generarLoginAleatorio(), Encriptacion.encriptarPasswordSHA1("admin"), "administrador", "administra", "", "", "");
			citador = new Citador(generarNIF(), UtilidadesPruebas.generarLoginAleatorio(), Encriptacion.encriptarPasswordSHA1("citador"), "citador", "cita", "adad@gmail.com", "912312312", "612131212");
			tCabecera = new Cabecera();
			tEspecialista = new Especialista("Neurologia");
			tPediatra = new Pediatra();
			cabecera = new Medico(generarNIF(), UtilidadesPruebas.generarLoginAleatorio(), Encriptacion.encriptarPasswordSHA1("medCabecera"), "Eduardo", "PC", "", "", "", tCabecera);
			pediatra = new Medico(generarNIF(), UtilidadesPruebas.generarLoginAleatorio(), Encriptacion.encriptarPasswordSHA1("medPediatra"), "Carmen", "GG", "carmen@gmail.com", "", "", tPediatra);
			especialista = new Medico(generarNIF(), UtilidadesPruebas.generarLoginAleatorio(), Encriptacion.encriptarPasswordSHA1("medEspecialista"), "Juan", "PF", "asdsad@yahoo.es", "987654321", "678901234", tEspecialista);
			cabecera.setCentroSalud(controlador.consultarCentros().firstElement());
			pediatra.setCentroSalud(controlador.consultarCentros().firstElement());
			especialista.setCentroSalud(controlador.consultarCentros().firstElement());
			citador.setCentroSalud(controlador.consultarCentros().firstElement());
			admin.setCentroSalud(controlador.consultarCentros().firstElement());
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
					admin.setDni(UtilidadesPruebas.generarNIF());
					admin.setLogin(UtilidadesPruebas.generarLoginAleatorio());
					valido = false;
				}
			}while(!valido);
			do {
				try {
					controlador.crearUsuario(citador);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					admin.setDni(UtilidadesPruebas.generarNIF());
					admin.setLogin(UtilidadesPruebas.generarLoginAleatorio());
					valido = false;
				}
			}while(!valido);
			do {
				try {
					controlador.crearUsuario(cabecera);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					admin.setDni(UtilidadesPruebas.generarNIF());
					admin.setLogin(UtilidadesPruebas.generarLoginAleatorio());
					valido = false;
				}
			}while(!valido);
			do {
				try {
					controlador.crearUsuario(pediatra);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					admin.setDni(UtilidadesPruebas.generarNIF());
					admin.setLogin(UtilidadesPruebas.generarLoginAleatorio());
					valido = false;
				}
			}while(!valido);
			do {
				try {
					controlador.crearUsuario(especialista);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					admin.setDni(UtilidadesPruebas.generarNIF());
					admin.setLogin(UtilidadesPruebas.generarLoginAleatorio());
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
			if (!eliminadoMedico) controlador.eliminarMedico(pediatra);
			if (!eliminadoMedico) controlador.eliminarMedico(especialista);
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
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), new NIFIncorrectoException().getMessage());
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inválido
			txtNIFBuscado.setText("111111");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), new NIFIncorrectoException().getMessage());
			// Probamos con un NIF que no esté dado de alta en el sistema
			txtNIFBuscado.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), "No existe ningún usuario con el DNI introducido.");
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
			txtNombre.setText("Pedro$");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new NombreIncorrectoException().getMessage());
			txtNombre.setText("Pedro");
			// Ponemos unos apellidos incorrectos y comprobamos que se seleccionan
			txtApellidos.setText("---");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new ApellidoIncorrectoException().getMessage());
			txtApellidos.setText("Jiménez Serrano");
			// Ponemos un nombre de usuario incorrecto y comprobamos que se selecciona
			txtLogin.setText("admin- ");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new LoginIncorrectoException().getMessage());
			txtLogin.setText("admin87");
			// Ponemos una contraseña invalida y comprobamos que se selecciona
			// TODO: sigue fallando la contraseña. Ahora me dice que se almacena correctamente
			/*txtPassword.setPassword("123456");			
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new ContraseñaIncorrectaException().getMessage());
			jtxtPassword.setText("12345678");
			// Comprobamos que al no coincidir las contraseñas, se selecciona la primera contraseña
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new ContraseñaIncorrectaException().getMessage());*/
			jtxtPassword.setText("12345678");
			jtxtPasswordConf.setText("12345678");
			// Probamos un e-mail invalido y comprobamos que se selecciona (este campo es opcional)
			txtCorreoElectronico.setText("pjs80@gmail");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new CorreoElectronicoIncorrectoException().getMessage());
			txtCorreoElectronico.setText("pjs80@gmail.com");
			// Ponemos un teléfono fijo incorrecto y comprobamos que se selecciona
			txtTelefonoFijo.setText("926 147 130");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new TelefonoFijoIncorrectoException().getMessage());
			txtTelefonoFijo.setText("926147130");
			// Ponemos un teléfono móvil incorrecto y comprobamos que se selecciona
			txtTelefonoMovil.setText("61011122");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnGuardar), new TelefonoMovilIncorrectoException().getMessage());
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
		txtNIFBuscado.setText(admin.getDni());
		btnBuscar.click();
		assertEquals(txtNIFBuscado.getText(), "");
		assertEquals(txtNIF.getText(), admin.getDni());
		assertEquals(jcmbRol.getSelectedItem().toString(), "Administrador");
		assertTrue(!btnCalendario.isVisible().isTrue());
	}
	
	public void testConsultarCitador() {
		txtNIFBuscado.setText(citador.getDni());
		btnBuscar.click();
		assertEquals(txtNIFBuscado.getText(), "");
		assertEquals(txtNIF.getText(), citador.getDni());
		assertEquals(jcmbRol.getSelectedItem().toString(), "Citador");
		assertTrue(!txtEspecialidad.isVisible().isTrue());
		assertTrue(!btnCalendario.isVisible().isTrue());
	}
	
	public void testConsultarMedico() {
		txtNIFBuscado.setText(cabecera.getDni());
		btnBuscar.click();
		assertEquals(txtNIFBuscado.getText(), "");
		assertEquals(txtNIF.getText(), cabecera.getDni());
		assertEquals(jcmbRol.getSelectedItem().toString(), "Medico (Cabecera)");
		assertTrue(!txtEspecialidad.isVisible().isTrue());
		assertTrue(btnCalendario.isVisible().isTrue());
	}

	public void testConsultarEspecialista() {
		txtNIFBuscado.setText(especialista.getDni());
		btnBuscar.click();
		assertEquals(txtNIFBuscado.getText(), "");
		assertEquals(txtNIF.getText(), especialista.getDni());
		assertEquals(jcmbRol.getSelectedItem().toString(), "Medico (Especialista)");
		assertTrue(txtEspecialidad.isVisible().isTrue());
		assertTrue(btnCalendario.isVisible().isTrue());
	}
	
	public void testActualizarAdministrador() {
		String nuevoNombre = "nombre nuevo del administrador";
		
		// Actualizamos el nombre del administrador
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(admin.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), admin.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertTrue(!btnGuardar.isEnabled().isTrue());
			// A continuación se pasará a tratar de editar el administrador			
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled().isTrue());
			// Editamos el nombre del administrador
			txtNombre.setText(nuevoNombre);
			// Guardamos el administrador
			btnGuardar.click();
			comprobarCamposVacios();
			// Comprobamos que el administrador ha sido modificado correctamente
			txtNIFBuscado.setText(admin.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), admin.getDni());
			// Comprobamos que en el textbox del nombre sale nuevoNombre
			assertEquals(nuevoNombre, txtNombre.getText());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		// Cambiamos el rol del administrador a citador
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(admin.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), admin.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertTrue(!btnGuardar.isEnabled().isTrue());
			// A continuación se pasará a tratar de editar el administrador			
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled().isTrue());
			// Editamos el rol del administrador
			jcmbRol.grabFocus();
			jcmbRol.setSelectedIndex(1);
			// Guardamos el administrador
			btnGuardar.click();
			comprobarCamposVacios();
			// Comprobamos que el administrador ha sido modificado correctamente
			txtNIFBuscado.setText(admin.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), admin.getDni());
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
			txtNIFBuscado.setText(citador.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), citador.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertTrue(!btnGuardar.isEnabled().isTrue());
			// A continuación se pasará a tratar de editar el citador	
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled().isTrue());
			// Editamos el correo del citador
			txtCorreoElectronico.setText(nuevoCorreo);
			// Guardamos el citador
			btnGuardar.click();
			comprobarCamposVacios();
			// Comprobamos que el citador ha sido modificado correctamente
			txtNIFBuscado.setText(citador.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), citador.getDni());			
			assertEquals(nuevoCorreo, txtCorreoElectronico.getText());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Cambiamos el rol del citador a administrador
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(citador.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), citador.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertTrue(!btnGuardar.isEnabled().isTrue());
			// A continuación se pasará a tratar de editar el citador
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled().isTrue());
			// Editamos el rol del citador
			jcmbRol.grabFocus();
			jcmbRol.setSelectedIndex(0);
			// Guardamos el citador
			btnGuardar.click();
			comprobarCamposVacios();
			// Comprobamos que el citador ha sido modificado correctamente
			txtNIFBuscado.setText(citador.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), citador.getDni());
			assertEquals(jcmbRol.getSelectedItem().toString(), "Administrador");
		} catch(Exception e) {
			fail(e.toString());
		}
		
	}
	
	public void testActualizarMedico() {
		String nuevoLogin = cabecera.getLogin() + "Cambiado";
		String nuevoTelefono = "918213122";
		final Vector<PeriodoTrabajo> periodos = new Vector<PeriodoTrabajo>();
		periodos.add(periodo1);
		periodos.add(new PeriodoTrabajo(9, 10, DiaSemana.Lunes));
		
		// Actualizamos el login del medico
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), cabecera.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertTrue(!btnGuardar.isEnabled().isTrue());
			// A continuación se pasará a tratar de editar el medico	
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled().isTrue());
			// Editamos el login del médico
			txtLogin.setText(nuevoLogin);
			// Guardamos el medico
			btnGuardar.click();
			comprobarCamposVacios();
			// Comprobamos que el medico ha sido modificado correctamente
			txtNIFBuscado.setText(cabecera.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), cabecera.getDni());			
			assertEquals(nuevoLogin, txtLogin.getText());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Modificamos el telefono de un especialista
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(especialista.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), especialista.getDni());
			assertTrue(txtEspecialidad.isVisible().isTrue());
			assertEquals(((Especialista)especialista.getTipoMedico()).getEspecialidad(), txtEspecialidad.getText());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertTrue(!btnGuardar.isEnabled().isTrue());
			// A continuación se pasará a tratar de editar el medico	
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled().isTrue());
			// Editamos el teléfono del médico
			txtTelefonoFijo.setText(nuevoTelefono);
			// Guardamos el medico
			btnGuardar.click();
			comprobarCamposVacios();
			// Comprobamos que el medico ha sido modificado correctamente
			txtNIFBuscado.setText(especialista.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), especialista.getDni());			
			assertEquals(nuevoTelefono, txtTelefonoFijo.getText());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Modificamos el calendario del médico
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), cabecera.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Guardar no está habilitado
			assertTrue(!btnGuardar.isEnabled().isTrue());
			// A continuación se pasará a tratar de editar el citador
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Guardar ahora sí está habilitado
			assertTrue(btnGuardar.isEnabled().isTrue());
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
			btnGuardar.click();
			comprobarCamposVacios();
			// Comprobamos que el médico ha sido modificado correctamente
			txtNIFBuscado.setText(cabecera.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), cabecera.getDni());
			assertEquals(jlblHoras.getText(), "7 horas semanales");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testEliminarAdministrador () {
		eliminadoAdmin = false;
		try {
			// Buscamos el usuario
			txtNIFBuscado.setText(admin.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), admin.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Eliminar no está habilitado
			assertTrue(!btnEliminar.isEnabled().isTrue());
			// A continuación se pasará a tratar de eliminar el administrador
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Eliminar ahora sí está habilitado
			assertTrue(btnEliminar.isEnabled().isTrue());
			// Eliminamos el administrador confirmando la operación en el diálogo de confirmación
			WindowInterceptor.init(btnEliminar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			comprobarCamposVacios();
			// Comprobamos que el usuario ha sido eliminado correctamente
			// Buscamos el usuario
			txtNIFBuscado.setText(admin.getDni());
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
			txtNIFBuscado.setText(cabecera.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), cabecera.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Eliminar no está habilitado
			assertTrue(!btnEliminar.isEnabled().isTrue());
			// A continuación se pasará a tratar de eliminar el médico
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Eliminar ahora sí está habilitado
			assertTrue(btnEliminar.isEnabled().isTrue());
			// Eliminamos el medico confirmando la operación en el diálogo de confirmación
			WindowInterceptor.init(btnEliminar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			comprobarCamposVacios();
			// Comprobamos que el usuario ha sido eliminado correctamente
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getDni());
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
		final Window [] windows = new Window[1];
		
		try {
			
			// Creamos un beneficiario de prueba
			beneficiarioPrueba = new Beneficiario ();
			beneficiarioPrueba.setNif(UtilidadesPruebas.generarNIF());
			beneficiarioPrueba.setNss(UtilidadesPruebas.generarNSS());
			beneficiarioPrueba.setNombre("beneficiario");
			beneficiarioPrueba.setApellidos("de prueba");
			beneficiarioPrueba.setFechaNacimiento(new Date("01/01/1980"));
			beneficiarioPrueba.setDireccion(new Direccion("lagasca", "", "", "", "Madrid", "Madrid", 28000));
			beneficiarioPrueba.setCentroSalud(controlador.consultarCentros().firstElement());
			controlador.crearBeneficiario(beneficiarioPrueba);
			
			// Creamos una cita de prueba		
			Cita c = controlador.pedirCita(beneficiarioPrueba, controlador.consultarBeneficiarioPorNIF(beneficiarioPrueba.getNif()).getMedicoAsignado().getDni(), new Date(2010 - 1900, 3, 14, 10, 15), dominio.conocimiento.IConstantes.DURACION_CITA);

			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getDni());
			btnBuscar.click();
			assertEquals(txtNIFBuscado.getText(), "");
			assertEquals(txtNIF.getText(), cabecera.getDni());
			// Comprobamos que, inicalmente, el checkbox Editar está habilitado y no está seleccionado
			assertTrue(chkEditar.isEnabled().isTrue());
			assertTrue(!chkEditar.isSelected().isTrue());
			// Comprobamos que, inicialmente, el botón Eliminar no está habilitado
			assertTrue(!btnEliminar.isEnabled().isTrue());
			// A continuación se pasará a tratar de eliminar el médico
			chkEditar.select();
			assertTrue(chkEditar.isSelected().isTrue());
			// Comprobamos que el boton de Eliminar ahora sí está habilitado
			assertTrue(btnEliminar.isEnabled().isTrue());
			// Capturamos el primer mensaje de confirmacion
			WindowInterceptor.init(btnEliminar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					windows[0]=window;
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			// TODO: no funciona bien
			// Capturamos el segundo mensaje de confirmacion
			WindowInterceptor.init(windows[0].getButton(YES_OPTION).triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			comprobarCamposVacios();
			// Comprobamos que el usuario ha sido eliminado correctamente
			// Buscamos el usuario
			txtNIFBuscado.setText(cabecera.getDni());
			btnBuscar.click();
			WindowInterceptor.init(btnBuscar.triggerClick()).process(new WindowHandler() {
				public Trigger process (Window window) {
					assertTrue(window.titleContains("Error"));
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Como no se encuentra al usuario, el boton Editar tiene que estar deshabilitado
			assertFalse(chkEditar.isEnabled());
			controlador.anularCita(c);
			eliminadoMedico = true;
			System.out.println(eliminadoMedico);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	private void comprobarCamposVacios() {
		assertTrue(txtNIFBuscado.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
		assertTrue(txtApellidos.getText().equals(""));
		assertTrue(txtLogin.getText().equals(""));
		assertTrue(new String(jtxtPassword.getPassword()).equals(""));
		assertTrue(new String(jtxtPasswordConf.getPassword()).equals(""));
		assertTrue(txtCorreoElectronico.getText().equals(""));
		assertTrue(txtTelefonoFijo.getText().equals(""));
		assertTrue(txtTelefonoMovil.getText().equals(""));
		assertTrue(jcmbRol.getSelectedIndex()==-1);
		assertTrue(!btnCalendario.isVisible().isTrue());
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
