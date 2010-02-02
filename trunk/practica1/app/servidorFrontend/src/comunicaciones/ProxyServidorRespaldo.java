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

	private IServidorRespaldo conexionRemota;
	
	public void conectar(String ip, int puerto) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR;
        conexionRemota = (IServidorRespaldo)Naming.lookup(url);
	}

	// Métodos de acceso a la base de datos
	
	public void abrir() throws RemoteException, SQLException {
		conexionRemota.abrir();
	}

	public void cerrar() throws RemoteException, SQLException {
		conexionRemota.cerrar();
	}

	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		return conexionRemota.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		conexionRemota.ejecutar(comando);
	}

	public void commit() throws RemoteException, SQLException {
		conexionRemota.commit();
	}

	public void rollback() throws RemoteException, SQLException {
		conexionRemota.rollback();
	}

	// Métodos de actualización del estado
	
	public void ponerMensaje(String mensaje) throws RemoteException {
		conexionRemota.ponerMensaje(mensaje);
	}
	
	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException {
		conexionRemota.actualizarClientesEscuchando(numeroClientes);
	}

}
