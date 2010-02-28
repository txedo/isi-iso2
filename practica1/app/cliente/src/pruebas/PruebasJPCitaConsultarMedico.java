package pruebas;

import java.util.Date;

import javax.swing.JTable;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPCitaConsultarMedico;

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
import excepciones.NIFIncorrectoException;
import excepciones.UsuarioYaExistenteException;

public class PruebasJPCitaConsultarMedico extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, pruebas.IConstantes {

	private ControladorCliente controlador;
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
	private JTextField jtxtNIF;
	private JTextField jtxtLogin;
	private JTextField jtxtNombre;
	private JTextField jtxtApellidos;
	private JTextField jtxtNIFBuscado;
	private JTable jtblCitas;
	private Window winPrincipal;
	
	private Medico cabecera;
	private TipoMedico tCabecera;
	private Beneficiario beneficiarioPrueba;
	private PeriodoTrabajo periodo1;
	
	private boolean valido;
	private String login;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		valido = false;
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
			
			// Creamos un médico
			tCabecera = new Cabecera();
			login = UtilidadesPruebas.generarLogin();
			cabecera = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Eduardo", "PC", "", "", "", tCabecera);
			periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Miercoles);
			cabecera.getCalendario().add(periodo1);
			
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
			
			// Consultamos el médico de nuevo, porque el centro de salud que realmente se le asigna
			// se hace de manera aleatoria
			cabecera = controlador.consultarMedico(cabecera.getNif());

			// Creamos el beneficiario en el mismo centro que el médico, para que no se le asigne otro diferente 
			beneficiarioPrueba = new Beneficiario ();
			beneficiarioPrueba.setNif(UtilidadesPruebas.generarNIF());
			beneficiarioPrueba.setNss(UtilidadesPruebas.generarNSS());
			beneficiarioPrueba.setNombre("beneficiario");
			beneficiarioPrueba.setApellidos("de prueba");
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

			// Creamos el panel
			panelCita = new JPCitaConsultarMedico(controlador.getVentanaPrincipal(), controlador);
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
			
			jtxtNIFBuscado = (JTextField)txtNIFBuscado.getAwtComponent();
			jtxtNIF = (JTextField)txtNIF.getAwtComponent();
			jtxtNombre = (JTextField)txtNombre.getAwtComponent();
			jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
			jtxtLogin = (JTextField)txtLogin.getAwtComponent();
			jtblCitas = (JTable) tblCitas.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			controlador.eliminarBeneficiario(beneficiarioPrueba);
			controlador.eliminarUsuario(cabecera);
			// Cerramos la sesión y la ventana del controlador
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
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inválido
			txtNIFBuscado.setText("111111");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// Probamos con un NIF que no esté dado de alta en el sistema
			txtNIFBuscado.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "No existe ningún médico con el NIF introducido.");
			// Ponemos un NIF correcto
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");		
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testBuscarMedico () {
		// Probamos con el NIF del médico de cabecera que es correcto y está dado de alta en el sistema
		txtNIFBuscado.setText(cabecera.getNif());
		assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
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
			c1 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);
			
			// Buscamos el médico
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
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
			c1 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,15), IConstantes.DURACION_CITA);
			c2 = controlador.pedirCita(beneficiarioPrueba, cabecera.getNif(), new Date(2010-1900,5,16,10,30), IConstantes.DURACION_CITA);
			// Buscamos el médico
			txtNIFBuscado.setText(cabecera.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Usuario encontrado.");
			assertEquals(txtNIF.getText(), cabecera.getNif());
			// La tabla de citas debe tener dos elementos
			assertTrue(tblCitas.getRowCount()==2);
			btnCitasHistorico.click();
			assertTrue(tblCitas.getRowCount()==2);			
			// Eliminamos las citas
			controlador.anularCita(c1);
			controlador.anularCita(c2);
		} catch(Exception e) {
			e.printStackTrace();
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
}
