package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.ComandoSQL;

/**
 * Proxy utilizado para conectarse con el servidor de respaldo.
 */
public class ProxyServidorRespaldo implements IServidorRespaldo {

	private IServidorRespaldo servidor;
	
	private String ip;
	private int puerto;
	
	public void conectar(String ip, int puerto) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		this.ip = ip;
		this.puerto = puerto;
		url = "rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR;
        servidor = (IServidorRespaldo)Naming.lookup(url);
	}
	
	private void reconectar() throws RemoteException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR;
        try {
			servidor = (IServidorRespaldo)Naming.lookup(url);
		} catch(NotBoundException e) {
			throw new RemoteException(e.getMessage());
		} catch(MalformedURLException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	// Métodos de acceso a la base de datos
	
	public void abrir() throws RemoteException, SQLException {
		try {
			servidor.abrir();
		} catch(RemoteException e) {
			reconectar();
			servidor.abrir();
		}
	}

	public void cerrar() throws RemoteException, SQLException {
		try {
			servidor.cerrar();
		} catch(RemoteException e) {
			reconectar();
			servidor.cerrar();
		}
	}

	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		ResultSet datos;
		
		try {
			datos = servidor.consultar(comando);
		} catch(RemoteException e) {
			reconectar();
			datos = servidor.consultar(comando);
		}
		return datos;
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		try {
			servidor.ejecutar(comando);
		} catch(RemoteException e) {
			reconectar();
			servidor.ejecutar(comando);
		}
	}

	public void commit() throws RemoteException, SQLException {
		try {
			servidor.commit();
		} catch(RemoteException e) {
			reconectar();
			servidor.commit();
		}
	}

	public void rollback() throws RemoteException, SQLException {
		try {
			servidor.rollback();
		} catch(RemoteException e) {
			reconectar();
			servidor.rollback();
		}
	}

	// Métodos de actualización del estado
	
	public void ponerMensaje(String tipoMensaje, String mensaje) throws RemoteException, SQLException {
		try {
			servidor.ponerMensaje(tipoMensaje, mensaje);
		} catch(RemoteException e) {
			reconectar();
			servidor.ponerMensaje(tipoMensaje, mensaje);
		}
	}

	public void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws RemoteException, SQLException {
		try {
			servidor.ponerMensaje(usuario, tipoMensaje, mensaje);
		} catch(RemoteException e) {
			reconectar();
			servidor.ponerMensaje(usuario, tipoMensaje, mensaje);
		}
	}

	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException, SQLException {
		try {
			servidor.actualizarClientesEscuchando(numeroClientes);
		} catch(RemoteException e) {
			reconectar();
			servidor.actualizarClientesEscuchando(numeroClientes);
		}
	}

}
