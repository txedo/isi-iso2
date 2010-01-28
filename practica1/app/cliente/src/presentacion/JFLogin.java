package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.conocimiento.Utilidades;
import dominio.control.ControladorCliente;
import excepciones.CadenaVaciaException;
import excepciones.IPInvalidaException;
import excepciones.EnteroIncorrectoException;
import excepciones.UsuarioIncorrectoException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
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
/**
 * Ventana de identificación de usuarios.
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

	private static final long serialVersionUID = -8335773788689456763L;
	
	private final int WIDTH = 296;
	private final int MAX_HEIGHT = 192;
	private final int MIN_HEIGHT = 140;
	
	private ControladorCliente controlador;
	private JLabel jLabel2;
	private JTextField txtUsuario;
	private JLabel lblDireccionServidor;
	private JTextField txtPuertoServidor;
	private JLabel lblPuertoServidor;
	private JTextField txtIPServidor;
	private JPanel jPanel1;
	private JButton btnAvanzado;
	private JButton btnConectar;
	private JPasswordField txtPassword;
	private JLabel jLabel3;
	private JSeparator jSeparator1;

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
			setLocationRelativeTo(null);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					thisWindowClosing(evt);
				}
			});
			{
				jPanel1 = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1.setPreferredSize(new java.awt.Dimension(296, 140));
				jPanel1.setSize(296, 140);
				jPanel1.setMinimumSize(new java.awt.Dimension(296, 140));
				{
					jSeparator1 = new JSeparator();
					jPanel1.add(jSeparator1, new AnchorConstraint(68, 21, 682, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					jSeparator1.setPreferredSize(new java.awt.Dimension(259, 10));
					jSeparator1.setVisible(false);
				}
				{
					btnAvanzado = new JButton();
					jPanel1.add(btnAvanzado, new AnchorConstraint(458, 291, 18, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					btnAvanzado.setLayout(null);
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
					txtPuertoServidor = new JTextField();
					jPanel1.add(txtPuertoServidor, new AnchorConstraint(100, 20, 986, 131, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtPuertoServidor.setText("2995");
					txtPuertoServidor.setPreferredSize(new java.awt.Dimension(139, 18));
					txtPuertoServidor.setVisible(false);
				}
				{
					lblPuertoServidor = new JLabel();
					jPanel1.add(lblPuertoServidor, new AnchorConstraint(103, 443, 934, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblPuertoServidor.setText("Puerto");
					lblPuertoServidor.setPreferredSize(new java.awt.Dimension(118, 14));
					lblPuertoServidor.setVisible(false);
				}
				{
					txtIPServidor = new JTextField();
					jPanel1.add(txtIPServidor, new AnchorConstraint(75, 20, 678, 131, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtIPServidor.setText("127.0.0.1");
					txtIPServidor.setPreferredSize(new java.awt.Dimension(139, 20));
					txtIPServidor.setLayout(null);
				}
				{
					lblDireccionServidor = new JLabel();
					jPanel1.add(lblDireccionServidor, new AnchorConstraint(78, 443, 654, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblDireccionServidor.setText("Dirección IP del servidor");
					lblDireccionServidor.setPreferredSize(new java.awt.Dimension(118, 14));
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
			lblDireccionServidor.setVisible(false);
			pack();
			this.setSize(296, 140);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void thisWindowClosing(WindowEvent evt) {
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

	//$hide>>$
	
	public void setControlador(ControladorCliente controlador) {
		this.controlador = controlador;
	}
	
	private void btnConectarActionPerformed(ActionEvent evt) {
		try {
			if (txtUsuario.getText().equals("")) {
				txtUsuario.grabFocus();
				throw new CadenaVaciaException("Debe escribir su nombre de usuario.");
			}
			if (txtPassword.getText().equals("")) {
				txtPassword.grabFocus();
				throw new CadenaVaciaException("Debe escribir su contraseña.");
			}
			if (txtIPServidor.getText().equals("")) {
				txtIPServidor.grabFocus();
				throw new CadenaVaciaException("Debe introducir una dirección IP válida.");
			}
			Utilidades.comprobarDireccionIP(txtIPServidor.getText());
			if (txtPuertoServidor.getText().equals("")) {
				txtPuertoServidor.grabFocus();
				throw new CadenaVaciaException("Debe introducir un puerto válido.");
			}
			Utilidades.comprobarEntero(txtPuertoServidor.getText());
			int puerto = Integer.parseInt(txtPuertoServidor.getText());
			controlador.iniciarSesion(txtIPServidor.getText(), puerto, txtUsuario.getText(), new String(txtPassword.getPassword()));
		} catch (SQLException e) {
			Dialogos.mostrarDialogoError(this, "Error en el sistema", e.getMessage());
		} catch (UsuarioIncorrectoException e) {
			Dialogos.mostrarDialogoError(this, "Error al autentificar", e.getMessage());
		} catch (IPInvalidaException e) {
			txtIPServidor.selectAll();
			txtIPServidor.grabFocus();
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
		} catch (CadenaVaciaException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
		} catch (EnteroIncorrectoException e) {
			txtPuertoServidor.selectAll();
			txtPuertoServidor.grabFocus();
			Dialogos.mostrarDialogoError(this, "Error", "El puerto debe ser un entero positivo entre 1024 y 65536.");
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(this, "Error al autentificar", e.getLocalizedMessage());
		}
	}
	
	private void btnAvanzadoActionPerformed(ActionEvent evt) {
		if (btnAvanzado.getText().equals("Avanzado >>")) {
			btnAvanzado.setText("<< Avanzado");
			//Grande [296, 167]
			this.setSize(new java.awt.Dimension(WIDTH, MAX_HEIGHT));
			jPanel1.setSize(new java.awt.Dimension(WIDTH, MAX_HEIGHT));
			jSeparator1.setVisible(true);
			txtIPServidor.setVisible(true);
			lblDireccionServidor.setVisible(true);
			txtPuertoServidor.setVisible(true);
			lblPuertoServidor.setVisible(true);
		}
		else {
			if (btnAvanzado.getText().equals("<< Avanzado")) {
				btnAvanzado.setText("Avanzado >>");
				//Pequeño [296, 140]
				this.setSize(new java.awt.Dimension(WIDTH, MIN_HEIGHT));
				jPanel1.setSize(new java.awt.Dimension(WIDTH, MIN_HEIGHT));
				jSeparator1.setVisible(false);
				txtIPServidor.setVisible(false);
				lblDireccionServidor.setVisible(false);
				txtPuertoServidor.setVisible(false);
				lblPuertoServidor.setVisible(false);
			}
		}
	}

	//$hide<<$
	
}
