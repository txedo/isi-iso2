package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import com.toedter.calendar.JDateChooser;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
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
public class JPCitaTramitar extends JPBase implements IConstantes {

	private static final long serialVersionUID = 8297107492599580450L;
	
	private final int SABADO = 6;
	private final int DOMINGO = 0;
	private final String ID_NIF = "NIF";
	private final String ID_NSS = "NSS";
	
	private ComboBoxModel cmbIdentificacionModel;
	private JDateChooser dtcDiaCita;
	private JLabel lblMedico;
	private JLabel lblHora;
	private JLabel lblDia;
	private JButton btnAceptar;
	private JTextField txtMedico;
	private JComboBox cbHorasCitas;	
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
	private JTextField txtIdVolante;
	private JLabel lblVolante;
	
	private ArrayList<String> horasTrabajoDia;
	private ArrayList<String> horasOcupadasDia;
	private JComboBox cmbIdentificacion;
	
	private Beneficiario beneficiario;
    private String horaSeleccionada = "";

	public JPCitaTramitar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		crearModelos(new String [] {""});
	}
	
	private void crearModelos(String [] elementos) {
		ComboBoxModel cbHorasCitasModel = new DefaultComboBoxModel(elementos);
		cbHorasCitas.setModel(cbHorasCitasModel);
		cbHorasCitas.setSelectedIndex(0);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				txtIdVolante = new JTextField();
				this.add(txtIdVolante, new AnchorConstraint(265, 83, 739, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtIdVolante.setPreferredSize(new java.awt.Dimension(209, 23));
			}
			{
				lblVolante = new JLabel();
				this.add(lblVolante, new AnchorConstraint(268, 350, 729, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblVolante.setText("Volante");
				lblVolante.setPreferredSize(new java.awt.Dimension(68, 16));
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
				this.add(lblApellidos, new AnchorConstraint(149, 310, 522, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos");
				lblApellidos.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(121, 310, 433, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre");
				lblNombre.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNSS = new JLabel();
				this.add(lblNSS, new AnchorConstraint(93, 310, 344, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNSS.setText("NSS");
				lblNSS.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(67, 310, 261, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF");
				lblNIF.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			
			{
				lblHora = new JLabel();
				this.add(lblHora, new AnchorConstraint(240, 350, 657, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHora.setText("Hora cita");
				lblHora.setPreferredSize(new java.awt.Dimension(68, 16));
			}
			{
				lblMedico = new JLabel();
				this.add(lblMedico, new AnchorConstraint(182, 310, 508, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedico.setText("Medico asignado");
				lblMedico.setPreferredSize(new java.awt.Dimension(108, 16));
			}
			{
				lblDia = new JLabel();
				this.add(lblDia, new AnchorConstraint(210, 363, 591, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDia.setText("Dia cita");
				lblDia.setPreferredSize(new java.awt.Dimension(55, 16));
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(145, 83, 534, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(209, 23));
				txtApellidos.setFocusable(false);
				txtApellidos.setEditable(false);
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(117, 83, 442, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(209, 23));
				txtNombre.setFocusable(false);
				txtNombre.setEditable(false);
			}
			{
				txtNSS = new JTextField();
				this.add(txtNSS, new AnchorConstraint(89, 83, 357, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNSS.setPreferredSize(new java.awt.Dimension(209, 23));
				txtNSS.setFocusable(false);
				txtNSS.setEditable(false);
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(63, 83, 271, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(209, 23));
				txtNIF.setFocusable(false);
				txtNIF.setEditable(false);
			}
			{
				btnAceptar = new JButton();
				this.add(btnAceptar, new AnchorConstraint(330, 20, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnAceptar.setText("Aceptar");
				btnAceptar.setPreferredSize(new java.awt.Dimension(77, 23));
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAceptarActionPerformed(evt);
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
				this.add(txtMedico, new AnchorConstraint(179, 83, 519, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtMedico.setPreferredSize(new java.awt.Dimension(209, 23));
				txtMedico.setFocusable(false);
				txtMedico.setEditable(false);
			}
			{
				cbHorasCitas = new JComboBox();
				this.add(cbHorasCitas, new AnchorConstraint(237, 83, 262, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cbHorasCitas.setPreferredSize(new java.awt.Dimension(209, 23));
				cbHorasCitas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cbHorasCitasActionPerformed(evt);
					}
				});
			}
			{
				dtcDiaCita = new JDateChooser();
				this.add(dtcDiaCita, new AnchorConstraint(208, 83, 144, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcDiaCita.setPreferredSize(new java.awt.Dimension(209, 23));
				dtcDiaCita.setDateFormatString("dd/MM/yyyy");
				dtcDiaCita.setToolTipText("Formato dd/MM/yyyy. Para más ayuda haga clic en el icono de la derecha.");
				dtcDiaCita.setMinSelectableDate(new Date());
				dtcDiaCita.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						dtcDiaCitaPropertyChange(evt);
					}
				});
				
			{
				btnBuscar = new JButton();
				this.add(btnBuscar, new AnchorConstraint(36, 11, 152, 847, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscar.setDefaultCapable(true);
				//jPanelConsultar.getRootPane().setDefaultButton(btnBuscar);
				btnBuscar.setText("Buscar");
				btnBuscar.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarActionPerformed(evt);
					}
				});
			}
				
			cambiarEstado(false);

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		String sIdentificacion;
		String sTipo;
		
		try {

			// Obtenemos el identificador para buscar el beneficiario
			sIdentificacion = txtIdentificacion.getText().toUpperCase();
			sTipo = (String)cmbIdentificacion.getSelectedItem();
			if(sIdentificacion.equals("")) {
				throw new CadenaVaciaException();
			}

			// Buscamos el beneficiario solicitado
			if(sTipo.equals(ID_NIF)) {
				Validacion.comprobarNIF(sIdentificacion);
				beneficiario = getControlador().getBeneficiario(sIdentificacion);
			} else if(sTipo.equals(ID_NSS)) {
				Validacion.comprobarNSS(sIdentificacion);
				beneficiario = getControlador().getBeneficiarioPorNSS(sIdentificacion);
			}

			// Mostramos los datos del beneficiario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la búsqueda", "Beneficiario encontrado.");			
			cambiarEstado(true);
			
			txtIdentificacion.setText("");
			txtNIF.setText(beneficiario.getNif());
			txtNSS.setText(beneficiario.getNss());
			txtNombre.setText(beneficiario.getNombre());
			txtApellidos.setText(beneficiario.getApellidos());
			txtMedico.setText(beneficiario.getMedicoAsignado().getDni());

		} catch(SQLException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(BeneficiarioInexistenteException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El beneficiario no se encuentra dado de alta en el sistema.");
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();	
		} catch(CadenaVaciaException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un NIF o NSS.");
			txtIdentificacion.grabFocus();
		} catch(NIFIncorrectoException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir NIF válido.");
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();
		} catch(NSSIncorrectoException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un NSS válido.");
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();
			
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void limpiarCamposConsultar() {
		txtNIF.setText("");
		txtNSS.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtMedico.setText("");
		txtIdVolante.setText("");
		dtcDiaCita.setDate(null);
		cambiarEstado(false);
		crearModelos(new String [] {""});
	}
	
	private void cambiarEstado(boolean estado) {
		btnAceptar.setEnabled(estado);
		dtcDiaCita.setEnabled(estado);
		cbHorasCitas.setEnabled(estado);
		txtIdVolante.setEditable(estado);
		txtIdVolante.setFocusable(estado);
	}
	
	// Cuando se selecciona un día en el calendario, se comprueban las
	// horas de trabajo del médico asignado al beneficiario introducido 
	private void dtcDiaCitaPropertyChange(PropertyChangeEvent evt) {
		/* 
		 * Se obtiene una matriz al llamar al método correspondiente del gestor de citas.
		 * El primer elemento es una tabla donde se indica los días en los que un médico trabaja, 
		 * junto con todas las horas posibles para dar cita esos días, según la jornada de dicho médico.
		 * El segundo elemento es una tabla donde se indica la fecha donde ya hay asignadas citas, 
		 * junto con todas las horas ya ocupadas de esa fecha.
		 */
		Object [] informacion;
		
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		String [] horas;
		Date fecha = dtcDiaCita.getDate();
		if (fecha != null) {
			try {
				// No se consideran sabados ni domingos
				if (fecha.getDay() != DOMINGO && fecha.getDay() != SABADO) {
					informacion = (Object[]) getControlador().obtenerHorasMedico(txtMedico.getText());
					// Obtenemos las horas en las que trabaja ese médico para el día seleccionado en el JDateChooser
					horasTrabajoDia = ((Hashtable<DiaSemana, ArrayList<String>>)informacion[0]).get(DiaSemana.values()[fecha.getDay()-1]);
					// Si el día seleccionado es laboral para el medico, se muestran todas las horas de trabajo
					if (horasTrabajoDia != null){
						horas = new String[horasTrabajoDia.size()];
						for (int i=0; i<horas.length; i++) {
							horas[i] = horasTrabajoDia.get(i);
						}
						// Asignamos esas horas al combobox
						crearModelos(horas);
						// Si el día introducido coincide con algún día donde el médico ya tiene citas asignadas, 
						// se pasa al render del comboBox las horas ya ocupadas, para mostrarlas en rojo
						if (((Hashtable<String, ArrayList<String>>)informacion[1]).containsKey(formatoDeFecha.format(dtcDiaCita.getDate()))) {
							horasOcupadasDia = ((Hashtable<String, ArrayList<String>>)informacion[1]).get(formatoDeFecha.format(dtcDiaCita.getDate()));
							cbHorasCitas.setRenderer(new ListCellRendererCitas(horasOcupadasDia));
						}
						
					}
					else
						crearModelos(new String [] {"El día introducido no es laboral para ese médico"});
				}
				else
					crearModelos(new String [] {"El día introducido no es laboral para ese médico"});

			} catch(SQLException e) {
				limpiarCamposConsultar();
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch(RemoteException e) {
				limpiarCamposConsultar();
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch (Exception e) {
				limpiarCamposConsultar();
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			}
		}
	}
	
	
	
	private void cbHorasCitasActionPerformed(ActionEvent evt) {
		comprobarValidezHora();
	}
	
	private boolean comprobarValidezHora () {
		// No se puede seleccionar una hora en rojo
		if (cbHorasCitas.getSelectedIndex() != -1) {
			// Esto es para evitar que se seleccione el item si el día no es laboral
			if (!cbHorasCitas.getSelectedItem().toString().equals("El día introducido no es laboral para ese médico")) {
				if (horasOcupadasDia != null)
					if (horasOcupadasDia.contains(cbHorasCitas.getSelectedItem().toString()))
						horaSeleccionada = "";
					else
						horaSeleccionada = cbHorasCitas.getSelectedItem().toString();
			} else
				horaSeleccionada = "";
		}
		return !horaSeleccionada.equals("");
	}
		
	private void btnAceptarActionPerformed(ActionEvent evt) {
		// Se registra la cita si el día es laboral
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("hh:mm");
		Medico med;
		Date fecha = dtcDiaCita.getDate();
		Cita c;
		try {
			horaSeleccionada = cbHorasCitas.getSelectedItem().toString();
			// Si es laboral ese dia y la hora no está ocupada, se pide la cita, con o sin volante.
			if (comprobarValidezHora()) {
				Date fechaAux = formatoDeFecha.parse(cbHorasCitas.getSelectedItem().toString());
				med = getControlador().consultarMedico(txtMedico.getText());
				if (txtIdVolante.getText().equals(""))
					c = getControlador().pedirCita(beneficiario, med.getDni(), new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), fechaAux.getHours(), fechaAux.getMinutes()), DURACION_CITA);
				else
					c = getControlador().pedirCita(beneficiario, Long.parseLong(txtIdVolante.getText()), new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), fechaAux.getHours(), fechaAux.getMinutes()), DURACION_CITA);
				
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "Cita registrada.");
				limpiarCamposConsultar();
			}
			else
				Dialogos.mostrarDialogoError(getFrame(), "Error", "Seleccione un día que sea laboral para el médico y una hora libre (no marcada en rojo)");
		} catch (RemoteException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch (MedicoInexistenteException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "No existe el médico solicitado");
		} catch (ParseException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch (BeneficiarioInexistenteException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El beneficiario no está dado de alta en el sistema");
		} catch (FechaNoValidaException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "La fecha y hora seleccionada no es válida para ese médico");
		} catch (SQLException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch (Exception e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}

	// $hide>>$
	
	// $hide<<$

}
