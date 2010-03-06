package pruebas;

import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
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

import presentacion.JPCitaVolanteTramitar;
import presentacion.auxiliar.OperacionesInterfaz;

import com.toedter.calendar.JDateChooser;
import comunicaciones.ConfiguracionCliente;
import comunicaciones.RemotoCliente;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Volante;
import dominio.control.Cliente;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.FechaCitaIncorrectaException;
import excepciones.FormatoFechaIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;

public class PruebasJPCitaVolanteTramitar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {
	
	private ControladorCliente controlador;
	private ControladorCliente controladorAuxiliar;
	private JPCitaVolanteTramitar panel;
	private Panel panelBeneficiario;
	private Panel pnlPanel;
	private Window winPrincipal;
	
	// Componentes de UISpec4J
	// Primera parte
	private Button btnBuscar;
	private ComboBox cmbIdentificacion;
	private TextBox txtIdentificacion;
	private TextBox txtNIF;
	private TextBox txtNSS;
	private TextBox txtNombre;
	private TextBox txtApellidos;
	// Segunda parte
	private TextBox txtNumeroVolante;
	private Button btnBuscarVolante;
	private TextBox txtMedicoAsignado;
	private TextBox txtCentro;
	// Tercera parte
	private TextBox txtDiaCita;
	private ComboBox cmbHorasCitas;
	private TextBox txtMedico;
	private Button btnRegistrar;
	
	// Componentes Swing
	// Primera parte
	private JButton jbtnBuscar;
	private JComboBox jcmbIdentificacion;
	private JTextField jtxtIdentificacion;
	private JTextField jtxtNIF;
	private JTextField jtxtNSS;
	private JTextField jtxtNombre;
	private JTextField jtxtApellidos;
	// Segunda parte
	private JTextField jtxtNumeroVolante;
	private JButton jbtnBuscarVolante;
	private JTextField jtxtMedicoAsignado;
	private JTextField jtxtCentro;
	// Tercera parte
	private JTextField jtxtDiaCita;
	private JComboBox jcmbHorasCitas;
	private JTextField jtxtMedico;
	private JButton jbtnRegistrar;
	
	private Medico cabecera;
	private TipoMedico tCabecera;
	private Medico especialista;
	private TipoMedico tEspecialista;
	private Beneficiario beneficiarioPrueba;
	private PeriodoTrabajo periodo1;
	private Medico medicoAsignado;
	private Volante volante;
	
	private final int horaInicio = 10;
	private final int horaFinal = 16;
	private Vector<Medico> medicosEliminados;
	
	private long idVolante;
	private boolean eliminado;
	
	protected void setUp() {
		medicosEliminados = new Vector<Medico>();
		eliminado = false;
		idVolante = -1;
		
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
			
			// Establecemos la operación activa de la ventana principal
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.TramitarCitaVolante);
			
			// Eliminamos todos los especialistas de la base de datos
			medicosEliminados = controlador.obtenerMedicosTipo(new Especialista());
			
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
			
