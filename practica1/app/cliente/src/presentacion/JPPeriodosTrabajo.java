package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.PeriodoTrabajo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

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
public class JPPeriodosTrabajo extends JPanel implements IConstantes {
	
	private static final long serialVersionUID = -8549178364759642024L;
	
	private static final int WIDTH = 125;
	private static final int HEIGHT = 18;
	private static final int DESPL_X = 156;
	private static final int DESPL_Y = 27;
	private static final int INIT_X = 0;
	private static final int INIT_Y = 0;

	private ArrayList<JCheckBox> chbPeriodos = new ArrayList<JCheckBox>();
	private JCheckBox chbPeriodoTrabajo;
	private Vector<PeriodoTrabajo> periodos;
	private DiaSemana diaSemana;
	
	public JPPeriodosTrabajo(DiaSemana d) {
		super();
		diaSemana = d;
		initGUI();
	}
	
	private void initGUI() {
		try {
			int posX = INIT_X;
			int posY = INIT_Y;
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			{
				// Creamos un checkbox para cada periodo de trabajo y calculamos sus coordenadas con respecto al anterior
				for (int i = HORA_INICIO_JORNADA; i < HORA_FIN_JORNADA; i++) {
					// Ponemos la mitad de los checkboxes en una columna, y la otra mitad en otra
					if (i == HORA_INICIO_JORNADA + (HORA_FIN_JORNADA - HORA_INICIO_JORNADA)/2) {
						posY = INIT_Y + DESPL_X;
						posX = INIT_X;
					}
					// Creamos el checkbox y lo a�adimos a un ArrayList para menter una referencia hacia �l
					chbPeriodos.add(crearPeriodoTrabajoEnComboBox(i, posX, posY));
					posX = posX + DESPL_Y;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	}
	
	public void activarPeriodos(boolean b) {
		// Activa o desactiva todos los checkboxes del panel
		for (JCheckBox cb : chbPeriodos) {
			cb.setEnabled(b);
		}
	}
	
	public void deseleccionarPeriodos() {
		// Activa o desactiva todos los checkboxes del panel
		for (JCheckBox cb : chbPeriodos) {
			cb.setSelected(false);
		}
	}
	
	public void seleccionarPeriodos (int hora_inicio, int hora_final) {
		// Dado un periodo de trabajo, en el que las horas de inicio y final no tiene por qu� ser consecutivas, selecciona los checkboxes correspondientes
		while (hora_inicio < hora_final) {
			for (JCheckBox p : chbPeriodos) {
				if (p.getText().equals(getIntervaloHora2String(hora_inicio, hora_inicio+1))) {
					p.setSelected(true);
					hora_inicio++;
				}
			}
		}
	}
	
	public Vector<PeriodoTrabajo> getPeriodosTrabajo () {
		periodos = new Vector<PeriodoTrabajo>();
		for (JCheckBox p : chbPeriodos) {
			if (p.isSelected()) {
				PeriodoTrabajo pt = new PeriodoTrabajo();
				pt.setHoraInicio(getString2IntervaloHora(p.getText())[0]);
				pt.setHoraFinal(getString2IntervaloHora(p.getText())[1]);
				pt.setDia(diaSemana);
				periodos.add(pt);
			}
		}
		return periodos;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

}