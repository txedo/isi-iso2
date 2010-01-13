package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICliente extends Remote {
	public final String NOMBRE_CLIENTE = "cliente";
	public final int PUERTO_CLIENTE = 3995;
	
	public String getDireccionIP () throws RemoteException;
	public int getPuerto() throws RemoteException;
	public void activar() throws MalformedURLException, RemoteException;
    public void desactivar() throws RemoteException, MalformedURLException, NotBoundException;
	public void actualizarVentanas () throws RemoteException;
	
}
