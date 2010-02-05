package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.TipoMedico;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CitaNoValidaException;

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

	private DefaultTableModel modeloTabla;
	private Beneficiario beneficiario;
	private Vector<Cita> citas;
	private SimpleDateFormat formatoDia;
	private SimpleDateFormat formatoHora;
	private int fila;

	private JScrollPane scpTablaCitas;
	private JButton btnRestablecer;
	private JSeparator sepSeparador;
	private JPBeneficiarioConsultar pnlBeneficiario;
	private JButton btnAnular;
	private JLabel lblCitas;
	private JTable tblTablaCitas;

	public JPCitaConsultar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		formatoDia = new SimpleDateFormat("dd/MM/yyyy");
		formatoHora = new SimpleDateFormat("HH:mm");
		crearTabla(0);
		fila = -1;
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 471));
			{
				sepSeparador = new JSeparator();
				this.add(sepSeparador, new AnchorConstraint(249, 6, 548, 5, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador.setPreferredSize(new java.awt.Dimension(419, 10));
			}
			{
				pnlBeneficiario = new JPBeneficiarioConsultar(this.getFrame(), this.getControlador());
				this.add(pnlBeneficiario, new AnchorConstraint(0, 0, 511, 0, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				pnlBeneficiario.setPreferredSize(new java.awt.Dimension(430, 237));
				pnlBeneficiario.reducirPanel();
				pnlBeneficiario.addBeneficiarioBuscadoListener(new BeneficiarioBuscadoListener() {
					public void beneficiarioBuscado(EventObject evt) {
						pnlBeneficiarioBeneficiarioBuscado(evt);
					}
				});
			}
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(916, 145, 14, 673, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnAnular = new JButton();
				this.add(btnAnular, new AnchorConstraint(916, 12, 14, 836, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnAnular.setText("Anular");
				btnAnular.setPreferredSize(new java.awt.Dimension(120, 26));
				btnAnular.setEnabled(false);
				btnAnular.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAnularActionPerformed(evt);
					}
				});
			}
			{
				lblCitas = new JLabel();
				this.add(lblCitas, new AnchorConstraint(260, 273, 506, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCitas.setText("Citas encontradas:");
				lblCitas.setPreferredSize(new java.awt.Dimension(108, 16));
			}
			{
				scpTablaCitas = new JScrollPane();
				this.add(scpTablaCitas, new AnchorConstraint(282, 12, 53, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				scpTablaCitas.setPreferredSize(new java.awt.Dimension(409, 136));
				{
					tblTablaCitas = new JTable();
					scpTablaCitas.setViewportView(tblTablaCitas);				
					tblTablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					ListSelectionModel tableCitasSelectionModel = tblTablaCitas.getSelectionModel();
					tableCitasSelectionModel.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							tblTablaCitasSelectionModelValueChanged(evt);
						}
					});
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//$hide>>$
	
	private void pnlBeneficiarioBeneficiarioBuscado(EventObject evt) {
		// Borramos los datos de la última consulta de citas
		limpiarCamposConsulta();

		// Obtenemos el beneficiario que se ha buscado en el panel de consulta
		// (puede ser null si ocurrió un error al buscar el beneficiario)
		beneficiario = pnlBeneficiario.getBeneficiario();
		
		if(beneficiario != null) {
		
			try {
				
				// Obtenemos y mostramos las citas del beneficiario
				citas = getControlador().consultarCitas(beneficiario.getNif());
				if(citas != null) {
					crearTabla(citas.size());
					rellenarTabla(citas);
				}
			
			} catch(BeneficiarioInexistenteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			
			} catch(SQLException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch(RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch(Exception e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			}
		
		}
	}
	
	private void tblTablaCitasSelectionModelValueChanged(ListSelectionEvent evt) {
		ListSelectionModel modelo;
		
		modelo = (ListSelectionModel)evt.getSource();
		if(!modelo.isSelectionEmpty()) {
			// Guardamos el índice de la cita seleccionada y
			// activamos el botón para anularla
			fila = modelo.getMinSelectionIndex();
			btnAnular.setEnabled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void btnAnularActionPerformed(ActionEvent evt) {
		Cita cita;
		
		try {
			
			// Obtenemos la cita seleccionada
			cita = citas.get(fila);
			
			// Solicitamos al servidor que se anule la cita
			getControlador().anularCita(cita);

			// 
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "La cita se ha eliminado correctamente.");
			((DefaultTableModel)tblTablaCitas.getModel()).removeRow(fila);

		} catch(CitaNoValidaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void crearTabla(int nfilas) {
		Vector<String> encabezado;

		// Inicializamos una nueva tabla de citas
		encabezado = new Vector<String>();
		encabezado.add("Día");
		encabezado.add("Hora");
		encabezado.add("Médico");
		encabezado.add("DNI Médico");
		encabezado.add("Tipo Médico");
		encabezado.add("Especialidad");
		modeloTabla = new DefaultTableModel(encabezado, nfilas);
		tblTablaCitas.setModel(modeloTabla);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(2).setMinWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(200);
	}
	
	private void rellenarTabla(Vector<Cita> citas) {
		Date fecha;
		TipoMedico tipo;
		int fila, col;
		
		for(fila = 0; fila < citas.size(); fila++) {
			col = 0;
			fecha = citas.get(fila).getFechaYHora();
			tipo = citas.get(fila).getMedico().getTipoMedico();
			tblTablaCitas.setValueAt(formatoDia.format(fecha), fila, col++);			
			tblTablaCitas.setValueAt(formatoHora.format(fecha), fila, col++);
			tblTablaCitas.setValueAt(citas.get(fila).getMedico().getApellidos() + ", " + citas.get(fila).getMedico().getNombre(), fila, col++);
			tblTablaCitas.setValueAt(citas.get(fila).getMedico().getDni(), fila, col++);
			tblTablaCitas.setValueAt(tipo.getClass().getSimpleName(), fila, col++);
			if(tipo.getCategoria() == CategoriasMedico.Especialista) {
				tblTablaCitas.setValueAt(((Especialista)tipo).getEspecialidad(), fila, col++);
			} else {
				tblTablaCitas.setValueAt("", fila, col++);
			}
		}
	}
	
	private void limpiarTabla() {
		DefaultTableModel modelo;
		
		modelo = (DefaultTableModel)tblTablaCitas.getModel();
		while(modelo.getRowCount() > 0){
			modelo.removeRow(0);
		}
	}
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		pnlBeneficiario.limpiarCamposConsulta();
		limpiarCamposConsulta();
	}
	
	private void limpiarCamposConsulta() {
		tblTablaCitas.clearSelection();
		limpiarTabla();
		btnAnular.setEnabled(false);
	}

	//$hide<<$

}
