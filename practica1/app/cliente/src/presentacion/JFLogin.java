package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;

import dominio.ControladorLogin;
import excepciones.UsuarioIncorrectoException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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
public class JFLogin extends javax.swing.JFrame {
	
	private ControladorLogin controlador;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JTextField txtUsuario;
	private JButton btnConectar;
	private JPasswordField txtPassword;
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
				btnConectar = new JButton();
				getContentPane().add(btnConectar, new AnchorConstraint(109, 202, 440, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnConectar.setText("Conectar");
				btnConectar.setPreferredSize(new java.awt.Dimension(87, 28));
				btnConectar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnConectarActionPerformed(evt);
					}
				});
			}
			{
				txtPassword = new JPasswordField();
				getContentPane().add(txtPassword, new AnchorConstraint(73, 10, 10, 99, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword.setPreferredSize(new java.awt.Dimension(266, 21));
			}
			{
				jLabel3 = new JLabel();
				getContentPane().add(jLabel3, new AnchorConstraint(77, 10, 10, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jLabel3.setText("Contraseña: ");
				jLabel3.setPreferredSize(new java.awt.Dimension(79, 14));
			}
			{
				txtUsuario = new JTextField();
				getContentPane().add(txtUsuario, new AnchorConstraint(39, 10, 10, 99, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtUsuario.setPreferredSize(new java.awt.Dimension(266, 21));
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2, new AnchorConstraint(43, 10, 10, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jLabel2.setText("Usuario: ");
				jLabel2.setPreferredSize(new java.awt.Dimension(79, 14));
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
	
	private void btnConectarActionPerformed(ActionEvent evt) {
		try {
			controlador.iniciarSesion(txtUsuario.getText(), new String(txtPassword.getPassword()));
		} catch (SQLException e) {
			Utilidades.mostrarDialogoError(this, "Error en el sistema", e.getMessage());
		} catch (UsuarioIncorrectoException e) {
			Utilidades.mostrarDialogoError(this, "Error al autentificar", "El usuario o contraseña son incorrectos.");
		} catch (Exception e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		}
	}

}
