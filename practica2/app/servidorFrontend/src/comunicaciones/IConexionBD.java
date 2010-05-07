package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import persistencia.ConsultaHibernate;

/**
 * Interfaz que deben implementar las clases que proporcionen acceso a una
 * base de datos para poder ser utilizadas por el gestor de conexiones de
 * bases de datos.
 */
public interface IConexionBD extends Remote {
	
	public List<?> consultar(ConsultaHibernate consulta) throws RemoteException, SQLException;

	public void iniciarTransaccion() throws RemoteException, SQLException;
	
	public Object insertar(Object objeto) throws RemoteException, SQLException;

	public void actualizar(Object objeto) throws RemoteException, SQLException;

	public void eliminar(Object objeto) throws RemoteException, SQLException;

	public void borrarCache(Object objeto) throws RemoteException, SQLException;
	
	public void commit() throws RemoteException, SQLException;
	
	public void rollback() throws RemoteException, SQLException;
		
}
