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

import com.toedter.calendar.JDateChooser;
import comunicaciones.ConfiguracionCliente;

import presentacion.JPCitaVolanteTramitar;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Volante;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.UsuarioYaExistenteException;


public class PruebasJPCitaVolanteTramitar extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {
	
	private ControladorCliente controlador;
	private ControladorCliente controladorCabecera;
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
	
	protected void setUp() {
		boolean valido = true;
		String login;
		medicosEliminados = new Vector<Medico>();
		
		try {
			// Establecemos conexi�n con el servidor front-end
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
			
			// Eliminamos todos los especialistas de la base de datos
			medicosEliminados = controlador.obtenerMedicosTipo(new Especialista());
			// Creamos e insertamos un m�dico de cabecera, un especialista y un beneficiario			
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
			
			// Consultamos el m�dico de nuevo, porque el centro de salud que realmente se le asigna
			// se hace de manera aleatoria
			cabecera = controlador.consultarMedico(cabecera.getNif());

			// Creamos el beneficiario en el mismo centro que el m�dico, para que no se le asigne otro diferente 
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
			
			// Creamos el m�dico especialista que ser� el receptor de los volantes
			tEspecialista = new Especialista("Neurologia");
			login = UtilidadesPruebas.generarLogin();
			especialista = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Francisco", "Garrido", "", "", "", tEspecialista);
			especialista.getCalendario().add(periodo1);
			
			// Mientras existan los usuarios, se genera otro login y otro NIF
			do {
				try {
					controlador.crearUsuario(especialista);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					especialista.setNif(UtilidadesPruebas.generarNIF());
					login = UtilidadesPruebas.generarLogin();
					especialista.setLogin(login);
					especialista.setPassword(login);
					valido = false;
				}
			}while(!valido);
			
			// Consultamos el m�dico de nuevo, porque el centro de salud que realmente se le asigna
			// se hace de manera aleatoria
			especialista = controlador.consultarMedico(especialista.getNif());

			// Creamos el panel
			panel = new JPCitaVolanteTramitar(controlador.getVentanaPrincipal(), controlador);
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
		// Para borrar los datos, hay que entrar con la sesi�n del administrador
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
			// Restauramos los m�dicos que se hayan podido eliminar
			for (Medico m : medicosEliminados) {
				controlador.crearMedico(m);
			}
			// Eliminamos el medico de prueba y el beneficiario de prueba
			controlador.eliminarMedico(cabecera);
			controlador.eliminarBeneficiario(beneficiarioPrueba);
			// Cerramos la sesi�n y la ventana del controlador			
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
		} catch(Exception e) {
			fail(e.toString());
		}	
		winPrincipal.dispose();
	}
	
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
			// identificacion se selecciona por tener un formato inv�lido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// Probamos con un NIF que no est� dado de alta en el sistema y NO lo damos de alta en el sistema
			txtIdentificacion.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, NO_OPTION), "El beneficiario con NIF 00000000A no se encuentra dado de alta en el sistema.\n�Quiere registrarlo para poder tramitar su cita?");
			// Buscamos un beneficiario por su NSS
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			// Ponemos un NSS incorrecto y comprobamos que el campo del
			// identificacion se selecciona por tener un formato inv�lido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NSSIncorrectoException().getMessage());
			// Probamos con un NSS que no est� dado de alta en el sistema y NO lo damos de alta en el sistema
			txtIdentificacion.setText("000000000000");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, NO_OPTION), "El beneficiario con NSS 000000000000 no se encuentra dado de alta en el sistema.\n�Quiere registrarlo para poder tramitar su cita?");
		} catch(Exception e) {
			fail(e.toString());
		}
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
		String esperado = "El beneficiario con NIF " + beneficiarioPrueba.getNif() + " no puede pedir cita\nporque no tiene ning�n m�dico asignado.";
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
		String esperado = "El beneficiario con NIF " + beneficiarioPrueba.getNif() + " no puede pedir cita\nporque no tiene ning�n m�dico asignado.";
		assertEquals(UtilidadesPruebas.obtenerTextoSegundoDialogo(btnBuscar, OK_OPTION, OK_OPTION), esperado);
		// Todos los componentes de tramitar el volante y pedir cita deben estar deshabilitados
		comprobarControlesDeshabilitados();
	}
	
	public void testTramitarVolanteCita () {
		long idVolante = -1;
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
		idVolante = emitirVolante(beneficiarioPrueba, cabecera, especialista);
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
			// Ponemos una fecha en la que no trabaja el m�dico
			// Como el m�dico trabaja los mi�rcoles, seleccionamos un martes
			//txtFechaCita.setText("17/10/2010");
			esperado = "El d�a seleccionado no es laboral para el m�dico";
			assertEquals(esperado, (String)jcmbHorasCitas.getSelectedItem());
			assertEquals(1, jcmbHorasCitas.getItemCount());
			// Ponemos una fecha en la que s� trabaja el m�dico
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
	
	public void testTramitarVolanteCitaOtroBeneficiario () {
		long idVolante = -1;
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
		idVolante = emitirVolante(beneficiario, cabecera, especialista);
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
	
	private long emitirVolante(final Beneficiario bene, final Medico emisor, final Medico receptor) {
		long idVolante = -1;
		controladorCabecera = new ControladorCliente();
		Window winPrincipal2 = WindowInterceptor.run(new Trigger() {
			public void run() {
				try {
					controladorCabecera.iniciarSesion(new ConfiguracionCliente(IPServidorFrontend, puertoServidorFrontend), cabecera.getLogin(), cabecera.getLogin());
				} catch(Exception e) {
					fail(e.toString());
				}
			}
		});
		try {
			idVolante = controladorCabecera.emitirVolante(bene, emisor, receptor);
			controladorCabecera.cerrarSesion();
			controladorCabecera.cerrarControlador();
		} catch (Exception e) {
			fail (e.toString());
		}
		winPrincipal2.dispose();
		return idVolante;
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