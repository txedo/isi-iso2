package presentacion;

import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;
import presentacion.auxiliar.OperacionCambiadaEvent;
import presentacion.auxiliar.OperacionCambiadaListener;
import presentacion.auxiliar.OperacionesInterfaz;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Usuario;
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
/**
* Panel que agrupa todas las operaciones sobre usuarios.
*/
public class JPUsuarios extends JPBase {

	private static final long serialVersionUID = -3062194232916521414L;

	private EventListenerList listenerList;
	private OperacionesInterfaz operacionSeleccionada;
	
	private JPUsuarioRegistrar jPanelRegistrar;
	private JPUsuarioConsultar jPanelConsultar;
	private JPUsuarioConsultar jPanelConsultarModificar;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;

	public JPUsuarios() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPUsuarios
	}
	
	public JPUsuarios(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		listenerList = new EventListenerList();
		operacionSeleccionada = OperacionesInterfaz.OperacionInvalida;
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
					public void operacionCambiada(OperacionCambiadaEvent evt) {
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
				jPanelConsultarModificar = new JPUsuarioConsultar(this.getFrame(), this.getControlador());
				this.add(jPanelConsultarModificar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelConsultarModificar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			{
				jPanelConsultar = new JPUsuarioConsultar(this.getFrame(), this.getControlador());
				this.add(jPanelConsultar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelConsultar.setPreferredSize(new java.awt.Dimension(406, 390));
				jPanelConsultar.desactivarModificacion();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//$hide>>$
	
	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.RegistrarUsuario);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarModificarUsuario);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarUsuario);
	}

	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(OperacionesInterfaz.RegistrarUsuario);
		jPanelRegistrar.setVisible(true);
		jPanelConsultarModificar.setVisible(false);
		jPanelConsultar.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(OperacionCambiadaEvent evt) {
		Object[] listeners;
		int i;
		
		operacionSeleccionada = evt.getOperacion();
		
		if(jPanelRegistrar != null) {
			jPanelRegistrar.setVisible(false);
		}
		if(jPanelConsultarModificar != null) {
			jPanelConsultarModificar.setVisible(false);
		}
		if(jPanelConsultar != null) {
			jPanelConsultar.setVisible(false);
		}
		if(operacionSeleccionada == OperacionesInterfaz.RegistrarUsuario) {
			jPanelRegistrar.setVisible(true);
			jPanelRegistrar.repaint();
		}	
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarModificarUsuario) {
			jPanelConsultarModificar.setVisible(true);
			jPanelConsultarModificar.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarUsuario) {
			jPanelConsultar.setVisible(true);
			jPanelConsultar.repaint();
		}
		
		// Notificamos que ha cambiado la operación seleccionada
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == OperacionCambiadaListener.class) {
				((OperacionCambiadaListener)listeners[i + 1]).operacionCambiada(new OperacionCambiadaEvent(this, operacionSeleccionada));
			}
		}
	}
	
	// Métodos públicos
	
	public OperacionesInterfaz getOperacionSeleccionada() {
		return operacionSeleccionada;
	}

	public void addOperacionCambiadaListener(OperacionCambiadaListener listener) {
		listenerList.add(OperacionCambiadaListener.class, listener);
	}

	public void removeOperacionCambiadaListener(OperacionCambiadaListener listener) {
		listenerList.remove(OperacionCambiadaListener.class, listener);
	}
	
	public void desactivarCrearUsuario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.RegistrarUsuario);
	}

	public void desactivarConsultarModificarUsuario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarModificarUsuario);
	}

	public void desactivarConsultarUsuario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarUsuario);
	}
	
	public boolean hayOperacionesDisponibles() {
		return (jPanelListaOperaciones.getNumeroOperaciones() > 0);
	}
	
	// <métodos del observador>

	public void usuarioActualizado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case ConsultarUsuario:
			jPanelConsultar.usuarioActualizado(usuario);
			break;
		case ConsultarModificarUsuario:
			jPanelConsultarModificar.usuarioActualizado(usuario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case ConsultarUsuario:
			jPanelConsultar.usuarioEliminado(usuario);
			break;
		case ConsultarModificarUsuario:
			jPanelConsultarModificar.usuarioEliminado(usuario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void restablecerPaneles() {
		jPanelConsultar.restablecerPanel();
		jPanelConsultarModificar.restablecerPanel();
		jPanelRegistrar.restablecerPanel();
	}
	
	//$hide<<$
	
}
