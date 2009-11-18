package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistencia.AgenteRemoto;
import persistencia.ComandoSQL;
import persistencia.IConexion;

public class ProxyAgente extends UnicastRemoteObject implements IConexion{

	private AgenteRemoto agente = null;
	
	public ProxyAgente() throws RemoteException, SQLException{
		super();
		agente = AgenteRemoto.getAgente();
		LocateRegistry.createRegistry(1099);
	}

	public void conectar() throws MalformedURLException, RemoteException {
        try {            
            Naming.bind("rmi://127.0.0.1:1099/servidorRespaldo", this);
        }
        catch (AlreadyBoundException ex) {
            Naming.rebind("rmi://127.0.0.1:1099/servidorfrontend", this);
        }
    }
	
	public void desconectar() throws RemoteException, MalformedURLException, NotBoundException {
        Naming.unbind("rmi://127.0.0.1:1099/servidorRespaldo");
    }
	
	@Override
	public void cerrar() throws SQLException {
		agente.cerrar();
		
	}

	@Override
	public void commit() throws SQLException {
		agente.commit();
		
	}

	@Override
	public ResultSet consultar(ComandoSQL comando) throws SQLException {
		ResultSet resultado = agente.consultar(comando);
		return resultado;
	}

	@Override
	public void ejecutar(ComandoSQL comando) throws SQLException {
		agente.ejecutar(comando);
		
	}

	@Override
	public void rollback() throws SQLException {
		agente.rollback();
		
	}

}
