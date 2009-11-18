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
	private JScrollPane jScrollPane1;
	private JLabel labelBarraEstado;
	private JButton botonDesconectar;
	private JButton botonConectar;
	private JTextArea textLog;
	
	public JFServidorFrontend() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Servidor Front-End");
			//this.setAlwaysOnTop(true);
			this.setPreferredSize(new java.awt.Dimension(374, 266));
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
				jPanel1.setPreferredSize(new java.awt.Dimension(374, 266));
				{
					jScrollPane1 = new JScrollPane();
					jPanel1.add(jScrollPane1, new AnchorConstraint(62, 10, 23, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					jScrollPane1.setPreferredSize(new java.awt.Dimension(346, 154));
					jScrollPane1.setMinimumSize(new java.awt.Dimension(346, 155));
					{
						textLog = new JTextArea();
						jScrollPane1.setViewportView(textLog);
						textLog.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						textLog.setEditable(false);
					}
				}
				{
					labelBarraEstado = new JLabel();
					jPanel1.add(labelBarraEstado, new AnchorConstraint(937, 10, 3, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					labelBarraEstado.setText("Servidor desconectado.");
					labelBarraEstado.setPreferredSize(new java.awt.Dimension(346, 14));
				}
				{
					botonDesconectar = new JButton();
					jPanel1.add(botonDesconectar, new AnchorConstraint(13, 643, 124, 140, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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

			pack();
			
		} catch (Exception e) {
			textLog.setText(textLog.getText() + e.toString());
		}
	}
	
	public void setControladorPresentacion(ControladorPresentacion controlador) {
		this.controlador = controlador;
	}
	
	private void botonConectarActionPerformed(ActionEvent evt) {
		try {
			this.controlador.getServidor().conectar();
			this.botonConectar.setEnabled(false);
			this.botonDesconectar.setEnabled(true);
			this.labelBarraEstado.setText("Servidor preparado.");
		} catch (MalformedURLException e) {
			textLog.setText(textLog.getText() + e.toString());
		} catch (RemoteException e) {
			textLog.setText(textLog.getText() + e.toString());
		}
	}
	
	private void botonDesconectarActionPerformed(ActionEvent evt) {
		try {
			this.controlador.getServidor().desconectar();
			this.botonDesconectar.setEnabled(false);
			this.botonConectar.setEnabled(true);
			this.labelBarraEstado.setText("Servidor desconectado.");
		} catch (RemoteException e) {
			textLog.setText(textLog.getText() + e.toString());
		} catch (MalformedURLException e) {
			textLog.setText(textLog.getText() + e.toString());
		} catch (NotBoundException e) {
			textLog.setText(textLog.getText() + e.toString());
		}
	}

	@Override
	public void actualizarTexto(String mensaje) {
		textLog.setText(textLog.getText() + mensaje);	
	}

}