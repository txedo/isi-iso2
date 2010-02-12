package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Volante;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;
import excepciones.FechaNoValidaException;
import excepciones.IdVolanteIncorrectoException;
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
* Panel que permite tramitar citas para beneficiarios del sistema a partir
* de los volantes emitidos por los médicos de cabecera.
*/
public class JPCitaVolanteTramitar extends JPBase {

	private static final long serialVersionUID = 8297107492599580450L;

	private Beneficiario beneficiario;
	private Volante volante;
	private Vector<Date> diasOcupados;
	private Hashtable<Date, Vector<String>> citasOcupadas;
	private Hashtable<DiaSemana, Vector<String>> horasCitas;
	
	private JLabel lblBuscarVolante;
	private JTextField txtNumeroVolante;
	private JTextField txtMedicoAsignado;
	private JLabel lblMedicoAsignado;
	private JTextField txtCentro;
	private JLabel lblCentro;
	private JButton btnBuscarVolante;
	private JLabel lblNumeroVolante;
	private JSeparator sepSeparador2;
	private JDateChooserCitas dtcDiaCita;
	private JLabel lblDatos;
	private JSeparator sepSeparador1;
	private JPBeneficiarioConsultar pnlBeneficiario;
	private JLabel lblHora;
	private JLabel lblDia;
	private JButton btnRegistrar;
	private JComboBox cmbHorasCitas;

