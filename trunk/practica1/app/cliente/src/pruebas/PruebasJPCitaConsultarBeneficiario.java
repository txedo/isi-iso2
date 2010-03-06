package pruebas;

import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.ComboBox;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPCitaConsultarBeneficiario;
import presentacion.auxiliar.OperacionesInterfaz;

import comunicaciones.ConfiguracionCliente;

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
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;

public class PruebasJPCitaConsultarBeneficiario extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {

	private ControladorCliente controlador, controladorAuxiliar;
	private JPCitaConsultarBeneficiario panelCita;
	private Panel panelBeneficiario;
	private Panel pnlPanel;
	private Table tblCitas;
	private Button btnCitasHistorico;
	private Button btnAnular;
	private Button btnBuscar;
	private Button btnRestablecer;
	private ComboBox cmbIdentificacion;
	private TextBox txtIdentificacion;
	private TextBox txtNIF;
	private TextBox txtNSS;
	private TextBox txtNombre;
	private TextBox txtApellidos;	
	private ComboBox cmbCentros;
	private TextBox txtMedicoAsignado;
	private JComboBox jcmbIdentificacion;
	private JTextField jtxtIdentificacion;
	private JTextField jtxtNIF;
	private JTextField jtxtNSS;
	private JTextField jtxtNombre;
	private JTextField jtxtApellidos;
	private JComboBox jcmbCentros;
	private JTextField jtxtMedicoAsignado;
	private JTable jtblCitas;
	private Window winPrincipal;
	
	private Medico cabecera;
	private TipoMedico tCabecera;
	private Beneficiario beneficiarioPrueba;
	
	private boolean beneficiarioEliminado;
	private boolean medicoEliminado;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		beneficiarioEliminado = false;
		medicoEliminado = false;
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
			
