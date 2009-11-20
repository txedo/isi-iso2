package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.ComandoSQL;

/**
 * Proxy utilizado para acceder a una conexión de base de datos remota.
 */
public class ProxyAgenteRemoto implements IConexion {

	private IConexion conexion;
	
	public void conectar(String ip) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/servidorrespaldo";
        conexion = (IConexion)Naming.lookup(url);
	}

	public void commit() throws RemoteException, SQLException {
		conexion.commit();
	}

	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		return conexion.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		conexion.ejecutar(comando);
	}

	public void rollback() throws RemoteException, SQLException {
		conexion.rollback();
	}

	public void abrir() throws RemoteException, SQLException {
		conexion.abrir();
	}

	public void cerrar() throws RemoteException, SQLException {
		conexion.cerrar();
	}

}
