package presentacion;

import javax.swing.JLabel;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

/**
 * Panel que permite anular citas existentes.
 */
public class JPCitaAnular extends JPBase {

	private static final long serialVersionUID = 117161427277876393L;
	
	private JLabel lblPrueba;

	public JPCitaAnular() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				lblPrueba = new JLabel();
				this.add(lblPrueba, new AnchorConstraint(32, 280, 73, 29, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				lblPrueba.setText("Anular cita");
				lblPrueba.setPreferredSize(new java.awt.Dimension(108, 16));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// $hide>>$
	
	// $hide<<$

}
