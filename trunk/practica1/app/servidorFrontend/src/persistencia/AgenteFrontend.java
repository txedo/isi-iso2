package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Agente de la base de datos del servidor frontend.
 */
public class AgenteFrontend {

	protected static AgenteFrontend instancia = null;
	protected Connection conexion;
	protected String url = "jdbc:mysql://localhost:3306/bdssca?user=iso&password=osi";
	
	protected AgenteFrontend() throws SQLException {
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(IllegalAccessException e) {
			System.out.println("Error al inicializar la conexión con la base de datos: " + e.getLocalizedMessage());
		} catch(InstantiationException e) {
			System.out.println("Error al inicializar la conexión con la base de datos: " + e.getLocalizedMessage());
        } catch(ClassNotFoundException e) {
        	System.out.println("Error al inicializar la conexión con la base de datos: " + e.getLocalizedMessage());
        }
        // Abrimos la conexión de forma predeterminada
        abrir();
	}
	
	public static AgenteFrontend getAgente() throws SQLException {
		if(instancia == null) {
			instancia = new AgenteFrontend();
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
