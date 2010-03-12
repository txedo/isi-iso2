package comunicaciones;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.AgenteFrontend;
import persistencia.ComandoSQL;

/**
 * Clase intermedia para conectarse con el agente de la base de datos
 * principal.
 */
public class ConexionBDFrontend implements IConexionBD {

	private AgenteFrontend agente;
	
	public ConexionBDFrontend() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		agente = AgenteFrontend.getAgente();
	}
	
	public AgenteFrontend getAgente() {
		return agente;
	}
	
	// Métodos del agente
	
	public void abrir() throws RemoteException, SQLException {
		agente.abrir();
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
