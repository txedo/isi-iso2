package dominio.conocimiento;

import java.io.Serializable;
import java.util.Date;

import dominio.UtilidadesDominio;

/**
 * Clase que representa un volante que relaciona un beneficiario, un médico
 * emisor y un médico receptor.
 */
public class Volante implements Serializable {

	private static final long serialVersionUID = -8633666128386005254L;
	
	private long id;
	private Medico emisor;
	private Medico receptor;
	private Beneficiario beneficiario;
	private Cita cita;
	private Date fechaCaducidad;
	
	public Volante() {
	}
	
	public Volante(Medico emisor, Medico receptor, Beneficiario beneficiario, Cita cita, Date fechaCaducidad) {
		this.emisor = emisor;
		this.receptor = receptor;
		this.beneficiario = beneficiario;
		this.cita = cita;
		this.fechaCaducidad = fechaCaducidad;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Medico getEmisor() {
		return emisor;
	}

	public void setEmisor(Medico emisor) {
		this.emisor = emisor;
	}

	public Medico getReceptor() {
		return receptor;
	}

	public void setReceptor(Medico receptor) {
		this.receptor = receptor;
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}
	
	public Cita getCita() {
		return cita;
	}

	public void setCita(Cita cita) {
		this.cita = cita;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	
	public boolean equals(Object o) {
		Volante v;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Volante) {
			v = (Volante)o;
			dev = receptor.equals(v.getReceptor()) && emisor.equals(v.getEmisor()) && beneficiario.equals(v.getBeneficiario()) && UtilidadesDominio.fechaIgual(fechaCaducidad, v.getFechaCaducidad(), false);
			if(cita == null) {
				dev = dev && v.getCita() == null;
			} else {
				dev = dev && v.getCita() != null && cita.equals(v.getCita());
			}
		}
		return dev;
	}

	public String toString() {
		return "E:" + emisor.getDni() + ", R:" + receptor.getDni() + ", B:" + beneficiario.getNif() + ", " + (cita == null ? "(sin cita)" : cita.getFechaYHora().toString());
	}
	
}
