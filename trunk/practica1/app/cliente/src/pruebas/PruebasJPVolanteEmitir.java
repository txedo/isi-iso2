package pruebas;

import java.util.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.ComboBox;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPVolanteEmitir;
import presentacion.auxiliar.OperacionesInterfaz;

import comunicaciones.ConfiguracionCliente;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.control.ControladorCliente;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;

public class PruebasJPVolanteEmitir extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, IConstantesPruebas {

	private ControladorCliente controlador, controladorAuxiliar;
	private JPVolanteEmitir pnlEmitir;
	private Panel panelBeneficiario;
	private Panel pnlPanel;	
	private Button btnEmitir;
	private Button btnBuscar;
	private ComboBox cmbIdentificacion;
	private TextBox txtIdentificacion;
	private TextBox txtNIF;
	private TextBox txtNSS;
	private TextBox txtNombre;
	private TextBox txtApellidos;
	private TextBox txtMedicoAsignado;
	private ComboBox cmbEspecialidad;
	private ListBox lstEspecialistas;
	private JComboBox jcmbIdentificacion;
	private JTextField jtxtIdentificacion;
	private JTextField jtxtNIF;
	private JTextField jtxtNSS;
	private JTextField jtxtNombre;
	private JTextField jtxtApellidos;
	private JComboBox jcmbEspecialidad;
	private JList jlstEspecialistas;
	private Window winPrincipal;
	
	private Medico cabecera, especialista, medicoAuxiliar = null;
	private TipoMedico tCabecera, tEspecialista;
	private Beneficiario beneficiarioPrueba;
	private Vector<Medico> medicosEliminados;
	private boolean beneficiarioEliminado, medicoEliminado;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		beneficiarioEliminado = false;
		medicoEliminado = false;
		medicosEliminados = new Vector<Medico>();
		
