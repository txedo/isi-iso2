package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase que representa un usuario del sistema con rol de administrador.
 */
public class Administrador extends Citador implements Serializable, Cloneable {

	private static final long serialVersionUID = -7526652846901308346L;

	public Administrador() {
		super();
	}
	
	public Administrador(String dni, String login, String password, String nombre, String apellidos, String correo, String telefono, String movil) {
		super(dni, login, password, nombre, apellidos, correo, telefono, movil);
	}
	
	public RolesUsuario getRol() {
		return RolesUsuario.Administrador;
	}
	
	public Object clone() {
		Administrador a;
		
		a = new Administrador(dni, login, password, nombre, apellidos, correo, telefono, movil);
		a.setCentroSalud(centro);
		return a;
	}
	
}
