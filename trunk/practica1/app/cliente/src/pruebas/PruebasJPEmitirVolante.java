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
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.JPEmitirVolante;

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
import excepciones.BeneficiarioYaExistenteException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.UsuarioYaExistenteException;

public class PruebasJPEmitirVolante extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas, pruebas.IConstantes {

	private ControladorCliente controlador;
	private JPEmitirVolante pnlEmitir;
	private Panel panelBeneficiario;
	private Panel pnlPanel;
	private Table tblCitas;
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
	private PeriodoTrabajo periodo1;
	
	private boolean valido;
	private String login;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		valido = false;
		try {
			// Establecemos conexión con el servidor front-end con el rol de administrador
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
			
			// Creamos un médico de cabecera
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
			
			// Creamos el beneficiario en el mismo centro que el médico de cabecera , para que no se le 
			// asigne otro médico diferente 
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
			pnlEmitir = new JPEmitirVolante(controlador.getVentanaPrincipal(), controlador);
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
			// Iniciamos sesión con el rol de administrador para eliminar los datos
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
			controlador.eliminarBeneficiario(beneficiarioPrueba);
			controlador.eliminarUsuario(cabecera);			
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
			// Iniciamos sesión como médico
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
			// Iniciamos sesión como médico
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
			// Iniciamos sesión como médico
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
			especialistas = controlador.obtenerMedicosTipo(tEspecialista);
			// Limpiamos la base de datos de especialistas
			for (Medico m: especialistas) {
				controlador.eliminarUsuario(m);
			}
			
			// Iniciamos sesión como médico
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
			
			// Iniciamos sesión como administrador
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
		valido = false;
		try {
			especialistas = controlador.obtenerMedicosTipo(tEspecialista);
			// Limpiamos la base de datos de especialistas
			for (Medico m: especialistas) {
				controlador.eliminarUsuario(m);
			}
			
			// Insertamos un único especialista
			login = UtilidadesPruebas.generarLogin();
			especialista = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Marta", "PC", "", "", "", tEspecialista);
			periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Miercoles);
			especialista.getCalendario().add(periodo1);
			
			// Mientras exista el usuario, se genera otro login y otro NIF
			do {
				try {
					controlador.crearUsuario(especialista);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					especialista.setNif(UtilidadesPruebas.generarNIF());
					login = UtilidadesPruebas.generarLogin();
					especialista.setLogin(login);
					especialista.setPassword(login);
					valido = false;
				}
			}while(!valido);
			
			// Iniciamos sesión como médico
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
			
			// Iniciamos sesión como administrador
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
		valido = false;
		try {
			especialistas = controlador.obtenerMedicosTipo(tEspecialista);
			// Limpiamos la base de datos de especialistas
			for (Medico m: especialistas) {
				controlador.eliminarUsuario(m);
			}
			
			// Insertamos un único especialista
			login = UtilidadesPruebas.generarLogin();
			especialista = new Medico(UtilidadesPruebas.generarNIF(), login, login, "Marta", "PC", "", "", "", tEspecialista);
			periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Miercoles);
			especialista.getCalendario().add(periodo1);
			
			// Mientras exista el usuario, se genera otro login y otro NIF
			do {
				try {
					controlador.crearUsuario(especialista);
					valido = true;
				} catch (UsuarioYaExistenteException e) {
					especialista.setNif(UtilidadesPruebas.generarNIF());
					login = UtilidadesPruebas.generarLogin();
					especialista.setLogin(login);
					especialista.setPassword(login);
					valido = false;
				}
			}while(!valido);
			
			// Iniciamos sesión como médico
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
			
			// Iniciamos sesión como administrador
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

}
