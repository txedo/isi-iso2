package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Agente de la base de datos del servidor de respaldo.
 */
public class AgenteRespaldo {

	protected static AgenteRespaldo instancia = null;
	protected Connection conexion;
	protected String url = "jdbc:mysql://localhost:3306/bdsscarespaldo?user=isor&password=rosi";
	
	protected AgenteRespaldo() throws SQLException {
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(IllegalAccessException e) {
            e.printStackTrace();
		} catch(InstantiationException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Abrimos la conexión de forma predeterminada
        abrir();
	}
	
	public static AgenteRespaldo getAgente() throws SQLException {
		if(instancia == null) {
			instancia = new AgenteRespaldo();
		}
		return instancia;
	}
	
	public Connection getConexion() {
		return conexion;
	}
	
	public ResultSet consultar(ComandoSQL comando) throws SQLException {
		PreparedStatement sentencia;
		ResultSet resultado;

		// Obtenemos y ejecutamos la sentencia en la base de datos del agente
		sentencia = comando.crearStatement(conexion);
		resultado = sentencia.executeQuery();
		
		return resultado;
	}

	public void ejecutar(ComandoSQL comando) throws SQLException {
		PreparedStatement sentencia;
		
		// Obtenemos y ejecutamos la sentencia en la base de datos del agente
		sentencia = comando.crearStatement(conexion);
		sentencia.executeUpdate();
	}
	
	public void commit() throws SQLException {
		conexion.commit();
	}

	public void rollback() throws SQLException {
		conexion.rollback();
	}

	public void abrir() throws SQLException {
		if(conexion == null || conexion.isClosed()) {
			// Indicamos que las modificaciones de la base de datos
			// no se deben aplicar hasta llamar al método 'commit'
			conexion = DriverManager.getConnection(url);
			conexion.setAutoCommit(false); 
		}
	}
	
	public void cerrar() throws SQLException {
		conexion.close();
	}
	
}
