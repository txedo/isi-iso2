package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Agente de la base de datos local del servidor frontend.
 */
public class AgenteRemoto  {

	protected static AgenteRemoto instancia = null;
	protected Connection conexion;
	protected String url = "jdbc:mysql://localhost:3306/bdsscaRespaldo?user=isoRespaldo&password=Rosi";
	
	protected AgenteRemoto() throws SQLException {
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
		} catch(InstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
		// Indicamos que las modificaciones de la base de datos
		// no se deben aplicar hasta llamar al método 'commit'
		conexion = DriverManager.getConnection(url);
		conexion.setAutoCommit(false);
	}
	
	public static AgenteRemoto getAgente() throws SQLException {
		if(instancia == null) {
			instancia = new AgenteRemoto();
		}
		if(instancia.conexion.isClosed()) {
			// Reabrimos la base de datos
			instancia.conexion = DriverManager.getConnection(instancia.url);
			instancia.conexion.setAutoCommit(false); 
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

	public void cerrar() throws SQLException {
		conexion.close();
	}
	
}
