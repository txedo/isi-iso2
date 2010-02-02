package presentacion;

import java.util.EventObject;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.control.ControladorCliente;

/**
* Panel que agrupa todas las operaciones sobre usuarios.
*/
public class JPUsuarios extends JPBase {

	private static final long serialVersionUID = -3062194232916521414L;

	private JPUsuarioRegistrar jPanelRegistrar;
	private JPUsuarioConsultar jPanelConsultar;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;

	public JPUsuarios(JFrame frame, ControladorCliente controlador) {
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
				jPanelRegistrar = new JPUsuarioRegistrar(this.getFrame(), this.getControlador());
				this.add(jPanelRegistrar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelRegistrar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			{
				jPanelConsultar = new JPUsuarioConsultar(this.getFrame(), this.getControlador());
				this.add(jPanelConsultar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelConsultar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//$hide>>$
	
	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.RegistrarUsuario);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarModificarUsuario);
	}

	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(OperacionesInterfaz.RegistrarUsuario);
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
		if(jPanelListaOperaciones.getOperacion() == OperacionesInterfaz.RegistrarUsuario) {
			jPanelRegistrar.setVisible(true);
			jPanelRegistrar.repaint();
		}
		
		if(jPanelListaOperaciones.getOperacion() == OperacionesInterfaz.ConsultarModificarUsuario) {
			jPanelConsultar.setVisible(true);
			jPanelConsultar.repaint();
		}
	}

	public void desactivarCrearUsuario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.RegistrarUsuario);
	}

	public void desactivarConsultarUsuario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarModificarUsuario);
	}
	
	//$hide<<$
	
}
