package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.PeriodoTrabajo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.event.EventListenerList;

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
 * Panel que permite editar las horas de trabajo de un día determinado
 * de la semana.
 */
public class JPPeriodosTrabajo extends javax.swing.JPanel implements IConstantes {
	
	private static final long serialVersionUID = -8549178364759642024L;
	
	private static final int WIDTH = 125;
	private static final int HEIGHT = 18;
	private static final int DESPL_X = 156;
	private static final int DESPL_Y = 27;
	private static final int INIT_X = 0;
	private static final int INIT_Y = 0;
	
	private EventListenerList listenerList;

	private ArrayList<JCheckBox> chbPeriodos = new ArrayList<JCheckBox>();
	private JCheckBox chbPeriodoTrabajo;
	private Vector<PeriodoTrabajo> periodos;
	private DiaSemana diaSemana;

	private int horasDeseleccionadas = 0;
	private int horasSeleccionadas = 0;
		
	public JPPeriodosTrabajo(DiaSemana dia) {
		super();
		initGUI();
		diaSemana = dia;
		listenerList = new EventListenerList();
		// Creamos un checkbox para cada periodo de trabajo
		// y calculamos sus coordenadas con respecto al anterior
		int posX = INIT_X;
		int posY = INIT_Y;
		for(int i = HORA_INICIO_JORNADA; i < HORA_FIN_JORNADA; i++) {
			// Ponemos la mitad de los checkboxes en una columna, y la otra mitad en otra
			if(i == HORA_INICIO_JORNADA + (HORA_FIN_JORNADA - HORA_INICIO_JORNADA)/2) {
				posY = INIT_Y + DESPL_X;
				posX = INIT_X;
			}
			// Creamos el checkbox y lo añadimos a una lista para meter una referencia
			chbPeriodos.add(crearPeriodoTrabajoEnComboBox(i, posX, posY));
			posX += DESPL_Y;
		}
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(100, 100));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	public DiaSemana getDiaSemana() {
		return diaSemana;
	}
	
	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}
	
	public int getHorasSeleccionadas() {
		return horasSeleccionadas;
	}
	
	public int getHorasDeseleccionadas() {
		return horasDeseleccionadas;
	}

	public Vector<PeriodoTrabajo> getPeriodosTrabajo () {
		Vector<PeriodoTrabajo> aux = new Vector<PeriodoTrabajo>();
		periodos = new Vector<PeriodoTrabajo>();
		// Obtenemos todos los periodos de trabajo seleccionados en el panel
		for (JCheckBox p : chbPeriodos) {
			if (p.isSelected()) {
				PeriodoTrabajo pt = new PeriodoTrabajo();
				pt.setHoraInicio(getString2IntervaloHora(p.getText())[0]);
				pt.setHoraFinal(getString2IntervaloHora(p.getText())[1]);
				pt.setDia(diaSemana);
				aux.add(pt);
			}
		}
		// Comprimimos las horas consecutivas en un solo periodo
		// NOTA: los periodos están ordenados porque así fue su creación
		if (aux.size() <= 1) {
			// Si se han seleccionado 0 o 1 periodos, no comprimimos nada
			periodos = aux;
		}
		else {
			PeriodoTrabajo a;
			PeriodoTrabajo b;
			int hora_inicial;
			int hora_final;
			for (int i = 0; i < aux.size(); i++) {
				// Tomamos los periodos i e i+1
				a = aux.get(i);
				if (i < (aux.size()-1)) b = aux.get(i+1);
				else b = aux.get(i);
				// Cogemosla hora inicial del periodo
				hora_inicial = a.getHoraInicio();
				// Mientras se trate de periodos consecutivas, vamos avanzando hasta que se rompa la secuencia
				while (a.getHoraFinal() == b.getHoraInicio()) {
					i++;
					a = aux.get(i);
					if (i < (aux.size()-1)) b = aux.get(i+1);
					else b = aux.get(i);
				}
				// Si salimos del bucle, es porque se ha roto la secuencia y tomamos la hora final de la secuencia
				hora_final = a.getHoraFinal();
				periodos.add(new PeriodoTrabajo(hora_inicial, hora_final, a.getDia()));
			}
		}
		return periodos;
	}
	
	private JCheckBox crearPeriodoTrabajoEnComboBox (int hora_inicio, int posX, int posY) {
		chbPeriodoTrabajo = new JCheckBox();
		this.add(chbPeriodoTrabajo, new AnchorConstraint(posX, 0, 0, posY, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
		chbPeriodoTrabajo.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
		chbPeriodoTrabajo.setBounds(posX, posY, WIDTH, HEIGHT);
		String intervaloHora = getIntervaloHora2String(hora_inicio, hora_inicio+1);
		chbPeriodoTrabajo.setText(intervaloHora);
		chbPeriodoTrabajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chbPeriodoTrabajoActionPerformed(evt);
			}
		});
		chbPeriodoTrabajo.setVisible(true);
		
		return chbPeriodoTrabajo;
	}
	
	private String getIntervaloHora2String (int hora_inicio, int hora_fin) {
		return hora_inicio + ":00 - " + hora_fin + ":00";
	}
	
	private int [] getString2IntervaloHora (String hora) {
		// Dado un periodo en formato "hh:00 - hh:00", devuelve un array con dos enteros
		// int[0] contiene la hora de inicio, int[1] contiene la hora de fin
		int [] hora_inicio_fin = new int [2];
		String [] partes = hora.split(" - ");
		String [] hora_inicio = partes[0].split(":");
		hora_inicio_fin[0] = Integer.parseInt(hora_inicio[0]);
		String [] hora_fin = partes[1].split(":");
		hora_inicio_fin[1] = Integer.parseInt(hora_fin[0]);
		return hora_inicio_fin;
	}

	private void chbPeriodoTrabajoActionPerformed(ActionEvent evt) {
		Object [] listeners;
		// Notificamos que se ha seleccionado un checkbox
		if (((JCheckBox)evt.getSource()).isSelected()) {
			listeners = listenerList.getListenerList();
			for(int i = 0; i < listeners.length; i += 2) {
				if(listeners[i] == HoraSeleccionadaListener.class) {
					((HoraSeleccionadaListener)listeners[i + 1]).horaSeleccionada(new EventObject(this));
				}
			}
		}
		else {
			listeners = listenerList.getListenerList();
			for(int i = 0; i < listeners.length; i += 2) {
				if(listeners[i] == HoraNoSeleccionadaListener.class) {
					((HoraNoSeleccionadaListener)listeners[i + 1]).horaNoSeleccionada(new EventObject(this));
				}
			}
		}
	}
	
	public void addHoraSeleccionadaListener(HoraSeleccionadaListener listener) {
		listenerList.add(HoraSeleccionadaListener.class, listener);
	}

	public void removeHoraSeleccionadaListener(HoraSeleccionadaListener listener) {
		listenerList.remove(HoraSeleccionadaListener.class, listener);
	}
	
	public void addHoraNoSeleccionadaListener(HoraNoSeleccionadaListener listener) {
		listenerList.add(HoraNoSeleccionadaListener.class, listener);
	}

	public void removeNoHoraSeleccionadaListener(HoraNoSeleccionadaListener listener) {
		listenerList.remove(HoraNoSeleccionadaListener.class, listener);
	}
	
	public void activarPeriodos(boolean b) {
		// Activa o desactiva todos los checkboxes del panel
		for (JCheckBox cb : chbPeriodos) {
			cb.setEnabled(b);
		}
	}
	
	public void deseleccionarPeriodos() {
		horasDeseleccionadas = 0;
		// Desactivamos solo las horas marcadas
		for (JCheckBox cb : chbPeriodos) {
			if (cb.isSelected()){
				cb.setSelected(false);
				horasDeseleccionadas ++;
			}
		}
	}
	
	public void seleccionarPeriodos (int hora_inicio, int hora_final) {
		// Dado un periodo de trabajo, en el que las horas de inicio y final no tiene por qué ser consecutivas, selecciona los checkboxes correspondientes
		while (hora_inicio < hora_final) {
			for (JCheckBox p : chbPeriodos) {
				if (p.getText().equals(getIntervaloHora2String(hora_inicio, hora_inicio+1))) {
					p.setSelected(true);
					horasSeleccionadas ++;
				}
			}
			hora_inicio++;
		}
	}
	
	public void seleccionarPeriodos (Vector<PeriodoTrabajo> periodos) {
		horasSeleccionadas = 0;
		for (PeriodoTrabajo pt : periodos) {
			seleccionarPeriodos(pt.getHoraInicio(), pt.getHoraFinal());
			
		}
	}

	//$hide<<$
	
}
