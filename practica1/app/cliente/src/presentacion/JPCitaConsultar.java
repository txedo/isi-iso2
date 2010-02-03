package presentacion;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Validacion;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CadenaVaciaException;
import excepciones.CitaNoValidaException;
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
 * Panel que permite consultar y anular citas existentes.
 */
public class JPCitaConsultar extends JPBase implements IConstantes {

	private static final long serialVersionUID = 117161427277876393L;
	
	private final String ID_NIF = "NIF";
	private final String ID_NSS = "NSS";

	private ListSelectionModel filaSeleccionada;
	private int fila = -1;
	private DefaultComboBoxModel cmbIdentificacionModel;	
	private Beneficiario beneficiario;
	private SimpleDateFormat formatoDeFecha;
	private SimpleDateFormat formatoDeHora;

	private JScrollPane jScrollPane1;
	private JButton btnReestablecer;
	private JButton btnAnular;
	private JLabel lblCitas;
	private JTable tableCitas;
	private JComboBox cmbIdentificacion;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JLabel lblNSS;
	private JLabel lblNIF;
	private JLabel lblBuscar;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtNSS;
	private JTextField txtNIF;
	private JTextField txtIdentificacion;
	private JButton btnBuscar;

	public JPCitaConsultar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		formatoDeHora = new SimpleDateFormat("HH:mm");
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				lblBuscar = new JLabel();
				this.add(lblBuscar, new AnchorConstraint(14, 264, 90, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblBuscar.setText("Buscar beneficiario por:");
				lblBuscar.setPreferredSize(new java.awt.Dimension(156, 14));
			}
			{
				btnReestablecer = new JButton();
				this.add(btnReestablecer, new AnchorConstraint(351, 116, 960, 673, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnReestablecer.setText("Reestablecer");
				btnReestablecer.setPreferredSize(new java.awt.Dimension(99, 23));
				btnReestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnReestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnAnular = new JButton();
				this.add(btnAnular, new AnchorConstraint(351, 12, 960, 836, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnAnular.setText("Anular");
				btnAnular.setPreferredSize(new java.awt.Dimension(99, 23));
				btnAnular.setEnabled(false);
				btnAnular.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAnularActionPerformed(evt);
					}
				});
			}
			{
				lblCitas = new JLabel();
				this.add(lblCitas, new AnchorConstraint(181, 310, 506, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCitas.setText("Citas encontradas:");
				lblCitas.setPreferredSize(new java.awt.Dimension(108, 16));
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
				this.add(jScrollPane1, new AnchorConstraint(203, 12, 788, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jScrollPane1.setPreferredSize(new java.awt.Dimension(406, 136));
				{
					tableCitas = new JTable();
					jScrollPane1.setViewportView(tableCitas);				
				}
			}
			tableCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			filaSeleccionada = tableCitas.getSelectionModel();
			filaSeleccionada.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (!lsm.isSelectionEmpty()) {
							// Al seleccionar una fila, activamos el botón para borrarla
							fila = lsm.getMinSelectionIndex();
							btnAnular.setEnabled(true);
					}
				}
			});
			
			crearTabla(0);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void crearTabla(int nfilas) {
		Vector<String> encabezado = new Vector<String>();
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
	
	private void limpiarTabla() {
		TableModel modelo = tableCitas.getModel();
		while(modelo.getRowCount()>0){
			((DefaultTableModel) modelo).removeRow(0);
		}
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		String sIdentificacion;
		String sTipo;
		Vector<Cita> citas = null;
		
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
			citas = getControlador().obtenerCitas(beneficiario.getNif());
			if (citas != null) {
				crearTabla(citas.size());
				rellenarTabla(citas);
			}
			
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
	
	private void btnAnularActionPerformed(ActionEvent evt) {
		try {
			// Se crea la cita con los datos seleccionados y se elimina de la base de datos y de la tabla
			String fechaString = tableCitas.getValueAt(fila, 0).toString();
			String horaString = tableCitas.getValueAt(fila, 1).toString();
			Date fecha = formatoDeFecha.parse(fechaString);
			Date hora = formatoDeHora.parse(horaString);
			Medico med = getControlador().consultarMedico(beneficiario.getMedicoAsignado().getDni());
			Cita c = new Cita(new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), hora.getHours(), hora.getMinutes()), DURACION_CITA, beneficiario, med);
			getControlador().anularCita(c);
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "Cita eliminada.");
			((DefaultTableModel) tableCitas.getModel()).removeRow(fila);
		} catch(SQLException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch (CitaNoValidaException e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch (Exception e) {
			limpiarCamposConsultar();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}

	private void btnReestablecerActionPerformed(ActionEvent evt) {
		limpiarCamposConsultar();
	}
	
	private void limpiarCamposConsultar() {
		txtIdentificacion.setText("");
		txtApellidos.setText("");
		txtNIF.setText("");
		txtNSS.setText("");
		txtNombre.setText("");
		tableCitas.clearSelection();
		limpiarTabla();
		btnAnular.setEnabled(false);
	}

	// $hide>>$
	
	// $hide<<$

}
