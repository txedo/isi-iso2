package presentacion;

import javax.swing.JLabel;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

/**
* Panel que permite eliminar usuarios existentes.
*/
public class JPUsuarioEliminar extends JPBase {

	private static final long serialVersionUID = 4450130327435952644L;
	
	private JLabel lblPrueba;

	public JPUsuarioEliminar() {
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
				lblPrueba.setText("Eliminar usuario");
				lblPrueba.setPreferredSize(new java.awt.Dimension(108, 16));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// $hide>>$
	
	// $hide<<$

}
