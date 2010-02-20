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

	// M�todos del cliente

	public String getDireccionIP() throws RemoteException {
		return cliente.getDireccionIP();
	}

	public int getPuerto() throws RemoteException {
		return cliente.getPuerto();
	}

	public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
		Thread hilo;
		
		// Lanzamos la operaci�n en otro hilo para no detener el servidor
		hilo = new Thread(new HiloActualizarVentanas(cliente, operacion, dato));
		hilo.start();
	}
	
	public void cerrarSesion() throws RemoteException {
		Thread hilo;
		
		// Lanzamos la operaci�n en otro hilo para no detener el servidor
		hilo = new Thread(new HiloCerrarSesion(cliente));
		hilo.start();
	}
	
	public void servidorInaccesible() throws RemoteException {
		Thread hilo;
		
		// Lanzamos la operaci�n en otro hilo para no detener el servidor
		hilo = new Thread(new HiloServidorInaccesible(cliente));
		hilo.start();
	}
	
	/**
	 * Hilo utilizado para lanzar la operaci�n cerrarSesion en un cliente.
	 */
	private class HiloCerrarSesion implements Runnable {
	
		private ICliente cliente;
		
		public HiloCerrarSesion(ICliente cliente) {
			this.cliente = cliente;
		}
		
		public void run() {
			try {
				cliente.cerrarSesion();
			} catch(RemoteException e) {
				// Aqu� no se puede manejar la excepci�n
			}
		}
		
	}

	/**
	 * Hilo utilizado para lanzar la operaci�n actualizarVentanas en un cliente.
	 */
	private class HiloActualizarVentanas implements Runnable {
	
		private ICliente cliente;
		private int operacion;
		private Object dato;
		
		public HiloActualizarVentanas(ICliente cliente, int operacion, Object dato) {
			this.cliente = cliente;
			this.operacion = operacion;
			this.dato = dato;
		}
		
		public void run() {
			try {
				cliente.actualizarVentanas(operacion, dato);
			} catch(RemoteException e) {
				// Aqu� no se puede manejar la excepci�n
			}
		}
		
	}
	
	/**
	 * Hilo utilizado para lanzar la operaci�n servidorInaccesible en un cliente.
	 */
	private class HiloServidorInaccesible implements Runnable {
	
		private ICliente cliente;
		
		public HiloServidorInaccesible(ICliente cliente) {
			this.cliente = cliente;
		}
		
		public void run() {
			try {
				cliente.servidorInaccesible();
			} catch(RemoteException e) {
				// Aqu� no se puede manejar la excepci�n
			}
		}
		
	}
}
