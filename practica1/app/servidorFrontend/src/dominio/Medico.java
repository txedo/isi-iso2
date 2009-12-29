package dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase que representa un usuario del sistema con rol de m�dico.
 */
public class Medico extends Usuario implements Serializable {

	private static final long serialVersionUID = -8629345838800810415L;

	private ArrayList<PeriodoTrabajo> calendario;
	private TipoMedico tipoMedico;
	
	public Medico(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro, TipoMedico tipo) {
		super(dni, login, password, nombre, apellidos, centro);
		calendario = new ArrayList<PeriodoTrabajo>();
		tipoMedico=tipo;
	}

	public Medico() {
		super(); 
	}
	
	public Roles getRol() {
		return Roles.Medico;
	}
	
	public ArrayList<PeriodoTrabajo> getCalendario() {
		return calendario;
	}

	public void setCalendario(ArrayList<PeriodoTrabajo> calendario) {
		this.calendario = calendario;
	}
	
	public boolean fechaEnCalendario(Date fecha, long duracion) {
		Calendar calend;
		double horaInicio;
		double horaFinal;
		DiaSemana dia;
		int diaNum, horaNum, minutoNum;
		boolean diaOk, fechaOk;
		
		// Convertimos la fecha y duraci�n pasadas como par�metro
		// en d�a y horas de inicio y final
		diaOk = true;
		calend = Calendar.getInstance();
		calend.setTime(fecha);
		diaNum = calend.get(Calendar.DAY_OF_WEEK);
		switch(diaNum) {
			case Calendar.MONDAY:
				dia = DiaSemana.Lunes;
				break;
			case Calendar.TUESDAY:
				dia = DiaSemana.Martes;
				break;
			case Calendar.WEDNESDAY:
				dia = DiaSemana.Miercoles;
				break;
			case Calendar.THURSDAY:
				dia = DiaSemana.Jueves;
				break;
			case Calendar.FRIDAY:
				dia = DiaSemana.Viernes;
				break;
			default:
				// Este d�a de la semana no es v�lido
				diaOk = false;
				dia = DiaSemana.Lunes;
				break;
		}
		horaNum = calend.get(Calendar.HOUR_OF_DAY);
		minutoNum = calend.get(Calendar.MINUTE);
		horaInicio = horaNum + (minutoNum / (double)60);
		horaFinal = horaNum + ((minutoNum + duracion) / (double)60);

		// Comprobamos si dentro del calendario de trabajo del m�dico
		// hay alg�n per�odo de trabajo que englobe las horas obtenidas 
		fechaOk = false;
		if(diaOk) {
			for(PeriodoTrabajo periodo : calendario) {
				if(periodo.getDia() == dia && horaInicio >= (double)periodo.getHoraInicio() && horaFinal <= (double)periodo.getHoraFinal()) {
					fechaOk = true;
				}
			}
		}
		
		return fechaOk;
	}
	
	public boolean equals(Object o) {
		Medico m;
		boolean dev;
		int i;
		
		dev = false;
		if(o != null && o instanceof Medico) {
			m = (Medico)o;
			dev = super.equals(m);
			// Para que dos m�dicos sean iguales, deben tener el
			// mismo calendario (aunque los per�odos de trabajo
			// no tienen por qu� estar en el mismo orden)
			dev = dev && calendario.size() == m.getCalendario().size();
			for(i = 0; dev && i < calendario.size(); i++) {
				dev = dev && m.getCalendario().contains(calendario.get(i));
			}
			for(i = 0; dev && i < m.getCalendario().size(); i++) {
				dev = dev && calendario.contains(m.getCalendario().get(i));
			}
		}
		return dev;
	}

	public TipoMedico getTipoMedico() {
		return tipoMedico;
	}

	public void setTipoMedico(TipoMedico tipoMedico) {
		this.tipoMedico = tipoMedico;
	}
	
}
