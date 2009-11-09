package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
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
	private JPanel jPanel1;
	private JButton botonDesconectar;
	private JButton botonConectar;
	private JTextArea jTextArea1;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFServidorFrontend inst = new JFServidorFrontend();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public JFServidorFrontend() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Servidor Front-End");
			this.setAlwaysOnTop(true);

			jPanel1 = new JPanel();
			AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1.setPreferredSize(new java.awt.Dimension(374, 252));
				{
					botonDesconectar = new JButton();
					jPanel1.add(botonDesconectar, new AnchorConstraint(12, 680, 173, 134, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
					jPanel1.add(botonConectar, new AnchorConstraint(12, 330, 173, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					botonConectar.setText("Conectar");
					botonConectar.setPreferredSize(new java.awt.Dimension(110, 30));
					botonConectar.setAutoscrolls(true);
					botonConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							botonConectarActionPerformed(evt);
						}
					});
				}
				{
					jTextArea1 = new JTextArea();
					jPanel1.add(jTextArea1, new AnchorConstraint(54, 12, 11, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
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
		System.out.println("botonConectar.actionPerformed, event="+evt);
		//TODO add your code for botonConectar.actionPerformed
	}
	
	private void botonDesconectarActionPerformed(ActionEvent evt) {
		System.out.println("botonDesconectar.actionPerformed, event="+evt);
		//TODO add your code for botonDesconectar.actionPerformed
	}

}
