package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Proxy utilizado para acceder a una ventana de estado remota.
 */
public class ProxyEstadoRespaldo implements IConexionEstado {

	private IConexionEstado conexionRemota;
	
	public void conectar(String ip) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION_ESTADO) + "/servidorrespaldo";
        conexionRemota = (IConexionEstado)Naming.lookup(url);
	}

	public void ponerMensaje(String mensaje) throws RemoteException {
		conexionRemota.ponerMensaje(mensaje);
	}

	public void actualizarClientesEscuchando(int numeroClientes)
			throws RemoteException {
		conexionRemota.actualizarClientesEscuchando(numeroClientes);		
	}
	
}
