package dominio.conocimiento;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dominio.UtilidadesDominio;

/**
 * Clase que representa una cita solicitada por un beneficiario para un médico.
 */
public class Cita implements Serializable, Cloneable {
		
	private static final long serialVersionUID = 590630882906518367L;
	
	private static SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
	
	private long id;
	private Date fechaYHora;
	private long duracion;
	private Beneficiario beneficiario;
	private Medico medico;
	
	public Cita() {
		id = -1;
		fechaYHora = new Date();
		duracion = 0;
		beneficiario = null;
		medico = null;
	}
	
	public Cita(Date fechaYhora, long duracion, Beneficiario beneficiario, Medico medico) {
		id = -1;
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
	
	public boolean citaEnHoras(int horaDesde, int horaHasta) {
		Calendar calend;
		boolean dev;
		int horaD;

		// Devuelve true si la hora de la cita está contenida en
		// el rango de horas pasado como parámtro
		calend = Calendar.getInstance();
		calend.setTime(getFechaYHora());
		horaD = calend.get(Calendar.HOUR_OF_DAY);
		dev = (horaD >= horaDesde && horaD < horaHasta);
		return dev;
	}
	
	public static String cadenaHoraCita(Date fechaYHora) {
		return formatoHora.format(fechaYHora);
	}

	public static String cadenaDiaCita(Date fechaYHora) {
		return formatoDia.format(fechaYHora);
	}

	public static Date horaCadenaCita(String fechaYHora) throws ParseException {
		return formatoHora.parse(fechaYHora);
	}
	
	public Object clone() {
		Cita c;
		
		c = new Cita((Date)getFechaYHora().clone(), getDuracion(), (Beneficiario)getBeneficiario().clone(), (Medico)getMedico().clone());
		c.setId(getId());
		return c;
	}
	
	public boolean equals(Object o) {
		Cita c;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Cita) {
			c = (Cita)o;
			dev = UtilidadesDominio.fechaIgual(getFechaYHora(), c.getFechaYHora(), true)
			    && getDuracion() == c.getDuracion() && getBeneficiario().equals(c.getBeneficiario())
			    && getMedico().equals(c.getMedico());
		}
		return dev;
	}

}
