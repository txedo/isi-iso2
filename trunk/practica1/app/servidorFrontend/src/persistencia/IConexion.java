package persistencia;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interfaz de acceso a una base de datos, que es utilizada por el
 * gestor de conexiones.
 */
public interface IConexion extends Remote {

	public final int PUERTO_CONEXION = 1099;
	
	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException;
	
	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException;
	
	public void commit() throws RemoteException, SQLException;
	
	public void rollback() throws RemoteException, SQLException;
	
	public void cerrar() throws RemoteException, SQLException;
	
}
