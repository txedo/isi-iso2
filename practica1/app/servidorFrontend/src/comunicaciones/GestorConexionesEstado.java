package comunicaciones;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import comunicaciones.IConexionEstado;

/**
 * Gestor que permite enviar los mensajes generados por el servidor a
 * varias ventanas (locales o remotas).
 */
public class GestorConexionesEstado {

	private static ArrayList<IConexionEstado> conexiones = new ArrayList<IConexionEstado>();
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static void ponerConexion(IConexionEstado conexion) {
		if(!conexiones.contains(conexion)) {
			conexiones.add(conexion);
		}
	}
	
	public static void quitarConexion(IConexionEstado conexion) {
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
		for(IConexionEstado log : conexiones) {
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
		for(IConexionEstado log : conexiones) {
			try {
				log.ponerMensaje(fecha + " (" + login + "): " + mensaje);
			} catch(RemoteException e) {
				// Ignoramos el error
			}
		}
	}
	
}
