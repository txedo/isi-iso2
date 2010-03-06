package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import presentacion.auxiliar.BeneficiarioBuscadoListener;
import presentacion.auxiliar.ComparatorMedicosApellido;
import presentacion.auxiliar.Dialogos;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Volante;
import dominio.conocimiento.Usuario;
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
public class JPVolanteEmitir extends JPBase {
	
	private static final long serialVersionUID = -2491308165795545454L;
	
	private Vector<Medico> especialistas;
	private long idVolante;
	private Beneficiario beneficiario = null;

	private JPBeneficiarioConsultar pnlBeneficiario;
	private JSeparator sepSeparador;
	private JPanel jPanelMedico;
	private JLabel lblEspecialidad;
	private JButton btnAceptar;
	private JList lstEspecialistas;
	private JLabel jLabel1;
	private JTextField txtCentro;
	private JLabel lblCentro;
	private JScrollPane jScrollPane1;
	private JComboBox cbEspecialidad;
	private ListModel lstEspecialistasModel; 
	
	public JPVolanteEmitir() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPEmitirVolante
	}
	
	public JPVolanteEmitir(JFPrincipal frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(565, 483));
			{
				jPanelMedico = new JPanel();
				AnchorLayout jPanelMedicoLayout = new AnchorLayout();
				jPanelMedico.setLayout(jPanelMedicoLayout);
				this.add(jPanelMedico, new AnchorConstraint(252, 5, 885, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jPanelMedico.setPreferredSize(new java.awt.Dimension(554, 231));
				{
					jLabel1 = new JLabel();
					jPanelMedico.add(jLabel1, new AnchorConstraint(40, 444, 366, 4, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					jLabel1.setText("Especialidad");
					jLabel1.setPreferredSize(new java.awt.Dimension(106, 16));
				}
				{
					txtCentro = new JTextField();
					jPanelMedico.add(txtCentro, new AnchorConstraint(151, 12, 714, 133, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					txtCentro.setPreferredSize(new java.awt.Dimension(409, 23));
					txtCentro.setEditable(false);
					txtCentro.setFocusable(false);
					txtCentro.setName("txtCentro");
				}
				{
					lblCentro = new JLabel();
					jPanelMedico.add(lblCentro, new AnchorConstraint(154, 456, 694, 4, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblCentro.setText("Centro de salud");
					lblCentro.setPreferredSize(new java.awt.Dimension(94, 16));
				}
				{
					lblEspecialidad = new JLabel();
					jPanelMedico.add(lblEspecialidad, new AnchorConstraint(12, 374, 154, 4, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblEspecialidad.setText("Seleccione un especialista: ");
					lblEspecialidad.setPreferredSize(new java.awt.Dimension(176, 16));
				}
				{
					cbEspecialidad = new JComboBox();
					jPanelMedico.add(cbEspecialidad, new AnchorConstraint(37, 12, 310, 133, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					cbEspecialidad.setPreferredSize(new java.awt.Dimension(409, 23));
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanelMedico.add(jScrollPane1, new AnchorConstraint(72, 12, 316, 133, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					jScrollPane1.setPreferredSize(new java.awt.Dimension(409, 70));
					{
						
						lstEspecialistas = new JList();
						jScrollPane1.setViewportView(lstEspecialistas);
						lstEspecialistas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						lstEspecialistas.setPreferredSize(new java.awt.Dimension(319, 65));
					}
				}
				{
					btnAceptar = new JButton();
					jPanelMedico.add(btnAceptar, new AnchorConstraint(192, 11, 932, 842, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					btnAceptar.setText("Emitir volante");
					btnAceptar.setPreferredSize(new java.awt.Dimension(129, 26));
					btnAceptar.setName("btnEmitir");
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
				pnlBeneficiario.setPreferredSize(new java.awt.Dimension(565, 241));
				pnlBeneficiario.setName("pnlBeneficiario");
				pnlBeneficiario.reducirPanel();
				pnlBeneficiario.addBeneficiarioBuscadoListener(new BeneficiarioBuscadoListener() {
					public void beneficiarioBuscado(EventObject evt) {
						pnlBeneficiarioBeneficiarioBuscado(evt);
					}
				});
			}
			// Inicializaciones de algunos controles
			lstEspecialistas.setEnabled(false);
			lstEspecialistas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			lstEspecialistas.setName("lstEspecialistas");
			lstEspecialistas.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent evt) {
					lstEspecialistasValueChanged(evt);
				}
			});
			crearModelos(new String [] {""});
			rellenarModeloComboBox(new String [] {""});
			cbEspecialidad.setFocusable(false);
			cbEspecialidad.setEnabled(false);
			cbEspecialidad.setName("cmbEspecialidad");
			cbEspecialidad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					cbEspecialidadActionPerformed(evt);
				}
			});

			btnAceptar.setEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void cbEspecialidadActionPerformed(ActionEvent evt) {
		txtCentro.setText("");
		inicializarEspecialistas();
	}
	
	private void lstEspecialistasValueChanged(ListSelectionEvent evt) {
		CentroSalud centro;
		if (especialistas.size()!=0 && lstEspecialistas.getSelectedIndex()!=-1) {
			centro = especialistas.get(lstEspecialistas.getSelectedIndex()).getCentroSalud();
			txtCentro.setText(centro.getNombre() + " " + centro.getDireccion());
		}
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
			especialistas = (Vector<Medico>) getControlador().obtenerMedicosTipo(new Especialista(cbEspecialidad.getSelectedItem().toString()));
			if (especialistas.size()>0) {
				Collections.sort(especialistas, new ComparatorMedicosApellido());
				info = new String[especialistas.size()];
				// Mostramos los nombres y NIFs de los especialistas existentes en la especialidad indicada
				for (int i=0; i<especialistas.size(); i++)
					info[i] = especialistas.get(i).getApellidos() + ", " + especialistas.get(i).getNombre() + " (" + especialistas.get(i).getNif() + ")";
				crearModelos(info);
				lstEspecialistas.setFocusable(true);
				lstEspecialistas.setEnabled(true);
				btnAceptar.setEnabled(true);
			}
			else {
				crearModelos(info);
				btnAceptar.setEnabled(false);
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
		SimpleDateFormat formatoFecha;
		Usuario usuario;
		Volante volante;
		boolean valido;
		
		valido = true;
		if(lstEspecialistas.getSelectedIndex()==-1) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe seleccionar un especialista");
			valido = false;
		}
		if(valido) {
			try {
				// Consultamos los datos del propio usuario
				usuario = getControlador().consultarPropioUsuario();
				// Solicitamos al servidor que se cree el volante
				idVolante = getControlador().emitirVolante(beneficiario, (Medico)usuario, especialistas.get(lstEspecialistas.getSelectedIndex()));
				volante = getControlador().consultarVolante(idVolante);
				// Recuperamos el volante y mostramos sus datos
				formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El volante del beneficiario se ha emitido correctamente.\nEl identificador asignado al volante es " + idVolante + " y se podrá\nutilizar hasta el " + formatoFecha.format(volante.getFechaCaducidad()) + ".");
				restablecerPanel();
			} catch (RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch (SQLException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch (Exception e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			}
		}
	}
	
	private void limpiarPanelMedico() {
		lstEspecialistas.clearSelection();
		crearModelos(new String [] {""});
		rellenarModeloComboBox(new String[] {""});
		cbEspecialidad.setEnabled(false);
		cbEspecialidad.setFocusable(false);
		lstEspecialistas.setEnabled(false);
		txtCentro.setText("");
	}

	// Métodos públicos
	
	// <Métodos del observador>
	
	public void beneficiarioActualizado(Beneficiario beneficiario) {
		pnlBeneficiario.beneficiarioActualizado(beneficiario);
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		if (this.beneficiario!=null && beneficiario.getNif().equals(this.beneficiario.getNif())) {
			pnlBeneficiario.beneficiarioEliminado(beneficiario);
			limpiarPanelMedico();
		}
		
	}
	
	public void usuarioActualizado(Usuario usuario) {
		System.out.println(beneficiario);
		System.out.println(usuario);
		if(beneficiario != null) {
			if (usuario.getRol() == RolesUsuario.Medico) {
				if (beneficiario.getMedicoAsignado().getNif().equals(((Medico)usuario).getNif())) {
					// Otro cliente ha actualizado el médico asignado al beneficiario
					pnlBeneficiario.usuarioActualizado(usuario);
				}
				else if (especialistas.size()>0 && ((Medico)usuario).getTipoMedico().getCategoria().equals(CategoriasMedico.Especialista)) {
					for (Medico e: especialistas) {
						if (e.getNif().equals(((Medico)usuario).getNif()))
							Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha actualizado un especialista desde otro cliente.");
							inicializarEspecialistas();
					}
				}
			}
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		if(beneficiario != null) {
			if (usuario.getRol() == RolesUsuario.Medico) {
				if (beneficiario.getMedicoAsignado().getNif().equals(((Medico)usuario).getNif())) {
					// Otro cliente ha eliminado el médico asignado al beneficiario
					pnlBeneficiario.usuarioEliminado(usuario);
					limpiarPanelMedico();
				}
				else if (especialistas.size()>0 && ((Medico)usuario).getTipoMedico().getCategoria().equals(CategoriasMedico.Especialista)) {
					if (especialistas.contains((Medico)usuario))
						Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha eliminado un especialista desde otro cliente.");
						inicializarEspecialistas();					
				}
			}
		}
	}
	
	public void restablecerPanel() {
		pnlBeneficiario.restablecerPanel();
		limpiarPanelMedico();
		beneficiario = null;
		btnAceptar.setEnabled(false);
	}
	
	//$hide<<$

}
