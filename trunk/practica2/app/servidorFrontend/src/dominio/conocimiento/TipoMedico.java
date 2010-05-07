package dominio.conocimiento;

/**
 * Clase base para los tipos de médico.
 */
public abstract class TipoMedico implements Cloneable {
	
	private int id;
	
	public TipoMedico() {
		id = -1;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public abstract CategoriasMedico getCategoria();
	
	public abstract Object clone();
		
	public String toString() {
		return getCategoria().name();
	}

}
