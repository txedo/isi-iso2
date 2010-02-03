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
	
	public void conectar(String ip, int puerto) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR;
        servidor = (IServidorRespaldo)Naming.lookup(url);
	}

	// Métodos de acceso a la base de datos
	
	public void abrir() throws RemoteException, SQLException {
		servidor.abrir();
	}

	public void cerrar() throws RemoteException, SQLException {
		servidor.cerrar();
	}

	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		return servidor.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		servidor.ejecutar(comando);
	}

	public void commit() throws RemoteException, SQLException {
		servidor.commit();
	}

	public void rollback() throws RemoteException, SQLException {
		servidor.rollback();
	}

	// Métodos de actualización del estado
	
	public void ponerMensaje(String tipoMensaje, String mensaje) throws RemoteException, SQLException {
		servidor.ponerMensaje(tipoMensaje, mensaje);
	}

	public void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws RemoteException, SQLException {
		servidor.ponerMensaje(usuario, tipoMensaje, mensaje);
	}

	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException, SQLException {
		servidor.actualizarClientesEscuchando(numeroClientes);
	}

}
