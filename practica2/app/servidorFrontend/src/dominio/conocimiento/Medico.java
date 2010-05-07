package dominio.conocimiento;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Clase que representa un usuario del sistema con rol de médico.
 */
public class Medico extends Usuario implements IMedico, Serializable, Cloneable {

	private static final long serialVersionUID = -8629345838800810415L;

	private Set<PeriodoTrabajo> calendario;
	private Set<TipoMedico> tiposMedico;
	
	public Medico() {
		super();
		calendario = new HashSet<PeriodoTrabajo>();
		tiposMedico = new HashSet<TipoMedico>();
	}
	
	public Medico(String nif, String login, String password, String nombre, String apellidos, String correo, String telefono, String movil, TipoMedico tipo) {
		super(nif, login, password, nombre, apellidos, correo, telefono, movil);
		calendario = new HashSet<PeriodoTrabajo>();
		tiposMedico = new HashSet<TipoMedico>();
		tiposMedico.add(tipo);
	}
	
	public Roles getRol() {
		return Roles.Médico;
	}
	
	public Set<PeriodoTrabajo> getCalendario() {
		return calendario;
	}

	public void setCalendario(Set<PeriodoTrabajo> calendario) {
		this.calendario = calendario;
	}
	
	public Set<TipoMedico> getTiposMedico() {
		return tiposMedico;
	}

	public void setTiposMedico(Set<TipoMedico> tiposMedico) {
		this.tiposMedico = tiposMedico;
	}

	public TipoMedico getTipoMedico() {
		TipoMedico tipo;
		
		if(tiposMedico == null || tiposMedico.size() == 0) {
			tipo = null;
		} else {
			tipo = (TipoMedico)tiposMedico.toArray()[0];
		}
		return tipo;
	}

	public void setTipoMedico(TipoMedico tipoMedico) {
		tiposMedico = new HashSet<TipoMedico>();
		tiposMedico.add(tipoMedico);
	}

	public boolean fechaEnCalendario(Date fecha, long duracion) {
		Calendar calend;
		double horaInicio;
		double horaFinal;
		DiaSemana dia;
		int diaNum, horaNum, minutoNum;
		boolean diaOk, fechaOk;
		
		// Convertimos la fecha y duración pasadas como parámetro
		// en día y horas de inicio y final
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
				// Este día de la semana no es válido
				diaOk = false;
				dia = DiaSemana.Lunes;
				break;
		}
		horaNum = calend.get(Calendar.HOUR_OF_DAY);
		minutoNum = calend.get(Calendar.MINUTE);
		horaInicio = horaNum + (minutoNum / (double)60);
		horaFinal = horaNum + ((minutoNum + duracion) / (double)60);

		// Comprobamos si dentro del calendario de trabajo del médico
		// hay algún período de trabajo que englobe las horas obtenidas 
		fechaOk = false;
		if(diaOk) {
			for(PeriodoTrabajo periodo : getCalendario()) {
				if(periodo.getDia() == dia && horaInicio >= (double)periodo.getHoraInicio() && horaFinal <= (double)periodo.getHoraFinal()) {
					fechaOk = true;
				}
			}
		}
		
		return fechaOk;
	}
	
	public Vector<String> horasCitas(DiaSemana dia, int duracionCitas) {
		Vector<String> horas;
		Calendar dias;
		int intervalos;
		int i;
		
		// Generamos una lista con las horas a las que un médico podría
		// dar cita en un determinado día de la semana
		horas = new Vector<String>();
		for(PeriodoTrabajo periodo : getCalendario()) {
			if(periodo.getDia() == dia) {
				// Calculamos cuántas citas se pueden dar en este período de trabajo
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
	
	public Vector<String> horasCitas(DiaSemana dia, int horaInicio, int horaFinal, int duracionCitas) {
		Vector<String> horas;
		Calendar dias;
		int horaRealInicio, horaRealFinal;
		int intervalos;
		int i;
		
		// Generamos una lista con las horas a las que un médico podría dar
		// cita en un determinado día de la semana y un rango limitado de horas
		horas = new Vector<String>();
		for(PeriodoTrabajo periodo : getCalendario()) {
			if(periodo.getDia() == dia && periodo.horaEnPeriodo(horaInicio, horaFinal)) {
				// Reducimos el período según el rango de horas pasado
				if(periodo.getHoraInicio() < horaInicio) {
					horaRealInicio = horaInicio;
				} else {
					horaRealInicio = periodo.getHoraInicio();
				}
				if(periodo.getHoraFinal() > horaFinal) {
					horaRealFinal = horaFinal;
				} else {
					horaRealFinal = periodo.getHoraFinal();
				}
				// Calculamos cuántas citas se pueden dar en este período de trabajo
				intervalos = (((horaRealFinal - horaRealInicio) * 60) / duracionCitas);
				// Generamos las horas de las citas
				dias = Calendar.getInstance();
				dias.set(Calendar.HOUR_OF_DAY, horaRealInicio);
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
	
	@SuppressWarnings("unchecked")
	public Object clone() {
		Iterator<PeriodoTrabajo> it;
		HashSet<PeriodoTrabajo> periodos;
		Medico m;
		
		m = new Medico(getNif(), getLogin(), getPassword(), getNombre(), getApellidos(), getCorreo(), getTelefono(), getMovil(), (TipoMedico)getTipoMedico().clone());
		m.setCentroSalud(getCentroSalud() == null ? null : (CentroSalud)getCentroSalud().clone());
		periodos = new HashSet<PeriodoTrabajo>();
		it = getCalendario().iterator();
		while(it.hasNext()) {
			periodos.add((PeriodoTrabajo)it.next().clone());			
		}
		m.setCalendario(periodos);
		m.setTipoMedico((TipoMedico)getTipoMedico().clone());
		return m;
	}
	
	public boolean equals(Object o) {
		Iterator<PeriodoTrabajo> it1, it2;
		PeriodoTrabajo periodo;
		Medico m;
		boolean ok, dev;
		
		dev = false;
		if(o != null && o instanceof Medico) {
			m = (Medico)o;
			dev = super.equals(m) && getTipoMedico().equals(m.getTipoMedico());
			// Para que dos médicos sean iguales, deben tener el
			// mismo calendario (aunque los períodos de trabajo
			// no tienen por qué estar en el mismo orden)
			dev = dev && getCalendario().size() == m.getCalendario().size();
			it1 = getCalendario().iterator();
			while(it1.hasNext()) {
				periodo = it1.next();
				it2 = m.getCalendario().iterator();
				ok = false;
				while(it2.hasNext()) {
					if(periodo.equals(it2.next())) {
						ok = true;
					}
				}
				dev = dev && ok;
			}
			it1 = m.getCalendario().iterator();
			while(it1.hasNext()) {
				periodo = it1.next();
				it2 = calendario.iterator();
				ok = false;
				while(it2.hasNext()) {
					if(periodo.equals(it2.next())) {
						ok = true;
					}
				}
				dev = dev && ok;
			}
		}
		return dev;
	}
	
}
