package dominio.conocimiento;

import java.io.Serializable;
import java.util.Date;

import dominio.UtilidadesDominio;

/**
 * Clase que representa un volante que relaciona un beneficiario, un médico
 * emisor y un médico receptor.
 */
public class Volante implements Serializable, Cloneable {

	private static final long serialVersionUID = -8633666128386005254L;
	
	private long id;
	private Medico emisor;
	private Medico receptor;
	private Beneficiario beneficiario;
	private Cita cita;
	private Date fechaCaducidad;
	
	public Volante() {
		id = -1;
		emisor = null;
		receptor = null;
		beneficiario = null;
		cita = null;
		fechaCaducidad = new Date();
	}
	
	public Volante(Medico emisor, Medico receptor, Beneficiario beneficiario, Cita cita, Date fechaCaducidad) {
		id = -1;
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
	
	public Object clone() {
		Volante v;
		
		v = new Volante((Medico)getEmisor().clone(), (Medico)getReceptor().clone(), (Beneficiario)getBeneficiario().clone(), (getCita() == null ? null : (Cita)getCita().clone()), (Date)getFechaCaducidad().clone());
		v.setId(getId());
		return v;
	}
	
	public boolean equals(Object o) {
		Volante v;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Volante) {
			v = (Volante)o;
			dev = getReceptor().equals(v.getReceptor()) && getEmisor().equals(v.getEmisor())
			    && getBeneficiario().equals(v.getBeneficiario())
			    && UtilidadesDominio.fechaIgual(getFechaCaducidad(), v.getFechaCaducidad(), false);
			if(getCita() == null) {
				dev = dev && v.getCita() == null;
			} else {
				dev = dev && v.getCita() != null && getCita().equals(v.getCita());
			}
		}
		return dev;
	}

	public String toString() {
		return "E:" + getEmisor().getNif() + ", R:" + getReceptor().getNif()
		       + ", B:" + getBeneficiario().getNif() + ", "
		       + (getCita() == null ? "(sin cita)" : getCita().getFechaYHora().toString());
	}
	
}
