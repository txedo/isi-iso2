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
	
	private Medico cabecera, especialista;
	private TipoMedico tCabecera, tEspecialista;
	private Beneficiario beneficiarioPrueba;
	
	private boolean beneficiarioEliminado, medicoEliminado;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		beneficiarioEliminado = false;
		medicoEliminado = false;
		try {
			// Establecemos conexi�n con el servidor front-end con el rol de administrador
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
						
			tEspecialista = new Especialista("Neurologia");
			
			// Creamos un m�dico de cabecera 
			tCabecera = new Cabecera();
			cabecera = new Medico();
			cabecera.setNombre("Eduardo");
			cabecera.setApellidos("Ram�rez Garc�a");
			cabecera.setTipoMedico(tCabecera);
			cabecera.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			cabecera = (Medico)UtilidadesPruebas.crearUsuario(controlador, cabecera); 

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
			beneficiarioPrueba = UtilidadesPruebas.crearBeneficiario(controlador, beneficiarioPrueba);
			
			// Obtenemos el panel, iniciando sesi�n como m�dico
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
			jtxtIdentificacion = (JTextField)txtIdentificacion.getAwtComponent();
			jtxtNIF = (JTextField)txtNIF.getAwtComponent();
			jtxtNSS = (JTextField)txtNSS.getAwtComponent();
			jtxtNombre = (JTextField)txtNombre.getAwtComponent();
			jtxtApellidos = (JTextField)txtApellidos.getAwtComponent();
			jlstEspecialistas = (JList)lstEspecialistas.getAwtComponent();
			jcmbEspecialidad = (JComboBox)cmbEspecialidad.getAwtComponent();

		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesi�n auxiliar de las pruebas del observador
			UtilidadesPruebas.cerrarControladorAuxiliar();
			// Iniciamos sesi�n con el rol de administrador para eliminar los datos
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
			if (!beneficiarioEliminado) controlador.eliminarBeneficiario(beneficiarioPrueba);
			if (!medicoEliminado) controlador.eliminarUsuario(cabecera);			
			// Cerramos la sesi�n y la ventana del controlador
			controlador.getVentanaPrincipal().dispose();
			controlador.cerrarSesion();
		} catch(Exception e) {
			fail(e.toString());
		}
		winPrincipal.dispose();
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
	
	public void testEspecialistasVacios() {
		Vector<Medico> especialistas;
		try {
			especialistas = controlador.obtenerMedicosTipo(tEspecialista);
			// Limpiamos la base de datos de especialistas
			// Volvemos a la sesi�n del administrador
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
			for (Medico m: especialistas) {
				controlador.eliminarUsuario(m);
			}
			
			// Iniciamos sesi�n como m�dico
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
			// Buscamos un beneficiario
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// Al seleccionar una especialidad, debe haber 0 especialistas (s�lo existe la cadena vac�a)
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedIndex(2);
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), "");
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedIndex(0);
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), "");
			// El bot�n de emitir debe estar deshabilitado
			assertFalse(btnEmitir.isEnabled());
			
			// Iniciamos sesi�n como administrador
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
			// Volvemos a dejar la base de datos en su estado
			for (Medico m: especialistas) {
				controlador.crearUsuario(m);
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testEmitirVolanteIncorrecto() {
		Vector<Medico> especialistas;
		try {
			especialistas = controlador.obtenerMedicosTipo(tEspecialista);
			// Limpiamos la base de datos de especialistas
			// Volvemos a la sesi�n del administrador
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
			for (Medico m: especialistas) {
				controlador.eliminarUsuario(m);
			}
			
			// Insertamos un �nico especialista
			tEspecialista = new Especialista("Neurologia");
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(tEspecialista);
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controlador, especialista); 
			
			// Iniciamos sesi�n como m�dico
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
			// Buscamos un beneficiario
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La lista de especialista s�lo debe tenr uno al seleccionar la especialidad Neurologia
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Neurologia");
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")");
			// El bot�n de emitir debe estar habilitado
			assertTrue(btnEmitir.isEnabled());
			// No se puede emitir volante sin seleccionar especialista
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnEmitir, OK_OPTION), "Debe seleccionar un especialista");
			
			// Iniciamos sesi�n como administrador
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
			// Volvemos a dejar la base de datos en su estado
			for (Medico m: especialistas) {
				controlador.crearUsuario(m);
			}
			
			controlador.eliminarUsuario(especialista);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}	
	
	public void testEmitirVolante() {
		Vector<Medico> especialistas;
		
		try {
			especialistas = controlador.obtenerMedicosTipo(tEspecialista);
			// Limpiamos la base de datos de especialistas
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
			for (Medico m: especialistas) {
				controlador.eliminarUsuario(m);
			}
			
			// Creamos el m�dico especialista que ser� el receptor de los volantes
			tEspecialista = new Especialista("Neurologia");
			especialista = new Medico();
			especialista.setNombre("Juan");
			especialista.setApellidos("Especialista");
			especialista.setTipoMedico(tEspecialista);
			especialista.getCalendario().add(new PeriodoTrabajo(10, 16, DiaSemana.Miercoles));
			especialista = (Medico)UtilidadesPruebas.crearUsuario(controlador, especialista); 
			
			// Iniciamos sesi�n como m�dico
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
			// Buscamos un beneficiario
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			// La lista de especialista s�lo debe tenr uno al seleccionar la especialidad Neurologia
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Neurologia");
			assertEquals(jlstEspecialistas.getModel().getSize(), 1);
			assertEquals(jlstEspecialistas.getModel().getElementAt(0), especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")");
			// Seleccionamos el especialsita
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			// El bot�n de emitir debe estar habilitado
			assertTrue(btnEmitir.isEnabled());
			// Emitimos el volante
			assertTrue(UtilidadesPruebas.obtenerTextoDialogo(btnEmitir, OK_OPTION).contains("El volante del beneficiario se ha emitido correctamente"));
			
			// Iniciamos sesi�n como administrador
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
			// Volvemos a dejar la base de datos en su estado
			for (Medico m: especialistas) {
				controlador.crearUsuario(m);
			}
			
			controlador.eliminarUsuario(especialista);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testObservadorBeneficiarioActualizadoEliminado () {
		// Iniciamos sesi�n con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.usuarioAdminAuxiliar, IDatosConexionPruebas.passwordAdminAuxiliar);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {			
			// El m�dico busca al beneficiario de prueba
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar, OK_OPTION), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			
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
			Thread.sleep(500);
			// La ventana del primer administrador se ha debido limpiar
			comprobarCamposRestablecidos();
			beneficiarioEliminado = true;
		} catch (Exception e) {
			fail (e.toString());
		}
	}
	
	public void testObservadorUsuarioActualizadoEliminado () {
		// Iniciamos sesi�n con un segundo administrador
		try {
			// Iniciamos el controlador auxiliar con otro usuario administrador
			controladorAuxiliar = UtilidadesPruebas.crearControladorAuxiliar(IDatosConexionPruebas.usuarioAdminAuxiliar, IDatosConexionPruebas.passwordAdminAuxiliar);
		} catch(Exception e) {
			fail(e.toString());
		}
		try{	
			
			// El m�dico busca al beneficiario de prueba
			jcmbIdentificacion.grabFocus();
			jcmbIdentificacion.setSelectedIndex(0);
			txtIdentificacion.setText(beneficiarioPrueba.getNif());
			assertEquals(UtilidadesPruebas.obtenerTextoDialogo(btnBuscar), "Beneficiario encontrado.");
			// Se comprueba que tiene m�dico asignado
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			jcmbEspecialidad.grabFocus();
			jcmbEspecialidad.setSelectedItem("Neurologia");
			jlstEspecialistas.grabFocus();
			jlstEspecialistas.setSelectedIndex(0);
			
			// En este momento el segundo administrador modifica el m�dico asignado al beneficiario
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					cabecera.setNombre("Otro nombre");
					controladorAuxiliar.modificarMedico(cabecera);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio del m�dico
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(500);
			// La ventana del primer administrador se ha debido actualizar con el nuevo nombre de medico de cabecera
			assertEquals(cabecera.getApellidos() + ", " + cabecera.getNombre() + " (" + cabecera.getNif() + ")", txtMedicoAsignado.getText());
			
			// En este momento el segundo administrador modifica el especialista
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					especialista.setApellidos("Otros apellidos");
					controladorAuxiliar.modificarMedico(especialista);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa del cambio del m�dico
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(500);
			// La ventana del m�dico se ha debido actualizar con los nuevos apellidos del especialista
			assertEquals(especialista.getApellidos() + ", " + especialista.getNombre() + " (" + especialista.getNif() + ")", jlstEspecialistas.getSelectedValue().toString());
		
			// Ahora procedemos a eliminar el medico desde el segundo administrador
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(cabecera);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminaci�n del m�dico
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(500);
			// La ventana del m�dico se ha debido actualizar borrando los campos, pues el beneficiario se ha quedado sin m�dico asignado
			comprobarCamposRestablecidos();
			medicoEliminado = true;
			
			// Ahora procedemos a eliminar el especialista
			WindowInterceptor.init(new Trigger() {
				public void run() throws Exception {
					controladorAuxiliar.eliminarMedico(especialista);
				}
			}).process(new WindowHandler() {
				public Trigger process(Window window) {
					// Capturamos la ventana que avisa de la eliminaci�n del m�dico
					return window.getButton(OK_OPTION).triggerClick();
				}
			}).run();
			// Dormimos el hilo en espera de la respuesta del servidor
			Thread.sleep(500);
			// La ventana del m�dico se ha debido actualizar, no mostrando ning�n especialista
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