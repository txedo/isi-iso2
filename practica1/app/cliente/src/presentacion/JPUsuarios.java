package presentacion;

import java.util.EventObject;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Operaciones;
import dominio.control.ControladorPrincipal;

/**
* Panel que agrupa todas las operaciones sobre usuarios.
*/
public class JPUsuarios extends JPBase {

	private static final long serialVersionUID = -3062194232916521414L;

	private JPUsuarioModificar jPanelModificar;
	private JPUsuarioEliminar jPanelEliminar;
	private JPUsuarioCrear jPanelCrear;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;

	public JPUsuarios() {
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
				jPanelCrear = new JPUsuarioCrear();
				this.add(jPanelCrear, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			}
			{
				jPanelModificar = new JPUsuarioModificar();
				this.add(jPanelModificar, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			}
			{
				jPanelEliminar = new JPUsuarioEliminar();
				this.add(jPanelEliminar, new AnchorConstraint(1, 1000, 1001, 237, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//$hide>>$
	
	public void setControlador(ControladorPrincipal controlador) {
		super.setControlador(controlador);
		jPanelCrear.setControlador(controlador);
		jPanelModificar.setControlador(controlador);
		jPanelEliminar.setControlador(controlador);
	}
	
	public void setFrame(JFrame frame) {
		super.setFrame(frame);
		jPanelCrear.setFrame(frame);
		jPanelModificar.setFrame(frame);
		jPanelEliminar.setFrame(frame);
	}
	
	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(Operaciones.CrearUsuario);
		jPanelListaOperaciones.ponerOperacion(Operaciones.ModificarUsuario);
		jPanelListaOperaciones.ponerOperacion(Operaciones.EliminarUsuario);
	}

	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(Operaciones.CrearUsuario);
		jPanelCrear.setVisible(true);
		jPanelModificar.setVisible(false);
		jPanelEliminar.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(EventObject evt) {
		if(jPanelCrear.isValid()) {
			jPanelCrear.setVisible(false);
		}
		if(jPanelModificar.isValid()) {
			jPanelModificar.setVisible(false);
		}
		if(jPanelEliminar.isValid()) {
			jPanelEliminar.setVisible(false);
		}
		if(jPanelListaOperaciones.getOperacion() == Operaciones.CrearUsuario) {
			jPanelCrear.setVisible(true);
			jPanelCrear.repaint();
		}
		if(jPanelListaOperaciones.getOperacion() == Operaciones.ModificarUsuario) {
			jPanelModificar.setVisible(true);
			jPanelModificar.repaint();
		}
		if(jPanelListaOperaciones.getOperacion() == Operaciones.EliminarUsuario) {
			jPanelEliminar.setVisible(true);
			jPanelEliminar.repaint();
		}
	}

	public void desactivarCrearUsuario() {
		jPanelListaOperaciones.quitarOperacion(Operaciones.CrearUsuario);
	}

	public void desactivarModificarUsuario() {
		jPanelListaOperaciones.quitarOperacion(Operaciones.ModificarUsuario);
	}

	public void desactivarEliminarUsuario() {
		jPanelListaOperaciones.quitarOperacion(Operaciones.EliminarUsuario);
	}

	//$hide<<$
	
}
