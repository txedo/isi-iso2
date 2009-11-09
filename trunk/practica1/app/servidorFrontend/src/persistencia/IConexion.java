package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interfaz de acceso a una base de datos, que es utilizada por el
 * gestor de conexiones.
 */
public interface IConexion {

	public ResultSet consultar(ComandoSQL comando) throws SQLException;
	
	public void ejecutar(ComandoSQL comando) throws SQLException;
	
	public void commit() throws SQLException;
	
	public void rollback() throws SQLException;
	
	public void cerrar() throws SQLException;
	
}
