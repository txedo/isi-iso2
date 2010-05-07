package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase que representa un período de tiempo en el que un médico puede
 * atender pacientes.
 */
public class PeriodoTrabajo implements Serializable, Cloneable {

	private static final long serialVersionUID = 8623785764161131532L;
	
	private int id;
	private int horaInicio;
	private int horaFinal;
	private int diaNumero;
	
	public PeriodoTrabajo() {
		id = -1;
		horaInicio = 0;
		horaFinal = 0;
		diaNumero = -1;
	}
	
	public PeriodoTrabajo(int horaInicio, int horaFinal, DiaSemana dia) {
		id = -1;
		this.horaInicio = horaInicio;
		this.horaFinal = horaFinal;
		this.diaNumero = dia.ordinal();
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
	
	public int getDiaNumero() {
		return diaNumero;
	}
	
	public void setDiaNumero(int diaNumero) {
		this.diaNumero = diaNumero;
	}
	
	public DiaSemana getDia() {
		return DiaSemana.values()[diaNumero];
	}
	
	public void setDia(DiaSemana dia) {
		this.diaNumero = dia.ordinal();
	}
	
	public int getNumeroHoras() {
		return (getHoraFinal() - getHoraInicio());
	}

	public boolean horaEnPeriodo(int horaInicio, int horaFinal) {
		boolean dev;
		
		// Devuelve true si este período se solapa con
		// alguna de las horas del rango indicado
		dev = (horaInicio >= getHoraInicio() && horaInicio < getHoraFinal());
		dev = dev || (horaFinal > getHoraInicio() && horaFinal <= getHoraFinal());
		dev = dev || (horaInicio <= getHoraInicio() && horaFinal >= getHoraFinal());
		return dev;
	}
	
	public Object clone() {
		PeriodoTrabajo p;
		
		p = new PeriodoTrabajo(getHoraInicio(), getHoraFinal(), getDia());
		p.setId(getId());
		return p;
	}
		
	public boolean equals(Object o) {
		PeriodoTrabajo p;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof PeriodoTrabajo) {
			p = (PeriodoTrabajo)o;
			dev = getHoraInicio() == getHoraInicio() && getHoraFinal() == p.getHoraFinal()
			    && getDiaNumero() == p.getDiaNumero();
		}
		return dev;
	}
	
}
