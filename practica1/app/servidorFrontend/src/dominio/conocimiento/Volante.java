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
	
	public Volante() {
	}
	
	public Volante(long id, Medico emisor, Medico receptor, Beneficiario beneficiario) {
		this.id = id;
		this.emisor = emisor;
		this.receptor = receptor;
		this.beneficiario = beneficiario;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean equals(Object o) {
		Volante v;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Volante) {
			v = (Volante)o;
			dev = receptor.equals(v.getReceptor()) && emisor.equals(v.getEmisor()) && beneficiario.equals(v.getBeneficiario());
		}
		return dev;
	}

}
