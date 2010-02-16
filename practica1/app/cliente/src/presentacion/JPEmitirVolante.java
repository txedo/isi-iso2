package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;

import java.util.Collections;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.ComboBoxModel;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.ordenPorApellido;
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
	
	private JPBeneficiarioConsultar pnlBeneficiario;
	private JSeparator sepSeparador;
	private JPanel jPanelMedico;
	private JLabel lblEspecialidad;
	private JButton btnAceptar;
	private JLabel lblSelectl;
	private JList lstEspecialistas;
	private JScrollPane jScrollPane1;
	private JComboBox cbEspecialidad;
	private ListModel lstEspecialistasModel; 
	
	private Vector<Medico> especialistas;
	private long idVolante;
	private Beneficiario beneficiario = null;

	public JPEmitirVolante(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(565, 450));
			{
				jPanelMedico = new JPanel();
				AnchorLayout jPanelMedicoLayout = new AnchorLayout();
				jPanelMedico.setLayout(jPanelMedicoLayout);
				this.add(jPanelMedico, new AnchorConstraint(252, 5, 885, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jPanelMedico.setPreferredSize(new java.awt.Dimension(544, 185));
				{
					lblEspecialidad = new JLabel();
					jPanelMedico.add(lblEspecialidad, new AnchorConstraint(12, 374, 154, 4, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblEspecialidad.setText("Seleccione una especialidad: ");
					lblEspecialidad.setPreferredSize(new java.awt.Dimension(176, 16));
				}
				{
					cbEspecialidad = new JComboBox();
					jPanelMedico.add(cbEspecialidad, new AnchorConstraint(34, 392, 310, 4, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					cbEspecialidad.setPreferredSize(new java.awt.Dimension(213, 23));
				}
				{
					lblSelectl = new JLabel();
					jPanelMedico.add(lblSelectl, new AnchorConstraint(12, 167, 186, 229, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblSelectl.setText("Seleccione un especialista:");
					lblSelectl.setPreferredSize(new java.awt.Dimension(158, 16));
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanelMedico.add(jScrollPane1, new AnchorConstraint(34, 15, 316, 229, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					jScrollPane1.setPreferredSize(new java.awt.Dimension(310, 110));
					{
						
						lstEspecialistas = new JList();
						jScrollPane1.setViewportView(lstEspecialistas);
						lstEspecialistas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						lstEspecialistas.setPreferredSize(new java.awt.Dimension(306, 107));
					}
				}
				{
					btnAceptar = new JButton();
					jPanelMedico.add(btnAceptar, new AnchorConstraint(156, 11, 996, 842, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					btnAceptar.setText("Aceptar");
					btnAceptar.setPreferredSize(new java.awt.Dimension(77, 23));
					btnAceptar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAceptarActionPerformed(evt);
						}
					});
				}
			}
			{
				sepSeparador = new JSeparator();
				this.add(sepSeparador, new AnchorConstraint(249, 5, 493, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador.setPreferredSize(new java.awt.Dimension(554, 10));
			}
			{
				pnlBeneficiario = new JPBeneficiarioConsultar(getFrame(), getControlador());
				this.add(pnlBeneficiario, new AnchorConstraint(0, 0, 437, 0, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				pnlBeneficiario.setPreferredSize(new java.awt.Dimension(555, 237));
				pnlBeneficiario.reducirPanel();
				pnlBeneficiario.addBeneficiarioBuscadoListener(new BeneficiarioBuscadoListener() {
					public void beneficiarioBuscado(EventObject evt) {
						pnlBeneficiarioBeneficiarioBuscado(evt);
					}
				});
			}
			// Inicializaciones de algunos controles
			lstEspecialistas.setEnabled(false);
			crearModelos(new String [] {""});
			rellenarModeloComboBox(new String [] {""});
			cbEspecialidad.setFocusable(false);
			cbEspecialidad.setEnabled(false);
			cbEspecialidad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					cbEspecialidadActionPerformed(evt);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void cbEspecialidadActionPerformed(ActionEvent evt) {
			inicializarEspecialistas();
	}

	//$hide>>$
	
	private void crearModelos(String [] elementos) {
		lstEspecialistasModel = new DefaultComboBoxModel(elementos);
		lstEspecialistas.setModel(lstEspecialistasModel);
	}
	
	private void rellenarModeloComboBox(String [] valores) {
		ComboBoxModel cbEspecialidadModel = new DefaultComboBoxModel(valores);
		cbEspecialidad.setModel(cbEspecialidadModel);
	}
	
	@SuppressWarnings("unchecked")
	public void inicializarEspecialistas() {		
		String [] info = {""};
		try {
			especialistas = (Vector<Medico>) getControlador().obtenerMedicos(CategoriasMedico.Especialista, cbEspecialidad.getSelectedItem().toString());
			if (especialistas.size()>0) {
				Collections.sort(especialistas, new ordenPorApellido());
				info = new String[especialistas.size()];
				// Mostramos los nombres y DNIs de los especialistas existentes en la especialidad indicada
				for (int i=0; i<especialistas.size(); i++)
					info[i] = especialistas.get(i).getApellidos() + ", " + especialistas.get(i).getNombre() + " (" + especialistas.get(i).getDni() + ")";
				crearModelos(info);
				lstEspecialistas.setFocusable(true);
				lstEspecialistas.setEnabled(true);
			}
			else {
				crearModelos(info);
			}
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
	
	private void pnlBeneficiarioBeneficiarioBuscado(EventObject evt) {
		// Borramos los datos de la última consulta
		limpiarPanelMedico();
		String [] valores = new String[Especialidades.values().length];
		// Obtenemos el beneficiario que se ha buscado en el panel de consulta
		// (puede ser null si ocurrió un error al buscar el beneficiario)
		beneficiario = pnlBeneficiario.getBeneficiario();
		if(beneficiario != null) {
			for (int i=0; i<Especialidades.values().length; i++) 
				valores[i] = Especialidades.values()[i].toString();
			rellenarModeloComboBox(valores);
			cbEspecialidad.setEnabled(true);
			cbEspecialidad.setFocusable(true);
			lstEspecialistas.setEnabled(true);
			cbEspecialidad.setSelectedIndex(0);
		}
	}
	
	private void btnAceptarActionPerformed(ActionEvent evt) {
		boolean valido = true;

		if (beneficiario == null) { 
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un beneficiario");
			valido = false;
		}
		
		if (valido && lstEspecialistas.getSelectedIndex()==-1) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe seleccionar un especialista");
			valido = false;
		}
		if (valido)
			try {
				idVolante = getControlador().emitirVolante(beneficiario, ((Medico)((Sesion)(getControlador().getSesion())).getUsuario()), especialistas.get(lstEspecialistas.getSelectedIndex()));
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El volante se ha emitido para el beneficiario. El identificador de dicho volante es " + idVolante);
				pnlBeneficiario.limpiarCamposConsulta();
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
		lstEspecialistas.clearSelection();
		crearModelos(new String [] {""});
		rellenarModeloComboBox(new String[] {""});
		cbEspecialidad.setEnabled(false);
		cbEspecialidad.setFocusable(false);
		lstEspecialistas.setEnabled(false);
	}
	
	//$hide<<$

}
