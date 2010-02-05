package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Sesion;
import dominio.control.ControladorCliente;
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
 * Panel que permite emitir volantes para ir a la consulta de especialistas.
 */
public class JPEmitirVolante extends JPBase {
	
	private static final long serialVersionUID = -2491308165795545454L;
	
	private JPBeneficiarioConsultar jPanelBeneficiario;
	private JSeparator jSeparator1;
	private JPanel jPanelMedico;
	private JTextField txtNombre;
	private JTextField txtEspecialidad;
	private JButton btnAceptar;
	private JLabel lblEspecialidad;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JTextField txtApellidos;
	private JLabel lblSelectl;
	private JList lstEspecialistas;
	private ListModel lstEspecialistasModel; 
	
	private ArrayList<Medico> especialistas;
	private long idVolante;

	public JPEmitirVolante(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(565, 390);
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			{
				jPanelMedico = new JPanel();
				AnchorLayout jPanelMedicoLayout = new AnchorLayout();
				jPanelMedico.setLayout(jPanelMedicoLayout);
				this.add(jPanelMedico, new AnchorConstraint(200, 5, 885, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jPanelMedico.setPreferredSize(new java.awt.Dimension(554, 185));
				{
					lblEspecialidad = new JLabel();
					jPanelMedico.add(lblEspecialidad, new AnchorConstraint(107, 245, 807, 226, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblEspecialidad.setText("Especialidad");
					lblEspecialidad.setPreferredSize(new java.awt.Dimension(77, 16));
				}
				{
					lblApellidos = new JLabel();
					jPanelMedico.add(lblApellidos, new AnchorConstraint(72, 264, 578, 226, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblApellidos.setText("Apellidos");
					lblApellidos.setPreferredSize(new java.awt.Dimension(58, 16));
				}
				{
					lblNombre = new JLabel();
					jPanelMedico.add(lblNombre, new AnchorConstraint(37, 274, 349, 226, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblNombre.setText("Nombre");
					lblNombre.setPreferredSize(new java.awt.Dimension(48, 16));
				}
				{
					txtEspecialidad = new JTextField();
					jPanelMedico.add(txtEspecialidad, new AnchorConstraint(104, 12, 833, 306, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtEspecialidad.setPreferredSize(new java.awt.Dimension(230, 23));
				}
				{
					txtApellidos = new JTextField();
					jPanelMedico.add(txtApellidos, new AnchorConstraint(69, 12, 604, 306, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtApellidos.setPreferredSize(new java.awt.Dimension(230, 23));
				}
				{
					txtNombre = new JTextField();
					jPanelMedico.add(txtNombre, new AnchorConstraint(34, 12, 375, 306, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtNombre.setPreferredSize(new java.awt.Dimension(230, 23));
				}
				{
					lblSelectl = new JLabel();
					jPanelMedico.add(lblSelectl, new AnchorConstraint(11, 354, 186, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblSelectl.setText("Seleccione NIF de un especialista:");
					lblSelectl.setPreferredSize(new java.awt.Dimension(188, 16));
				}
				{
					
					lstEspecialistas = new JList();
					lstEspecialistas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					jPanelMedico.add(lstEspecialistas, new AnchorConstraint(33, 302, 316, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lstEspecialistas.setPreferredSize(new java.awt.Dimension(178, 107));
					lstEspecialistas.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							lstEspecialistasValueChanged(evt);
						}
					});
				}
				{
					btnAceptar = new JButton();
					jPanelMedico.add(btnAceptar, new AnchorConstraint(156, 11, 996, 479, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnAceptar.setText("Aceptar");
					btnAceptar.setPreferredSize(new java.awt.Dimension(64, 23));
					btnAceptar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAceptarActionPerformed(evt);
						}
					});
				}
			}
			{
				jSeparator1 = new JSeparator();
				this.add(jSeparator1, new AnchorConstraint(196, 5, 493, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jSeparator1.setPreferredSize(new java.awt.Dimension(554, 10));
			}
			{
				jPanelBeneficiario = new JPBeneficiarioConsultar(getFrame(), getControlador());
				this.add(jPanelBeneficiario, new AnchorConstraint(5, 5, 437, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jPanelBeneficiario.setPreferredSize(new java.awt.Dimension(554, 186));
				jPanelBeneficiario.ocultarControles();
				jPanelBeneficiario.addBeneficiarioBuscadoListener(new BeneficiarioBuscadoListener() {
					public void beneficiarioBuscado(EventObject evt) {
						jPanelBeneficiarioBeneficiarioBuscado(evt);
					}
				});
			}
			// Inicializaciones de algunos controles
			txtNombre.setEditable(false);
			txtNombre.setFocusable(false);
			txtApellidos.setEditable(false);
			txtApellidos.setFocusable(false);
			txtEspecialidad.setEditable(false);
			txtEspecialidad.setFocusable(false);			
			crearModelos(new String [] {""});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void crearModelos(String [] elementos) {
		lstEspecialistasModel = new DefaultComboBoxModel(elementos);
		lstEspecialistas.setModel(lstEspecialistasModel);
	}
	
	public void inicializarEspecialistas() {
		String [] nifs;
		try {
			especialistas = (ArrayList<Medico>) getControlador().obtenerMedicos("Especialista");
			nifs = new String[especialistas.size()];
			for (int i=0; i<especialistas.size(); i++)
				nifs[i] = especialistas.get(i).getDni();
			crearModelos(nifs);
			lstEspecialistas.setFocusable(true);
			lstEspecialistas.setEnabled(true);
		} catch(SQLException e) {
			e.printStackTrace();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(MedicoInexistenteException e) {
			crearModelos(new String [] {"No existe ningun especialista"});
			lstEspecialistas.setFocusable(false);
			lstEspecialistas.setEnabled(false);

		} catch(Exception e) {
			e.printStackTrace();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void lstEspecialistasValueChanged(ListSelectionEvent evt) {
		if (lstEspecialistas.getSelectedIndex() != -1) {
			txtNombre.setText(especialistas.get(lstEspecialistas.getSelectedIndex()).getNombre());
			txtApellidos.setText(especialistas.get(lstEspecialistas.getSelectedIndex()).getApellidos());
			txtEspecialidad.setText(((Especialista)(especialistas.get(lstEspecialistas.getSelectedIndex()).getTipoMedico())).getEspecialidad());
		}
	}
	
	private void jPanelBeneficiarioBeneficiarioBuscado(EventObject evt) {
		// TODO Auto-generated method stub
	}
	
	private void btnAceptarActionPerformed(ActionEvent evt) {
		boolean valido = true;
		Medico medico = null;
		Medico nuevoMedico = null;
		Beneficiario bene;
		
//		if (jPanelBeneficiario.getNif().equals("")) { 
	//		Dialogos.mostrarDialogoAdvertencia(getFrame(), "Advertencia", "Debe introducir un beneficiario");
		//	valido = false;
//		}
		if (valido && txtNombre.getText().equals("")) {
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Advertencia", "Debe seleccionar un especialista");
			valido = false;
		}
		if (valido)
			try {
				bene = jPanelBeneficiario.getBeneficiario();
				medico = bene.getMedicoAsignado();
				idVolante = getControlador().emitirVolante(bene, ((Medico)((Sesion)(getControlador().getSesion())).getUsuario()), especialistas.get(lstEspecialistas.getSelectedIndex()));
				nuevoMedico = getControlador().consultarBeneficiario(bene.getNif()).getMedicoAsignado();
				if (!medico.equals(nuevoMedico))
					Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El volante se ha emitido para el beneficiario. El identificador de dicho volante es " + idVolante+"\nTambién se ha asignado un médico de cabecera al beneficiario. El DNI del nuevo médico es "+nuevoMedico.getDni());
				else
					Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El volante se ha emitido para el beneficiario. El identificador de dicho volante es " + idVolante);
				jPanelBeneficiario.limpiarCamposConsulta();
				limpiarPanelMedico();
			} catch (RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch (SQLException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch (Exception e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			}
	}
	
	private void limpiarPanelMedico() {
		txtNombre.setText("");
		txtApellidos.setText("");
		txtEspecialidad.setText("");
		lstEspecialistas.clearSelection();
	}
	
	//$hide<<$

}
