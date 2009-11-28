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
public class ProxyBDRespaldo implements IConexion {

	private IConexion conexionRemota;
	
	public void conectar(String ip) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/servidorrespaldo";
        conexionRemota = (IConexion)Naming.lookup(url);
	}

	public void commit() throws RemoteException, SQLException {
		conexionRemota.commit();
	}

	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		return conexionRemota.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		conexionRemota.ejecutar(comando);
	}

	public void rollback() throws RemoteException, SQLException {
		conexionRemota.rollback();
	}

	public void abrir() throws RemoteException, SQLException {
		conexionRemota.abrir();
	}

	public void cerrar() throws RemoteException, SQLException {
		conexionRemota.cerrar();
	}

}
