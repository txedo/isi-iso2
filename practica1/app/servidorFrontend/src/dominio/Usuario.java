package dominio;

public class Usuario {
	private String dni;
	private String login;
	private String password;
	private String nombre;
	private String apellidos;
	
	public Usuario() {
		
	}
	
	public static Usuario consultar(String dni) {
		//return FPUsuario.blablabla
	}
	
	public static Usuario consultar(String login, String password) {
		//return FPUsuario.blablabla
	}
	
	public void insertar() {
		
	}
	
	public void actualizar() {
		
	}
	
	public void borrar() {
		
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
	
}
