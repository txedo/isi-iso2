package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Operaciones;
import dominio.control.ControladorCliente;
import excepciones.SesionInvalidaException;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

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
	
	private JPUsuarios jPanelGestionarUsuarios;
	private JPCitas jPanelGestionarCitas;
	private JPBeneficiarios jPanelGestionarBeneficiarios;
	private JPanel jPanelOperaciones;
	private JMenuItem menuitemConfiguracion;
	private JMenuItem menuitemAcercaDe;
	private JSeparator jSeparator2;
	private JMenuItem menuitemCerrarSesion;
	private JMenuItem menuitemIniciarSesion;
	private JMenuItem menuitemSalir;
	private JMenu jMenu4;
	private JMenu jMenu3;
	private JMenu jMenu1;
	private JMenuBar jMenuBar;
	private JPanel jPanelEstablecerSustituto;
	private JPanel jPanelModificarCalendario;
	private JPanel jPanelConsultarMedico;
	private JPBienvenida jPanelBienvenida;
	private JTabbedPane jTabbedPaneOperaciones;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFPrincipal inst = new JFPrincipal();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public JFPrincipal() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setPreferredSize(new java.awt.Dimension(700, 500));
			this.setTitle("SSCA - Unidad de Citación");
			this.setMinimumSize(new java.awt.Dimension(600, 450));
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
						menuitemIniciarSesion = new JMenuItem();
						jMenu1.add(menuitemIniciarSesion);
						menuitemIniciarSesion.setText("Iniciar Sesion...");
					}
					{
						menuitemCerrarSesion = new JMenuItem();
						jMenu1.add(menuitemCerrarSesion);
						menuitemCerrarSesion.setText("Cerrar Sesión");
					}
					{
						jSeparator2 = new JSeparator();
						jMenu1.add(jSeparator2);
					}
					{
						menuitemSalir = new JMenuItem();
						jMenu1.add(menuitemSalir);
						menuitemSalir.setText("Salir");
					}
				}
				{
					jMenu3 = new JMenu();
					jMenuBar.add(jMenu3);
					jMenu3.setText("Opciones");
					{
						menuitemConfiguracion = new JMenuItem();
						jMenu3.add(menuitemConfiguracion);
						menuitemConfiguracion.setText("Configuración...");
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar.add(jMenu4);
					jMenu4.setText("Ayuda");
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
				jPanelOperaciones.setPreferredSize(new java.awt.Dimension(589, 391));
				{
					jTabbedPaneOperaciones = new JTabbedPane();
					jPanelOperaciones.add(jTabbedPaneOperaciones, new AnchorConstraint(1, 979, 962, 20, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jTabbedPaneOperaciones.setPreferredSize(new java.awt.Dimension(578, 292));
					{
						jPanelBienvenida = new JPBienvenida();
						jTabbedPaneOperaciones.addTab("Inicio", null, jPanelBienvenida, null);
					}
					{
						jPanelGestionarBeneficiarios = new JPBeneficiarios();
						jTabbedPaneOperaciones.addTab("Gestionar Beneficiarios", null, jPanelGestionarBeneficiarios, null);
					}
					{
						jPanelGestionarUsuarios = new JPUsuarios();
						jTabbedPaneOperaciones.addTab("Gestionar Usuarios", null, jPanelGestionarUsuarios, null);
					}
					{
						jPanelGestionarCitas = new JPCitas();
						jTabbedPaneOperaciones.addTab("Gestionar Citas", null, jPanelGestionarCitas, null);
					}
					{
						jPanelConsultarMedico = new JPanel();
						AnchorLayout jPanelConsultarMedicoLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Consultar Medico", null, jPanelConsultarMedico, null);
						jPanelConsultarMedico.setLayout(jPanelConsultarMedicoLayout);
					}
					{
						jPanelModificarCalendario = new JPanel();
						AnchorLayout jPanelModificarCalendarioLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Modificar Calendario", null, jPanelModificarCalendario, null);
						jPanelModificarCalendario.setLayout(jPanelModificarCalendarioLayout);
					}
					{
						jPanelEstablecerSustituto = new JPanel();
						AnchorLayout jPanelEstablecerSustitutoLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Establecer Sustituto", null, jPanelEstablecerSustituto, null);
						jPanelEstablecerSustituto.setLayout(jPanelEstablecerSustitutoLayout);
					}
				}
			}
			pack();
			this.setSize(700, 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	public void setControlador(ControladorCliente controlador) {
		this.controlador = controlador;
		jPanelGestionarUsuarios.setControlador(controlador);
		jPanelGestionarBeneficiarios.setControlador(controlador);
	}
	
	public void iniciar() {
		ArrayList<Operaciones> operaciones = null;

		try {
			operaciones = (ArrayList<Operaciones>)controlador.operacionesDisponibles();
			this.configurarInterfaz(operaciones);
		} catch (RemoteException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.toString());
		} catch (SesionInvalidaException e) {
			Dialogos.mostrarDialogoError(this, "Error", "Sesión inválida.");
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
		}
	}
	
	private void configurarInterfaz(ArrayList<Operaciones> operaciones) {
		// Inicializamos las pestañas
		if(!operaciones.contains(Operaciones.RegistrarBeneficiario)
				&& !operaciones.contains(Operaciones.ConsultarBeneficiario)) {
			jTabbedPaneOperaciones.remove(jPanelGestionarBeneficiarios);
		}
		if(!operaciones.contains(Operaciones.TramitarCita)
				&& !operaciones.contains(Operaciones.AnularCita)) {
			jTabbedPaneOperaciones.remove(jPanelGestionarCitas);
		}
		if(!operaciones.contains(Operaciones.CrearUsuario)
				&& !operaciones.contains(Operaciones.ModificarUsuario)
				&& !operaciones.contains(Operaciones.EliminarUsuario)) {
			jTabbedPaneOperaciones.remove(jPanelGestionarUsuarios);
		}
		if(!operaciones.contains(Operaciones.ConsultarMedico)) {
			jTabbedPaneOperaciones.remove(jPanelConsultarMedico);
		}
		if(!operaciones.contains(Operaciones.ModificarCalendario)) {
			jTabbedPaneOperaciones.remove(jPanelModificarCalendario);
		}
		if(!operaciones.contains(Operaciones.EstablecerSustituto)) {
			jTabbedPaneOperaciones.remove(jPanelEstablecerSustituto);
		}
		
		// Inicializamos el contenido de cada pestaña
		if(!operaciones.contains(Operaciones.RegistrarBeneficiario))
			jPanelGestionarBeneficiarios.desactivarRegistrarBeneficiario();
		/*if(!operaciones.contains(Operaciones.ModificarBeneficiario))
			jPanelGestionarBeneficiarios.desactivarModificarBeneficiario();*/
		if(!operaciones.contains(Operaciones.ConsultarBeneficiario))
			jPanelGestionarBeneficiarios.desactivarConsultarBeneficiario();

		if(!operaciones.contains(Operaciones.CrearUsuario))
			jPanelGestionarUsuarios.desactivarCrearUsuario();
		/*if(!operaciones.contains(Operaciones.ModificarUsuario))
			jPanelGestionarUsuarios.desactivarModificarUsuario();
		if(!operaciones.contains(Operaciones.EliminarUsuario))
			jPanelGestionarUsuarios.desactivarEliminarUsuario();*/
		
		if(!operaciones.contains(Operaciones.TramitarCita))
			jPanelGestionarCitas.desactivarTramitarCita();
		if(!operaciones.contains(Operaciones.AnularCita))
			jPanelGestionarCitas.desactivarAnularCita();
		
		// Controlamos si, en los paneles de consultar, se puede o no también modificar/eliminar datos,
		// según los permisos de cada rol
		if (!operaciones.contains(Operaciones.ModificarBeneficiario)
				&& operaciones.contains(Operaciones.ConsultarBeneficiario))
			jPanelGestionarBeneficiarios.desactivarModificacion();
		
		jPanelBienvenida.repaint();
	}
	
	private void thisWindowClosing(WindowEvent evt) {
		try {
			if (controlador != null)
				controlador.cerrarSesion ();
		} catch (RemoteException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.toString());
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(this, "Error", e.toString());
		}
	}

	//$hide<<$
	
}
