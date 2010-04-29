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
	
	public Administrador(String nif, String login, String password, String nombre, String apellidos, String correo, String telefono, String movil) {
		super(nif, login, password, nombre, apellidos, correo, telefono, movil);
	}
	
	public Roles getRol() {
		return Roles.Administrador;
	}
	
	public Object clone() {
		Administrador a;
		
		a = new Administrador(nif, login, password, nombre, apellidos, correo, telefono, movil);
		if(centro == null) {
			a.setCentroSalud(null);
		} else {
			a.setCentroSalud((CentroSalud)centro.clone());
		}
		return a;
	}
	
}
