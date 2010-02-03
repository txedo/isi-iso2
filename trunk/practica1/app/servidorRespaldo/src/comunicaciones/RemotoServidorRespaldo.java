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
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.ComandoSQL;

/**
 * Clase que exporta la instancia que será utilizada por el servidor
 * front-end para ejecutar operaciones sobre el servidor de respaldo.
 */
public class RemotoServidorRespaldo extends UnicastRemoteObject implements IServidorRespaldo {

	private static final long serialVersionUID = -6996855286696746774L;

	private ConexionBDRespaldo basedatos;
	private ConexionLogVentana estado;
	private boolean registro;

	private static RemotoServidorRespaldo instancia;
	
	protected RemotoServidorRespaldo() throws RemoteException {
		super();
		basedatos = new ConexionBDRespaldo();
		estado = new ConexionLogVentana();
		registro = false;
	}
	
	public static RemotoServidorRespaldo getServidor() throws RemoteException {
		if(instancia == null) {
			instancia = new RemotoServidorRespaldo();
		}
		return instancia;
	}
	
	public void activar(String ip, int puerto) throws MalformedURLException, RemoteException, SQLException {
		// Si el objeto ya estaba exportado, controlamos las
		// excepciones y no las lanzamos hacia arriba
        try {
        	if(!registro) {
        		LocateRegistry.createRegistry(puerto);
        		registro = true;
        	}
        	exportObject(this, puerto);
        } catch(ExportException ex) {
        	if(!ex.getMessage().toLowerCase().equals("object already exported")) {
        		throw ex;
        	}
        }
        try {
            Naming.bind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR, this);
        }
    }
		
	public void desactivar(String ip, int puerto) throws RemoteException, MalformedURLException {
		// Si el objeto no estaba exportado, controlamos las
		// excepciones y no las lanzamos hacia arriba
		try {
			unexportObject(this, false);
		} catch(NoSuchObjectException ex) {
		}
		try {
			Naming.unbind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR);
		} catch(NotBoundException ex) {
		}
    }
	
	public ConexionBDRespaldo getConexionBD() {
		return basedatos;
	}
	
	public ConexionLogVentana getConexionEstado() {
		return estado;
	}
	
	// Métodos de acceso a la base de datos
	
	public void abrir() throws RemoteException, SQLException {
		basedatos.abrir();
	}
	
	public void cerrar() throws RemoteException, SQLException {
		basedatos.cerrar();
	}
	
	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		return basedatos.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		basedatos.ejecutar(comando);
	}

	public void commit() throws RemoteException, SQLException {
		basedatos.commit();
	}

	public void rollback() throws RemoteException, SQLException {
		basedatos.rollback();
	}

	// Métodos del log del servidor

	public void ponerMensaje(String tipoMensaje, String mensaje) throws RemoteException {
		estado.ponerMensaje(tipoMensaje, mensaje);
	}

	public void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws RemoteException {
		estado.ponerMensaje(usuario, tipoMensaje, mensaje);
	}

	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException {
		estado.actualizarClientesEscuchando(numeroClientes);
	}
	
}
