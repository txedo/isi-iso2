package dominio.conocimiento;

import java.io.Serializable;


/**
 * Clase abstracta que representa un usuario del sistema.
 */
public abstract class Usuario implements Serializable {

	protected String dni;
	protected String login;
	protected String password;
	protected String nombre;
	protected String apellidos;
	protected CentroSalud centro;
	
	public Usuario() {
	}
	
	public Usuario(String dni, String login, String password, String nombre, String apellidos) {
		this.dni = dni;
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}

	public abstract Roles getRol();
	
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public CentroSalud getCentroSalud() {
		return centro;
	}
	
	public void setCentroSalud(CentroSalud centro) {
		this.centro = centro;
	}

	public boolean equals(Object o) {
		Usuario u;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Usuario) {
			u = (Usuario)o;
			dev = dni.equals(u.getDni()) && login.equals(u.getLogin()) && password.equals(u.getPassword()) && nombre.equals(u.getNombre()) && apellidos.equals(u.getApellidos()) && centro.equals(u.getCentroSalud());
		}
		return dev;
	}
	
}
