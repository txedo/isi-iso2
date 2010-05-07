package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import persistencia.ConsultaHibernate;

/**
 * Interfaz con las operaciones que se pueden solicitar al servidor
 * de respaldo.
 */
public interface IServidorRespaldo extends Remote, IConexionBD, IConexionLog {

	public final String NOMBRE_SERVIDOR = "servidorrespaldo";	
	
	// Métodos de acceso a la base de datos (IConexionBD)
	
	public List<?> consultar(ConsultaHibernate consulta) throws RemoteException, SQLException;

	public void iniciarTransaccion() throws RemoteException, SQLException;
	
	public Object insertar(Object objeto) throws RemoteException, SQLException;

	public void actualizar(Object objeto) throws RemoteException, SQLException;

	public void eliminar(Object objeto) throws RemoteException, SQLException;

	public void borrarCache(Object objeto) throws RemoteException, SQLException;
	
	public void commit() throws RemoteException, SQLException;
	
	public void rollback() throws RemoteException, SQLException;
	
	// Métodos de actualización del estado (IConexionLog)

	public void ponerMensaje(String tipoMensaje, String mensaje) throws RemoteException, SQLException;

	public void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws RemoteException, SQLException;
	
	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException, SQLException;
	
}
