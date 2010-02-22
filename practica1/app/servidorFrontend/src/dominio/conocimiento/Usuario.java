package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase abstracta que representa un usuario del sistema.
 */
public abstract class Usuario implements Serializable, Cloneable {

	protected String dni;
	protected String login;
	protected String password;
	protected String nombre;
	protected String apellidos;
	protected String correo;
	protected String telefono;
	protected String movil;
	protected CentroSalud centro;
	
	public Usuario() {
	}
	
	public Usuario(String dni, String login, String password, String nombre, String apellidos, String correo, String telefono, String movil) {
		this.dni = dni;
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.correo = correo;
		this.telefono = telefono;
		this.movil = movil;
	}

	public abstract RolesUsuarios getRol();
	
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
	
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}
	
	public CentroSalud getCentroSalud() {
		return centro;
	}
	
	public void setCentroSalud(CentroSalud centro) {
		this.centro = centro;
	}

	public abstract Object clone();
	
	public boolean equals(Object o) {
		Usuario u;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Usuario) {
			u = (Usuario)o;
			dev = dni.equals(u.getDni()) && login.equals(u.getLogin()) && password.equals(u.getPassword()) && nombre.equals(u.getNombre()) && apellidos.equals(u.getApellidos()) && correo.equals(u.getCorreo()) && telefono.equals(u.getTelefono()) && movil.equals(u.getMovil());
			if(centro == null) {
				dev = dev & (u.getCentroSalud() == null);
			} else {
				dev = dev & centro.equals(u.getCentroSalud());
			}
		}
		return dev;
	}

	public String toString() {
		return getRol().toString() + ": " + dni + ", " + login + ", " + password + ", " + nombre + ", " + apellidos;
	}
	
}
