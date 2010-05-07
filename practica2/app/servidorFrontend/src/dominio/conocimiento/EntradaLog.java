package dominio.conocimiento;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase que representa una entrada en el log del sistema.
 */
public class EntradaLog implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 7845723551715284252L;
	
	private int id;
	private String usuario;
	private Timestamp fecha;
	private String accion;
	private String mensaje;
	
	public EntradaLog() {
		id = -1;
		usuario = "";
		fecha = new Timestamp((new Date()).getTime());
		accion = "";
		mensaje = "";
	}
	
	public EntradaLog(String usuario, Timestamp fecha, String accion, String mensaje) {
		id = -1;
		this.usuario = usuario;
		this.fecha = fecha;
		this.accion = accion;
		this.mensaje = mensaje;
	}

	public EntradaLog(String usuario, String accion, String mensaje) {
		id = -1;
		this.usuario = usuario;
		this.fecha = new Timestamp((new Date()).getTime());
		this.accion = accion;
		this.mensaje = mensaje;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public Object clone() {
		EntradaLog e;
		
		e = new EntradaLog(getUsuario(), getAccion(), getMensaje());
		e.setFecha((Timestamp)getFecha().clone());
		e.setId(getId());
		return e;
	}
	
	public boolean equals(Object o) {
		EntradaLog e;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof EntradaLog) {
			e = (EntradaLog)o;
			dev = getFecha().equals(e.getFecha()) && getAccion().equals(e.getAccion())
			    && getMensaje().equals(e.getMensaje());
			if(getUsuario() == null) {
				dev = dev && e.getUsuario() == null;
			} else {
				dev = dev && getUsuario().equals(e.getUsuario());
			}
		}
		return dev;
	}
	
}
