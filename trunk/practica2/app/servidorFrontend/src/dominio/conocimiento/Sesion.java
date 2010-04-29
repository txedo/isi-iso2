package dominio.conocimiento;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa una sesión iniciada por un cliente en el servidor
 * front-end.
 */
public abstract class Sesion implements ISesion, Serializable {

	private static final long serialVersionUID = 4617941634511694961L;
	
	private long idSesion;
	private Date horaInicio;

	public Sesion(long idSesion) {
		this.idSesion = idSesion;
		this.horaInicio = new Date();
	}
	
	public long getId() {
		return idSesion;
	}

	public void setId(long idSesion) {
		this.idSesion = idSesion;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public abstract long getRol();

	public abstract String getNombre();

	public boolean equals(Object o) {
		Sesion s;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Sesion) {
			s = (Sesion)o;
			dev = idSesion == s.getId() && getRol() == s.getRol() && horaInicio.equals(s.getHoraInicio());
		}
		return dev;
	}

}
