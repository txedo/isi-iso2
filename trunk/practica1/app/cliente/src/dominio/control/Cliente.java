package dominio.control;

import java.rmi.RemoteException;
import comunicaciones.ICliente;

/**
 * Clase utilizada por el servidor front-end que implementa las
 * operaciones que se pueden solicitar a los clientes.
 */
public class Cliente implements ICliente {

	private String direccionIP;
	private int puerto;
	
	public Cliente(String direccionIP, int puerto) {
		this.direccionIP = direccionIP;
		this.puerto = puerto;
	}
	
	public String getDireccionIP() throws RemoteException {
		return direccionIP;
	}
	
	public int getPuerto() throws RemoteException {
		return puerto;
	}

	public void actualizarVentanas() throws RemoteException {
		// TODO Auto-generated method stub
	}

}
