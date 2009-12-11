package dominio;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase que representa una entrada en el log del sistema.
 */
public class EntradaLog {
	
	private String usuario;
	private Timestamp fecha;
	private String accion;
	private String mensaje;
	
	public EntradaLog() {
	}
	
	public EntradaLog(String usuario, Timestamp fecha, String accion, String mensaje) {
		this.usuario = usuario;
		this.fecha = fecha;
		this.accion = accion;
		this.mensaje = mensaje;
	}

	public EntradaLog(String usuario, String accion, String mensaje) {
		this.usuario = usuario;
		this.fecha = new Timestamp((new Date()).getTime());
		this.accion = accion;
		this.mensaje = mensaje;
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
	
	public boolean equals(Object o) {
		EntradaLog e;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof EntradaLog) {
			e = (EntradaLog)o;
			dev = usuario.equals(e.getUsuario()) && fecha.equals(e.getFecha()) && accion.equals(e.getAccion()) && mensaje.equals(e.getMensaje());
		}
		return dev;
	}
	
}
