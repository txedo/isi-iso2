package dominio;

import java.io.Serializable;

/**
 * Clase que representa un usuario del sistema con rol de citador.
 */
public class Citador extends Usuario implements Serializable {

	private static final long serialVersionUID = -5154437290766625917L;

	public Citador(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro) {
		super(dni, login, password, nombre, apellidos, centro);
	}

	public Citador(){
		super();
	}
	
	public Rol getRol(){
		return Rol.Citador;
	}
	
}
