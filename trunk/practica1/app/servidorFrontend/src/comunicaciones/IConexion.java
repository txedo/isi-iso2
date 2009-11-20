package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.ComandoSQL;

/**
 * Interfaz que deben implementar las clases que proporcionen acceso a una
 * base de datos para poder ser utilizadas por el gestor de conexiones.
 */
public interface IConexion extends Remote {

	public final int PUERTO_CONEXION = 1099;
	
	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException;
	
	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException;
	
	public void commit() throws RemoteException, SQLException;
	
	public void rollback() throws RemoteException, SQLException;
	
	public void abrir() throws RemoteException, SQLException;
	
	public void cerrar() throws RemoteException, SQLException;
	
}
