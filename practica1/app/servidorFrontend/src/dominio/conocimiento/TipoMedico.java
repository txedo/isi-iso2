package dominio.conocimiento;

public abstract class TipoMedico {

	public TipoMedico() {
	}
	
	public abstract CategoriasMedico getCategoria();
	
	public String toString() {
		return getCategoria().name();
	}

}
