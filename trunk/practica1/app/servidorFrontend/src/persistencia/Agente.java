package persistencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Agente implements IConexion {

	protected static Agente instancia = null;
	protected Connection conexion;
	protected String url = "jdbc:mysql://localhost:3306/bdssca?user=iso&password=osi";
	
	protected Agente() throws SQLException {
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
	
	public static Agente getAgente() throws SQLException {
		if(instancia == null) {
			instancia = new Agente();
		}
		if(instancia.conexion.isClosed()) {
			instancia.conexion = DriverManager.getConnection(instancia.url);
		}
		return instancia;
	}
	
	public ResultSet consultar(ComandoSQL sql) throws SQLException {
		PreparedStatement prepared;
		CallableStatement callable;
		ResultSet resultado;
		int i;
		
		resultado = null;
		
		switch(sql.getTipo()) {
		case Sentencia:
			prepared = conexion.prepareStatement(sql.getSentencia());
			for(i = 0; i < sql.getParametros().length; i++) {
				prepared.setObject(i + 1, sql.getParametros()[i]);
			}
			resultado = prepared.executeQuery();
			break;
		case Procedimiento:
			callable = conexion.prepareCall(sql.getSentencia());
			for(i = 0; i < sql.getParametros().length; i++) {
				callable.setObject(i + 1, sql.getParametros()[i]);
			}
			resultado = callable.executeQuery();
			break;
		}
		
		return resultado;
	}

	public void ejecutar(ComandoSQL sql) throws SQLException {
		PreparedStatement prepared;
		CallableStatement callable;
		int i;
		
		switch(sql.getTipo()) {
		case Sentencia:
			prepared = conexion.prepareStatement(sql.getSentencia());
			for(i = 0; i < sql.getParametros().length; i++) {
				prepared.setObject(i + 1, sql.getParametros()[i]);
			}
			prepared.executeUpdate();
			break;
		case Procedimiento:
			callable = conexion.prepareCall(sql.getSentencia());
			for(i = 0; i < sql.getParametros().length; i++) {
				callable.setObject(i + 1, sql.getParametros()[i]);
			}
			callable.executeUpdate();
			break;
		}
	}
	
}
