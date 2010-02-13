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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
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
public class JPCalendarioConsultar extends JPBase implements IConstantes {
	
	private static final long serialVersionUID = -8579214415627504678L;
	private Vector<String> dias;
	private Vector<JPPeriodosTrabajo> jpPeriodos;
	private Vector<PeriodoTrabajo> periodosTrabajo;
	private JPPeriodosTrabajo pTrabajo;
	private int lastSelectedIndex = -1;
	private JScrollPane jScrollPane1;
	private JLabel lblHoras;
	private JButton btnRestablecerTodo;
	private JButton btnRestablecer;
	private JButton btnPropagar;
	private JButton btnAceptar;
	private JList jListDiaSemana;
	private JFrame parent = null;
	
	private final int DESPL_X= 150;
	private final int DESPL_Y = 10;
	
	private int horas;
	
	public JPCalendarioConsultar(JFrame parent, Vector<PeriodoTrabajo> p) {
		super();
		this.parent = parent;
		this.periodosTrabajo = p;
		for (PeriodoTrabajo pt: periodosTrabajo)
			horas += pt.getHoraFinal() - pt.getHoraInicio();
		initGUI();
		actualizarPeriodosTrabajo(this.periodosTrabajo);
		
	}

	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(565, 390));
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			{
				lblHoras = new JLabel();
				this.add(lblHoras, new AnchorConstraint(187, 230, 532, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHoras.setText("Horas semanales seleccionadas: " + horas);
				lblHoras.setPreferredSize(new java.awt.Dimension(189, 13));
			}
			{
				btnRestablecerTodo = new JButton();
				this.add(btnRestablecerTodo, new AnchorConstraint(916, 275, 11, 308, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnRestablecerTodo.setText("Restablecer todo");
				btnRestablecerTodo.setPreferredSize(new java.awt.Dimension(120, 22));
				btnRestablecerTodo.setEnabled(false);
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
				btnRestablecer.setEnabled(false);
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnAceptar = new JButton();
				this.add(btnAceptar, new AnchorConstraint(911, 10, 11, 779, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnAceptar.setText("Aceptar");
				btnAceptar.setPreferredSize(new java.awt.Dimension(90, 22));
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAceptarActionPerformed(evt);
					}
				});
			}
			{
				btnPropagar = new JButton();
				this.add(btnPropagar, new AnchorConstraint(134, 216, 467, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnPropagar.setText("Propagar selección");
				btnPropagar.setPreferredSize(new java.awt.Dimension(132, 24));
				btnPropagar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnPropagarActionPerformed(evt);
					}
				});
			}
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1, new AnchorConstraint(10, 222, 332, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				jScrollPane1.setPreferredSize(new java.awt.Dimension(132, 112));
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
					jListDiaSemana.setPreferredSize(new java.awt.Dimension(127, 109));
					jListDiaSemana.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							jListDiaSemanaValueChanged(evt);
						}
					});
					jListDiaSemana.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					jListDiaSemana.setSelectedIndex(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setModificable (boolean b) {	
		for (JPPeriodosTrabajo p: jpPeriodos) {
			p.activarPeriodos(b);
		}
		btnPropagar.setEnabled(b);
		btnRestablecer.setEnabled(b);
		btnRestablecerTodo.setEnabled(b);
	}

	private void crearPanelPeriodosTrabajo (DiaSemana s) {
		// Creamos un panel de periodos de trabajo para el día "s"
		pTrabajo = new JPPeriodosTrabajo(s);
		// Posicionamos el panel de periodos de trabajo
		this.add(pTrabajo, new AnchorConstraint(DESPL_Y, 951, 878, DESPL_X, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
		lblHoras.setText("Horas semanales seleccionadas: " + horas);
	}
	
	private void pTrabajoHoraNoSeleccionada(EventObject evt) {
		horas--;
		lblHoras.setText("Horas semanales seleccionadas: " + horas);
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
	
	private void btnAceptarActionPerformed(ActionEvent evt) {
		// Siempre creamos una nueva instancia del Vector para evitar errores
		obtenerPeriodosDeTrabajoSeleccionadosComprimidos();
		((JFCalendarioLaboral)parent).setPeriodos(periodosTrabajo);
		((JFCalendarioLaboral)parent).cerrarVentana();
	}

	private void obtenerPeriodosDeTrabajoSeleccionadosComprimidos() {
		periodosTrabajo = new Vector<PeriodoTrabajo>();
		for (JPPeriodosTrabajo pt : jpPeriodos) {
			periodosTrabajo.addAll(pt.getPeriodosTrabajo());
		}	
	}

	public Vector<PeriodoTrabajo> getPeriodosTrabajo() {
		obtenerPeriodosDeTrabajoSeleccionadosComprimidos();
		return periodosTrabajo;
	}
	
	private void btnPropagarActionPerformed(ActionEvent evt) {
		int horasAux = 0;
		horas = 0;
		int index = jListDiaSemana.getSelectedIndex();
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
		lblHoras.setText("Horas semanales seleccionadas: " + horas);
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
		jpp.deseleccionarPeriodos();
		horas -= jpp.getHorasDeseleccionadas();
		lblHoras.setText("Horas semanales seleccionadas: " + horas);
	}
}
