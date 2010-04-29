package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import presentacion.auxiliar.BeneficiarioBuscadoListener;
import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.JDateChooserCitas;
import presentacion.auxiliar.ListCellRendererCitas;
import presentacion.auxiliar.UtilidadesListaHoras;
import presentacion.auxiliar.Validacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import dominio.UtilidadesDominio;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;
import excepciones.FechaCitaIncorrectaException;
import excepciones.FechaNoValidaException;
import excepciones.FormatoFechaIncorrectoException;
import excepciones.MedicoInexistenteException;

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
 * Panel que permite tramitar citas para beneficiarios del sistema.
 */
public class JPCitaTramitar extends JPBase {

	private static final long serialVersionUID = 8297107492599580450L;

	private Beneficiario beneficiario;
	private Vector<Date> diasOcupados;
	private JTextField txtMedico;
	private JLabel lblMedico;
	private Hashtable<Date, Vector<String>> citasOcupadas;
	private Hashtable<DiaSemana, Vector<String>> horasCitas;

	private JDateChooserCitas dtcDiaCita;
	private JLabel lblDatos;
	private JSeparator sepSeparador;
	private JPBeneficiarioConsultar pnlBeneficiario;
	private JLabel lblHora;
	private JLabel lblDia;
	private JButton btnRegistrar;
	private JComboBox cmbHorasCitas;

