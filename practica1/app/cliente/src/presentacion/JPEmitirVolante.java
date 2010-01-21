package presentacion;
import com.cloudgarden.layout.AnchorConstraint;

import java.awt.Dimension;
import javax.swing.BorderFactory;

import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

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
	
	private JPBeneficiarioConsultar jPanelBeneficiario;
	private JSeparator jSeparator1;
	private JPanel jPanelMedico;

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
				jPanelMedico = new JPanel();
				this.add(jPanelMedico, new AnchorConstraint(192, 12, 885, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jPanelMedico.setPreferredSize(new java.awt.Dimension(541, 153));
			}
			{
				jSeparator1 = new JSeparator();
				this.add(jSeparator1, new AnchorConstraint(182, 5, 493, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jSeparator1.setPreferredSize(new java.awt.Dimension(554, 10));
			}
			this.add(jPanelBeneficiario, new AnchorConstraint(5, 5, 437, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
			jPanelBeneficiario.setPreferredSize(new java.awt.Dimension(554, 171));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
