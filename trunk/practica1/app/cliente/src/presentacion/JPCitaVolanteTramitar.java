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
import java.text.SimpleDateFormat;
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
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import dominio.UtilidadesDominio;
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
	private JLabel lblMedico;
	private JTextField txtMedico;
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

	public JPCitaVolanteTramitar() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPCitaVolanteTramitar
	}
	
	public JPCitaVolanteTramitar(JFPrincipal frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		cambiarEstadoConsulta(false);
		cambiarEstadoTramitacion(false);
		volante = null;
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 493));
			{
				btnBuscarVolante = new JButton();
				this.add(btnBuscarVolante, new AnchorConstraint(228, 11, 557, 822, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscarVolante.setText("Buscar");
				btnBuscarVolante.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscarVolante.setName("btnBuscarVolante");
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
				txtNumeroVolante.setName("txtNumeroVolante");
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
				pnlBeneficiario.setPreguntarRegistro(true);
				pnlBeneficiario.setName("pnlBeneficiario");
				pnlBeneficiario.addBeneficiarioBuscadoListener(new BeneficiarioBuscadoListener() {
					public void beneficiarioBuscado(EventObject evt) {
						pnlBeneficiarioConsultarBeneficiarioBuscado(evt);
					}
				});
			}
			
			{
				lblHora = new JLabel();
				this.add(lblHora, new AnchorConstraint(391, 252, 657, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
				this.add(btnRegistrar, new AnchorConstraint(455, 11, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
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
				this.add(cmbHorasCitas, new AnchorConstraint(388, 12, 262, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
				this.add(dtcDiaCita, new AnchorConstraint(361, 12, 144, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcDiaCita.setPreferredSize(new java.awt.Dimension(280, 23));
				dtcDiaCita.setDateFormatString("dd/MM/yyyy");
				dtcDiaCita.setToolTipText("Formato dd/MM/yyyy. Haga clic en el icono de la derecha para desplegar un calendario.");
				dtcDiaCita.setMinSelectableDate(new Date());
				dtcDiaCita.setName("dtcDiaCita");
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
				txtCentro.setName("txtCentro");
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
				txtMedicoAsignado.setName("txtMedicoAsignado2");
			}
			{
				lblMedicoAsignado = new JLabel();
				this.add(lblMedicoAsignado, new AnchorConstraint(266, 277, 950, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedicoAsignado.setText("Médico");
				lblMedicoAsignado.setPreferredSize(new java.awt.Dimension(110, 18));
			}
			{
				lblMedico = new JLabel();
				this.add(lblMedico, new AnchorConstraint(418, 252, 895, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedico.setText("Médico");
				lblMedico.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				txtMedico = new JTextField();
				this.add(txtMedico, new AnchorConstraint(415, 12, 904, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtMedico.setEditable(false);
				txtMedico.setPreferredSize(new java.awt.Dimension(280, 23));
				txtMedico.setName("txtMedico");
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
			// Para poder pedir cita, el beneficiario debe tener
			// un médico asignado
			if(beneficiario.getMedicoAsignado() == null) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "El beneficiario seleccionado no puede pedir cita\nporque no tiene ningún médico asignado.");
			} else {
				// Activamos la búsqueda de volantes
				cambiarEstadoConsulta(true);
			}
		}
	}
	
	private void btnBuscarVolanteActionPerformed(ActionEvent evt) {
		SimpleDateFormat formatoFecha;
		
		// Borramos los datos de la última tramitación de cita
		limpiarCamposTramitacion();
		txtMedicoAsignado.setText("");
		txtCentro.setText("");
		
		try {
			
			// Comprobamos los datos del volante
			Validacion.comprobarVolante(txtNumeroVolante.getText().trim());
			
			// Recuperamos los datos del volante del servidor
			volante = getControlador().consultarVolante(Long.parseLong(txtNumeroVolante.getText().trim()));
			
			// Comprobamos si el volante ya se ha usado para dar una cita
			// o si no está asociado al beneficiario seleccionado
			if(volante.getCita() != null) {
				throw new IdVolanteIncorrectoException("El volante seleccionado ya se ha utilizado para pedir una cita y no se puede usar de nuevo.");
			}
			if(beneficiario != null && !volante.getBeneficiario().equals(beneficiario)) {
				throw new IdVolanteIncorrectoException("El volante seleccionado no fue emitido para el beneficiario con NIF " + beneficiario.getNif() + ".");
			}
			if(volante.getFechaCaducidad().before(new Date())) {
				formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
				throw new IdVolanteIncorrectoException("El volante seleccionado caducó el " + formatoFecha.format(volante.getFechaCaducidad()) + ".");
			}
			
			// Mostramos los datos del volante encontrado
			Dialogos.mostrarDialogoInformacion(null, "Búsqueda correcta", "Volante encontrado.");
			txtNumeroVolante.setText("");
			txtMedicoAsignado.setText(volante.getReceptor().getApellidos() + ", " + volante.getReceptor().getNombre() + " (" + volante.getReceptor().getNif() + ")");
			txtCentro.setText(volante.getReceptor().getCentroSalud().getNombre() + "; " + volante.getReceptor().getCentroSalud().getDireccion());
			
			// Mostramos las fechas y horas disponibles para pedir una cita
			mostrarHorasCitasMedico();

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
	
	private void mostrarHorasCitasMedico() {
		Vector<DiaSemana> diasDesactivados;
		Calendar cal;
		
		try {
			
			// Consultamos al servidor toda la información
			// necesaria para el panel de tramitación
			diasOcupados = getControlador().consultarDiasCompletos(volante.getReceptor().getNif());
			citasOcupadas = getControlador().consultarHorasCitasMedico(volante.getReceptor().getNif());
			horasCitas = getControlador().consultarHorarioMedico(volante.getReceptor().getNif());
						
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

			// Activamos la tramitación de citas
			cambiarEstadoTramitacion(true);
			
		} catch(SQLException e) {
			e.printStackTrace();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			e.printStackTrace();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			e.printStackTrace();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
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
				medico = getControlador().consultarMedicoCita(volante.getReceptor().getNif(), diaCita);
				if(medico.getNif().equals(volante.getReceptor().getNif())) {
					txtMedico.setText(medico.getApellidos() + ", " + medico.getNombre() + " (" + medico.getNif() + ")");
				} else {
					txtMedico.setText(medico.getApellidos() + ", " + medico.getNombre() + " (" + medico.getNif() + "), sustituye a " + volante.getReceptor().getApellidos() + ", " + volante.getReceptor().getNombre() + " (" + volante.getReceptor().getNif() + ")");
				}
			} else {
				txtMedico.setText("(fecha no válida)");
			}
			
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
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
		UtilidadesListaHoras.rellenarListaHoras(cmbHorasCitas, null, null);
		txtMedico.setText("");
		cambiarEstadoTramitacion(false);
	}

	// Métodos públicos

	// <métodos del observador>
	
	public void beneficiarioActualizado(Beneficiario beneficiario) {
		if (this.beneficiario!=null && beneficiario.getNif().equals(this.beneficiario.getNif()))
			pnlBeneficiario.beneficiarioActualizado(beneficiario);
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		if (this.beneficiario!=null && beneficiario.getNif().equals(this.beneficiario.getNif())) {
			pnlBeneficiario.beneficiarioEliminado(beneficiario);
			limpiarCamposConsulta();
			limpiarCamposTramitacion();
		}
		
	}
	
	public void usuarioActualizado(Usuario usuario) {
		if(beneficiario != null) {
			if (volante != null) {
				if (usuario.getRol() == RolesUsuario.Medico && ((Medico)usuario).getTipoMedico().getCategoria().equals(CategoriasMedico.Especialista)) {
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha modificado el especialista asociado al volante desde otro cliente.");
					// Otro cliente ha actualizado el especialista asignado al volante
					txtMedicoAsignado.setText(usuario.getApellidos() + ", " + usuario.getNombre() + " (" + usuario.getNif() + ")");
					// Se puede haber modificado el horario del médico, por lo que recargamos las horas disponibles para dar cita
					mostrarHorasCitasMedico();
				}
			}
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		if(beneficiario != null) {
			if (volante != null) {
				if (usuario.getRol() == RolesUsuario.Medico && ((Medico)usuario).getTipoMedico().getCategoria().equals(CategoriasMedico.Especialista)) {
					// Otro cliente ha eliminado el médico especialista asociado al volante
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha eliminado el especialista asociado al volante desde otro cliente.");
					restablecerPanel();
				}
			}
		}
	}
	
	public void citaRegistrada(Cita cita) {
		Volante vol;
		Date dia;
		int indHora;
		
		if(beneficiario != null && volante != null) {	
			// Se vuelve a consultar el volante, para ver si la cita que se ha registrado corresponde a este volante o no
			try {
				vol = getControlador().consultarVolante(volante.getId());
				if (vol.getCita()!= null && vol.getCita().equals(cita)) {
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado una cita desde otro cliente para este volante, por lo que éste ya no se puede utilizar.");
					restablecerPanel();
				}
				else if (cita.getMedico().equals(vol.getReceptor())) {
					// Otro cliente ha registrado una cita para el mismo médico del volante
					// Se vuelven a recuperar las horas de ese médico, para marcar la hora que se ha registrado en otro cliente
					dia = dtcDiaCita.getDate();
					indHora = cmbHorasCitas.getSelectedIndex();
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado una cita desde otro cliente para este médico el día " + Cita.cadenaDiaCita(cita.getFechaYHora()) + " a las " + Cita.cadenaHoraCita(cita.getFechaYHora()) + ".");
					mostrarHorasCitasMedico();
					dtcDiaCita.setDate(dia);
					cmbHorasCitas.setSelectedIndex(indHora);
				}
			} catch(SQLException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			} catch(RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			} catch(Exception e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			}
		}
	}
	
	public void citaAnulada(Cita cita) {
		Date dia;
		int indHora;
		
		if(beneficiario != null && volante != null) {
			if(volante.getCita() != null && volante.getCita().equals(cita)) {
				Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha anulado la cita desde otro cliente para este volante.");
				mostrarHorasCitasMedico();
			}
			else if (cita.getMedico().equals(volante.getReceptor())) {
				// Otro cliente ha anulado una cita para el mismo médico del volante
				// Se vuelven a recuperar las horas de ese médico, para marcar la hora que se ha quedado libre
				dia = dtcDiaCita.getDate();
				indHora = cmbHorasCitas.getSelectedIndex();
				Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha anulado una cita desde otro cliente para este médico el día " + Cita.cadenaDiaCita(cita.getFechaYHora()) + " a las " + Cita.cadenaHoraCita(cita.getFechaYHora()) + ".");
				mostrarHorasCitasMedico();
				dtcDiaCita.setDate(dia);
				cmbHorasCitas.setSelectedIndex(indHora);
			}
		}
	}

	public void sustitucionRegistrada(Sustitucion sustitucion) {
		if(beneficiario != null && volante.getReceptor().equals(sustitucion.getMedico())
		 && UtilidadesDominio.fechaIgual(sustitucion.getDia(), dtcDiaCita.getDate(), false)) {
			// Otro cliente ha registrado una sustitución para el médico
			// con el que se quiere pedir cita en el día seleccionado
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado una sustitución desde otro cliente para el médico del volante en el día seleccionado.");
			mostrarMedicoCita();
		}
	}
	
	public void restablecerPanel() {
		pnlBeneficiario.restablecerPanel();
		limpiarCamposConsulta();
		limpiarCamposTramitacion();
		beneficiario = null;
	}
	
	//$hide<<$

}
