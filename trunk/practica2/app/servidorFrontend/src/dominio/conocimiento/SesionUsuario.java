package dominio.conocimiento;

/**
 * Clase que representa una sesión iniciada por un usuario del sistema
 * en el servidor front-end.
 */
public class SesionUsuario extends Sesion {

	private static final long serialVersionUID = 7682926390745702387L;

	private Usuario usuario;
	
	public SesionUsuario(long idSesion, Usuario usuario) {
		super(idSesion);
		this.usuario = usuario;
	}

	public long getRol() {
		return usuario.getRol().ordinal();
	}
	
	public String getNombre() {
		return usuario.getLogin();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean equals(Object o) {
		SesionUsuario s;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof SesionUsuario) {
			s = (SesionUsuario)o;
			dev = super.equals(s) && usuario.equals(s.getUsuario());
		}
		return dev;
	}
	
}
