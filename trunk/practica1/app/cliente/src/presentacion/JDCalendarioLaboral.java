package presentacion;
import com.cloudgarden.layout.AnchorLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import dominio.conocimiento.PeriodoTrabajo;
import dominio.control.ControladorCliente;


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
public class JDCalendarioLaboral extends javax.swing.JDialog {

	private JPanel parent;
	private JPCalendarioConsultar jPanelConsultarCalendario;
	
	public JDCalendarioLaboral(JPanel parent, Vector<PeriodoTrabajo> p) {
		super ();
		this.parent = parent;
		jPanelConsultarCalendario = new JPCalendarioConsultar(this, p);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				setLocationRelativeTo(getRootPane());
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				this.setTitle("Configuración del calendario laboral");
				this.setSize(430, 300);
				this.setResizable(false);
				this.setModal(true);
				this.add(jPanelConsultarCalendario);
				jPanelConsultarCalendario.setVisible(true);
			}
			{
				this.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						thisWindowClosing(evt);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPeriodos (Vector<PeriodoTrabajo> p) {
		if (parent instanceof JPUsuarioRegistrar) {
			((JPUsuarioRegistrar)parent).setPeriodos(p);
		}
		if (parent instanceof JPUsuarioConsultar) {
			((JPUsuarioConsultar)parent).setPeriodos(p);
		}
	}
	
	private void thisWindowClosing(WindowEvent evt) {
		this.setPeriodos(jPanelConsultarCalendario.getPeriodosTrabajo());
	}

}
