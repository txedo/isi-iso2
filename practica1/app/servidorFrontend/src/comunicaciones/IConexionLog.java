package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz que deben implementar las clases que vayan a mostrar el log
 * generado por el sistema para poder ser utilizadas por el gestor de logs.
 */
public interface IConexionLog extends Remote {

	public final int PUERTO_CONEXION = 1098;
	
	public void ponerMensaje(String mensaje) throws RemoteException;
	
}
