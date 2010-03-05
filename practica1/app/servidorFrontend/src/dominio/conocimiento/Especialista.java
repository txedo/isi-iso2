package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase de tipo de médico que representa un médico especialista para el
 * que se puede pedir cita teniendo un volante.
 */
public class Especialista extends TipoMedico implements Serializable{

	private static final long serialVersionUID = 8975103923917109469L;
	
	private String especialidad;
	
	public Especialista() {
		super();
		especialidad = "";
	}
	
	public Especialista(String especialidad) {
		super();
		this.especialidad = especialidad;
	}

	public CategoriasMedico getCategoria() {
		return CategoriasMedico.Especialista;
	}
	
	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
	
	public boolean equals(Object o) {
		Especialista e;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Especialista) {
			e = (Especialista)o;
			dev = especialidad.equals(e.getEspecialidad());
		}
		return dev;
	}
	
	public String toString() {
		return super.toString() + " (" + especialidad + ")";
	}
	
}
