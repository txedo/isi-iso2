package dominio.conocimiento;


public class Especialista extends TipoMedico{

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
