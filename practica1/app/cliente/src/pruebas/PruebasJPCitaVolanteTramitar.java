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
import presentacion.JPBeneficiarioConsultar;
import presentacion.JPCitaVolanteTramitar;
import presentacion.auxiliar.OperacionesInterfaz;
import com.toedter.calendar.JDateChooser;
import comunicaciones.ConfiguracionCliente;
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
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.FechaCitaIncorrectaException;
import excepciones.FormatoFechaIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;

/**
 * Pruebas del panel de tramitaci�n de citas a partir de volantes.
 */
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
	
	private JComboBox jcmbIdentificacion;
	private JComboBox jcmbHorasCitas;
	
	private Medico cabecera1;
	private TipoMedico tCabecera;
	private Medico especialista1, especialista2;
	private TipoMedico tEspecialista;
	private Beneficiario beneficiarioPrueba;
	
	private final int horaInicio = 10;
	private final int horaFinal = 16;
	private Vector<Medico> medicosEliminados;
	
	private long idVolante, idVolantePrueba;
	private boolean eliminado;
	private Vector<Date> diaSustitucion;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		medicosEliminados = new Vector<Medico>();
		eliminado = false;
		idVolante = -1;
		
		try {
			// Establecemos conexi�n con el servidor front-end
			controlador = new ControladorCliente();
			// Iniciamos sesion como administrador
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IP_SERVIDOR_FRONTEND, PUERTO_SERVIDOR_FRONTEND), USUARIO_ADMIN, PASSWORD_ADMIN);
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			
			// Establecemos la operaci�n activa de la ventana principal
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.TramitarCitaVolante);
			
			// Eliminamos todos los especialistas de la base de datos
			medicosEliminados = controlador.obtenerMedicosTipo(new Especialista());
			
			// Creamos un m�dico de cabecera 
			tCabecera = new Cabecera();
			cabecera1 = new Medico();
			cabecera1.setNombre("Eduardo");
			cabecera1.setApellidos("Ram�rez Garc�a");
			cabecera1.setTipoMedico(tCabecera);
			cabecera1.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			cabecera1 = (Medico)UtilidadesPruebas.crearUsuario(controlador, cabecera1); 
			
			// Creamos el beneficiario en el mismo centro que el m�dico, para que no se le asigne otro diferente 
			beneficiarioPrueba = new Beneficiario ();
			beneficiarioPrueba.setNombre("beneficiario");
			beneficiarioPrueba.setApellidos("de prueba");
			beneficiarioPrueba.setFechaNacimiento(new Date("01/01/1980"));
			beneficiarioPrueba.setDireccion(new Direccion("lagasca", "", "", "", "Madrid", "Madrid", 28000));
			beneficiarioPrueba.setCentroSalud(cabecera1.getCentroSalud());
			beneficiarioPrueba.setMedicoAsignado(cabecera1);
			beneficiarioPrueba = UtilidadesPruebas.crearBeneficiario(controlador, beneficiarioPrueba);
			
			// Creamos el m�dico especialista que ser� el receptor de los volantes
			tEspecialista = new Especialista("Neurologia");
			especialista1 = new Medico();
			especialista1.setNombre("Juan");
			especialista1.setApellidos("Especialista");
			especialista1.setTipoMedico(tEspecialista);
			especialista1.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista1 = (Medico)UtilidadesPruebas.crearUsuario(controlador, especialista1); 

			// Creamos el m�dico especialista que sustituir� al anterior
			especialista2 = new Medico();
			especialista2.setNombre("Sustituto");
			especialista2.setApellidos("Especialista");
			especialista2.setTipoMedico(tEspecialista);
			especialista2.getCalendario().add(new PeriodoTrabajo(17, 23, DiaSemana.Miercoles));
			especialista2 = (Medico)UtilidadesPruebas.crearUsuario(controlador, especialista2); 

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
			jcmbIdentificacion = (JComboBox)cmbIdentificacion.getAwtComponent();
			jcmbHorasCitas = (JComboBox)cmbHorasCitas.getAwtComponent();
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			UtilidadesPruebas.cerrarControladorAuxiliar();
			// Restauramos los m�dicos que se hayan podido eliminar
			for (Medico m : medicosEliminados) {
				controlador.crearMedico(m);
			}
			// Eliminamos el medico de prueba, beneficiario de prueba y el especialista de prueba
			controlador.eliminarMedico(cabecera1);
			if (!eliminado) controlador.eliminarBeneficiario(beneficiarioPrueba);
			controlador.eliminarMedico(especialista1);
			// Cerramos la sesi�n y la ventana del controlador			
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
			// identificacion se selecciona por tener un formato inv�lido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// Probamos con un NIF que no est� dado de alta en el sistema y NO lo damos de alta en el sistema
			txtIdentificacion.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, NO_OPTION), "El beneficiario con NIF 00000000A no se encuentra dado de alta en el sistema.\n�Quiere registrarlo en el sistema de salud?");
			// Buscamos un beneficiario por su NSS
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			// Ponemos un NSS incorrecto y comprobamos que el campo del
			// identificacion se selecciona por tener un formato inv�lido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NSSIncorrectoException().getMessage());
			// Probamos con un NSS que no est� dado de alta en el sistema y NO lo damos de alta en el sistema
			txtIdentificacion.setText("000000000000");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, NO_OPTION), "El beneficiario con NSS 000000000000 no se encuentra dado de alta en el sistema.\n�Quiere registrarlo en el sistema de salud?");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testRegistrarBeneficiarioInexistente () {
		// Buscamos un beneficiario por su NIF
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		// Probamos con un NIF que no est� dado de alta en el sistema
		txtIdentificacion.setText("00000000a");
		// Esteblecemos que se pregunte si se desea registrar el beneficiario
		((JPBeneficiarioConsultar)panelBeneficiario.getAwtComponent()).setPreguntarRegistro(true);
		// Al contestar que si, pasamos al panel de registrar beneficiario, por lo que ya no existir� la tabla para consultar las citas
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, YES_OPTION), "El beneficiario con NIF 00000000A no se encuentra dado de alta en el sistema.\n�Quiere registrarlo en el sistema de salud?");
		// Se ha debido pasar a la ventana de registro de beneficiario
		assertEquals(OperacionesInterfaz.RegistrarBeneficiario, controlador.getVentanaPrincipal().getOperacionSeleccionada());
		
	}
	
	public void testBuscarBeneficiarioPorNIFSinMedicoAsignado () {
		// Borramos todos los m�dicos del tipo de m�dico asignado al beneficiario
		// para evitar que el sistema le asigne otro autom�ticamente
		try {
			medicosEliminados = controlador.obtenerMedicosTipo(tCabecera);
			for (Medico m : medicosEliminados) {
				controlador.eliminarMedico(m);
			}
		} catch(Exception e) {
			fail(e.toString());
		}
		// Le desasignamos el m�dico al beneficiario que tenemos en memoria
		// el servidor automaticamente ha actualizado su estado en la base de datos
		beneficiarioPrueba.setMedicoAsignado(null);
		// Probamos con el NIF de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		String esperado = "El beneficiario seleccionado no puede pedir cita\nporque no tiene ning�n m�dico asignado.";
		assertEquals(UtilidadesPruebas.obtenerTextoSegundoDialogo(btnBuscar, OK_OPTION, OK_OPTION), esperado);
		// Todos los componentes de tramitar el volante y pedir cita deben estar deshabilitados
		comprobarControlesDeshabilitados();
	}

	public void testBuscarBeneficiarioPorNSSSinMedicoAsignado () {
		// Borramos todos los m�dicos del tipo de m�dico asignado al beneficiario
		// para evitar que el sistema le asigne otro autom�ticamente
		try {
			medicosEliminados = controlador.obtenerMedicosTipo(tCabecera);
			for (Medico m : medicosEliminados) {
				controlador.eliminarMedico(m);
			}
		} catch(Exception e) {
			fail(e.toString());
		}
		// Le desasignamos el m�dico al beneficiario que tenemos en memoria
		// el servidor automaticamente ha actualizado su estado en la base de datos
		beneficiarioPrueba.setMedicoAsignado(null);
		// Probamos con el NSS de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(1);
		txtIdentificacion.setText(beneficiarioPrueba.getNss());
		String esperado = "El beneficiario seleccionado no puede pedir cita\nporque no tiene ning�n m�dico asignado.";
		assertEquals(UtilidadesPruebas.obtenerTextoSegundoDialogo(btnBuscar, OK_OPTION, OK_OPTION), esperado);
		// Todos los componentes de tramitar el volante y pedir cita deben estar deshabilitados
		comprobarControlesDeshabilitados();
	}
	
	public void testTramitarVolanteCita () {
		String esperado = "";
		// Inicialmente deben estar deshabilitados los componentes para tramitar el volante y la cita
		comprobarCamposRestablecidos();
		comprobarControlesDeshabilitados();
		// Probamos con el NIF de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
		// y que adem�s tiene un m�dico asignado
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
		// Comprobamos que los de tramitar la cita a�n est�n deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Buscamos un volante que no existe
		txtNumeroVolante.setText("1000");
		esperado = "No existe ning�n volante con el id 1000.";
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista1.getCentroSalud().getNombre() + "; " + especialista1.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());
			// Ponemos una fecha en la que no trabaja el m�dico
			// Como el m�dico trabaja los mi�rcoles, seleccionamos un martes
			// Ponemos una fecha en la que no trabaja el m�dico, para ello eliminamos su periodo de trabajo
			// Como el m�dico trabaja los mi�rcoles, seleccionamos un martes
			txtDiaCita.setText("03/03/2015");
			esperado = "El d�a seleccionado no es laboral para el m�dico";
			assertEquals(esperado, (String)jcmbHorasCitas.getSelectedItem());
			assertEquals(1, jcmbHorasCitas.getItemCount());
			// Ponemos una fecha anterior a la actual (es decir, una fecha caducada)
			txtDiaCita.setText("01/01/2009");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnRegistrar, OK_OPTION), new FechaCitaIncorrectaException().getLocalizedMessage());
			// Ponemos un formato de fecha que no es v�lido
			txtDiaCita.setText("asdf");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnRegistrar, OK_OPTION), new FormatoFechaIncorrectoException().getLocalizedMessage());
			// Ponemos una fecha en la que s� trabaja el m�dico
			txtDiaCita.setText("20/10/2010");
			assertEquals((horaFinal-horaInicio)*(60/IConstantes.DURACION_CITA), jcmbHorasCitas.getItemCount());
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
			// Comprobamos que los de tramitar la cita a�n est�n deshabilitados, porque primero hay que buscar el volante
			assertFalse(txtDiaCita.isEnabled());
			assertFalse(cmbHorasCitas.isEnabled());
			assertFalse(txtMedico.isEditable());
			assertFalse(btnRegistrar.isEnabled());
			// Ponemos el n�mero del volante que tramitamos anteriormente y esperamos el error
			txtNumeroVolante.setText(idVolante + "");
			esperado = "El volante seleccionado ya se ha utilizado para pedir una cita y no se puede usar de nuevo.";
			// Comprobamos que el estado de la ventana es el correcto
			assertTrue(txtMedicoAsignado.getText().equals(""));
			assertFalse(txtDiaCita.isEnabled());
			assertFalse(jcmbHorasCitas.isEnabled());
			assertFalse(btnRegistrar.isEnabled());
		}
	}
	
	@SuppressWarnings("deprecation")
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
		beneficiario.setCentroSalud(cabecera1.getCentroSalud());
		beneficiario.setMedicoAsignado(cabecera1);
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
			idVolante = UtilidadesPruebas.emitirVolante(beneficiario, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			// Buscamos el primer beneficiario y tratamos de tramitar el volante del segundo
			// Se espera un mensaje de error
			// Inicialmente deben estar deshabilitados los componentes para tramitar el volante y la cita
			comprobarCamposRestablecidos();
			comprobarControlesDeshabilitados();
			// Probamos con el NIF de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
			// y que adem�s tiene un m�dico asignado
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
			// Comprobamos que los de tramitar la cita a�n est�n deshabilitados, porque primero hay que buscar el volante
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
	
	public void testObservadorCitasRegistradas () {
		String esperado = "";
		
		// Iniciamos sesi�n con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
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
		// Comprobamos que los de tramitar la cita a�n est�n deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista1.getCentroSalud().getNombre() + "; " + especialista1.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());	
			// Se selecciona un d�a en el que trabajar� el m�dico
			txtDiaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*(60/IConstantes.DURACION_CITA), jcmbHorasCitas.getItemCount());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			try {
				// En este momento el segundo administrador pide una cita para el especialista receptor, en la hora seleccionada por el primero
				// Para ello, se necesita un segundo volante con el mismo especialista. Se crea uno de prueba
				idVolantePrueba = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
				WindowInterceptor.init(new Trigger() {
					@SuppressWarnings("deprecation")
					public void run() throws Exception {
						controladorAuxiliar.pedirCita(beneficiarioPrueba, idVolantePrueba, new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita para el especialista receptor del volante
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(TIME_OUT);
				// La ventana del primer administrador se ha debido actualizar, mostrando que la fecha ya no es v�lida
				txtDiaCita.setText("04/03/2015");
				assertTrue(jcmbHorasCitas.getSelectedIndex()==0);
				assertEquals(txtMedico.getText(), "(fecha no v�lida)");
				
				// Ahora pedimos una cita para el primer volante en otra fecha
				Thread.sleep(TIME_OUT);
				WindowInterceptor.init(new Trigger() {
					@SuppressWarnings("deprecation")
					public void run() throws Exception {
						controladorAuxiliar.pedirCita(beneficiarioPrueba, idVolante, new Date(2012-1900,3-1,7,horaInicio,0), IConstantes.DURACION_CITA);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita registrada para el volante
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(TIME_OUT);
				// La ventana del primer administrador se ha debido actualizar, borrando los campos, porque el volante ya no se puede utilizar
				comprobarCamposRestablecidos();
				
				// La cita y los volantes de prueba se borrar�n en el tearDown, porque se borra su m�dico
			} catch (Exception e) {
				fail (e.toString());
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void testObservadorCitasAnuladas () {
	
		String esperado = "";
		
		// Iniciamos sesi�n con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
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
		// Comprobamos que los de tramitar la cita a�n est�n deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista1.getCentroSalud().getNombre() + "; " + especialista1.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());	
			// Se selecciona un d�a en el que trabajar� el m�dico
			txtDiaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*(60/IConstantes.DURACION_CITA), jcmbHorasCitas.getItemCount());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			try {
				// En este momento el segundo administrador pide una cita para el especialista receptor en la hora seleccionada por el primero
				// Para ello, se necesita un segundo volante con el mismo especialista. Se crea uno de prueba
				idVolantePrueba = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
				WindowInterceptor.init(new Trigger() {
					@SuppressWarnings("deprecation")
					public void run() throws Exception {
						controladorAuxiliar.pedirCita(beneficiarioPrueba, idVolantePrueba, new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita registrada para el especialista receptor
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(TIME_OUT);
				// La ventana del primer administrador se ha debido actualizar, mostrando que la fecha ya no es v�lida
				txtDiaCita.setText("04/03/2015");
				assertTrue(jcmbHorasCitas.getSelectedIndex()==0);
				assertEquals(txtMedico.getText(), "(fecha no v�lida)");
				
				// Ahora procedemos a eliminar esta cita (no asignada al volante) desde el segundo administrador
				WindowInterceptor.init(new Trigger() {
					@SuppressWarnings("deprecation")
					public void run() throws Exception {
						controladorAuxiliar.anularCita(new Cita(new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA, beneficiarioPrueba, controlador.consultarVolante(idVolante).getReceptor()));
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa que la cita del especialista ha sido eliminada
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();	
				
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(TIME_OUT);
				// La ventana del primer administrador se ha debido actualizar, mostrando que la fecha ya es v�lida 
				txtDiaCita.setText("04/03/2015");
				assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
				assertEquals(txtMedico.getText(), especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")");
				
				// Se emite una cita de prueba para el volante (desde el primer administrador)
				controlador.pedirCita(beneficiarioPrueba, idVolante, new Date(2015-1900,3-1,4,horaInicio,0), IConstantes.DURACION_CITA);
				// Consultamos de nuevo el volante, comporbando que se muestra el di�logo de error
				txtNumeroVolante.setText(String.valueOf(idVolante));
				esperado = "El volante seleccionado ya se ha utilizado para pedir una cita y no se puede usar de nuevo.";
				assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
				assertFalse(txtDiaCita.isEnabled());
				assertFalse(cmbHorasCitas.isEnabled());
				
				// Ahora procedemos a eliminar esta cita que se di� con el volante desde el segundo administrador
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						controladorAuxiliar.anularCita(controladorAuxiliar.consultarVolante(idVolante).getCita());
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la anulaci�n de la cita asociada al volante
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
			
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(TIME_OUT);
				// La ventana del primer administrador se ha debido actualizar, habilitando los componentes para poder pedir cita, pues el volante ya no tiene cita asignada
				assertTrue(txtDiaCita.isEnabled());
				assertTrue(cmbHorasCitas.isEnabled());	
			} catch (Exception e) {
				fail (e.toString());
			}
		}
	}
		
	public void testObservadorUsuarioActualizadoEliminado () {
		String esperado = "";
		final PeriodoTrabajo periodo2 = new PeriodoTrabajo(horaInicio, horaFinal-2, DiaSemana.Miercoles);
		
		// Iniciamos sesi�n con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
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
		// Comprobamos que los de tramitar la cita a�n est�n deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista1.getCentroSalud().getNombre() + "; " + especialista1.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());
			// Se selecciona un d�a en el que trabajar� el m�dico
			txtDiaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*(60/IConstantes.DURACION_CITA), jcmbHorasCitas.getItemCount());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			try {
				// En este momento el segundo administrador modifica el calendario del m�dico
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						Thread.sleep(100);
						// Reemplazamos el periodo que ya tenia el medico
						especialista1.getCalendario().clear();
						especialista1.getCalendario().add(periodo2);
						controladorAuxiliar.modificarMedico(especialista1);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(TIME_OUT);
				// La ventana del primer administrador se ha debido actualizar teniendo menos horas en el combobox
				txtDiaCita.setText("04/03/2015");
				assertEquals((horaFinal-2-horaInicio)*(60/IConstantes.DURACION_CITA), jcmbHorasCitas.getItemCount());
				
				// Ahora procedemos a eliminar el medico desde el segundo administrador
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						controladorAuxiliar.eliminarMedico(especialista1);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la nueva cita
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(TIME_OUT);
				// La ventana del primer administrador se ha debido actualizar borrando los campos, pues se ha eliminado el especialista
				comprobarCamposRestablecidos();
				medicosEliminados.add(especialista1);
			} catch (Exception e) {
				fail (e.toString());
			}
		}
	}
	
	public void testObservadorBeneficiarioActualizadoEliminado () {
		String esperado = "";
		// Iniciamos sesi�n con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
		} catch(Exception e) {
			fail(e.toString());
		}
		// El primer administrador busca al beneficiario de prueba
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
		// Comprobamos que los componentes de tramitar el volante se han habilitado
		assertTrue(txtNumeroVolante.isEnabled());
		assertTrue(txtNumeroVolante.getText().equals(""));
		assertTrue(btnBuscarVolante.isEnabled());
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(txtCentro.getText().equals(""));
		// Comprobamos que los de tramitar la cita a�n est�n deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			esperado = "Volante encontrado.";
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), esperado);
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista1.getCentroSalud().getNombre() + "; " + especialista1.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());
			// Se selecciona un d�a en el que trabajar� el m�dico
			txtDiaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*(60/IConstantes.DURACION_CITA), jcmbHorasCitas.getItemCount());
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
						// Capturamos la ventana que avisa de la nueva cita
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
	}
	
	@SuppressWarnings("deprecation")
	public void testObservadorSustitucionRegistrada () {
		String cadena;
		
		diaSustitucion = new Vector<Date>();
		diaSustitucion.add(new Date(2015 - 1900, 2, 4)); // Mi�rcoles 4/Marzo/2015
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// El primer administrador busca al beneficiario de prueba
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
		// Comprobamos que los componentes de tramitar el volante se han habilitado
		assertTrue(txtNumeroVolante.isEnabled());
		assertTrue(txtNumeroVolante.getText().equals(""));
		assertTrue(btnBuscarVolante.isEnabled());
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(txtCentro.getText().equals(""));
		// Comprobamos que los de tramitar la cita a�n est�n deshabilitados, porque primero hay que buscar el volante
		assertFalse(txtDiaCita.isEnabled());
		assertFalse(cmbHorasCitas.isEnabled());
		assertFalse(txtMedico.isEditable());
		assertFalse(btnRegistrar.isEnabled());
		// Ahora emitimos un volante y lo buscamos para tramitarlo
		try {
			idVolante = UtilidadesPruebas.emitirVolante(beneficiarioPrueba, cabecera1, especialista1, cabecera1.getLogin(), cabecera1.getLogin());
		} catch (Exception e) {
			fail(e.toString());
		}
		if (idVolante != -1) {
			txtNumeroVolante.setText(String.valueOf(idVolante));
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscarVolante, OK_OPTION), "Volante encontrado.");
			// Comprobamos que los campos se actualizan correctamente
			assertEquals(especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")", txtMedicoAsignado.getText());
			assertEquals(especialista1.getCentroSalud().getNombre() + "; " + especialista1.getCentroSalud().getDireccion(), txtCentro.getText());
			// Comprobamos que los campos de pedir cita se han habilitado
			assertTrue(txtDiaCita.isEnabled());
			assertTrue(cmbHorasCitas.isEnabled());
			// Se selecciona un d�a en el que trabajar� el m�dico
			txtDiaCita.setText("04/03/2015");
			assertEquals((horaFinal-horaInicio)*(60/IConstantes.DURACION_CITA), jcmbHorasCitas.getItemCount());
			// Comprobamos que el m�dico que dar� la cita ser�,
			// en principio, el asignado al volante
			cadena = especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")";
			assertEquals(cadena, txtMedico.getText());
			// Comprobamos que aparece seleccionada por defecto la primera hora disponible (las 10:00)
			assertTrue(jcmbHorasCitas.getSelectedIndex() == 0);
			try {
				// En este momento el segundo administrador pide una sustituci�n para el m�dico asignado al beneficiario
				WindowInterceptor.init(new Trigger() {
					public void run() throws Exception {
						controladorAuxiliar.asignarSustituto(especialista1, diaSustitucion, 10, 13, especialista2);
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						// Capturamos la ventana que avisa de la sustitucion registrada
						return window.getButton(OK_OPTION).triggerClick();
					}
				}).run();
				// Dormimos el hilo en espera de la respuesta del servidor
				Thread.sleep(TIME_OUT);
				// La ventana del primer administrador se ha debido actualizar con el nombre del m�dico que realmente va a dar la cita (el sustituto)
				cadena = especialista2.getApellidos() + ", " + especialista2.getNombre() + " (" + especialista2.getNif() + "), sustituye a " + especialista1.getApellidos() + ", " + especialista1.getNombre() + " (" + especialista1.getNif() + ")";
				assertEquals(cadena, txtMedico.getText());
			} catch (Exception e) {
				fail (e.toString());
			}
		}
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
