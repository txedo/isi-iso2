package dominio;

import java.io.Serializable;

/**
 * Clase que representa un período de tiempo en el que un médico puede
 * atender pacientes.
 */
public class PeriodoTrabajo implements Serializable {

	private static final long serialVersionUID = 8623785764161131532L;
	
	private int id;
	private int horaInicio;
	private int horaFinal;
	private DiaSemana dia;
	
	public PeriodoTrabajo() {
	}
	
	public PeriodoTrabajo(int horaInicio, int horaFinal, DiaSemana dia) {
		this.horaInicio = horaInicio;
		this.horaFinal = horaFinal;
		this.dia = dia;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public DiaSemana getDia() {
		return dia;
	}
	
	public void setDia(DiaSemana dia) {
		this.dia = dia;
	}
	
	public boolean equals(Object o) {
		PeriodoTrabajo p;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof PeriodoTrabajo) {
			p = (PeriodoTrabajo)o;
			dev = horaInicio == getHoraInicio() && horaFinal == p.getHoraFinal() && dia.equals(p.getDia());
		}
		return dev;
	}
	
}
