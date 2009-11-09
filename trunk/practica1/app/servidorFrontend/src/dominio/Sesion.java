package dominio;

import java.util.Date;

public class Sesion implements ISesion {
	private long idSesion;
	private Date horaInicio;
	private Usuario usuario;
	
	public Sesion (long idSesion, Usuario usuario) {
		this.idSesion = idSesion;
		this.usuario = usuario;
		//TODO Calcular hora para la hora de inicio
		//this.horaInicio = Calcular la hora
	}
	
	@Override
	public long getId() {
		return idSesion;
	}

	@Override
	public long getRol() {
		return usuario.getRol().ordinal();
	}

	public long getIdSesion() {
		return idSesion;
	}

	public void setIdSesion(long idSesion) {
		this.idSesion = idSesion;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}