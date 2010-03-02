package pruebas;

import java.util.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.ComboBox;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import com.toedter.calendar.JDateChooser;

import comunicaciones.ConfiguracionCliente;
import comunicaciones.RemotoCliente;

import presentacion.JPCitaTramitar;
import presentacion.auxiliar.OperacionesInterfaz;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.control.Cliente;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.UsuarioYaExistenteException;

public class PruebasJPCitaTramitar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {
	
	private final int horaInicio = 10;
	private final int horaFinal = 16;
	
	private ControladorCliente controlador;
	private ControladorCliente controladorAuxiliar;
	private JPCitaTramitar panel;
	private Panel panelBeneficiario;
	private Panel pnlPanel;
	private Window winPrincipal;
	
	private Button btnBuscar;
	private ComboBox cmbIdentificacion;
	private TextBox txtIdentificacion;
	private TextBox txtNIF;
	private TextBox txtNSS;
	private TextBox txtNombre;
	private TextBox txtApellidos;	
	private ComboBox cmbCentros;
	private TextBox txtMedicoAsignado;
	private TextBox txtFechaCita;
	private ComboBox cmbHorasCitas;
	private TextBox txtMedico;
	private Button btnRegistrar;
	
	private JComboBox jcmbIdentificacion;
	private JTextField jtxtIdentificacion;
	private JTextField jtxtNIF;
	private JTextField jtxtNSS;
	private JTextField jtxtNombre;
	private JTextField jtxtApellidos;
	private JComboBox jcmbCentros;
	private JTextField jtxtMedicoAsignado;
	private JComboBox jcmbHorasCitas;
	
	private Medico cabecera;
	private TipoMedico tCabecera;
	private Beneficiario beneficiarioPrueba;
	private PeriodoTrabajo periodo1;
	private Medico medicoAsignado;
	
	private Vector<Medico> medicosEliminados;
	
	
	protected void setUp() {
		boolean valido = true;
		String login;
		medicosEliminados = new Vector<Medico>();
		
		try {
			// Establecemos conexión con el servidor front-end
			controlador = new ControladorCliente();
			// Iniciamos sesion como administrador
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IPServidorFrontend, puertoServidorFrontend), usuarioAdmin, passwordAdmin);
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			
			// Creamos e insertamos un médico y un beneficiario			
			tCabecera = new Cabecera();
			login = UtilidadesPruebas.generarLogin();
			cabecera = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Eduardo", "PC", "", "", "", tCabecera);
			periodo1 = new PeriodoTrabajo(horaInicio, horaFinal, DiaSemana.Miercoles);
			cabecera.getCalendario().add(periodo1);
			
			// Mientras existan los usuarios, se genera otro login y otro NIF
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
			
			// Consultamos el médico de nuevo, porque el centro de salud que realmente se le asigna
			// se hace de manera aleatoria
			cabecera = controlador.consultarMedico(cabecera.getNif());

			// Creamos el beneficiario en el mismo centro que el médico, para que no se le asigne otro diferente 
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
			do {
				try {					
					controlador.crearBeneficiario(beneficiarioPrueba);
					valido = true;
				} catch (BeneficiarioYaExistenteException e) {
					beneficiarioPrueba.setNif(UtilidadesPruebas.generarNIF());					
					beneficiarioPrueba.setNss(UtilidadesPruebas.generarNSS());
					valido = false;
				}
			}while(!valido);
			medicoAsignado = cabecera;

