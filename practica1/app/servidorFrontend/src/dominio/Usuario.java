package dominio;

import java.sql.SQLException;
import persistencia.FPUsuario;

public class Usuario {

	private int dni;
	private String login;
	private String password;
	private String nombre;
	private String apellidos;
	
	public Usuario() {

	}
	
	public static Usuario consultar(int dni) throws SQLException {
		return FPUsuario.consultar(dni);
	}
	
	public static Usuario consultar(String login, String password) throws SQLException {
		return FPUsuario.consultar(login, password);
	}
	
	public void insertar() {
		
	}
	
	public void actualizar() {
		
	}
	
	public void borrar() {
		
	}

	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
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
	
}
