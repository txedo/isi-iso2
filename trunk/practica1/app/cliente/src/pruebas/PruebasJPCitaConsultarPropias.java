package pruebas;

import java.util.Date;

import javax.swing.JTable;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPCitaConsultarPropias;

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
import excepciones.BeneficiarioYaExistenteException;
import excepciones.UsuarioYaExistenteException;

public class PruebasJPCitaConsultarPropias extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas {

	private ControladorCliente controlador;
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
	private PeriodoTrabajo periodo1;
	
	private boolean valido;
	private String login;
	
	protected void setUp() {
		valido = false;
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
			
			// Creamos un médico
			tCabecera = new Cabecera();
			login = UtilidadesPruebas.generarLogin();
			cabecera = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Eduardo", "PC", "", "", "", tCabecera);
			cabecera.setCentroSalud(controlador.consultarCentros().firstElement());
			periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Miercoles);
			cabecera.getCalendario().add(periodo1);
			
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
			beneficiarioPrueba.setCentroSalud(controlador.consultarCentros().firstElement());
			beneficiarioPrueba.setMedicoAsignado(cabecera);
			
			// Mientras exista el usuario, se genera otro login y otro NIF
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
					
			// Creamos el panel
			panel = new JPCitaConsultarPropias(controlador.getVentanaPrincipal(), controlador);
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
			controlador.eliminarMedico(cabecera);
			controlador.eliminarBeneficiario(beneficiarioPrueba);
			// Cerramos la sesión y la ventana del controlador
			controlador.cerrarSesion();
			winPrincipal.dispose();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testConsultarCitasVacias() {
		// Iniciamos sesion como el médico asignado al beneficiario, para consultar sus citas
		try {
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
			// Al abrir la ventana, como el médico no tiene citas asignadas, no habrá ninguna fila (ni en citas pendientes ni en el historico)
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==0);
			btnCitasPendientes.click();
			assertTrue(tblCitas.getRowCount()==0);
		} catch(Exception e) {
			fail(e.toString());
		}

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
		
		// Borramo la cita (como administrador)
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
	
}
