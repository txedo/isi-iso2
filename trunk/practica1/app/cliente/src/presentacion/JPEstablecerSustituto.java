package presentacion;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.JDateChooserCitas;
import presentacion.auxiliar.UsuarioBuscadoListener;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.RolesUsuarios;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorCliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
 * Panel que permite buscar y elegir un m�dico adecuado para sustituir a
 * otro m�dico en una determinada fecha.
 */
public class JPEstablecerSustituto extends JPBase {

	private static final long serialVersionUID = -3582780436000291004L;
	
	private Medico medico;
	private Vector<Medico> sustitutos;
	
	private ListModel lstSustitutosModel; 
	private JDateChooserCitas dtcDiaSustitucion;
	private JLabel lblDatos;
	private JSeparator sepSeparador;
	private JPUsuarioConsultar pnlMedico;
	private JLabel lblHora;
	private JLabel lblDia;
	private JLabel lblSustitutos;
	private JButton btnRestablecerTodo;
	private JButton btnAsignarSustituto;
	private JList lstSustitutos;
	private JScrollPane scpSustitutos;
	private JButton btnBuscarSustitutos;
	private JSeparator sepSeparador2;
	private JSpinner spnHoraHasta;
	private JLabel lblHoraHasta;
	private JSpinner spnHoraDesde;
	private JLabel lblHoraDesde;

