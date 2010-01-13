package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.control.ControladorPrincipal;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import java.util.regex.*;

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
public class JFServidorFrontend extends javax.swing.JFrame implements IVentanaLog {
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = -113838536647924014L;
	
	private ControladorPrincipal controlador;
	private JPanel jPanel;
	private JScrollPane scpPanelLog;
	private JLabel lblIPRespaldo;
	private JTextField txtIPBDPrincipal;
	private JTextField txtIPRespaldo;
	private JLabel lblIPBDPrincipal;
	private JLabel lblBarraEstado;
	private JButton btnDesconectar;
	private JLabel lblClientesConectados;
	private JButton btnConectar;
	private JTextArea txtLog;
	
	public JFServidorFrontend() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Servidor Front-End");
			this.setPreferredSize(new java.awt.Dimension(550, 400));
			this.setMinimumSize(new java.awt.Dimension(500, 300));
			this.addWindowListener(new WindowAdapter() { 
				public void windowClosing(WindowEvent e) {    
					System.exit(0);
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
					txtIPRespaldo = new JTextField();
					jPanel.add(txtIPRespaldo, new AnchorConstraint(24, 12, 191, 805, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					txtIPRespaldo.setPreferredSize(new java.awt.Dimension(88, 21));
					txtIPRespaldo.setText("127.0.0.1");
				}
				{
					lblIPBDPrincipal = new JLabel();
					jPanel.add(lblIPBDPrincipal, new AnchorConstraint(8, 111, 94, 606, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					lblIPBDPrincipal.setText("IP BD principal:");
					lblIPBDPrincipal.setPreferredSize(new java.awt.Dimension(92, 14));
				}
				{
					scpPanelLog = new JScrollPane();
					jPanel.add(scpPanelLog, new AnchorConstraint(55, 10, 54, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					scpPanelLog.setPreferredSize(new java.awt.Dimension(522, 212));
					scpPanelLog.setMinimumSize(new java.awt.Dimension(346, 155));
					{
						txtLog = new JTextArea();
						scpPanelLog.setViewportView(txtLog);
						txtLog.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						txtLog.setEditable(false);
						txtLog.setFont(new java.awt.Font("Tahoma",0,12));
						txtLog.setPreferredSize(new java.awt.Dimension(523, 184));
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
							botonDesconectarActionPerformed(evt);
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
							botonConectarActionPerformed(evt);
						}
					});
				}
				{
					txtIPBDPrincipal = new JTextField();
					jPanel.add(txtIPBDPrincipal, new AnchorConstraint(24, 115, 191, 608, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					txtIPBDPrincipal.setPreferredSize(new java.awt.Dimension(88, 21));
					txtIPBDPrincipal.setText("127.0.0.1");
				}
				{
					lblIPRespaldo = new JLabel();
					jPanel.add(lblIPRespaldo, new AnchorConstraint(8, 8, 94, 805, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					lblIPRespaldo.setText("IP Serv. respaldo:");
					lblIPRespaldo.setPreferredSize(new java.awt.Dimension(92, 14));
				}
			}
			pack();
		} catch(Exception e) {
			txtLog.setText(txtLog.getText() + e.toString());
		}
	}
	
	//$hide>>$
	
	public void setControladorPresentacion(ControladorPrincipal controlador) {
		this.controlador = controlador;
	}
	
	private void botonConectarActionPerformed(ActionEvent evt) {
		boolean respaldoValido=false;
		boolean frontValido=true;
		try {
			Pattern pat = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
			
			// Iniciamos el servidor frontend y la conexión con el de respaldo (si las IPs son validas)
			if (!txtIPRespaldo.getText().equals("")) {
				if (!pat.matcher(txtIPRespaldo.getText()).matches()) {
					JOptionPane.showMessageDialog(null, "Formato de IP del servidor de respaldo incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
					respaldoValido=false;
				}
				else
					respaldoValido=true;
			}
			else
				respaldoValido=true;
			if (pat.matcher(txtIPBDPrincipal.getText()).matches()) 
				frontValido=true;
			else {
				JOptionPane.showMessageDialog(null, "Formato de IP del servidor frontend incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
				frontValido=false;
			}
			if (respaldoValido && frontValido){
				controlador.iniciarServidor(txtIPBDPrincipal.getText(), txtIPRespaldo.getText());
				// Cambiamos el estado de la ventana
				btnConectar.setEnabled(false);
				btnDesconectar.setEnabled(true);
				txtIPBDPrincipal.setEditable(false);
				txtIPRespaldo.setEditable(false);
				lblBarraEstado.setText("Servidor preparado.");
			}
		} catch (SQLException e) {
			actualizarTexto("Error: " + e.toString());
		} catch (MalformedURLException e) {
			actualizarTexto("Error: " + e.toString());
		} catch (UnknownHostException e) {
			actualizarTexto("Error: " + e.toString());
		// Si da fallo al conectar, lo desconectamos para hacer el "unexport" y que funcione mas tarde
		} catch(RemoteException e) {
			actualizarTexto("Error: " + e.toString());
			e.printStackTrace();
			try {
				controlador.detenerServidor(txtIPRespaldo.getText());
			} catch (RemoteException e1) {
				actualizarTexto("Error: " + e1.toString());
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				actualizarTexto("Error: " + e1.toString());
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				actualizarTexto("Error: " + e1.toString());
				e1.printStackTrace();
			} catch (NotBoundException e1) {
				actualizarTexto("Error: " + e1.toString());
				e1.printStackTrace();
			} catch (SQLException e1) {
				actualizarTexto("Error: " + e1.toString());
				e1.printStackTrace();
			}
		} catch (NotBoundException e) {
			actualizarTexto("Error: " + e.toString());
		}
	}
	
	private void botonDesconectarActionPerformed(ActionEvent evt) {
		try {
			// Detenemos el servidor frontend y la conexión con el de respaldo
			controlador.detenerServidor(txtIPRespaldo.getText());
			// Cambiamos el estado de la ventana
			btnDesconectar.setEnabled(false);
			btnConectar.setEnabled(true);
			txtIPBDPrincipal.setEditable(true);
			txtIPRespaldo.setEditable(true);
			lblBarraEstado.setText("Servidor desconectado.");
		} catch(SQLException e) {
			actualizarTexto("Error: " + e.toString());
		} catch(RemoteException e) {
			actualizarTexto("Error: " + e.toString());
		} catch(MalformedURLException e) {
			actualizarTexto("Error: " + e.toString());
		} catch(UnknownHostException e) {
			actualizarTexto("Error: " + e.toString());
		} catch(NotBoundException e) {
			actualizarTexto("Error: " + e.toString());
		}
	}

	public void actualizarTexto(String mensaje) {
		txtLog.setText(txtLog.getText() + mensaje + "\n");	
	}

	//$hide<<$
	
}
