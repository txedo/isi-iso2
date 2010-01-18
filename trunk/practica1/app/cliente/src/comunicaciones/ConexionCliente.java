package comunicaciones;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import dominio.control.Cliente;

/**
 * Clase que exporta la instancia que ser� utilizada por el servidor
 * front-end para ejecutar operaciones en los clientes.
 */
public class ConexionCliente extends UnicastRemoteObject implements ICliente {

	private static final long serialVersionUID = -6461417903923553869L;
	
	private ICliente cliente;
	
	public ConexionCliente() throws RemoteException, UnknownHostException {
		super();
		// El constructor de 'UnicastRemoteObject' exporta autom�ticamente
		// este objeto; aqu� cancelamos la exportaci�n porque ya llamamos
		// manualmente a 'exportObject' en el m�todo 'conectar'
		unexportObject(this, false);

		boolean puertoUsado;
		int puerto;
		String direccionIP;
		
		// Buscamos un puerto que no est� en uso en el equipo
		puertoUsado = true;
		puerto = PUERTO_INICIAL_CLIENTE;
		do {
			try {
				LocateRegistry.createRegistry(puerto);
				puertoUsado = false;
			}
			catch (ExportException ee) {
				puerto++;
			}
		} while(puertoUsado);
		// Creamos el objeto Cliente
		direccionIP = Inet4Address.getLocalHost().getHostAddress();
		cliente = new Cliente(direccionIP, puerto);
	}
	
    public void activar() throws RemoteException, MalformedURLException {
        exportObject(cliente, cliente.getPuerto());
        try {
            Naming.bind("rmi://" + cliente.getDireccionIP() + ":" + String.valueOf(cliente.getPuerto()) + "/" + NOMBRE_CLIENTE, cliente);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + cliente.getDireccionIP() + ":" + String.valueOf(cliente.getPuerto()) + "/" + NOMBRE_CLIENTE, cliente);
        }
    }
    
    public void desactivar() throws RemoteException, MalformedURLException, NotBoundException {
        unexportObject(this, false);
    	Naming.unbind("rmi://" + cliente.getDireccionIP() + ":" + String.valueOf(cliente.getPuerto()) + "/" + NOMBRE_CLIENTE);
    }

    // M�todos del cliente

	public String getDireccionIP() throws RemoteException {
		return cliente.getDireccionIP();
	}

	public int getPuerto() throws RemoteException {
		return cliente.getPuerto();
	}

	public void actualizarVentanas() throws RemoteException {
		cliente.actualizarVentanas();
	}
    
}
