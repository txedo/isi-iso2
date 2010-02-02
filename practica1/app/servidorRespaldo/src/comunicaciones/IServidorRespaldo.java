package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.ComandoSQL;

/**
 * Interfaz con las operaciones que se pueden solicitar al servidor
 * de respaldo.
 */
public interface IServidorRespaldo extends Remote, IConexionBD, IConexionEstado {

	public final String NOMBRE_SERVIDOR = "servidorrespaldo";	
	
	// Métodos de acceso a la base de datos (IConexionBD)
	
	public void abrir() throws RemoteException, SQLException;
	
	public void cerrar() throws RemoteException, SQLException;

	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException;
	
	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException;
	
	public void commit() throws RemoteException, SQLException;
	
	public void rollback() throws RemoteException, SQLException;
	
	// Métodos de actualización del estado (IConexionEstado)
	
	public void ponerMensaje(String mensaje) throws RemoteException;
	
	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException;
	
}
