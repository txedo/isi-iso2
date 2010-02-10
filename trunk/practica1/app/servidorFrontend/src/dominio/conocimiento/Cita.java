package dominio.conocimiento;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que representa una cita solicitada por un beneficiario para un médico.
 */
public class Cita implements Serializable {
		
	private static final long serialVersionUID = 590630882906518367L;
	
	private static SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
	
	private long id;
	private Date fechaYHora;
	private long duracion;
	private Beneficiario beneficiario;
	private Medico medico;
	
	public Cita() {
	}
	
	public Cita(Date fechaYhora, long duracion, Beneficiario beneficiario, Medico medico) {
		this.fechaYHora = fechaYhora;
		this.duracion = duracion;
		this.beneficiario = beneficiario;
		this.medico = medico;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Date getFechaYHora() {
		return fechaYHora;
	}
	
	public void setFechaYHora(Date fechaYHora) {
		this.fechaYHora = fechaYHora;
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
	
	public static String cadenaHoraCita(Date fechaYHora) {
		return formatoHora.format(fechaYHora);
	}

	public static Date horaCadenaCita(String fechaYHora) throws ParseException {
		return formatoHora.parse(fechaYHora);
	}
	
	public boolean equals(Object o) {
		Cita c;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Cita) {
			c = (Cita)o;
			dev = fechaYHora.equals(c.getFechaYHora()) && duracion == c.getDuracion() && beneficiario.equals(c.getBeneficiario()) && medico.equals(c.getMedico());
		}
		return dev;
	}

}
