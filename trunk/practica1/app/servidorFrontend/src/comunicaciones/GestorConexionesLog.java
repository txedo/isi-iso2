package comunicaciones;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import comunicaciones.IConexionLog;

/**
 * Gestor que permite enviar los mensajes generados por el servidor a
 * varios logs (locales o remotos).
 */
public class GestorConexionesLog {

	private static ArrayList<IConexionLog> conexiones = new ArrayList<IConexionLog>();
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static void ponerConexion(IConexionLog conexion) {
		if(!conexiones.contains(conexion)) {
			conexiones.add(conexion);
		}
	}
	
	public static void quitarConexion(IConexionLog conexion) {
		if(conexiones.contains(conexion)) {
			conexiones.remove(conexion);
		}
	}

	public static void quitarConexiones() {
		conexiones.clear();
	}
	
	public static void ponerMensaje(String mensaje) {
		String fecha;
		
		fecha = formatoFecha.format(new Date());
		for(IConexionLog log : conexiones) {
			try {
				log.ponerMensaje(fecha + ": " + mensaje);
			} catch(RemoteException e) {
				// Ignoramos el error
			}
		}
	}
	
	public static void ponerMensaje(String login, String mensaje) {
		String fecha;
		
		fecha = formatoFecha.format(new Date());
		for(IConexionLog log : conexiones) {
			try {
				log.ponerMensaje(fecha + " (" + login + "): " + mensaje);
			} catch(RemoteException e) {
				// Ignoramos el error
			}
		}
	}
	
}
