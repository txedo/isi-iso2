package presentacion;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.cloudgarden.layout.AnchorConstraint;
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
/**
 * Panel con el mensaje de bienvenida del sistema.
 */
public class JPBienvenida extends JPanel {

	private static final long serialVersionUID = 6607107361409604924L;
	
	private JTextArea txtAreaBienvenida;

	public JPBienvenida() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(565, 390);
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			{
				txtAreaBienvenida = new JTextArea();
				this.add(txtAreaBienvenida, new AnchorConstraint(39, 979, 963, 22, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				txtAreaBienvenida.setText("Mensaje de bienvenida");
				txtAreaBienvenida.setPreferredSize(new java.awt.Dimension(539, 291));
				txtAreaBienvenida.setFocusable(false);
				txtAreaBienvenida.setEditable(false);
				txtAreaBienvenida.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				txtAreaBienvenida.setOpaque(false);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
