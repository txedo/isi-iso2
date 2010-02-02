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

	private static AgenteRespaldo instancia = null;
	private Connection conexion;
	private String ip;
	private int puerto;
	
	protected AgenteRespaldo() {
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(IllegalAccessException e) {
			System.out.println("Error al inicializar la conexión con la base de datos: " + e.getLocalizedMessage());
		} catch(InstantiationException e) {
			System.out.println("Error al inicializar la conexión con la base de datos: " + e.getLocalizedMessage());
        } catch(ClassNotFoundException e) {
        	System.out.println("Error al inicializar la conexión con la base de datos: " + e.getLocalizedMessage());
        }
        // La conexión no se abre de forma predeterminada
        // porque se necesita establecer la IP
	}
	
	public static AgenteRespaldo getAgente() {
		if(instancia == null) {
			instancia = new AgenteRespaldo();
		}
		return instancia;
	}

	public Connection getConexion() {
		return conexion;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}
	
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	public void abrir() throws SQLException {
		String url;
		
		if(conexion == null || conexion.isClosed()) {
			// Indicamos que las modificaciones de la base de datos
			// no se deben aplicar hasta llamar al método 'commit'
			url = "jdbc:mysql://" + ip + ":" + String.valueOf(puerto) + "/bdsscarespaldo?user=isor&password=rosi";
			conexion = DriverManager.getConnection(url);
			conexion.setAutoCommit(false); 
		}
	}
	
	public void cerrar() throws SQLException {
		conexion.close();
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
	
}
