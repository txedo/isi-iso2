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
import presentacion.JPCitaTramitar;
import presentacion.auxiliar.OperacionesInterfaz;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.control.ControladorCliente;
import excepciones.FechaCitaIncorrectaException;
import excepciones.FormatoFechaIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;

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
	private JComboBox jcmbCentros;
	private JComboBox jcmbHorasCitas;
	
	private Medico cabecera;
	private TipoMedico tCabecera;
	private Beneficiario beneficiarioPrueba;
	private Medico medicoAsignado;
	
	private Vector<Medico> medicosEliminados;
	private boolean eliminado;
	
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		medicosEliminados = new Vector<Medico>();
		eliminado = false;
		
		try {
			
			// Establecemos conexión con el servidor e iniciamos sesión como administrador
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

			// Establecemos la operación activa de la ventana principal
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.TramitarCita);

			// Creamos un médico de cabecera 
			tCabecera = new Cabecera();
			cabecera = new Medico();
			cabecera.setNombre("Eduardo");
			cabecera.setApellidos("Ramírez García");
			cabecera.setTipoMedico(tCabecera);
			cabecera.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			cabecera = (Medico)UtilidadesPruebas.crearUsuario(controlador, cabecera); 

			// Creamos el beneficiario en el mismo centro que el médico, para que no se le asigne otro diferente 
			beneficiarioPrueba = new Beneficiario ();
			beneficiarioPrueba.setNombre("beneficiario");
			beneficiarioPrueba.setApellidos("de prueba");
			beneficiarioPrueba.setFechaNacimiento(new Date("01/01/1980"));
			beneficiarioPrueba.setDireccion(new Direccion("lagasca", "", "", "", "Madrid", "Madrid", 28000));
			beneficiarioPrueba.setCentroSalud(cabecera.getCentroSalud());
			beneficiarioPrueba.setMedicoAsignado(cabecera);
			beneficiarioPrueba = UtilidadesPruebas.crearBeneficiario(controlador, beneficiarioPrueba);
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
			jcmbCentros = (JComboBox)cmbCentros.getAwtComponent();
			jcmbHorasCitas = (JComboBox)cmbHorasCitas.getAwtComponent();
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesión auxiliar de las pruebas del observador
			UtilidadesPruebas.cerrarControladorAuxiliar();
			
			// Restauramos los médicos que se hayan podido eliminar
			for (Medico m : medicosEliminados) {
				controlador.crearMedico(m);
			}
			// Eliminamos el medico de prueba y el beneficiario de prueba
			controlador.eliminarMedico(cabecera);
			if (!eliminado) controlador.eliminarBeneficiario(beneficiarioPrueba);
			// Cerramos la sesión y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas con datos no válidos */
	public void testDatosInvalidos() {
		
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
		String esperado = "El beneficiario seleccionado no puede pedir cita\nporque no tiene ningún médico asignado.";
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
		String esperado = "El beneficiario seleccionado no puede pedir cita\nporque no tiene ningún médico asignado.";
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
		txtFechaCita.setText("03/03/2015");
		esperado = "El día seleccionado no es laboral para el médico";
		assertEquals(esperado, (String)jcmbHorasCitas.getSelectedItem());
		assertEquals(1, jcmbHorasCitas.getItemCount());
		// Ponemos una fecha anterior a la actual (es decir, una fecha caducada)
		txtFechaCita.setText("01/01/2009");
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnRegistrar, OK_OPTION), new FechaCitaIncorrectaException().getLocalizedMessage());
		// Ponemos un formato de fecha que no es válido
		txtFechaCita.setText("asdf");
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnRegistrar, OK_OPTION), new FormatoFechaIncorrectoException().getLocalizedMessage());
		// Ponemos una fecha en la que sí trabaja el médico
		txtFechaCita.setText("04/03/2015");
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
		txtFechaCita.setText("04/03/2015");
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
	public void testObservadorCitas() {
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.usuarioAdminAuxiliar, IDatosConexionPruebas.passwordAdminAuxiliar);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
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
			// Seleccionamos un día en el que trabajará el médico
			txtFechaCita.setText("04/03/2015");
			assertEquals(txtMedico.getText(), beneficiarioPrueba.getMedicoAsignado().getApellidos() + ", " + beneficiarioPrueba.getMedicoAsignado().getNombre() + " (" + beneficiarioPrueba.getMedicoAsignado().getNif() + ")");
			assertEquals((horaFinal - horaInicio) * 4, jcmbHorasCitas.getItemCount());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			// Solicitamos una cita con el segundo administrador conectado en la hora seleccionada
			// por el primero para ver que se pasa a mostrar la cita como ocupada
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.pedirCita(beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado().getNif(), new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la nueva cita
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar marcando la
			// hora que estaba seleccionada como no válida (por defecto, se mantiene la misma
			// hora seleccionada al actualizar la ventana, aunque deje de estar disponible)
			assertEquals(txtFechaCita.getText(), "04/03/2015");
			assertEquals(txtMedico.getText(), "(fecha no válida)");
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ahora procedemos a eliminar la cita desde el segundo administrador
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.anularCita(new Cita(new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA, beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado()));
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la anulación de la cita
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar marcando la
			// hora que estaba seleccionada como válida
			assertEquals(txtFechaCita.getText(), "04/03/2015");
			assertEquals(txtMedico.getText(), beneficiarioPrueba.getMedicoAsignado().getApellidos() + ", " + beneficiarioPrueba.getMedicoAsignado().getNombre() + " (" + beneficiarioPrueba.getMedicoAsignado().getNif() + ")");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testObservadorUsuarioActualizadoEliminado () {
		final PeriodoTrabajo periodo2 = new PeriodoTrabajo(horaInicio, horaFinal-2, DiaSemana.Miercoles);
		
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.usuarioAdminAuxiliar, IDatosConexionPruebas.passwordAdminAuxiliar);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
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
			txtFechaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*4, jcmbHorasCitas.getItemCount());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			// En este momento el segundo administrador modifica el calendario del médico
			cabecera.getCalendario().clear();
			cabecera.getCalendario().add(periodo2);
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					// Reemplazamos el periodo de trabajo que ya tenía el médico
					controladorAuxiliar.modificarMedico(cabecera);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio del médico
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar teniendo menos horas en el combobox
			txtFechaCita.setText("04/03/2015");
			assertEquals((horaFinal-2-horaInicio)*4, jcmbHorasCitas.getItemCount());
			
			// Ahora procedemos a eliminar el médico desde el segundo administrador
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(cabecera);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminación del médico asignado al beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar borrando los campos, pues el beneficiario se ha quedado sin médico asignado
			comprobarCamposRestablecidos();
			medicosEliminados.add(cabecera);
		} catch (Exception e) {
			fail (e.toString());
		}
	}
	
	public void testObservadorBeneficiarioActualizadoEliminado () {
		// Iniciamos sesión con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.usuarioAdminAuxiliar, IDatosConexionPruebas.passwordAdminAuxiliar);
		} catch(Exception e) {
			fail(e.toString());
		}
		
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
		txtFechaCita.setText("04/03/2015");
		assertEquals((horaFinal-horaInicio)*4, jcmbHorasCitas.getItemCount());
		// Comprobamos que aparece seleccionada por defecto la primera hora disponible
		assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
		try {
			// En este momento el segundo administrador modifica el beneficiario
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					beneficiarioPrueba.setNombre("Otro nombre");
					controladorAuxiliar.modificarBeneficiario(beneficiarioPrueba);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio del beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar con el nuevo nombre del beneficiario
			assertEquals(txtNombre.getText(), beneficiarioPrueba.getNombre());
			
			// Ahora procedemos a eliminar el beneficiario desde el segundo administrador
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarBeneficiario(beneficiarioPrueba);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminación del beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar borrando los campos, pues no existe ya el beneficiario
			comprobarCamposRestablecidos();
			eliminado = true;
		} catch (Exception e) {
			fail (e.toString());
		}
	}
	
	public void testObservadorSustitucionRegistrada () {
		// TODO: Txedo, haz la prueba para este observador
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
