package dominio.conocimiento;

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
	
	public boolean horaEnPeriodo(int horaInicio, int horaFinal) {
		boolean dev;
		
		// Devuelve true si este período se solapa con
		// alguna de las horas del rango indicado
		dev = (horaInicio >= this.horaInicio && horaInicio < this.horaFinal);
		dev = dev || (horaFinal > this.horaInicio && horaFinal <= this.horaFinal);
		dev = dev || (horaInicio <= this.horaInicio && horaFinal >= this.horaFinal);
		return dev;
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
