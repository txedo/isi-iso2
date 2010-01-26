package presentacion;

import java.util.EventObject;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Operaciones;
import dominio.control.ControladorCliente;

/**
* Panel que agrupa todas las operaciones sobre citas.
*/
public class JPCitas extends JPBase {

	private static final long serialVersionUID = 3597203747113684691L;
		
	private JPCitaTramitar jPanelTramitar;
	private JPCitaConsultar jPanelConsultar;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	
	public JPCitas() {
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
				jPanelTramitar = new JPCitaTramitar();
				this.add(jPanelTramitar, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			}
			{
				jPanelConsultar = new JPCitaConsultar();
				this.add(jPanelConsultar, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// $hide>>$
	
	public void setControlador(ControladorCliente controlador) {
		super.setControlador(controlador);
		jPanelTramitar.setControlador(controlador);
		jPanelConsultar.setControlador(controlador);
	}
	
	public void setFrame(JFrame frame) {
		super.setFrame(frame);
		jPanelTramitar.setFrame(frame);
		jPanelConsultar.setFrame(frame);
	}
	
	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(Operaciones.TramitarCita);
		jPanelListaOperaciones.ponerOperacion(Operaciones.ConsultarCitas);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(Operaciones.TramitarCita);
		jPanelTramitar.setVisible(true);
		jPanelConsultar.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(EventObject evt) {
		if(jPanelTramitar.isValid()) {
			jPanelTramitar.setVisible(false);
		}
		if(jPanelConsultar.isValid()) {
			jPanelConsultar.setVisible(false);
		}
		if(jPanelListaOperaciones.getOperacion() == Operaciones.TramitarCita) {
			jPanelTramitar.setVisible(true);
			jPanelTramitar.repaint();
		}
		if(jPanelListaOperaciones.getOperacion() == Operaciones.ConsultarCitas) {
			jPanelConsultar.setVisible(true);
			jPanelConsultar.repaint();
		}
	}

	public void desactivarTramitarCita() {
		jPanelListaOperaciones.quitarOperacion(Operaciones.TramitarCita);
	}

	public void desactivarConsultarCita() {
		jPanelListaOperaciones.quitarOperacion(Operaciones.ConsultarCitas);
	}

	// $hide<<$
	
}
