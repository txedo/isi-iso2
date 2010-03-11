package pruebas;

import java.util.Date;

import javax.swing.JTable;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPCitaConsultarMedico;
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

/**
 * Pruebas del panel de consulta de citas de médicos.
 */
public class PruebasJPCitaConsultarMedico extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {

	private ControladorCliente controlador, controladorAuxiliar;
	private JPCitaConsultarMedico panelCita;
	private Panel panelUsuario;
	private Panel pnlPanel;
	private Table tblCitas;
	private Button btnCitasHistorico;
	private Button btnBuscar;
	private Button btnRestablecer;
	private TextBox txtNIF;
	private TextBox txtLogin;
	private TextBox txtNombre;
	private TextBox txtApellidos;	
	private TextBox txtNIFBuscado;
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
						controlador.iniciarSesion(new ConfiguracionCliente(IP_SERVIDOR_FRONTEND, PUERTO_SERVIDOR_FRONTEND), USUARIO_ADMIN, PASSWORD_ADMIN);
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			
			// Indicamos que la operación activa del primer administador es la de consultar las citas de un médico 
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.ConsultarCitasMedico);
			
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
			panelCita = (JPCitaConsultarMedico)p1.getPanel("jPanelConsultarCitasMedico").getAwtContainer();
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panelCita);
			panelUsuario = pnlPanel.getPanel("pnlUsuario");
			txtNIFBuscado = panelUsuario.getTextBox("txtNIFBuscado");
			txtNIF = pnlPanel.getTextBox("txtNIF");
			txtNombre = pnlPanel.getTextBox("txtNombre");
			txtApellidos = pnlPanel.getTextBox("txtApellidos");
			txtLogin = panelUsuario.getTextBox("txtLogin");
			btnBuscar = panelUsuario.getButton("btnBuscar");
			btnCitasHistorico = pnlPanel.getButton("btnHistoricoCitas");
			btnRestablecer = pnlPanel.getButton("btnRestablecer");
			tblCitas = pnlPanel.getTable("tblTablaCitas");

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
	
	public void testDatosInvalidos() {			
		try {
			// Ponemos un NIF nulo
			txtNIFBuscado.setText("");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			assertFalse(btnCitasHistorico.isEnabled());
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inválido
			txtNIFBuscado.setText("111111");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			assertFalse(btnCitasHistorico.isEnabled());
			// Probamos con un NIF que no esté dado de alta en el sistema
			txtNIFBuscado.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "No existe ningún médico con el NIF introducido.");
			assertFalse(btnCitasHistorico.isEnabled());
			// Ponemos un NIF correcto
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Médico encontrado.");		
			assertTrue(btnCitasHistorico.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testBuscarMedico () {
		// Probamos con el NIF del médico de cabecera que es correcto y está dado de alta en el sistema
		txtNIFBuscado.setText(cabecera.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Médico encontrado.");
		assertEquals(txtNIF.getText(), cabecera.getNif());
		// La tabla de citas debe estar vacía
		assertTrue(tblCitas.getRowCount()==0);
		btnCitasHistorico.click();
		assertTrue(tblCitas.getRowCount()==0);
	}
	
	public void testBuscarCita () {
		// Creamos una cita para el beneficiario
		Cita c1, c2;
		try {
			c1 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,IConstantes.DURACION_CITA), IConstantes.DURACION_CITA);
			
			// Buscamos el médico
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Médico encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			// La tabla de citas debe tener un elemento
			assertTrue(tblCitas.getRowCount()==1);
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(beneficiarioPrueba.getNif(), tblCitas.getContentAt(0, 3));
			controlador.anularCita(c1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Probamos a tener más de una cita
		try {
			c1 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,IConstantes.DURACION_CITA), IConstantes.DURACION_CITA);
			c2 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,2*IConstantes.DURACION_CITA), IConstantes.DURACION_CITA);
			// Buscamos el médico
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Médico encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			// La tabla de citas debe tener dos elementos
			assertTrue(tblCitas.getRowCount()==2);
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==2);			
			// Eliminamos las citas
			controlador.anularCita(c1);
			controlador.anularCita(c2);
		} catch(Exception e) {
			fail(e.toString());
		}
	}	
		
	public void testRestablecer () {
		btnRestablecer.click();
		assertTrue(tblCitas.getRowCount()==0);
		assertTrue(txtNIFBuscado.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
	}
	
	public void testObservadorBeneficiarioActualizadoEliminado () {
		// Iniciamos sesión con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Se pide una cita de prueba para el beneficiario de prueba y su médico asignado
			controlador.pedirCita(beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado().getNif(), new Date(2015-1900,3-1,4,10,IConstantes.DURACION_CITA), IConstantes.DURACION_CITA);
			
			// El primer administrador busca al médico de prueba
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Médico encontrado.");
			assertEquals(txtNIFBuscado.getText(), "");
			// Debe haber una cita en la tabla
			assertTrue(tblCitas.getRowCount()==1);
			
			// En este momento el segundo administrador modifica el beneficiario de la cita
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
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar con el nuevo nombre del beneficiario
			assertEquals(jtblCitas.getModel().getValueAt(0, 2), beneficiarioPrueba.getApellidos() + ", " + beneficiarioPrueba.getNombre());
			
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
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar con la nueva tabla de citas
			assertTrue(tblCitas.getRowCount()==0);
			beneficiarioEliminado = true;
		} catch (Exception e) {
			fail (e.toString());
		}
	}
	
	public void testObservadorUsuarioActualizadoEliminado () {
		// Iniciamos sesión con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// El primer administrador busca al médico de prueba
		txtNIFBuscado.setText(cabecera.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Médico encontrado.");
		assertEquals(txtNIFBuscado.getText(), "");
		assertTrue(tblCitas.getRowCount()==0);
		try{
			// En este momento el segundo administrador modifica el médico
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					cabecera.setNombre("Otro nombre");
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
			// La ventana del primer administrador se ha debido actualizar con el nuevo nombre del médico de cabecera
			assertEquals(cabecera.getNombre(), txtNombre.getText());
			
			// Ahora procedemos a eliminar el médico desde el segundo administrador
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(cabecera);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminación del médico
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar borrando los campos, pues se ha eliminado el médico
			comprobarCamposRestablecidos();
			medicoEliminado = true;
		} catch (Exception e) {
			fail (e.toString());
		}		
	}
	
	public void testObservadorCitas() {
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN_AUXILIAR, IDatosConexionPruebas.PASSWORD_ADMIN_AUXILIAR);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Médico encontrado.");
			assertEquals(txtNIFBuscado.getText(), "");
			assertTrue(tblCitas.getRowCount()==0);
			// Pedimos una cita desde el controlador auxiliar para este médico
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.pedirCita(beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado().getNif(), new Date(2015-1900,3-1,4,10,IConstantes.DURACION_CITA), IConstantes.DURACION_CITA);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la nueva cita registrada
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			// La tabla de citas se debe haber actualizado en el primer administrador
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(jtblCitas.getModel().getValueAt(0, 3), beneficiarioPrueba.getNif());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ahora procedemos a eliminar la cita desde el segundo administrador
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.anularCita(new Cita(new Date(2015-1900,3-1,4,10,IConstantes.DURACION_CITA), IConstantes.DURACION_CITA, beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado()));
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la anulación de la cita
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			// La tabla se debe haber quedado vacia
			assertTrue(tblCitas.getRowCount()==0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	private void comprobarCamposRestablecidos () {
		assertTrue(tblCitas.getRowCount()==0);
		assertTrue(txtNIFBuscado.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
	}
}
