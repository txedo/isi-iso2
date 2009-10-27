package dominio;

import java.util.Hashtable;

public class GestorSesiones {
	private Hashtable<Long,Sesion> sesiones = new Hashtable<Long,Sesion>();
	
	public static ISesion identificar(String login, String password) {
		//return (ISesion)s
	}
	
	public static boolean permisoEjecucion(long idSesion, Operacion operacion) {
		boolean permitido = false;
		
		return permitido;
	}

	private Hashtable<Long,Sesion> getSesiones() {
		return sesiones;
	}

	private void setSesiones(Hashtable<Long,Sesion> sesiones) {
		this.sesiones = sesiones;
	}
	
}