			// Obtenemos el panel
			Panel p1 = winPrincipal.getPanel("jPanelGestionarCitas");
			panel = (JPCitaTramitar)p1.getPanel("jPanelTramitar").getAwtContainer();
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panel);
			panelBeneficiario = pnlPanel.getPanel("pnlBeneficiario");
			cmbIdentificacion = panelBeneficiario.getComboBox("cmbIdentificacion");
			txtIdentificacion = panelBeneficiario.getTextBox("txtIdentificacion");
			txtNIF = panelBeneficiario.getTextBox("txtNIF");
			txtNSS = panelBeneficiario.getTextBox("txtNSS");
			txtNombre = panelBeneficiario.getTextBox("txtNombre");
			txtApellidos = panelBeneficiario.getTextBox("txtApellidos");
			txtMedicoAsignado = panelBeneficiario.getTextBox("txtMedicoAsignado");
			cmbCentros = panelBeneficiario.getComboBox("cmbCentros");
			btnBuscar = panelBeneficiario.getButton("btnBuscar");
			
			txtFechaCita = new TextBox((JTextField)((JDateChooser)pnlPanel.getSwingComponents(JDateChooser.class)[0]).getDateEditor());
			cmbHorasCitas = pnlPanel.getComboBox("cmbHorasCitas");
			txtMedico = pnlPanel.getTextBox("txtMedico");
			btnRegistrar = pnlPanel.getButton("btnRegistrar");
			
			jcmbIdentificacion = (JComboBox)cmbIdentificacion.getAwtComponent();
			jtxtIdentificacion = (JTextField)txtIdentificacion.getAwtComponent();
			jtxtNIF = (JTextField)txtNIF.getAwtComponent();
			jtxtNSS = (JTextField)txtNSS.getAwtComponent();
			jtxtNombre = (JTextField)txtNombre.getAwtComponent();
			jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
			jtxtMedicoAsignado = (JTextField)txtMedicoAsignado.getAwtComponent();
			jcmbCentros = (JComboBox)cmbCentros.getAwtComponent();
			jcmbHorasCitas = (JComboBox)cmbHorasCitas.getAwtComponent();
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		// Para borrar los datos, hay que entrar con la sesión del administrador
		try {
			controlador.cerrarSesion();
			winPrincipal.dispose();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IPServidorFrontend, puertoServidorFrontend), usuarioAdmin, passwordAdmin);
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			// Restauramos los médicos que se hayan podido eliminar
			for (Medico m : medicosEliminados) {
				controlador.crearMedico(m);
			}
			// Eliminamos el medico de prueba y el beneficiario de prueba
			controlador.eliminarMedico(cabecera);
			controlador.eliminarBeneficiario(beneficiarioPrueba);
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
		String[] invalidos;
		
		try {
			// Buscamos un beneficiario por su NIF
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			// Inicialmente probamos con un NIF nulo
			txtIdentificacion.setText("");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Debe introducir un NIF o un NSS.");
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inválido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// Probamos con un NIF que no esté dado de alta en el sistema y NO lo damos de alta en el sistema
			txtIdentificacion.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, NO_OPTION), "El beneficiario con NIF 00000000A no se encuentra dado de alta en el sistema.\n¿Quiere registrarlo para poder tramitar su cita?");
			// Buscamos un beneficiario por su NSS
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			// Ponemos un NSS incorrecto y comprobamos que el campo del
			// identificacion se selecciona por tener un formato inválido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NSSIncorrectoException().getMessage());
			// Probamos con un NSS que no esté dado de alta en el sistema y NO lo damos de alta en el sistema
			txtIdentificacion.setText("000000000000");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, NO_OPTION), "El beneficiario con NSS 000000000000 no se encuentra dado de alta en el sistema.\n¿Quiere registrarlo para poder tramitar su cita?");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testBuscarBeneficiarioPorNIFSinMedicoAsignado () {
		// Borramos todos los médicos del tipo de médico asignado al beneficiario
		// para evitar que el sistema le asigne otro automáticamente
		try {
			medicosEliminados = controlador.obtenerMedicosTipo(tCabecera);
			for (Medico m : medicosEliminados) {
				controlador.eliminarMedico(m);
			}
		} catch(Exception e) {
			fail(e.toString());
		}
		// Le desasignamos el médico al beneficiario que tenemos en memoria
		// el servidor automaticamente ha actualizado su estado en la base de datos
		beneficiarioPrueba.setMedicoAsignado(null);
		// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		String esperado = "El beneficiario con NIF " + beneficiarioPrueba.getNif() + " no puede pedir cita\nporque no tiene ningún médico asignado.";
		assertEquals(UtilidadesPruebas.obtenerTextoSegundoDialogo(btnBuscar, OK_OPTION, OK_OPTION), esperado);
		// Se comprueba que NO tiene médico asignado
		assertEquals("(ninguno)", txtMedicoAsignado.getText());
		// Todos los componentes de pedir cita deben estar deshabilitados
		comprobarControlesDeshabilitados();
	}

	public void testBuscarBeneficiarioPorNSSSinMedicoAsignado () {
		// Borramos todos los médicos del tipo de médico asignado al beneficiario
		// para evitar que el sistema le asigne otro automáticamente
		try {
			medicosEliminados = controlador.obtenerMedicosTipo(tCabecera);
			for (Medico m : medicosEliminados) {
				controlador.eliminarMedico(m);
			}
		} catch(Exception e) {
			fail(e.toString());
		}
		// Le desasignamos el médico al beneficiario que tenemos en memoria
		// el servidor automaticamente ha actualizado su estado en la base de datos
		beneficiarioPrueba.setMedicoAsignado(null);
		// Probamos con el NSS de beneficiarioPrueba que es correcto y está dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(1);
		txtIdentificacion.setText(beneficiarioPrueba.getNss());
		String esperado = "El beneficiario con NIF " + beneficiarioPrueba.getNif() + " no puede pedir cita\nporque no tiene ningún médico asignado.";
		assertEquals(UtilidadesPruebas.obtenerTextoSegundoDialogo(btnBuscar, OK_OPTION, OK_OPTION), esperado);
		// Se comprueba que NO tiene médico asignado
		assertEquals("(ninguno)", txtMedicoAsignado.getText());
		// Todos los componentes de pedir cita deben estan deshabilitados
		comprobarControlesDeshabilitados();
	}
	
	public void testTramitarCita () {
		String esperado = "";
		// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
		// y que además tiene un médico asignado
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), "Beneficiario encontrado.");
		// Se comprueba que tiene médico asignado
		assertEquals(medicoAsignado.getApellidos() + ", " + medicoAsignado.getNombre() + " (" + medicoAsignado.getNif() + ")", txtMedicoAsignado.getText());
		// Comprobamos que los componentes se han habilitado
		assertTrue(txtFechaCita.isEnabled());
		assertTrue(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertTrue(btnRegistrar.isEnabled());
		// Ponemos una fecha en la que no trabaja el médico, para ello eliminamos su periodo de trabajo
		// Como el médico trabaja los miércoles, seleccionamos un martes
		txtFechaCita.setText("17/10/2010");
		esperado = "El día seleccionado no es laboral para el médico";
		assertEquals(esperado, (String)jcmbHorasCitas.getSelectedItem());
		assertEquals(1, jcmbHorasCitas.getItemCount());
		// Ponemos una fecha en la que sí trabaja el médico
		txtFechaCita.setText("20/10/2010");
		assertEquals((horaFinal-horaInicio)*4, jcmbHorasCitas.getItemCount());
		// Seleccionamos la primera hora disponible y tramitamos la cita
		jcmbHorasCitas.grabFocus();
		jcmbHorasCitas.setSelectedIndex(0);
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnRegistrar, OK_OPTION), "La cita ha quedado registrada.");
		// Comprobamos que los campos se han reseteado
		comprobarCamposRestablecidos();
		comprobarControlesDeshabilitados();
		// Volvemos a consultar el mismo beneficiario
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), "Beneficiario encontrado.");
		// Se comprueba que tiene médico asignado
		assertEquals(medicoAsignado.getApellidos() + ", " + medicoAsignado.getNombre() + " (" + medicoAsignado.getNif() + ")", txtMedicoAsignado.getText());
		// Comprobamos que los componentes se han habilitado de nuevo
		assertTrue(txtFechaCita.isEnabled());
		assertTrue(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertTrue(btnRegistrar.isEnabled());
		// Ponemos la misma fecha en la que tramitamos la cita
		txtFechaCita.setText("20/10/2010");
		assertEquals((horaFinal-horaInicio)*4, jcmbHorasCitas.getItemCount());
		// Comprobamos que aparece seleccionada por defecto la segunda hora disponible porque la primera ya está asignada
		assertTrue(jcmbHorasCitas.getSelectedIndex() == 1);
		// Seleccionamos la primera hora (la misma que registramos antes)
		jcmbHorasCitas.setSelectedIndex(0);
		// Tramitamos la cita y vemos que la operacion no es posible
		esperado = "Seleccione un día que sea laboral para el médico y una hora libre (no marcada en rojo) para registrar la cita.";
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnRegistrar, OK_OPTION), esperado);
	}
	
	@SuppressWarnings("deprecation")
	public void testObservadorCitaRegistrada () {
		// Iniciamos sesión con un segundo administrador
		controladorAuxiliar = new ControladorCliente();
		Window winPrincipal2 = WindowInterceptor.run(new Trigger() {
			public void run() {
				try {
					controladorAuxiliar.iniciarSesion(new ConfiguracionCliente(IPServidorFrontend, puertoServidorFrontend), usuarioAdminAuxiliar, passwordAdminAuxiliar);
					// Ahora el controlador del proxy se ha cambiado al controlador auxiliar
					// Volvemos a restablecer en el proxy el controlador principal de las pruebas
					((Cliente)(RemotoCliente.getCliente().getClienteExportado())).setControlador(controlador);
				} catch(Exception e) {
					fail(e.toString());
				}
			}
		});
		// Indicamos que la operación activa del primer administador es la de tramitar cita
		controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.TramitarCita);
		// El primer administrador busca al beneficiario de prueba
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), "Beneficiario encontrado.");
		// Se comprueba que tiene médico asignado
		assertEquals(medicoAsignado.getApellidos() + ", " + medicoAsignado.getNombre() + " (" + medicoAsignado.getNif() + ")", txtMedicoAsignado.getText());
		// Comprobamos que los componentes se han habilitado
		assertTrue(txtFechaCita.isEnabled());
		assertTrue(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertTrue(btnRegistrar.isEnabled());
		// Se selecciona un día en el que trabajará el médico
		txtFechaCita.setText("20/10/2010");
		assertEquals((horaFinal-horaInicio)*4, jcmbHorasCitas.getItemCount());
		// Comprobamos que aparece seleccionada por defecto la primera hora disponible
		assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
		try {
			// En este momento el segundo administrador pide una cita en la hora seleccionada por el primero
			Trigger t = new Trigger() {
				@Override
				public void run() throws Exception {
					controladorAuxiliar.pedirCita(beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado().getNif(), new Date(2010-1900,10-1,20,horaInicio,0), IConstantes.DURACION_CITA);
				}
			};
			WindowInterceptor.init(t).process(new WindowHandler() {
				public Trigger process(Window window) {

					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(500);
			// La ventana del primer administrador se ha debido actualizar seleccionando la siguiente hora disponible en el día que se ha pedido cita desde el administrador auxiliar
			txtFechaCita.setText("20/10/2010");
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 1);
			controladorAuxiliar.cerrarSesion();
			controladorAuxiliar.cerrarControlador();
		} catch (Exception e) {
			fail (e.toString());
		}
		winPrincipal2.dispose();
	}

	private void comprobarCamposRestablecidos () {
		assertTrue(txtIdentificacion.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNSS.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
		assertTrue(txtApellidos.getText().equals(""));
		assertFalse(jcmbCentros.isEnabled());
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(txtFechaCita.getText().equals(""));
		assertTrue(jcmbHorasCitas.getItemCount() == 0);
		assertTrue(txtMedico.getText().equals(""));
	}
	
	private void comprobarControlesDeshabilitados() {
		assertFalse(txtFechaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
	}

}
