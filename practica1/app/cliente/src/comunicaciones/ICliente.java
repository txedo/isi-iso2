package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz con las operaciones que se pueden solicitar a los
 * clientes.
 */
public interface ICliente extends Remote {
	
	public final String NOMBRE_CLIENTE = "cliente";
	public final int PUERTO_INICIAL_CLIENTE = 3995;
	
	public String getDireccionIP() throws RemoteException;
	
	public int getPuerto() throws RemoteException;
	
	public void actualizarVentanas() throws RemoteException;
	
}
