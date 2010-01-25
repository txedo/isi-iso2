package presentacion;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Utilidades;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CadenaVaciaException;
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
 * Panel que permite anular citas existentes.
 */
public class JPCitaAnular extends JPBase {

	private static final long serialVersionUID = 117161427277876393L;
	
	private final String ID_NIF = "NIF";
	private final String ID_NSS = "NSS";
	
	private JScrollPane jScrollPane1;
	private JTable tableCitas;
	
	private ListSelectionModel filaSeleccionada;
	private int fila= -1;
	private int nfilas = 0;
	private JComboBox cmbIdentificacion;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JLabel lblNSS;
	private JLabel lblNIF;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtNSS;
	private JTextField txtNIF;
	private JTextField txtIdentificacion;
	private JButton btnBuscar;

	private DefaultComboBoxModel cmbIdentificacionModel;

	public JPCitaAnular() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
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
				txtIdentificacion = new JTextField();
				this.add(txtIdentificacion, new AnchorConstraint(36, 83, 188, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtIdentificacion.setPreferredSize(new java.awt.Dimension(209, 23));
				txtIdentificacion.setEnabled(true);
			}
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
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1, new AnchorConstraint(171, 85, 788, 121, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jScrollPane1.setPreferredSize(new java.awt.Dimension(224, 136));
				{
					/*TableModel tableCitasModel = 
						new DefaultTableModel(
								new String[][] { { "One", "Two" }, { "Three", "Four" } },
								new String[] { "Column 1", "Column 2" });*/
					tableCitas = new JTable();
					jScrollPane1.setViewportView(tableCitas);
					//tableCitas.setModel(tableCitasModel);
				}
			}
			tableCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			filaSeleccionada = tableCitas.getSelectionModel();
			filaSeleccionada.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (!lsm.isSelectionEmpty()) 
							// Al seleccionar una fila, mostramos sus servicios disponibles
							fila = lsm.getMinSelectionIndex();
				}
			});
			
			crearTabla(3);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void crearTabla(int nfilas) {
		Vector encabezado = new Vector();
		encabezado.add("Fecha");
		encabezado.add("Hora");
		encabezado.add("DNI Medico");
		encabezado.add("Tipo Medico");
		encabezado.add("Especialidad");
		ModeloTabla modeloTabla=new ModeloTabla(encabezado, nfilas);
		tableCitas.setModel(modeloTabla);
	}
	
	private void rellenarTabla(Vector<Cita> citas) {
		Date fecha;
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatoDeHora = new SimpleDateFormat("HH:mm");
		TipoMedico tipo;
		int col;
		for (int fila=0; fila<citas.size(); fila++) {
			col = 0;
			fecha = citas.get(fila).getFechaYhora();
			tipo = citas.get(fila).getMedico().getTipoMedico();
			tableCitas.setValueAt(formatoDeFecha.format(fecha), fila, col);			
			tableCitas.setValueAt(formatoDeHora.format(fecha), fila, ++col);
			tableCitas.setValueAt(citas.get(fila).getMedico().getDni(), fila, ++col);
			tableCitas.setValueAt(tipo.getClass().getSimpleName(), fila, ++col);
			if (tipo instanceof Especialista)
				tableCitas.setValueAt(((Especialista)tipo).getEspecialidad(), fila, ++col);
			else
				tableCitas.setValueAt("", fila, ++col);
		}
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		String sIdentificacion;
		String sTipo;
		Beneficiario beneficiario = null;
		
		try {

			// Obtenemos el identificador para buscar el beneficiario
			sIdentificacion = txtIdentificacion.getText().toUpperCase();
			sTipo = (String)cmbIdentificacion.getSelectedItem();
			if(sIdentificacion.equals("")) {
				throw new CadenaVaciaException();
			}

			// Buscamos el beneficiario solicitado
			if(sTipo.equals(ID_NIF)) {
				Utilidades.comprobarNIF(sIdentificacion);
				beneficiario = getControlador().getBeneficiario(sIdentificacion);
			} else if(sTipo.equals(ID_NSS)) {
				Utilidades.comprobarNSS(sIdentificacion);
				beneficiario = getControlador().getBeneficiarioPorNSS(sIdentificacion);
			}

			// Mostramos los datos del beneficiario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la búsqueda", "Beneficiario encontrado.");			
			rellenarTabla(getControlador().obtenerCitas(beneficiario.getNif()));
			
			txtIdentificacion.setText("");
			txtNIF.setText(beneficiario.getNif());
			txtNSS.setText(beneficiario.getNss());
			txtNombre.setText(beneficiario.getNombre());
			txtApellidos.setText(beneficiario.getApellidos());

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
	}
	
	// $hide>>$
	
	// $hide<<$

}
