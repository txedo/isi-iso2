package pruebas;

import java.util.Date;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.uispec4j.Button;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.Spinner;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import presentacion.auxiliar.OperacionesInterfaz;
import com.toedter.calendar.JDateChooser;
import comunicaciones.ConfiguracionCliente;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorCliente;
import excepciones.FechaSustitucionIncorrectaException;
import excepciones.NIFIncorrectoException;
import excepciones.UsuarioInexistenteException;

/**
 * Pruebas del panel de asignación de sustitutos.
 */
public class PruebasJPSustitucionEstablecer extends org.uispec4j.UISpecTestCase implements IConstantesPruebas {

	private ControladorCliente controlador, controladorAuxiliar;
	private Window winPrincipal;
	
	private Administrador adminPredefinido;
	private Medico pediatra1, pediatra2, pediatra3, pediatra4;
	private Beneficiario beneficiario1;
	
	private Panel panelSustitutos;
	private Panel panelMedico;
	
	private TextBox txtNIFBuscado;
	private TextBox txtNIF;
	private TextBox txtNombre;
	private TextBox txtApellidos;
	private Button btnBuscarUsuario;
	
	private TextBox txtDiaSustitucion;
	private Spinner spnHoraDesde;
	private Spinner spnHoraHasta;
	private Button btnBuscarSustitutos;

	private ListBox lstSustitutos;
	private Button btnAsignarSustituto;
	private Button btnRestablecer;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		Vector<Date> dias;
		Panel panel;
		
