package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.ComandoSQL;
import persistencia.IConexion;

/**
 * Proxy utilizado para conectarse con un agente remoto.
 */
public class ProxyAgenteRemoto implements IConexion {

	private IConexion conexion;
	
	public void conectar(String ip, String basedatos) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + basedatos;
        conexion = (IConexion)Naming.lookup(url);
	}

	public void cerrar() throws RemoteException, SQLException {
		conexion.cerrar();
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
	
}
