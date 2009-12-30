package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ProxyLogRespaldo implements IConexionLog {

	private IConexionLog conexionRemota;
	
	public void conectar(String ip) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/servidorrespaldo";
        conexionRemota = (IConexionLog)Naming.lookup(url);
	}

	public void ponerMensaje(String mensaje) throws RemoteException {
		conexionRemota.ponerMensaje(mensaje);
	}
	
}
