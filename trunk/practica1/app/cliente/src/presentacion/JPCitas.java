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

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Sustitucion;
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
* Panel que agrupa todas las operaciones sobre citas.
*/
public class JPCitas extends JPBase {

	private static final long serialVersionUID = 3597203747113684691L;
	
	private EventListenerList listenerList;
	private OperacionesInterfaz operacionSeleccionada;

	private JPCitaTramitar jPanelTramitar;
	private JPCitaVolanteTramitar jPanelVolanteTramitar;
	private JPCitaConsultarBeneficiario jPanelConsultarCitasBeneficiario;
	private JPCitaConsultarMedico jPanelConsultarCitasMedico;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	
	public JPCitas() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPCitas
	}
	
	public JPCitas(JFrame frame, ControladorCliente controlador) {
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
				jPanelConsultarCitasBeneficiario = new JPCitaConsultarBeneficiario(this.getFrame(), this.getControlador());
				this.add(jPanelConsultarCitasBeneficiario, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelConsultarCitasBeneficiario.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			{
				jPanelConsultarCitasMedico = new JPCitaConsultarMedico(this.getFrame(), this.getControlador());
				this.add(jPanelConsultarCitasMedico, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jPanelConsultarCitasMedico.setPreferredSize(new java.awt.Dimension(406, 390));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// $hide>>$
	
	private void inicializarOperaciones() {
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.TramitarCita);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.TramitarCitaVolante);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarAnularCitasBeneficiario);
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarCitasMedico);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(OperacionesInterfaz.TramitarCita);
		jPanelTramitar.setVisible(true);
		jPanelConsultarCitasBeneficiario.setVisible(false);
		jPanelConsultarCitasMedico.setVisible(false);
		jPanelVolanteTramitar.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(OperacionCambiadaEvent evt) {
		Object[] listeners;
		int i;

		operacionSeleccionada = evt.getOperacion();
		
		if(jPanelTramitar != null) {
			jPanelTramitar.setVisible(false);
		}
		if(jPanelVolanteTramitar != null) {
			jPanelVolanteTramitar.setVisible(false);
		}
		if(jPanelConsultarCitasBeneficiario != null) {
			jPanelConsultarCitasBeneficiario.setVisible(false);
		}
		if(jPanelConsultarCitasMedico != null) {
			jPanelConsultarCitasMedico.setVisible(false);
		}
		if(operacionSeleccionada == OperacionesInterfaz.TramitarCita) {
			jPanelTramitar.setVisible(true);
			jPanelTramitar.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.TramitarCitaVolante) {
			jPanelVolanteTramitar.setVisible(true);
			jPanelVolanteTramitar.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarAnularCitasBeneficiario) {
			jPanelConsultarCitasBeneficiario.setVisible(true);
			jPanelConsultarCitasBeneficiario.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarCitasMedico) {
			jPanelConsultarCitasMedico.setVisible(true);
			jPanelConsultarCitasMedico.repaint();
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
	
	public void desactivarTramitarCita() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.TramitarCita);
	}
	
	public void desactivarTramitarCitaVolante() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.TramitarCitaVolante);
	}

	public void desactivarConsultarAnularCitaBeneficiario() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarAnularCitasBeneficiario);
	}

	public void desactivarConsultarCitaMedico() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarCitasMedico);
	}

	public boolean hayOperacionesDisponibles() {
		return (jPanelListaOperaciones.getNumeroOperaciones() > 0);
	}

	public void beneficiarioActualizado(Beneficiario beneficiario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelTramitar.beneficiarioActualizado(beneficiario);
			break;
		case TramitarCitaVolante:
			jPanelVolanteTramitar.beneficiarioActualizado(beneficiario);
			break;
		case ConsultarAnularCitasBeneficiario:
			jPanelConsultarCitasBeneficiario.beneficiarioActualizado(beneficiario);
			break;
		case ConsultarCitasMedico:
			jPanelConsultarCitasMedico.beneficiarioActualizado(beneficiario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelTramitar.beneficiarioEliminado(beneficiario);
			break;
		case TramitarCitaVolante:
			jPanelVolanteTramitar.beneficiarioEliminado(beneficiario);
			break;
		case ConsultarAnularCitasBeneficiario:
			jPanelConsultarCitasBeneficiario.beneficiarioEliminado(beneficiario);
			break;
		case ConsultarCitasMedico:
			jPanelConsultarCitasMedico.beneficiarioEliminado(beneficiario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioActualizado(Usuario usuario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelTramitar.usuarioActualizado(usuario);
			break;
		case TramitarCitaVolante:
			jPanelVolanteTramitar.usuarioActualizado(usuario);
			break;
		case ConsultarAnularCitasBeneficiario:
			jPanelConsultarCitasBeneficiario.usuarioActualizado(usuario);
			break;
		case ConsultarCitasMedico:
			jPanelConsultarCitasMedico.usuarioActualizado(usuario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelTramitar.usuarioEliminado(usuario);
			break;
		case TramitarCitaVolante:
			jPanelVolanteTramitar.usuarioEliminado(usuario);
			break;
		case ConsultarAnularCitasBeneficiario:
			jPanelConsultarCitasBeneficiario.usuarioEliminado(usuario);
			break;
		case ConsultarCitasMedico:
			jPanelConsultarCitasMedico.usuarioEliminado(usuario);
			break;
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void citaRegistrada(Cita cita) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelTramitar.citaRegistrada(cita);
			break;
		case TramitarCitaVolante:
			jPanelVolanteTramitar.citaRegistrada(cita);
			break;	
		case ConsultarCitasMedico:
			jPanelConsultarCitasMedico.citaRegistrada(cita);
			break;
		case ConsultarAnularCitasBeneficiario:
			jPanelConsultarCitasBeneficiario.citaRegistrada(cita);
			break;		
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void citaAnulada(Cita cita) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelTramitar.citaAnulada(cita);
			break;
		case TramitarCitaVolante:
			jPanelVolanteTramitar.citaAnulada(cita);
			break;
		case ConsultarCitasMedico:
			jPanelConsultarCitasMedico.citaAnulada(cita);
			break;
		case ConsultarAnularCitasBeneficiario:
			jPanelConsultarCitasBeneficiario.citaAnulada(cita);
			break;	
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void sustitucionRegistrada(Sustitucion sustitucion) {
		// Redirigimos la operaci�n al panel seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelTramitar.sustitucionRegistrada(sustitucion);
			break;
		case TramitarCitaVolante:
			jPanelVolanteTramitar.sustitucionRegistrada(sustitucion);
			break;
/*		case ConsultarCitasMedico:
			jPanelConsultarCitasMedico.citaAnulada(cita);
			break;
		case ConsultarAnularCitasBeneficiario:
			jPanelConsultarCitasBeneficiario.citaAnulada(cita);
			break;*/	
		default:
			// La operaci�n no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void restablecerPaneles() {
		jPanelConsultarCitasBeneficiario.restablecerPanel();
		jPanelConsultarCitasMedico.restablecerPanel();
		jPanelTramitar.restablecerPanel();
		jPanelVolanteTramitar.restablecerPanel();
	}

	
	//$hide<<$
	
}
