package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.control.ControladorCliente;
import excepciones.HorasJornadaIncorrectasException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
public class JPPeriodosTrabajo extends JPBase {
	
	private static final long serialVersionUID = -8549178364759642024L;
	
	//TODO definir las constantes en un interface para poderlas configurar desde la UI
	private static final int MAX_HORAS_JORNADA = 6;
	private static final int HORA_INICIO_JORNADA = 9;
	private static final int HORA_FIN_JORNADA = 21;
	//--
	private static final int WIDTH = 125;
	private static final int HEIGHT = 18;
	private static final int DESPL_X = 156;
	private static final int DESPL_Y = 27;
	private static final int INIT_X = 0;
	private static final int INIT_Y = 12;
	
	private JLabel lblHorasRestantes;
	private JLabel lblHorasJornada;
	private ArrayList<JCheckBox> chbPeriodos = new ArrayList<JCheckBox>();
	private JCheckBox chbPeriodoTrabajo;
	private ArrayList<PeriodoTrabajo> periodos;
	private DiaSemana diaSemana;
	
	public JPPeriodosTrabajo(JFrame frame, ControladorCliente controlador, DiaSemana d) {
		super(frame, controlador);
		diaSemana = d;
		initGUI();
	}
	
	private void initGUI() {
		try {
			int posX = INIT_X;
			int posY = INIT_Y;
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			setPreferredSize(new Dimension(400, 300));
			{
				for (int i = HORA_INICIO_JORNADA; i < HORA_FIN_JORNADA; i++) {
					if (i == HORA_INICIO_JORNADA + (HORA_FIN_JORNADA - HORA_INICIO_JORNADA)/2) {
						posY = INIT_Y + DESPL_X;
						posX = INIT_X;
					}
					chbPeriodos.add(crearPeriodoTrabajoEnComboBox(i, posX, posY));
					posX = posX + DESPL_Y;
				}
			}
			this.add(getLblHorasRestantes(), new AnchorConstraint(641, 378, 708, 31, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			this.add(getLblHorasJornada(), new AnchorConstraint(581, 386, 641, 31, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
			try {
				actualizarHorasRestantes();
			} catch (HorasJornadaIncorrectasException e) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private JCheckBox crearPeriodoTrabajoEnComboBox (int hora_inicio, int posX, int posY) {
		chbPeriodoTrabajo = new JCheckBox();
		this.add(chbPeriodoTrabajo, new AnchorConstraint(posX, 203, 281, posY, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
		chbPeriodoTrabajo.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
		chbPeriodoTrabajo.setBounds(posX, posY, WIDTH, HEIGHT);
		String intervaloHora = getIntervaloHora2String(hora_inicio, hora_inicio+1);
		chbPeriodoTrabajo.setText(intervaloHora);
		
		chbPeriodoTrabajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chbPeriodoTrabajoActionPerformed(evt);
			}
		});
		
		return chbPeriodoTrabajo;
	}
	
	private String getIntervaloHora2String (int hora_inicio, int hora_fin) {
		return hora_inicio + ":00 - " + hora_fin + ":00";
	}
	
	private int [] getString2IntervaloHora (String hora) {
		int [] hora_inicio_fin = new int [2];
		String [] partes = hora.split(" - ");
		String [] hora_inicio = partes[0].split(":");
		hora_inicio_fin[0] = Integer.parseInt(hora_inicio[0]);
		String [] hora_fin = partes[1].split(":");
		hora_inicio_fin[1] = Integer.parseInt(hora_fin[0]);
		return hora_inicio_fin;
	}
	
	private JLabel getLblHorasJornada() {
		if(lblHorasJornada == null) {
			lblHorasJornada = new JLabel();
			lblHorasJornada.setText("Jornada laboral (h): " + MAX_HORAS_JORNADA);
			lblHorasJornada.setPreferredSize(new java.awt.Dimension(142, 18));
		}
		return lblHorasJornada;
	}
	
	private JLabel getLblHorasRestantes() {
		if(lblHorasRestantes == null) {
			lblHorasRestantes = new JLabel();
			lblHorasRestantes.setPreferredSize(new java.awt.Dimension(139, 20));
		}
		return lblHorasRestantes;
	}
	
	private void actualizarHorasRestantes () throws HorasJornadaIncorrectasException {
		int contador = 0;
		for (JCheckBox p : chbPeriodos) {
			if (p.isSelected()) contador++;
		}
		if (contador <= MAX_HORAS_JORNADA) {
			lblHorasRestantes.setText("Horas por seleccionar: " + (MAX_HORAS_JORNADA - contador));
		}
		else {
			throw new HorasJornadaIncorrectasException();
		}
		
	}

	private void chbPeriodoTrabajoActionPerformed(ActionEvent evt) {
		try {
			actualizarHorasRestantes();
		} catch (HorasJornadaIncorrectasException e) {
			((JCheckBox)evt.getSource()).setSelected(false);
			Dialogos.mostrarDialogoError(null, "Error", "Ha sobrepasado el máximo de horas configurado para una jornada laboral.");
		}
	}
	
	public void activarPeriodos(boolean b) {
		for (JCheckBox cb : chbPeriodos) {
			cb.setEnabled(b);
		}
	}

	public boolean esJornadaLaboralCompleta () {
		boolean res = false;
		int contador = 0;
		for (JCheckBox p : chbPeriodos) {
			if (p.isSelected()) contador++;
		}
		if (contador == MAX_HORAS_JORNADA) {
			res = true;
		}
		
		return res;
	}
	
	public void seleccionarPeriodo (int hora_inicio, int hora_final) {
		for (JCheckBox p : chbPeriodos) {
			if (p.getText().equals(getIntervaloHora2String(hora_inicio, hora_final))) {
				p.setSelected(true);
			}
		}
		try {
			actualizarHorasRestantes();
		} catch (HorasJornadaIncorrectasException e) {

		}
	}
	
	public ArrayList<PeriodoTrabajo> getPeriodosTrabajo () {
		periodos = new ArrayList<PeriodoTrabajo>();
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
