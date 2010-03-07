package pruebas;

import java.util.Date;
import java.util.Vector;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JFAvisos;

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

/**
 * Pruebas de la ventana de avisos.
 */
public class PruebasJFAvisos extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas {

	private ControladorCliente controlador;
	private Panel pnlPanel;
	private Table tblTablaAvisos;
	private Button btnCerrar;
	private Window winPrincipal;
	private JFAvisos frmAvisos;
	
	private Medico cabecera;
	private TipoMedico tCabecera;
	private Beneficiario beneficiarioPrueba1, beneficiarioPrueba2;
	private PeriodoTrabajo periodo1;
	
	private boolean valido;
	private String login;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		valido = false;
		try {
			// Establecemos conexión con el servidor front-end
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
			
			// Creamos un médico de cabecera 
			tCabecera = new Cabecera();
			cabecera = new Medico();
			cabecera.setNombre("Eduardo");
			cabecera.setApellidos("Ramírez García");
			cabecera.setTipoMedico(tCabecera);
			cabecera.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			cabecera = (Medico)UtilidadesPruebas.crearUsuario(controlador, cabecera); 

			// Creamos el beneficiario en el mismo centro que el médico, para que no se le asigne otro diferente 
			beneficiarioPrueba1 = new Beneficiario();
			beneficiarioPrueba1.setNombre("beneficiario");
			beneficiarioPrueba1.setApellidos("de prueba");
			beneficiarioPrueba1.setCorreo(" ");
			beneficiarioPrueba1.setTelefono(" ");
			beneficiarioPrueba1.setMovil(" ");
			beneficiarioPrueba1.setFechaNacimiento(new Date("01/01/1980"));
			beneficiarioPrueba1.setDireccion(new Direccion("lagasca", "", "", "", "Madrid", "Madrid", 28000));
			beneficiarioPrueba1.setCentroSalud(cabecera.getCentroSalud());
			beneficiarioPrueba1.setMedicoAsignado(cabecera);
			beneficiarioPrueba1 = UtilidadesPruebas.crearBeneficiario(controlador, beneficiarioPrueba1);
			
			beneficiarioPrueba2 = new Beneficiario ();
			beneficiarioPrueba2.setNif(UtilidadesPruebas.generarNIF());
			beneficiarioPrueba2.setNss(UtilidadesPruebas.generarNSS());
			beneficiarioPrueba2.setNombre("beneficiario");
			beneficiarioPrueba2.setApellidos("de prueba 2");
			beneficiarioPrueba2.setCorreo(" ");
			beneficiarioPrueba2.setTelefono(" ");
			beneficiarioPrueba2.setMovil(" ");
			beneficiarioPrueba2.setFechaNacimiento(new Date("01/10/1990"));
			beneficiarioPrueba2.setDireccion(new Direccion("lagasca", "", "", "", "Madrid", "Madrid", 28000));
			beneficiarioPrueba2.setCentroSalud(controlador.consultarCentros().firstElement());
			beneficiarioPrueba2.setMedicoAsignado(cabecera);
			beneficiarioPrueba1 = UtilidadesPruebas.crearBeneficiario(controlador, beneficiarioPrueba2);
			
			frmAvisos = new JFAvisos();
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(frmAvisos);
			tblTablaAvisos = pnlPanel.getTable("tblTablaAvisos");
			btnCerrar = pnlPanel.getButton("btnCerrar");
					
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			controlador.eliminarMedico(cabecera);
			controlador.eliminarBeneficiario(beneficiarioPrueba1);
			controlador.eliminarBeneficiario(beneficiarioPrueba2);
			// Cerramos la sesión y la ventana del controlador
			controlador.cerrarSesion();
			winPrincipal.dispose();
			frmAvisos.dispose();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void testMostrarAvisoVacio() {
		try {			
			Window win;
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						frmAvisos.setVisible(true);
						assertTrue(tblTablaAvisos.getRowCount()==0);
						btnCerrar.click();
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testMostrarBeneficiarios() {
		try {
			Window win;
			final Vector<Beneficiario> beneficiarios = new Vector<Beneficiario>();
			beneficiarios.add(beneficiarioPrueba1);
			beneficiarios.add(beneficiarioPrueba2);
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						frmAvisos.mostrarBeneficiarios("Beneficiario", beneficiarios);
						assertEquals(tblTablaAvisos.getRowCount(), 2);
						btnCerrar.click();
					} catch(Exception e) {
						e.printStackTrace();
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void testMostrarCitas() {
		try {
			// Registramos una cita para el medico
			Cita c = controlador.pedirCita(beneficiarioPrueba1, cabecera.getNif(), new Date(2010-1900,8,8,10,15), IConstantes.DURACION_CITA);
			
			Window win;
			final Vector<Cita> citas = new Vector<Cita>();
			citas.add(c);
			
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						frmAvisos.mostrarCitas("Citas", citas);
						assertEquals(tblTablaAvisos.getRowCount(), 1);
						btnCerrar.click();
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	

}
