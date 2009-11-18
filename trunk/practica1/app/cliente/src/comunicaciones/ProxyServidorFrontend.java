package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import dominio.ISesion;
import excepciones.UsuarioIncorrectoException;

/**
 * Proxy utilizado para conectarse con el servidor frontend.
 */
public class ProxyServidorFrontend implements IServidorFrontend {
	
	private IServidorFrontend servidor;
	
	public void conectar(String ip) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(PUERTO_SERVIDOR) + "/" + NOMBRE_SERVIDOR;
        servidor = (IServidorFrontend)Naming.lookup(url);
	}
	
	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		return servidor.identificar(login, password);
	}
		
}
