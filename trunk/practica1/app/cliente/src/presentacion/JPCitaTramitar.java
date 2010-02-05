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
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Validacion;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CadenaVaciaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;

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
	
	private final String ID_NIF = "NIF";
	private final String ID_NSS = "NSS";

	private Beneficiario beneficiario;
	private Vector<Date> diasOcupados;
	private Hashtable<Date, Vector<String>> citasOcupadas;
	private Hashtable<DiaSemana, Vector<String>> horasCitas;

	private JComboBox cmbIdentificacion;
	private ComboBoxModel cmbIdentificacionModel;
	private JDateChooserCitas dtcDiaCita;
	private JLabel lblMedico;
	private JTextField txtCentro;
	private JLabel lblCentro;
	private JLabel lblHora;
	private JLabel lblDia;
	private JButton btnRegistrar;
	private JTextField txtMedico;
	private JComboBox cmbHorasCitas;	
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JLabel lblNSS;
	private JLabel lblNIF;
	private JTextField txtNSS;
	private JTextField txtNombre;
	private JTextField txtNIF;
	private JTextField txtApellidos;
	private JLabel lblBuscar;
	private JButton btnBuscar;
	private JTextField txtIdentificacion;

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
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				lblCentro = new JLabel();
				this.add(lblCentro, new AnchorConstraint(216, 312, 606, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCentro.setText("Centro asignado");
				lblCentro.setPreferredSize(new java.awt.Dimension(108, 16));
			}
			{
				lblBuscar = new JLabel();
				this.add(lblBuscar, new AnchorConstraint(14, 264, 90, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblBuscar.setText("Buscar beneficiario por:");
				lblBuscar.setPreferredSize(new java.awt.Dimension(156, 14));
			}
			{
				cmbIdentificacion = new JComboBox();
				this.add(cmbIdentificacion, new AnchorConstraint(37, 236, 188, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbIdentificacion.setPreferredSize(new java.awt.Dimension(100, 22));
				cmbIdentificacionModel = new DefaultComboBoxModel(new String[] { ID_NIF, ID_NSS });
				cmbIdentificacion.setModel(cmbIdentificacionModel);
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(161, 310, 522, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos");
				lblApellidos.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(133, 310, 433, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre");
				lblNombre.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNSS = new JLabel();
				this.add(lblNSS, new AnchorConstraint(105, 310, 344, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNSS.setText("NSS");
				lblNSS.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(77, 310, 261, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF");
				lblNIF.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			
			{
				lblHora = new JLabel();
				this.add(lblHora, new AnchorConstraint(273, 321, 657, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHora.setText("Hora de la cita");
				lblHora.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblMedico = new JLabel();
				this.add(lblMedico, new AnchorConstraint(188, 312, 508, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedico.setText("Médico asignado");
				lblMedico.setPreferredSize(new java.awt.Dimension(108, 16));
			}
			{
				lblDia = new JLabel();
				this.add(lblDia, new AnchorConstraint(243, 321, 591, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDia.setText("Día de la cita");
				lblDia.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(157, 12, 534, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(280, 23));
				txtApellidos.setEditable(false);
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(129, 12, 442, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(280, 23));
				txtNombre.setEditable(false);
			}
			{
				txtNSS = new JTextField();
				this.add(txtNSS, new AnchorConstraint(101, 12, 357, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNSS.setPreferredSize(new java.awt.Dimension(280, 23));
				txtNSS.setEditable(false);
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(73, 12, 271, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(280, 23));
				txtNIF.setEditable(false);
			}
			{
				btnRegistrar = new JButton();
				this.add(btnRegistrar, new AnchorConstraint(312, 12, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnRegistrar.setText("Registrar cita");
				btnRegistrar.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRegistrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRegistrarActionPerformed(evt);
					}
				});
			}
			{
				txtIdentificacion = new JTextField();
				this.add(txtIdentificacion, new AnchorConstraint(36, 83, 188, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtIdentificacion.setPreferredSize(new java.awt.Dimension(209, 23));
				txtIdentificacion.setDragEnabled(true);
			}
			{
				txtMedico = new JTextField();
				this.add(txtMedico, new AnchorConstraint(185, 12, 519, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtMedico.setPreferredSize(new java.awt.Dimension(280, 23));
				txtMedico.setEditable(false);
			}
			{
				cmbHorasCitas = new JComboBox();
				this.add(cmbHorasCitas, new AnchorConstraint(270, 12, 262, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbHorasCitas.setModel(new DefaultComboBoxModel(new String[] {}));
				cmbHorasCitas.setRenderer(new DefaultListCellRenderer());
				cmbHorasCitas.setPreferredSize(new java.awt.Dimension(280, 23));
			}
			{
				dtcDiaCita = new JDateChooserCitas();
				this.add(dtcDiaCita, new AnchorConstraint(241, 12, 144, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
				btnBuscar = new JButton();
				this.add(btnBuscar, new AnchorConstraint(36, 11, 152, 847, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscar.setDefaultCapable(true);
				btnBuscar.setText("Buscar");
				btnBuscar.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarActionPerformed(evt);
					}
				});
			}
			{
				txtCentro = new JTextField();
				this.add(txtCentro, new AnchorConstraint(213, 12, 616, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCentro.setEditable(false);
				txtCentro.setPreferredSize(new java.awt.Dimension(280, 23));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
		
	private void btnBuscarActionPerformed(ActionEvent evt) {
		String identificacion, tipo;
		
		// Borramos la información del antiguo beneficiario consultado
		limpiarCamposConsulta();
		
		try {

			// Obtenemos el identificador para buscar el beneficiario
			identificacion = txtIdentificacion.getText().toUpperCase();
			tipo = (String)cmbIdentificacion.getSelectedItem();
			if(identificacion.equals("")) {
				throw new CadenaVaciaException();
			}

			// Validamos el identificador y buscamos el beneficiario solicitado
			if(tipo.equals(ID_NIF)) {
				Validacion.comprobarNIF(identificacion);
				beneficiario = getControlador().consultarBeneficiario(identificacion);
			} else if(tipo.equals(ID_NSS)) {
				Validacion.comprobarNSS(identificacion);
				beneficiario = getControlador().consultarBeneficiarioPorNSS(identificacion);
			}

			// Mostramos los datos del beneficiario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Búsqueda correcta", "Beneficiario encontrado.");			
			txtIdentificacion.setText("");
			txtNIF.setText(beneficiario.getNif());
			txtNSS.setText(beneficiario.getNss());
			txtNombre.setText(beneficiario.getNombre());
			txtApellidos.setText(beneficiario.getApellidos());
			txtMedico.setText(beneficiario.getMedicoAsignado().getNombre() + " " + beneficiario.getMedicoAsignado().getApellidos() + " (" + beneficiario.getMedicoAsignado().getDni() + ")");
			txtCentro.setText(beneficiario.getMedicoAsignado().getCentroSalud().getNombre() + "; " + beneficiario.getMedicoAsignado().getCentroSalud().getDireccion());
			cambiarEstado(true);

			// Consultamos al servidor toda la información
			// necesaria para el panel de tramitación
			diasOcupados = getControlador().consultarDiasCompletos(beneficiario.getMedicoAsignado().getDni());
			citasOcupadas = getControlador().consultarCitasMedico(beneficiario.getMedicoAsignado().getDni());
			horasCitas = getControlador().consultarHorasCitas(beneficiario.getMedicoAsignado().getDni());
			
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

		} catch(BeneficiarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();	

		} catch(CadenaVaciaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un NIF o un NSS.");
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();
		} catch(NIFIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();
		} catch(NSSIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();
			
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
				// Solicitamos la cita
				getControlador().pedirCita(beneficiario, beneficiario.getMedicoAsignado().getDni(), new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), hora.getHours(), hora.getMinutes()), IConstantes.DURACION_CITA);				
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "La cita ha quedado registrada.");
				limpiarCamposConsulta();
			}

		} catch(ParseException e) {
			limpiarCamposConsulta();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "La fecha seleccionada no tiene un formato válido.");

		} catch(MedicoInexistenteException e) {
			limpiarCamposConsulta();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		} catch(BeneficiarioInexistenteException e) {
			limpiarCamposConsulta();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		} catch(FechaNoValidaException e) {
			limpiarCamposConsulta();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());

		} catch(SQLException e) {
			limpiarCamposConsulta();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			limpiarCamposConsulta();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			limpiarCamposConsulta();
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
	
	private void cambiarEstado(boolean estado) {
		btnRegistrar.setEnabled(estado);
		dtcDiaCita.setEnabled(estado);
		cmbHorasCitas.setEnabled(estado);
	}
	
	private void limpiarCamposConsulta() {
		txtNIF.setText("");
		txtNSS.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtMedico.setText("");
		txtCentro.setText("");
		dtcDiaCita.setDate(null);
		rellenarListaHoras(null, null);
		cambiarEstado(false);
	}
	
	//$hide<<$

}
