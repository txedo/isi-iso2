package dominio.conocimiento;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * Clase que representa un usuario del sistema con rol de m�dico.
 */
public class Medico extends Usuario implements Serializable, Cloneable {

	private static final long serialVersionUID = -8629345838800810415L;

	private Vector<PeriodoTrabajo> calendario;
	private TipoMedico tipoMedico;
	
	public Medico() {
		super();
	}
	
	public Medico(String dni, String login, String password, String nombre, String apellidos, TipoMedico tipo) {
		super(dni, login, password, nombre, apellidos);
		calendario = new Vector<PeriodoTrabajo>();
		tipoMedico = tipo;
	}
	
	public RolesUsuarios getRol() {
		return RolesUsuarios.Medico;
	}
	
	public Vector<PeriodoTrabajo> getCalendario() {
		return calendario;
	}

	public void setCalendario(Vector<PeriodoTrabajo> calendario) {
		this.calendario = calendario;
	}
	
	public TipoMedico getTipoMedico() {
		return tipoMedico;
	}

	public void setTipoMedico(TipoMedico tipoMedico) {
		this.tipoMedico = tipoMedico;
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
	
	public boolean calendariosDiferentes(Medico medico, Date fecha) {
		Vector<PeriodoTrabajo> otroCalendario;
		Calendar calend;
		DiaSemana dia;
		int diaNum;
		boolean diaOk, diferente;
		
		// Obtenemos los per�odos de trabajo del otro m�dico
		otroCalendario = medico.getCalendario();
		
		// Obtenemos el d�a de la semana asociado a la fecha pasada
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
		
		// Comprobamos si los per�odos de trabajo de los dos m�dicos para el
		// d�a indicado son excluyentes, es decir, no se solapa ninguna hora
		if(diaOk) {
			diferente = true;
			for(PeriodoTrabajo otroPeriodo : otroCalendario) {
				for(PeriodoTrabajo periodo : calendario) {
					if(otroPeriodo.getDia() == dia && periodo.getDia() == dia) {
						if(!((periodo.getHoraInicio() < otroPeriodo.getHoraInicio() && periodo.getHoraFinal() < otroPeriodo.getHoraInicio())
						   || (periodo.getHoraInicio() > otroPeriodo.getHoraFinal() && periodo.getHoraFinal() > otroPeriodo.getHoraFinal()))) {
							diferente = false;
						}						
					}
				}
			}
		} else {
			diferente = false;
		}
		
		return diferente;
	}
	
	public Vector<String> horasCitas(DiaSemana dia, int duracionCitas) {
		Vector<String> horas;
		Calendar dias;
		int intervalos;
		int i;
		
		// Generamos una lista con las horas a las que un m�dico podr�a
		// dar cita en un determinado d�a de la semana
		horas = new Vector<String>();
		for(PeriodoTrabajo periodo : calendario) {
			if(periodo.getDia() == dia) {
				// Calculamos cu�ntas citas se pueden dar en este per�odo de trabajo
				intervalos = (((periodo.getHoraFinal() - periodo.getHoraInicio()) * 60) / duracionCitas);
				// Generamos las horas de las citas
				dias = Calendar.getInstance();
				dias.set(Calendar.HOUR_OF_DAY, periodo.getHoraInicio());
				dias.set(Calendar.MINUTE, 0);
				dias.set(Calendar.SECOND, 0);
				for(i = 0; i < intervalos; i++) {
					horas.add(Cita.cadenaHoraCita(dias.getTime()));
					dias.add(Calendar.MINUTE, duracionCitas);
				}
			}
		}
		
		return horas;
	}
	
	public Object clone() {
		Medico m;
		
		m = new Medico(dni, login, password, nombre, apellidos, tipoMedico);
		m.setCentroSalud(centro);
		return m;
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
	
}