	public JPCitaTramitar() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPCitaTramitar
	}
	
	public JPCitaTramitar(JFPrincipal frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		cambiarEstado(false);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 416));
			{
				txtMedico = new JTextField();
				this.add(txtMedico, new AnchorConstraint(339, 12, 833, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtMedico.setPreferredSize(new java.awt.Dimension(280, 23));
				txtMedico.setEditable(false);
				txtMedico.setName("txtMedico");
			}
			{
				lblDatos = new JLabel();
				this.add(lblDatos, new AnchorConstraint(259, 215, 633, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDatos.setText("Datos de la cita:");
				lblDatos.setPreferredSize(new java.awt.Dimension(83, 16));
			}
			{
				sepSeparador = new JSeparator();
				this.add(sepSeparador, new AnchorConstraint(249, 6, 587, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador.setPreferredSize(new java.awt.Dimension(418, 10));
			}
			{
				pnlBeneficiario = new JPBeneficiarioConsultar(this.getFrame(), this.getControlador());
				this.add(pnlBeneficiario, new AnchorConstraint(0, 0, 608, 0, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				pnlBeneficiario.setPreferredSize(new java.awt.Dimension(430, 237));
				pnlBeneficiario.reducirPanel();
				pnlBeneficiario.setPreguntarRegistro(true);
				pnlBeneficiario.setName("pnlBeneficiario");
				pnlBeneficiario.addBeneficiarioBuscadoListener(new BeneficiarioBuscadoListener() {
					public void beneficiarioBuscado(EventObject evt) {
						pnlBeneficiarioBeneficiarioBuscado(evt);
					}
				});
			}
			
			{
				lblHora = new JLabel();
				this.add(lblHora, new AnchorConstraint(315, 252, 657, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHora.setText("Hora");
				lblHora.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblDia = new JLabel();
				this.add(lblDia, new AnchorConstraint(287, 252, 591, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDia.setText("Día");
				lblDia.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				btnRegistrar = new JButton();
				this.add(btnRegistrar, new AnchorConstraint(378, 11, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnRegistrar.setText("Registrar cita");
				btnRegistrar.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRegistrar.setName("btnRegistrar");
				btnRegistrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRegistrarActionPerformed(evt);
					}
				});
			}
			{
				cmbHorasCitas = new JComboBox();
				this.add(cmbHorasCitas, new AnchorConstraint(312, 12, 262, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbHorasCitas.setModel(new DefaultComboBoxModel(new String[] {}));
				cmbHorasCitas.setRenderer(new ListCellRendererCitas());
				cmbHorasCitas.setPreferredSize(new java.awt.Dimension(280, 23));
				cmbHorasCitas.setName("cmbHorasCitas");
				cmbHorasCitas.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						cmbHorasCitasItemStateChanged(evt);
					}
				});
			}
			{
				dtcDiaCita = new JDateChooserCitas();
				this.add(dtcDiaCita, new AnchorConstraint(284, 12, 144, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcDiaCita.setPreferredSize(new java.awt.Dimension(280, 23));
				dtcDiaCita.setDateFormatString("dd/MM/yyyy");
				dtcDiaCita.setToolTipText("Formato dd/MM/yyyy. Haga clic en el icono de la derecha para desplegar un calendario.");
				dtcDiaCita.setMinSelectableDate(new Date());
				dtcDiaCita.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						dtcDiaCitaPropertyChange(evt);
					}
				});
			}
			{
				lblMedico = new JLabel();
				this.add(lblMedico, new AnchorConstraint(342, 252, 824, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedico.setText("Médico");
				lblMedico.setPreferredSize(new java.awt.Dimension(99, 16));
			}
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		}
	}

	//$hide>>$

	private void pnlBeneficiarioBeneficiarioBuscado(EventObject evt) {
		// Borramos los datos de la última tramitación de cita
		limpiarCamposTramitacion();

		// Obtenemos el beneficiario que se ha buscado en el panel de consulta
		// (puede ser null si ocurrió un error al buscar el beneficiario)
		beneficiario = pnlBeneficiario.getBeneficiario();
		
		if(beneficiario != null) {
			// Mostramos las fechas y horas disponibles para pedir una cita
			mostrarHorasCitasMedico();
		}
	}
	
	private void mostrarHorasCitasMedico() {
		Vector<DiaSemana> diasDesactivados;
		Calendar cal;

		try {
			
			// Para poder pedir cita, el beneficiario debe tener
			// un médico asignado
			if(beneficiario.getMedicoAsignado() == null) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "El beneficiario seleccionado no puede pedir cita\nporque no tiene ningún médico asignado.");
			} else {
				
				// Consultamos al servidor toda la información
				// necesaria para el panel de tramitación
				diasOcupados = getControlador().consultarDiasCompletos(beneficiario.getMedicoAsignado().getNif());
				citasOcupadas = getControlador().consultarHorasCitasMedico(beneficiario.getMedicoAsignado().getNif());
				horasCitas = getControlador().consultarHorarioMedico(beneficiario.getMedicoAsignado().getNif());
				
				// Deshabilitamos los días de la semana que no son
				// laborables para el médico del beneficiario
				dtcDiaCita.quitarDiasSemanaDesactivados();
				diasDesactivados = new Vector<DiaSemana>();
				for(DiaSemana dia : DiaSemana.values()) {
					if(horasCitas.get(dia) == null || horasCitas.get(dia).size() == 0) {
						dtcDiaCita.ponerDiaSemanaDesactivado(dia);
						diasDesactivados.add(dia);
					}
				}
				
				// Deshabilitamos los días que el médico no puede
				// pasar consulta en el calendario del panel
				dtcDiaCita.quitarFechasDesactivadas();
				dtcDiaCita.setMinSelectableDate(new Date());
				for(Date dia : diasOcupados) {
					dtcDiaCita.ponerFechaDesactivada(dia);
				}

				// Buscamos el primer día y hora disponible para una cita
				if(beneficiario.getMedicoAsignado().getCalendario().size() == 0) {
					dtcDiaCita.setDate(new Date());
				} else {
					cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.set(Calendar.HOUR, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					while(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					      || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
					      || diasDesactivados.contains(UtilidadesDominio.diaFecha(cal.getTime()))
					      || diasOcupados.contains(cal.getTime())) {
						cal.add(Calendar.DAY_OF_MONTH, 1);
					}
					dtcDiaCita.setDate(cal.getTime());
				}
				UtilidadesListaHoras.obtenerListaHoras(dtcDiaCita, horasCitas, citasOcupadas, cmbHorasCitas);

				// Activamos el registro de citas
				cambiarEstado(true);
				
			}
			
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	}
	
	private void dtcDiaCitaPropertyChange(PropertyChangeEvent evt) {
		UtilidadesListaHoras.obtenerListaHoras(dtcDiaCita, horasCitas, citasOcupadas, cmbHorasCitas);
	}
	
	private void cmbHorasCitasItemStateChanged(ItemEvent evt) {
		if(evt.getStateChange() == ItemEvent.SELECTED) {
			mostrarMedicoCita();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void mostrarMedicoCita() {
		Date fecha, hora, diaCita;
		Medico medico;
		
		try {
			
			if(cmbHorasCitas.getSelectedItem() != null && horaSeleccionadaValida()) {
				// Obtenemos la hora de la cita
				hora = Cita.horaCadenaCita(cmbHorasCitas.getSelectedItem().toString());
				fecha = dtcDiaCita.getDate();
				diaCita = new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), hora.getHours(), hora.getMinutes());
				// Consultamos qué médico daría realmente la cita
				medico = getControlador().consultarMedicoCita(beneficiario.getMedicoAsignado().getNif(), diaCita);
				if(medico.getNif().equals(beneficiario.getMedicoAsignado().getNif())) {
					txtMedico.setText(medico.getApellidos() + ", " + medico.getNombre() + " (" + medico.getNif() + ")");
				} else {
					txtMedico.setText(medico.getApellidos() + ", " + medico.getNombre() + " (" + medico.getNif() + "), sustituye a " + beneficiario.getMedicoAsignado().getApellidos() + ", " + beneficiario.getMedicoAsignado().getNombre() + " (" + beneficiario.getMedicoAsignado().getNif() + ")");
				}
			} else {
				txtMedico.setText("(fecha no válida)");
			}
			
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch (ParseException e) {
			// Esta excepción no se maneja para que no se muestren mensajes inesperados
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void btnRegistrarActionPerformed(ActionEvent evt) {
		Date fecha, hora;
		
		try {
			Validacion.comprobarFechaCita(dtcDiaCita.getDate());
			// Comprobamos que la hora seleccionada sea válida
			if(!horaSeleccionadaValida()) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "Seleccione un día que sea laboral para el médico y una hora libre (no marcada en rojo) para registrar la cita.");
			} else {
				// Obtenemos la hora definitiva de la cita
				hora = Cita.horaCadenaCita(cmbHorasCitas.getSelectedItem().toString());
				fecha = dtcDiaCita.getDate();
				
				// Solicitamos la cita
				getControlador().pedirCita(beneficiario, beneficiario.getMedicoAsignado().getNif(), new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), hora.getHours(), hora.getMinutes()), IConstantes.DURACION_CITA);
				
				// Mostramos el resultado de la operación y limpiamos el panel
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "La cita ha quedado registrada.");
				restablecerPanel();
			}

		} catch(ParseException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "La fecha seleccionada no tiene un formato válido.");
		} catch(MedicoInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		} catch(BeneficiarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		} catch(FechaNoValidaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch (FormatoFechaIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch (FechaCitaIncorrectaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
		
	}

	private boolean horaSeleccionadaValida() {
		Vector<Object> desactivadas;
		boolean valido;
		
		// Sólo se devuelve true si hay una hora seleccionada
		// en la lista que no está de color rojo
		valido = true;
		if(!cmbHorasCitas.isEnabled()) {
			valido = false;
		} else {
			if(cmbHorasCitas.getSelectedIndex() == -1) {
				valido = false;
			} else {
				desactivadas = ((ListCellRendererCitas)cmbHorasCitas.getRenderer()).getElementosDesactivados();
				if(desactivadas.contains(cmbHorasCitas.getSelectedItem())) {
					valido = false;
				}
			}
		}
		
		return valido;
	}
	
	private void cambiarEstado(boolean estado) {
		btnRegistrar.setEnabled(estado);
		dtcDiaCita.setEnabled(estado);
		cmbHorasCitas.setEnabled(estado);
	}
	
	private void limpiarCamposTramitacion() {
		dtcDiaCita.setDate(null);
		UtilidadesListaHoras.rellenarListaHoras(cmbHorasCitas, null, null);
		txtMedico.setText("");
		cambiarEstado(false);
	}
	
	// Métodos públicos
	
	public void beneficiarioActualizado(Beneficiario beneficiario) {
		pnlBeneficiario.beneficiarioActualizado(beneficiario);
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		if (this.beneficiario!=null && beneficiario.getNif().equals(this.beneficiario.getNif())) {
			pnlBeneficiario.beneficiarioEliminado(beneficiario);
			limpiarCamposTramitacion();
		}
	}
	
	public void usuarioActualizado(Usuario usuario) {
		if(beneficiario != null && usuario.getRol() == Roles.Médico
		 && beneficiario.getMedicoAsignado().getNif().equals(((Medico)usuario).getNif())) {
			// Otro cliente ha actualizado el médico asignado al beneficiario que pide la cita
			pnlBeneficiario.usuarioActualizado(usuario);
			// Se puede haber modificado el horario del médico, por lo que recargamos las horas disponibles para dar cita
			mostrarHorasCitasMedico();
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		if(beneficiario != null && usuario.getRol() == Roles.Médico
		 && beneficiario.getMedicoAsignado().getNif().equals(((Medico)usuario).getNif())) {
			// Otro cliente ha eliminado el médico asignado al beneficiario que pide la cita
			pnlBeneficiario.usuarioEliminado(usuario);
			limpiarCamposTramitacion();
		}
	}
	
	public void citaRegistrada(Cita cita) {
		Date dia;
		int indHora;
		
		if(beneficiario != null && cita.getMedico().getNif().equals(beneficiario.getMedicoAsignado().getNif())) {
			// Otro cliente ha registrado una cita para el mismo médico que
			// este beneficiario; se vuelven a recuperar las horas de ese médico,
			// para marcar la hora que se ha registrado en otro cliente
			dia = dtcDiaCita.getDate();
			indHora = cmbHorasCitas.getSelectedIndex();
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado una cita desde otro cliente para este médico el día " + Cita.cadenaDiaCita(cita.getFechaYHora()) + " a las " + Cita.cadenaHoraCita(cita.getFechaYHora()) + ".");
			mostrarHorasCitasMedico();
			dtcDiaCita.setDate(dia);
			cmbHorasCitas.setSelectedIndex(indHora);
		}
	}
	
	public void citaAnulada(Cita cita) {
		Date dia;
		int indHora;
		
		if(beneficiario != null && cita.getMedico().getNif().equals(beneficiario.getMedicoAsignado().getNif())) {
			// Otro cliente ha anulado una cita para el mismo médico que este beneficiario.
			// Se vuelven a recuperar las horas de ese médico, para marcar la hora que se ha quedado libre
			dia = dtcDiaCita.getDate();
			indHora = cmbHorasCitas.getSelectedIndex();
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha anulado una cita desde otro cliente para este médico el día " + Cita.cadenaDiaCita(cita.getFechaYHora()) + " a las " + Cita.cadenaHoraCita(cita.getFechaYHora()) + ".");
			mostrarHorasCitasMedico();
			dtcDiaCita.setDate(dia);
			cmbHorasCitas.setSelectedIndex(indHora);
		}
	}
	
	public void sustitucionRegistrada(Sustitucion sustitucion) {
		if(beneficiario != null && beneficiario.getMedicoAsignado().getNif().equals(sustitucion.getMedico().getNif())
		 && UtilidadesDominio.fechaIgual(sustitucion.getDia(), dtcDiaCita.getDate(), false)) {
			// Otro cliente ha registrado una sustitución para el médico
			// con el que se quiere pedir cita en el día seleccionado
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado una sustitución desde otro cliente para el médico del beneficiario en el día seleccionado.");
			mostrarMedicoCita();
		}
	}
		
	public void restablecerPanel() {
		pnlBeneficiario.restablecerPanel();
		limpiarCamposTramitacion();
		beneficiario = null;
	}
	
	//$hide<<$

}
