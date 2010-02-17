package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import dominio.conocimiento.ICodigosOperacionesCliente;

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

	public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
		Thread hilo;
		
		tOperacion = operacion;
		tDato = dato;
		// Lanzamos la operación en otro hilo para no detener el servidor
		hilo = new Thread(new Runnable() {
			public void run() {
				try {
					cliente.actualizarVentanas(tOperacion, tDato);
				} catch(RemoteException e) {
					// Ignoramos la excepción
				}
			}
		});
		hilo.start();
	}
	
	public void cerrarSesion() throws RemoteException {
		Thread hilo;
		
		// Lanzamos la operación en otro hilo para no detener el servidor
		hilo = new Thread(new Runnable() {
			public void run() {
				try {
					cliente.cerrarSesion();
				} catch(RemoteException e) {
					// Ignoramos la excepción
				}
			}
		});
		hilo.start();
	}
	
	private int tOperacion;
	private Object tDato;

}
