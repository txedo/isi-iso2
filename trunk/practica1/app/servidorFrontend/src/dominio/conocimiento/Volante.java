package dominio.conocimiento;

import java.io.Serializable;

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
	
	public Volante() {
	}
	
	public Volante(Medico emisor, Medico receptor, Beneficiario beneficiario, Cita cita) {
		this.emisor = emisor;
		this.receptor = receptor;
		this.beneficiario = beneficiario;
		this.cita = cita;
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

	public boolean equals(Object o) {
		Volante v;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Volante) {
			v = (Volante)o;
			dev = receptor.equals(v.getReceptor()) && emisor.equals(v.getEmisor()) && beneficiario.equals(v.getBeneficiario());
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
