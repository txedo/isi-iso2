package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Interfaz que deben implementar las clases que vayan a mostrar o
 * procesar los mensajes de estado generados por el sistema.
 */
public interface IConexionLog extends Remote {

	public void ponerMensaje(String tipoMensaje, String mensaje) throws RemoteException, SQLException;
	
	public void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws RemoteException, SQLException;
	
	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException, SQLException;
	
}
