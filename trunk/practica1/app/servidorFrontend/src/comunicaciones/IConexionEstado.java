package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz que deben implementar las clases que vayan a mostrar los
 * mensajes de estado generados por el sistema.
 */
public interface IConexionEstado extends Remote {

	public final int PUERTO_CONEXION_ESTADO = 1098;
	
	public void ponerMensaje(String mensaje) throws RemoteException;
	
}
