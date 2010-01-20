package presentacion;

import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Operaciones;
import dominio.control.ControladorPrincipal;

import java.util.EventObject;

/**
* Panel que agrupa todas las operaciones sobre beneficiarios.
*/
public class JPBeneficiarios extends JPBase {

	private static final long serialVersionUID = -8179883245270149179L;

	private JPBeneficiarioRegistrar jPanelRegistrar;
	private JPBeneficiarioConsultar jPanelConsultar;
	//private JPBeneficiarioModificar jPanelModificar;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	
	public JPBeneficiarios() {
		super();
		initGUI();
		inicializarOperaciones();
		ocultarPaneles();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(565, 390);
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			{
				jPanelListaOperaciones = new JPOperaciones();
				this.add(jPanelListaOperaciones, new AnchorConstraint(5, 214, 0, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelListaOperaciones.addOperacionCambiadaListener(new OperacionCambiadaListener() {
					public void operacionCambiada(EventObject evt) {
						jPanelListaOperacionesOperacionCambiada(evt);
					}
				});
			}
			{
				jSeparator = new JSeparator();
				this.add(jSeparator, new AnchorConstraint(1, 237, 1001, 224, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jSeparator.setLayout(null);
				jSeparator.setPreferredSize(new java.awt.Dimension(7, 296));
				jSeparator.setOrientation(SwingConstants.VERTICAL);
			}
			{
				jPanelConsultar = new JPBeneficiarioConsultar();
				this.add(jPanelConsultar, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			}
			{
				jPanelRegistrar = new JPBeneficiarioRegistrar();
				this.add(jPanelRegistrar, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			}
			/*{
				jPanelModificar = new JPBeneficiarioModificar();
				this.add(jPanelModificar, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			}*/
						
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	public void setControlador(ControladorPrincipal controlador) {
		super.setControlador(controlador);
		jPanelRegistrar.setControlador(controlador);
		jPanelConsultar.setControlador(controlador);
		//jPanelModificar.setControlador(controlador);
	}
	
	public void setDesactivarModificacion(){
		jPanelConsultar.setDesactivarModificacion();
	}
	
	public void setFrame(JFrame frame) {
		super.setFrame(frame);
		jPanelRegistrar.setFrame(frame);
		jPanelConsultar.setFrame(frame);
		//jPanelModificar.setFrame(frame);
	}
	
	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(Operaciones.RegistrarBeneficiario);
		jPanelListaOperaciones.ponerOperacion(Operaciones.ConsultarBeneficiario);
		//jPanelListaOperaciones.ponerOperacion(Operaciones.ModificarBeneficiario);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(Operaciones.RegistrarBeneficiario);
		jPanelRegistrar.setVisible(true);
		jPanelConsultar.setVisible(false);
		//jPanelModificar.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(EventObject evt) {
		if(jPanelRegistrar.isValid()) {
			jPanelRegistrar.setVisible(false);
		}
		if(jPanelConsultar.isValid()) {
			jPanelConsultar.setVisible(false);
		}
		if(jPanelListaOperaciones.getOperacion() == Operaciones.RegistrarBeneficiario) {
			jPanelRegistrar.setVisible(true);
			jPanelRegistrar.repaint();
		}
		if(jPanelListaOperaciones.getOperacion() == Operaciones.ConsultarBeneficiario) {
			jPanelConsultar.setVisible(true);
			jPanelConsultar.repaint();
		}
		/*if(jPanelListaOperaciones.getOperacion() == Operaciones.ModificarBeneficiario) {
			jPanelModificar.setVisible(true);
			jPanelModificar.repaint();
		}*/
	}
	
	public void desactivarRegistrarBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(Operaciones.RegistrarBeneficiario);
	}

	public void desactivarConsultarBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(Operaciones.ConsultarBeneficiario);
	}

	public void desactivarModificarBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(Operaciones.ModificarBeneficiario);
	}

	//$hide<<$
	
}
