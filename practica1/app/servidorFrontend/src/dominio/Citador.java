package dominio;

public class Citador extends Usuario {

	public Citador(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro) {
		super(dni, login, password, nombre, apellidos, centro);
	}

	public Citador(){
		super();
	}
}