			// Creamos el médico especialista que será el receptor de los volantes
			tEspecialista = new Especialista("Neurologia");
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(tEspecialista);
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controlador, especialista); 

			// Obtenemos el panel
			Panel p1 = winPrincipal.getPanel("jPanelGestionarCitas");
			panel = (JPCitaVolanteTramitar)p1.getPanel("jPanelVolanteTramitar").getAwtContainer();
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panel);
			panelBeneficiario = pnlPanel.getPanel("pnlBeneficiario");
			// Componentes de UISpec4J
			// Primera parte
			btnBuscar = panelBeneficiario.getButton("btnBuscar");
			cmbIdentificacion = panelBeneficiario.getComboBox("cmbIdentificacion");
			txtIdentificacion = panelBeneficiario.getTextBox("txtIdentificacion");
			txtNIF = panelBeneficiario.getTextBox("txtNIF");
			txtNSS = panelBeneficiario.getTextBox("txtNSS");
			txtNombre = panelBeneficiario.getTextBox("txtNombre");
			txtApellidos = panelBeneficiario.getTextBox("txtApellidos");
			// Segunda parte
			txtNumeroVolante = pnlPanel.getTextBox("txtNumeroVolante");
			btnBuscarVolante = pnlPanel.getButton("btnBuscarVolante");
			txtCentro = pnlPanel.getTextBox("txtCentro");
			txtMedicoAsignado = pnlPanel.getTextBox("txtMedicoAsignado2");
			// Tercera parte
			txtDiaCita = new TextBox((JTextField)((JDateChooser)pnlPanel.getSwingComponents(JDateChooser.class)[0]).getDateEditor());
			cmbHorasCitas = pnlPanel.getComboBox("cmbHorasCitas");
			txtMedico = pnlPanel.getTextBox("txtMedico");
			btnRegistrar = pnlPanel.getButton("btnRegistrar");

			// Componentes Swing
			// Primera parte
			jcmbIdentificacion = (JComboBox)cmbIdentificacion.getAwtComponent();
			jtxtIdentificacion = (JTextField)txtIdentificacion.getAwtComponent();
			jtxtNIF = (JTextField)txtNIF.getAwtComponent();
			jtxtNSS = (JTextField)txtNSS.getAwtComponent();
			jtxtNombre = (JTextField)txtNombre.getAwtComponent();
			jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
			// Segunda parte

			// Tercera parte
			jcmbHorasCitas = (JComboBox)cmbHorasCitas.getAwtComponent();
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
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
		winPrincipal.dispose();
	}
	
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
		// Todos los componentes de tramitar el volante y pedir cita deben estar deshabilitados
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
		// Todos los componentes de tramitar el volante y pedir cita deben estar deshabilitados
		comprobarControlesDeshabilitados();
	}
	
	public void testTramitarVolanteCita () {
		String esperado = "";
		// Inicialmente deben estar deshabilitados los componentes para tramitar el volante y la cita
		comprobarCamposRestablecidos();
		comprobarControlesDeshabilitados();
		// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
		// y que además tiene un médico asignado
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), "Beneficiario encontrado.");
		// Comprobamos que los componentes de tramitar el volante se han habilitado
		assertTrue(txtNumeroVolante.isEnabled());
		assertTrue(txtNumeroVolante.getText().equals(""));
		assertTrue(btnBuscarVolante.isEnabled());
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(txtCentro.getText().equals(""));
		// Comprobamos que los de tramitar la cita aún están deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Buscamos un volante que no existe
		txtNumeroVolante.setText("1000");
		esperado = "No existe ningún volante con el id 1000.";
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera, especialista, cabecera.getLogin(), cabecera.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista.getCentroSalud().getNombre() + "; " + especialista.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());
			// Ponemos una fecha en la que no trabaja el médico
			// Como el médico trabaja los miércoles, seleccionamos un martes
			// Ponemos una fecha en la que no trabaja el médico, para ello eliminamos su periodo de trabajo
			// Como el médico trabaja los miércoles, seleccionamos un martes
			txtDiaCita.setText("03/03/2015");
			esperado = "El día seleccionado no es laboral para el médico";
			assertEquals(esperado, (String)jcmbHorasCitas.getSelectedItem());
			assertEquals(1, jcmbHorasCitas.getItemCount());
			// Ponemos una fecha anterior a la actual (es decir, una fecha caducada)
			txtDiaCita.setText("01/01/2009");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnRegistrar, OK_OPTION), new FechaCitaIncorrectaException().getLocalizedMessage());
			// Ponemos un formato de fecha que no es válido
			txtDiaCita.setText("asdf");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnRegistrar, OK_OPTION), new FormatoFechaIncorrectoException().getLocalizedMessage());
			// Ponemos una fecha en la que sí trabaja el médico
			txtDiaCita.setText("20/10/2010");
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
			// Comprobamos que los componentes de tramitar el volante se han habilitado de nuevo
			assertTrue(txtNumeroVolante.isEnabled());
			assertTrue(txtNumeroVolante.getText().equals(""));
			assertTrue(btnBuscarVolante.isEnabled());
			assertTrue(txtMedicoAsignado.getText().equals(""));
			assertTrue(txtCentro.getText().equals(""));
			// Comprobamos que los de tramitar la cita aún están deshabilitados, porque primero hay que buscar el volante
			assertFalse(txtDiaCita.isEnabled());
			assertFalse(cmbHorasCitas.isEnabled());
			assertFalse(txtMedico.isEditable());
			assertFalse(btnRegistrar.isEnabled());
			// Ponemos el número del volante que tramitamos anteriormente y esperamos el error
			txtNumeroVolante.setText(idVolante + "");
			esperado = "El volante seleccionado ya se ha utilizado para pedir una cita y no se puede usar de nuevo.";
			// Comprobamos que el estado de la ventana es el correcto
			assertTrue(txtMedicoAsignado.getText().equals(""));
			assertFalse(txtDiaCita.isEnabled());
			assertFalse(jcmbHorasCitas.isEnabled());
			assertFalse(btnRegistrar.isEnabled());
		}
	}
	
	public void testTramitarVolanteCitaOtroBeneficiario () {
		String esperado = "";
		boolean valido = false;
		// Creamos un segundo beneficiario
		Beneficiario beneficiario = new Beneficiario ();
		beneficiario.setNif(UtilidadesPruebas.generarNIF());
		beneficiario.setNss(UtilidadesPruebas.generarNSS());
		beneficiario.setNombre("beneficiario auxiliar");
		beneficiario.setApellidos("de prueba");
		beneficiario.setFechaNacimiento(new Date("01/01/1985"));
		beneficiario.setDireccion(new Direccion("velazquez", "", "", "", "Madrid", "Madrid", 28000));
		beneficiario.setCentroSalud(cabecera.getCentroSalud());
		beneficiario.setMedicoAsignado(cabecera);
		do {
			try {					
				controlador.crearBeneficiario(beneficiario);
				valido = true;
			} catch (BeneficiarioYaExistenteException e) {
				beneficiario.setNif(UtilidadesPruebas.generarNIF());					
				beneficiario.setNss(UtilidadesPruebas.generarNSS());
				valido = false;
			} catch (Exception e) {
				fail (e.toString());
			}
		}while(!valido);
		// Emitimos un segundo volante para este beneficiario
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiario, cabecera, especialista, cabecera.getLogin(), cabecera.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			// Buscamos el primer beneficiario y tratamos de tramitar el volante del segundo
			// Se espera un mensaje de error
			// Inicialmente deben estar deshabilitados los componentes para tramitar el volante y la cita
			comprobarCamposRestablecidos();
			comprobarControlesDeshabilitados();
			// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
			// y que además tiene un médico asignado
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), "Beneficiario encontrado.");
			// Comprobamos que los componentes de tramitar el volante se han habilitado
			assertTrue(txtNumeroVolante.isEnabled());
			assertTrue(txtNumeroVolante.getText().equals(""));
			assertTrue(btnBuscarVolante.isEnabled());
			assertTrue(txtMedicoAsignado.getText().equals(""));
			assertTrue(txtCentro.getText().equals(""));
			// Comprobamos que los de tramitar la cita aún están deshabilitados, porque primero hay que buscar el volante
			assertFalse(txtDiaCita.isEnabled());
			assertFalse(cmbHorasCitas.isEnabled());
			assertFalse(txtMedico.isEditable());
			assertFalse(btnRegistrar.isEnabled());
			// Buscamos el volante que pertenece al beneficiario auxiliar
			txtNumeroVolante.setText(idVolante + "");
			esperado = "El volante seleccionado no fue emitido para el beneficiario con NIF " + beneficiarioPrueba.getNif() + ".";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
		}
	}
	
	public void testObservadorCitaRegistradaAnulada () {
		String esperado = "";
		
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
		// Comprobamos que los componentes de tramitar el volante se han habilitado
		assertTrue(txtNumeroVolante.isEnabled());
		assertTrue(txtNumeroVolante.getText().equals(""));
		assertTrue(btnBuscarVolante.isEnabled());
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(txtCentro.getText().equals(""));
		// Comprobamos que los de tramitar la cita aún están deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera, especialista, cabecera.getLogin(), cabecera.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista.getCentroSalud().getNombre() + "; " + especialista.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());	
			// Se selecciona un día en el que trabajará el médico
			txtDiaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*4, jcmbHorasCitas.getItemCount());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			try {
				// En este momento el segundo administrador pide una cita en la hora seleccionada por el primero
				// TODO: La cita hay que pedirla para el médico especialista receptor
				// del volante, no para el médico asignado al beneficiario, porque su
				// horario de citas no se utiliza para tramitar una cita con un volante
/*				WindowInterceptor.init(new Trigger() {
					@SuppressWarnings("deprecation")
					public void run() throws Exception {
						controladorAuxiliar.pedirCita(beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado().getNif(), new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(500);
				// La ventana del primer administrador se ha debido actualizar seleccionando la siguiente hora disponible en el día que se ha pedido cita desde el administrador auxiliar
				txtDiaCita.setText("04/03/2015");
				assertTrue(jcmbHorasCitas.getSelectedIndex() == 1);*/
				
				// Ahora pedimos cita para este mismo volante
				Thread.sleep(500);
				WindowInterceptor.init(new Trigger() {
					@SuppressWarnings("deprecation")
					public void run() throws Exception {
						controladorAuxiliar.pedirCita(beneficiarioPrueba, idVolante, new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(500);
				// La ventana del primer administrador se ha debido actualizar, borrando los campos porque el volante ya no se puede utilizar
				comprobarCamposRestablecidos();
				
				// TODO: Cuando se pide la cita anterior, creo que la ventana
				// se restablece, por lo que se pierde el volante seleccionado
				// y por eso el siguiente código no lanza ninguna ventana
				
				// Ahora procedemos a eliminar la cita sin volante desde el segundo administrador
				WindowInterceptor.init(new Trigger() {
					@SuppressWarnings("deprecation")
					public void run() throws Exception {
						controladorAuxiliar.anularCita(new Cita(new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA, beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado()));
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();	
				
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(500);
				// La ventana del primer administrador se ha debido actualizar seleccionando la primera hora disponible en el día que se ha anulado cita desde el administrador auxiliar
				txtDiaCita.setText("04/03/2015");
				assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
				
				// Ahora procedemos a eliminar la cita que se dió con el volante desde el segundo administrador
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						controladorAuxiliar.anularCita(controladorAuxiliar.consultarVolante(idVolante).getCita());
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(500);
				// La ventana del primer administrador se ha debido actualizar seleccionando la primera hora disponible en el día que se ha anulado cita desde el administrador auxiliar
				txtDiaCita.setText("04/03/2015");
				assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);							
			} catch (Exception e) {
				fail (e.toString());
			}
		}
	}
	
	public void testObservadorUsuarioActualizadoEliminado () {
		String esperado = "";
		PeriodoTrabajo periodo2 = new PeriodoTrabajo(horaInicio, horaFinal-2, DiaSemana.Miercoles);
		// Reemplazamos el periodo que ya tenia el medico
		especialista.getCalendario().set(0, periodo2);
		
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
		// Comprobamos que los componentes de tramitar el volante se han habilitado
		assertTrue(txtNumeroVolante.isEnabled());
		assertTrue(txtNumeroVolante.getText().equals(""));
		assertTrue(btnBuscarVolante.isEnabled());
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(txtCentro.getText().equals(""));
		// Comprobamos que los de tramitar la cita aún están deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera, especialista, cabecera.getLogin(), cabecera.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista.getCentroSalud().getNombre() + "; " + especialista.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());
			// Se selecciona un día en el que trabajará el médico
			txtDiaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*4, jcmbHorasCitas.getItemCount());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			try {
				// En este momento el segundo administrador modifica el calendario del médico
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						Thread.sleep(100);
						controladorAuxiliar.modificarMedico(especialista);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(500);
				// La ventana del primer administrador se ha debido actualizar teniendo menos horas en el combobox
				txtDiaCita.setText("04/03/2015");
				assertEquals((horaFinal-2-horaInicio)*4, jcmbHorasCitas.getItemCount());
				
				// Ahora procedemos a eliminar el medico desde el segundo administrador
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						controladorAuxiliar.eliminarMedico(especialista);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(500);
				// La ventana del primer administrador se ha debido actualizar borrando los campos, pues se ha eliminado el especialista
				comprobarCamposRestablecidos();
				medicosEliminados.add(especialista);
			} catch (Exception e) {
				fail (e.toString());
			}
		}
	}
	
	public void testObservadorBeneficiarioActualizadoEliminado () {
		String esperado = "";
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
		// Comprobamos que los componentes de tramitar el volante se han habilitado
		assertTrue(txtNumeroVolante.isEnabled());
		assertTrue(txtNumeroVolante.getText().equals(""));
		assertTrue(btnBuscarVolante.isEnabled());
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(txtCentro.getText().equals(""));
		// Comprobamos que los de tramitar la cita aún están deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera, especialista, cabecera.getLogin(), cabecera.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista.getCentroSalud().getNombre() + "; " + especialista.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());
			// Se selecciona un día en el que trabajará el médico
			txtDiaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*4, jcmbHorasCitas.getItemCount());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			try {
				// En este momento el segundo administrador modifica el beneficiario
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						beneficiarioPrueba.setNombre("Otro Nombre");
						controladorAuxiliar.modificarBeneficiario(beneficiarioPrueba);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(500);
				// La ventana del primer administrador se ha debido actualizar con el nuevo nombre del beneficiario
				assertEquals(txtNombre.getText(), beneficiarioPrueba.getNombre());
				
				// Ahora procedemos a eliminar el beneficiario desde el segundo administrador
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						controladorAuxiliar.eliminarBeneficiario(beneficiarioPrueba);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(500);
				// La ventana del primer administrador se ha debido actualizar borrando los campos, pues no existe ya el beneficiario
				comprobarCamposRestablecidos();
				eliminado = true;
			} catch (Exception e) {
				fail (e.toString());
			}
		}
	}
	
	public void testObservadorSustitucionRegistrada () {
		// TODO
	}

	private void comprobarCamposRestablecidos () {
		assertTrue(txtIdentificacion.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNSS.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
		assertTrue(txtApellidos.getText().equals(""));
		assertTrue(txtNumeroVolante.getText().equals(""));
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(txtDiaCita.getText().equals(""));
		assertTrue(jcmbHorasCitas.getItemCount() == 0);
		assertTrue(txtMedico.getText().equals(""));
	}
	
	private void comprobarControlesDeshabilitados() {
		assertFalse(txtNumeroVolante.isEnabled());
		assertFalse(btnBuscarVolante.isEnabled());
		assertFalse(txtMedicoAsignado.isEnabled());
		assertFalse(txtCentro.isEnabled());
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
	}
	
}
