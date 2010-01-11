package comunicaciones;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;


public class Cliente extends UnicastRemoteObject implements ICliente {

	private static final long serialVersionUID = -7444411253650233146L;
	private String direccionIP;
	private int puerto = PUERTO_CLIENTE;
	
	public Cliente() throws RemoteException, UnknownHostException {
		super();
		unexportObject(this, false);
		boolean puertoUsado = true;
		do {
			try {
				LocateRegistry.createRegistry(puerto);
				puertoUsado = false;
			}
			catch (ExportException ee) {
				puerto++;
			}
		} while (puertoUsado);
		
		InetAddress ia = Inet4Address.getLocalHost();
		direccionIP = ia.getHostAddress();
	}
	
	public String getDireccionIP () throws RemoteException {
		return direccionIP;
	}
	
	public int getPuerto () throws RemoteException {
		return puerto;
	}
	
    public void conectar() throws RemoteException, MalformedURLException {
        exportObject(this, puerto);
        try {
            Naming.bind("rmi://" + direccionIP + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + direccionIP + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE, this);
        }
    }
    
    public void desconectar() throws RemoteException, MalformedURLException, NotBoundException {
        unexportObject(this, false);
    	Naming.unbind("rmi://" + direccionIP + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE);
    }

	@Override
	public void actualizarVentanas() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
