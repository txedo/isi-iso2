package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;

import dominio.ControladorLogin;
import excepciones.UsuarioIncorrectoException;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private ControladorLogin controlador;
	private JLabel jLabel2;
	private JTextField txtUsuario;
	private JLabel jLabel4;
	private JTextField txtIPServidor;
	private JPanel jPanel1;
	private JButton btnAvanzado;
	private JButton btnConectar;
	private JPasswordField txtPassword;
	private JLabel jLabel3;

	public JFLogin() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Inicio de sesión");
			this.setPreferredSize(new java.awt.Dimension(296, 140));
			this.setMinimumSize(new java.awt.Dimension(296, 140));
			this.setMaximumSize(new java.awt.Dimension(2147483647, 178));
			this.setResizable(false);
			{
				jPanel1 = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1.setPreferredSize(new java.awt.Dimension(296, 140));
				jPanel1.setSize(296, 140);
				jPanel1.setMinimumSize(new java.awt.Dimension(296, 140));
				{
					btnAvanzado = new JButton();
					btnAvanzado.setLayout(null);
					jPanel1.add(btnAvanzado, new AnchorConstraint(458, 291, 18, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					btnAvanzado.setText("Avanzado >>");
					btnAvanzado.setPreferredSize(new java.awt.Dimension(101, 23));
					btnAvanzado.setRequestFocusEnabled(false);
					btnAvanzado.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAvanzadoActionPerformed(evt);
						}
					});
				}
				{
					btnConectar = new JButton();
					jPanel1.add(btnConectar, new AnchorConstraint(450, 20, 18, 698, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnConectar.setDefaultCapable(true);
					getRootPane().setDefaultButton(btnConectar);
					btnConectar.setText("Iniciar sesión");
					btnConectar.setSelected(true);
					btnConectar.setPreferredSize(new java.awt.Dimension(97, 23));
					btnConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnConectarActionPerformed(evt);
						}
					});
				}
				{
					txtIPServidor = new JTextField();
					jPanel1.add(txtIPServidor, new AnchorConstraint(65, 20, 678, 131, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtIPServidor.setText("127.0.0.1");
					txtIPServidor.setPreferredSize(new java.awt.Dimension(139, 20));
					txtIPServidor.setLayout(null);
				}
				{
					jLabel4 = new JLabel();
					jPanel1.add(jLabel4, new AnchorConstraint(68, 443, 654, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					jLabel4.setText("Dirección IP del servidor");
					jLabel4.setPreferredSize(new java.awt.Dimension(118, 14));
				}
				{
					txtPassword = new JPasswordField();
					jPanel1.add(txtPassword, new AnchorConstraint(40, 20, 440, 131, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtPassword.setPreferredSize(new java.awt.Dimension(139, 20));
					txtPassword.setLayout(null);
				}
				{
					jLabel3 = new JLabel();
					jPanel1.add(jLabel3, new AnchorConstraint(43, 446, 418, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					jLabel3.setText("Contraseña");
					jLabel3.setPreferredSize(new java.awt.Dimension(119, 14));
				}
				{
					txtUsuario = new JTextField();
					jPanel1.add(txtUsuario, new AnchorConstraint(15, 20, 262, 131, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtUsuario.setPreferredSize(new java.awt.Dimension(139, 20));
					txtUsuario.setLayout(null);
				}
				{
					jLabel2 = new JLabel();
					jPanel1.add(jLabel2, new AnchorConstraint(18, 450, 240, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					jLabel2.setText("Usuario");
					jLabel2.setPreferredSize(new java.awt.Dimension(119, 14));
				}
			}
			txtIPServidor.setVisible(false);
			jLabel4.setVisible(false);
			pack();
			this.setSize(296, 140);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setControlador(ControladorLogin controlador) {
		this.controlador = controlador;
	}
	
	private void btnConectarActionPerformed(ActionEvent evt) {
		try {
			controlador.iniciarSesion(txtIPServidor.getText(), txtUsuario.getText(), new String(txtPassword.getPassword()));
		} catch (SQLException e) {
			Utilidades.mostrarDialogoError(this, "Error en el sistema", e.getMessage());
		} catch (UsuarioIncorrectoException e) {
			Utilidades.mostrarDialogoError(this, "Error al autentificar", "El usuario o contraseña son incorrectos.");
		} catch (Exception e) {
			Utilidades.mostrarDialogoError(this, "Error", e.toString());
		}
	}
	
	private void btnAvanzadoActionPerformed(ActionEvent evt) {
		if (btnAvanzado.getText().equals("Avanzado >>")) {
			btnAvanzado.setText("<< Avanzado");
			//Grande [296, 167]
			this.setSize(new java.awt.Dimension(296, 167));
			jPanel1.setSize(new java.awt.Dimension(296, 167));
			txtIPServidor.setVisible(true);
			jLabel4.setVisible(true);
		}
		else {
			if (btnAvanzado.getText().equals("<< Avanzado")) {
				btnAvanzado.setText("Avanzado >>");
				//Pequeño [296, 140]
				this.setSize(new java.awt.Dimension(296, 140));
				jPanel1.setSize(new java.awt.Dimension(296, 140));
				txtIPServidor.setVisible(false);
				jLabel4.setVisible(false);
			}
		}
	}

}
