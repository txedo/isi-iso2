package comunicaciones;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import dominio.control.Cliente;
import dominio.control.ControladorCliente;

/**
 * Clase que exporta la instancia que será utilizada por el servidor
 * front-end para ejecutar operaciones en los clientes.
 */
public class RemotoCliente extends UnicastRemoteObject implements ICliente {

	private static final long serialVersionUID = -6461417903923553869L;
	
	private ICliente cliente;
	
	private static RemotoCliente instancia;
	
	protected RemotoCliente() throws RemoteException, UnknownHostException {
		super();

		boolean puertoUsado;
		int puerto;
		String direccionIP;
		
		// Buscamos un puerto que no esté en uso en el equipo
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
	
	public static RemotoCliente getCliente() throws RemoteException, UnknownHostException {
		if(instancia == null) {
			instancia = new RemotoCliente();
		}
		return instancia;
	}
	
    public void activar() throws RemoteException, MalformedURLException {
		// Si el objeto ya estaba exportado, controlamos las
		// excepciones y no las lanzamos hacia arriba
    	try {
    		exportObject(cliente, cliente.getPuerto());
        } catch(ExportException ex) {
        	if(!ex.getMessage().toLowerCase().equals("object already exported")) {
        		throw ex;
        	}
        }
        try {
            Naming.bind("rmi://" + cliente.getDireccionIP() + ":" + String.valueOf(cliente.getPuerto()) + "/" + NOMBRE_CLIENTE, cliente);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + cliente.getDireccionIP() + ":" + String.valueOf(cliente.getPuerto()) + "/" + NOMBRE_CLIENTE, cliente);
        }
    }
    
    public void desactivar() throws RemoteException, MalformedURLException, NotBoundException {
		// Si el objeto no estaba exportado, controlamos las
		// excepciones y no las lanzamos hacia arriba
    	try {
    		unexportObject(this, false);
    	} catch(NoSuchObjectException ex) {
    	}
    	try {
    		Naming.unbind("rmi://" + cliente.getDireccionIP() + ":" + String.valueOf(cliente.getPuerto()) + "/" + NOMBRE_CLIENTE);
    	} catch(NotBoundException ex) {
    	}
    }
    
    public void setControlador(ControladorCliente controlador) {
    	((Cliente)cliente).setControlador(controlador);
    }

    // Métodos del cliente

	public String getDireccionIP() throws RemoteException {
		return cliente.getDireccionIP();
	}

	public int getPuerto() throws RemoteException {
		return cliente.getPuerto();
	}

	public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
		cliente.actualizarVentanas(operacion, dato);
	}
	
	public void servidorInaccesible () throws RemoteException {
		cliente.servidorInaccesible();
	}
	
	public void cerrarSesion() throws RemoteException {
		cliente.cerrarSesion();
	}
    
}
