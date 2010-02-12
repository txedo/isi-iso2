package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Proxy utilizado para conectarse con los clientes.
 */
public class ProxyCliente implements ICliente {

	private ICliente cliente;
	
	public void conectar(String ip, int puerto) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE;
        cliente = (ICliente)Naming.lookup(url);
	}

	// Métodos del cliente

	public String getDireccionIP() throws RemoteException {
		return cliente.getDireccionIP();
	}

	public int getPuerto() throws RemoteException {
		return cliente.getPuerto();
	}

	public void actualizarVentanas() throws RemoteException {
		cliente.actualizarVentanas();
	}
	
	public void cerrarSesion() throws RemoteException {
		Runnable run;
		Thread hilo;
		
		// Lanzamos la operación en otro hilo para no detener el servidor
		run = new Runnable() {
			public void run() {
				try {
					cliente.cerrarSesion();
				} catch(RemoteException e) {
					// Ignoramos la excepción
				}
			}
		};
		hilo = new Thread(run);
		hilo.start();
	}

}
