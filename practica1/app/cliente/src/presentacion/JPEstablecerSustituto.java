package presentacion;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Medico;
import dominio.conocimiento.RolesUsuarios;
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
 * Panel que permite buscar y elegir un médico adecuado para sustituir a
 * otro médico en una determinada fecha.
 */
public class JPEstablecerSustituto extends JPBase {

	private static final long serialVersionUID = -3582780436000291004L;
	
	private JDateChooserCitas dtcDiaCita;
	private JLabel lblDatos;
	private JSeparator sepSeparador;
	private JPUsuarioConsultar pnlMedico;
	private JLabel lblHora;
	private JLabel lblDia;
	private JButton btnBuscarSust;
	private JSpinner spnHoraHasta;
	private JLabel lblHoraHasta;
	private JSpinner spnHoraDesde;
	private JLabel lblHoraDesde;

	public JPEstablecerSustituto(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		//TODO:cambiarEstado(false);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 435));
			{
				SpinnerNumberModel spnHoraDesdeModel = new SpinnerNumberModel(0, 0, 24, 1);
				spnHoraDesde = new JSpinner();
				this.add(spnHoraDesde, new AnchorConstraint(227, 538, 578, 158, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				spnHoraDesde.setModel(spnHoraDesdeModel);
				spnHoraDesde.setPreferredSize(new java.awt.Dimension(51, 23));
				spnHoraDesde.getEditor().setPreferredSize(new java.awt.Dimension(36, 19));
			}
			{
				lblHoraDesde = new JLabel();
				this.add(lblHoraDesde, new AnchorConstraint(230, 403, 568, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHoraDesde.setText("Desde:");
				lblHoraDesde.setPreferredSize(new java.awt.Dimension(35, 16));
			}
			{
				lblDatos = new JLabel();
				this.add(lblDatos, new AnchorConstraint(175, 215, 633, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDatos.setText("Datos de la sustitución:");
				lblDatos.setPreferredSize(new java.awt.Dimension(177, 16));
			}
			{
				sepSeparador = new JSeparator();
				this.add(sepSeparador, new AnchorConstraint(165, 6, 587, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador.setPreferredSize(new java.awt.Dimension(418, 10));
			}
			{
				pnlMedico = new JPUsuarioConsultar(this.getFrame(), this.getControlador());
				this.add(pnlMedico, new AnchorConstraint(0, 0, 608, 0, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				pnlMedico.setPreferredSize(new java.awt.Dimension(430, 155));
				pnlMedico.reducirPanel();
				pnlMedico.addUsuarioBuscadoListener(new UsuarioBuscadoListener() {
					public void usuarioBuscado(EventObject evt) {
						pnlMedicoUsuarioBuscado(evt);
					}
				});
			}
			
			{
				lblHora = new JLabel();
				this.add(lblHora, new AnchorConstraint(230, 252, 657, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHora.setText("Hora");
				lblHora.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblDia = new JLabel();
				this.add(lblDia, new AnchorConstraint(203, 252, 591, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDia.setText("Día");
				lblDia.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				btnBuscarSust = new JButton();
				this.add(btnBuscarSust, new AnchorConstraint(261, 12, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscarSust.setText("Buscar sustitutos");
				btnBuscarSust.setPreferredSize(new java.awt.Dimension(120, 26));
			}
			{
				dtcDiaCita = new JDateChooserCitas();
				this.add(dtcDiaCita, new AnchorConstraint(200, 12, 144, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcDiaCita.setPreferredSize(new java.awt.Dimension(302, 23));
				dtcDiaCita.setDateFormatString("dd/MM/yyyy");
				dtcDiaCita.setToolTipText("Formato dd/MM/yyyy. Para más ayuda haga clic en el icono de la derecha.");
				dtcDiaCita.setMinSelectableDate(new Date());
			}
			{
				lblHoraHasta = new JLabel();
				this.add(lblHoraHasta, new AnchorConstraint(230, 647, 568, 221, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHoraHasta.setText("Hasta:");
				lblHoraHasta.setPreferredSize(new java.awt.Dimension(35, 16));
			}
			{
				SpinnerNumberModel spnHoraHastaModel = new SpinnerNumberModel(0, 0, 24, 1);
				spnHoraHasta = new JSpinner();
				this.add(spnHoraHasta, new AnchorConstraint(227, 777, 578, 261, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				spnHoraHasta.setModel(spnHoraHastaModel);
				spnHoraHasta.setPreferredSize(new java.awt.Dimension(51, 23));
				spnHoraHasta.getEditor().setPreferredSize(new java.awt.Dimension(32, 19));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void pnlMedicoUsuarioBuscado(EventObject evt) {
		Hashtable<DiaSemana, Vector<String>> horasCitas;
		Usuario usuario;
		Medico medico;
		
		// Borramos los datos de la última consulta y
		// selección de sustituto
		limpiarCamposConsulta();
//		limpiarCamposSustitucion();

		// Obtenemos el usuario que se ha buscado en el panel de consulta
		// (puede ser null si ocurrió un error al buscar el usuario)
		usuario = pnlMedico.getUsuario();
		
		if(usuario != null) {
			
			try {
				
				// Comprobamos que el usuario es un médico
				if(usuario.getRol() != RolesUsuarios.Medico) {
					Dialogos.mostrarDialogoError(getFrame(), "Error", "Sólo se pueden planificar sustituciones para usuarios del sistema que sean médicos.");
				} else {
					
					// Guardamos el médico seleccionado
					medico = (Medico)usuario;
					
					// Consultamos al servidor el horario completo del médico
					// para saber qué días no va a trabajar
					horasCitas = getControlador().consultarHorarioMedico(medico.getDni());
					// Deshabilitamos los días de la semana que no son
					// laborables para el médico
					dtcDiaCita.quitarDiasSemanaDesactivados();
					for(DiaSemana dia : DiaSemana.values()) {
						if(horasCitas.get(dia) == null || horasCitas.get(dia).size() == 0) {
							dtcDiaCita.ponerDiaSemanaDesactivado(dia);
						}
					}
					
					// Activamos la búsqueda de sustitutos
					cambiarEstadoConsulta(true);
					
				}

			} catch(SQLException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			} catch(RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			} catch(Exception e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			}
		
		}
	}
	
	private void cambiarEstadoConsulta(boolean estado) {
		btnBuscarSust.setEnabled(estado);
		dtcDiaCita.setEnabled(estado);
		spnHoraDesde.setEnabled(estado);
		spnHoraHasta.setEnabled(estado);
	}
	
	private void limpiarCamposConsulta() {
		dtcDiaCita.setDate(null);
		spnHoraDesde.getModel().setValue(0);
		spnHoraHasta.getModel().setValue(0);
		cambiarEstadoConsulta(false);
	}
	
	//$hide<<$
	
}
