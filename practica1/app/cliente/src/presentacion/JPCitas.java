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
	private JPCitaConsultarPropias jPanelConsultarCitasPropias;
	private JSeparator jSeparator;
	private JPOperaciones jPanelListaOperaciones;
	private JScrollPane jScrollTramitar;
	private JScrollPane jScrollTramitarConVolante;
	private JScrollPane jScrollConsultarCitasBeneficiario;
	private JScrollPane jScrollConsultarCitasMedico;
	private JScrollPane jScrollConsultarCitasPropias;
	
	public JPCitas() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPCitas
	}
	
	public JPCitas(JFPrincipal frame, ControladorCliente controlador) {
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
				jScrollTramitar = new JScrollPane();
				this.add(jScrollTramitar, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollTramitar.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollTramitar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollTramitar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelTramitar = new JPCitaTramitar(this.getFrame(), this.getControlador());
					jScrollTramitar.setViewportView(jPanelTramitar);
				}
			}
			{
				jScrollTramitarConVolante = new JScrollPane();
				this.add(jScrollTramitarConVolante, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollTramitarConVolante.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollTramitarConVolante.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollTramitarConVolante.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelVolanteTramitar = new JPCitaVolanteTramitar(this.getFrame(), this.getControlador());
					jScrollTramitarConVolante.setViewportView(jPanelVolanteTramitar);
				}
			}
			{
				jScrollConsultarCitasBeneficiario = new JScrollPane();
				this.add(jScrollConsultarCitasBeneficiario, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollConsultarCitasBeneficiario.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollConsultarCitasBeneficiario.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollConsultarCitasBeneficiario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelConsultarCitasBeneficiario = new JPCitaConsultarBeneficiario(this.getFrame(), this.getControlador());
					jScrollConsultarCitasBeneficiario.setViewportView(jPanelConsultarCitasBeneficiario);
				}
			}
			{
				jScrollConsultarCitasMedico = new JScrollPane();
				this.add(jScrollConsultarCitasMedico, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollConsultarCitasMedico.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollConsultarCitasMedico.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollConsultarCitasMedico.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelConsultarCitasMedico = new JPCitaConsultarMedico(this.getFrame(), this.getControlador());
					jScrollConsultarCitasMedico.setViewportView(jPanelConsultarCitasMedico);
				}
			}
			{
				jScrollConsultarCitasPropias = new JScrollPane();
				this.add(jScrollConsultarCitasPropias, new AnchorConstraint(0, 0, 0, 159, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				jScrollConsultarCitasPropias.setPreferredSize(new java.awt.Dimension(406, 390));
				jScrollConsultarCitasPropias.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollConsultarCitasPropias.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				{
					jPanelConsultarCitasPropias = new JPCitaConsultarPropias(this.getFrame(), this.getControlador());
					jScrollConsultarCitasPropias.setViewportView(jPanelConsultarCitasPropias);
				}
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
		jPanelListaOperaciones.ponerOperacion(OperacionesInterfaz.ConsultarCitasPropias);
	}
	
	private void ocultarPaneles() {
		jPanelListaOperaciones.setOperacion(OperacionesInterfaz.TramitarCita);
		jScrollTramitar.setVisible(true);
		jScrollTramitarConVolante.setVisible(false);
		jScrollConsultarCitasBeneficiario.setVisible(false);
		jScrollConsultarCitasMedico.setVisible(false);
		jScrollConsultarCitasPropias.setVisible(false);
	}
	
	private void jPanelListaOperacionesOperacionCambiada(OperacionCambiadaEvent evt) {
		Object[] listeners;
		int i;

		operacionSeleccionada = evt.getOperacion();
		
		if(jScrollTramitar != null) {
			jScrollTramitar.setVisible(false);
		}
		if(jScrollTramitarConVolante != null) {
			jScrollTramitarConVolante.setVisible(false);
		}
		if(jScrollConsultarCitasBeneficiario != null) {
			jScrollConsultarCitasBeneficiario.setVisible(false);
		}
		if(jScrollConsultarCitasMedico != null) {
			jScrollConsultarCitasMedico.setVisible(false);
		}
		if(jScrollConsultarCitasPropias != null) {
			jScrollConsultarCitasPropias.setVisible(false);
		}
		if(operacionSeleccionada == OperacionesInterfaz.TramitarCita) {
			jScrollTramitar.setVisible(true);
			jScrollTramitar.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.TramitarCitaVolante) {
			jScrollTramitarConVolante.setVisible(true);
			jScrollTramitarConVolante.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarAnularCitasBeneficiario) {
			jScrollConsultarCitasBeneficiario.setVisible(true);
			jScrollConsultarCitasBeneficiario.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarCitasMedico) {
			jScrollConsultarCitasMedico.setVisible(true);
			jScrollConsultarCitasMedico.repaint();
		}
		if(operacionSeleccionada == OperacionesInterfaz.ConsultarCitasPropias) {
			jScrollConsultarCitasPropias.setVisible(true);
			jScrollConsultarCitasPropias.repaint();
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

	public void desactivarConsultarCitaPropia() {
		jPanelListaOperaciones.quitarOperacion(OperacionesInterfaz.ConsultarCitasPropias);
	}

	public boolean hayOperacionesDisponibles() {
		return (jPanelListaOperaciones.getNumeroOperaciones() > 0);
	}

	public void beneficiarioActualizado(Beneficiario beneficiario) {
		// Redirigimos la operación al panel seleccionado
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
		case ConsultarCitasPropias:
			jPanelConsultarCitasPropias.beneficiarioActualizado(beneficiario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		// Redirigimos la operación al panel seleccionado
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
		case ConsultarCitasPropias:
			jPanelConsultarCitasPropias.beneficiarioEliminado(beneficiario);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioActualizado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
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
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		// Redirigimos la operación al panel seleccionado
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
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void citaRegistrada(Cita cita) {
		// Redirigimos la operación al panel seleccionado
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
		case ConsultarCitasPropias:
			jPanelConsultarCitasPropias.citaRegistrada(cita);
			break;	
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void citaAnulada(Cita cita) {
		// Redirigimos la operación al panel seleccionado
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
		case ConsultarCitasPropias:
			jPanelConsultarCitasPropias.citaAnulada(cita);
			break;	
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void sustitucionRegistrada(Sustitucion sustitucion) {
		// Redirigimos la operación al panel seleccionado
		switch(operacionSeleccionada) {
		case TramitarCita:
			jPanelTramitar.sustitucionRegistrada(sustitucion);
			break;
		case TramitarCitaVolante:
			jPanelVolanteTramitar.sustitucionRegistrada(sustitucion);
			break;
		default:
			// La operación no va a cambiar el estado del panel seleccionado
		}
	}
	
	public void restablecerPaneles() {
		jPanelConsultarCitasBeneficiario.restablecerPanel();
		jPanelConsultarCitasMedico.restablecerPanel();
		jPanelTramitar.restablecerPanel();
		jPanelVolanteTramitar.restablecerPanel();
		jPanelConsultarCitasPropias.restablecerPanel();
	}
	
	//$hide<<$
	
}
