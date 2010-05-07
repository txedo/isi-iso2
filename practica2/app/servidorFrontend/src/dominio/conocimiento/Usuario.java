package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase abstracta que representa un usuario del sistema.
 */
public abstract class Usuario implements Serializable, Cloneable {

	protected String nif;
	protected String login;
	protected String password;
	protected String nombre;
	protected String apellidos;
	protected String correo;
	protected String telefono;
	protected String movil;
	protected CentroSalud centroSalud;
	
	public Usuario() {
		nif = "";
		login = "";
		password = "";
		nombre = "";
		apellidos = "";
		correo = "";
		telefono = "";
		movil = "";
		centroSalud = null;
	}
	
	public Usuario(String nif, String login, String password, String nombre, String apellidos, String correo, String telefono, String movil) {
		this.nif = nif;
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.correo = correo;
		this.telefono = telefono;
		this.movil = movil;
		centroSalud = null;
	}

	public abstract Roles getRol();
	
	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
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
		return centroSalud;
	}
	
	public void setCentroSalud(CentroSalud centro) {
		this.centroSalud = centro;
	}

	public abstract Object clone();
	
	public boolean equals(Object o) {
		Usuario u;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Usuario) {
			u = (Usuario)o;
			if(getRol() == u.getRol()) {
				dev = getNif().equals(u.getNif()) && getLogin().equals(u.getLogin())
				    && getPassword().equals(u.getPassword()) && getNombre().equals(u.getNombre())
				    && getApellidos().equals(u.getApellidos()) && getCorreo().equals(u.getCorreo())
				    && getTelefono().equals(u.getTelefono()) && getMovil().equals(u.getMovil());
				if(getCentroSalud() == null) {
					dev = dev && (u.getCentroSalud() == null);
				} else {
					dev = dev && getCentroSalud().equals(u.getCentroSalud());
				}
			}
		}
		return dev;
	}

	public String toString() {
		return getRol().toString() + ": " + getNif() + ", " + getLogin() + ", " + getPassword()
		       + ", " + getNombre() + ", " + getApellidos() + ", " + getCorreo() + ", "
		       + getTelefono() + ", " + getMovil()
		       + ", C: " + (getCentroSalud() == null ? "(ninguno)" : getCentroSalud().getNombre());
	}
	
}
