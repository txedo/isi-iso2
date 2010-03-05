package presentacion;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
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
* Panel que agrupa todas las operaciones sobre sustituciones.
*/
public class JPSustituciones extends JPBase {

	private static final long serialVersionUID = -7175632273033085117L;
	
	private EventListenerList listenerList;
	private OperacionesInterfaz operacionSeleccionada;
	
	private JPSustitucionEstablecer jPanelEstablecer;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	private JScrollPane jScrollEstablecer;
	
	public JPSustituciones() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPSustituciones
	}
	
	public JPSustituciones(JFPrincipal frame, ControladorCliente controlador) {
		super(frame, controlador);
		listenerList = new EventListenerList();
		operacionSeleccionada = OperacionesInterfaz.OperacionInvalida;
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
				jScrollEstablecer = new JScrollPane();
				this.add(jScrollEstablecer, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollEstablecer.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollEstablecer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollEstablecer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelEstablecer = new JPSustitucionEstablecer(this.getFrame(), this.getControlador());
					jPanelEstablecer.setName("jPanelEstablecer");
					jScrollEstablecer.setViewportView(jPanelEstablecer);
					jPanelEstablecer.setName("jPanelEstablecer");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$

	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.EstablecerSustituto);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(OperacionesInterfaz.EstablecerSustituto);
		jScrollEstablecer.setVisible(true);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(OperacionCambiadaEvent evt) {
		Object[] listeners;
		int i;

		operacionSeleccionada = evt.getOperacion();
		
		if(jScrollEstablecer != null) {
			jScrollEstablecer.setVisible(false);
		}
		if(operacionSeleccionada == OperacionesInterfaz.EstablecerSustituto) {
			jScrollEstablecer.setVisible(true);
			jScrollEstablecer.repaint();
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

	public void setOperacionSeleccionada(OperacionesInterfaz operacionSeleccionada) {
		jPanelListaOperaciones.setOperacion(operacionSeleccionada);
		this.operacionSeleccionada = operacionSeleccionada;
	}

	public void addOperacionCambiadaListener(OperacionCambiadaListener listener) {
		listenerList.add(OperacionCambiadaListener.class, listener);
	}

	public void removeOperacionCambiadaListener(OperacionCambiadaListener listener) {
		listenerList.remove(OperacionCambiadaListener.class, listener);
	}
	
	public void desactivarEstablecerSustituto() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.EstablecerSustituto);
	}
	
	public boolean hayOperacionesDisponibles() {
		return (jPanelListaOperaciones.getNumeroOperaciones() > 0);
	}
	
	public void usuarioRegistrado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case EstablecerSustituto:
			jPanelEstablecer.usuarioRegistrado(usuario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioActualizado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case EstablecerSustituto:
			jPanelEstablecer.usuarioActualizado(usuario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case EstablecerSustituto:
			jPanelEstablecer.usuarioEliminado(usuario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void restablecerPaneles() {
		jPanelEstablecer.restablecerPanel();
	}
	
	//$hide<<$
	
}
