package comunicaciones;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import comunicaciones.IConexionLog;

/**
 * Gestor que envía los mensajes generados por el servidor a las clases que
 * los necesiten, ya sea para guardarlos en una base de datos o mostrarlos
 * por pantalla.
 */
public class GestorConexionesLog {

	private static ArrayList<IConexionLog> conexiones = new ArrayList<IConexionLog>();
	
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
	
	public static void ponerMensaje(String tipoMensaje, String mensaje) throws RemoteException, SQLException {
		for(IConexionLog log : conexiones) {
			log.ponerMensaje(tipoMensaje, mensaje);
		}
	}

	public static void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws RemoteException, SQLException {
		for(IConexionLog log : conexiones) {
			log.ponerMensaje(usuario, tipoMensaje, mensaje);
		}
	}

	public static void actualizarClientesEscuchando(int numeroClientes) throws RemoteException, SQLException {
		for(IConexionLog log : conexiones) {
			log.actualizarClientesEscuchando(numeroClientes);
		}
	}
	
}
