package dominio;

import java.io.Serializable;

/**
 * Clase que representa un usuario del sistema con rol de médico.
 */
public class Medico extends Usuario implements Serializable {

	public Medico(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro) {
		super(dni, login, password, nombre, apellidos, centro);
	}

	public Medico() { 
		super(); 
	}
	
	public Roles getRol() {
		return Roles.Medico;
	}
	
}
