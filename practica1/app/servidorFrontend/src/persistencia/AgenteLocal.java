package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Agente de la base de datos local del servidor frontend.
 */
public class AgenteLocal implements IConexion {

	protected static AgenteLocal instancia = null;
	protected Connection conexion;
	protected String url = "jdbc:mysql://localhost:3306/bdssca?user=iso&password=osi";
	
	protected AgenteLocal() throws SQLException {
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
		conexion = DriverManager.getConnection(url);
	}
	
	public static AgenteLocal getAgente() throws SQLException {
		if(instancia == null) {
			instancia = new AgenteLocal();
		}
		if(instancia.conexion.isClosed()) {
			instancia.conexion = DriverManager.getConnection(instancia.url);
			instancia.conexion.setAutoCommit(false); 
		}
		return instancia;
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

	public Connection getConexion() {
		return conexion;
	}

	public void closeDB() throws SQLException{
		conexion.close();
		
	}
	
}