		try {
			
			// Establecemos conexión con el servidor e iniciamos sesión como administrador
			controlador = new ControladorCliente();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IDatosConexionPruebas.IP_SERVIDOR_FRONTEND, IDatosConexionPruebas.PUERTO_SERVIDOR_FRONTEND), IDatosConexionPruebas.USUARIO_ADMIN, IDatosConexionPruebas.PASSWORD_ADMIN);
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			
			// Establecemos la operación activa de la ventana principal
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.EstablecerSustituto);

			// Obtenemos el panel de asignación de sustitutos
			panel = winPrincipal.getPanel("jPanelGestionarSustituciones");
			panelSustitutos = panel.getPanel("jPanelEstablecer");
			
			// Obtenemos los componentes del panel
			panelMedico = panelSustitutos.getPanel("pnlMedico");
			// (panel de usuarios)
			txtNIFBuscado = panelMedico.getTextBox("txtNIFBuscado");
			txtNIF = panelMedico.getTextBox("txtNIF");
			txtNombre = panelMedico.getTextBox("txtNombre");
			txtApellidos = panelMedico.getTextBox("txtApellidos");
			btnBuscarUsuario = panelMedico.getButton("btnBuscar");
			// (datos de la sustitución)
			txtDiaSustitucion = new TextBox((JTextField)((JDateChooser)panelSustitutos.getSwingComponents(JDateChooser.class)[0]).getDateEditor());
			spnHoraDesde = panelSustitutos.getSpinner("spnHoraDesde");
			spnHoraHasta = panelSustitutos.getSpinner("spnHoraHasta");
			btnBuscarSustitutos = panelSustitutos.getButton("btnBuscarSustitutos");
			// (lista de sustitutos)
			lstSustitutos = panelSustitutos.getListBox("lstSustitutos");
			btnRestablecer = panelSustitutos.getButton("btnRestablecer");
			btnAsignarSustituto = panelSustitutos.getButton("btnAsignarSustituto");

			// Obtenemos los datos del administrador predefinido
			adminPredefinido = (Administrador)controlador.consultarPropioUsuario();
			
			// Creamos varios médicos de prueba
			pediatra1 = new Medico();
			pediatra1.setNombre("Ana");
			pediatra1.setApellidos("López Ortiz");
			pediatra1.getCalendario().add(new PeriodoTrabajo(10, 14, DiaSemana.Lunes));
			pediatra1.getCalendario().add(new PeriodoTrabajo(9, 13, DiaSemana.Martes));
			pediatra1.getCalendario().add(new PeriodoTrabajo(15, 20, DiaSemana.Jueves));
			pediatra1.getCalendario().add(new PeriodoTrabajo(9, 10, DiaSemana.Viernes));
			pediatra1.setTipoMedico(new Pediatra());
			pediatra1 = (Medico)UtilidadesPruebas.crearUsuario(controlador, pediatra1);
			pediatra2 = new Medico();
			pediatra2.setNombre("José");
			pediatra2.setApellidos("Díaz Romero");
			pediatra2.getCalendario().add(new PeriodoTrabajo(15, 19, DiaSemana.Jueves));
			pediatra2.getCalendario().add(new PeriodoTrabajo(10, 18, DiaSemana.Viernes));
			pediatra2.setTipoMedico(new Pediatra());
			pediatra2 = (Medico)UtilidadesPruebas.crearUsuario(controlador, pediatra2);
			pediatra3 = new Medico();
			pediatra3.setNombre("Juan");
			pediatra3.setApellidos("Pérez Fernández");
			pediatra3.getCalendario().add(new PeriodoTrabajo(10, 14, DiaSemana.Lunes));
			pediatra3.getCalendario().add(new PeriodoTrabajo(15, 19, DiaSemana.Jueves));
			pediatra3.setTipoMedico(new Pediatra());
			pediatra3 = (Medico)UtilidadesPruebas.crearUsuario(controlador, pediatra3);
			
			// Creamos un beneficiario de prueba 
			beneficiario1 = new Beneficiario();
			beneficiario1.setNombre("beneficiario");
			beneficiario1.setApellidos("de prueba");
			beneficiario1.setFechaNacimiento(new Date(2009 - 1900, 1, 1));
			beneficiario1.setDireccion(new Direccion("Calle de Prueba", "", "", "", "Madrid", "Madrid", 28000));
			beneficiario1.setCentroSalud(pediatra1.getCentroSalud());
			beneficiario1.setMedicoAsignado(pediatra1);
			beneficiario1 = UtilidadesPruebas.crearBeneficiario(controlador, beneficiario1);
			
			// Asignamos varias sustituciones
			dias = new Vector<Date>();
			dias.add(new Date(2015 - 1900, 4, 5)); // Martes 5/Mayo/2015
			controlador.asignarSustituto(pediatra1, dias, 10, 12, pediatra2);
			dias = new Vector<Date>();
			dias.add(new Date(2015 - 1900, 4, 8)); // Viernes 8/Mayo/2015
			controlador.asignarSustituto(pediatra2, dias, 14, 20, pediatra1);
			
			// Pedimos varias citas
			controlador.pedirCita(beneficiario1, pediatra1.getNif(), new Date(2015 - 1900, 4, 12, 10, 30, 0), IConstantes.DURACION_CITA);
			controlador.pedirCita(beneficiario1, pediatra1.getNif(), new Date(2015 - 1900, 4, 19, 10, 30, 0), IConstantes.DURACION_CITA);
			
			// Comprobamos que el panel está vacío inicialmente
			comprobarPanelUsuarioVacio();
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesión auxiliar de las pruebas del observador
			UtilidadesPruebas.cerrarControladorAuxiliar();
			// Eliminamos todos los usuarios y beneficiarios creados
			try {
				controlador.eliminarUsuario(pediatra1);
			} catch(UsuarioInexistenteException e) {
			}
			controlador.eliminarUsuario(pediatra2);
			controlador.eliminarUsuario(pediatra3);
			if(pediatra4 != null) {
				try {
					controlador.eliminarUsuario(pediatra4);
				} catch(UsuarioInexistenteException e) {
				}
			}
			controlador.eliminarBeneficiario(beneficiario1);
			// Cerramos la sesión y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con datos incorrectos para el médico sustituido */
	public void testDatosSustituidoIncorrecto() {			
		try {
			// Ponemos un NIF vacío y comprobamos que todo sigue deshabilitado
			txtNIFBuscado.setText("");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), new NIFIncorrectoException().getMessage());
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Ponemos un NIF incorrecto y comprobamos que se muestra
			// un error porque el NIF tiene un formato inválido
			txtNIFBuscado.setText("111111");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), new NIFIncorrectoException().getMessage());
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con un médico sustituido inexistente o que no es médico */
	public void testSustituidoInexistente() {			
		try {
			// Ponemos el NIF de un usuario que no está dado de alta en el sistema
			txtNIFBuscado.setText("00000000A");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "No existe ningún médico con el NIF introducido.");
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Ponemos el NIF de un usuario que no es médico
			txtNIFBuscado.setText(adminPredefinido.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "El NIF introducido no pertenece a un médico.");
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas con fechas inválidas para la sustitución */
	public void testFechaSustitucionInvalida() {
		try {
			// Ponemos el NIF de un médico válido
			txtNIFBuscado.setText(pediatra1.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "Médico encontrado.");
			comprobarPanelUsuarioRelleno(pediatra1);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Dejamos la fecha de la sustitución en blanco
			txtDiaSustitucion.setText("");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), new FechaSustitucionIncorrectaException().getMessage());
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Ponemos una fecha con un formato no válido
			txtDiaSustitucion.setText("4/a/20xp");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), new FechaSustitucionIncorrectaException().getMessage());
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Ponemos una fecha anterior a la actual
			txtDiaSustitucion.setText("1/1/2005");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), new FechaSustitucionIncorrectaException().getMessage());
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Ponemos una fecha válida pero una hora de inicio inválida
			txtDiaSustitucion.setText("10/10/2015");
			spnHoraDesde.setValue(-1);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "La hora inicial de la sustitución queda fuera del horario laboral de los médicos.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Ponemos una fecha válida pero una hora de final inválida
			spnHoraDesde.setValue(14);
			spnHoraHasta.setValue(30);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "La hora final de la sustitución queda fuera del horario laboral de los médicos.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Ponemos las horas inicial y final intercambiadas
			spnHoraDesde.setValue(20);
			spnHoraHasta.setValue(14);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "La hora final de la sustitución debe ser mayor que la inicial.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos buscar los sustitutos para un día en el que el
			// médico sustituido no tiene trabajo
			txtDiaSustitucion.setText("13/05/2015"); // Miércoles 13/Mayo/2015
			spnHoraDesde.setValue(IConstantes.HORA_INICIO_JORNADA);
			spnHoraHasta.setValue(IConstantes.HORA_FIN_JORNADA);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "El médico que se quiere sustituir no trabaja en la fecha y horas indicadas.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Intentamos buscar los sustitutos para un día en el que el
			// médico trabaja pero no a las horas indicadas
			txtDiaSustitucion.setText("11/05/2015"); // Lunes 11/Mayo/2015
			spnHoraDesde.setValue(16);
			spnHoraHasta.setValue(20);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "El médico que se quiere sustituir no trabaja en la fecha y horas indicadas.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas con fechas de sustitución válidas pero en las que no hay sustitutos posibles */
	public void testNingunSustituto() {
		try {
			// Ponemos el NIF de un médico válido
			txtNIFBuscado.setText(pediatra1.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "Médico encontrado.");
			comprobarPanelUsuarioRelleno(pediatra1);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos buscar sustitutos para un día en el que el
			// médico sustituido está sustituyendo a otro médico
			txtDiaSustitucion.setText("8/5/2015"); // Viernes 8/Mayo/2015
			spnHoraDesde.setValue(IConstantes.HORA_INICIO_JORNADA);
			spnHoraHasta.setValue(IConstantes.HORA_FIN_JORNADA);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "El médico que se quiere sustituir tiene una sustitución asignada en todas o algunas de las horas indicadas.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Intentamos buscar sustitutos para un día en el que el
			// médico sustituido está sustituyendo a otro médico
			txtDiaSustitucion.setText("5/5/2015"); // Martes 5/Mayo/2015
			spnHoraDesde.setValue(IConstantes.HORA_INICIO_JORNADA);
			spnHoraHasta.setValue(IConstantes.HORA_FIN_JORNADA);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "El médico que se quiere sustituir ya está siendo sustituido por otro médico en todas o algunas de las horas indicadas.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos buscar sustitutos para un día en el que
			// ningún médico puede hacer la sustitución
			txtDiaSustitucion.setText("7/5/2015"); // Jueves 7/Mayo/2015
			spnHoraDesde.setValue(17);
			spnHoraHasta.setValue(20);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "No se ha encontrado ningún médico que pueda hacer la sustitución solicitada.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas sustituciones válidas */
	public void testSustitucionValida() {
		try {
			// Ponemos el NIF de un médico válido
			txtNIFBuscado.setText(pediatra1.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "Médico encontrado.");
			comprobarPanelUsuarioRelleno(pediatra1);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ponemos los datos de una sustitución en la que hay un único posible sustituto
			txtDiaSustitucion.setText("11/5/2015"); // Lunes 11/Mayo/2015
			spnHoraDesde.setValue(10);
			spnHoraHasta.setValue(13);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "Se ha encontrado 1 posible sustituto.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2 }));
			// Ponemos los datos de una sustitución en la que hay dos posibles sustitutos
			txtDiaSustitucion.setText("12/5/2015"); // Martes 12/Mayo/2015
			spnHoraDesde.setValue(9);
			spnHoraHasta.setValue(12);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "Se han encontrado 2 posibles sustitutos.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2, pediatra3 }));
			// Intentamos asignar un sustituto antes de seleccionarlo
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnAsignarSustituto, OK_OPTION), "Seleccione el médico que hará la sustitución.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ponemos los datos de una sustitución válida que no afecta a ninguna cita
			txtDiaSustitucion.setText("11/5/2015"); // Lunes 11/Mayo/2015
			spnHoraDesde.setValue(10);
			spnHoraHasta.setValue(15);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "Se ha encontrado 1 posible sustituto.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2 }));
			// Asignamos el sustituto
			lstSustitutos.selectIndex(0);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnAsignarSustituto, OK_OPTION), "La sustitución se ha almacenado correctamente.");
			comprobarPanelUsuarioVacio();
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ponemos el NIF de un médico válido
			txtNIFBuscado.setText(pediatra1.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "Médico encontrado.");
			comprobarPanelUsuarioRelleno(pediatra1);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ponemos los datos de una sustitución válida que afecta a una cita
			txtDiaSustitucion.setText("12/5/2015"); // Martes 12/Mayo/2015
			spnHoraDesde.setValue(10);
			spnHoraHasta.setValue(15);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "Se han encontrado 2 posibles sustitutos.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2, pediatra3 }));
			// Asignamos el sustituto
			lstSustitutos.selectIndex(0);
			WindowInterceptor.init(btnAsignarSustituto.triggerClick()).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Aviso de asignación correcta
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Ventana con las citas afectadas
					return window.getButton(CLOSE_OPTION).triggerClick();
				}
			}).run();
			comprobarPanelUsuarioVacio();
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas del botón Restablecer */
	public void testRestablecer() {
		try {
			// Ponemos el NIF de un médico válido
			txtNIFBuscado.setText(pediatra1.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "Médico encontrado.");
			comprobarPanelUsuarioRelleno(pediatra1);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Restablecemos el panel
			btnRestablecer.click();
			comprobarPanelUsuarioVacio();
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ponemos el NIF de un médico válido
			txtNIFBuscado.setText(pediatra1.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "Médico encontrado.");
			comprobarPanelUsuarioRelleno(pediatra1);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			// Ponemos los datos de una sustitución en la que hay un único posible sustituto
			txtDiaSustitucion.setText("11/5/2015"); // Lunes 11/Mayo/2015
			spnHoraDesde.setValue(10);
			spnHoraHasta.setValue(13);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "Se ha encontrado 1 posible sustituto.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2 }));
			// Restablecemos el panel
			btnRestablecer.click();
			comprobarPanelUsuarioVacio();
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas del observador relacionadas con los usuarios */
	public void testObservadorUsuarios() {
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Buscamos los sutitutos para un cierto médico
			txtNIFBuscado.setText(pediatra1.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarUsuario, OK_OPTION), "Médico encontrado.");
			comprobarPanelUsuarioRelleno(pediatra1);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
			txtDiaSustitucion.setText("12/5/2015"); // Martes 12/Mayo/2015
			spnHoraDesde.setValue(10);
			spnHoraHasta.setValue(15);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "Se han encontrado 2 posibles sustitutos.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2, pediatra3 }));
			// Registramos un nuevo médico con el controlador auxiliar que se deberá
			// mostrar como sustituto posible en la ventana del controlador principal 
			pediatra4 = new Medico();
			pediatra4.setNif(UtilidadesPruebas.generarNIF());
			pediatra4.setLogin(UtilidadesPruebas.generarLogin());
			pediatra4.setPassword(pediatra4.getLogin());
			pediatra4.setNombre("Susana");
			pediatra4.setApellidos("Blanco Romero");
			pediatra4.setTipoMedico(new Pediatra());
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.crearMedico(pediatra4);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del nuevo usuario registrado
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que indica los resultados de la nueva
					// búsqueda de sustitutos; ahora debe haber 3 sustitutos en lugar de 2
					assertEquals(((JOptionPane)((JDialog)window.getAwtComponent()).getContentPane().getComponents()[0]).getMessage().toString(), "Se han encontrado 3 posibles sustitutos.");
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2, pediatra3, pediatra4 }));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Eliminamos el médico recién creado desde el controlador auxiliar para
			// comprobar que se elimina en la ventana del controlador principal
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(pediatra4);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del usuario eliminado
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que indica los resultados de la nueva
					// búsqueda de sustitutos; ahora debe volver a haber 2 sustitutos
					assertEquals(((JOptionPane)((JDialog)window.getAwtComponent()).getContentPane().getComponents()[0]).getMessage().toString(), "Se han encontrado 2 posibles sustitutos.");
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2, pediatra3 }));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos los datos del médico sustituido mostrado en la ventana del
			// controlador principal desde el controlador auxiliar
			pediatra1.setNombre("Ana2");
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.modificarMedico(pediatra1);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio del usuario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Buscamos los sutitutos en un cierto día
			txtDiaSustitucion.setText("11/5/2015"); // Lunes 11/Mayo/2015
			spnHoraDesde.setValue(10);
			spnHoraHasta.setValue(15);
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarSustitutos, OK_OPTION), "Se ha encontrado 1 posible sustituto.");
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2 }));
			// Modificamos desde el controlador auxiliar los datos de un médico que podría
			// ser un posible sustituto, para ver si se muestra en el controlador principal
			pediatra3.getCalendario().clear();
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.modificarMedico(pediatra3);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del usuario modificado
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que indica los resultados de la nueva
					// búsqueda de sustitutos; ahora debe haber 2 sustitutos en lugar de 1
					assertEquals(((JOptionPane)((JDialog)window.getAwtComponent()).getContentPane().getComponents()[0]).getMessage().toString(), "Se han encontrado 2 posibles sustitutos.");
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra2, pediatra3 }));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos desde el controlador auxiliar el calendario de un posible médico
			// sustituto para ver si se deja de mostrar en la ventana del controlador principal
			pediatra2.getCalendario().add(new PeriodoTrabajo(10, 20, DiaSemana.Lunes));
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.modificarMedico(pediatra2);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del usuario modificado
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que indica los resultados de la nueva
					// búsqueda de sustitutos; ahora debe volver a haber 1 sustituto
					assertEquals(((JOptionPane)((JDialog)window.getAwtComponent()).getContentPane().getComponents()[0]).getMessage().toString(), "Se ha encontrado 1 posible sustituto.");
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			comprobarDatosSustitucionHabilitado();
			comprobarListaSustitutosHabilitada();
			assertTrue(UtilidadesPruebas.comprobarListaUsuarios(lstSustitutos, new Usuario[] { pediatra3 }));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Por último, eliminamos desde el controlador auxiliar el médico para el que
			// estábamos buscando un sustituto, para borrar el panel del controlador principal
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(pediatra1);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del usuario eliminado
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			comprobarPanelUsuarioVacio();
			comprobarDatosSustitucionDeshabilitado();
			comprobarListaSustitutosDeshabilitada();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	private void comprobarPanelUsuarioVacio() {
		assertEquals("", txtNIFBuscado.getText());
		assertEquals("", txtNIF.getText());
		assertEquals("", txtNombre.getText());
		assertEquals("", txtApellidos.getText());
	}
	
	private void comprobarPanelUsuarioRelleno(Usuario usuario) {
		assertEquals(usuario.getNif(), txtNIF.getText());
		assertEquals(usuario.getNombre(), txtNombre.getText());
		assertEquals(usuario.getApellidos(), txtApellidos.getText());
	}
	
	private void comprobarDatosSustitucionDeshabilitado() {
		assertFalse(txtDiaSustitucion.isEnabled());
		assertFalse(spnHoraDesde.isEnabled());
		assertFalse(spnHoraHasta.isEnabled());
		assertFalse(btnBuscarSustitutos.isEnabled());
		assertEquals("", txtDiaSustitucion.getText());
		assertTrue(spnHoraDesde.valueEquals(IConstantes.HORA_INICIO_JORNADA));
		assertTrue(spnHoraHasta.valueEquals(IConstantes.HORA_FIN_JORNADA));
	}

	private void comprobarDatosSustitucionHabilitado() {
		assertTrue(txtDiaSustitucion.isEnabled());
		assertTrue(spnHoraDesde.isEnabled());
		assertTrue(spnHoraHasta.isEnabled());
		assertTrue(btnBuscarSustitutos.isEnabled());
	}

	private void comprobarListaSustitutosDeshabilitada() {
		assertFalse(lstSustitutos.isEnabled());
		assertFalse(btnAsignarSustituto.isEnabled());
		assertTrue(lstSustitutos.isEmpty());
		assertTrue(btnRestablecer.isEnabled());
	}

	private void comprobarListaSustitutosHabilitada() {
		assertTrue(lstSustitutos.isEnabled());
		assertTrue(btnAsignarSustituto.isEnabled());
		assertTrue(btnRestablecer.isEnabled());
		assertFalse(lstSustitutos.isEmpty());
	}
	
}
