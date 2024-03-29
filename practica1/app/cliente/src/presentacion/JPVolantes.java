package presentacion;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.OperacionCambiadaEvent;
import presentacion.auxiliar.OperacionCambiadaListener;
import presentacion.auxiliar.OperacionesInterfaz;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
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
 * Panel que agrupa todas las operaciones sobre volantes.
 */
public class JPVolantes extends JPBase {

	private static final long serialVersionUID = -7175632273033085117L;
	
	private EventListenerList listenerList;
	private OperacionesInterfaz operacionSeleccionada;
	
	private JPVolanteEmitir jPanelEmitir;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	private JScrollPane jScrollEmitir;
	
	public JPVolantes() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPVolantes
	}
	
	public JPVolantes(JFPrincipal frame, ControladorCliente controlador) {
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
				jScrollEmitir = new JScrollPane();
				this.add(jScrollEmitir, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollEmitir.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollEmitir.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollEmitir.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelEmitir = new JPVolanteEmitir(this.getFrame(), this.getControlador());
					jPanelEmitir.setName("jPanelEmitir");
					jScrollEmitir.setViewportView(jPanelEmitir);
				}
			}
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		}
	}
	
	//$hide>>$

	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.EmitirVolante);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(OperacionesInterfaz.EmitirVolante);
		jScrollEmitir.setVisible(true);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(OperacionCambiadaEvent evt) {
		Object[] listeners;
		int i;

		operacionSeleccionada = evt.getOperacion();
		
		if(jScrollEmitir != null) {
			jScrollEmitir.setVisible(false);
		}
		if(operacionSeleccionada == OperacionesInterfaz.EmitirVolante) {
			jScrollEmitir.setVisible(true);
			jScrollEmitir.repaint();
		}

		// Notificamos que ha cambiado la operaci�n seleccionada
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == OperacionCambiadaListener.class) {
				((OperacionCambiadaListener)listeners[i + 1]).operacionCambiada(new OperacionCambiadaEvent(this, operacionSeleccionada));
			}
		}
	}
	
	// M�todos p�blicos
	
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
	
	public void desactivarEmitirVolante() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.EmitirVolante);
	}
	
	public boolean hayOperacionesDisponibles() {
		return (jPanelListaOperaciones.getNumeroOperaciones() > 0);
	}
	
	public void beneficiarioActualizado(Beneficiario beneficiario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case EmitirVolante:
			jPanelEmitir.beneficiarioActualizado(beneficiario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case EmitirVolante:
			jPanelEmitir.beneficiarioEliminado(beneficiario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioRegistrado(Usuario usuario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case EmitirVolante:
			jPanelEmitir.usuarioRegistrado(usuario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioActualizado(Usuario usuario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case EmitirVolante:
			jPanelEmitir.usuarioActualizado(usuario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case EmitirVolante:
			jPanelEmitir.usuarioEliminado(usuario);
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void restablecerPaneles() {
		jPanelEmitir.restablecerPanel();
	}
	
	//$hide<<$
	
}
