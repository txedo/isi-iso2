package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.control.ControladorCliente;
import excepciones.ContraseñaIncorrectaException;
import excepciones.IPInvalidaException;
import excepciones.LoginIncorrectoException;
import excepciones.PuertoInvalidoException;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.Validacion;

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
	
	private final int WIDTH = 285;
	private final int MAX_HEIGHT = 272;
	private final int MIN_HEIGHT = 175;
	
	private ControladorCliente controlador;
	private JLabel lblUsuario;
	private JTextField txtUsuario;
	private JLabel lblDireccionServidor;
	private JTextField txtPuertoServidor;
	private JLabel lblPuertoServidor;
	private JTextField txtDireccionServidor;
	private JPanel pnlPanel;
	private JButton btnAvanzado;
	private JButton btnConectar;
	private JPasswordField txtPassword;
	private JLabel lblPassword;
	private JPanel pnlDatosUsuario;
	private JPanel pnlDatosServidor;

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
			this.setPreferredSize(new java.awt.Dimension(WIDTH, MIN_HEIGHT));
			this.setMinimumSize(new java.awt.Dimension(WIDTH, MIN_HEIGHT));
			this.setMaximumSize(new java.awt.Dimension(WIDTH, MAX_HEIGHT));
			this.setResizable(false);
			setLocationRelativeTo(null);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					thisWindowClosing(evt);
				}
			});
			{
				pnlPanel = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(pnlPanel, BorderLayout.CENTER);
				pnlPanel.setLayout(jPanel1Layout);
				pnlPanel.setName("pnlPanel");
				{
					pnlDatosServidor = new JPanel();
					pnlDatosServidor.setLayout(null);
					pnlPanel.add(pnlDatosServidor, new AnchorConstraint(104, 905, 1004, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					pnlDatosServidor.setBorder(BorderFactory.createTitledBorder("Datos del servidor"));
					pnlDatosServidor.setPreferredSize(new java.awt.Dimension(259, 89));
					pnlDatosServidor.setVisible(false);
					pnlDatosServidor.setName("pnlDatosServidor");
					{
						txtPuertoServidor = new JTextField();
						pnlDatosServidor.add(txtPuertoServidor, new AnchorConstraint(580, 950, 846, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtPuertoServidor.setText("2995");
						txtPuertoServidor.setBounds(115, 53, 125, 20);
						txtPuertoServidor.setName("txtPuertoServidor");
					}
					{
						lblPuertoServidor = new JLabel();
						pnlDatosServidor.add(lblPuertoServidor, new AnchorConstraint(593, 483, 780, 38, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						lblPuertoServidor.setText("Puerto");
						lblPuertoServidor.setBounds(13, 57, 91, 14);
						lblPuertoServidor.setName("lblPuertoServidor");
					}
					{
						txtDireccionServidor = new JTextField();
						pnlDatosServidor.add(txtDireccionServidor, new AnchorConstraint(246, 950, 513, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtDireccionServidor.setText("127.0.0.1");
						txtDireccionServidor.setBounds(115, 24, 125, 20);
						txtDireccionServidor.setLayout(null);
						txtDireccionServidor.setName("txtDireccionServidor");
					}
					{
						lblDireccionServidor = new JLabel();
						pnlDatosServidor.add(lblDireccionServidor, new AnchorConstraint(286, 483, 473, 38, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						lblDireccionServidor.setBounds(13, 28, 91, 14);
						lblDireccionServidor.setText("Dirección IP");
						lblDireccionServidor.setName("lblDireccionServidor");
					}
				}
				{
					pnlDatosUsuario = new JPanel();
					pnlDatosUsuario.setLayout(null);
					pnlPanel.add(pnlDatosUsuario, new AnchorConstraint(10, 905, 630, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					pnlDatosUsuario.setPreferredSize(new java.awt.Dimension(259, 86));
					pnlDatosUsuario.setBorder(BorderFactory.createTitledBorder("Datos de usuario"));
					pnlDatosUsuario.setName("pnlDatosUsuario");
					{
						txtPassword = new JPasswordField();
						pnlDatosUsuario.add(txtPassword, new AnchorConstraint(580, 950, 846, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtPassword.setLayout(null);
						txtPassword.setBounds(115, 50, 125, 20);
						txtPassword.setName("txtPassword");
					}
					{
						lblPassword = new JLabel();
						pnlDatosUsuario.add(lblPassword, new AnchorConstraint(593, 483, 780, 38, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						lblPassword.setText("Contraseña");
						lblPassword.setBounds(13, 54, 85, 14);
						lblPassword.setName("lblPassword");
					}
					{
						txtUsuario = new JTextField();
						pnlDatosUsuario.add(txtUsuario, new AnchorConstraint(246, 950, 513, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtUsuario.setLayout(null);
						txtUsuario.setBounds(115, 22, 125, 20);
						txtUsuario.setName("txtUsuario");
					}
					{
						lblUsuario = new JLabel();
						pnlDatosUsuario.add(lblUsuario, new AnchorConstraint(286, 483, 473, 38, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						lblUsuario.setText("Usuario");
						lblUsuario.setBounds(13, 26, 85, 14);
						lblUsuario.setName("lblUsuario");
					}
				}
				{
					btnAvanzado = new JButton();
					pnlPanel.add(btnAvanzado, new AnchorConstraint(458, 291, 11, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					btnAvanzado.setLayout(null);
					btnAvanzado.setText("Avanzado >>");
					btnAvanzado.setPreferredSize(new java.awt.Dimension(103, 30));
					btnAvanzado.setRequestFocusEnabled(false);
					btnAvanzado.setName("btnAvanzado");
					btnAvanzado.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAvanzadoActionPerformed(evt);
						}
					});
				}
				{
					btnConectar = new JButton();
					pnlPanel.add(btnConectar, new AnchorConstraint(450, 10, 11, 698, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnConectar.setDefaultCapable(true);
					getRootPane().setDefaultButton(btnConectar);
					btnConectar.setText("Iniciar sesión");
					btnConectar.setSelected(true);
					btnConectar.setPreferredSize(new java.awt.Dimension(107, 30));
					btnConectar.setName("btnConectar");
					btnConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnConectarActionPerformed(evt);
						}
					});
				}
			}
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$

	private void thisWindowClosing(WindowEvent evt) {
		try {
			controlador.cerrarControlador();
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
		} catch(MalformedURLException e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
		} catch(NotBoundException e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
		}
	}
	
	public void setControlador(ControladorCliente controlador) {
		this.controlador = controlador;
	}
	
	private void btnConectarActionPerformed(ActionEvent evt) {
		try {
			
			// Comprobamos los campos de la ventana
			Validacion.comprobarUsuario(txtUsuario.getText().trim());
			//TODO:quitado temporalmente... Validacion.comprobarContraseña(new String(txtPassword.getPassword()));
			Validacion.comprobarDireccionIP(txtDireccionServidor.getText().trim());
			Validacion.comprobarPuerto(txtPuertoServidor.getText().trim());
			
			int puerto = Integer.parseInt(txtPuertoServidor.getText().trim());
			controlador.iniciarSesion(txtDireccionServidor.getText(), puerto, txtUsuario.getText(), new String(txtPassword.getPassword()));
		
		} catch(UsuarioIncorrectoException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());

		} catch(LoginIncorrectoException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
			txtUsuario.selectAll();
			txtUsuario.grabFocus();
		} catch(ContraseñaIncorrectaException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
			txtPassword.selectAll();
			txtPassword.grabFocus();
		} catch(IPInvalidaException e) {
			Dialogos.mostrarDialogoError(this, "Error", "La dirección IP del servidor tiene un formato incorrecto.");
			txtDireccionServidor.selectAll();
			txtDireccionServidor.grabFocus();
		} catch(PuertoInvalidoException e) {
			Dialogos.mostrarDialogoError(this, "Error", "El puerto del servidor tiene un formato incorrecto.");
			txtPuertoServidor.selectAll();
			txtPuertoServidor.grabFocus();

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getLocalizedMessage());
		}
	}
	
	private void btnAvanzadoActionPerformed(ActionEvent evt) {
		if(btnAvanzado.getText().equals("Avanzado >>")) {
			btnAvanzado.setText("<< Avanzado");
			this.setSize(new java.awt.Dimension(WIDTH, MAX_HEIGHT));
			pnlPanel.setSize(new java.awt.Dimension(WIDTH, MAX_HEIGHT));
			pnlDatosServidor.setVisible(true);
			txtDireccionServidor.setVisible(true);
			lblDireccionServidor.setVisible(true);
			txtPuertoServidor.setVisible(true);
			lblPuertoServidor.setVisible(true);
		} else {
			if(btnAvanzado.getText().equals("<< Avanzado")) {
				btnAvanzado.setText("Avanzado >>");
				this.setSize(new java.awt.Dimension(WIDTH, MIN_HEIGHT));
				pnlPanel.setSize(new java.awt.Dimension(WIDTH, MIN_HEIGHT));
				pnlDatosServidor.setVisible(false);
				txtDireccionServidor.setVisible(false);
				lblDireccionServidor.setVisible(false);
				txtPuertoServidor.setVisible(false);
				lblPuertoServidor.setVisible(false);
			}
		}
	}

	//$hide<<$
	
}
