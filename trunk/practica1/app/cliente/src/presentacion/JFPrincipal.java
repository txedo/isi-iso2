package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Roles;
import dominio.control.ControladorCliente;
import excepciones.SesionInvalidaException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


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
	
	private JPUsuarios jPanelGestionarUsuarios;
	private JPCitas jPanelGestionarCitas;
	private JPBeneficiarios jPanelGestionarBeneficiarios;
	private JPanel jPanelOperaciones;
	private JMenuItem menuitemAcercaDe;
	private JSeparator jSeparator2;
	private JMenuItem menuitemCerrarSesion;
	private JMenuItem menuitemSalir;
	private JMenu jMenu4;
	private JMenu jMenu1;
	private JMenuBar jMenuBar;
	private JPanel jPanelEstablecerSustituto;
	private JPCalendarioConsultar jPanelConsultarCalendario;
	private JButton btnCerrarSesion;
	private JButton btnCerrarAplicacion;
	private JLabel lblBarraEstado;
	private JSeparator jSeparator1;
	private JMenuItem menuitemContenidoAyuda;
	private JPEmitirVolante jPanelEmitirVolante;
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
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setPreferredSize(new java.awt.Dimension(700, 500));
			this.setTitle("SSCA - Unidad de Citaci�n");
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
						menuitemCerrarSesion = new JMenuItem();
						jMenu1.add(menuitemCerrarSesion);
						menuitemCerrarSesion.setText("Cerrar Sesi�n");
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
					btnCerrarSesion = new JButton();
					jPanelOperaciones.add(btnCerrarSesion, new AnchorConstraint(930, 160, 11, 590, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnCerrarSesion.setText("Cerrar sesi�n");
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
					btnCerrarAplicacion.setText("Cerrar aplicaci�n");
					btnCerrarAplicacion.setPreferredSize(new java.awt.Dimension(123, 22));
					btnCerrarAplicacion.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCerrarAplicacionActionPerformed(evt);
						}
					});
				}
				{
					lblBarraEstado = new JLabel();
					jPanelOperaciones.add(lblBarraEstado, new AnchorConstraint(942, 571, 986, 16, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					lblBarraEstado.setPreferredSize(new java.awt.Dimension(384, 21));
				}
				{
					jTabbedPaneOperaciones = new JTabbedPane();
					jPanelOperaciones.add(jTabbedPaneOperaciones, new AnchorConstraint(1, 979, 903, 19, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jTabbedPaneOperaciones.setPreferredSize(new java.awt.Dimension(664, 434));
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
						jPanelConsultarCalendario = new JPCalendarioConsultar();
						jTabbedPaneOperaciones.addTab("Consultar Calendario", null, jPanelConsultarCalendario, null);
					}
					{
						jPanelEstablecerSustituto = new JPanel();
						AnchorLayout jPanelEstablecerSustitutoLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Establecer Sustituto", null, jPanelEstablecerSustituto, null);
						jPanelEstablecerSustituto.setLayout(jPanelEstablecerSustitutoLayout);
					}
					
					{
						jPanelEmitirVolante = new JPEmitirVolante();
						jTabbedPaneOperaciones.addTab("Emitir Volante", null, jPanelEmitirVolante, null);
					}
				}
			}
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnCerrarSesionActionPerformed(ActionEvent evt) {
		if (Dialogos.mostrarDialogoPregunta(null, "Confirmar cierre de sesi�n", "�Est� seguro de querer cerrar la sesi�n?")) {
			cerrarSesion();
			controlador.identificarse();
		}
	}
	
	private void btnCerrarAplicacionActionPerformed(ActionEvent evt) {
		thisWindowClosing(null);
	}

	//$hide>>$
	
	public void setControlador(ControladorCliente controlador) {
		this.controlador = controlador;
		jPanelGestionarUsuarios.setControlador(controlador);
		jPanelGestionarBeneficiarios.setControlador(controlador);
		jPanelGestionarCitas.setControlador(controlador);
		jPanelConsultarCalendario.setControlador(controlador);
		jPanelEmitirVolante.setControlador(controlador);
		if (controlador.getSesion().getRol() == Roles.Medico.ordinal())
			jPanelEmitirVolante.inicializarEspecialistas();
	}
	
	@SuppressWarnings("unchecked")
	public void iniciar() {
		ArrayList<Operaciones> operaciones = null;

		try {
			operaciones = (ArrayList<Operaciones>)controlador.operacionesDisponibles();
			this.configurarInterfaz(operaciones);
		} catch (RemoteException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
		} catch (SesionInvalidaException e) {
			Dialogos.mostrarDialogoError(this, "Error", "Sesi�n inv�lida.");
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
		}
	}
	
	private void configurarInterfaz(ArrayList<Operaciones> operaciones) {
		// Inicializamos la barra de estado
		lblBarraEstado.setText("Sesi�n iniciada: " + controlador.getUsuarioAutenticado() + "@" + Roles.values()[(int)controlador.getSesion().getRol()]);
		// Inicializamos las pesta�as
		if(!operaciones.contains(Operaciones.RegistrarBeneficiario)
				&& !operaciones.contains(Operaciones.ConsultarBeneficiario)) {
			jTabbedPaneOperaciones.remove(jPanelGestionarBeneficiarios);
		}
		if(!operaciones.contains(Operaciones.TramitarCita)
				&& !operaciones.contains(Operaciones.ConsultarCitas)){
			jTabbedPaneOperaciones.remove(jPanelGestionarCitas);
		}
		
		if (!operaciones.contains(Operaciones.EmitirVolante))
			jTabbedPaneOperaciones.remove(jPanelEmitirVolante);
		
		if(!operaciones.contains(Operaciones.CrearUsuario)
				&& !operaciones.contains(Operaciones.ConsultarUsuario)) {
			jTabbedPaneOperaciones.remove(jPanelGestionarUsuarios);
		}
		if(!operaciones.contains(Operaciones.ConsultarMedico)) {
			//TODO Permitir ver el calendario del m�dico
		}
		if(!operaciones.contains(Operaciones.ModificarCalendario)) {
			jTabbedPaneOperaciones.remove(jPanelConsultarCalendario);
		}
		if(!operaciones.contains(Operaciones.EstablecerSustituto)) {
			jTabbedPaneOperaciones.remove(jPanelEstablecerSustituto);
		}

		// Inicializamos el contenido de cada pesta�a
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
		if(!operaciones.contains(Operaciones.ConsultarUsuario))
			jPanelGestionarUsuarios.desactivarConsultarUsuario();
		
		if(!operaciones.contains(Operaciones.TramitarCita))
			jPanelGestionarCitas.desactivarTramitarCita();
		if(!operaciones.contains(Operaciones.ConsultarCitas))
			jPanelGestionarCitas.desactivarConsultarCita();
		
		// Controlamos si, en los paneles de consultar, se puede o no tambi�n modificar/eliminar datos,
		// seg�n los permisos de cada rol
		if (!operaciones.contains(Operaciones.ModificarBeneficiario)
				&& operaciones.contains(Operaciones.ConsultarBeneficiario))
			jPanelGestionarBeneficiarios.desactivarModificacion();
		
		jPanelBienvenida.repaint();
	}
	
	private void thisWindowClosing(WindowEvent evt) {
		if (Dialogos.mostrarDialogoPregunta(null, "Confirmar cierre de la aplicaci�n", "�Est� seguro de querer cerrar la aplicaci�n? En caso afirmativo, la sesi�n finalizar� autom�ticamente.")) {
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
	
	private void cerrarSesion () {
		try {
			if (controlador != null)
				controlador.cerrarSesion ();
				lblBarraEstado.setText("La sesi�n ha finalizado con �xito.");
		} catch (RemoteException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getLocalizedMessage());
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getLocalizedMessage());
		}
	}

	//$hide<<$
	
}
