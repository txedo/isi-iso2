package dominio;

import java.io.Serializable;

public class Administrador extends Citador implements Serializable {

	public Administrador(String dni, String login, String password,
			String nombre, String apellidos, CentroSalud centro) {
		super(dni, login, password, nombre, apellidos, centro);
		
	}
	
	public Administrador() {
		super();
	}
	
	public Roles getRol(){
		return Roles.Administrador;
	}
}
