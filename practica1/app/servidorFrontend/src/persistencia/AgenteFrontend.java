package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Agente de la base de datos principal.
 */
public class AgenteFrontend {

	private static final String BASEDATOS_NOMBRE = "bdssca";
	private static final String BASEDATOS_USUARIO = "iso";
	private static final String BASEDATOS_CLAVE = "osi";
	
	private static AgenteFrontend instancia = null;
	private Connection conexion;
	private String ip;
	private int puerto;
	
	protected AgenteFrontend() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}
	
	public static AgenteFrontend getAgente() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if(instancia == null) {
			instancia = new AgenteFrontend();
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
			// no se deben aplicar hasta llamar al m�todo 'commit'
			url = "jdbc:mysql://" + ip + ":" + String.valueOf(puerto) + "/" + BASEDATOS_NOMBRE + "?user=" + BASEDATOS_USUARIO + "&password=" + BASEDATOS_CLAVE;
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
