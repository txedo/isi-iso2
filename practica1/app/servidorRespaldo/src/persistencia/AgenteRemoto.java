package persistencia;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Agente de la base de datos local del servidor de respaldo.
 */
public class AgenteRemoto extends UnicastRemoteObject implements IConexion {

	protected static AgenteRemoto instancia = null;
	protected Connection conexion;
	protected String url = "jdbc:mysql://localhost:3306/bdsscarespaldo?user=isor&password=rosi";
	
	protected AgenteRemoto() throws RemoteException, SQLException {
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
	
	public static AgenteRemoto getAgente() throws RemoteException, SQLException {
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
	
	public void conectar(String ip, String basedatos) throws MalformedURLException, RemoteException {
        try {            
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + basedatos, this);
        }
        catch (AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + basedatos, this);
        }
    }
	
	public void desconectar(String ip, String basedatos) throws RemoteException, MalformedURLException, NotBoundException {
        Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + basedatos);
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
