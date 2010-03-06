package pruebas;

import java.util.Date;

import javax.swing.JTable;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPCitaConsultarPropias;
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

public class PruebasJPCitaConsultarPropias extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {

	private ControladorCliente controlador, controladorAuxiliar;
	private JPCitaConsultarPropias panel;
	private Panel pnlPanel;
	private Table tblCitas;
	private Button btnCitasPendientes;
	private Button btnCitasHistorico;
	private JTable jtblCitas;
	private Window winPrincipal;
	
	private Medico cabecera;
	private TipoMedico tCabecera;
	private Beneficiario beneficiarioPrueba;
	
	private boolean beneficiarioEliminado;
		
	protected void setUp() {

		beneficiarioEliminado = false;
		
		try {
			// Iniciamos sesion con un administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.usuarioAdmin, IDatosConexionPruebas.passwordAdmin);
			
			// Creamos un médico de cabecera 
			tCabecera = new Cabecera();
			cabecera = new Medico();
			cabecera.setNombre("Eduardo");
			cabecera.setApellidos("Ramírez García");
			cabecera.setTipoMedico(tCabecera);
			cabecera.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			cabecera = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, cabecera); 

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
			beneficiarioPrueba = UtilidadesPruebas.crearBeneficiario(controladorAuxiliar, beneficiarioPrueba);
			
			// Iniciamos sesion con el médico de cabecera de prueba que hemos creado
			controlador = new ControladorCliente();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IPServidorFrontend, puertoServidorFrontend), cabecera.getLogin(), cabecera.getLogin());
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			// Indicamos que la operación activa del médico es la de consultar las citas propias de un médico
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.ConsultarCitasPropias);
			
			// Obtenemos el panel
			Panel p1 = winPrincipal.getPanel("jPanelGestionarCitas");
			panel = (JPCitaConsultarPropias)p1.getPanel("jPanelConsultarCitasPropias").getAwtContainer();

			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panel);
			tblCitas = pnlPanel.getTable("tblTablaCitas");
			btnCitasPendientes = pnlPanel.getButton("btnCitasPendientes");
			btnCitasHistorico = pnlPanel.getButton("btnCitasHistorico");		
			
			jtblCitas = (JTable) tblCitas.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Borramos los usuarios y beneficiarios creados con la sesion del administrador
			controladorAuxiliar.eliminarMedico(cabecera);
			if (!beneficiarioEliminado) controladorAuxiliar.eliminarBeneficiario(beneficiarioPrueba);
			// Cerramos la sesión auxiliar de las pruebas del observador
			UtilidadesPruebas.cerrarControladorAuxiliar();
			// Cerramos la sesión y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testConsultarCitasVacias() {
		// Al abrir la ventana, como el médico no tiene citas asignadas, no habrá ninguna fila (ni en citas pendientes ni en el historico)
		btnCitasHistorico.click();
		assertTrue(tblCitas.getRowCount()==0);
		btnCitasPendientes.click();
		assertTrue(tblCitas.getRowCount()==0);
	}
	
	public void testConsultarCitas() {
		Cita c1 = null;
		
		try {
			// Registramos una cita futura de prueba (se necesita rol de administrador)
			c1= controladorAuxiliar.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);

			// Consultamos la cita del medico del beneficiario
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(tblCitas.getContentAt(0, 3), beneficiarioPrueba.getNif());
			btnCitasPendientes.click();
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(tblCitas.getContentAt(0, 3), beneficiarioPrueba.getNif());
		} catch (Exception e) {
			fail(e.toString());
		}
		
		try {
			// Borramos la cita (como administrador)
			controladorAuxiliar.anularCita(c1);
			// TODO comprobar que se actualiza en la ventana del médico
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	public void testObservadorBeneficiario () {
		// Comprueba que cuando se actualiza o elimina un beneficiario desde un teminal,
		// las ventanas de otros terminales se actualizan correctamente
		Cita c1 = null;

		try {
			// Registramos una cita futura de prueba para el médico (se necesita rol de administrador)
			c1 = controladorAuxiliar.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);
			
			// Consultamos las citas del médico, que debe ser una
			btnCitasPendientes.click();
			assertTrue(tblCitas.getRowCount()==1);
		
			// En este momento el administrador modifica el beneficiario de la cita
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
			// La ventana del médico se ha debido actualizar con el nuevo nombre del beneficiario
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
			// La tabla de citas debe estar vacía, pues se ha borrado el beneficiario de la cita
			assertTrue(tblCitas.getRowCount()==0);
			beneficiarioEliminado = true;
			// Borramos la cita de prueba
			try {
				controladorAuxiliar.anularCita(c1);
				fail ("ERROR: Al borrar el beneficiario, la cita debería haberse eliminado en cascada.");
			} catch (Exception e) {
				// Oraculo negativo, debe producirse esta excepcion para un funcionamiento correcto
			}
		} catch (Exception e) {
			fail (e.toString());
		}
	}
	
	public void testObservadorCitas() {
		try {
			// Consultamos las citas del médico, que no debe tener ninguna
			btnCitasPendientes.click();
			assertTrue(tblCitas.getRowCount()==0);
			// Pedimos una cita para este médico desde el controlador auxiliar
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.pedirCita(beneficiarioPrueba, beneficiarioPrueba.getMedicoAsignado().getNif(), new Date(2015-1900,3-1,4,10,0), IConstantes.DURACION_CITA);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la nueva cita registrada
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			Thread.sleep(TIME_OUT);
			// La tabla de citas se debe haber actualizado
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(jtblCitas.getModel().getValueAt(0, 3), beneficiarioPrueba.getNif());
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
			Thread.sleep(TIME_OUT);
			// La tabla se debe haber quedado vacia
			assertTrue(tblCitas.getRowCount()==0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
