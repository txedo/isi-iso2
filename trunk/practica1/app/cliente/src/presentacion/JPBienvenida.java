package presentacion;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import presentacion.auxiliar.Dialogos;

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
	private JLabel lblTitulo;
	private JLabel lblImagen;

	public JPBienvenida() {
		super();
		initGUI();
		lblTitulo.setHorizontalAlignment(JLabel.CENTER);
		lblImagen.setHorizontalAlignment(JLabel.CENTER);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			{
				lblTitulo = new JLabel();
				this.add(lblTitulo, new AnchorConstraint(53, 923, 147, 61, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_REL));
				lblTitulo.setText("Sistema de Salud de Comunidades Autónomas");
				lblTitulo.setPreferredSize(new java.awt.Dimension(487, 27));
				lblTitulo.setFont(new java.awt.Font("Tahoma",1,24));
			}
			{
				lblImagen = new JLabel();
				this.add(lblImagen, new AnchorConstraint(135, 714, 878, 261, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_REL));
				lblImagen.setPreferredSize(new java.awt.Dimension(256, 284));
				lblImagen.setIcon(new ImageIcon(getClass().getClassLoader().getResource("imagenes/Health-care-shield-256.png")));
				lblImagen.setMinimumSize(new java.awt.Dimension(258, 261));
			}
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
		}
	}
	
}
