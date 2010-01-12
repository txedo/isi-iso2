package presentacion;

import javax.swing.JLabel;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

/**
* Panel que permite consultar usuarios existentes.
*/
public class JPUsuarioModificar extends JPBase {

	private static final long serialVersionUID = -1005024006111092815L;
	
	private JLabel lblPrueba;

	public JPUsuarioModificar() {
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
				lblPrueba.setText("Modificar usuario");
				lblPrueba.setPreferredSize(new java.awt.Dimension(108, 16));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// $hide>>$
	
	// $hide<<$

}
