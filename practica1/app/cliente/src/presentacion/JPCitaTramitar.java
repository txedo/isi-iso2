package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import com.toedter.calendar.JDateChooser;

import excepciones.*;
import dominio.conocimiento.DiaSemana;
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
* Panel que permite tramitar citas para beneficiarios del sistema.
*/
public class JPCitaTramitar extends JPBase {

	private static final long serialVersionUID = 8297107492599580450L;
	private JDateChooser dtcDiaCita;
	private JLabel lblMedico;
	private JLabel lblHora;
	private JLabel lblDia;
	private JButton btnAceptar;
	private JTextField txtMedico;
	private JComboBox cbHorasCitas;
	private JPBeneficiarioConsultar jPanelBeneficiario;
	
	private ArrayList<String> horasTrabajoDia;
	private ArrayList<String> horasOcupadasDia;

	public JPCitaTramitar() {
		super();
		initGUI();
		crearModelos(new String [] {""});
	}
	
	private void crearModelos(String [] elementos) {
		ComboBoxModel cbHorasCitasModel = new DefaultComboBoxModel(elementos);
		cbHorasCitas.setModel(cbHorasCitasModel);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				lblHora = new JLabel();
				this.add(lblHora, new AnchorConstraint(240, 350, 657, 18, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHora.setText("Hora cita");
				lblHora.setPreferredSize(new java.awt.Dimension(62, 16));
			}
			{
				lblMedico = new JLabel();
				this.add(lblMedico, new AnchorConstraint(182, 310, 508, 18, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedico.setText("Medico asignado");
				lblMedico.setPreferredSize(new java.awt.Dimension(102, 16));
			}
			{
				lblDia = new JLabel();
				this.add(lblDia, new AnchorConstraint(210, 363, 591, 18, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDia.setText("Dia cita");
				lblDia.setPreferredSize(new java.awt.Dimension(49, 16));
			}
			{
				btnAceptar = new JButton();
				this.add(btnAceptar, new AnchorConstraint(310, 20, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnAceptar.setText("Aceptar");
				btnAceptar.setPreferredSize(new java.awt.Dimension(67, 23));
			}
			{
				txtMedico = new JTextField();
				this.add(txtMedico, new AnchorConstraint(179, 87, 519, 144, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtMedico.setPreferredSize(new java.awt.Dimension(199, 23));
				txtMedico.setFocusable(false);
				txtMedico.setEditable(false);
			}
			{
				jPanelBeneficiario = new JPBeneficiarioConsultar();
				this.add(jPanelBeneficiario, new AnchorConstraint(5, 5, 437, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jPanelBeneficiario.setPreferredSize(new java.awt.Dimension(419, 171));
			}
			{
				cbHorasCitas = new JComboBox();
				this.add(cbHorasCitas, new AnchorConstraint(237, 87, 262, 144, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cbHorasCitas.setPreferredSize(new java.awt.Dimension(199, 23));
				cbHorasCitas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cbHorasCitasActionPerformed(evt);
					}
				});
			}
			{
				dtcDiaCita = new JDateChooser();
				this.add(dtcDiaCita, new AnchorConstraint(208, 87, 144, 144, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcDiaCita.setPreferredSize(new java.awt.Dimension(199, 23));
				dtcDiaCita.setDateFormatString("dd/MM/yyyy");
				dtcDiaCita.setToolTipText("Formato dd/MM/yyyy. Para más ayuda haga clic en el icono de la derecha.");
				dtcDiaCita.setMinSelectableDate(new Date());
				dtcDiaCita.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						dtcDiaCitaPropertyChange(evt);
					}
				});
				
				cambiarEstado(true);

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setControlador(ControladorCliente controlador) {
		super.setControlador(controlador);
		jPanelBeneficiario.setControlador(controlador);
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
				// TODO: poner el medico asignado al beneficiario, cuando éste se busque en el sistema
				informacion = (Object[]) getControlador().obtenerHorasMedico("87654321");
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
						cbHorasCitas.setRenderer(new RenderJComboBox(horasOcupadasDia));
					}
					
				}
				else
					crearModelos(new String [] {"El día introducido no es laboral para ese médico"});
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void cbHorasCitasActionPerformed(ActionEvent evt) {
		// TODO: no se puede seleccionar ni el item 0 (que es el titulo del combobox) ni 
		// aquellas horas que no esten disponibles (es decir, que se encuentran en la lista
		// "horasOcupadas")
	}
	
	private void cambiarEstado(boolean estado) {
		btnAceptar.setEnabled(estado);
		dtcDiaCita.setEnabled(estado);
		cbHorasCitas.setEnabled(estado);
	}

	// $hide>>$
	
	// $hide<<$

}
