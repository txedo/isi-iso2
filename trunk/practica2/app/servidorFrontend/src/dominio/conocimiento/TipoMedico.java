package dominio.conocimiento;

/**
 * Clase base para los tipos de m�dico.
 */
public abstract class TipoMedico implements Cloneable {

	public TipoMedico() {
	}
	
	public abstract CategoriasMedico getCategoria();
	
	public abstract Object clone();
	
	public String toString() {
		return getCategoria().name();
	}

}
