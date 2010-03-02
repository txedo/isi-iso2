package pruebas;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import comunicaciones.ICliente;

/**
 * Cliente dummy utilizado en las pruebas del servidor front-end.
 */
class ClientePrueba extends UnicastRemoteObject implements ICliente {

	private static final long serialVersionUID = -6461417903923553869L;

	private final int PUERTO_INICIAL_CLIENTE = 3995;

	private int ultimaOperacion;
	private Object ultimoDato;
	private boolean llamadoServidorInaccesible;
	private boolean llamadoCerrarSesion;
	private boolean llamadoCerrarSesionEliminacion;
	
	private boolean registro;
	private int puerto;
	
	public ClientePrueba() throws RemoteException {
		super();
		registro = false;
		llamadoServidorInaccesible = false;
		llamadoCerrarSesion = false;
		llamadoCerrarSesionEliminacion = false;
		ultimaOperacion = -1;
		ultimoDato = null;
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

    public String getDireccionIP() throws RemoteException {
		return IDatosPruebas.IP_ESCUCHA_CLIENTES;
	}

	public int getPuerto() throws RemoteException {
		return puerto;
	}

	public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
		ultimaOperacion = operacion;
		ultimoDato = dato;
	}
	
	public void servidorInaccesible() throws RemoteException {
		llamadoServidorInaccesible = true;
	}
	
	public void cerrarSesion() throws RemoteException {
		llamadoCerrarSesion = true;
	}

	public void cerrarSesionEliminacion() throws RemoteException {
		llamadoCerrarSesionEliminacion = true;
	}

	public int getUltimaOperacion() {
		return ultimaOperacion;
	}

	public Object getUltimoDato() {
		return ultimoDato;
	}

	public boolean isLlamadoServidorInaccesible() {
		return llamadoServidorInaccesible;
	}

	public boolean isLlamadoCerrarSesion() {
		return llamadoCerrarSesion;
	}

	public boolean isLlamadoCerrarSesionEliminacion() {
		return llamadoCerrarSesionEliminacion;
	}
	
}
