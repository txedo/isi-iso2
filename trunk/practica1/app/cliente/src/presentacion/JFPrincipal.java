package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.Beneficiario;
import dominio.ControladorLogin;
import dominio.Medico;
import dominio.Operaciones;
import excepciones.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DebugGraphics;
import javax.swing.DefaultComboBoxModel;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
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
public class JFPrincipal extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private ControladorLogin controlador;
	private DefaultListModel jListOperacionesBeneficiariosModel;
	private DefaultListModel jListOperacionesUsuariosModel;
	private DefaultListModel jListOperacionesCitasModel;
	
	private JPanel jPanelOperaciones;
	private JPanel jPanelRegistrarBeneficiario;
	private JPanel jPanelTramitarCita;
	private JButton btnRestablecerRB;
	private JButton btnCrearBeneficiarioRB;
	private JTextField txtTelefonoMovilRB;
	private JTextField txtTelefonoFijoRB;
	private JTextField txtPisoRB;
	private JLabel jLabel24;
	private JTextField txtMedicoAsignadoCB;
	private JLabel jLabel23;
	private JLabel jLabel22;
	private JLabel jLabel21;
	private JLabel jLabel20;
	private JLabel jLabel19;
	private JLabel jLabel18;
	private JLabel jLabel17;
	private JLabel jLabel16;
	private JTextField txtTelefonoMovilCB;
	private JTextField txtTelefonoFijoCB;
	private JList jListTipoUsuario;
	private JButton btnRestablecerCU;
	private JButton btnCrearUsuarioCU;
	private JLabel jLabel29;
	private JPasswordField txtPasswordCU;
	private JList jListOperacionesCitas;
	private JLabel jLabel31;
	private JPanel jPanelListaOperacionesGestionarCitas;
	private JSeparator jSeparator4;
	private JLabel jLabel30;
	private JLabel jLabel28;
	private JLabel jLabel27;
	private JLabel jLabel26;
	private JLabel jLabel25;
	private JLabel jLabel14;
	private JTextField txtApellidosCU;
	private JTextField txtNombreCU;
	private JPasswordField txtPassword2CU;
	private JTextField txtLoginCU;
	private JTextField txtDNICU;
	private JList jListOperacionesUsuarios;
	private JLabel jLabel9;
	private JSeparator jSeparator3;
	private JPanel jPanelListaOperacionesGestionarusuarios;
	private JButton btnAplicarCB;
	private JCheckBox checkboxEditarCB;
	private JTextArea txtAreaBienvenida;
	private JMenuItem menuitemConfiguracion;
	private JMenuItem menuitemAcercaDe;
	private JSeparator jSeparator2;
	private JMenuItem menuitemCerrarSesion;
	private JMenuItem menuitemIniciarSesion;
	private JMenuItem menuitemSalir;
	private JTextField txtCorreoElectronicoCB;
	private JTextField txtDomicilioCB;
	private JTextField txtApellidosCB;
	private JTextField txtNombreCB;
	private JTextField txtNSSCB;
	private JTextField txtNIFCB;
	private JTextField txtIdentificacionCB;
	private JButton btnBuscarCB;
	private JLabel jLabel15;
	private JComboBox cbIdentificacionCB;
	private JLabel jLabel13;
	private JLabel jLabel12;
	private JTextField txtPuertaRB;
	private JLabel jLabel11;
	private JTextField txtNumeroRB;
	private JPanel jPanelModificarUsuario;
	private JMenu jMenu4;
	private JMenu jMenu3;
	private JMenu jMenu1;
	private JMenuBar jMenuBar;
	private JLabel jLabel10;
	private JList jListOperacionesBeneficiarios;
	private JSeparator jSeparator1;
	private JPanel jPanelListaOperacionesGestionarBeneficiarios;
	private JPanel jPanelGestionarUsuarios;
	private JPanel jPanelGestionarCitas;
	private JPanel jPanelGestionarBeneficiarios;
	private JPanel jPanelEstablecerSustituto;
	private JPanel jPanelModificarCalendario;
	private JPanel jPanelConsultarMedico;
	private JPanel jPanelEliminarUsuario;
	private JPanel jPanelCrearUsuario;
	private JPanel jPanelConsultarBeneficiario;
	private JPanel jPanelEliminarCita;
	private JTextField txtCorreoElectronicoRB;
	private JTextField txtDomicilioRB;
	private JTextField txtApellidosRB;
	private JTextField txtNombreRB;
	private JTextField txtNSSRB;
	private JLabel jLabel5;
	private JTextField txtNIFRB;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel4;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel jPanelBienvenida;
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
		this.iniciarModelosDeListas();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setPreferredSize(new java.awt.Dimension(600, 425));
			this.setTitle("SSCA - Unidad de Citación");
			this.setMinimumSize(new java.awt.Dimension(600, 425));
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
				jPanelOperaciones.setPreferredSize(new java.awt.Dimension(592, 354));
				{
					jTabbedPaneOperaciones = new JTabbedPane();
					jPanelOperaciones.add(jTabbedPaneOperaciones, new AnchorConstraint(1, 979, 962, 20, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jTabbedPaneOperaciones.setPreferredSize(new java.awt.Dimension(578, 292));
					{
						jPanelBienvenida = new JPanel();
						AnchorLayout jPanelBienvenidaLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Inicio", null, jPanelBienvenida, null);
						jPanelBienvenida.setLayout(jPanelBienvenidaLayout);
						jPanelBienvenida.setSize(565, 390);
						jPanelBienvenida.setPreferredSize(new java.awt.Dimension(565, 390));
						{
							txtAreaBienvenida = new JTextArea();
							jPanelBienvenida.add(txtAreaBienvenida, new AnchorConstraint(39, 979, 963, 22, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							txtAreaBienvenida.setText("Mensaje de bienvenida\nasdf\nasdf\nasdf\nasdf\nasdf\nasdf\nasdf\nasdf");
							txtAreaBienvenida.setPreferredSize(new java.awt.Dimension(539, 291));
							txtAreaBienvenida.setFocusable(false);
							txtAreaBienvenida.setEditable(false);
							txtAreaBienvenida.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
							txtAreaBienvenida.setOpaque(false);
						}
					}
					{
						jPanelGestionarBeneficiarios = new JPanel();
						jTabbedPaneOperaciones.addTab("Gestionar Beneficiarios", null, jPanelGestionarBeneficiarios, null);
						AnchorLayout jPanelGestionarBeneficiariosLayout = new AnchorLayout();
						jPanelGestionarBeneficiarios.setLayout(jPanelGestionarBeneficiariosLayout);
						jPanelGestionarBeneficiarios.setSize(565, 390);
						jPanelGestionarBeneficiarios.setPreferredSize(new java.awt.Dimension(565, 390));
						{
							jPanelRegistrarBeneficiario = new JPanel();
							jPanelGestionarBeneficiarios.add(jPanelRegistrarBeneficiario, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jPanelRegistrarBeneficiarioLayout = new AnchorLayout();
							jPanelRegistrarBeneficiario.setLayout(jPanelRegistrarBeneficiarioLayout);
							jPanelRegistrarBeneficiario.setPreferredSize(new java.awt.Dimension(430, 296));
							{
								btnRestablecerRB = new JButton();
								jPanelRegistrarBeneficiario.add(btnRestablecerRB, new AnchorConstraint(275, 182, 961, 549, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
								btnRestablecerRB.setText("Restablecer");
								btnRestablecerRB.setPreferredSize(new java.awt.Dimension(154, 26));
								btnRestablecerRB.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										btnRestablecerRBActionPerformed(evt);
									}
								});
							}
							{
								btnCrearBeneficiarioRB = new JButton();
								jPanelRegistrarBeneficiario.add(btnCrearBeneficiarioRB, new AnchorConstraint(275, 17, 961, 765, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
								btnCrearBeneficiarioRB.setDefaultCapable(true);
								jPanelRegistrarBeneficiario.getRootPane().setDefaultButton(btnCrearBeneficiarioRB);
								btnCrearBeneficiarioRB.setText("Crear beneficiario");
								btnCrearBeneficiarioRB.setPreferredSize(new java.awt.Dimension(154, 26));
								btnCrearBeneficiarioRB.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										btnCrearBeneficiarioRBActionPerformed(evt);
									}
								});
							}
							{
								txtTelefonoMovilRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtTelefonoMovilRB, new AnchorConstraint(229, 16, 805, 185, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtTelefonoMovilRB.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtTelefonoFijoRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtTelefonoFijoRB, new AnchorConstraint(200, 16, 710, 185, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtTelefonoFijoRB.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtCorreoElectronicoRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtCorreoElectronicoRB, new AnchorConstraint(172, 16, 612, 185, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtCorreoElectronicoRB.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtPuertaRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtPuertaRB, new AnchorConstraint(146, 961, 534, 388, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtPuertaRB.setPreferredSize(new java.awt.Dimension(25, 22));
							}
							{
								txtPisoRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtPisoRB, new AnchorConstraint(146, 775, 534, 308, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtPisoRB.setPreferredSize(new java.awt.Dimension(25, 22));
							}
							{
								txtNumeroRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtNumeroRB, new AnchorConstraint(146, 598, 534, 232, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtNumeroRB.setPreferredSize(new java.awt.Dimension(25, 22));
							}
							{
								txtDomicilioRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtDomicilioRB, new AnchorConstraint(119, 17, 514, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtDomicilioRB.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtApellidosRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtApellidosRB, new AnchorConstraint(92, 17, 416, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtApellidosRB.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtNombreRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtNombreRB, new AnchorConstraint(65, 17, 318, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtNombreRB.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtNSSRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtNSSRB, new AnchorConstraint(39, 17, 223, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtNSSRB.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtNIFRB = new JTextField();
								jPanelRegistrarBeneficiario.add(txtNIFRB, new AnchorConstraint(12, 17, 125, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtNIFRB.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								jLabel8 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel8, new AnchorConstraint(232, 249, 790, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel8.setText("Teléfono móvil");
								jLabel8.setPreferredSize(new java.awt.Dimension(168, 18));
							}
							{
								jLabel7 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel7, new AnchorConstraint(203, 247, 692, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel7.setText("Teléfono fijo");
								jLabel7.setPreferredSize(new java.awt.Dimension(170, 13));
							}
							{
								jLabel6 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel6, new AnchorConstraint(175, 245, 594, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel6.setText("Correo electrónico");
								jLabel6.setPreferredSize(new java.awt.Dimension(172, 12));
							}
							{
								jLabel13 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel13, new AnchorConstraint(152, 905, 519, 339, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel13.setText("Puerta");
								jLabel13.setPreferredSize(new java.awt.Dimension(50, 11));
							}
							{
								jLabel12 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel12, new AnchorConstraint(149, 717, 525, 270, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel12.setText("Piso");
								jLabel12.setPreferredSize(new java.awt.Dimension(38, 16));
							}
							{
								jLabel11 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel11, new AnchorConstraint(152, 540, 515, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel11.setText("Numero");
								jLabel11.setPreferredSize(new java.awt.Dimension(48, 10));
							}
							{
								jLabel5 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel5, new AnchorConstraint(122, 246, 496, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel5.setText("Domicilio");
								jLabel5.setPreferredSize(new java.awt.Dimension(172, 14));
								jLabel5.setBounds(12, 122, 172, 14);
							}
							{
								jLabel4 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel4, new AnchorConstraint(95, 246, 398, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel4.setText("Apellidos");
								jLabel4.setPreferredSize(new java.awt.Dimension(172, 14));
								jLabel4.setBounds(12, 95, 172, 14);
							}
							{
								jLabel3 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel3, new AnchorConstraint(68, 246, 303, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel3.setText("Nombre");
								jLabel3.setPreferredSize(new java.awt.Dimension(172, 14));
								jLabel3.setBounds(12, 68, 172, 14);
							}
							{
								jLabel2 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel2, new AnchorConstraint(42, 246, 209, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel2.setText("Número de Seguridad Social");
								jLabel2.setPreferredSize(new java.awt.Dimension(172, 15));
								jLabel2.setBounds(12, 42, 172, 15);
							}
							{
								jLabel1 = new JLabel();
								jPanelRegistrarBeneficiario.add(jLabel1, new AnchorConstraint(15, 246, 110, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel1.setText("NIF");
								jLabel1.setPreferredSize(new java.awt.Dimension(172, 14));
								jLabel1.setBounds(12, 15, 172, 14);
							}
						}
						{
							jPanelConsultarBeneficiario = new JPanel();
							jPanelGestionarBeneficiarios.add(jPanelConsultarBeneficiario, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jPanelConsultarBeneficiarioLayout = new AnchorLayout();
							jPanelConsultarBeneficiario.setLayout(jPanelConsultarBeneficiarioLayout);
							jPanelConsultarBeneficiario.setPreferredSize(new java.awt.Dimension(430, 296));
							{
								btnAplicarCB = new JButton();
								jPanelConsultarBeneficiario.add(btnAplicarCB, new AnchorConstraint(280, 12, 963, 794, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
								btnAplicarCB.setText("Aplicar");
								btnAplicarCB.setPreferredSize(new java.awt.Dimension(77, 23));
								btnAplicarCB.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										btnAplicarCBActionPerformed(evt);
									}
								});
								btnAplicarCB.setEnabled(false);
							}
							{
								checkboxEditarCB = new JCheckBox();
								jPanelConsultarBeneficiario.add(checkboxEditarCB, new AnchorConstraint(255, 0, 854, 788, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
								checkboxEditarCB.setText("¿Editar?");
								checkboxEditarCB.setPreferredSize(new java.awt.Dimension(95, 14));
								checkboxEditarCB.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										checkboxEditarCBActionPerformed(evt);
									}
								});
								checkboxEditarCB.setEnabled(false);
							}
							{
								jLabel24 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel24, new AnchorConstraint(281, 318, 950, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel24.setText("Medico asignado");
								jLabel24.setPreferredSize(new java.awt.Dimension(110, 18));
							}
							{
								jLabel23 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel23, new AnchorConstraint(256, 318, 861, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel23.setText("Teléfono móvil");
								jLabel23.setPreferredSize(new java.awt.Dimension(110, 15));
							}
							{
								jLabel22 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel22, new AnchorConstraint(229, 318, 776, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel22.setText("Teléfono fijo");
								jLabel22.setPreferredSize(new java.awt.Dimension(110, 15));
							}
							{
								jLabel21 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel21, new AnchorConstraint(202, 261, 690, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel21.setText("Correo electrónico");
								jLabel21.setPreferredSize(new java.awt.Dimension(161, 15));
							}
							{
								jLabel20 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel20, new AnchorConstraint(175, 318, 604, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel20.setText("Domicilio");
								jLabel20.setPreferredSize(new java.awt.Dimension(110, 15));
							}
							{
								jLabel19 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel19, new AnchorConstraint(149, 318, 522, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel19.setText("Apellidos");
								jLabel19.setPreferredSize(new java.awt.Dimension(110, 15));
							}
							{
								jLabel18 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel18, new AnchorConstraint(121, 318, 433, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel18.setText("Nombre");
								jLabel18.setPreferredSize(new java.awt.Dimension(110, 15));
							}
							{
								jLabel17 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel17, new AnchorConstraint(93, 318, 344, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel17.setText("NSS");
								jLabel17.setPreferredSize(new java.awt.Dimension(110, 15));
							}
							{
								jLabel16 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel16, new AnchorConstraint(67, 318, 261, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel16.setText("NIF");
								jLabel16.setPreferredSize(new java.awt.Dimension(110, 15));
							}
							{
								txtMedicoAsignadoCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtMedicoAsignadoCB, new AnchorConstraint(278, 101, 953, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtMedicoAsignadoCB.setEditable(false);
								txtMedicoAsignadoCB.setFocusable(false);
								txtMedicoAsignadoCB.setPreferredSize(new java.awt.Dimension(217, 22));
							}
							{
								txtTelefonoMovilCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtTelefonoMovilCB, new AnchorConstraint(252, 102, 874, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtTelefonoMovilCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtTelefonoMovilCB.setFocusable(false);
								txtTelefonoMovilCB.setEditable(false);
							}
							{
								txtTelefonoFijoCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtTelefonoFijoCB, new AnchorConstraint(225, 102, 788, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtTelefonoFijoCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtTelefonoFijoCB.setFocusable(false);
								txtTelefonoFijoCB.setEditable(false);
							}
							{
								txtCorreoElectronicoCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtCorreoElectronicoCB, new AnchorConstraint(197, 102, 700, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtCorreoElectronicoCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtCorreoElectronicoCB.setFocusable(false);
								txtCorreoElectronicoCB.setEditable(false);
							}
							{
								txtDomicilioCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtDomicilioCB, new AnchorConstraint(171, 102, 617, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtDomicilioCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtDomicilioCB.setFocusable(false);
								txtDomicilioCB.setEditable(false);
							}
							{
								txtApellidosCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtApellidosCB, new AnchorConstraint(145, 102, 534, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtApellidosCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtApellidosCB.setFocusable(false);
								txtApellidosCB.setEditable(false);
							}
							{
								txtNombreCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtNombreCB, new AnchorConstraint(116, 102, 442, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtNombreCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtNombreCB.setFocusable(false);
								txtNombreCB.setEditable(false);
							}
							{
								txtNSSCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtNSSCB, new AnchorConstraint(89, 102, 357, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtNSSCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtNSSCB.setFocusable(false);
								txtNSSCB.setEditable(false);
							}
							{
								txtNIFCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtNIFCB, new AnchorConstraint(62, 102, 271, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtNIFCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtNIFCB.setFocusable(false);
								txtNIFCB.setEditable(false);
							}
							{
								txtIdentificacionCB = new JTextField();
								jPanelConsultarBeneficiario.add(txtIdentificacionCB, new AnchorConstraint(36, 102, 188, 112, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtIdentificacionCB.setPreferredSize(new java.awt.Dimension(216, 23));
								txtIdentificacionCB.setDragEnabled(true);
							}
							{
								btnBuscarCB = new JButton();
								jPanelConsultarBeneficiario.add(btnBuscarCB, new AnchorConstraint(36, 13, 188, 791, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
								btnBuscarCB.setDefaultCapable(true);
								jPanelConsultarBeneficiario.getRootPane().setDefaultButton(btnBuscarCB);
								btnBuscarCB.setText("Buscar");
								btnBuscarCB.setPreferredSize(new java.awt.Dimension(77, 23));
								btnBuscarCB.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										btnBuscarCBActionPerformed(evt);
									}
								});
							}
							{
								jLabel15 = new JLabel();
								jPanelConsultarBeneficiario.add(jLabel15, new AnchorConstraint(14, 272, 90, 2, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel15.setText("Buscar beneficiario por:");
								jLabel15.setPreferredSize(new java.awt.Dimension(156, 14));
							}
							{
								ComboBoxModel cbIdentificacionModel = 
									new DefaultComboBoxModel(
											new String[] { "NIF", "NSS" });
								cbIdentificacionCB = new JComboBox();
								jPanelConsultarBeneficiario.add(cbIdentificacionCB, new AnchorConstraint(37, 236, 188, 1, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								cbIdentificacionCB.setModel(cbIdentificacionModel);
								cbIdentificacionCB.setPreferredSize(new java.awt.Dimension(100, 22));
							}
						}
						{
							jSeparator1 = new JSeparator();
							jPanelGestionarBeneficiarios.add(jSeparator1, new AnchorConstraint(1, 237, 1001, 224, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jSeparator1Layout = new AnchorLayout();
							jSeparator1.setLayout(jSeparator1Layout);
							jSeparator1.setPreferredSize(new java.awt.Dimension(7, 296));
							jSeparator1.setOrientation(SwingConstants.VERTICAL);
						}
						{
							jPanelListaOperacionesGestionarBeneficiarios = new JPanel();
							AnchorLayout jPanel1Layout = new AnchorLayout();
							jPanelGestionarBeneficiarios.add(jPanelListaOperacionesGestionarBeneficiarios, new AnchorConstraint(5, 214, 0, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
							jPanelListaOperacionesGestionarBeneficiarios.setLayout(jPanel1Layout);
							jPanelListaOperacionesGestionarBeneficiarios.setPreferredSize(new java.awt.Dimension(114, 310));
							{
								jLabel10 = new JLabel();
								jPanelListaOperacionesGestionarBeneficiarios.add(jLabel10, new AnchorConstraint(0, 1004, 89, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel10.setText("Operaciones:");
								jLabel10.setPreferredSize(new java.awt.Dimension(107, 15));
							}
							{
								jListOperacionesBeneficiarios = new JList(jListOperacionesBeneficiariosModel);
								AnchorLayout jList1Layout = new AnchorLayout();
								jPanelListaOperacionesGestionarBeneficiarios.add(jListOperacionesBeneficiarios, new AnchorConstraint(21, 1004, 11, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
								jListOperacionesBeneficiarios.setPreferredSize(new java.awt.Dimension(107, 264));
								jListOperacionesBeneficiarios.setLayout(null);
								jListOperacionesBeneficiarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
								jListOperacionesBeneficiarios.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
								jListOperacionesBeneficiarios.addListSelectionListener(new ListSelectionListener() {
									public void valueChanged(ListSelectionEvent evt) {
										jListOperacionesBeneficiariosValueChanged(evt);
									}
								});
							}
						}
					}
					{
						jPanelGestionarUsuarios = new JPanel();
						jTabbedPaneOperaciones.addTab("Gestionar Usuarios", null, jPanelGestionarUsuarios, null);
						AnchorLayout jPanelGestionarUsuariosLayout = new AnchorLayout();
						jPanelGestionarUsuarios.setLayout(jPanelGestionarUsuariosLayout);
						{
							jPanelCrearUsuario = new JPanel();
							jPanelGestionarUsuarios.add(jPanelCrearUsuario, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jPanelCrearUsuarioLayout = new AnchorLayout();
							jPanelCrearUsuario.setLayout(jPanelCrearUsuarioLayout);
							jPanelCrearUsuario.setPreferredSize(new java.awt.Dimension(430, 315));
							jPanelCrearUsuario.setSize(430, 315);
							{
								btnRestablecerCU = new JButton();
								jPanelCrearUsuario.add(btnRestablecerCU, new AnchorConstraint(275, 182, 957, 219, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
								btnRestablecerCU.setText("Restablecer");
								btnRestablecerCU.setPreferredSize(new java.awt.Dimension(154, 26));
							}
							{
								btnCrearUsuarioCU = new JButton();
								jPanelCrearUsuario.add(btnCrearUsuarioCU, new AnchorConstraint(275, 17, 957, 603, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
								btnCrearUsuarioCU.setText("Crear usuario");
								btnCrearUsuarioCU.setPreferredSize(new java.awt.Dimension(154, 26));
							}
							{
								ListModel jListTipoUsuarioModel = 
									new DefaultComboBoxModel(
											new String[] { "Administrador", "Médico", "Citador" });
								jListTipoUsuario = new JList();
								jPanelCrearUsuario.add(jListTipoUsuario, new AnchorConstraint(180, 17, 773, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jListTipoUsuario.setModel(jListTipoUsuarioModel);
								jListTipoUsuario.setPreferredSize(new java.awt.Dimension(229, 63));
								jListTipoUsuario.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
								jListTipoUsuario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
								jListTipoUsuario.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
							}
							{
								txtPassword2CU = new JPasswordField();
								jPanelCrearUsuario.add(txtPassword2CU, new AnchorConstraint(146, 17, 534, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtPassword2CU.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtPasswordCU = new JPasswordField();
								jPanelCrearUsuario.add(txtPasswordCU, new AnchorConstraint(119, 17, 449, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtPasswordCU.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtLoginCU = new JTextField();
								jPanelCrearUsuario.add(txtLoginCU, new AnchorConstraint(92, 17, 363, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtLoginCU.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtApellidosCU = new JTextField();
								jPanelCrearUsuario.add(txtApellidosCU, new AnchorConstraint(65, 17, 277, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtApellidosCU.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtNombreCU = new JTextField();
								jPanelCrearUsuario.add(txtNombreCU, new AnchorConstraint(39, 17, 195, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtNombreCU.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								txtDNICU = new JTextField();
								jPanelCrearUsuario.add(txtDNICU, new AnchorConstraint(12, 17, 109, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								txtDNICU.setPreferredSize(new java.awt.Dimension(229, 22));
							}
							{
								jLabel30 = new JLabel();
								jPanelCrearUsuario.add(jLabel30, new AnchorConstraint(176, 246, 604, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel30.setText("Tipo de usuario");
								jLabel30.setPreferredSize(new java.awt.Dimension(172, 14));
							}
							{
								jLabel27 = new JLabel();
								jPanelCrearUsuario.add(jLabel27, new AnchorConstraint(149, 246, 519, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel27.setText("Confirmar contraseña");
								jLabel27.setPreferredSize(new java.awt.Dimension(172, 14));
							}
							{
								jLabel26 = new JLabel();
								jPanelCrearUsuario.add(jLabel26, new AnchorConstraint(122, 246, 433, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel26.setText("Contraseña");
								jLabel26.setPreferredSize(new java.awt.Dimension(172, 14));
							}
							{
								jLabel25 = new JLabel();
								jPanelCrearUsuario.add(jLabel25, new AnchorConstraint(95, 246, 347, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel25.setText("Usuario");
								jLabel25.setPreferredSize(new java.awt.Dimension(172, 14));
							}
							{
								jLabel29 = new JLabel();
								jPanelCrearUsuario.add(jLabel29, new AnchorConstraint(68, 246, 261, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel29.setText("Apellidos");
								jLabel29.setPreferredSize(new java.awt.Dimension(172, 14));
							}
							{
								jLabel28 = new JLabel();
								jPanelCrearUsuario.add(jLabel28, new AnchorConstraint(42, 246, 182, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel28.setText("Nombre");
								jLabel28.setPreferredSize(new java.awt.Dimension(172, 15));
							}
							{
								jLabel14 = new JLabel();
								jPanelCrearUsuario.add(jLabel14, new AnchorConstraint(15, 246, 93, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel14.setText("DNI");
								jLabel14.setPreferredSize(new java.awt.Dimension(172, 14));
							}
						}
						{
							jPanelModificarUsuario = new JPanel();
							jPanelGestionarUsuarios.add(jPanelModificarUsuario, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jPanelModificarUsuarioLayout = new AnchorLayout();
							jPanelModificarUsuario.setLayout(jPanelModificarUsuarioLayout);
							jPanelModificarUsuario.setPreferredSize(new java.awt.Dimension(430, 315));
						}
						{
							jPanelEliminarUsuario = new JPanel();
							jPanelGestionarUsuarios.add(jPanelEliminarUsuario, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jPanelEliminarUsuarioLayout = new AnchorLayout();
							jPanelEliminarUsuario.setLayout(jPanelEliminarUsuarioLayout);
							jPanelEliminarUsuario.setPreferredSize(new java.awt.Dimension(430, 315));
						}
						{
							jSeparator3 = new JSeparator();
							jPanelGestionarUsuarios.add(jSeparator3, new AnchorConstraint(1, 237, 1001, 224, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jSeparator3Layout = new AnchorLayout();
							jSeparator3.setLayout(null);
							jSeparator3.setPreferredSize(new java.awt.Dimension(7, 315));
							jSeparator3.setOrientation(SwingConstants.VERTICAL);
						}
						{
							jPanelListaOperacionesGestionarusuarios = new JPanel();
							jPanelGestionarUsuarios.add(jPanelListaOperacionesGestionarusuarios, new AnchorConstraint(5, 214, 0, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
							AnchorLayout jPanelListaOperacionesGestionarusuariosLayout = new AnchorLayout();
							jPanelListaOperacionesGestionarusuarios.setLayout(jPanelListaOperacionesGestionarusuariosLayout);
							jPanelListaOperacionesGestionarusuarios.setPreferredSize(new java.awt.Dimension(114, 310));
							{
								jLabel9 = new JLabel();
								jPanelListaOperacionesGestionarusuarios.add(jLabel9, new AnchorConstraint(0, 1004, 50, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel9.setText("Operaciones:");
								jLabel9.setPreferredSize(new java.awt.Dimension(107, 15));
							}
							{
								jListOperacionesUsuarios = new JList(jListOperacionesUsuariosModel);
								jPanelListaOperacionesGestionarusuarios.add(jListOperacionesUsuarios, new AnchorConstraint(21, 1004, 11, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
								jListOperacionesUsuarios.setPreferredSize(new java.awt.Dimension(107, 278));
								jListOperacionesUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
								jListOperacionesUsuarios.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
								jListOperacionesUsuarios.addListSelectionListener(new ListSelectionListener() {
									public void valueChanged(ListSelectionEvent evt) {
										jListOperacionesUsuariosValueChanged(evt);
									}
								});
							}
						}
					}
					{
						jPanelGestionarCitas = new JPanel();
						jTabbedPaneOperaciones.addTab("Gestionar Citas", null, jPanelGestionarCitas, null);
						AnchorLayout jPanelGestionarCitasLayout = new AnchorLayout();
						jPanelGestionarCitas.setLayout(jPanelGestionarCitasLayout);
						jPanelGestionarCitas.setPreferredSize(new java.awt.Dimension(565, 390));
						jPanelGestionarCitas.setSize(565, 390);
						{
							jPanelEliminarCita = new JPanel();
							jPanelGestionarCitas.add(jPanelEliminarCita, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jPanelEliminarCitaLayout = new AnchorLayout();
							jPanelEliminarCita.setLayout(jPanelEliminarCitaLayout);
							jPanelEliminarCita.setPreferredSize(new java.awt.Dimension(430, 322));
						}
						{
							jPanelTramitarCita = new JPanel();
							jPanelGestionarCitas.add(jPanelTramitarCita, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							AnchorLayout jPanelTramitarCitaLayout = new AnchorLayout();
							jPanelTramitarCita.setLayout(jPanelTramitarCitaLayout);
							jPanelTramitarCita.setPreferredSize(new java.awt.Dimension(430, 322));
						}
						{
							jSeparator4 = new JSeparator();
							jPanelGestionarCitas.add(jSeparator4, new AnchorConstraint(1, 237, 1001, 224, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							jSeparator4.setPreferredSize(new java.awt.Dimension(7, 322));
							jSeparator4.setOrientation(SwingConstants.VERTICAL);
						}
						{
							jPanelListaOperacionesGestionarCitas = new JPanel();
							AnchorLayout jPanelListaOperacionesGestionarCitasLayout = new AnchorLayout();
							jPanelListaOperacionesGestionarCitas.setLayout(jPanelListaOperacionesGestionarCitasLayout);
							jPanelGestionarCitas.add(jPanelListaOperacionesGestionarCitas, new AnchorConstraint(5, 214, 0, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
							jPanelListaOperacionesGestionarCitas.setPreferredSize(new java.awt.Dimension(114, 317));
							{
								jLabel31 = new JLabel();
								jPanelListaOperacionesGestionarCitas.add(jLabel31, new AnchorConstraint(0, 1004, 48, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
								jLabel31.setText("Operaciones:");
								jLabel31.setPreferredSize(new java.awt.Dimension(107, 15));
							}
							{
								jListOperacionesCitas = new JList(jListOperacionesCitasModel);
								jPanelListaOperacionesGestionarCitas.add(jListOperacionesCitas, new AnchorConstraint(21, 1004, 11, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
								jListOperacionesCitas.setModel(jListOperacionesCitasModel);
								jListOperacionesCitas.setPreferredSize(new java.awt.Dimension(107, 285));
								jListOperacionesCitas.addListSelectionListener(new ListSelectionListener() {
									public void valueChanged(ListSelectionEvent evt) {
										jListOperacionesCitasValueChanged(evt);
									}
								});
							}
						}
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
			this.setSize(600, 425);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnRestablecerRBActionPerformed(ActionEvent evt) {
		LimpiarRegistroBeneficiario();
	}
	
	private void btnCrearBeneficiarioRBActionPerformed(ActionEvent evt) {
		Beneficiario beneficiario = null;
		
		try {
			if (!Utilidades.comprobarNIF(txtNIFRB.getText()))
				throw new NIFIncorrectoException();
			if (!Utilidades.comprobarNSS(txtNSSRB.getText()))
				throw new NSSIncorrectoException();
			if (!Utilidades.comprobarCadena(txtNombreRB.getText()))
				throw new NombreIncorrectoException();
			if (!Utilidades.comprobarCadena(txtApellidosRB.getText()))
				throw new ApellidoIncorrectoException();
			if (!Utilidades.comprobarCadena(txtDomicilioRB.getText()))
				throw new DomicilioIncorrectoException();
			if (!Utilidades.comprobarNumeroEntero(txtNumeroRB.getText()))
				throw new NumeroDomicilioIncorrectoException();
			if (!Utilidades.comprobarNumeroEntero(txtPisoRB.getText()) && !txtPisoRB.getText().equals(""))
				throw new PisoDomicilioIncorrectoException();
			if (!Utilidades.comprobarLetra(txtPuertaRB.getText()) && !txtPuertaRB.getText().equals(""))
				throw new PuertaDomicilioIncorrectoException();
			if (!Utilidades.comprobarCorreoElectronico(txtCorreoElectronicoRB.getText()))
				throw new CorreoElectronicoIncorrectoException();
			if (!Utilidades.comprobarTelefono(txtTelefonoFijoRB.getText()))
				throw new TelefonoFijoIncorrectoException();
			if (!Utilidades.comprobarTelefono(txtTelefonoMovilRB.getText()))
				throw new TelefonoMovilIncorrectoException();

			beneficiario = new Beneficiario();
			beneficiario.setNif(txtNIFRB.getText().toUpperCase());
			beneficiario.setNss(txtNSSRB.getText());
			beneficiario.setNombre(txtNombreRB.getText());
			beneficiario.setApellidos(txtApellidosRB.getText());
			beneficiario.setDomicilio(txtDomicilioRB.getText() + ", nº " + txtNumeroRB.getText());
			if (!txtPisoRB.getText().equals(""))
				beneficiario.setDomicilio(beneficiario.getDomicilio() + ", " + txtPisoRB.getText() + "º");
			if (!txtPuertaRB.getText().equals(""))
				beneficiario.setDomicilio(beneficiario.getDomicilio() + " " + txtPuertaRB.getText().toUpperCase());
			beneficiario.setCorreo(txtCorreoElectronicoRB.getText());
			beneficiario.setTelefono(Integer.parseInt(txtTelefonoFijoRB.getText()));
			beneficiario.setMovil(Integer.parseInt(txtTelefonoMovilRB.getText()));

			controlador.crearBeneficiario(beneficiario);
			Utilidades.mostrarDialogoInformacion(this, "Operacion correcta", "El beneficiario ha sido dado de alta en el sistema.");
			LimpiarRegistroBeneficiario();
		} catch (SQLException e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		} catch (RemoteException e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		} catch (BeneficiarioYaExistenteException e) {
			Utilidades.mostrarDialogoError(this, "Error", "El usuario ya se encuentra dado de alta en el sistema.");
		} catch (NIFIncorrectoException e) {
			txtNIFRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El NIF debe ser el numero de DNI (incluyendo el 0) y la letra sin guion.");
			txtNIFRB.grabFocus();
		} catch (NSSIncorrectoException e) {
			txtNSSRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El NSS debe contener 12 digitos.");
			txtNSSRB.grabFocus();
		} catch (NombreIncorrectoException e) {
			txtNombreRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El nombre del beneficiario solo puede contener letras y espacios.");
			txtNombreRB.grabFocus();
		} catch (ApellidoIncorrectoException e) {
			txtApellidosRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "Los apellidos del beneficiario solo pueden contener letras y espacios.");
			txtApellidosRB.grabFocus();
		} catch (DomicilioIncorrectoException e) {
			txtDomicilioRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El domicilio solo puede contener caracteres alfanumericos.");
			txtDomicilioRB.grabFocus();
		} catch (NumeroDomicilioIncorrectoException e) {
			txtNumeroRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El numero del domicilio debe ser un numero entero.");
			txtNumeroRB.grabFocus();
		} catch (PisoDomicilioIncorrectoException e) {
			txtPisoRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El piso del domicilio debe ser un numero entero.");
			txtPisoRB.grabFocus();
		} catch (PuertaDomicilioIncorrectoException e) {
			txtPuertaRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "La letra del domicilio debe ser un caracter alfabetico.");
			txtPuertaRB.grabFocus();
		} catch (CorreoElectronicoIncorrectoException e) {
			txtCorreoElectronicoRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El formato del correo electronico es incorrecto.");
			txtCorreoElectronicoRB.grabFocus();
		} catch (TelefonoFijoIncorrectoException e) {
			txtTelefonoFijoRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El telefono fijo deben ser 9 digitos sin separadores.");
			txtTelefonoFijoRB.grabFocus();
		} catch (TelefonoMovilIncorrectoException e) {
			txtTelefonoMovilRB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El telefono movil deben ser 9 digitos sin separadores.");
			txtTelefonoMovilRB.grabFocus();
		} catch (Exception e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		}
	}

	private void LimpiarRegistroBeneficiario() {
		txtNIFRB.setText("");
		txtNSSRB.setText("");
		txtNombreRB.setText("");
		txtApellidosRB.setText("");
		txtDomicilioRB.setText("");
		txtNumeroRB.setText("");
		txtPisoRB.setText("");
		txtPuertaRB.setText("");
		txtCorreoElectronicoRB.setText("");
		txtTelefonoFijoRB.setText("");
		txtTelefonoMovilRB.setText("");
	}

	public void setControlador(ControladorLogin controlador) {
		this.controlador = controlador;
	}
	
	public void iniciar() {
		ArrayList<Operaciones> operaciones = null;

		try {
			operaciones = (ArrayList<Operaciones>)controlador.operacionesDisponibles();
			this.configurarInterfaz(operaciones);
		} catch (RemoteException e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		} catch (SesionInvalidaException e) {
			Utilidades.mostrarDialogoError(this, "Error", "Sesión inválida.");
		}
	}
	
	private void iniciarModelosDeListas () {
		jListOperacionesBeneficiariosModel = new DefaultListModel();
		jListOperacionesBeneficiariosModel.addElement("Registrar beneficiario");
		jListOperacionesBeneficiariosModel.addElement("Consultar beneficiario");
		
		jListOperacionesUsuariosModel = new DefaultListModel();
		jListOperacionesUsuariosModel.addElement("Registrar usuario");
		jListOperacionesUsuariosModel.addElement("Modificar usuario");
		jListOperacionesUsuariosModel.addElement("Eliminar usuario");
		
		jListOperacionesCitasModel = new DefaultListModel();
		jListOperacionesCitasModel.addElement("Tramitar cita");
		jListOperacionesCitasModel.addElement("Eliminar cita");
	}
	
	private void configurarInterfaz (ArrayList<Operaciones> operaciones) {
		// Inicializamos las pestañas
		if (!operaciones.contains(Operaciones.RegistrarBeneficiario)
				&& !operaciones.contains(Operaciones.ConsultarBeneficiario))
			jTabbedPaneOperaciones.remove(jPanelGestionarBeneficiarios);
		if (!operaciones.contains(Operaciones.TramitarCita)
				&& !operaciones.contains(Operaciones.EliminarCita))
			jTabbedPaneOperaciones.remove(jPanelGestionarCitas);
		if (!operaciones.contains(Operaciones.CrearUsuario)
				&& !operaciones.contains(Operaciones.ModificarUsuario)
				&& !operaciones.contains(Operaciones.EliminarUsuario))
			jTabbedPaneOperaciones.remove(jPanelGestionarUsuarios);
		if (!operaciones.contains(Operaciones.ConsultarMedico))
			jTabbedPaneOperaciones.remove(jPanelConsultarMedico);
		if (!operaciones.contains(Operaciones.ModificarCalendario))
			jTabbedPaneOperaciones.remove(jPanelModificarCalendario);
		if (!operaciones.contains(Operaciones.EstablecerSustituto))
			jTabbedPaneOperaciones.remove(jPanelEstablecerSustituto);
		
		// Inicializamos el contenido de cada pestaña
		if (!operaciones.contains(Operaciones.RegistrarBeneficiario))
			jListOperacionesBeneficiariosModel.removeElement("Registrar beneficiario");
		if (!operaciones.contains(Operaciones.ModificarBeneficiario))
			jListOperacionesBeneficiariosModel.removeElement("Modificar beneficiario");
		if (!operaciones.contains(Operaciones.ConsultarBeneficiario))
			jListOperacionesBeneficiariosModel.removeElement("Consultar beneficiario");
		jListOperacionesBeneficiarios.setSelectedIndex(jListOperacionesBeneficiarios.getFirstVisibleIndex());
		
		if (!operaciones.contains(Operaciones.CrearUsuario))
			jListOperacionesUsuariosModel.removeElement("Registrar usuario");
		if (!operaciones.contains(Operaciones.ModificarUsuario))
			jListOperacionesUsuariosModel.removeElement("Modificar usuario");
		if (!operaciones.contains(Operaciones.EliminarUsuario))
			jListOperacionesUsuariosModel.removeElement("Eliminar usuario");
		jListOperacionesUsuarios.setSelectedIndex(jListOperacionesUsuarios.getFirstVisibleIndex());
		
		if (!operaciones.contains(Operaciones.TramitarCita))
			jListOperacionesCitasModel.removeElement("Tramitar cita");
		if (!operaciones.contains(Operaciones.EliminarCita))
			jListOperacionesCitasModel.removeElement("Eliminar cita");
		jListOperacionesCitas.setSelectedIndex(jListOperacionesCitas.getFirstVisibleIndex());
		
		jPanelBienvenida.repaint();
	}
	
	private void jListOperacionesBeneficiariosValueChanged(ListSelectionEvent evt) {
		if (jPanelRegistrarBeneficiario.isValid()) jPanelRegistrarBeneficiario.setVisible(false);
		if (jPanelConsultarBeneficiario.isValid()) jPanelConsultarBeneficiario.setVisible(false);
		if (jListOperacionesBeneficiarios.getSelectedValue().equals("Registrar beneficiario")) {
			jPanelRegistrarBeneficiario.setVisible(true);
			jPanelRegistrarBeneficiario.repaint();
		}
		if (jListOperacionesBeneficiarios.getSelectedValue().equals("Consultar beneficiario")) {
			jPanelConsultarBeneficiario.setVisible(true);
			jPanelConsultarBeneficiario.repaint();
		}
	}
	
	private void btnBuscarCBActionPerformed(ActionEvent evt) {
		Beneficiario bene = null;
		String sIdentificacion = txtIdentificacionCB.getText().toUpperCase();
		String sTipo = (String)cbIdentificacionCB.getSelectedItem();
		
		try {
			if (sIdentificacion.equals("")) {
				throw new CadenaVaciaException();
			}
			if (sTipo.equals("NIF")) {
				if (!Utilidades.comprobarNIF(sIdentificacion))
					throw new NIFIncorrectoException();
				else {
					bene = controlador.getBeneficiario(sIdentificacion);
				}
			}
			else if (sTipo.equals("NSS")) {
				if (!Utilidades.comprobarNSS(sIdentificacion))
					throw new NSSIncorrectoException();
				else {
					bene = controlador.getBeneficiarioPorNSS(sIdentificacion);
				}
			}
			Utilidades.mostrarDialogoInformacion(this, "Resultados de la búsqueda", "Usuario encontrado.");
			txtIdentificacionCB.setText("");
			txtNIFCB.setText(bene.getNif());
			txtNSSCB.setText(bene.getNss());
			txtNombreCB.setText(bene.getNombre());
			txtApellidosCB.setText(bene.getApellidos());
			txtDomicilioCB.setText(bene.getDomicilio());
			txtCorreoElectronicoCB.setText(bene.getCorreo());
			txtTelefonoFijoCB.setText(Integer.toString(bene.getTelefono()));
			txtTelefonoMovilCB.setText(Integer.toString(bene.getMovil()));
			txtMedicoAsignadoCB.setText(bene.getMedicoAsignado().getDni());
			checkboxEditarCB.setEnabled(true);
		} catch (CadenaVaciaException e) {
			Utilidades.mostrarDialogoError(this, "Error", "Debe introducir un NIF o NSS.");
			txtIdentificacionCB.grabFocus();
		} catch (NIFIncorrectoException e) {
			Utilidades.mostrarDialogoError(this, "Error", "Debe introducir NIF válido.");
			txtIdentificacionCB.selectAll();
			txtIdentificacionCB.grabFocus();
		} catch (NSSIncorrectoException e) {
			Utilidades.mostrarDialogoError(this, "Error", "Debe introducir un NSS válido.");
			txtIdentificacionCB.selectAll();
			txtIdentificacionCB.grabFocus();
		} catch (RemoteException e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		} catch (SQLException e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		} catch (BeneficiarioInexistenteException e) {
			Utilidades.mostrarDialogoError(this, "Error", "El beneficiario no se encuentra dado de alta en el sistema.");
			txtIdentificacionCB.selectAll();
			txtIdentificacionCB.grabFocus();
		} catch (Exception e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		}
	}
	
	private void checkboxEditarCBActionPerformed(ActionEvent evt) {
		boolean bEstado = checkboxEditarCB.isSelected();
		configurarFormularioConsultarBeneficiario(bEstado);
	}
	
	private void configurarFormularioConsultarBeneficiario(boolean estado) {
		txtNombreCB.setEditable(estado);
		txtNombreCB.setFocusable(estado);
		txtApellidosCB.setEditable(estado);
		txtApellidosCB.setFocusable(estado);
		txtDomicilioCB.setEditable(estado);
		txtDomicilioCB.setFocusable(estado);
		txtCorreoElectronicoCB.setEditable(estado);
		txtCorreoElectronicoCB.setFocusable(estado);
		txtTelefonoFijoCB.setEditable(estado);
		txtTelefonoFijoCB.setFocusable(estado);
		txtTelefonoMovilCB.setEditable(estado);
		txtTelefonoMovilCB.setFocusable(estado);
		btnAplicarCB.setEnabled(estado);
	}
	
	private void btnAplicarCBActionPerformed(ActionEvent evt) {
		Beneficiario beneficiario = null;
		try {
			if (!Utilidades.comprobarNIF(txtNIFCB.getText()))
				throw new NIFIncorrectoException();
			if (!Utilidades.comprobarNSS(txtNSSCB.getText()))
				throw new NSSIncorrectoException();
			if (!Utilidades.comprobarCadena(txtNombreCB.getText()))
				throw new NombreIncorrectoException();
			if (!Utilidades.comprobarCadena(txtApellidosCB.getText()))
				throw new ApellidoIncorrectoException();
			if (!Utilidades.comprobarDomicilio(txtDomicilioCB.getText()))
				throw new DomicilioIncorrectoException();
			if (!Utilidades.comprobarCorreoElectronico(txtCorreoElectronicoCB.getText()))
				throw new CorreoElectronicoIncorrectoException();
			if (!Utilidades.comprobarTelefono(txtTelefonoFijoCB.getText()))
				throw new TelefonoFijoIncorrectoException();
			if (!Utilidades.comprobarTelefono(txtTelefonoMovilCB.getText()))
				throw new TelefonoMovilIncorrectoException();
			
			beneficiario = new Beneficiario();
			beneficiario.setNif(txtNIFCB.getText().toUpperCase());
			beneficiario.setNss(txtNSSCB.getText());
			beneficiario.setNombre(txtNombreCB.getText());
			beneficiario.setApellidos(txtApellidosCB.getText());
			beneficiario.setDomicilio(txtDomicilioCB.getText());
			beneficiario.setCorreo(txtCorreoElectronicoCB.getText());
			beneficiario.setTelefono(Integer.parseInt(txtTelefonoFijoCB.getText()));
			beneficiario.setMovil(Integer.parseInt(txtTelefonoMovilCB.getText()));
			Medico medico = controlador.consultarMedico(txtMedicoAsignadoCB.getText());
			beneficiario.setMedicoAsignado(medico);
			controlador.modificarBeneficiario(beneficiario);
			Utilidades.mostrarDialogoInformacion(this, "Operacion correcta", "El beneficiario ha sido modificado correctamente.");
			LimpiarConsultarBeneficiario();
			configurarFormularioConsultarBeneficiario(false);
		} catch (NIFIncorrectoException e) {
			txtNIFCB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El NIF debe ser el numero de DNI (incluyendo el 0) y la letra sin guion.");
			txtNIFCB.grabFocus();
		} catch (NSSIncorrectoException e) {
			txtNSSCB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El NSS debe contener 12 digitos.");
			txtNSSCB.grabFocus();
		} catch (NombreIncorrectoException e) {
			txtNombreCB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El nombre del beneficiario solo puede contener letras y espacios.");
			txtNombreCB.grabFocus();
		} catch (ApellidoIncorrectoException e) {
			txtApellidosCB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "Los apellidos del beneficiario solo pueden contener letras y espacios.");
			txtApellidosCB.grabFocus();
		} catch (DomicilioIncorrectoException e) {
			txtDomicilioCB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El domicilio solo puede contener caracteres alfanumericos.");
			txtDomicilioCB.grabFocus();
		} catch (CorreoElectronicoIncorrectoException e) {
			txtCorreoElectronicoCB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El formato del correo electronico es incorrecto.");
			txtCorreoElectronicoCB.grabFocus();
		} catch (TelefonoFijoIncorrectoException e) {
			txtTelefonoFijoCB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El telefono fijo deben ser 9 digitos sin separadores.");
			txtTelefonoFijoCB.grabFocus();
		} catch (TelefonoMovilIncorrectoException e) {
			txtTelefonoMovilCB.selectAll();
			Utilidades.mostrarDialogoError(this, "Error", "El telefono movil deben ser 9 digitos sin separadores.");
			txtTelefonoMovilCB.grabFocus();
		} catch (RemoteException e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		} catch (SQLException e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		} catch (BeneficiarioInexistenteException e) {
			Utilidades.mostrarDialogoError(this, "Error", "El beneficiario no se encuentra dado de alta en el sistema.");
		} catch (MedicoInexistenteException e) {
			Utilidades.mostrarDialogoError(this, "Error", "El médico del beneficiario no se encuentra dado de alta en el sistema.");
		} catch (Exception e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		}
	}
	
	private void LimpiarConsultarBeneficiario() {
		txtNIFCB.setText("");
		txtNSSCB.setText("");
		txtNombreCB.setText("");
		txtApellidosCB.setText("");
		txtDomicilioCB.setText("");
		txtCorreoElectronicoCB.setText("");
		txtTelefonoFijoCB.setText("");
		txtTelefonoMovilCB.setText("");
		txtMedicoAsignadoCB.setText("");
		checkboxEditarCB.setSelected(false);
		checkboxEditarCB.setEnabled(false);
	}
	
	private void jListOperacionesUsuariosValueChanged(ListSelectionEvent evt) {
		if (jPanelCrearUsuario.isValid()) jPanelCrearUsuario.setVisible(false);
		if (jPanelModificarUsuario.isValid()) jPanelModificarUsuario.setVisible(false);
		if (jPanelEliminarUsuario.isValid()) jPanelEliminarUsuario.setVisible(false);
		if (jListOperacionesUsuarios.getSelectedValue().equals("Registrar usuario")) {
			jPanelCrearUsuario.setVisible(true);
			jPanelCrearUsuario.repaint();
		}
		if (jListOperacionesUsuarios.getSelectedValue().equals("Modificiar usuario")) {
			jPanelModificarUsuario.setVisible(true);
			jPanelModificarUsuario.repaint();
		}
		if (jListOperacionesUsuarios.getSelectedValue().equals("Elimiar usuario")) {
			jPanelEliminarUsuario.setVisible(true);
			jPanelEliminarUsuario.repaint();
		}
	}
	
	private void thisWindowClosing(WindowEvent evt) {
		try {
			if (controlador != null)
				controlador.cerrarSesion ();
		} catch (RemoteException e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		} catch (Exception e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		}
	}
	
	private void jListOperacionesCitasValueChanged(ListSelectionEvent evt) {
		if (jPanelTramitarCita.isValid()) jPanelTramitarCita.setVisible(false);
		if (jPanelEliminarCita.isValid()) jPanelEliminarCita.setVisible(false);
		if (jListOperacionesCitas.getSelectedValue().equals("Tramitar cita")) {
			jPanelTramitarCita.setVisible(true);
			jPanelTramitarCita.repaint();
		}
		if (jListOperacionesCitas.getSelectedValue().equals("Eliminar cita")) {
			jPanelEliminarCita.setVisible(true);
			jPanelEliminarCita.repaint();
		}
	}

}
