package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.AgenteRespaldo;
import persistencia.ComandoSQL;

/**
 * Clase intermedia para conectarse con el agente de base de datos del
 * servidor de respaldo.
 */
public class ConexionBDRespaldo extends UnicastRemoteObject implements IConexion {

	private static final long serialVersionUID = -6996855286696746774L;
	private final String NOMBRE_BASEDATOS = "servidorrespaldo";

	private AgenteRespaldo agente;

	public ConexionBDRespaldo() throws SQLException, RemoteException {
		super();
		// El constructor de 'UnicastRemoteObject' exporta automáticamente
		// este objeto; aquí cancelamos la exportación porque ya llamamos
		// manualmente a 'exportObject' en el método 'conectar'
		unexportObject(this, false);
		LocateRegistry.createRegistry(PUERTO_CONEXION);
		agente = AgenteRespaldo.getAgente();
	}

	public void conectar(String ip) throws MalformedURLException, RemoteException, SQLException {
        exportObject(this, PUERTO_CONEXION);
        try {
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_BASEDATOS, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_BASEDATOS, this);
        }
    }
	
	public void desconectar(String ip) throws RemoteException, MalformedURLException, NotBoundException {		
		unexportObject(this, false);
		Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_BASEDATOS);
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

	public void abrir() throws RemoteException, SQLException {
		agente.abrir();
	}
	
	public void cerrar() throws RemoteException, SQLException {
		agente.cerrar();
	}

}
