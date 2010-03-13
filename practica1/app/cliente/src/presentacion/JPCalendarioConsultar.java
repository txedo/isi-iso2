package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.PeriodoTrabajo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.HoraNoSeleccionadaListener;
import presentacion.auxiliar.HoraSeleccionadaListener;

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
 * Panel que permite ver y editar las horas de trabajo de cada día
 * de la semana.
 */
public class JPCalendarioConsultar extends javax.swing.JPanel implements IConstantes {
	
	private static final long serialVersionUID = -8579214415627504678L;

	private final int DESPL_X = 157;
	private final int DESPL_Y = 29;

	private Vector<String> dias;
	private Vector<JPPeriodosTrabajo> jpPeriodos;
	private Vector<PeriodoTrabajo> periodosTrabajo;
	
	private int horas;
	private int lastSelectedIndex = -1;

	private JPPeriodosTrabajo pTrabajo;
	private JScrollPane scpDiasSemana;
	private JLabel lblHoras;
	private JLabel lblDias;
	private JLabel lblHorasSemanales;
	private JButton btnPropagar;
	private JList lstDiasSemana;
		
	public JPCalendarioConsultar() {
		super();
		initGUI();
		// Creamos la lista de dias de trabajo y un panel para cada
		// día de la semana (el índice de ambos arrays coincide)
		jpPeriodos = new Vector<JPPeriodosTrabajo>();
		dias = new Vector<String>();
		for(DiaSemana dia : DiaSemana.values()) {
			dias.add(dia.toString());
			crearPanelPeriodosTrabajo(dia);
		}
		// Rellenamos la lista de días
		ListModel jListDiaSemanaModel = new DefaultComboBoxModel(dias.toArray());
		lstDiasSemana.setModel(jListDiaSemanaModel);
		lstDiasSemana.setSelectedIndex(0);
	}

	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			{
				lblDias = new JLabel();
				this.add(lblDias, new AnchorConstraint(9, 84, 67, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDias.setText("Días");
				lblDias.setPreferredSize(new java.awt.Dimension(36, 16));
			}
			{
				lblHorasSemanales = new JLabel();
				this.add(lblHorasSemanales, new AnchorConstraint(198, 230, 532, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHorasSemanales.setText("Horas semanales seleccionadas: 0");
				lblHorasSemanales.setPreferredSize(new java.awt.Dimension(212, 13));
			}
			{
				btnPropagar = new JButton();
				this.add(btnPropagar, new AnchorConstraint(155, 216, 467, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnPropagar.setText("Propagar selección");
				btnPropagar.setPreferredSize(new java.awt.Dimension(132, 24));
				btnPropagar.setName("btnPropagar");
				btnPropagar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnPropagarActionPerformed(evt);
					}
				});
			}
			{
				scpDiasSemana = new JScrollPane();
				this.add(scpDiasSemana, new AnchorConstraint(31, 222, 332, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				scpDiasSemana.setPreferredSize(new java.awt.Dimension(132, 112));
				{
					lstDiasSemana = new JList();
					scpDiasSemana.setViewportView(lstDiasSemana);
					lstDiasSemana.setPreferredSize(new java.awt.Dimension(127, 109));
					lstDiasSemana.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							jListDiaSemanaValueChanged(evt);
						}
					});
					lstDiasSemana.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					lstDiasSemana.setName("lstDiasSemanas");
					lstDiasSemana.setSelectedIndex(0);
				}
			}
			{
				lblHoras = new JLabel();
				this.add(lblHoras, new AnchorConstraint(9, 349, 67, 161, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHoras.setText("Horas");
				lblHoras.setPreferredSize(new java.awt.Dimension(40, 16));
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getMessage());
		}
	}
	
	//$hide>>$
	
	public Vector<PeriodoTrabajo> getPeriodosTrabajo() {
		obtenerPeriodosDeTrabajoSeleccionadosComprimidos();
		return periodosTrabajo;
	}
	
	public void setPeriodosTrabajo(Vector<PeriodoTrabajo> periodos) {
		this.periodosTrabajo = periodos;
		horas = 0;
		for(PeriodoTrabajo pt : periodosTrabajo) {
			horas += pt.getHoraFinal() - pt.getHoraInicio();
		}
		lblHorasSemanales.setText("Horas semanales seleccionadas: " + horas);
		actualizarPeriodosTrabajo(this.periodosTrabajo);
	}
	
	public int getHorasSemanales() {
		return horas;
	}

	public void setModificable(boolean modificable) {	
		for(JPPeriodosTrabajo panel : jpPeriodos) {
			panel.activarPeriodos(modificable);
		}
		btnPropagar.setEnabled(modificable);
	}
	
	private void crearPanelPeriodosTrabajo(DiaSemana dia) {
		// Creamos un panel de periodos de trabajo para el día indicado
		pTrabajo = new JPPeriodosTrabajo(dia);
		// Posicionamos el panel de periodos de trabajo
		this.add(pTrabajo, new AnchorConstraint(DESPL_Y, 0, 0, DESPL_X, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
		// Le damos un tamaño al panel
		pTrabajo.setPreferredSize(new java.awt.Dimension(400, 300));
		// Inicialmente, deshabilitamos todos sus checkboxes
		pTrabajo.activarPeriodos(false);
		pTrabajo.setVisible(false);
		pTrabajo.addHoraSeleccionadaListener(new HoraSeleccionadaListener() {
			public void horaSeleccionada(EventObject evt) {
				pTrabajoHoraSeleccionada(evt);
			}
		});
		pTrabajo.addHoraNoSeleccionadaListener(new HoraNoSeleccionadaListener() {
			public void horaNoSeleccionada(EventObject evt) {
				pTrabajoHoraNoSeleccionada(evt);
			}
		});
		jpPeriodos.add(pTrabajo);
	}
	
	private void pTrabajoHoraSeleccionada(EventObject evt) {
		horas++;
		lblHorasSemanales.setText("Horas semanales seleccionadas: " + horas);
	}
	
	private void pTrabajoHoraNoSeleccionada(EventObject evt) {
		horas--;
		lblHorasSemanales.setText("Horas semanales seleccionadas: " + horas);
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
		if (lstDiasSemana.getSelectedIndex() != -1) {
			JPPeriodosTrabajo pe;
			for (JPPeriodosTrabajo p: jpPeriodos) p.setVisible(false);
			int index = lstDiasSemana.getSelectedIndex();
			// Si se desean aplicar cambios a todos los dias, cogemos el último Panel ya que está reservado para este propósito
			if (lstDiasSemana.getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
				pe = jpPeriodos.get(lastSelectedIndex);
			}
			else {
				pe = jpPeriodos.get(index);
			}
			pe.setVisible(true);
		}
	}
	
	private void obtenerPeriodosDeTrabajoSeleccionadosComprimidos() {
		periodosTrabajo = new Vector<PeriodoTrabajo>();
		for (JPPeriodosTrabajo pt : jpPeriodos) {
			periodosTrabajo.addAll(pt.getPeriodosTrabajo());
		}	
	}
	
	private void btnPropagarActionPerformed(ActionEvent evt) {
		int horasAux = 0;
		horas = 0;
		int index = lstDiasSemana.getSelectedIndex();
		JPPeriodosTrabajo jpptActivo = jpPeriodos.get(index);
		for (JPPeriodosTrabajo jppt : jpPeriodos) {
			if (!jppt.equals(jpptActivo)) {
				jppt.deseleccionarPeriodos();
				jppt.seleccionarPeriodos(jpptActivo.getPeriodosTrabajo());
				horas += jppt.getHorasSeleccionadas();
				horasAux = jppt.getHorasSeleccionadas();
			}
			
		}
		horas += horasAux;
		lblHorasSemanales.setText("Horas semanales seleccionadas: " + horas);
	}
	
	public void restablecerDiaSeleccionado() {
		if(lstDiasSemana.getSelectedIndex() != -1) {
			restablecerFormulario(lstDiasSemana.getSelectedIndex());
		}
	}
	
	public void restablecerTodo() {
		for (DiaSemana dia : DiaSemana.values()) {
			restablecerFormulario(dia.ordinal());
		}
	}
	
	private void restablecerFormulario (int index) {
		JPPeriodosTrabajo jpp = jpPeriodos.get(index);
		jpp.deseleccionarPeriodos();
		horas -= jpp.getHorasDeseleccionadas();
		lblHorasSemanales.setText("Horas semanales seleccionadas: " + horas);
	}
	
	//$hide<<$
	
}
