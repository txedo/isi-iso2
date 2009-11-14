package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.ControladorLogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
public class JFLogin extends javax.swing.JDialog {
	
	private ControladorLogin controlador;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JTextField txtUsuario;
	private JButton jButton1;
	private JTextField txtPassword;
	private JLabel jLabel3;

	public JFLogin() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Identificación");
			this.setPreferredSize(new java.awt.Dimension(383, 178));
			this.setMinimumSize(new java.awt.Dimension(10, 178));
			this.setMaximumSize(new java.awt.Dimension(2147483647, 178));
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1, new AnchorConstraint(109, 202, 440, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jButton1.setText("Conectar");
				jButton1.setPreferredSize(new java.awt.Dimension(87, 28));
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
			{
				txtPassword = new JTextField();
				getContentPane().add(txtPassword, new AnchorConstraint(72, 10, 10, 99, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword.setPreferredSize(new java.awt.Dimension(285, 21));
			}
			{
				jLabel3 = new JLabel();
				getContentPane().add(jLabel3, new AnchorConstraint(75, 10, 10, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jLabel3.setText("Contraseña: ");
				jLabel3.setPreferredSize(new java.awt.Dimension(87, 14));
			}
			{
				txtUsuario = new JTextField();
				getContentPane().add(txtUsuario, new AnchorConstraint(39, 10, 10, 79, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtUsuario.setPreferredSize(new java.awt.Dimension(305, 21));
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2, new AnchorConstraint(42, 10, 10, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jLabel2.setText("Usuario: ");
				jLabel2.setPreferredSize(new java.awt.Dimension(67, 14));
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, new AnchorConstraint(13, 307, 245, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jLabel1.setText("Inicio de sesión");
				jLabel1.setPreferredSize(new java.awt.Dimension(103, 14));
			}
			pack();
			this.setSize(383, 178);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setControlador(ControladorLogin controlador) {
		this.controlador = controlador;
	}
	
	private void jButton1ActionPerformed(ActionEvent evt) {
		controlador.iniciarSesion(txtUsuario.getText(), txtPassword.getText());
	}

}
