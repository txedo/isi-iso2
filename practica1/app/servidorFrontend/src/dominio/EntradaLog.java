package dominio;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import persistencia.FPEntradaLog;

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
	
	public static ArrayList<EntradaLog> consultarLog () throws SQLException {
		return FPEntradaLog.consultarLog();
	}
	
	public void insertar () throws SQLException {
		FPEntradaLog.insertar(this);
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
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
	
}
