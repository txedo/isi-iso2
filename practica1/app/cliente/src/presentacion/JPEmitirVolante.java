package presentacion;
import com.cloudgarden.layout.AnchorConstraint;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;

import com.cloudgarden.layout.AnchorLayout;


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
public class JPEmitirVolante extends javax.swing.JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2491308165795545454L;
	
	private JPBeneficiarioConsultar jPanelBeneficiario;
	private JSeparator jSeparator1;
	private JPanel jPanelMedico;
	private JTextField txtNombre;
	private JTextField txtEspecialidad;
	private JLabel lblEspecialidad;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JTextField txtApellidos;
	private JLabel lblSelectl;
	private JList lstEspecialistas;
	private JButton btnAceptar;

	public JPEmitirVolante() {
		super();
		jPanelBeneficiario = new JPBeneficiarioConsultar();
		jPanelBeneficiario.ocultarControles();
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(565, 390);
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			{
				btnAceptar = new JButton();
				this.add(btnAceptar, new AnchorConstraint(356, 15, 973, 485, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnAceptar.setText("Aceptar");
				btnAceptar.setPreferredSize(new java.awt.Dimension(65, 23));
			}
			{
				jPanelMedico = new JPanel();
				AnchorLayout jPanelMedicoLayout = new AnchorLayout();
				jPanelMedico.setLayout(jPanelMedicoLayout);
				this.add(jPanelMedico, new AnchorConstraint(200, 5, 885, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jPanelMedico.setPreferredSize(new java.awt.Dimension(548, 144));
				{
					lblEspecialidad = new JLabel();
					jPanelMedico.add(lblEspecialidad, new AnchorConstraint(107, 271, 807, 200, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblEspecialidad.setText("Especialidad");
					lblEspecialidad.setPreferredSize(new java.awt.Dimension(77, 16));
				}
				{
					lblApellidos = new JLabel();
					jPanelMedico.add(lblApellidos, new AnchorConstraint(72, 290, 578, 200, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblApellidos.setText("Apellidos");
					lblApellidos.setPreferredSize(new java.awt.Dimension(58, 16));
				}
				{
					lblNombre = new JLabel();
					jPanelMedico.add(lblNombre, new AnchorConstraint(37, 300, 349, 200, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblNombre.setText("Nombre");
					lblNombre.setPreferredSize(new java.awt.Dimension(48, 16));
				}
				{
					txtEspecialidad = new JTextField();
					jPanelMedico.add(txtEspecialidad, new AnchorConstraint(104, 12, 833, 277, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtEspecialidad.setPreferredSize(new java.awt.Dimension(259, 23));
				}
				{
					txtApellidos = new JTextField();
					jPanelMedico.add(txtApellidos, new AnchorConstraint(69, 12, 604, 276, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtApellidos.setPreferredSize(new java.awt.Dimension(260, 23));
				}
				{
					txtNombre = new JTextField();
					jPanelMedico.add(txtNombre, new AnchorConstraint(34, 12, 375, 276, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtNombre.setPreferredSize(new java.awt.Dimension(260, 23));
				}
				{
					lblSelectl = new JLabel();
					jPanelMedico.add(lblSelectl, new AnchorConstraint(81, 278, 186, 23, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					lblSelectl.setText("Seleccione un especialista:");
					lblSelectl.setPreferredSize(new java.awt.Dimension(138, 16));
				}
				{
					ListModel lstEspecialistasModel = 
						new DefaultComboBoxModel(
								new String[] {});
					lstEspecialistas = new JList();
					jPanelMedico.add(lstEspecialistas, new AnchorConstraint(34, 396, 316, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lstEspecialistas.setModel(lstEspecialistasModel);
					lstEspecialistas.setPreferredSize(new java.awt.Dimension(140, 107));
				}
			}
			{
				jSeparator1 = new JSeparator();
				this.add(jSeparator1, new AnchorConstraint(191, 5, 493, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jSeparator1.setPreferredSize(new java.awt.Dimension(554, 10));
			}
			this.add(jPanelBeneficiario, new AnchorConstraint(5, 5, 437, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
			jPanelBeneficiario.setPreferredSize(new java.awt.Dimension(554, 171));

			txtNombre.setEditable(false);
			txtNombre.setFocusable(false);
			txtApellidos.setEditable(false);
			txtApellidos.setFocusable(false);
			txtEspecialidad.setEditable(false);
			txtEspecialidad.setFocusable(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
