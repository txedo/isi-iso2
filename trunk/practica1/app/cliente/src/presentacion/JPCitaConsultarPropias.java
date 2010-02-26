package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.TableCellRendererCitas;
import presentacion.auxiliar.UtilidadesTablas;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Medico;
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
 * Panel que permite consultar las citas existentes del médico que ha iniciado sesión.
 */
public class JPCitaConsultarPropias extends JPBase {

	private static final long serialVersionUID = 117161427277876393L;

	private Vector<Cita> citas;
	private boolean viendoHistorico;
	private Medico medico;
	
	private JScrollPane scpTablaCitas;
	private JButton btnCitasHistoricas;
	private JButton btnCitasPendientes;
	private JLabel lblCitas;
	private JTable tblTablaCitas;
	
	public JPCitaConsultarPropias() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPCitaConsultar
	}
	
	public JPCitaConsultarPropias(JFPrincipal frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		UtilidadesTablas.crearTablaCitasMedico(tblTablaCitas, 0);
		viendoHistorico = false;
		citas = new Vector<Cita>();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 280));
			{
				btnCitasPendientes = new JButton();
				this.add(btnCitasPendientes, new AnchorConstraint(888, 164, 12, 289, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnCitasPendientes.setText("Ver citas pendientes");
				btnCitasPendientes.setPreferredSize(new java.awt.Dimension(142, 26));
				btnCitasPendientes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCitasPendientesActionPerformed(evt);
					}
				});
			}
			{
				lblCitas = new JLabel();
				this.add(lblCitas, new AnchorConstraint(12, 273, 506, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCitas.setText("Citas pendientes encontradas:");
				lblCitas.setPreferredSize(new java.awt.Dimension(228, 16));
			}
			{
				scpTablaCitas = new JScrollPane();
				this.add(scpTablaCitas, new AnchorConstraint(34, 12, 50, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				scpTablaCitas.setPreferredSize(new java.awt.Dimension(408, 251));
				{
					tblTablaCitas = new JTable();
					scpTablaCitas.setViewportView(tblTablaCitas);				
					tblTablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					tblTablaCitas.setCellEditor(null);
					tblTablaCitas.setDefaultRenderer(Object.class, new TableCellRendererCitas());
				}
			}
			{
				btnCitasHistoricas = new JButton();
				this.add(btnCitasHistoricas, new AnchorConstraint(888, 11, 12, 643, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnCitasHistoricas.setText("Ver histórico de citas");
				btnCitasHistoricas.setPreferredSize(new java.awt.Dimension(142, 26));
				btnCitasHistoricas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCitasHistoricasActionPerformed(evt);
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//$hide>>$

	private void btnCitasHistoricasActionPerformed(ActionEvent evt) {
		mostrarHistoricoCitas();
	}
	
	private void btnCitasPendientesActionPerformed(ActionEvent evt) {
		mostrarCitasPendientes();
	}

	private void mostrarCitasPendientes() {
		try {
			
			// Consultamos los datos del médico
			medico = (Medico)getControlador().consultarPropioUsuario();
			
			// Obtenemos y mostramos las citas del médico
			// (por defecto, sólo las pendientes)
			citas = getControlador().consultarCitasPendientesPropiasMedico();
			UtilidadesTablas.crearTablaCitasMedico(tblTablaCitas, citas.size());
			UtilidadesTablas.rellenarTablaCitasMedico(tblTablaCitas, citas);
			
			// Indicamos que estamos mostrando sólo las citas pendientes
			lblCitas.setText("Citas pendientes encontradas:");
			viendoHistorico = false;
			
			// Seleccionamos la primera cita de la lista (si la hay)
			if(citas.size() > 0) {
				tblTablaCitas.getSelectionModel().setSelectionInterval(0, 0);
			}

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	
	}
	
	private void mostrarHistoricoCitas() {
		Vector<Cita> pendientes;
		
		try {

			// Consultamos los datos del médico
			medico = (Medico)getControlador().consultarPropioUsuario();

			// Obtenemos y mostramos todas las citas del médico,
			// marcando en azul las que son pasadas
			citas = getControlador().consultarCitasPropiasMedico();
			pendientes = getControlador().consultarCitasPendientesPropiasMedico();
			UtilidadesTablas.crearTablaCitasMedico(tblTablaCitas, citas.size());
			UtilidadesTablas.rellenarTablaCitasMedico(tblTablaCitas, citas, pendientes);
			
			// Indicamos que estamos mostrando todas las citas
			lblCitas.setText("Citas encontradas:");
			viendoHistorico = true;

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
		UtilidadesTablas.limpiarTabla(tblTablaCitas);
		lblCitas.setText("Citas pendientes encontradas:");
		viendoHistorico = false;
		citas.clear();
	}
	
	// Métodos públicos
	
	public void beneficiarioActualizado(Beneficiario beneficiario) {
		boolean actualizado = false;
		Cita c;
		
		// Si alguno de los beneficiarios que se muestran en la tabla de citas ha cambiado, se refresca la tabla
		if (medico != null && citas.size()>0) {
			for (int i=0; !actualizado && i<citas.size(); i++) {
				c = citas.get(i);
				if (c.getBeneficiario().getNif().equals(beneficiario.getNif())) {
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se han modificado los datos de algún beneficiario desde otro cliente.");
					actualizado = true;
					if (!viendoHistorico)
						mostrarCitasPendientes();
					else
						mostrarHistoricoCitas();
				}
			}
		}
	}
	
	public void beneficiarioEliminado(Beneficiario beneficiario) {
		boolean actualizado = false;
		Cita c;

		// Si alguno de los beneficiarios que se muestran en la tabla de citas se ha eliminado, se refresca la tabla
		if (medico != null && citas.size()>0) {
			for (int i=0; !actualizado && i<citas.size(); i++) {
				c = citas.get(i);
				if (c.getBeneficiario().getNif().equals(beneficiario.getNif())) {
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha eliminado algún beneficiario desde otro cliente.");
					actualizado = true;
					if (!viendoHistorico)
						mostrarCitasPendientes();
					else
						mostrarHistoricoCitas();
				}
			}
		}
	}
	
	public void citaRegistrada(Cita cita) {
		if(medico != null && medico.equals(cita.getMedico())) {
			// Otro cliente ha registrado una cita para este médico.
			// Se vuelven a recuperar las citas para mostrar la nueva
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado una cita desde otro cliente para este médico.");
			if (!viendoHistorico)
				mostrarCitasPendientes();
			else
				mostrarHistoricoCitas();
		}
	}
	
	public void citaAnulada(Cita cita) {
		if(medico != null && medico.equals(cita.getMedico())) {
			// Otro cliente ha anulado una cita para este médico.
			// Se vuelven a recuperar las citas para mostrar las citas restantes
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha anulado una cita desde otro cliente para este médico.");
			if (!viendoHistorico)
				mostrarCitasPendientes();
			else
				mostrarHistoricoCitas();
		}
	}
	
	public void restablecerPanel() {
		limpiarCamposConsulta();
	}
	
	//$hide<<$

}

