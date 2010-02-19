package dominio.conocimiento;

/**
 * Clase base para los tipos de m�dico.
 */
public abstract class TipoMedico {

	public TipoMedico() {
	}
	
	public abstract CategoriasMedico getCategoria();
	
	public String toString() {
		return getCategoria().name();
	}

}
