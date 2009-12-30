package dominio;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa una cita solicitada por un beneficiario para un médico.
 */
public class Cita implements Serializable {
		
	private static final long serialVersionUID = 590630882906518367L;
	
	private Date fechaYhora;
	private long duracion;
	private Beneficiario beneficiario;
	private Medico medico;
	
	public Cita() {
	}
	
	public Cita(Date fechaYhora, long duracion, Beneficiario beneficiario, Medico medico) {
		this.fechaYhora = fechaYhora;
		this.duracion = duracion;
		this.beneficiario = beneficiario;
		this.medico = medico;
	}
	
	public Date getFechaYhora() {
		return fechaYhora;
	}
	
	public void setFechaYhora(Date fechaYhora) {
		this.fechaYhora = fechaYhora;
	}
	
	public long getDuracion() {
		return duracion;
	}
	
	public void setDuracion(long duracion) {
		this.duracion = duracion;
	}
	
	public Beneficiario getBeneficiario() {
		return beneficiario;
	}
	
	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}
	
	public Medico getMedico() {
		return medico;
	}
	
	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public boolean equals(Object o) {
		Cita c;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Cita) {
			c = (Cita)o;
			dev = fechaYhora.equals(c.getFechaYhora()) && duracion == c.getDuracion() && beneficiario.equals(c.getBeneficiario()) && medico.equals(c.getMedico());
		}
		return dev;
	}

}
