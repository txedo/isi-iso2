package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import comunicaciones.ConexionBDRespaldo;

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
public class JFServidorRespaldo extends javax.swing.JFrame implements IVentana {
	
	private ConexionBDRespaldo conexion;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JLabel jlbIPRespaldo;
	private JTextField txtIPRespaldo;
	private JLabel lblBarraEstado;
	private JButton btnDesconectar;
	private JButton btnConectar;
	private JTextArea textLog;
	
	public JFServidorRespaldo() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Servidor Respaldo");
			//this.setAlwaysOnTop(true);
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
				jPanel1.setPreferredSize(new java.awt.Dimension(374, 266));
				{
					jlbIPRespaldo = new JLabel();
					jPanel1.add(jlbIPRespaldo, new AnchorConstraint(8, 8, 94, 805, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					jlbIPRespaldo.setText("IP Respaldo:");
					jlbIPRespaldo.setPreferredSize(new java.awt.Dimension(92, 14));
				}
				{
					txtIPRespaldo = new JTextField();
					jPanel1.add(txtIPRespaldo, new AnchorConstraint(24, 12, 191, 805, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					txtIPRespaldo.setPreferredSize(new java.awt.Dimension(88, 21));
					txtIPRespaldo.setText("127.0.0.1");
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanel1.add(jScrollPane1, new AnchorConstraint(55, 10, 23, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
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
					lblBarraEstado = new JLabel();
					jPanel1.add(lblBarraEstado, new AnchorConstraint(937, 10, 3, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblBarraEstado.setText("Servidor desconectado.");
					lblBarraEstado.setPreferredSize(new java.awt.Dimension(346, 14));
				}
				{
					btnDesconectar = new JButton();
					jPanel1.add(btnDesconectar, new AnchorConstraint(13, 643, 124, 140, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
					jPanel1.add(btnConectar, new AnchorConstraint(13, 322, 165, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnConectar.setText("Conectar");
					btnConectar.setPreferredSize(new java.awt.Dimension(110, 30));
					btnConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							botonConectarActionPerformed(evt);
						}
					});
				}

			pack();
			
		} catch (Exception e) {
			this.actualizarTexto(e.toString());
		}
	}
	
	public void setConexion(ConexionBDRespaldo c) {
		this.conexion = c;
	}
	
	private void botonConectarActionPerformed(ActionEvent evt) {
		try {
			// Iniciamos el servidor de respaldo
			conexion.conectar(txtIPRespaldo.getText());
			// Cambiamos el estado de la ventana
			btnConectar.setEnabled(false);
			btnDesconectar.setEnabled(true);
			txtIPRespaldo.setEditable(false);
			lblBarraEstado.setText("Servidor preparado.");
		} catch (MalformedURLException e) {
			actualizarTexto("Error: " + e.toString());
		} catch (RemoteException e) {
			actualizarTexto("Error: " + e.toString());
		}
	}
	
	private void botonDesconectarActionPerformed(ActionEvent evt) {
		try {
			// Detenemos el servidor de respaldo
			conexion.desconectar(txtIPRespaldo.getText());
			// Cambiamos el estado de la ventana
			btnDesconectar.setEnabled(false);
			btnConectar.setEnabled(true);
			txtIPRespaldo.setEditable(true);
			lblBarraEstado.setText("Servidor desconectado.");
		} catch (RemoteException e) {
			actualizarTexto("Error: " + e.toString());
		} catch (MalformedURLException e) {
			actualizarTexto("Error: " + e.toString());
		} catch (NotBoundException e) {
			actualizarTexto("Error: " + e.toString());
		}
	}

	public void actualizarTexto(String mensaje) {
		textLog.setText(textLog.getText() + mensaje + "\n");	
	}

}
