package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Validacion;
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
import javax.swing.BorderFactory;
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
	private JPanel JPDatosUsuario;
	private JPanel JPDatosConexion;

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
				jPanel1 = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(jPanel1Layout);
				{
					JPDatosConexion = new JPanel();
					JPDatosConexion.setLayout(null);
					jPanel1.add(JPDatosConexion, new AnchorConstraint(104, 905, 1004, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					JPDatosConexion.setBorder(BorderFactory.createTitledBorder("Datos del servidor"));
					JPDatosConexion.setPreferredSize(new java.awt.Dimension(259, 89));
					JPDatosConexion.setVisible(false);
					{
						txtPuertoServidor = new JTextField();
						JPDatosConexion.add(txtPuertoServidor, new AnchorConstraint(580, 950, 846, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtPuertoServidor.setText("2995");
						txtPuertoServidor.setBounds(115, 53, 125, 20);
					}
					{
						lblPuertoServidor = new JLabel();
						JPDatosConexion.add(lblPuertoServidor, new AnchorConstraint(593, 483, 780, 38, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						lblPuertoServidor.setText("Puerto");
						lblPuertoServidor.setBounds(13, 57, 91, 14);
					}
					{
						txtIPServidor = new JTextField();
						JPDatosConexion.add(txtIPServidor, new AnchorConstraint(246, 950, 513, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtIPServidor.setText("127.0.0.1");
						txtIPServidor.setLayout(null);
					}
					{
						lblDireccionServidor = new JLabel();
						JPDatosConexion.add(lblDireccionServidor, new AnchorConstraint(286, 483, 473, 38, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						lblDireccionServidor.setText("Dirección IP");
					}
				}
				{
					JPDatosUsuario = new JPanel();
					JPDatosUsuario.setLayout(null);
					jPanel1.add(JPDatosUsuario, new AnchorConstraint(10, 905, 630, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					JPDatosUsuario.setPreferredSize(new java.awt.Dimension(259, 86));
					JPDatosUsuario.setBorder(BorderFactory.createTitledBorder("Datos de usuario"));
					{
						txtPassword = new JPasswordField();
						JPDatosUsuario.add(txtPassword, new AnchorConstraint(580, 950, 846, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtPassword.setLayout(null);
						txtPassword.setBounds(115, 50, 125, 20);
					}
					{
						jLabel3 = new JLabel();
						JPDatosUsuario.add(jLabel3, new AnchorConstraint(593, 483, 780, 38, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						jLabel3.setText("Contraseña");
						jLabel3.setBounds(13, 54, 85, 14);
					}
					{
						txtUsuario = new JTextField();
						JPDatosUsuario.add(txtUsuario, new AnchorConstraint(246, 950, 513, 487, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtUsuario.setLayout(null);
						txtUsuario.setBounds(115, 22, 125, 20);
					}
					{
						jLabel2 = new JLabel();
						JPDatosUsuario.add(jLabel2, new AnchorConstraint(286, 483, 473, 38, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						jLabel2.setText("Usuario");
						jLabel2.setBounds(13, 26, 85, 14);
					}
				}
				{
					btnAvanzado = new JButton();
					jPanel1.add(btnAvanzado, new AnchorConstraint(458, 291, 11, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					btnAvanzado.setLayout(null);
					btnAvanzado.setText("Avanzado >>");
					btnAvanzado.setPreferredSize(new java.awt.Dimension(103, 30));
					btnAvanzado.setRequestFocusEnabled(false);
					btnAvanzado.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAvanzadoActionPerformed(evt);
						}
					});
				}
				{
					btnConectar = new JButton();
					jPanel1.add(btnConectar, new AnchorConstraint(450, 10, 11, 698, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnConectar.setDefaultCapable(true);
					getRootPane().setDefaultButton(btnConectar);
					btnConectar.setText("Iniciar sesión");
					btnConectar.setSelected(true);
					btnConectar.setPreferredSize(new java.awt.Dimension(107, 30));
					btnConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnConectarActionPerformed(evt);
						}
					});
				}
			}
			txtIPServidor.setBounds(115, 24, 125, 20);
			lblDireccionServidor.setBounds(13, 28, 91, 14);
			pack();
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
			Validacion.comprobarDireccionIP(txtIPServidor.getText());
			if (txtPuertoServidor.getText().equals("")) {
				txtPuertoServidor.grabFocus();
				throw new CadenaVaciaException("Debe introducir un puerto válido.");
			}
			Validacion.comprobarEntero(txtPuertoServidor.getText());
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
		if(btnAvanzado.getText().equals("Avanzado >>")) {
			btnAvanzado.setText("<< Avanzado");
			this.setSize(new java.awt.Dimension(WIDTH, MAX_HEIGHT));
			jPanel1.setSize(new java.awt.Dimension(WIDTH, MAX_HEIGHT));
			JPDatosConexion.setVisible(true);
			txtIPServidor.setVisible(true);
			lblDireccionServidor.setVisible(true);
			txtPuertoServidor.setVisible(true);
			lblPuertoServidor.setVisible(true);
		} else {
			if(btnAvanzado.getText().equals("<< Avanzado")) {
				btnAvanzado.setText("Avanzado >>");
				this.setSize(new java.awt.Dimension(WIDTH, MIN_HEIGHT));
				jPanel1.setSize(new java.awt.Dimension(WIDTH, MIN_HEIGHT));
				JPDatosConexion.setVisible(false);
				txtIPServidor.setVisible(false);
				lblDireccionServidor.setVisible(false);
				txtPuertoServidor.setVisible(false);
				lblPuertoServidor.setVisible(false);
			}
		}
	}

	//$hide<<$
	
}