		try {
			// Iniciamos sesion con un administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.usuarioAdmin, IDatosConexionPruebas.passwordAdmin);
				
			tEspecialista = new Especialista("Neurologia");
			
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
			// Establecemos la operación activa de la ventana principal
			controlador.getVentanaPrincipal().setOperacionSeleccionada(OperacionesInterfaz.EmitirVolante);
			
			Panel p1 = winPrincipal.getPanel("jPanelGestionarVolantes");
			pnlEmitir = (JPVolanteEmitir) p1.getPanel("jPanelEmitir").getAwtContainer();
			// Obtenemos los componentes del panel
			pnlPanel = new Panel(pnlEmitir);
			panelBeneficiario = pnlPanel.getPanel("pnlBeneficiario");
			cmbIdentificacion = panelBeneficiario.getComboBox("cmbIdentificacion");
			txtIdentificacion = panelBeneficiario.getTextBox("txtIdentificacion");
			txtNIF = panelBeneficiario.getTextBox("txtNIF");
			txtNSS = panelBeneficiario.getTextBox("txtNSS");
			txtNombre = panelBeneficiario.getTextBox("txtNombre");
			txtApellidos = panelBeneficiario.getTextBox("txtApellidos");
			txtMedicoAsignado = panelBeneficiario.getTextBox("txtMedicoAsignado");
			btnEmitir = pnlPanel.getButton("btnEmitir");
			btnBuscar = panelBeneficiario.getButton("btnBuscar");
			lstEspecialistas = pnlPanel.getListBox("lstEspecialistas");
			cmbEspecialidad = pnlPanel.getComboBox("cmbEspecialidad");
			
			jcmbIdentificacion = (JComboBox)cmbIdentificacion.getAwtComponent();
			jtxtIdentificacion = (JTextField)txtIdentificacion.getAwtComponent();
			jtxtNIF = (JTextField)txtNIF.getAwtComponent();
			jtxtNSS = (JTextField)txtNSS.getAwtComponent();
			jtxtNombre = (JTextField)txtNombre.getAwtComponent();
			jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
			jlstEspecialistas = (JList)lstEspecialistas.getAwtComponent();
			jcmbEspecialidad = (JComboBox)cmbEspecialidad.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesión y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
			// Eliminados los usuarios creados en las pruebas
			if (!beneficiarioEliminado) controladorAuxiliar.eliminarBeneficiario(beneficiarioPrueba);
			if (!medicoEliminado) controladorAuxiliar.eliminarUsuario(cabecera);
			for (Medico m : medicosEliminados) {
				controladorAuxiliar.crearMedico(m);
			}
			// Cerramos la sesión auxiliar de las pruebas del observador
			UtilidadesPruebas.cerrarControladorAuxiliar();
		} catch(Exception e) {
			fail(e.toString());
		}
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
			// El botón de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inválido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// El botón de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			// Probamos con un NIF que no esté dado de alta en el sistema
			txtIdentificacion.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "El beneficiario con NIF 00000000A no se encuentra dado de alta en el sistema.");
			// El botón de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			// Buscamos un beneficiario por su NSS
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			// Ponemos un NSS incorrecto y comprobamos que el campo del
			// identificacion se selecciona por tener un formato inválido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NSSIncorrectoException().getMessage());
			// El botón de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			// Probamos con un NSS que no esté dado de alta en el sistema
			txtIdentificacion.setText("000000000000");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "El beneficiario con NSS 000000000000 no se encuentra dado de alta en el sistema.");
			// El botón de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testBuscarBeneficiarioPorNIF () {
		try {
			// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// El botón de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testBuscarBeneficiarioPorNSS () {
		try {
			// Probamos con el NIF de beneficiarioPrueba que es correcto y está dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			txtIdentificacion.setText(beneficiarioPrueba.getNss());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// El botón de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testEspecialistasVacios() {
		Vector<Medico> especialistas;
		try {
			// Limpiamos la base de datos de especialistas
			especialistas = controladorAuxiliar.obtenerMedicosTipo(tEspecialista);
			for (Medico m: especialistas) {
				controladorAuxiliar.eliminarUsuario(m);
			}
			// Buscamos un beneficiario
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// Al seleccionar una especialidad, debe haber 0 especialistas (sólo existe la cadena vacía)
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedIndex(2);
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), "");
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedIndex(0);
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), "");
			// El botón de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			
			// Volvemos a dejar la base de datos en su estado
			for (Medico m: especialistas) {
				controladorAuxiliar.crearUsuario(m);
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testEmitirVolanteIncorrecto() {
		Vector<Medico> especialistas;
		try {
			// Limpiamos la base de datos de especialistas
			especialistas = controladorAuxiliar.obtenerMedicosTipo(tEspecialista);
			for (Medico m: especialistas) {
				controladorAuxiliar.eliminarUsuario(m);
			}
			
			// Insertamos un único especialista
			tEspecialista = new Especialista("Neurologia");
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(tEspecialista);
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, especialista); 
			
			// Buscamos un beneficiario (rol de médico)
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La lista de especialista sólo debe tenr uno al seleccionar la especialidad Neurologia
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Neurologia");
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")");
			// El botón de emitir debe estar habilitado
			assertTrue(btnEmitir.isEnabled());
			// No se puede emitir volante sin seleccionar especialista
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnEmitir, OK_OPTION), "Debe seleccionar un especialista");
			
			// Volvemos a dejar la base de datos en su estado
			for (Medico m: especialistas) {
				controladorAuxiliar.crearUsuario(m);
			}
			
			controladorAuxiliar.eliminarUsuario(especialista);
		} catch(Exception e) {
			fail(e.toString());
		}
	}	
	
	public void testEmitirVolante() {
		Vector<Medico> especialistas;
		
		try {
			// Limpiamos la base de datos de especialistas
			especialistas = controladorAuxiliar.obtenerMedicosTipo(tEspecialista);
			for (Medico m: especialistas) {
				controladorAuxiliar.eliminarUsuario(m);
			}
			
			// Creamos el médico especialista que será el receptor de los volantes
			tEspecialista = new Especialista("Neurologia");
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(tEspecialista);
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, especialista); 
			
			// Buscamos un beneficiario (rol de médico)
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La lista de especialista sólo debe tenr uno al seleccionar la especialidad Neurologia
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Neurologia");
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")");
			// Seleccionamos el especialsita
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			// El botón de emitir debe estar habilitado
			assertTrue(btnEmitir.isEnabled());
			// Emitimos el volante
			assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnEmitir, OK_OPTION).contains("El volante del beneficiario se ha emitido correctamente"));
			
			// Volvemos a dejar la base de datos en su estado
			for (Medico m: especialistas) {
				controladorAuxiliar.crearUsuario(m);
			}
			controladorAuxiliar.eliminarUsuario(especialista);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testObservadorBeneficiario () {
		// Comprueba que cuando se actualiza o elimina un beneficiario desde un teminal,
		// las ventanas de otros terminales se actualizan correctamente
		try {			
			// El médico busca al beneficiario de prueba
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			
			// En este momento el administrador modifica el beneficiario
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
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido limpiar
			comprobarCamposRestablecidos();
			beneficiarioEliminado = true;
		} catch (Exception e) {
			fail (e.toString());
		}
	}
	
	public void testObservadorUsuario () {
		// Comprueba que cuando se actualiza o elimina un usuario desde un teminal,
		// las ventanas de otros terminales se actualizan correctamente
		try {
			// Borramos todos los médicos de cabecera del sistema excepto el médico de cabecera de prueba
			medicosEliminados = controladorAuxiliar.obtenerMedicosTipo(tCabecera);
			for (Medico m : medicosEliminados) {
				if (!m.getLogin().equals(cabecera.getLogin()))
					controladorAuxiliar.eliminarMedico(m);
			}
			// Creamos un médico auxiliar y se lo asignamos al beneficiario
			// De este modo cuando eliminemos el médico asignado al beneficiario, no será el mismo que tiene iniciada la sesión
			medicoAuxiliar = new Medico();
			medicoAuxiliar.setNombre("Rodrigo");
			medicoAuxiliar.setApellidos("de Juan Tellez");
			medicoAuxiliar.setTipoMedico(tCabecera);
			medicoAuxiliar.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			medicoAuxiliar = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, medicoAuxiliar);
			beneficiarioPrueba.setMedicoAsignado(medicoAuxiliar);
			beneficiarioPrueba.setCentroSalud(medicoAuxiliar.getCentroSalud());
			controladorAuxiliar.modificarBeneficiario(beneficiarioPrueba);
		} catch (Exception e) {
			fail (e.toString());
		} 
		try{		
			// Creamos (como administrador) el médico especialista que se va a modificar/ eliminar
			tEspecialista = new Especialista("Cardiologia");
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(tEspecialista);
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, especialista); 
			
			// El médico busca al beneficiario de prueba
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene médico asignado
			assertEquals(medicoAuxiliar.getApellidos() + ", " + medicoAuxiliar.getNombre() + " (" + medicoAuxiliar.getNif() + ")", txtMedicoAsignado.getText());
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Cardiologia");
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			
			// En este momento el administrador modifica el médico asignado al beneficiario
			medicoAuxiliar.setNombre("Otro nombre");
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.modificarMedico(medicoAuxiliar);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio del médico asignado al beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar con el nuevo nombre del médico de cabecera
			assertEquals(medicoAuxiliar.getApellidos() + ", " + medicoAuxiliar.getNombre() + " (" + medicoAuxiliar.getNif() + ")", txtMedicoAsignado.getText());
			// Volvemos a seleccionar la especialidad del especialista de prueba (porque la interfaz resetea la selección)
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Cardiologia");
			
			// En este momento el administrador modifica el único especialista registrado en el sistema
			especialista.setApellidos("Otros apellidos");
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.modificarMedico(especialista);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio en el especialista para el que se iba a emitir el volante
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del médico se ha debido actualizar con los nuevos apellidos del especialista
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			assertEquals(especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")", jlstEspecialistas.getSelectedValue().toString());
		
			// Ahora procedemos a eliminar el médico asignado al beneficiaro
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(medicoAuxiliar);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminación del médico asignado al beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del médico se ha debido actualizar borrando los campos, pues el beneficiario se ha quedado sin médico asignado
			comprobarCamposRestablecidos();
			medicoEliminado = true;
			
			// Automaticamente se le ha asignado al médico el otro médico de cabecera que hay registrado en la base de datos, que es el que tiene la sesión iniciada
			// Volvemos a consultar el beneficiario y comprobamos que es cierto
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			// TODO FALLA A PARTIR DE AQUI
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// Volvemos a seleccionar la especialidad Cardiologia
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Cardiologia");
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			// Ahora procedemos a eliminar el especialista para el que se iba a emitir el volante
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(especialista);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminación del especialista
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT+1000);
			// La ventana del médico se ha debido actualizar, no mostrando ningún especialista
			assertEquals(jlstEspecialistas.getModel().getSize(), 0);
		} catch (Exception e) {
			fail (e.toString());
		}		
	}
	
	private void comprobarCamposRestablecidos () {
		assertTrue(txtIdentificacion.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNSS.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
		assertTrue(txtApellidos.getText().equals(""));
		assertTrue(txtMedicoAsignado.getText().equals(""));
	}

}
