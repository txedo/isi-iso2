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
import java.sql.SQLException;
import java.util.ArrayList;
import presentacion.IVentanaEstado;

/**
 * Clase que exporta el objeto que será utilizado por el servidor
 * front-end para mostrar el estado del servidor en la ventana
 * principal del servidor de respaldo.
 */
public class ConexionEstadoRespaldo extends UnicastRemoteObject implements IConexionEstado {

	private static final long serialVersionUID = 8245499027365521538L;
	private final String NOMBRE_LOG = "servidorrespaldo";
	
	private ArrayList<IVentanaEstado> ventanas;
	
	private static ConexionEstadoRespaldo instancia;
	
	protected ConexionEstadoRespaldo() throws RemoteException {
		super();
		LocateRegistry.createRegistry(PUERTO_CONEXION_ESTADO);
		ventanas = new ArrayList<IVentanaEstado>();
	}

	public static ConexionEstadoRespaldo getConexion() throws RemoteException {
		if(instancia == null) {
			instancia = new ConexionEstadoRespaldo();
		}
		return instancia;
	}
	
	public void activar(String ip) throws MalformedURLException, RemoteException, SQLException {
		// Si el objeto ya estaba exportado, controlamos las
		// excepciones y no las lanzamos hacia arriba
		try {
			exportObject(this, PUERTO_CONEXION_ESTADO);
        } catch(ExportException ex) {
        	if(!ex.getMessage().toLowerCase().equals("object already exported")) {
        		throw ex;
        	}
        }
        try {
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION_ESTADO) + "/" + NOMBRE_LOG, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION_ESTADO) + "/" + NOMBRE_LOG, this);
        }
    }
		
	public void desactivar(String ip) throws RemoteException, MalformedURLException {
		// Si el objeto no estaba exportado, controlamos las
		// excepciones y no las lanzamos hacia arriba
		try {
			unexportObject(this, false);
		} catch(NoSuchObjectException ex) {
		}
		try {
			Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION_ESTADO) + "/" + NOMBRE_LOG);
		} catch(NotBoundException ex) {
		}
	}
	
	public void ponerVentana(IVentanaEstado ventana) {
		ventanas.add(ventana);
	}
	
	public void ponerMensaje(String mensaje) {
		for(IVentanaEstado ventana : ventanas) {
			ventana.ponerMensaje(mensaje);
		}
	}

	public void actualizarClientesEscuchando(int numeroClientes) {
		for(IVentanaEstado ventana : ventanas) {
			ventana.actualizarClientesEscuchando(numeroClientes);
		}
	}
	
}
