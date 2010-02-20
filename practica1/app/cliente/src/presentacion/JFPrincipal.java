package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuarios;
import dominio.control.ControladorCliente;
import excepciones.SesionInvalidaException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import presentacion.auxiliares.OperacionCambiadaEvent;
import presentacion.auxiliares.OperacionCambiadaListener;
import presentacion.auxiliares.OperacionesInterfaz;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * Ventana principal del cliente, en la que se muestran todos los
 * paneles con las operaciones disponibles.
 */
public class JFPrincipal extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 1155264551580232934L;

	private ControladorCliente controlador;
	private OperacionesInterfaz operacionSeleccionada;
	
	private JPBeneficiarios jPanelGestionarBeneficiarios;
	private JPUsuarios jPanelGestionarUsuarios;
	private JPCitas jPanelGestionarCitas;
	private JPSustituciones jPanelGestionarSustituciones;
	private JPVolantes jPanelGestionarVolantes;
	private JPanel jPanelOperaciones;
	private JMenuItem menuitemAcercaDe;
	private JSeparator jSeparator2;
	private JMenuItem menuitemCerrarSesion;
	private JMenuItem menuitemSalir;
	private JMenu jMenu4;
	private JMenu jMenu1;
	private JMenuBar jMenuBar;
	private JLabel lblPuertoEscucha;
	private JButton btnCerrarSesion;
	private JButton btnCerrarAplicacion;
	private JLabel lblBarraEstado;
	private JSeparator jSeparator1;
	private JMenuItem menuitemContenidoAyuda;
	private JPBienvenida jPanelBienvenida;
	private JTabbedPane jTabbedPaneOperaciones;

	public JFPrincipal(ControladorCliente controlador) {
		super();
		this.controlador = controlador;
		operacionSeleccionada = OperacionesInterfaz.OperacionInvalida;
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setPreferredSize(new java.awt.Dimension(850, 700));
			this.setTitle("SSCA - Unidad de Citación");
			this.setMinimumSize(new java.awt.Dimension(600, 450));
			setLocationRelativeTo(null);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					thisWindowClosing(evt);
				}
			});
			{
				jMenuBar = new JMenuBar();
				setJMenuBar(jMenuBar);
				{
					jMenu1 = new JMenu();
					jMenuBar.add(jMenu1);
					jMenu1.setText("Archivo");
					{
						menuitemCerrarSesion = new JMenuItem();
						jMenu1.add(menuitemCerrarSesion);
						menuitemCerrarSesion.setText("Cerrar sesión");
						menuitemCerrarSesion.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuitemCerrarSesionActionPerformed(evt);
							}
						});
					}
					{
						jSeparator2 = new JSeparator();
						jMenu1.add(jSeparator2);
					}
					{
						menuitemSalir = new JMenuItem();
						jMenu1.add(menuitemSalir);
						menuitemSalir.setText("Salir");
						menuitemSalir.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuitemSalirActionPerformed(evt);
							}
						});
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar.add(jMenu4);
					jMenu4.setText("Ayuda");
					{
						menuitemContenidoAyuda = new JMenuItem();
						jMenu4.add(menuitemContenidoAyuda);
						menuitemContenidoAyuda.setText("Contenido de la ayuda");
					}
					{
						jSeparator1 = new JSeparator();
						jMenu4.add(jSeparator1);
					}
					{
						menuitemAcercaDe = new JMenuItem();
						jMenu4.add(menuitemAcercaDe);
						menuitemAcercaDe.setText("Acerca de...");
					}
				}
			}
			{
				jPanelOperaciones = new JPanel();
				AnchorLayout jPanelOperacionesLayout = new AnchorLayout();
				getContentPane().add(jPanelOperaciones, BorderLayout.CENTER);
				jPanelOperaciones.setLayout(jPanelOperacionesLayout);
				jPanelOperaciones.setPreferredSize(new java.awt.Dimension(692, 479));
				{
					lblPuertoEscucha = new JLabel();
					jPanelOperaciones.add(lblPuertoEscucha, new AnchorConstraint(961, 567, 4, 12, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblPuertoEscucha.setPreferredSize(new java.awt.Dimension(382, 17));
				}
				{
					btnCerrarSesion = new JButton();
					jPanelOperaciones.add(btnCerrarSesion, new AnchorConstraint(930, 160, 11, 590, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnCerrarSesion.setText("Cerrar sesión");
					btnCerrarSesion.setPreferredSize(new java.awt.Dimension(124, 22));
					btnCerrarSesion.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCerrarSesionActionPerformed(evt);
						}
					});
				}
				{
					btnCerrarAplicacion = new JButton();
					jPanelOperaciones.add(btnCerrarAplicacion, new AnchorConstraint(930, 16, 11, 799, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnCerrarAplicacion.setText("Cerrar aplicación");
					btnCerrarAplicacion.setPreferredSize(new java.awt.Dimension(123, 22));
					btnCerrarAplicacion.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCerrarAplicacionActionPerformed(evt);
						}
					});
				}
				{
					lblBarraEstado = new JLabel();
					jPanelOperaciones.add(lblBarraEstado, new AnchorConstraint(928, 568, 21, 12, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblBarraEstado.setPreferredSize(new java.awt.Dimension(383, 17));
				}
				{
					jTabbedPaneOperaciones = new JTabbedPane();
					jPanelOperaciones.add(jTabbedPaneOperaciones, new AnchorConstraint(8, 16, 46, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					jTabbedPaneOperaciones.setPreferredSize(new java.awt.Dimension(656, 387));
					jTabbedPaneOperaciones.setFont(new java.awt.Font("Tahoma",0,12));
					jTabbedPaneOperaciones.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent evt) {
							jTabbedPaneOperacionesStateChanged(evt);
						}
					});
					{
						jPanelBienvenida = new JPBienvenida();
						jTabbedPaneOperaciones.addTab("Inicio", null, jPanelBienvenida, null);
					}
					{
						jPanelGestionarBeneficiarios = new JPBeneficiarios(this, controlador);
						jTabbedPaneOperaciones.addTab("Gestionar Beneficiarios", null, jPanelGestionarBeneficiarios, null);
						jPanelGestionarBeneficiarios.addOperacionCambiadaListener(new OperacionCambiadaListener() {
							public void operacionCambiada(OperacionCambiadaEvent evt) {
								jPanelGestionarBeneficiariosOperacionCambiada(evt);
							}
						});
					}
					{
						jPanelGestionarUsuarios = new JPUsuarios(this, controlador);
						jTabbedPaneOperaciones.addTab("Gestionar Usuarios", null, jPanelGestionarUsuarios, null);
						jPanelGestionarUsuarios.addOperacionCambiadaListener(new OperacionCambiadaListener() {
							public void operacionCambiada(OperacionCambiadaEvent evt) {
								jPanelGestionarUsuariosOperacionCambiada(evt);
							}
						});
					}
					{
						jPanelGestionarCitas = new JPCitas(this, controlador);
						jTabbedPaneOperaciones.addTab("Gestionar Citas", null, jPanelGestionarCitas, null);
						jPanelGestionarCitas.addOperacionCambiadaListener(new OperacionCambiadaListener() {
							public void operacionCambiada(OperacionCambiadaEvent evt) {
								jPanelGestionarCitasOperacionCambiada(evt);
							}
						});
					}
					{
						jPanelGestionarSustituciones = new JPSustituciones();
						jTabbedPaneOperaciones.addTab("Gestionar Sustituciones", null, jPanelGestionarSustituciones, null);
						jPanelGestionarSustituciones.addOperacionCambiadaListener(new OperacionCambiadaListener() {
							public void operacionCambiada(OperacionCambiadaEvent evt) {
								jPanelGestionarSustitucionesOperacionCambiada(evt);
							}
						});
					}
					{
						jPanelGestionarVolantes = new JPVolantes(this, controlador);
						jTabbedPaneOperaciones.addTab("Gestionar Volantes", null, jPanelGestionarVolantes, null);
						jPanelGestionarVolantes.addOperacionCambiadaListener(new OperacionCambiadaListener() {
							public void operacionCambiada(OperacionCambiadaEvent evt) {
								jPanelGestionarVolantesOperacionCambiada(evt);
							}
						});
					}
				}
			}
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void btnCerrarSesionActionPerformed(ActionEvent evt) {
		confirmarCerrarSesion();
	}
	
	private void btnCerrarAplicacionActionPerformed(ActionEvent evt) {
		cerrarAplicacion();
	}
	
	private void menuitemCerrarSesionActionPerformed(ActionEvent evt) {
		confirmarCerrarSesion();
	}
	
	private void menuitemSalirActionPerformed(ActionEvent evt) {
		cerrarAplicacion();
	}

	private void thisWindowClosing(WindowEvent evt) {
		cerrarAplicacion();
	}
	
	private void jTabbedPaneOperacionesStateChanged(ChangeEvent evt) {
		Component comp;
		
		comp = jTabbedPaneOperaciones.getSelectedComponent();
		if(comp == jPanelGestionarBeneficiarios) {
			operacionSeleccionada = jPanelGestionarBeneficiarios.getOperacionSeleccionada();
		} else if(comp == jPanelGestionarUsuarios) {
			operacionSeleccionada = jPanelGestionarUsuarios.getOperacionSeleccionada();
		} else if(comp == jPanelGestionarCitas) {
			operacionSeleccionada = jPanelGestionarCitas.getOperacionSeleccionada();
		} else if(comp == jPanelGestionarSustituciones) {
			operacionSeleccionada = jPanelGestionarSustituciones.getOperacionSeleccionada();
		} else if(comp == jPanelGestionarVolantes) {
			operacionSeleccionada = jPanelGestionarVolantes.getOperacionSeleccionada();
		} else {
			operacionSeleccionada = OperacionesInterfaz.OperacionInvalida;
		}
		restablecerPaneles();
	}

	private void jPanelGestionarBeneficiariosOperacionCambiada(OperacionCambiadaEvent evt) {
		operacionSeleccionada = evt.getOperacion();
		restablecerPaneles();
	}
	
	private void jPanelGestionarUsuariosOperacionCambiada(OperacionCambiadaEvent evt) {
		operacionSeleccionada = evt.getOperacion();
		restablecerPaneles();
	}

	private void jPanelGestionarCitasOperacionCambiada(OperacionCambiadaEvent evt) {
		operacionSeleccionada = evt.getOperacion();
		restablecerPaneles();
	}
	
	private void jPanelGestionarSustitucionesOperacionCambiada(OperacionCambiadaEvent evt) {
		operacionSeleccionada = evt.getOperacion();
		restablecerPaneles();
	}
	
	private void jPanelGestionarVolantesOperacionCambiada(OperacionCambiadaEvent evt) {
		operacionSeleccionada = evt.getOperacion();
		restablecerPaneles();
	}
	
	private void restablecerPaneles() {
		if(jPanelGestionarBeneficiarios != null) {
			jPanelGestionarBeneficiarios.restablecerPaneles();
		}
		if(jPanelGestionarUsuarios != null) {
			jPanelGestionarUsuarios.restablecerPaneles();
		}
		if(jPanelGestionarCitas != null) {
			jPanelGestionarCitas.restablecerPaneles();
		}
		if(jPanelGestionarSustituciones != null) {
			jPanelGestionarSustituciones.restablecerPaneles();
		}
		if(jPanelGestionarVolantes != null) {
			jPanelGestionarVolantes.restablecerPaneles();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void iniciar() {
		Vector<Operaciones> operaciones = null;

		try {
			operaciones = controlador.operacionesDisponibles();
			this.configurarInterfaz(operaciones);
		} catch (RemoteException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
		} catch (SesionInvalidaException e) {
			Dialogos.mostrarDialogoError(this, "Error", "Sesión inválida.");
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
		}
	}
	
	private void configurarInterfaz(Vector<Operaciones> operaciones) {
		// Inicializamos la barra de estado
		lblBarraEstado.setText("Sesión iniciada: " + controlador.getUsuarioAutenticado() + "@" + RolesUsuarios.values()[(int)controlador.getSesion().getRol()]);
		lblPuertoEscucha.setText("Puerto de escucha: " + controlador.getPuertoEscucha());

		// Inicializamos los paneles de gestión de beneficiarios
		if(!operaciones.contains(Operaciones.ConsultarBeneficiario)) {
			jPanelGestionarBeneficiarios.desactivarConsultarBeneficiario();
			jPanelGestionarBeneficiarios.desactivarConsultarModificarBeneficiario();
		}
		if(!operaciones.contains(Operaciones.RegistrarBeneficiario)) {
			jPanelGestionarBeneficiarios.desactivarRegistrarBeneficiario();
		}
		if(!operaciones.contains(Operaciones.ModificarBeneficiario)) {
			jPanelGestionarBeneficiarios.desactivarConsultarModificarBeneficiario();
		}
		if(!operaciones.contains(Operaciones.EliminarBeneficiario)) {
			jPanelGestionarBeneficiarios.desactivarConsultarModificarBeneficiario();
		}
		if(operaciones.contains(Operaciones.ConsultarBeneficiario)
		 && operaciones.contains(Operaciones.ModificarBeneficiario)
		 && operaciones.contains(Operaciones.EliminarBeneficiario)) {
			jPanelGestionarBeneficiarios.desactivarConsultarBeneficiario();
		}
		
		// Inicializamos los paneles de gestión de usuarios
		if(!operaciones.contains(Operaciones.ConsultarUsuario)) {
			jPanelGestionarUsuarios.desactivarConsultarUsuario();
			jPanelGestionarUsuarios.desactivarConsultarModificarUsuario();
		}
		if(!operaciones.contains(Operaciones.RegistrarUsuario)) {
			jPanelGestionarUsuarios.desactivarCrearUsuario();
		}
		if(!operaciones.contains(Operaciones.ModificarUsuario)) {
			jPanelGestionarUsuarios.desactivarConsultarModificarUsuario();
		}
		if(!operaciones.contains(Operaciones.EliminarUsuario)) {
			jPanelGestionarUsuarios.desactivarConsultarModificarUsuario();
		}
		if(operaciones.contains(Operaciones.ConsultarUsuario)
		 && operaciones.contains(Operaciones.ModificarUsuario)
		 && operaciones.contains(Operaciones.EliminarUsuario)) {
			jPanelGestionarUsuarios.desactivarConsultarUsuario();
		}
		
		// Inicializamos los paneles de gestión de citas
		if(!operaciones.contains(Operaciones.ConsultarCitas)) {
			jPanelGestionarCitas.desactivarConsultarAnularCita();
		}
		if(!operaciones.contains(Operaciones.TramitarCita)) {
			jPanelGestionarCitas.desactivarTramitarCita();
		}
		if(!operaciones.contains(Operaciones.TramitarCitaVolante)) {
			jPanelGestionarCitas.desactivarTramitarCitaVolante();
		}
		
		// Inicializamos los paneles de gestión de sustituciones
		if(!operaciones.contains(Operaciones.EstablecerSustituto)) {
			jPanelGestionarSustituciones.desactivarEstablecerSustituto();
		}
		
		// Inicializamos los paneles de gestión de volantes
		if(!operaciones.contains(Operaciones.EmitirVolante)) {
			jPanelGestionarVolantes.desactivarEmitirVolante();
		}
		
		// Quitamos las pestañas que se han quedado sin operaciones
		if(!jPanelGestionarBeneficiarios.hayOperacionesDisponibles()) {
			jTabbedPaneOperaciones.remove(jPanelGestionarBeneficiarios);
		}
		if(!jPanelGestionarUsuarios.hayOperacionesDisponibles()) {
			jTabbedPaneOperaciones.remove(jPanelGestionarUsuarios);
		}
		if(!jPanelGestionarCitas.hayOperacionesDisponibles()) {
			jTabbedPaneOperaciones.remove(jPanelGestionarCitas);
		}
		if(!jPanelGestionarSustituciones.hayOperacionesDisponibles()) {
			jTabbedPaneOperaciones.remove(jPanelGestionarSustituciones);
		}
		if(!jPanelGestionarVolantes.hayOperacionesDisponibles()) {
			jTabbedPaneOperaciones.remove(jPanelGestionarVolantes);
		}
		
		// Refrescamos el panel inicial de bienvenida
		jPanelBienvenida.repaint();
	}
	
	private void cerrarAplicacion() {
		if (Dialogos.mostrarDialogoPregunta(null, "Confirmar cierre de la aplicación", "¿Está seguro de querer cerrar la aplicación? En caso afirmativo, la sesión finalizará automáticamente.")) {
			cerrarSesion();
			try {
				controlador.cerrarControlador();
			} catch (RemoteException e) {
				Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
			} catch (MalformedURLException e) {
				Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
			} catch (NotBoundException e) {
				Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
			}
		}
	}
	
	private void confirmarCerrarSesion () {
		if (Dialogos.mostrarDialogoPregunta(null, "Confirmar cierre de sesión", "¿Está seguro de querer cerrar la sesión?")) {
			cerrarSesion();
			controlador.identificarse();
		}
	}

	private void cerrarSesion () {
		try {
			if (controlador != null) {
				controlador.cerrarSesion ();
				lblBarraEstado.setText("La sesión ha finalizado con éxito.");
				lblPuertoEscucha.setText("");
			}
		} catch (RemoteException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getLocalizedMessage());
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getLocalizedMessage());
		}
	}
	
	// Métodos públicos
	
	public void forzarCierreSesionDuplicada() {
		Dialogos.mostrarDialogoAdvertencia(this, "Aviso", "Se ha iniciado una sesión con el mismo nombre de usuario en otro equipo.\nEsta sesión se cerrará automáticamente.");
		controlador.identificarse();
	}
	
	public void forzarCierreServidorDesconectado() {
		Dialogos.mostrarDialogoAdvertencia(this, "Aviso", "Se ha perdido la conexión con el servidor principal.\nEsta sesión se cerrará automáticamente.");
		controlador.identificarse();
	}

	public void beneficiarioActualizado(Beneficiario beneficiario) {
		// Redirigimos la operación al grupo de paneles seleccionado
		switch(operacionSeleccionada) {
		case ConsultarBeneficiario:
		case ConsultarModificarBeneficiario:
		case RegistrarBeneficiario:
			jPanelGestionarBeneficiarios.beneficiarioActualizado(beneficiario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}

	public void beneficiarioEliminado(Beneficiario beneficiario) {
		// Redirigimos la operación al grupo de paneles seleccionado
		switch(operacionSeleccionada) {
		case ConsultarBeneficiario:
		case ConsultarModificarBeneficiario:
		case RegistrarBeneficiario:
			jPanelGestionarBeneficiarios.beneficiarioEliminado(beneficiario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void citaRegistrada(Cita cita) {
		// Redirigimos la operación al grupo de paneles seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelGestionarCitas.citaRegistrada(cita);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	//$hide<<$
	
}
