package dominio;

public class Volante {

	long id;
	Medico emisor;
	Medico receptor;
	Beneficiario beneficiario;
	
	public Volante () {}
	
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

	

}
