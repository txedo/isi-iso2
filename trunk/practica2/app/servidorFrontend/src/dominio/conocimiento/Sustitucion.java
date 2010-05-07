package dominio.conocimiento;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import dominio.UtilidadesDominio;

/**
 * Clase que representa el día y la hora en la que un médico va
 * a sustituir a otro.
 */
public class Sustitucion implements Serializable, Cloneable {

	private static final long serialVersionUID = 1215359054386009877L;
	
	private int id;
	private Date dia;
	private int horaInicio;
	private int horaFinal;
	private Medico medico;
	private Medico sustituto;
	
	public Sustitucion() {
		id = -1;
		dia = new Date();
		horaInicio = 0;
		horaFinal = 0;
		medico = null;
		sustituto = null;
	}
	
	public Sustitucion(Date dia, int horaInicio, int horaFinal, Medico medico, Medico sustituto) {
		id = -1;
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFinal = horaFinal;
		this.medico = medico;
		this.sustituto = sustituto;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getDia() {
		return dia;
	}
	
	public void setDia(Date dia) {
		this.dia = dia;
	}
	
	public int getHoraInicio() {
		return horaInicio;
	}
	
	public void setHoraInicio(int horaInicio) {
		this.horaInicio = horaInicio;
	}
	
	public int getHoraFinal() {
		return horaFinal;
	}
	
	public void setHoraFinal(int horaFinal) {
		this.horaFinal = horaFinal;
	}
	
	public Medico getMedico() {
		return medico;
	}
	
	public void setMedico(Medico medico) {
		this.medico = medico;
	}
	
	public Medico getSustituto() {
		return sustituto;
	}
	
	public void setSustituto(Medico sustituto) {
		this.sustituto = sustituto;
	}
	
	public boolean horaEnSustitucion(int horaInicio, int horaFinal) {
		boolean dev;
		
		// Devuelve true si las horas de esta sustitución se solapan
		// con alguna de las horas del rango indicado
		dev = (horaInicio >= getHoraInicio() && horaInicio < getHoraFinal());
		dev = dev || (horaFinal > getHoraInicio() && horaFinal <= getHoraFinal());
		dev = dev || (horaInicio <= getHoraInicio() && horaFinal >= getHoraFinal());
		return dev;
	}
	
	public boolean horaEnSustitucion(Date hora) {
		Calendar calend;
		boolean dev;
		int horaD;

		// Devuelve true si la hora está contenida en las horas de la sustitución
		calend = Calendar.getInstance();
		calend.setTime(hora);
		horaD = calend.get(Calendar.HOUR_OF_DAY);
		dev = (horaD >= getHoraInicio() && horaD < getHoraFinal());
		return dev;
	}
	
	public Object clone() {
		Sustitucion s;
		
		s = new Sustitucion((Date)getDia().clone(), getHoraInicio(), getHoraFinal(), (Medico)getMedico().clone(), (Medico)getSustituto().clone());
		s.setId(getId());
		return s;
	}
	
	public boolean equals(Object o) {
		Sustitucion s;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Sustitucion) {
			s = (Sustitucion)o;
			dev = UtilidadesDominio.fechaIgual(getDia(), s.getDia(), false)
			    && getMedico().equals(s.getMedico()) && getSustituto().equals(s.getSustituto());
		}
		return dev;
	}
	
}
