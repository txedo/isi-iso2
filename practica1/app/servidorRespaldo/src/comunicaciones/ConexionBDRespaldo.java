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
import persistencia.AgenteRespaldo;
import persistencia.ComandoSQL;

/**
 * Clase que exporta el objeto que será utilizado por el servidor
 * front-end para acceder al agente de la base de datos de respaldo.
 */
public class ConexionBDRespaldo extends UnicastRemoteObject implements IConexionBD {

	private static final long serialVersionUID = -6996855286696746774L;
	private final String NOMBRE_BASEDATOS = "servidorrespaldo";

	private AgenteRespaldo agente;
	private boolean registro;

	private static ConexionBDRespaldo instancia;
	
	protected ConexionBDRespaldo() throws RemoteException {
		super();
		agente = AgenteRespaldo.getAgente();
		registro = false;
	}
	
	public static ConexionBDRespaldo getConexion() throws RemoteException {
		if(instancia == null) {
			instancia = new ConexionBDRespaldo();
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
            Naming.bind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_BASEDATOS, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_BASEDATOS, this);
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
			Naming.unbind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_BASEDATOS);
		} catch(NotBoundException ex) {
		}
    }
	
	public AgenteRespaldo getAgente() {
		return agente;
	}
	
	// Métodos del agente
	
	public void abrir() throws RemoteException, SQLException {
		agente.abrir();
	}
	
	public void cerrar() throws RemoteException, SQLException {
		agente.cerrar();
	}
	
	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		return agente.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		agente.ejecutar(comando);
	}

	public void commit() throws RemoteException, SQLException {
		agente.commit();
	}

	public void rollback() throws RemoteException, SQLException {
		agente.rollback();
	}

}
