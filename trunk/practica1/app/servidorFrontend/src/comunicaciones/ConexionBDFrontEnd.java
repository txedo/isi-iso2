package comunicaciones;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistencia.AgenteFrontend;
import persistencia.ComandoSQL;
import persistencia.IConexion;

public class ConexionBDFrontEnd implements IConexion{

	AgenteFrontend agente = null;
	
	public ConexionBDFrontEnd () throws SQLException {
		agente = AgenteFrontend.getAgente();
	}

	public AgenteFrontend getAgente() {
		return agente;
	}
	
	@Override
	public void cerrar() throws RemoteException, SQLException {
		agente.cerrar();
		
	}

	@Override
	public void commit() throws RemoteException, SQLException {
		agente.commit();
		
	}

	@Override
	public ResultSet consultar(ComandoSQL comando) throws RemoteException,
			SQLException {
		return agente.consultar(comando);
	}

	@Override
	public void ejecutar(ComandoSQL comando) throws RemoteException,
			SQLException {
		agente.ejecutar(comando);
		
	}

	@Override
	public void rollback() throws RemoteException, SQLException {
		agente.rollback();
		
	}
	
	
	
	
}
