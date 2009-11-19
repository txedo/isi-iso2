package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.AgenteRespaldo;
import persistencia.ComandoSQL;
import persistencia.IConexion;

/**
 * Clase intermedia para conectarse con la base de datos del servidor de
 * respaldo.
 */
public class ConexionBDRespaldo extends UnicastRemoteObject implements IConexion {

	private final String NOMBRE_BASEDATOS = "servidorRespaldo";

	private AgenteRespaldo agente = null;

	public ConexionBDRespaldo() throws SQLException, RemoteException {
		super();
		agente = AgenteRespaldo.getAgente();
//		unexportObject(this, false);
		LocateRegistry.createRegistry(PUERTO_CONEXION);
	}

	public void conectar(String ip) throws MalformedURLException, RemoteException {
        try {
//            exportObject(this, PUERTO_CONEXION);
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_BASEDATOS, this);
        } catch(AlreadyBoundException ex) {
//            exportObject(this, PUERTO_CONEXION);
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_BASEDATOS, this);
        }
    }
	
	public void desconectar(String ip) throws RemoteException, MalformedURLException, NotBoundException {		
//		unexportObject(this, false);
		Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_BASEDATOS);
    }

	public AgenteRespaldo getAgente() {
		return agente;
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
