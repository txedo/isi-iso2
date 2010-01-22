package dominio.conocimiento;

import java.io.Serializable;


public class Especialista extends TipoMedico implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8975103923917109469L;
	private String especialidad;
	
	public Especialista () {
		super();
	}
	
	public Especialista (String especialidad) {
		this.especialidad = especialidad;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
}
