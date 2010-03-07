package presentacion;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;
import presentacion.auxiliar.OperacionCambiadaEvent;
import presentacion.auxiliar.OperacionCambiadaListener;
import presentacion.auxiliar.OperacionesInterfaz;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorCliente;
import javax.swing.BorderFactory;

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
	private JScrollPane jScrollConsultar;
	private JScrollPane jScrollRegistrar;
	private JScrollPane jScrollConsultarModificar;

	public JPBeneficiarios() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPBeneficiarios
	}
	
	public JPBeneficiarios(JFPrincipal frame, ControladorCliente controlador) {
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
				jScrollConsultar = new JScrollPane();
				this.add(jScrollConsultar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollConsultar.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollConsultar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollConsultar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelConsultar = new JPBeneficiarioConsultar(this.getFrame(), this.getControlador());
					jPanelConsultar.setName("jPanelConsultar");
					jScrollConsultar.setViewportView(jPanelConsultar);
					jPanelConsultar.desactivarModificacion();					
				}
			}
			{
				jScrollRegistrar = new JScrollPane();
				this.add(jScrollRegistrar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollRegistrar.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollRegistrar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollRegistrar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelRegistrar = new JPBeneficiarioRegistrar(this.getFrame(), this.getControlador());
					jPanelRegistrar.setName("jPanelRegistrar");
					jScrollRegistrar.setViewportView(jPanelRegistrar);
				}
			}
			{
				jScrollConsultarModificar = new JScrollPane();
				this.add(jScrollConsultarModificar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollConsultarModificar.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollConsultarModificar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollConsultarModificar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelConsultarModificar = new JPBeneficiarioConsultar(this.getFrame(), this.getControlador());
					jPanelConsultarModificar.setName("jPanelConsultarModificar");
					jScrollConsultarModificar.setViewportView(jPanelConsultarModificar);
				}
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
		jScrollRegistrar.setVisible(true);
		jScrollConsultarModificar.setVisible(false);
		jScrollConsultar.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(OperacionCambiadaEvent evt) {
		Object[] listeners;
		int i;

		operacionSeleccionada = evt.getOperacion();
		
		if(jScrollRegistrar != null) {
			jScrollRegistrar.setVisible(false);
		}
		if(jScrollConsultarModificar != null) {
			jScrollConsultarModificar.setVisible(false);
		}
		if(jScrollConsultar != null) {
			jScrollConsultar.setVisible(false);
		}
		if(operacionSeleccionada == OperacionesInterfaz.RegistrarBeneficiario) {
			jScrollRegistrar.setVisible(true);
			jScrollRegistrar.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarModificarBeneficiario) {
			jScrollConsultarModificar.setVisible(true);
			jScrollConsultarModificar.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarBeneficiario) {
			jScrollConsultar.setVisible(true);
			jScrollConsultar.repaint();
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
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case ConsultarBeneficiario:
			jPanelConsultar.beneficiarioActualizado(beneficiario);
			break;
		case ConsultarModificarBeneficiario:
			jPanelConsultarModificar.beneficiarioActualizado(beneficiario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case ConsultarBeneficiario:
			jPanelConsultar.beneficiarioEliminado(beneficiario);
			break;
		case ConsultarModificarBeneficiario:
			jPanelConsultarModificar.beneficiarioEliminado(beneficiario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioActualizado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case ConsultarBeneficiario:
			jPanelConsultar.usuarioActualizado(usuario);
			break;
		case ConsultarModificarBeneficiario:
			jPanelConsultarModificar.usuarioActualizado(usuario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case ConsultarBeneficiario:
			jPanelConsultar.usuarioEliminado(usuario);
			break;
		case ConsultarModificarBeneficiario:
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
