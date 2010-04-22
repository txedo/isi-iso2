package dominio.conocimiento;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase que representa el día y la hora en la que un médico va
 * a sustituir a otro.
 */
public class Sustitucion implements Serializable {

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
		dev = (horaInicio >= this.horaInicio && horaInicio < this.horaFinal);
		dev = dev || (horaFinal > this.horaInicio && horaFinal <= this.horaFinal);
		dev = dev || (horaInicio <= this.horaInicio && horaFinal >= this.horaFinal);
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
		dev = (horaD >= horaInicio && horaD < horaFinal);
		return dev;
	}
	
	public boolean equals(Object o) {
		Sustitucion s;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Sustitucion) {
			s = (Sustitucion)o;
			dev = dia.equals(s.getDia()) && medico.equals(s.getMedico()) && sustituto.equals(s.getSustituto());
		}
		return dev;
	}
	
}
