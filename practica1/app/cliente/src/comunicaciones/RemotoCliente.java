package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import dominio.control.Cliente;

/**
 * Clase que exporta la instancia que será utilizada por el servidor
 * front-end para ejecutar operaciones en los clientes.
 */
public class RemotoCliente extends UnicastRemoteObject implements ICliente {

	private static final long serialVersionUID = -6461417903923553869L;

	private final int PUERTO_INICIAL_CLIENTE = 3995;

	private ICliente cliente;
	private boolean registro;
	private int puerto;
	
	private static RemotoCliente instancia;
	
	protected RemotoCliente() throws RemoteException {
		super();
		cliente = new Cliente();
		registro = false;
	}
	
	public static RemotoCliente getCliente() throws RemoteException {
		if(instancia == null) {
			instancia = new RemotoCliente();
		}
		return instancia;
	}
	
    public void activar(String ip) throws RemoteException, MalformedURLException {
		boolean puertoUsado;

		// Si el objeto ya estaba exportado, controlamos las
		// excepciones y no las lanzamos hacia arriba
    	try {
    		if(!registro) {
    			// Buscamos un puerto que no esté ya en uso en el equipo
    			puertoUsado = true;
    			puerto = PUERTO_INICIAL_CLIENTE;
    			do {
    				try {
    					LocateRegistry.createRegistry(puerto);
    					puertoUsado = false;
    				} catch(ExportException e) {
    					puerto++;
    				}
    			} while(puertoUsado);
    			registro = true;
    		}
    		exportObject(this, puerto);
        } catch(ExportException ex) {
        	if(!ex.getMessage().toLowerCase().equals("object already exported")) {
        		throw ex;
        	}
        }
        try {
            Naming.bind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE, this);
        }
    }
    
    public void desactivar(String ip) throws RemoteException, MalformedURLException, NotBoundException {
		// Si el objeto no estaba exportado, controlamos las
		// excepciones y no las lanzamos hacia arriba
    	try {
    		unexportObject(this, false);
    	} catch(NoSuchObjectException ex) {
    	}
    	try {
    		Naming.unbind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE);
    	} catch(NotBoundException ex) {
    	}
    }
    
    public ICliente getClienteExportado() {
    	return cliente;
    }
    
    public int getPuertoEscucha() {
    	return puerto;
    }

    // Métodos del cliente

	public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
		cliente.actualizarVentanas(operacion, dato);
	}
	
	public void servidorInaccesible() throws RemoteException {
		cliente.servidorInaccesible();
	}
	
	public void cerrarSesion() throws RemoteException {
		cliente.cerrarSesion();
	}
	
	public void cerrarSesionEliminacion() throws RemoteException {
		cliente.cerrarSesionEliminacion();
	}
    
}
