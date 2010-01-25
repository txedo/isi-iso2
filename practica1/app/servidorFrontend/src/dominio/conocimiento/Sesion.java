package dominio.conocimiento;

import java.io.Serializable;
import java.util.Date;


public class Sesion implements ISesion, Serializable {

	private static final long serialVersionUID = 4617941634511694961L;
	
	private long idSesion;
	private Date horaInicio;
	private Usuario usuario;
	private boolean modificada;
	
	public Sesion(long idSesion, Usuario usuario) {
		this.idSesion = idSesion;
		this.usuario = usuario;
		modificada = false;
		//TODO Calcular hora para la hora de inicio
		//this.horaInicio = Calcular la hora
	}
	
	public long getId() {
		return idSesion;
	}

	public long getRol() {
		return usuario.getRol().ordinal();
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
	
	public boolean equals(Object o) {
		Sesion u;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Sesion) {
			u = (Sesion)o;
			dev = idSesion==u.getId() && usuario.equals(u.getUsuario());
		}
		return dev;
	}

	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}

	public boolean isModificada() {
		return modificada;
	}

}
