package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase que representa un usuario del sistema con rol de citador.
 */
public class Citador extends Usuario implements Serializable, Cloneable {

	private static final long serialVersionUID = -5154437290766625917L;

	public Citador(String dni, String login, String password, String nombre, String apellidos) {
		super(dni, login, password, nombre, apellidos);
	}

	public Citador(){
		super();
	}
	
	public RolesUsuarios getRol(){
		return RolesUsuarios.Citador;
	}

	public Object clone() {
		Citador c;
		
		c = new Citador(dni, login, password, nombre, apellidos);
		c.setCentroSalud(centro);
		return c;
	}
	
}