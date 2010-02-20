package presentacion;

import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import presentacion.auxiliares.OperacionCambiadaEvent;
import presentacion.auxiliares.OperacionCambiadaListener;
import presentacion.auxiliares.OperacionesInterfaz;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
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
* Panel que agrupa todas las operaciones sobre beneficiarios.
*/
public class JPBeneficiarios extends JPBase {

	private static final long serialVersionUID = -8179883245270149179L;

	private EventListenerList listenerList;
	private OperacionesInterfaz operacionSeleccionada;
	
	private JPBeneficiarioRegistrar jPanelRegistrar;
	private JPBeneficiarioConsultar jPanelConsultar;
	private JPBeneficiarioConsultar jPanelConsultarModificar;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	
	public JPBeneficiarios() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPBeneficiarios
	}
	
	public JPBeneficiarios(JFrame frame, ControladorCliente controlador) {
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
				jPanelRegistrar = new JPBeneficiarioRegistrar(this.getFrame(), this.getControlador());
				this.add(jPanelRegistrar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelRegistrar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			{
				jPanelConsultarModificar = new JPBeneficiarioConsultar(this.getFrame(), this.getControlador());
				this.add(jPanelConsultarModificar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelConsultarModificar.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			{
				jPanelConsultar = new JPBeneficiarioConsultar(this.getFrame(), this.getControlador());
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
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.RegistrarBeneficiario);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarModificarBeneficiario);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarBeneficiario);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(OperacionesInterfaz.RegistrarBeneficiario);
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
		if(operacionSeleccionada == OperacionesInterfaz.RegistrarBeneficiario) {
			jPanelRegistrar.setVisible(true);
			jPanelRegistrar.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarModificarBeneficiario) {
			jPanelConsultarModificar.setVisible(true);
			jPanelConsultarModificar.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarBeneficiario) {
			jPanelConsultar.setVisible(true);
			jPanelConsultar.repaint();
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
	
	public void addOperacionCambiadaListener(OperacionCambiadaListener listener) {
		listenerList.add(OperacionCambiadaListener.class, listener);
	}

	public void removeOperacionCambiadaListener(OperacionCambiadaListener listener) {
		listenerList.remove(OperacionCambiadaListener.class, listener);
	}
	
	public void desactivarRegistrarBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.RegistrarBeneficiario);
	}

	public void desactivarConsultarModificarBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarModificarBeneficiario);
	}

	public void desactivarConsultarBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarBeneficiario);
	}
	
	public boolean hayOperacionesDisponibles() {
		return (jPanelListaOperaciones.getNumeroOperaciones() > 0);
	}
	
	public void beneficiarioActualizado(Beneficiario beneficiario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case ConsultarBeneficiario:
			jPanelConsultar.beneficiarioActualizado(beneficiario);
			break;
		case ConsultarModificarBeneficiario:
			jPanelConsultarModificar.beneficiarioActualizado(beneficiario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case ConsultarBeneficiario:
			jPanelConsultar.beneficiarioEliminado(beneficiario);
			break;
		case ConsultarModificarBeneficiario:
			jPanelConsultarModificar.beneficiarioEliminado(beneficiario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void restablecerPaneles() {
		jPanelConsultar.restablecerPanel();
		jPanelConsultarModificar.restablecerPanel();
		jPanelRegistrar.restablecerPanel();
	}
	
	//$hide<<$
	
}
