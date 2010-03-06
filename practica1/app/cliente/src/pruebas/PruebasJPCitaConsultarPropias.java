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
			panel = (JPCitaConsultarPropias)p1.getPanel("jPanelConsultarCitasPropias").getAwtContainer();
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(panel);
			tblCitas = pnlPanel.getTable("tblTablaCitas");
			btnCitasPendientes = pnlPanel.getButton("btnCitasPendientes");
			btnCitasHistorico = pnlPanel.getButton("btnCitasHistorico");		
			
			jtblCitas = (JTable) tblCitas.getAwtComponent();
			
			// Iniciamos sesion como el médico asignado al beneficiario, para consultar sus citas
			controlador.cerrarSesion();
			winPrincipal.dispose();
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
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		// Para borrar los datos, hay que entrar con la sesión del administrador
		try {
			// Cerramos la sesión auxiliar de las pruebas del observador
			UtilidadesPruebas.cerrarControladorAuxiliar();
			// Para borrar los datos, hay que entrar con la sesión del administrador
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
			controlador.eliminarMedico(cabecera);
			if (!beneficiarioEliminado) controlador.eliminarBeneficiario(beneficiarioPrueba);
			// Cerramos la sesión y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
		} catch(Exception e) {
			e.printStackTrace();
		}
		winPrincipal.dispose();
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
		
		// Registramos una cita futura de prueba (se necesita rol de administrador)
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
			c1 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);

			// Consultamos la cita del medico del beneficiario
			controlador.cerrarSesion();
			winPrincipal.dispose();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IPServidorFrontend, puertoServidorFrontend), cabecera.getLogin(), cabecera.getLogin());
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(tblCitas.getContentAt(0, 3), beneficiarioPrueba.getNif());
			btnCitasPendientes.click();
			assertTrue(tblCitas.getRowCount()==1);
			assertEquals(tblCitas.getContentAt(0, 3), beneficiarioPrueba.getNif());
			
		} catch (Exception e) {
			fail(e.toString());
		}
		
		// Borramos la cita (como administrador)
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
			controlador.anularCita(c1);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	public void testObservadorBeneficiarioActualizadoEliminado () {
		Cita c1 = null;
		
		// Iniciamos sesión con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.usuarioAdminAuxiliar, IDatosConexionPruebas.passwordAdminAuxiliar);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// Registramos una cita futura de prueba para el médico (se necesita rol de administrador)
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
			c1 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);
			
			// Iniciamos de nuevo sesión como médico
			controlador.cerrarSesion();
			winPrincipal.dispose();
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
			
			// Consultamos las citas del médico, que debe ser una
			btnCitasPendientes.click();
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
					// TODO: no se muestra esta ventana
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
			controladorAuxiliar.anularCita(c1);
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
			// Indicamos que la operación activa del médico es la de consultar las citas propias de un médico
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.ConsultarCitasPropias);

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
			Thread.sleep(TIME_OUT);
			// La tabla se debe haber quedado vacia
			assertTrue(tblCitas.getRowCount()==0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