	public JPEstablecerSustituto() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPEstablecerSustituto
	}
	
	public JPEstablecerSustituto(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		cambiarEstadoConsulta(false);
		cambiarEstadoSustitucion(false);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 528));
			{
				scpSustitutos = new JScrollPane();
				this.add(scpSustitutos, new AnchorConstraint(360, 12, 924, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				scpSustitutos.setPreferredSize(new java.awt.Dimension(406, 111));
				{
					lstSustitutosModel = new DefaultComboBoxModel();
					lstSustitutos = new JList();
					scpSustitutos.setViewportView(lstSustitutos);
					lstSustitutos.setModel(lstSustitutosModel);
				}
			}
			{
				lblSustitutos = new JLabel();
				this.add(lblSustitutos, new AnchorConstraint(337, 261, 639, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblSustitutos.setText("Posibles sustitutos:");
				lblSustitutos.setPreferredSize(new java.awt.Dimension(100, 16));
			}
			{
				SpinnerNumberModel spnHoraDesdeModel = new SpinnerNumberModel(IConstantes.HORA_INICIO_JORNADA, IConstantes.HORA_INICIO_JORNADA, IConstantes.HORA_FIN_JORNADA, 1);
				spnHoraDesde = new JSpinner();
				this.add(spnHoraDesde, new AnchorConstraint(253, 538, 578, 158, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				spnHoraDesde.setModel(spnHoraDesdeModel);
				spnHoraDesde.setPreferredSize(new java.awt.Dimension(51, 23));
				spnHoraDesde.getEditor().setPreferredSize(new java.awt.Dimension(36, 19));
			}
			{
				lblHoraDesde = new JLabel();
				this.add(lblHoraDesde, new AnchorConstraint(256, 403, 568, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHoraDesde.setText("Desde:");
				lblHoraDesde.setPreferredSize(new java.awt.Dimension(35, 16));
			}
			{
				lblDatos = new JLabel();
				this.add(lblDatos, new AnchorConstraint(201, 215, 633, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDatos.setText("Datos de la sustituci�n:");
				lblDatos.setPreferredSize(new java.awt.Dimension(177, 16));
			}
			{
				sepSeparador = new JSeparator();
				this.add(sepSeparador, new AnchorConstraint(191, 6, 587, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador.setPreferredSize(new java.awt.Dimension(418, 10));
			}
			{
				pnlMedico = new JPUsuarioConsultar(this.getFrame(), this.getControlador());
				this.add(pnlMedico, new AnchorConstraint(0, 0, 608, 0, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				pnlMedico.setPreferredSize(new java.awt.Dimension(430, 184));
				pnlMedico.reducirPanel();
				pnlMedico.addUsuarioBuscadoListener(new UsuarioBuscadoListener() {
					public void usuarioBuscado(EventObject evt) {
						pnlMedicoUsuarioBuscado(evt);
					}
				});
			}
			
			{
				lblHora = new JLabel();
				this.add(lblHora, new AnchorConstraint(256, 252, 657, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHora.setText("Hora");
				lblHora.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblDia = new JLabel();
				this.add(lblDia, new AnchorConstraint(229, 252, 591, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDia.setText("D�a");
				lblDia.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				btnBuscarSustitutos = new JButton();
				this.add(btnBuscarSustitutos, new AnchorConstraint(287, 12, 855, 798, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscarSustitutos.setText("Buscar sustitutos");
				btnBuscarSustitutos.setPreferredSize(new java.awt.Dimension(120, 26));
				btnBuscarSustitutos.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarSustitutosActionPerformed(evt);
					}
				});
			}
			{
				dtcDiaSustitucion = new JDateChooserCitas();
				this.add(dtcDiaSustitucion, new AnchorConstraint(226, 12, 144, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcDiaSustitucion.setPreferredSize(new java.awt.Dimension(302, 23));
				dtcDiaSustitucion.setDateFormatString("dd/MM/yyyy");
				dtcDiaSustitucion.setToolTipText("Formato dd/MM/yyyy. Para m�s ayuda haga clic en el icono de la derecha.");
				dtcDiaSustitucion.setMinSelectableDate(new Date());
			}
			{
				lblHoraHasta = new JLabel();
				this.add(lblHoraHasta, new AnchorConstraint(256, 647, 568, 221, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHoraHasta.setText("Hasta:");
				lblHoraHasta.setPreferredSize(new java.awt.Dimension(35, 16));
			}
			{
				SpinnerNumberModel spnHoraHastaModel = new SpinnerNumberModel(IConstantes.HORA_FIN_JORNADA, IConstantes.HORA_INICIO_JORNADA, IConstantes.HORA_FIN_JORNADA, 1);
				spnHoraHasta = new JSpinner();
				this.add(spnHoraHasta, new AnchorConstraint(253, 777, 578, 261, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				spnHoraHasta.setModel(spnHoraHastaModel);
				spnHoraHasta.setPreferredSize(new java.awt.Dimension(51, 23));
				spnHoraHasta.getEditor().setPreferredSize(new java.awt.Dimension(32, 19));
			}
			{
				sepSeparador2 = new JSeparator();
				this.add(sepSeparador2, new AnchorConstraint(326, 6, 602, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				sepSeparador2.setPreferredSize(new java.awt.Dimension(418, 8));
			}
			{
				btnAsignarSustituto = new JButton();
				this.add(btnAsignarSustituto, new AnchorConstraint(483, 11, 970, 696, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnAsignarSustituto.setText("Asignar sustituto");
				btnAsignarSustituto.setPreferredSize(new java.awt.Dimension(120, 26));
				btnAsignarSustituto.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAsignarSustitutoActionPerformed(evt);
					}
				});
			}
			{
				btnRestablecerTodo = new JButton();
				this.add(btnRestablecerTodo, new AnchorConstraint(483, 308, 961, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnRestablecerTodo.setText("Restablecer todo");
				btnRestablecerTodo.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRestablecerTodo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerTodoActionPerformed(evt);
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//$hide>>$
	
	private void pnlMedicoUsuarioBuscado(EventObject evt) {
		Hashtable<DiaSemana, Vector<String>> horasCitas;
		Usuario usuario;
		
		// Borramos los datos de la �ltima consulta y
		// selecci�n de sustituto
		limpiarCamposConsulta();
		limpiarCamposSustitucion();

		// Obtenemos el usuario que se ha buscado en el panel de consulta
		// (puede ser null si ocurri� un error al buscar el usuario)
		usuario = pnlMedico.getUsuario();
		
		if(usuario != null) {
			
			try {
				
				// Comprobamos que el usuario es un m�dico
				if(usuario.getRol() != RolesUsuarios.Medico) {
					Dialogos.mostrarDialogoError(getFrame(), "Error", "S�lo se pueden planificar sustituciones para usuarios del sistema que sean m�dicos.");
				} else {
					
					// Guardamos el m�dico seleccionado
					medico = (Medico)usuario;
					
					// Consultamos al servidor el horario completo del m�dico
					// para saber qu� d�as no va a trabajar
					horasCitas = getControlador().consultarHorarioMedico(medico.getDni());
					// Deshabilitamos los d�as de la semana que no son
					// laborables para el m�dico
					dtcDiaSustitucion.quitarDiasSemanaDesactivados();
					for(DiaSemana dia : DiaSemana.values()) {
						if(horasCitas.get(dia) == null || horasCitas.get(dia).size() == 0) {
							dtcDiaSustitucion.ponerDiaSemanaDesactivado(dia);
						}
					}
					
					// Activamos la b�squeda de sustitutos
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
	
	private void btnBuscarSustitutosActionPerformed(ActionEvent evt) {
		Vector<String> nombres;
		
		// Borramos los datos de la �ltima selecci�n de sustituto
		limpiarCamposSustitucion();
		
		try {
			
			if(dtcDiaSustitucion.getDate() == null) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "Seleccione el d�a en el que se har� la sustituci�n.");
			} else if((Integer)spnHoraDesde.getValue() >= (Integer)spnHoraHasta.getValue()) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "La hora final de la sustituci�n debe ser mayor que la inicial.");
			} else {
				
				// Obtenemos la lista de m�dicos que pueden sustituir
				// al m�dico buscado en la fecha y hora dadas
				sustitutos = getControlador().obtenerPosiblesSustitutos(medico.getDni(), dtcDiaSustitucion.getDate(), (Integer)spnHoraDesde.getValue(), (Integer)spnHoraHasta.getValue());
				if(sustitutos.size() == 0) {
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "B�squeda fallida", "No se ha encontrado ning�n m�dico que pueda hacer la sustituci�n solicitada.");
				} else {
					
					// Mostramos los nombres de los m�dicos devueltos
					if(sustitutos.size() == 1) {
						Dialogos.mostrarDialogoInformacion(getFrame(), "B�squeda correcta", "Se ha encontrado 1 posible sustituto.");
					} else {
						Dialogos.mostrarDialogoInformacion(getFrame(), "B�squeda correcta", "Se han encontrado " + sustitutos.size() + " posibles sustitutos.");
					}
					nombres = new Vector<String>();
					for(Medico medico : sustitutos) {
						nombres.add(medico.getApellidos() + ", " + medico.getNombre() + " (" + medico.getDni() + ")");
					}
					rellenarListaSustitutos(nombres);
					
					// Activamos la selecci�n de sustituto
					cambiarEstadoSustitucion(true);
					
				}
			}
			
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	}

	private void btnAsignarSustitutoActionPerformed(ActionEvent evt) {
		Vector<Date> dias;
		Medico sustituto;

		try {
			
			if(lstSustitutos.getSelectedIndex() == -1) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", "Seleccione el m�dico que har� la sustituci�n.");
			} else {
				
				// Creamos la lista de d�as y recuperamos el sustituto
				dias = new Vector<Date>();
				dias.add(dtcDiaSustitucion.getDate());
				sustituto = sustitutos.get(lstSustitutos.getSelectedIndex());
				
				// Solicitamos que se asigne la sustituci�n
				getControlador().asignarSustituto(medico, dias, (Integer)spnHoraDesde.getValue(), (Integer)spnHoraHasta.getValue(), sustituto);

				// Mostramos el resultado de la operaci�n y limpiamos el panel
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operaci�n correcta", "La sustituci�n se ha almacenado correctamente.");
				restablecerPanel();
				
			}
			
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	}
	
	private void btnRestablecerTodoActionPerformed(ActionEvent evt) {
		restablecerPanel();
	}
	
	private void rellenarListaSustitutos(Vector<String> elementos) {
		lstSustitutosModel = new DefaultComboBoxModel(elementos);
		lstSustitutos.setModel(lstSustitutosModel);
	}
	
	private void cambiarEstadoConsulta(boolean estado) {
		btnBuscarSustitutos.setEnabled(estado);
		dtcDiaSustitucion.setEnabled(estado);
		spnHoraDesde.setEnabled(estado);
		spnHoraHasta.setEnabled(estado);
	}
	
	private void cambiarEstadoSustitucion(boolean estado) {
		btnAsignarSustituto.setEnabled(estado);
		lstSustitutos.setEnabled(estado);
	}
	
	private void limpiarCamposConsulta() {
		dtcDiaSustitucion.setDate(null);
		spnHoraDesde.getModel().setValue(IConstantes.HORA_INICIO_JORNADA);
		spnHoraHasta.getModel().setValue(IConstantes.HORA_FIN_JORNADA);
		cambiarEstadoConsulta(false);
	}
	
	private void limpiarCamposSustitucion() {
		rellenarListaSustitutos(new Vector<String>());
		cambiarEstadoSustitucion(false);
	}
	
	// M�todos p�blicos
	
	public void restablecerPanel() {
		pnlMedico.restablecerPanel();
		limpiarCamposConsulta();
		limpiarCamposSustitucion();	
	}
	
	//$hide<<$
	
}