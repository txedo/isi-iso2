package dominio;

import java.io.Serializable;

/**
 * Clase que representa un usuario del sistema con rol de administrador.
 */
public class Administrador extends Citador implements Serializable {

	private static final long serialVersionUID = -7526652846901308346L;

	public Administrador(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro) {
		super(dni, login, password, nombre, apellidos, centro);
	}
	
	public Administrador() {
		super();
	}
	
	public Rol getRol() {
		return Rol.Administrador;
	}
	
}
