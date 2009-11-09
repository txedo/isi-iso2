package dominio;

import java.sql.SQLException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;
import persistencia.FPUsuario;

/**
 * Clase abstracta que representa un usuario del sistema.
 */
public abstract class Usuario {

	private String dni;
	private String login;
	private String password;
	private String nombre;
	private String apellidos;
	private CentroSalud centro;
	
	public Usuario() {
	}
	
	public Usuario(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro) {
		this.dni = dni;
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.centro = centro;
	}

	public static Usuario consultar(int dni) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		return FPUsuario.consultar(dni);
	}
	
	public static Usuario consultar(String login, String password) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		return FPUsuario.consultar(login, password);
	}
	
	public void insertar() throws SQLException, CentroSaludIncorrectoException {
		FPUsuario.insertar(this);
	}
	
	public void actualizar() throws SQLException, CentroSaludIncorrectoException {
		FPUsuario.actualizar(this);
	}
	
	public void borrar() throws SQLException, CentroSaludIncorrectoException {
		FPUsuario.eliminar(this);
	}

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
	
}
