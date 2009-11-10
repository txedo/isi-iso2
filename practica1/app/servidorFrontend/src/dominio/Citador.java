package dominio;

import java.io.Serializable;

public class Citador extends Usuario implements Serializable {

	public Citador(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro) {
		super(dni, login, password, nombre, apellidos, centro);
	}

	public Citador(){
		super();
	}
	
	public Roles getRol(){
		return Roles.Citador;
	}
}