	public JPCitaVolanteTramitar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		cambiarEstadoConsulta(false);
		cambiarEstadoTramitacion(false);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 485));
			{
				btnBuscarVolante = new JButton();
				this.add(btnBuscarVolante, new AnchorConstraint(228, 11, 557, 822, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscarVolante.setText("Buscar");
				btnBuscarVolante.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscarVolante.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarVolanteActionPerformed(evt);
					}
				});
			}
			{
				txtNumeroVolante = new JTextField();
				this.add(txtNumeroVolante, new AnchorConstraint(228, 83, 557, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNumeroVolante.setPreferredSize(new java.awt.Dimension(209, 23));
			}
			{
				lblNumeroVolante = new JLabel();
				this.add(lblNumeroVolante, new AnchorConstraint(231, 231, 548, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNumeroVolante.setText("Nº del volante");
				lblNumeroVolante.setPreferredSize(new java.awt.Dimension(90, 16));
			}
			{
				lblBuscarVolante = new JLabel();
				this.add(lblBuscarVolante, new AnchorConstraint(205, 208, 497, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblBuscarVolante.setText("Buscar volante:");
				lblBuscarVolante.setPreferredSize(new java.awt.Dimension(132, 16));
			}
			{
				lblDatos = new JLabel();
				this.add(lblDatos, new AnchorConstraint(337, 215, 633, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDatos.setText("Datos de la cita:");
				lblDatos.setPreferredSize(new java.awt.Dimension(83, 16));
			}
			{
				sepSeparador1 = new JSeparator();
				this.add(sepSeparador1, new AnchorConstraint(327, 6, 587, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador1.setPreferredSize(new java.awt.Dimension(418, 10));
			}
			{
				pnlBeneficiario = new JPBeneficiarioConsultar(this.getFrame(), this.getControlador());
				this.add(pnlBeneficiario, new AnchorConstraint(0, 0, 608, 0, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				pnlBeneficiario.setPreferredSize(new java.awt.Dimension(430, 182));
				pnlBeneficiario.reducirPanel();
				pnlBeneficiario.addBeneficiarioBuscadoListener(new BeneficiarioBuscadoListener() {
					public void beneficiarioBuscado(EventObject evt) {
						pnlBeneficiarioConsultarBeneficiarioBuscado(evt);
					}
				});
			}
			
			{
				lblHora = new JLabel();
				this.add(lblHora, new AnchorConstraint(393, 252, 657, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHora.setText("Hora");
				lblHora.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblDia = new JLabel();
				this.add(lblDia, new AnchorConstraint(365, 252, 591, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDia.setText("Día");
				lblDia.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				btnRegistrar = new JButton();
				this.add(btnRegistrar, new AnchorConstraint(432, 12, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnRegistrar.setText("Registrar cita");
				btnRegistrar.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRegistrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRegistrarActionPerformed(evt);
					}
				});
			}
			{
				cmbHorasCitas = new JComboBox();
				this.add(cmbHorasCitas, new AnchorConstraint(390, 12, 262, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbHorasCitas.setModel(new DefaultComboBoxModel(new String[] {}));
				cmbHorasCitas.setRenderer(new DefaultListCellRenderer());
				cmbHorasCitas.setPreferredSize(new java.awt.Dimension(280, 23));
			}
			{
				dtcDiaCita = new JDateChooserCitas();
				this.add(dtcDiaCita, new AnchorConstraint(361, 12, 144, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcDiaCita.setPreferredSize(new java.awt.Dimension(280, 23));
				dtcDiaCita.setDateFormatString("dd/MM/yyyy");
				dtcDiaCita.setToolTipText("Formato dd/MM/yyyy. Para más ayuda haga clic en el icono de la derecha.");
				dtcDiaCita.setMinSelectableDate(new Date());
				dtcDiaCita.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						dtcDiaCitaPropertyChange(evt);
					}
				});
			}
			{
				sepSeparador2 = new JSeparator();
				this.add(sepSeparador2, new AnchorConstraint(193, 6, 447, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador2.setPreferredSize(new java.awt.Dimension(418, 6));
			}
			{
				txtCentro = new JTextField();
				this.add(txtCentro, new AnchorConstraint(291, 12, 893, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCentro.setEditable(false);
				txtCentro.setPreferredSize(new java.awt.Dimension(280, 22));
			}
			{
				lblCentro = new JLabel();
				this.add(lblCentro, new AnchorConstraint(293, 277, 891, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCentro.setText("Centro asignado");
				lblCentro.setPreferredSize(new java.awt.Dimension(110, 18));
			}
			{
				txtMedicoAsignado = new JTextField();
				this.add(txtMedicoAsignado, new AnchorConstraint(264, 12, 953, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtMedicoAsignado.setEditable(false);
				txtMedicoAsignado.setPreferredSize(new java.awt.Dimension(280, 22));
			}
			{
				lblMedicoAsignado = new JLabel();
				this.add(lblMedicoAsignado, new AnchorConstraint(266, 277, 950, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedicoAsignado.setText("Médico");
				lblMedicoAsignado.setPreferredSize(new java.awt.Dimension(110, 18));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$

	private void pnlBeneficiarioConsultarBeneficiarioBuscado(EventObject evt) {
		// Borramos los datos de la última consulta de
		// volante y tramitación de cita
		limpiarCamposConsulta();
		limpiarCamposTramitacion();

		// Obtenemos el beneficiario que se ha buscado en el panel de consulta
		// (puede ser null si ocurrió un error al buscar el beneficiario)
		beneficiario = pnlBeneficiario.getBeneficiario();
		
		if(beneficiario != null) {
			// Activamos la búsqueda de volantes
			cambiarEstadoConsulta(true);
		}
	}
	
	private void btnBuscarVolanteActionPerformed(ActionEvent evt) {
		// Borramos los datos de la última tramitación de cita
		limpiarCamposTramitacion();
		
		try {
			
			//TODO: Comprobamos el id del volante
			//Validacion.comprobarVolante(txtNumeroVolante.getText().trim());
			
			//TODO: Creo que ahora mismo no se comprueba si el volante
			// está asociado al beneficiario, hacer!
			
			// Recuperamos los datos del volante del servidor
			volante = getControlador().consultarVolante(Long.parseLong(txtNumeroVolante.getText().trim()));
			
			// Si el volante ya se ha usado para dar una cita, mostramos un error
			if(volante.getCita() != null) {
				throw new IdVolanteIncorrectoException("El volante seleccionado ya se ha utilizado para pedir una cita y no se puede usar de nuevo.");
			}
			
			// Mostramos los datos del volante encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Búsqueda correcta", "Volante encontrado.");
			txtNumeroVolante.setText("");
			txtMedicoAsignado.setText(volante.getReceptor().getApellidos() + ", " + volante.getReceptor().getNombre() + " (" + volante.getReceptor().getDni() + ")");
			txtCentro.setText(volante.getReceptor().getCentroSalud().getNombre() + "; " + volante.getReceptor().getCentroSalud().getDireccion());
			
			// Consultamos al servidor toda la información
			// necesaria para el panel de tramitación
			diasOcupados = getControlador().consultarDiasCompletos(volante.getReceptor().getDni());
			citasOcupadas = getControlador().consultarCitasMedico(volante.getReceptor().getDni());
			horasCitas = getControlador().consultarHorarioMedico(volante.getReceptor().getDni());
			
			// Deshabilitamos los días de la semana que no son
			// laborables para el médico del beneficiario
			dtcDiaCita.quitarDiasSemanaDesactivados();
			for(DiaSemana dia : DiaSemana.values()) {
				if(horasCitas.get(dia) == null || horasCitas.get(dia).size() == 0) {
					dtcDiaCita.ponerDiaSemanaDesactivado(dia);
				}
			}
			
			// Deshabilitamos los días que el médico no puede
			// pasar consulta en el calendario del panel
			dtcDiaCita.quitarFechasDesactivadas();
			dtcDiaCita.setMinSelectableDate(new Date());
			for(Date dia : diasOcupados) {
				dtcDiaCita.ponerFechaDesactivada(dia);
			}
			
			// Activamos la tramitación de citas
			cambiarEstadoTramitacion(true);

		} catch(IdVolanteIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNumeroVolante.selectAll();
			txtNumeroVolante.grabFocus();
			
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void dtcDiaCitaPropertyChange(PropertyChangeEvent evt) {
		Vector<String> horas;
		Vector<String> horasOcupadas;
		Date fecha;
		Calendar cal;
		int añoAct, mesAct, diaAct;
		
		// Obtenemos la fecha de hoy
		cal = Calendar.getInstance();
		añoAct = cal.get(Calendar.YEAR);
		mesAct = cal.get(Calendar.MONTH);
		diaAct = cal.get(Calendar.DAY_OF_MONTH);
		
		fecha = dtcDiaCita.getDate();
		if(fecha != null) {
			// Comprobamos si el día seleccionado es anterior a hoy
			cal.setTime(fecha);
			if(cal.get(Calendar.YEAR) < añoAct
			 || (cal.get(Calendar.YEAR) == añoAct && cal.get(Calendar.MONTH) < mesAct)
			 || (cal.get(Calendar.YEAR) == añoAct && cal.get(Calendar.MONTH) == mesAct && cal.get(Calendar.DAY_OF_MONTH) < diaAct)) {
				desactivarListaHoras("El día seleccionado no es válido");
			} else {
				// Obtenemos la lista de horas disponibles para
				// el día de la semana correspondiente
				horas = new Vector<String>();
				cal.setTime(fecha);
				switch(cal.get(Calendar.DAY_OF_WEEK)) {
				case Calendar.MONDAY:
					horas.addAll(horasCitas.get(DiaSemana.Lunes));
					break;
				case Calendar.TUESDAY:
					horas.addAll(horasCitas.get(DiaSemana.Martes));
					break;
				case Calendar.WEDNESDAY:
					horas.addAll(horasCitas.get(DiaSemana.Miercoles));
					break;
				case Calendar.THURSDAY:
					horas.addAll(horasCitas.get(DiaSemana.Jueves));
					break;
				case Calendar.FRIDAY:
					horas.addAll(horasCitas.get(DiaSemana.Viernes));
					break;
				default:
					// Los médicos no trabajan los fines de semana
					break;
				}
				// Si la lista no tiene ninguna hora, desactivamos
				// la selección de hora para la cita
				if(horas.size() == 0) {
					desactivarListaHoras("El día seleccionado no es laboral para el médico");
				} else {
					// Obtenemos las horas del día que el médico ya tiene ocupadas
					horasOcupadas = citasOcupadas.get(new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
					// Rellenamos la lista de horas
					rellenarListaHoras(horas, horasOcupadas);
				}
			}
		}
	}
		
	@SuppressWarnings("deprecation")
	private void btnRegistrarActionPerformed(ActionEvent evt) {
		Date fecha, hora;
		
		try {
		
			// Comprobamos que la hora seleccionada sea válida
			if(!horaSeleccionadaValida()) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "Seleccione un día que sea laboral para el médico y una hora libre (no marcada en rojo).");
			} else {
				// Obtenemos la hora definitiva de la cita
				hora = Cita.horaCadenaCita(cmbHorasCitas.getSelectedItem().toString());
				fecha = dtcDiaCita.getDate();
				// Solicitamos la cita a partir del volante
				getControlador().pedirCita(beneficiario, volante.getId(), new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), hora.getHours(), hora.getMinutes()), IConstantes.DURACION_CITA);				
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "La cita ha quedado registrada.");
				pnlBeneficiario.limpiarCamposConsulta();
				limpiarCamposConsulta();
				limpiarCamposTramitacion();
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
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	}

	private void rellenarListaHoras(Vector<String> horas, Vector<String> horasOcupadas) {
		int i;
		
		// Actualizamos la lista de horas
		cmbHorasCitas.removeAllItems();
		if(horas != null) {
			for(String hora : horas) {
				if(horasOcupadas != null && horasOcupadas.contains(hora)) {
					cmbHorasCitas.addItem("<html><font color=\"#FF0000\">" + hora + "</font></html>");
				} else {
					cmbHorasCitas.addItem(hora);
				}
			}
		}
		
		// Seleccionamos la primera hora no ocupada
		if(horas != null && horas.size() > 0) {
			i = 0;
			while(i < horas.size() && ((String)cmbHorasCitas.getItemAt(i)).startsWith("<html>")) {
				i++;
			}
			if(i >= horas.size()) {
				cmbHorasCitas.setSelectedIndex(-1);
			} else {
				cmbHorasCitas.setSelectedIndex(i);
			}
		} else {
			cmbHorasCitas.setSelectedIndex(-1);
		}
		
		// Activamos el control
		cmbHorasCitas.setEnabled(true);
	}

	private void desactivarListaHoras(String mensaje) {
		cmbHorasCitas.removeAllItems();
		cmbHorasCitas.addItem(mensaje);
		cmbHorasCitas.setEnabled(false);
	}
	
	private boolean horaSeleccionadaValida() {
		boolean valido;
		
		// Sólo se devuelve true si hay una hora seleccionada
		// en la lista que no está de color rojo
		valido = true;
		if(!cmbHorasCitas.isEnabled()) {
			valido = false;
		} else {
			if(cmbHorasCitas.getSelectedIndex() == -1) {
				valido = false;
			} else if(((String)cmbHorasCitas.getSelectedItem()).startsWith("<html>")) {
				valido = false;
			}
		}
		
		return valido;
	}
	
	private void cambiarEstadoConsulta(boolean estado) {
		btnBuscarVolante.setEnabled(estado);
		txtNumeroVolante.setEnabled(estado);
		txtMedicoAsignado.setEnabled(estado);
		txtCentro.setEnabled(estado);
	}
	
	private void cambiarEstadoTramitacion(boolean estado) {
		btnRegistrar.setEnabled(estado);
		dtcDiaCita.setEnabled(estado);
		cmbHorasCitas.setEnabled(estado);
	}
	
	private void limpiarCamposConsulta() {
		txtNumeroVolante.setText("");
		txtMedicoAsignado.setText("");
		txtCentro.setText("");
		cambiarEstadoConsulta(false);
	}
	
	private void limpiarCamposTramitacion() {
		dtcDiaCita.setDate(null);
		rellenarListaHoras(null, null);
		cambiarEstadoTramitacion(false);
	}
	
	//$hide<<$

}
