package comunicaciones;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.AgenteFrontend;
import persistencia.ComandoSQL;
import persistencia.IConexion;

/**
 * Clase intermedia para conectarse con la base de datos del servidor frontend.
 */
public class ConexionBDFrontend implements IConexion {

	private AgenteFrontend agente = null;
	
	public ConexionBDFrontend() throws SQLException {
		agente = AgenteFrontend.getAgente();
	}

	public AgenteFrontend getAgente() {
		return agente;
	}
	
	public void cerrar() throws RemoteException, SQLException {
		agente.cerrar();
	}

	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		return agente.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		agente.ejecutar(comando);
	}

	public void commit() throws RemoteException, SQLException {
		agente.commit();
	}

	public void rollback() throws RemoteException, SQLException {
		agente.rollback();
	}
	
}
