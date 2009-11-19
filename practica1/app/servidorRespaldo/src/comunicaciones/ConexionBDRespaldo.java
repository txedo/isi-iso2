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

import persistencia.AgenteRespaldo;
import persistencia.ComandoSQL;
import persistencia.IConexion;

public class ConexionBDRespaldo extends UnicastRemoteObject implements IConexion{

	AgenteRespaldo agente = null;
	
	public ConexionBDRespaldo () throws SQLException, RemoteException {
		super();
		agente = AgenteRespaldo.getAgente();
		LocateRegistry.createRegistry(PUERTO_CONEXION);
	}
	
	public void conectar(String ip) throws MalformedURLException, RemoteException {
        try {            
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/servidorRespaldo", this);
        }
        catch (AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/servidorRespaldo", this);
        }
    }
	
	public void desconectar(String ip) throws RemoteException, MalformedURLException, NotBoundException {		
		Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/servidorRespaldo");
        
    }

	public AgenteRespaldo getAgente() {
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
