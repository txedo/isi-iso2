package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.ControladorPresentacion;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
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
public class JFServidorFrontend extends javax.swing.JFrame implements IVentana {
	
	private ControladorPresentacion controlador;
	private JPanel jPanel1;
	private JScrollPane scpPanelLog;
	private JLabel jLabel1;
	private JTextField txtIPFrontend;
	private JTextField txtIPRespaldo;
	private JLabel lblIP;
	private JLabel labelBarraEstado;
	private JButton botonDesconectar;
	private JButton botonConectar;
	private JTextArea txtLog;
	
	public JFServidorFrontend() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Servidor Front-End");
			this.setPreferredSize(new java.awt.Dimension(500, 266));
			this.setMinimumSize(new java.awt.Dimension(374, 266));

			this.addWindowListener(new java.awt.event.WindowAdapter() { 
				public void windowClosing(java.awt.event.WindowEvent e) {    
					System.exit(0);
				}
			});

			jPanel1 = new JPanel();
			AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1.setPreferredSize(new java.awt.Dimension(527, 237));
				{
					txtIPRespaldo = new JTextField();
					jPanel1.add(txtIPRespaldo, new AnchorConstraint(24, 12, 191, 805, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					txtIPRespaldo.setPreferredSize(new java.awt.Dimension(88, 21));
					txtIPRespaldo.setText("127.0.0.1");
				}
				{
					lblIP = new JLabel();
					jPanel1.add(lblIP, new AnchorConstraint(8, 111, 94, 606, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					lblIP.setText("IP Frontend:");
					lblIP.setPreferredSize(new java.awt.Dimension(92, 14));
				}
				{
					scpPanelLog = new JScrollPane();
					jPanel1.add(scpPanelLog, new AnchorConstraint(55, 10, 23, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					scpPanelLog.setPreferredSize(new java.awt.Dimension(346, 154));
					scpPanelLog.setMinimumSize(new java.awt.Dimension(346, 155));
					{
						txtLog = new JTextArea();
						scpPanelLog.setViewportView(txtLog);
						txtLog.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						txtLog.setEditable(false);
					}
				}
				{
					labelBarraEstado = new JLabel();
					jPanel1.add(labelBarraEstado, new AnchorConstraint(937, 10, 5, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					labelBarraEstado.setText("Servidor desconectado.");
					labelBarraEstado.setPreferredSize(new java.awt.Dimension(396, 14));
				}
				{
					botonDesconectar = new JButton();
					jPanel1.add(botonDesconectar, new AnchorConstraint(13, 643, 124, 133, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					botonDesconectar.setText("Desconectar");
					botonDesconectar.setPreferredSize(new java.awt.Dimension(116, 30));
					botonDesconectar.setEnabled(false);
					botonDesconectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							botonDesconectarActionPerformed(evt);
						}
					});
				}
				{
					botonConectar = new JButton();
					jPanel1.add(botonConectar, new AnchorConstraint(13, 322, 165, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					botonConectar.setText("Conectar");
					botonConectar.setPreferredSize(new java.awt.Dimension(110, 30));
					botonConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							botonConectarActionPerformed(evt);
						}
					});
				}
				{
					txtIPFrontend = new JTextField();
					jPanel1.add(txtIPFrontend, new AnchorConstraint(24, 115, 191, 608, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					txtIPFrontend.setPreferredSize(new java.awt.Dimension(88, 21));
					txtIPFrontend.setText("127.0.0.1");
				}
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1, new AnchorConstraint(8, 8, 94, 805, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					jLabel1.setText("IP Respaldo:");
					jLabel1.setPreferredSize(new java.awt.Dimension(92, 14));
				}

			pack();
			
		} catch (Exception e) {
			txtLog.setText(txtLog.getText() + e.toString());
		}
	}
	
	public void setControladorPresentacion(ControladorPresentacion controlador) {
		this.controlador = controlador;
	}
	
	private void botonConectarActionPerformed(ActionEvent evt) {
		try {
			this.controlador.iniciarServidor(txtIPFrontend.getText(), txtIPRespaldo.getText());
			// Cambiamos el estado de la ventana
			botonConectar.setEnabled(false);
			botonDesconectar.setEnabled(true);
			txtIPFrontend.setEditable(false);
			txtIPRespaldo.setEditable(false);
			labelBarraEstado.setText("Servidor preparado.");
		} catch (MalformedURLException e) {
			txtLog.setText(txtLog.getText() + e.toString());
		} catch (RemoteException e) {
			txtLog.setText(txtLog.getText() + e.toString());
		}
	}
	
	private void botonDesconectarActionPerformed(ActionEvent evt) {
		try {
			this.controlador.iniciarServidor(txtIPFrontend.getText(), txtIPRespaldo.getText());
			// Cambiamos el estado de la ventana
			botonDesconectar.setEnabled(false);
			botonConectar.setEnabled(true);
			txtIPFrontend.setEditable(true);
			txtIPRespaldo.setEditable(true);
			labelBarraEstado.setText("Servidor desconectado.");
		} catch (RemoteException e) {
			txtLog.setText(txtLog.getText() + e.toString());
		} catch (MalformedURLException e) {
			txtLog.setText(txtLog.getText() + e.toString());
		} catch (NotBoundException e) {
			txtLog.setText(txtLog.getText() + e.toString());
		}
	}

	@Override
	public void actualizarTexto(String mensaje) {
		txtLog.setText(txtLog.getText() + mensaje);	
	}

}
