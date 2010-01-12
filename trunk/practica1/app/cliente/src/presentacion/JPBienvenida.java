package presentacion;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

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
