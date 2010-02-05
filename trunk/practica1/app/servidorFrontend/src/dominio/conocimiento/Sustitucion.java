package dominio.conocimiento;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa el d�a y la hora en la que un m�dico va
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
	}
	
	public Sustitucion(Date dia, int horaInicio, int horaFinal, Medico medico, Medico sustituto) {
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
