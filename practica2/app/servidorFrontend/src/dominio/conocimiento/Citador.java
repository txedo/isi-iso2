package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase que representa un usuario del sistema con rol de citador.
 */
public class Citador extends Usuario implements Serializable, Cloneable {

	private static final long serialVersionUID = -5154437290766625917L;

	public Citador() {
		super();
	}

	public Citador(String nif, String login, String password, String nombre, String apellidos, String correo, String telefono, String movil) {
		super(nif, login, password, nombre, apellidos, correo, telefono, movil);
	}
	
	public RolesUsuario getRol(){
		return RolesUsuario.Citador;
	}

	public Object clone() {
		Citador c;
		
		c = new Citador(nif, login, password, nombre, apellidos, correo, telefono, movil);
		c.setCentroSalud(centro);
		return c;
	}
	
}
