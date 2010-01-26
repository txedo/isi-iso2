package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.control.ControladorRespaldo;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

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
public class JFServidorRespaldo extends javax.swing.JFrame implements IVentanaEstado {
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = -3739906711082199809L;
	
	private ControladorRespaldo controlador;
	private JPanel jPanel;
	private JScrollPane scpPanelLog;
	private JLabel lblIPBDRespaldo;
	private JTextField txtIPBDRespaldo;
	private JLabel lblBarraEstado;
	private JButton btnDesconectar;
	private JButton btnConectar;
	private JTextArea textLog;
	private JLabel lblClientesConectados;
	
	public JFServidorRespaldo() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setTitle("Servidor Respaldo");
			this.setPreferredSize(new java.awt.Dimension(550, 400));
			this.setMinimumSize(new java.awt.Dimension(500, 300));
			this.addWindowListener(new WindowAdapter() { 
				public void windowClosing(WindowEvent evt) {    
					thisWindowClosing(evt);
				}
			});
			{
				jPanel = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel, BorderLayout.CENTER);
				jPanel.setLayout(jPanel1Layout);
				jPanel.setPreferredSize(new java.awt.Dimension(542, 327));
				{
					lblClientesConectados = new JLabel();
					jPanel.add(lblClientesConectados, new AnchorConstraint(855, 10, 30, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblClientesConectados.setText("0 clientes conectados.");
					lblClientesConectados.setPreferredSize(new java.awt.Dimension(522, 16));
				}
				{
					lblIPBDRespaldo = new JLabel();
					jPanel.add(lblIPBDRespaldo, new AnchorConstraint(8, 8, 94, 805, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					lblIPBDRespaldo.setText("IP BD respaldo:");
					lblIPBDRespaldo.setPreferredSize(new java.awt.Dimension(92, 14));
				}
				{
					txtIPBDRespaldo = new JTextField();
					jPanel.add(txtIPBDRespaldo, new AnchorConstraint(24, 12, 191, 805, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					txtIPBDRespaldo.setPreferredSize(new java.awt.Dimension(88, 21));
					txtIPBDRespaldo.setText("127.0.0.1");
				}
				{
					scpPanelLog = new JScrollPane();
					jPanel.add(scpPanelLog, new AnchorConstraint(55, 10, 54, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					scpPanelLog.setPreferredSize(new java.awt.Dimension(522, 212));
					scpPanelLog.setMinimumSize(new java.awt.Dimension(346, 155));
					{
						textLog = new JTextArea();
						scpPanelLog.setViewportView(textLog);
						textLog.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						textLog.setEditable(false);
						textLog.setFont(new java.awt.Font("Tahoma",0,12));
						textLog.setPreferredSize(new java.awt.Dimension(524, 263));
					}
				}
				{
					lblBarraEstado = new JLabel();
					jPanel.add(lblBarraEstado, new AnchorConstraint(937, 10, 11, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblBarraEstado.setText("Servidor desconectado.");
					lblBarraEstado.setPreferredSize(new java.awt.Dimension(522, 14));
				}
				{
					btnDesconectar = new JButton();
					jPanel.add(btnDesconectar, new AnchorConstraint(13, 643, 124, 133, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnDesconectar.setText("Desconectar");
					btnDesconectar.setPreferredSize(new java.awt.Dimension(116, 30));
					btnDesconectar.setEnabled(false);
					btnDesconectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnDesconectarActionPerformed(evt);
						}
					});
				}
				{
					btnConectar = new JButton();
					jPanel.add(btnConectar, new AnchorConstraint(13, 322, 165, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnConectar.setText("Conectar");
					btnConectar.setPreferredSize(new java.awt.Dimension(110, 30));
					btnConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnConectarActionPerformed(evt);
						}
					});
				}
			}
			pack();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	public void setControlador(ControladorRespaldo controlador) {
		this.controlador = controlador;
	}
	
	private void btnConectarActionPerformed(ActionEvent evt) {
		Pattern patronIP;
		boolean ipValida;
		
		// Creamos un patrón que define las IPs válidas
		patronIP = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + 
				"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
		
		// Comprobamos si la IP de la BD de respaldo es válida
		if(!patronIP.matcher(txtIPBDRespaldo.getText()).matches()) {
			Dialogos.mostrarDialogoError(this, "Error", "El formato de la IP del servidor de respaldo es incorrecto.");
			txtIPBDRespaldo.selectAll();
			txtIPBDRespaldo.grabFocus();
			ipValida = false;
		} else {
			ipValida = true;
		}
		
		// Si la IP es válida, conectamos el servidor de respaldo
		if(ipValida) {
			activarServidor();
		}	
	}
	
	private void btnDesconectarActionPerformed(ActionEvent evt) {
		desactivarServidor();
	}

	private void thisWindowClosing(WindowEvent evt) {
		boolean salir;
		
		// Si el servidor está activo, preguntamos antes de salir
		salir = false;
		if(controlador.getServidorActivo()) {
			if(Dialogos.mostrarDialogoPregunta(this, "Aviso", "Si cierras el servidor de respaldo, se desconectará automáticamente. ¿Realmente quieres salir?")) {
				if(desactivarServidor()) {
					salir = true;
				}
			}
		} else {
			salir = true;
		}
		
		if(salir) {
			setVisible(false);
			dispose();
			System.exit(0);
		}
	}
	
	private boolean activarServidor() {
		boolean ok;
		
		ok = false;
		try {
			// Iniciamos el servidor frontend y la conexión con el de respaldo
			controlador.iniciarServidorRespaldo(txtIPBDRespaldo.getText());
			// Cambiamos el estado de la ventana
			btnConectar.setEnabled(false);
			btnDesconectar.setEnabled(true);
			txtIPBDRespaldo.setEditable(false);
			lblBarraEstado.setText("Servidor preparado.");
			ok = true;
		} catch(SQLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(MalformedURLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(UnknownHostException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch (NotBoundException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(RemoteException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
			// Si se produce un fallo de RMI al conectar el
			// servidor, lo desconectamos para hacer el "unexport"
			// y que se pueda conectar de nuevo más tarde
			desactivarServidor();
		}
		
		return ok;
	}
	
	private boolean desactivarServidor() {
		boolean ok;
		
		ok = false;
		try {
			// Detenemos el servidor de respaldo
			controlador.detenerServidorRespaldo();
			// Cambiamos el estado de la ventana
			btnDesconectar.setEnabled(false);
			btnConectar.setEnabled(true);
			txtIPBDRespaldo.setEditable(true);
			lblBarraEstado.setText("Servidor desconectado.");
			ok = true;
		} catch(SQLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(RemoteException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(MalformedURLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(UnknownHostException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		}
		
		return ok;
	}
	
	public void ponerMensaje(String mensaje) {
		textLog.setText(textLog.getText() + mensaje + "\n");	
	}

	public void actualizarClientesEscuchando(int numeroClientes) {
		if(numeroClientes == 1) {
			lblClientesConectados.setText(numeroClientes + " cliente conectado.");
		} else {
			lblClientesConectados.setText(numeroClientes + " clientes conectados.");
		}
	}
	
	//$hide<<$
	
}
