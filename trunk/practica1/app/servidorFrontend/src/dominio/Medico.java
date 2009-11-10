package dominio;

public class Medico extends Usuario {

	public Medico(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro) {
		super(dni, login, password, nombre, apellidos, centro);
	}

	public Medico() { 
		super(); 
	}
	
	public Roles getRol(){
		return Roles.Medico;
	}
	
}
