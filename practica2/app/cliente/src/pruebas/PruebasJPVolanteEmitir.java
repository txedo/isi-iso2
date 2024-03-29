package pruebas;

import java.util.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JList;

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
import dominio.control.ControladorCliente;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;

/**
 * Pruebas del panel de emisi�n de volantes.
 */
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
	private JComboBox jcmbEspecialidad;
	private JList jlstEspecialistas;
	private Window winPrincipal;
	
	private Medico cabecera, especialista;
	//private Medico medicoAuxiliar;
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
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.USUARIO_ADMIN, IDatosConexionPruebas.PASSWORD_ADMIN);
			
			// Creamos un m�dico de cabecera 
			cabecera = new Medico();
			cabecera.setNombre("Eduardo");
			cabecera.setApellidos("Ram�rez Garc�a");
			cabecera.setTipoMedico(new Cabecera());
			cabecera.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			cabecera = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, cabecera); 

			// Creamos el beneficiario en el mismo centro que el m�dico, para que no se le asigne otro diferente 
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
			
			// Iniciamos sesion con el m�dico de cabecera de prueba que hemos creado
			controlador = new ControladorCliente();
			winPrincipal = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						controlador.iniciarSesion(new ConfiguracionCliente(IP_SERVIDOR_FRONTEND, PUERTO_SERVIDOR_FRONTEND), cabecera.getLogin(), cabecera.getLogin());
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			// Establecemos la operaci�n activa de la ventana principal
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
			jlstEspecialistas = (JList)lstEspecialistas.getAwtComponent();
			jcmbEspecialidad = (JComboBox)cmbEspecialidad.getAwtComponent();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesi�n y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
			// Recuperamos los m�dicos eliminados
			for (Medico m : medicosEliminados) {
				controladorAuxiliar.crearMedico(m);
			}
			// Eliminados los usuarios creados en las pruebas
			if (!beneficiarioEliminado) controladorAuxiliar.eliminarBeneficiario(beneficiarioPrueba);
			if (!medicoEliminado) controladorAuxiliar.eliminarUsuario(cabecera);
			
			// Cerramos la sesi�n auxiliar de las pruebas del observador
			UtilidadesPruebas.cerrarControladorAuxiliar();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas con datos no v�lidos */
	public void testDatosInvalidos() {
		try {
			// Buscamos un beneficiario por su NIF
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			// Inicialmente probamos con un NIF nulo
			txtIdentificacion.setText("");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Debe introducir un NIF o un NSS.");
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			// Ponemos un NIF incorrecto y comprobamos que el campo de
			// identificacion se selecciona por tener un formato inv�lido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NIFIncorrectoException().getMessage());
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			// Probamos con un NIF que no est� dado de alta en el sistema
			txtIdentificacion.setText("00000000a");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "El beneficiario con NIF 00000000A no se encuentra dado de alta en el sistema.");
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			// Buscamos un beneficiario por su NSS
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			// Ponemos un NSS incorrecto y comprobamos que el campo del
			// identificacion se selecciona por tener un formato inv�lido
			txtIdentificacion.setText("11223344");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), new NSSIncorrectoException().getMessage());
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			// Probamos con un NSS que no est� dado de alta en el sistema
			txtIdentificacion.setText("000000000000");
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "El beneficiario con NSS 000000000000 no se encuentra dado de alta en el sistema.");
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testBuscarBeneficiarioPorNIF () {
		try {
			// Probamos con el NIF de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testBuscarBeneficiarioPorNSS () {
		try {
			// Probamos con el NIF de beneficiarioPrueba que es correcto y est� dado de alta en el sistema
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(1);
			txtIdentificacion.setText(beneficiarioPrueba.getNss());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	// No funciona, Hibernate produce una excepci�n StaleStateException
	public void testEspecialistasVacios() {
		Vector<Medico> especialistas;
		try {
			// Limpiamos la base de datos de especialistas
			especialistas = controladorAuxiliar.obtenerMedicosTipo(new Especialista("Neurolog�a"));
			for (Medico m: especialistas) {
				controladorAuxiliar.eliminarUsuario(m);
			}
			// Buscamos un beneficiario
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// Al seleccionar una especialidad, debe haber 0 especialistas
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedIndex(2);
			assertEquals(jlstEspecialistas.getModel().getSize(), 0);
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedIndex(0);
			assertEquals(jlstEspecialistas.getModel().getSize(), 0);
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			
			// Volvemos a dejar la base de datos en su estado
			for (Medico m: especialistas) {
				controladorAuxiliar.crearUsuario(m);
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	// No funciona, Hibernate produce una excepci�n StaleStateException
	/*public void testEmitirVolanteIncorrecto() {
		Vector<Medico> especialistas;
		try {
			// Limpiamos la base de datos de especialistas
			especialistas = controladorAuxiliar.obtenerMedicosTipo(new Especialista("Neurologia"));
			for (Medico m: especialistas) {
				controladorAuxiliar.eliminarUsuario(m);
			}
			
			// Insertamos un �nico especialista
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(new Especialista("Neurolog�a"));
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, especialista); 
			
			// Buscamos un beneficiario (rol de m�dico)
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La lista de especialista s�lo debe tener uno al seleccionar la especialidad Neurologia
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Neurolog�a");
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")");
			// El bot�n de emitir debe estar habilitado
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
	}*/
	
	public void testEmitirVolante() {
		Vector<Medico> especialistas;
		
		try {
			// Limpiamos la base de datos de especialistas
			especialistas = controladorAuxiliar.obtenerMedicosTipo(new Especialista("Neurolog�a"));
			for (Medico m: especialistas) {
				controladorAuxiliar.eliminarUsuario(m);
			}
			
			// Creamos el m�dico especialista que ser� el receptor de los volantes
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(new Especialista("Neurolog�a"));
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, especialista); 
			
			// Buscamos un beneficiario (rol de m�dico)
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La lista de especialista s�lo debe tenr uno al seleccionar la especialidad Neurologia
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Neurolog�a");
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")");
			// Seleccionamos el especialsita
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			// El bot�n de emitir debe estar habilitado
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
			// El m�dico busca al beneficiario de prueba
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
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
			// La ventana del m�dico se ha debido actualizar con el nuevo nombre del beneficiario
			assertEquals(txtNombre.getText(), beneficiarioPrueba.getNombre());
			
			// Ahora eliminamos el beneficiario
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarBeneficiario(beneficiarioPrueba);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminaci�n del beneficiario
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
	
	// No funciona, Hibernate produce una excepci�n StaleStateException
	/*public void testObservadorUsuario () {
		// Comprueba que cuando se actualiza o elimina un usuario desde un terminal,
		// las ventanas de otros terminales se actualizan correctamente
		try {
			// Borramos todos los m�dicos de cabecera del sistema excepto el m�dico de cabecera de prueba
			Vector<Medico> medicos = controladorAuxiliar.obtenerMedicosTipo(new Cabecera());
			for (Medico m : medicos) {
				if (!m.getNif().equals(cabecera.getNif())) {
					controladorAuxiliar.eliminarMedico(m);
					medicosEliminados.add(m);
				}
			}
			// Creamos un m�dico auxiliar y se lo asignamos al beneficiario
			// De este modo cuando eliminemos el m�dico asignado al beneficiario, no ser� el mismo que tiene iniciada la sesi�n (que es el m�dico de cabecera)
			medicoAuxiliar = new Medico();
			medicoAuxiliar.setNombre("Rodrigo");
			medicoAuxiliar.setApellidos("de Juan Tellez");
			medicoAuxiliar.setTipoMedico(new Cabecera());
			medicoAuxiliar.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			medicoAuxiliar = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, medicoAuxiliar);
			beneficiarioPrueba.setMedicoAsignado(medicoAuxiliar);
			beneficiarioPrueba.setCentroSalud(medicoAuxiliar.getCentroSalud());
			controladorAuxiliar.modificarBeneficiario(beneficiarioPrueba);
			
		} catch (Exception e) {
			fail (e.toString());
		} 
		try{		
			// Creamos (como administrador) el m�dico especialista que se va a modificar/ eliminar
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(new Especialista("Cardiolog�a"));
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, especialista); 
			
			// El m�dico busca al beneficiario de prueba
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(medicoAuxiliar.getApellidos() + ", " + medicoAuxiliar.getNombre() + " (" + medicoAuxiliar.getNif() + ")", txtMedicoAsignado.getText());
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Cardiolog�a");
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			assertEquals(especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")", jlstEspecialistas.getSelectedValue().toString());
			
			// En este momento el administrador modifica el m�dico asignado al beneficiario
			medicoAuxiliar.setNombre("Otro nombre");
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.modificarMedico(medicoAuxiliar);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio del m�dico asignado al beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del primer administrador se ha debido actualizar con el nuevo nombre del m�dico de cabecera
			assertEquals(medicoAuxiliar.getApellidos() + ", " + medicoAuxiliar.getNombre() + " (" + medicoAuxiliar.getNif() + ")", txtMedicoAsignado.getText());
			
			// En este momento el administrador modifica el �nico especialista registrado en el sistema
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
			// La ventana del m�dico se ha debido actualizar con los nuevos apellidos del especialista
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			assertEquals(especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")", jlstEspecialistas.getSelectedValue().toString());
		
			// Ahora procedemos a eliminar el m�dico asignado al beneficiaro
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(medicoAuxiliar);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminaci�n del m�dico asignado al beneficiario
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del m�dico se ha debido actualizar borrando los campos, pues el beneficiario se ha quedado sin m�dico asignado
			comprobarCamposRestablecidos();	
			// Volvemos a dejar la base de datos sin el especialista de prueba
			controladorAuxiliar.eliminarUsuario(especialista);
		} catch (Exception e) {
			fail (e.toString());
		}		
	}*/
	
	// No funciona, Hibernate produce una excepci�n StaleStateException
	/*public void testObservadorEspecialista() {
		try {
			// Creamos (como administrador) el m�dico especialista que se va a eliminar
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(new Especialista("Cardiolog�a"));
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, especialista);
			
			// Buscamos al beneficiario de prueba
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// Volvemos a seleccionar la especialidad Cardiologia
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Cardiolog�a");
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			assertEquals(jlstEspecialistas.getSelectedValue().toString(), especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")");
			// Ahora procedemos a eliminar el especialista para el que se iba a emitir el volante
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(especialista);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminaci�n del especialista
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(TIME_OUT);
			// La ventana del m�dico se ha debido actualizar, no mostrando ning�n especialista
			assertEquals(jlstEspecialistas.getModel().getSize(), 0);	
			
			// Ahora insertamos de nuevo el especialista y vemos que se refresca la lista de esa especialidad
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					especialista = (Medico)UtilidadesPruebas.crearUsuario(controladorAuxiliar, especialista);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la inserci�n del especialista
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// La ventana del m�dico se ha debido actualizar, mostrando 1 especialista de esa especialidad
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Cardiolog�a");
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			
			// Volvemos a dejar la base de datos sin el especialista de prueba
			controladorAuxiliar.eliminarUsuario(especialista);			
		} catch (Exception e) {
			fail (e.toString());
		}
	}*/
	
	private void comprobarCamposRestablecidos () {
		assertTrue(txtIdentificacion.getText().equals(""));
		assertTrue(txtNIF.getText().equals(""));
		assertTrue(txtNSS.getText().equals(""));
		assertTrue(txtNombre.getText().equals(""));
		assertTrue(txtApellidos.getText().equals(""));
		assertTrue(txtMedicoAsignado.getText().equals(""));
	}

}
