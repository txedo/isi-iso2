package dominio;

import java.sql.Date;

public class Cita {
		
	private Date fechaYhora;
	private long duracion;
	Beneficiario beneficiario;
	Medico medico;
	
	public Cita () { }
	
	public Cita(Date fechaYhora, long duracion, Beneficiario beneficiario,
			Medico medico) {
		super();
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



}
