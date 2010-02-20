package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import presentacion.auxiliar.BeneficiarioBuscadoListener;
import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.JDateChooserCitas;
import presentacion.auxiliar.ListCellRendererCitas;
import presentacion.auxiliar.UtilidadesListaHoras;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;
import excepciones.FechaNoValidaException;
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
	
	public JPCitaTramitar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		cambiarEstado(false);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 435));
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
				this.add(btnRegistrar, new AnchorConstraint(354, 12, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
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
				this.add(cmbHorasCitas, new AnchorConstraint(312, 12, 262, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbHorasCitas.setModel(new DefaultComboBoxModel(new String[] {}));
				cmbHorasCitas.setRenderer(new ListCellRendererCitas());
				cmbHorasCitas.setPreferredSize(new java.awt.Dimension(280, 23));
			}
			{
				dtcDiaCita = new JDateChooserCitas();
				this.add(dtcDiaCita, new AnchorConstraint(284, 12, 144, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
		} catch(Exception e) {
			e.printStackTrace();
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
			
			mostrarFechasyHorasLaborablesMedico();
		
		}
	}
	
	private void mostrarFechasyHorasLaborablesMedico() {
		try {
			
			// Para poder pedir cita, el beneficiario debe tener
			// un médico asignado
			if(beneficiario.getMedicoAsignado() == null) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "El beneficiario con NIF " + beneficiario.getNif() + " no puede pedir cita\nporque no tiene ningún médico asignado.");
			} else {
				
				// Consultamos al servidor toda la información
				// necesaria para el panel de tramitación
				diasOcupados = getControlador().consultarDiasCompletos(beneficiario.getMedicoAsignado().getDni());
				citasOcupadas = getControlador().consultarHorasCitasMedico(beneficiario.getMedicoAsignado().getDni());
				horasCitas = getControlador().consultarHorarioMedico(beneficiario.getMedicoAsignado().getDni());
				
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
	
	@SuppressWarnings("deprecation")
	private void dtcDiaCitaPropertyChange(PropertyChangeEvent evt) {
		UtilidadesListaHoras.obtenerListaHoras(dtcDiaCita, horasCitas, citasOcupadas, cmbHorasCitas);
	}
		
	@SuppressWarnings("deprecation")
	private void btnRegistrarActionPerformed(ActionEvent evt) {
		Date fecha, hora;
		
		try {
		
			// Comprobamos que la hora seleccionada sea válida
			if(!horaSeleccionadaValida()) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "Seleccione un día que sea laboral para el médico y una hora libre (no marcada en rojo) para registrar la cita.");
			} else {
				
				// Obtenemos la hora definitiva de la cita
				hora = Cita.horaCadenaCita(cmbHorasCitas.getSelectedItem().toString());
				fecha = dtcDiaCita.getDate();
				
				// Solicitamos la cita
				getControlador().pedirCita(beneficiario, beneficiario.getMedicoAsignado().getDni(), new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), hora.getHours(), hora.getMinutes()), IConstantes.DURACION_CITA);
				
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
	
	private void cambiarEstado(boolean estado) {
		btnRegistrar.setEnabled(estado);
		dtcDiaCita.setEnabled(estado);
		cmbHorasCitas.setEnabled(estado);
	}
	
	private void limpiarCamposTramitacion() {
		dtcDiaCita.setDate(null);
		UtilidadesListaHoras.rellenarListaHoras(cmbHorasCitas, null, null);
		cambiarEstado(false);
	}
	
	// Métodos públicos
	
	// <métodos del observador>
	
	public void citaRegistrada(Cita cita) {
		if(beneficiario != null && cita.getMedico().equals(beneficiario.getMedicoAsignado())) {
			// Otro cliente ha registrado una cita para el mismo médico que este beneficiario.
			// Se vuelven a recuperar las horas de ese médico, para marcar la hora que se ha registrado en otro cliente
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado otra cita para este médico.");
			mostrarFechasyHorasLaborablesMedico();
		}
	}
	
	public void restablecerPanel() {
		pnlBeneficiario.restablecerPanel();
		limpiarCamposTramitacion();		
	}
	
	//$hide<<$

}
