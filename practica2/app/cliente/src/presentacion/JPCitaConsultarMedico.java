package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.TableCellRendererCitas;
import presentacion.auxiliar.UsuarioBuscadoListener;
import presentacion.auxiliar.UtilidadesTablas;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Usuario;
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
 * Panel que permite consultar citas existentes de m�dicos.
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
	
	public JPCitaConsultarMedico(JFPrincipal frame, ControladorCliente controlador) {
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
			this.setPreferredSize(new java.awt.Dimension(430, 393));
			{
				btnHistoricoCitas = new JButton();
				this.add(btnHistoricoCitas, new AnchorConstraint(916, 11, 11, 645, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnHistoricoCitas.setText("Ver hist�rico de citas");
				btnHistoricoCitas.setPreferredSize(new java.awt.Dimension(142, 26));
				btnHistoricoCitas.setEnabled(false);
				btnHistoricoCitas.setName("btnHistoricoCitas");
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
				pnlUsuario.setSoloMedicos(true);
				pnlUsuario.setName("pnlUsuario");
				pnlUsuario.addUsuarioBuscadoListener(new UsuarioBuscadoListener() {
					public void usuarioBuscado(EventObject evt) {
						pnlUsuarioUsuarioBuscado(evt);
					}
				});
			}
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(916, 163, 11, 673, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRestablecer.setName("btnRestablecer");
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
					tblTablaCitas.setName("tblTablaCitas");
				}
			}
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		}
	}
	
	//$hide>>$

	private void pnlUsuarioUsuarioBuscado(EventObject evt) {
		// Borramos los datos de la �ltima consulta de citas
		limpiarCamposConsulta();

		// Obtenemos el m�dico que se ha buscado en el panel de consulta
		// (puede ser null si ocurri� un error al buscar el usuario)
		medico = (Medico)pnlUsuario.getUsuario();
		
		if(medico != null) {
			// Mostramos, por defecto, las citas pendientes del m�dico
			mostrarCitasPendientes();
		}
	}
	
	private void btnHistoricoCitasActionPerformed(ActionEvent evt) {
		// Si se estaban viendo las citas pendientes y se pincha el bot�n, se pasa a mostrar el 
		// hist�rico de citas
		if (!viendoHistorico) {
			mostrarHistoricoCitas();
			btnHistoricoCitas.setText("Ver citas pendientes");
		}
		else {
			mostrarCitasPendientes();
			btnHistoricoCitas.setText("Ver hist�rico de citas");
		}
	}
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		restablecerPanel();
	}
	
	private void mostrarCitasPendientes() {
		try {
			
			// Obtenemos y mostramos las citas del medico
			// (por defecto, s�lo las pendientes)
			citas = getControlador().consultarCitasPendientesMedico(medico.getNif());
			UtilidadesTablas.crearTablaCitasMedico(tblTablaCitas, citas.size());
			UtilidadesTablas.rellenarTablaCitasMedico(tblTablaCitas, citas);
			
			// Indicamos que estamos mostrando s�lo las citas pendientes
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
			citas = getControlador().consultarCitasMedico(medico.getNif());
			pendientes = getControlador().consultarCitasPendientesMedico(medico.getNif());
			UtilidadesTablas.crearTablaCitasMedico(tblTablaCitas, citas.size());
			UtilidadesTablas.rellenarTablaCitasMedico(tblTablaCitas, citas, pendientes);
			
			// Indicamos que estamos mostrando todas las citas
			lblCitas.setText("Hist�rico de citas encontradas:");
			viendoHistorico = true;
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
	
	private void limpiarCamposConsulta() {
		UtilidadesTablas.limpiarTabla(tblTablaCitas);
		btnHistoricoCitas.setEnabled(false);
		lblCitas.setText("Citas pendientes encontradas:");
		viendoHistorico = false;
		btnHistoricoCitas.setText("Ver hist�rico de citas");
		citas.clear();
	}
	
	// M�todos p�blicos
	
	public void beneficiarioActualizado(Beneficiario beneficiario) {
		boolean actualizado = false;
		Cita c;
		// Si alguno de los beneficiarios que se muestran en la tabla de citas
		// ha cambiado, se refresca la tabla
		if (medico != null && citas.size()>0) {
			for (int i=0; !actualizado && i<citas.size(); i++) {
				c = citas.get(i);
				if (c.getBeneficiario().getNif().equals(beneficiario.getNif())) {
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se han modificado los datos de alg�n beneficiario desde otro cliente.");
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
		
		// Si alguno de los beneficiarios que se muestran en la tabla de citas se
		// ha eliminado, se refresca la tabla
		if (medico != null && citas.size()>0) {
			for (int i=0; !actualizado && i<citas.size(); i++) {
				c = citas.get(i);
				if (c.getBeneficiario().getNif().equals(beneficiario.getNif())) {
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha eliminado alg�n beneficiario desde otro cliente.");
					actualizado = true;
					if (!viendoHistorico)
						mostrarCitasPendientes();
					else
						mostrarHistoricoCitas();
				}
			}
		}
	}
	
	public void usuarioActualizado(Usuario usuario) {
		if(this.medico != null && usuario.getRol() == Roles.M�dico
		 && medico.getNif().equals(((Medico)usuario).getNif())) {
			// Otro cliente ha actualizado el m�dico del que se est�n consultando las citas
			pnlUsuario.usuarioActualizado(usuario);
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		if(this.medico != null && usuario.getRol() == Roles.M�dico
		 && medico.getNif().equals(((Medico)usuario).getNif())) {
			// Otro cliente ha eliminado el m�dico del que se est�n consultando las citas
			pnlUsuario.usuarioEliminado(usuario);
			limpiarCamposConsulta();
		}
	}
	
	public void citaRegistrada(Cita cita) {
		if(medico != null && medico.getNif().equals(cita.getMedico().getNif())) {
			// Otro cliente ha registrado una cita para este m�dico.
			// Se vuelven a recuperar las citas para mostrar la nueva
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha registrado una cita desde otro cliente para este m�dico.");
			if (!viendoHistorico)
				mostrarCitasPendientes();
			else
				mostrarHistoricoCitas();
		}
	}
	
	public void citaAnulada(Cita cita) {
		if(medico != null && medico.getNif().equals(cita.getMedico().getNif())) {
			// Otro cliente ha anulado una cita para este m�dico.
			// Se vuelven a recuperar las citas para mostrar las citas restantes
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "Se ha anulado una cita desde otro cliente para este m�dico.");
			if (!viendoHistorico)
				mostrarCitasPendientes();
			else
				mostrarHistoricoCitas();
		}
	}
	
	public void restablecerPanel() {
		pnlUsuario.restablecerPanel();
		limpiarCamposConsulta();
		medico = null;
	}

	//$hide<<$

}

