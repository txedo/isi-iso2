package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Utilidades;
import dominio.control.ControladorCliente;
import excepciones.JornadaIncompletaException;
import excepciones.MedicoInexistenteException;
import excepciones.NIFIncorrectoException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
public class JPCalendarioConsultar extends JPBase {
	
	private static final long serialVersionUID = -8579214415627504678L;
	private ArrayList<JPPeriodosTrabajo> periodos = null;
	private ArrayList<PeriodoTrabajo> periodosTrabajo;
	private JPPeriodosTrabajo pTrabajo;
	private int lastSelectedIndex = -1;
	private Medico mMedico;
	private JLabel lblNIFMedico;
	private JTextField txtNIF;
	private JScrollPane jScrollPane1;
	private JButton btnGuardar;
	private JCheckBox cbModificarTodos;
	private JList jListDiaSemana;
	private JCheckBox cbModificar;
	private JButton btnBuscar;
	private boolean creacion = false;
	private JPanel parent = null;
	
	public JPCalendarioConsultar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
	}
	
	public JPCalendarioConsultar(JFrame frame, ControladorCliente controlador, JPanel parent, String NIF) {
		super(frame, controlador);
		initGUI();
		txtNIF.setText(NIF);
		txtNIF.setEnabled(false);
		btnBuscar.setEnabled(false);
		cbModificar.setSelected(true);
		cbModificarActionPerformed(null);
		inicializarPanel();
		cbModificar.setEnabled(false);
		creacion = true;
		this.parent = parent;
	}
	
	private boolean esCreacion() {
		return creacion;
	}
	
	private void inicializarPanel() {
		if (periodos == null) {
			periodos = new ArrayList<JPPeriodosTrabajo>();
			for (DiaSemana s : DiaSemana.values()) {
				crearPanelPeriodosTrabajo(s);
			}
			// Creamos un panel más para "propagar" a todos los dias
			crearPanelPeriodosTrabajo(null);
		}
		cbModificar.setEnabled(true);
		jListDiaSemana.setEnabled(true);
		jListDiaSemana.setSelectedIndex(0);
	}

	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(565, 390);
			{
				btnGuardar = new JButton();
				this.add(btnGuardar, new AnchorConstraint(911, 11, 9, 779, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnGuardar.setText("Guardar calendario");
				btnGuardar.setPreferredSize(new java.awt.Dimension(114, 26));
				btnGuardar.setEnabled(false);
				btnGuardar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnGuardarActionPerformed(evt);
					}
				});
			}
			{
				cbModificarTodos = new JCheckBox();
				this.add(cbModificarTodos, new AnchorConstraint(190, 199, 491, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cbModificarTodos.setText("Propagar a todos los días");
				cbModificarTodos.setPreferredSize(new java.awt.Dimension(149, 19));
				cbModificarTodos.setEnabled(false);
				cbModificarTodos.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cbModificarTodosActionPerformed(evt);
					}
				});
			}
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1, new AnchorConstraint(42, 222, 332, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jScrollPane1.setPreferredSize(new java.awt.Dimension(115, 112));
				{
					// Creamos la lista de dias de trabajo
					// Tambien se crea un JPPeriodosTrabajo para cada dia (inicialmente NO visibles)
					// El índice de ambos arrays coincide
					ArrayList<String> dias = new ArrayList<String>();
					periodos = new ArrayList<JPPeriodosTrabajo>();
					for (DiaSemana s : DiaSemana.values()) {
						dias.add(s.toString());
						crearPanelPeriodosTrabajo(s);
					}
					// Creamos un panel más para "propagar" a todos los dias
					crearPanelPeriodosTrabajo(null);
					ListModel jListDiaSemanaModel = new DefaultComboBoxModel (dias.toArray());
					jListDiaSemana = new JList();
					jScrollPane1.setViewportView(jListDiaSemana);
					jListDiaSemana.setModel(jListDiaSemanaModel);
					jListDiaSemana.setPreferredSize(new java.awt.Dimension(112, 104));
					jListDiaSemana.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							jListDiaSemanaValueChanged(evt);
						}
					});
					jListDiaSemana.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					// Seleccionamos el primer dia de la semana
					jListDiaSemana.setEnabled(false);
				}
			}
			{
				cbModificar = new JCheckBox();
				this.add(cbModificar, new AnchorConstraint(164, 218, 408, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cbModificar.setText("Permitir modificar");
				cbModificar.setPreferredSize(new java.awt.Dimension(113, 19));
				cbModificar.setEnabled(false);
				cbModificar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cbModificarActionPerformed(evt);
					}
				});
			}
			{
				btnBuscar = new JButton();
				this.add(btnBuscar, new AnchorConstraint(7, 429, 78, 171, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnBuscar.setText("Buscar");
				btnBuscar.setDefaultCapable(true);
				//getRootPane().setDefaultButton(btnBuscar);
				btnBuscar.setPreferredSize(new java.awt.Dimension(71, 23));
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarActionPerformed(evt);
					}
				});
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(7, 285, 78, 65, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(96, 23));
				txtNIF.setToolTipText("Introduzca el NIF del médico del cual quiere consultar su calendario");
			}
			{
				lblNIFMedico = new JLabel();
				this.add(lblNIFMedico, new AnchorConstraint(11, 142, 62, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIFMedico.setText("NIF");
				lblNIFMedico.setPreferredSize(new java.awt.Dimension(70, 13));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		ArrayList<PeriodoTrabajo> periodos;
		try {
			Utilidades.comprobarNIF(txtNIF.getText());
			mMedico = getControlador().consultarMedico(txtNIF.getText());
			periodos = mMedico.getCalendario();
			inicializarPanel();
			actualizarPeriodosTrabajo(periodos);
		} catch (NIFIncorrectoException e) {
			txtNIF.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El NIF debe ser el número de DNI (incluyendo el 0) y la letra sin guión.");
			txtNIF.grabFocus();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MedicoInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void crearPanelPeriodosTrabajo (DiaSemana s) {
		pTrabajo = new JPPeriodosTrabajo(getFrame(), getControlador(), s);
		this.add(pTrabajo, new AnchorConstraint(42, 951, 878, 160, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
		pTrabajo.setPreferredSize(new java.awt.Dimension(400, 300));
		pTrabajo.activarPeriodos(false);
		pTrabajo.setVisible(false);
		periodos.add(pTrabajo);
	}
	
	private void actualizarPeriodosTrabajo(ArrayList<PeriodoTrabajo> periodosTrabajoAActualizar) {
		for (PeriodoTrabajo p : periodosTrabajoAActualizar) {
			DiaSemana s = p.getDia();
			int hora_inicio = p.getHoraInicio();
			int hora_final = p.getHoraFinal();
			JPPeriodosTrabajo jppe = periodos.get(s.ordinal());
			jppe.seleccionarPeriodo(hora_inicio, hora_final);
		}
	}

	private void jListDiaSemanaValueChanged(ListSelectionEvent evt) {
		if (jListDiaSemana.getSelectedIndex() != -1) {
			JPPeriodosTrabajo pe;
			for (JPPeriodosTrabajo p: periodos) p.setVisible(false);
			int index = jListDiaSemana.getSelectedIndex();
			// Si se desean aplicar cambios a todos los dias, cogemos el último Panel ya que está reservado para este propósito
			if (jListDiaSemana.getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
				pe = periodos.get(periodos.size()-1);
			}
			else {
				pe = periodos.get(index);
			}
			pe.setVisible(true);
		}
	}
	
	private void cbModificarActionPerformed(ActionEvent evt) {
		for (JPPeriodosTrabajo p: periodos) {
			p.activarPeriodos(cbModificar.isSelected());
		}
		cbModificarTodos.setEnabled(cbModificar.isSelected());
		btnGuardar.setEnabled(cbModificar.isSelected());
	}
	
	private void cbModificarTodosActionPerformed(ActionEvent evt) {
		if (cbModificarTodos.isSelected()) {
			lastSelectedIndex = jListDiaSemana.getSelectedIndex();
			jScrollPane1.setEnabled(false);
			jListDiaSemana.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jListDiaSemana.addSelectionInterval(0, DiaSemana.values().length-1);
			jListDiaSemana.setEnabled(false);
		}
		else {
			jListDiaSemana.setEnabled(true);
			jScrollPane1.setEnabled(true);
			jListDiaSemana.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListDiaSemana.setSelectedIndex(lastSelectedIndex);
		}
	}
	
	private void btnGuardarActionPerformed(ActionEvent evt) {
		// Siempre creamos una nueva instancia del ArrayList para evitar errores
		periodosTrabajo = new ArrayList<PeriodoTrabajo>();
		try {
			// Si no esta marcada la propagacion de cambios, recorremos todo el array menos el último elemento
			if (!cbModificarTodos.isSelected()) {
				for (JPPeriodosTrabajo pt : periodos) {
					if (!periodos.get(periodos.size()-1).equals(pt)) {
						if (pt.esJornadaLaboralCompleta()) {
							periodosTrabajo.addAll(pt.getPeriodosTrabajo());
						}
						else {
							// La jornada no esta completa y hay que completarla
							throw new JornadaIncompletaException();
						}
					}
				}	
			}
			// Si hay que propagar cambios como un calendario base cogemos el útimo panel
			else {
				// Iteraremos una vez por cada dia de la semana guardando sus valores
				JPPeriodosTrabajo pt = periodos.get(periodos.size()-1);
				if (pt.esJornadaLaboralCompleta()) {
					for (DiaSemana s : DiaSemana.values()) {
						pt.setDiaSemana(s);
						periodosTrabajo.addAll(pt.getPeriodosTrabajo());
					}
				}
				else {
					throw new JornadaIncompletaException();
				}
			}
			if (esCreacion ()) {
				// No guardar en la BD. Devolver la lista a la ventana que crea esto
				((JPUsuarioRegistrar)parent).setPeriodos(periodosTrabajo);
				((JDialog)this.getRootPane().getParent()).dispose();
			}
			else {
				// Se trata de una consulta->modificacion
				// Borrar y guardar en la BD
				mMedico.setCalendario(periodosTrabajo);
				getControlador().modificarMedico(mMedico);
				Dialogos.mostrarDialogoInformacion(null, "Operación satisfactoria", "El calendario ha sido actualizado correctamente.");
				restablecerFormulario();
			}
		} catch (JornadaIncompletaException e) {
			Dialogos.mostrarDialogoError(null, "Error", "La jornada de trabajo está incompleta.");
		} catch (RemoteException e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
		} catch (MedicoInexistenteException e) {
			Dialogos.mostrarDialogoError(null, "Error", "El médico indicado no se encuentra dado de alta en el sistema.");
		} catch (SQLException e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
		}
	}

	private void restablecerFormulario() {
		for (JPPeriodosTrabajo pt : periodos) {
			pt.setVisible(false);
			pt.setEnabled(false);
		}
		jListDiaSemana.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jListDiaSemana.clearSelection();
		jListDiaSemana.setEnabled(false);
		periodos = null;
		txtNIF.setText("");
		cbModificar.setSelected(false);
		cbModificar.setEnabled(false);
		cbModificarTodos.setSelected(false);
		cbModificarTodos.setEnabled(false);
	}
}
