package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz que deben implementar las clases que vayan a mostrar los
 * mensajes de estado generados por el sistema.
 */
public interface IConexionEstado extends Remote {

	public void ponerMensaje(String mensaje) throws RemoteException;
	
	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException;
	
}
