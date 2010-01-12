package presentacion;

import javax.swing.BorderFactory;
import javax.swing.DebugGraphics;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

/**
* Panel que permite registrar nuevos usuarios en el sistema.
*/
public class JPUsuarioCrear extends JPBase {

	private static final long serialVersionUID = 4857739286462180783L;
	
	private final String USU_ADMINISTRADOR = "Administrador";
	private final String USU_MEDICO = "Médico";
	private final String USU_CITADOR = "Citador";
	private final String MED_CABECERA = "Cabecera";
	private final String MED_PEDIATRA = "Pediatra";
	private final String MED_ESPECIALISTA = "Especialista";
	
	private DefaultListModel lstTipoUsuarioModel;
	private DefaultListModel lstTipoMedicoModel;
	private JList lstTipoUsuario;
	private JButton btnRestablecer;
	private JButton btnCrearUsuario;
	private JLabel lblApellidos;
	private JPasswordField txtPassword;
	private JList lstTipoMedico;
	private JLabel lblTipoUsuario;
	private JLabel lblNombre;
	private JLabel lblPassword2;
	private JLabel lblPassword;
	private JLabel lblLogin;
	private JLabel lblDNI;
	private JTextField txtApellidos;
	private JTextField txtNombre;
	private JPasswordField txtPassword2;
	private JTextField txtLogin;
	private JTextField txtDNI;
	
	public JPUsuarioCrear() {
		super();
		initGUI();
		crearModelos();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(275, 182, 957, 219, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(154, 26));
			}
			{
				btnCrearUsuario = new JButton();
				this.add(btnCrearUsuario, new AnchorConstraint(275, 17, 957, 603, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnCrearUsuario.setText("Crear usuario");
				btnCrearUsuario.setPreferredSize(new java.awt.Dimension(154, 26));
			}
			{
				lstTipoMedico = new JList();
				this.add(lstTipoMedico, new AnchorConstraint(518, 954, 674, 729, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				lstTipoMedico.setPreferredSize(new java.awt.Dimension(97, 54));
				lstTipoMedico.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				lstTipoMedico.setVisible(false);
			}
			{
				lstTipoUsuario = new JList();
				this.add(lstTipoUsuario, new AnchorConstraint(180, 127, 773, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lstTipoUsuario.setPreferredSize(new java.awt.Dimension(119, 54));
				lstTipoUsuario.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				lstTipoUsuario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lstTipoUsuario.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				lstTipoUsuario.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						lstTipoUsuarioValueChanged(evt);
					}
				});
			}
			{
				txtPassword2 = new JPasswordField();
				this.add(txtPassword2, new AnchorConstraint(146, 17, 534, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword2.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtPassword = new JPasswordField();
				this.add(txtPassword, new AnchorConstraint(119, 17, 449, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtLogin = new JTextField();
				this.add(txtLogin, new AnchorConstraint(92, 17, 363, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLogin.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(65, 17, 277, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(39, 17, 195, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtDNI = new JTextField();
				this.add(txtDNI, new AnchorConstraint(12, 17, 109, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtDNI.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				lblTipoUsuario = new JLabel();
				this.add(lblTipoUsuario, new AnchorConstraint(176, 246, 604, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTipoUsuario.setText("Tipo de usuario");
				lblTipoUsuario.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblPassword2 = new JLabel();
				this.add(lblPassword2, new AnchorConstraint(149, 246, 519, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPassword2.setText("Confirmar contraseña");
				lblPassword2.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblPassword = new JLabel();
				this.add(lblPassword, new AnchorConstraint(122, 246, 433, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPassword.setText("Contraseña");
				lblPassword.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblLogin = new JLabel();
				this.add(lblLogin, new AnchorConstraint(95, 246, 347, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblLogin.setText("Usuario");
				lblLogin.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(68, 246, 261, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos");
				lblApellidos.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(42, 246, 182, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre");
				lblNombre.setPreferredSize(new java.awt.Dimension(172, 15));
			}
			{
				lblDNI = new JLabel();
				this.add(lblDNI, new AnchorConstraint(15, 246, 93, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDNI.setText("DNI");
				lblDNI.setPreferredSize(new java.awt.Dimension(172, 14));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// $hide>>$
	
	private void crearModelos() {
		lstTipoUsuarioModel = new DefaultListModel();
		lstTipoUsuarioModel.addElement(USU_ADMINISTRADOR);
		lstTipoUsuarioModel.addElement(USU_MEDICO);
		lstTipoUsuarioModel.addElement(USU_CITADOR);
		lstTipoUsuario.setModel(lstTipoUsuarioModel);
		lstTipoMedicoModel = new DefaultListModel();
		lstTipoMedicoModel.addElement(MED_CABECERA);
		lstTipoMedicoModel.addElement(MED_PEDIATRA);
		lstTipoMedicoModel.addElement(MED_ESPECIALISTA);
		lstTipoMedico.setModel(lstTipoMedicoModel);
	}
	
	private void lstTipoUsuarioValueChanged(ListSelectionEvent evt) {
		if (lstTipoUsuario.getSelectedValue().equals(USU_MEDICO)) {
			lstTipoMedico.setSelectedIndex(0);
			lstTipoMedico.setVisible(true);
		} else {
			lstTipoMedico.setSelectedIndex(-1);
			lstTipoMedico.setVisible(false);
		}
	}
	
	// $hide<<$

}
