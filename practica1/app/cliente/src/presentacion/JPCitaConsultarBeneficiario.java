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

import presentacion.auxiliares.BeneficiarioBuscadoListener;
import presentacion.auxiliares.TableCellRendererCitas;
import presentacion.auxiliares.UtilidadesTablaCitas;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.TipoMedico;
import dominio.control.ControladorCliente;
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
public class JPCitaConsultarBeneficiario extends JPBase {

	private static final long serialVersionUID = 117161427277876393L;

	private Beneficiario beneficiario;
	private Vector<Cita> citas;
	private boolean viendoHistorico;

	private JButton btnHistoricoCitas;
	private JScrollPane scpTablaCitas;
	private JButton btnRestablecer;
	private JSeparator sepSeparador;
	private JPBeneficiarioConsultar pnlBeneficiario;
	private JButton btnAnular;
	private JLabel lblCitas;
	private JTable tblTablaCitas;

	public JPCitaConsultarBeneficiario() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPCitaConsultar
	}
	
	public JPCitaConsultarBeneficiario(JFrame frame, ControladorCliente controlador) {
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
				this.add(btnHistoricoCitas, new AnchorConstraint(916, 11, 14, 645, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
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
				this.add(btnRestablecer, new AnchorConstraint(916, 163, 14, 673, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
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
				this.add(btnAnular, new AnchorConstraint(916, 301, 14, 9, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				btnAnular.setText("Anular cita");
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
				lblCitas.setText("Citas pendientes encontradas:");
				lblCitas.setPreferredSize(new java.awt.Dimension(228, 16));
			}
			{
				scpTablaCitas = new JScrollPane();
				this.add(scpTablaCitas, new AnchorConstraint(282, 12, 53, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				scpTablaCitas.setPreferredSize(new java.awt.Dimension(409, 136));
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

	private void pnlBeneficiarioBeneficiarioBuscado(EventObject evt) {
		// Borramos los datos de la última consulta de citas
		limpiarCamposConsulta();

		// Obtenemos el beneficiario que se ha buscado en el panel de consulta
		// (puede ser null si ocurrió un error al buscar el beneficiario)
		beneficiario = pnlBeneficiario.getBeneficiario();
		
		if(beneficiario != null) {
			mostrarCitasPendientes();
		}
	}
	
	private void btnHistoricoCitasActionPerformed(ActionEvent evt) {
		mostrarHistoricoCitas();
	}
	
	private void btnAnularActionPerformed(ActionEvent evt) {
		Vector<Cita> pendientes;
		TableCellRendererCitas renderer;
		Cita cita;
		boolean respuesta;
		
		try {
			
			// Comprobamos que haya una cita seleccionada
			if(tblTablaCitas.getSelectedRow() == -1) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "Seleccione una cita que todavía esté pendiente (no marcada en azul) para anularla.");
			} else {
				// Comprobamos que la cita seleccionada no sea pasada
				renderer = (TableCellRendererCitas)tblTablaCitas.getDefaultRenderer(Object.class);
				if(renderer.getFilasDesactivadas().contains(tblTablaCitas.getSelectedRow())) {
					Dialogos.mostrarDialogoError(getFrame(), "Error", "Seleccione una cita que todavía esté pendiente (no marcada en azul) para anularla.");
				} else {
					// Obtenemos la cita seleccionada
					cita = citas.get(tblTablaCitas.getSelectedRow());
					respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", "¿Seguro que desea eliminar la cita seleccionada?");
					if (respuesta) {
						// Solicitamos al servidor que se anule la cita
						getControlador().anularCita(cita);
						// Mostramos de nuevo todas las citas
						Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "La cita se ha eliminado correctamente.");
					}
					if(viendoHistorico) {
						citas = getControlador().consultarHistoricoCitas(beneficiario.getNif());
						pendientes = getControlador().consultarCitasPendientesBeneficiario(beneficiario.getNif());
						UtilidadesTablaCitas.crearTabla(tblTablaCitas, citas.size());
						UtilidadesTablaCitas.rellenarTabla(tblTablaCitas, citas, pendientes);
					} else {
						citas = getControlador().consultarCitasPendientesBeneficiario(beneficiario.getNif());
						UtilidadesTablaCitas.crearTabla(tblTablaCitas, citas.size());
						UtilidadesTablaCitas.rellenarTabla(tblTablaCitas, citas);
					}
					// Seleccionamos la primera cita de la lista (si la hay)
					if(citas.size() > 0) {
						tblTablaCitas.getSelectionModel().setSelectionInterval(0, 0);
						btnAnular.setEnabled(true);
					}
				}
			}
			
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
		
	private void mostrarCitasPendientes() {
		try {
			
			// Obtenemos y mostramos las citas del beneficiario
			// (por defecto, sólo las pendientes)
			citas = getControlador().consultarCitasPendientesBeneficiario(beneficiario.getNif());
			UtilidadesTablaCitas.crearTabla(tblTablaCitas, citas.size());
			UtilidadesTablaCitas.rellenarTabla(tblTablaCitas, citas);
			
			// Indicamos que estamos mostrando sólo las citas pendientes
			lblCitas.setText("Citas pendientes encontradas:");
			viendoHistorico = false;
			btnHistoricoCitas.setEnabled(true);
			
			// Seleccionamos la primera cita de la lista (si la hay)
			if(citas.size() > 0) {
				tblTablaCitas.getSelectionModel().setSelectionInterval(0, 0);
				btnAnular.setEnabled(true);
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
			
			// Obtenemos y mostramos todas las citas del beneficiario,
			// marcando en azul las que son pasadas
			citas = getControlador().consultarHistoricoCitas(beneficiario.getNif());
			pendientes = getControlador().consultarCitasPendientesBeneficiario(beneficiario.getNif());
			UtilidadesTablaCitas.crearTabla(tblTablaCitas, citas.size());
			UtilidadesTablaCitas.rellenarTabla(tblTablaCitas, citas, pendientes);
			
			// Indicamos que estamos mostrando todas las citas
			lblCitas.setText("Citas encontradas:");
			viendoHistorico = true;
			btnHistoricoCitas.setEnabled(false);

			// Seleccionamos la primera cita de la lista (si la hay)
			if(citas.size() > 0) {
				tblTablaCitas.getSelectionModel().setSelectionInterval(0, 0);
				btnAnular.setEnabled(true);
			}

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		restablecerPanel();
	}
	
	private void limpiarCamposConsulta() {
		UtilidadesTablaCitas.limpiarTabla(tblTablaCitas);
		btnAnular.setEnabled(false);
		btnHistoricoCitas.setEnabled(false);
		lblCitas.setText("Citas pendientes encontradas:");
		viendoHistorico = false;
	}
	
	// Métodos públicos
	
	// <métodos del observador>
	
	public void citaRegistrada(Cita cita) {
		if(beneficiario != null && cita.getMedico().equals(beneficiario.getMedicoAsignado())) {
			// Otro cliente ha registrado una cita para el médico de este beneficiario
			// Se vuelven a recuperar las citas para mostrar la nueva
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado otra cita para este médico.");
			if (!viendoHistorico)
				mostrarCitasPendientes();
			else
				mostrarHistoricoCitas();
		}
	}
	
	public void restablecerPanel() {
		pnlBeneficiario.restablecerPanel();
		limpiarCamposConsulta();
	}

	//$hide<<$

}
