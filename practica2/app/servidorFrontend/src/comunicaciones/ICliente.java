package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz con las operaciones que se pueden solicitar a los clientes, y que
 * deben implementar las aplicaciones que quieran registrarse en el servidor.
 */
public interface ICliente extends Remote {
	
	public final String NOMBRE_CLIENTE = "cliente";
	
	public void actualizarVentanas(int operacion, Object dato) throws RemoteException;
	
	public void servidorInaccesible() throws RemoteException;
	
	public void cerrarSesion() throws RemoteException;
	
	public void cerrarSesionEliminacion() throws RemoteException;
	
}
