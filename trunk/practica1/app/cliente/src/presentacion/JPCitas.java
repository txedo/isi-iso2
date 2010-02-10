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
	private JPCitaVolanteTramitar jPanelVolanteTramitar;
	private JPCitaConsultar jPanelConsultar;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	
	public JPCitas(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
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
				jPanelTramitar = new JPCitaTramitar(this.getFrame(), this.getControlador());
				this.add(jPanelTramitar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelTramitar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			{
				jPanelVolanteTramitar = new JPCitaVolanteTramitar(this.getFrame(), this.getControlador());
				this.add(jPanelVolanteTramitar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelVolanteTramitar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			{
				jPanelConsultar = new JPCitaConsultar(this.getFrame(), this.getControlador());
				this.add(jPanelConsultar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelConsultar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// $hide>>$
	
	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.TramitarCita);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.TramitarCitaVolante);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarAnularCita);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(OperacionesInterfaz.TramitarCita);
		jPanelTramitar.setVisible(true);
		jPanelConsultar.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(EventObject evt) {
		if(jPanelTramitar.isValid()) {
			jPanelTramitar.setVisible(false);
		}
		if(jPanelVolanteTramitar.isValid()) {
			jPanelVolanteTramitar.setVisible(false);
		}
		if(jPanelConsultar.isValid()) {
			jPanelConsultar.setVisible(false);
		}
		if(jPanelListaOperaciones.getOperacion() == OperacionesInterfaz.TramitarCita) {
			jPanelTramitar.setVisible(true);
			jPanelTramitar.repaint();
		}
		if(jPanelListaOperaciones.getOperacion() == OperacionesInterfaz.TramitarCitaVolante) {
			jPanelVolanteTramitar.setVisible(true);
			jPanelVolanteTramitar.repaint();
		}
		if(jPanelListaOperaciones.getOperacion() == OperacionesInterfaz.ConsultarAnularCita) {
			jPanelConsultar.setVisible(true);
			jPanelConsultar.repaint();
		}
	}

	public void desactivarTramitarCita() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.TramitarCita);
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.TramitarCitaVolante);
	}

	public void desactivarConsultarAnularCita() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarAnularCita);
	}

	// $hide<<$
	
}
