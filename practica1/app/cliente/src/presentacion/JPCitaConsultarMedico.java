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
import javax.swing.table.DefaultTableModel;

import presentacion.auxiliares.TableCellRendererCitas;
import presentacion.auxiliares.UsuarioBuscadoListener;
import presentacion.auxiliares.UtilidadesTablaCitas;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.TipoMedico;
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
 * Panel que permite consultar y anular citas existentes.
 */
public class JPCitaConsultarMedico extends JPBase {

	private static final long serialVersionUID = 117161427277876393L;

	private Medico medico;
	private Vector<Cita> citas;
	private JButton btnHistoricoCitas;
	private JScrollPane scpTablaCitas;
	private JButton btnRestablecer;
	private JSeparator sepSeparador;
	private JPUsuarioConsultar pnlUsuario;
	private JLabel lblCitas;
	private JTable tblTablaCitas;
	
	private boolean viendoHistorico;

	public JPCitaConsultarMedico() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPCitaConsultar
	}
	
	public JPCitaConsultarMedico(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		UtilidadesTablaCitas.crearTabla(tblTablaCitas, 0);
		viendoHistorico = false;
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 471));
			{
				btnHistoricoCitas = new JButton();
				this.add(btnHistoricoCitas, new AnchorConstraint(916, 11, 11, 645, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnHistoricoCitas.setText("Ver histórico de citas");
				btnHistoricoCitas.setPreferredSize(new java.awt.Dimension(142, 26));
				btnHistoricoCitas.setEnabled(false);
				btnHistoricoCitas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnHistoricoCitasActionPerformed(evt);
					}
				});
			}
			{
				sepSeparador = new JSeparator();
				this.add(sepSeparador, new AnchorConstraint(189, 6, 548, 5, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador.setPreferredSize(new java.awt.Dimension(419, 10));
			}
			{
				pnlUsuario = new JPUsuarioConsultar(this.getFrame(), this.getControlador());
				this.add(pnlUsuario, new AnchorConstraint(0, 0, 511, 0, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				pnlUsuario.setPreferredSize(new java.awt.Dimension(430, 183));
				pnlUsuario.reducirPanel();
				pnlUsuario.addUsuarioBuscadoListener(new UsuarioBuscadoListener() {
					public void usuarioBuscado(EventObject evt) {
						pnlUsuarioUsuarioBuscado(evt);
					}
				});
			}
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(916, 164, 11, 673, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				lblCitas = new JLabel();
				this.add(lblCitas, new AnchorConstraint(200, 273, 506, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCitas.setText("Citas pendientes encontradas:");
				lblCitas.setPreferredSize(new java.awt.Dimension(228, 16));
			}
			{
				scpTablaCitas = new JScrollPane();
				this.add(scpTablaCitas, new AnchorConstraint(222, 12, 49, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				scpTablaCitas.setPreferredSize(new java.awt.Dimension(409, 200));
				{
					tblTablaCitas = new JTable();
					scpTablaCitas.setViewportView(tblTablaCitas);				
					tblTablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					tblTablaCitas.setCellEditor(null);
					tblTablaCitas.setDefaultRenderer(Object.class, new TableCellRendererCitas());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$

	private void pnlUsuarioUsuarioBuscado(EventObject evt) {
		// Borramos los datos de la última consulta de citas
		limpiarCamposConsulta();

		// Obtenemos el medico que se ha buscado en el panel de consulta
		// (puede ser null si ocurrió un error al buscar el usuario)
		medico = (Medico)pnlUsuario.getUsuario();
		
		if(medico != null) {

			mostrarCitasPendientes();
		}
	}
	
	private void btnHistoricoCitasActionPerformed(ActionEvent evt) {
		mostrarHistoricoCitas();
	}
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		restablecerPanel();
	}
	
	private void mostrarCitasPendientes() {
		try {
			
			// Obtenemos y mostramos las citas del medico
			// (por defecto, sólo las pendientes)
			citas = getControlador().consultarCitasPendientesMedico(medico.getDni());
			UtilidadesTablaCitas.crearTabla(tblTablaCitas, citas.size());
			UtilidadesTablaCitas.rellenarTabla(tblTablaCitas, citas);
			
			// Indicamos que estamos mostrando sólo las citas pendientes
			lblCitas.setText("Citas pendientes encontradas:");
			viendoHistorico = false;
			btnHistoricoCitas.setEnabled(true);
			
			// Seleccionamos la primera cita de la lista (si la hay)
			if(citas.size() > 0) {
				tblTablaCitas.getSelectionModel().setSelectionInterval(0, 0);
			}

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	
	}
	
	private void mostrarHistoricoCitas() {
		Vector<Cita> pendientes;
		try {
			
			// Obtenemos y mostramos todas las citas del medico,
			// marcando en azul las que son pasadas
			citas = getControlador().consultarCitasMedico(medico.getDni());
			pendientes = getControlador().consultarCitasPendientesMedico(medico.getDni());
			UtilidadesTablaCitas.crearTabla(tblTablaCitas, citas.size());
			UtilidadesTablaCitas.rellenarTabla(tblTablaCitas, citas, pendientes);
			
			// Indicamos que estamos mostrando todas las citas
			lblCitas.setText("Citas encontradas:");
			viendoHistorico = true;
			btnHistoricoCitas.setEnabled(false);

			// Seleccionamos la primera cita de la lista (si la hay)
			if(citas.size() > 0) {
				tblTablaCitas.getSelectionModel().setSelectionInterval(0, 0);
			}

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void limpiarCamposConsulta() {
		UtilidadesTablaCitas.limpiarTabla(tblTablaCitas);
		btnHistoricoCitas.setEnabled(false);
		lblCitas.setText("Citas pendientes encontradas:");
		viendoHistorico = false;
	}
	
	// Métodos públicos
	
	// <métodos del observador>
	
	public void citaRegistrada(Cita cita) {
		if(medico != null && medico.equals(cita.getMedico())) {
			// Otro cliente ha registrado una cita para este médico.
			// Se vuelven a recuperar las citas para mostrar la nueva
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado otra cita para este médico.");
			if (!viendoHistorico)
				mostrarCitasPendientes();
			else
				mostrarHistoricoCitas();
		}
	}
	
	public void restablecerPanel() {
		pnlUsuario.restablecerPanel();
		limpiarCamposConsulta();
	}

	//$hide<<$

}

