package dominio.conocimiento;

/**
 * Clase que representa una sesi�n iniciada por un beneficiario del
 * sistema en el servidor front-end.
 */
public class SesionBeneficiario extends Sesion {

	private static final long serialVersionUID = -3947203248144292723L;
	
	private Beneficiario beneficiario;
	
	public SesionBeneficiario(long idSesion, Beneficiario beneficiario) {
		super(idSesion);
		this.beneficiario = beneficiario;
	}

	public long getRol() {
		return beneficiario.getRol().ordinal();
	}

	public String getNombre() {
		// TODO: Alguna forma de identificar un�vocamente a los beneficiarios
		// (el equivalente al Login de los usuarios)
		return beneficiario.getNif();
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}
	
	public boolean equals(Object o) {
		SesionBeneficiario s;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof SesionUsuario) {
			s = (SesionBeneficiario)o;
			dev = super.equals(s) && beneficiario.equals(s.getBeneficiario());
		}
		return dev;
	}
	
}
