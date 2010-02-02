package presentacion;

import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.control.ControladorCliente;
import java.util.EventObject;

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
* Panel que agrupa todas las operaciones sobre beneficiarios.
*/
public class JPBeneficiarios extends JPBase {

	private static final long serialVersionUID = -8179883245270149179L;

	private JPBeneficiarioRegistrar jPanelRegistrar;
	private JPBeneficiarioConsultar jPanelConsultar;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	
	public JPBeneficiarios(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		inicializarOperaciones();
		ocultarPaneles();
	}
	
	private void initGUI() {
		try {
			this.setSize(565, 390);
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			{
				jPanelListaOperaciones = new JPOperaciones();
				this.add(jPanelListaOperaciones, new AnchorConstraint(6, 214, 0, 4, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelListaOperaciones.setPreferredSize(new java.awt.Dimension(139, 384));
				jPanelListaOperaciones.addOperacionCambiadaListener(new OperacionCambiadaListener() {
					public void operacionCambiada(EventObject evt) {
						jPanelListaOperacionesOperacionCambiada(evt);
					}
				});
			}
			{
				jSeparator = new JSeparator();
				this.add(jSeparator, new AnchorConstraint(0, 268, 0, 149, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jSeparator.setLayout(null);
				jSeparator.setOrientation(SwingConstants.VERTICAL);
				jSeparator.setPreferredSize(new java.awt.Dimension(5, 390));
			}
			{
				jPanelRegistrar = new JPBeneficiarioRegistrar(this.getFrame(), this.getControlador());
				this.add(jPanelRegistrar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelRegistrar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			{
				jPanelConsultar = new JPBeneficiarioConsultar(this.getFrame(), this.getControlador());
				this.add(jPanelConsultar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelConsultar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	public void desactivarModificacion(){
		jPanelConsultar.desactivarModificacion();
	}

	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.RegistrarBeneficiario);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarModificarBeneficiario);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.RegistrarBeneficiario);
		jPanelRegistrar.setVisible(true);
		jPanelConsultar.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(EventObject evt) {
		if(jPanelRegistrar.isValid()) {
			jPanelRegistrar.setVisible(false);
		}
		if(jPanelConsultar.isValid()) {
			jPanelConsultar.setVisible(false);
		}
		if(jPanelListaOperaciones.getOperacion() == OperacionesInterfaz.RegistrarBeneficiario) {
			jPanelRegistrar.setVisible(true);
			jPanelRegistrar.repaint();
		}
		if(jPanelListaOperaciones.getOperacion() == OperacionesInterfaz.ConsultarModificarBeneficiario) {
			jPanelConsultar.setVisible(true);
			jPanelConsultar.repaint();
		}
	}
	
	public void desactivarRegistrarBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.RegistrarBeneficiario);
	}

	public void desactivarConsultarModificarBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarModificarBeneficiario);
	}

	//$hide<<$
	
}
