package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.PeriodoTrabajo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerListModel;
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
public class JPCalendarioConsultar extends JPBase implements IConstantes {
	
	private static final long serialVersionUID = -8579214415627504678L;
	private Vector<String> dias;
	private Vector<JPPeriodosTrabajo> jpPeriodos = null;
	private Vector<PeriodoTrabajo> periodosTrabajo;
	private JPPeriodosTrabajo pTrabajo;
	private int lastSelectedIndex = -1;
	private JScrollPane jScrollPane1;
	private JLabel lblHoras;
	private JSpinner spnHoras;
	private JButton btnRestablecerTodo;
	private JButton btnRestablecer;
	private JButton btnPropagar;
	private JButton btnGuardar;
	private JList jListDiaSemana;
	private JCheckBox cbModificar;
	private JDialog parent = null;
	
	public JPCalendarioConsultar(JDialog parent, Vector<PeriodoTrabajo> p) {
		super();
		initGUI();
		this.parent = parent;
		this.periodosTrabajo = p;
		actualizarPeriodosTrabajo(this.periodosTrabajo);
		jListDiaSemana.setEnabled(true);
		jListDiaSemana.setSelectedIndex(0);
		cbModificar.setSelected(true);
		cbModificarActionPerformed(null);
		cbModificar.setEnabled(false);
	}

	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			{
				lblHoras = new JLabel();
				this.add(lblHoras, new AnchorConstraint(12, 415, 67, 153, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHoras.setText("Horas semanales");
				lblHoras.setPreferredSize(new java.awt.Dimension(102, 14));
			}
			{
				spnHoras = new JSpinner();
				inicializarSpinnerHoras (spnHoras);
				this.add(spnHoras, new AnchorConstraint(12, 615, 85, 267, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				spnHoras.setPreferredSize(new java.awt.Dimension(80, 21));
			}
			{
				btnRestablecerTodo = new JButton();
				this.add(btnRestablecerTodo, new AnchorConstraint(916, 275, 11, 308, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnRestablecerTodo.setText("Restablecer todo");
				btnRestablecerTodo.setPreferredSize(new java.awt.Dimension(120, 22));
				btnRestablecerTodo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerTodoActionPerformed(evt);
					}
				});
			}
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(916, 114, 11, 540, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(100, 22));
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnPropagar = new JButton();
				this.add(btnPropagar, new AnchorConstraint(161, 443, 467, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnPropagar.setText("Propagar selección");
				btnPropagar.setPreferredSize(new java.awt.Dimension(110, 21));
				btnPropagar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnPropagarActionPerformed(evt);
					}
				});
			}
			{
				btnGuardar = new JButton();
				this.add(btnGuardar, new AnchorConstraint(911, 10, 11, 779, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnGuardar.setText("Guardar");
				btnGuardar.setPreferredSize(new java.awt.Dimension(90, 22));
				btnGuardar.setEnabled(false);
				btnGuardar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnGuardarActionPerformed(evt);
					}
				});
			}
			{
				cbModificar = new JCheckBox();
				this.add(cbModificar, new AnchorConstraint(132, 218, 408, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cbModificar.setText("Permitir modificar");
				cbModificar.setPreferredSize(new java.awt.Dimension(110, 20));
				cbModificar.setEnabled(false);
				cbModificar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cbModificarActionPerformed(evt);
					}
				});
			}
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1, new AnchorConstraint(10, 222, 332, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jScrollPane1.setPreferredSize(new java.awt.Dimension(115, 112));
				{
					// Creamos la lista de dias de trabajo
					// Tambien se crea un JPPeriodosTrabajo para cada dia (inicialmente NO visibles)
					// El índice de ambos arrays coincide
					dias = new Vector<String>();
					jpPeriodos = new Vector<JPPeriodosTrabajo>();
					// Creamos un panel para cada dia que no sea sabado ni domingo
					for (DiaSemana s : DiaSemana.values()) {
						dias.add(s.toString());
						crearPanelPeriodosTrabajo(s);
					}
					ListModel jListDiaSemanaModel = new DefaultComboBoxModel (dias.toArray());
					jListDiaSemana = new JList();
					jScrollPane1.setViewportView(jListDiaSemana);
					jListDiaSemana.setModel(jListDiaSemanaModel);
					jListDiaSemana.setPreferredSize(new java.awt.Dimension(112, 109));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void inicializarSpinnerHoras(JSpinner spnHoras) {
		String [] horas = new String [HORAS_SEMANALES+1];
		for (int i = 0; i <= HORAS_SEMANALES; i++)
			horas[i] = i+"";
		SpinnerListModel spnHorasModel = new SpinnerListModel(horas);
		spnHoras.setModel(spnHorasModel);
	}

	private void crearPanelPeriodosTrabajo (DiaSemana s) {
		pTrabajo = new JPPeriodosTrabajo(s);
		this.add(pTrabajo, new AnchorConstraint(42, 951, 878, 160, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
		pTrabajo.setPreferredSize(new java.awt.Dimension(400, 300));
		pTrabajo.activarPeriodos(false);
		pTrabajo.setVisible(false);
		jpPeriodos.add(pTrabajo);
	}
	
	private void actualizarPeriodosTrabajo(Vector<PeriodoTrabajo> periodosTrabajoAActualizar) {
		if (periodosTrabajoAActualizar != null) {
			for (PeriodoTrabajo p : periodosTrabajoAActualizar) {
				DiaSemana dia = p.getDia();
				int hora_inicio = p.getHoraInicio();
				int hora_final = p.getHoraFinal();
				JPPeriodosTrabajo jppe = jpPeriodos.get(dia.ordinal());
				jppe.seleccionarPeriodos(hora_inicio, hora_final);	
			}
		}
	}

	private void jListDiaSemanaValueChanged(ListSelectionEvent evt) {
		if (jListDiaSemana.getSelectedIndex() != -1) {
			JPPeriodosTrabajo pe;
			for (JPPeriodosTrabajo p: jpPeriodos) p.setVisible(false);
			int index = jListDiaSemana.getSelectedIndex();
			// Si se desean aplicar cambios a todos los dias, cogemos el último Panel ya que está reservado para este propósito
			if (jListDiaSemana.getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
				pe = jpPeriodos.get(lastSelectedIndex);
			}
			else {
				pe = jpPeriodos.get(index);
			}
			pe.setVisible(true);
		}
	}
	
	private void cbModificarActionPerformed(ActionEvent evt) {
		for (JPPeriodosTrabajo p: jpPeriodos) {
			p.activarPeriodos(cbModificar.isSelected());
		}
		btnGuardar.setEnabled(cbModificar.isSelected());
	}
	
	private void btnGuardarActionPerformed(ActionEvent evt) {
		// Siempre creamos una nueva instancia del Vector para evitar errores
		periodosTrabajo = new Vector<PeriodoTrabajo>();
		obtenerPeriodosDeTrabajoSeleccionados();
		((JDCalendarioLaboral)parent).setPeriodos(periodosTrabajo);
		((JDCalendarioLaboral)parent).dispose();
	}

	private void obtenerPeriodosDeTrabajoSeleccionados() {
		periodosTrabajo = new Vector<PeriodoTrabajo>();
		for (JPPeriodosTrabajo pt : jpPeriodos) {
			periodosTrabajo.addAll(pt.getPeriodosTrabajo());
		}	
	}

	public Vector<PeriodoTrabajo> getPeriodosTrabajo() {
		obtenerPeriodosDeTrabajoSeleccionados();
		return periodosTrabajo;
	}
	
	private void btnPropagarActionPerformed(ActionEvent evt) {
		System.out.println("btnPropagar.actionPerformed, event="+evt);
		//TODO add your code for btnPropagar.actionPerformed
	}
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		int index = jListDiaSemana.getSelectedIndex();
		restablecerFormulario(index);
	}
	
	private void btnRestablecerTodoActionPerformed(ActionEvent evt) {
		for (DiaSemana dia : DiaSemana.values()) {
			restablecerFormulario(dia.ordinal());
		}
	}
	
	private void restablecerFormulario (int index) {
		JPPeriodosTrabajo jpp = jpPeriodos.get(index);
		jpp.activarPeriodos(false);
	}
}
