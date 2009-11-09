package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import comunicaciones.ServidorFrontend;

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
public class JFServidorFrontend extends javax.swing.JFrame {
	private ServidorFrontend servidorFE;
	private JPanel jPanel1;
	private JLabel labelBarraEstado;
	private JButton botonDesconectar;
	private JButton botonConectar;
	private JTextArea jTextArea1;

	
	public JFServidorFrontend(ServidorFrontend serv) {
		super();
		initGUI();
		this.servidorFE = serv;
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Servidor Front-End");
			this.setAlwaysOnTop(true);
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
<<<<<<< .mine
				jPanel1.setPreferredSize(new java.awt.Dimension(374, 266));
=======
				jPanel1.setPreferredSize(new java.awt.Dimension(374, 252));
>>>>>>> .r55
				{
					labelBarraEstado = new JLabel();
					jPanel1.add(labelBarraEstado, new AnchorConstraint(937, 10, 3, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					labelBarraEstado.setText("Servidor desconectado...");
					labelBarraEstado.setPreferredSize(new java.awt.Dimension(354, 14));
				}
				{
					botonDesconectar = new JButton();
<<<<<<< .mine
					jPanel1.add(botonDesconectar, new AnchorConstraint(10, 643, 124, 140, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
=======
					jPanel1.add(botonDesconectar, new AnchorConstraint(12, 680, 173, 134, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
>>>>>>> .r55
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
<<<<<<< .mine
					jPanel1.add(botonConectar, new AnchorConstraint(10, 322, 165, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
=======
					jPanel1.add(botonConectar, new AnchorConstraint(12, 330, 173, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
>>>>>>> .r55
					botonConectar.setText("Conectar");
					botonConectar.setPreferredSize(new java.awt.Dimension(110, 30));
<<<<<<< .mine
=======
					botonConectar.setAutoscrolls(true);
>>>>>>> .r55
					botonConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							botonConectarActionPerformed(evt);
						}
					});
				}
				{
					jTextArea1 = new JTextArea();
<<<<<<< .mine
					jPanel1.add(jTextArea1, new AnchorConstraint(56, 10, 28, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					jTextArea1.setPreferredSize(new java.awt.Dimension(354, 182));
=======
					jPanel1.add(jTextArea1, new AnchorConstraint(54, 12, 11, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
>>>>>>> .r55
					jTextArea1.setEditable(false);
					jTextArea1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					jTextArea1.setPreferredSize(new java.awt.Dimension(349, 187));
				}

			pack();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void botonConectarActionPerformed(ActionEvent evt) {
		try {
			this.servidorFE.conectar();
			this.botonConectar.setEnabled(false);
			this.botonDesconectar.setEnabled(true);
			this.labelBarraEstado.setText("Servidor conectado...");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void botonDesconectarActionPerformed(ActionEvent evt) {
		try {
			this.servidorFE.desconectar();
			this.botonDesconectar.setEnabled(false);
			this.botonConectar.setEnabled(true);
			this.labelBarraEstado.setText("Servidor desconectado...");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