			// Indicamos que la operación activa del primer administador es la de consultar beneficiario
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.ConsultarAnularCitasBeneficiario);
			
			// Creamos un médico de cabecera 
			tCabecera = new Cabecera();
			cabecera = new Medico();
			cabecera.setNombre("Eduardo");
			cabecera.setApellidos("Ramírez García");
			cabecera.setTipoMedico(tCabecera);
			cabecera.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			cabecera = (Medico)UtilidadesPruebas.crearUsuario(controlador, cabecera); 

			// Creamos el beneficiario en el mismo centro que el médico, para que no se le asigne otro diferente 
			beneficiarioPrueba = new Beneficiario();
			beneficiarioPrueba.setNombre("beneficiario");
			beneficiarioPrueba.setApellidos("de prueba");
			beneficiarioPrueba.setCorreo(" ");
			beneficiarioPrueba.setTelefono(" ");
			beneficiarioPrueba.setMovil(" ");
			beneficiarioPrueba.setFechaNacimiento(new Date("01/01/1980"));
			beneficiarioPrueba.setDireccion(new Direccion("lagasca", "", "", "", "Madrid", "Madrid", 28000));
			beneficiarioPrueba.setCentroSalud(cabecera.getCentroSalud());
			beneficiarioPrueba.setMedicoAsignado(cabecera);
			beneficiarioPrueba = UtilidadesPruebas.crearBeneficiario(controlador, beneficiarioPrueba);
			
			// Obtenemos el panel
			Panel p1 = winPrincipal.getPanel("jPanelGestionarCitas");
			panelCita = (JPCitaConsultarBeneficiario)p1.getPanel("jPanelConsultarCitasBeneficiario").getAwtContainer();
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panelCita);
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
			btnCitasHistorico = pnlPanel.getButton("btnHistoricoCitas");
			btnAnular = pnlPanel.getButton("btnAnular");
			btnRestablecer = pnlPanel.getButton("btnRestablecer");
			tblCitas = pnlPanel.getTable("tblTablaCitas");
			
			jcmbIdentificacion = (JComboBox)cmbIdentificacion.getAwtComponent();
			jtxtIdentificacion = (JTextField)txtIdentificacion.getAwtComponent();
			jtxtNIF = (JTextField)txtNIF.getAwtComponent();
			jtxtNSS = (JTextField)txtNSS.getAwtComponent();
			jtxtNombre = (JTextField)txtNombre.getAwtComponent();
			jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
			jtxtMedicoAsignado = (JTextField)txtMedicoAsignado.getAwtComponent();
			jcmbCentros = (JComboBox)cmbCentros.getAwtComponent();
			jtblCitas = (JTable) tblCitas.getAwtComponent();
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesión auxiliar de las pruebas del observador
			UtilidadesPruebas.cerrarControladorAuxiliar();
			// Borramos los objetos de prueba			
			if (!beneficiarioEliminado) controlador.eliminarBeneficiario(beneficiarioPrueba);
			if (!medicoEliminado) controlador.eliminarUsuario(cabecera);
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
			// Probamos con un NIF que no esté dado de alta en el sistema
			txtIdentificacion.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "El beneficiario con NIF 00000000A no se encuentra dado de alta en el sistema.");
			// Buscamos un beneficiario por su NSS
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			// Ponemos un NSS incorrecto y comprobamos que el campo del
			// identificacion se selecciona por tener un formato inválido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NSSIncorrectoException().getMessage());
			// Probamos con un NSS que no esté dado de alta en el sistema
			txtIdentificacion.setText("000000000000");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "El beneficiario con NSS 000000000000 no se encuentra dado de alta en el sistema.");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testBuscarBeneficiarioPorNIF () {
		// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(0);
		txtIdentificacion.setText(beneficiarioPrueba.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
		// Se comprueba que tiene médico asignado
		assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
		// La tabla de citas debe estar vacía
		assertTrue(tblCitas.getRowCount()==0);
		assertFalse(btnAnular.isEnabled());
		btnCitasHistorico.click();
		assertTrue(tblCitas.getRowCount()==0);
		assertFalse(btnAnular.isEnabled());
	}

	public void testBuscarBeneficiarioPorNSS () {
		// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
		jcmbIdentificacion.grabFocus();
		jcmbIdentificacion.setSelectedIndex(1);
		txtIdentificacion.setText(beneficiarioPrueba.getNss());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
		// Se comprueba que tiene médico asignado
		assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
		// La tabla de citas debe estar vacía
		assertTrue(tblCitas.getRowCount()==0);
		assertFalse(btnAnular.isEnabled());
		btnCitasHistorico.click();
		assertTrue(tblCitas.getRowCount()==0);
		assertFalse(btnAnular.isEnabled());
	}
	
	public void testBuscarCita () {
		// Creamos una cita para el beneficiario
		Cita c;
		try {
			c = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);
		
			// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La tabla de citas debe tener un elemento
			assertTrue(tblCitas.getRowCount()==1);
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(cabecera.getNif(), tblCitas.getContentAt(0, 3));
			controlador.anularCita(c);
		} catch(Exception e) {
			fail(e.toString());
		}
	}	
	
	public void testAnularCita () {
		// Creamos una cita para el beneficiario
		Cita c1, c2;
		try {
			c1 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);
		
			// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La tabla de citas debe tener un elemento
			assertTrue(tblCitas.getRowCount()==1);
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==1);
			// El boton de anular debe estar habilitado
			assertTrue(btnAnular.isEnabled());
			assertEquals(cabecera.getNif(), tblCitas.getContentAt(0, 3));
			tblCitas.selectRow(0);
			WindowInterceptor.init(btnAnular.triggerClick()).process(new WindowHandler() {
				// Aviso para confirmar la eliminacion
				public Trigger process(Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			// La tabla de citas debe estar vacia
			assertTrue(tblCitas.getRowCount()==0);
			assertFalse(btnAnular.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Probamos a tener más de una cita
		try {
			c1 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);
			c2 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,30), IConstantes.DURACION_CITA);
			// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La tabla de citas debe tener dos elementos
			assertTrue(tblCitas.getRowCount()==2);
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==2);
			// El boton de anular debe estar habilitado (porq se selecciona una cita por defecto)
			assertTrue(btnAnular.isEnabled());
			assertEquals(cabecera.getNif(), tblCitas.getContentAt(0, 3));
			assertEquals(cabecera.getNif(), tblCitas.getContentAt(1, 3));
			// Eliminamos una cita
			tblCitas.selectRow(0);
			WindowInterceptor.init(btnAnular.triggerClick()).process(new WindowHandler() {
				// Aviso para confirmar la eliminacion
				public Trigger process(Window window) {
					return window.getButton(YES_OPTION).triggerClick();
				}
			}).run();
			// La tabla de citas debe contener una cita
			assertTrue(tblCitas.getRowCount()==1);
			assertTrue(btnAnular.isEnabled());
			
			// Eliminamos la cita que falta
			controlador.anularCita(c2);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}	
	
	public void testRestablecer () {
		btnRestablecer.click();
		assertFalse(btnAnular.isEnabled());
		assertTrue(tblCitas.getRowCount()==0);
		assertTrue(txtIdentificacion.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNSS.getText().equals(""));
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
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
		// Se comprueba que tiene médico asignado
		assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
		try {
			// En este momento el segundo administrador modifica el beneficiario
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					beneficiarioPrueba.setNombre("Otro Nombre");
					controladorAuxiliar.modificarBeneficiario(beneficiarioPrueba);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio en el beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(500);
			// La ventana del primer administrador se ha debido actualizar con el nuevo nombre del beneficiario
			assertEquals(txtNombre.getText(), beneficiarioPrueba.getNombre());
			
			// Ahora eliminamos el beneficiario
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
			Thread.sleep(500);
			// La ventana del primer administrador se ha debido limpiar
			comprobarCamposRestablecidos();
			beneficiarioEliminado = true;
		} catch (Exception e) {
			fail (e.toString());
		}
	}
	
	public void testObservadorUsuarioActualizadoEliminado () {
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
		assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
		try{
			// En este momento el segundo administrador modifica el médico asignado al beneficiario
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					cabecera.setNombre("Otro nombre");
					controladorAuxiliar.modificarMedico(cabecera);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio del médico asignado al beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(500);
			// La ventana del primer administrador se ha debido actualizar con el nuevo nombre de medico de cabecera
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			
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
			Thread.sleep(500);
			// La ventana del primer administrador se ha debido actualizar borrando los campos, pues el beneficiario se ha quedado sin médico asignado
			comprobarCamposRestablecidos();
			medicoEliminado = true;
		} catch (Exception e) {
			fail (e.toString());
		}		
	}
	
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
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			assertTrue(tblCitas.getRowCount()==0);
			// Pedimos una cita desde el controlador auxiliar
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.pedirCita(beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado().getNif(), new Date(2015-1900,3-1,4,10,0), IConstantes.DURACION_CITA);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la nueva cita registrada para este beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(500);
			// La tabla de citas se debe haber actualizado
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(jtblCitas.getModel().getValueAt(0, 3), cabecera.getNif());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ahora procedemos a eliminar la cita desde el segundo administrador
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.anularCita(new Cita(new Date(2015-1900,3-1,4,10,0), IConstantes.DURACION_CITA, beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado()));
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la anulación de la cita
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(500);
			// La tabla se debe haber quedado vacia
			assertTrue(tblCitas.getRowCount()==0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	private void comprobarCamposRestablecidos () {
		assertTrue(txtIdentificacion.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNSS.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
		assertTrue(txtApellidos.getText().equals(""));
		assertFalse(jcmbCentros.isEnabled());
		assertTrue(txtMedicoAsignado.getText().equals(""));
		assertTrue(tblCitas.getRowCount()==0);
	}

}
