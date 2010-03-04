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
	
	public ProxyCliente (ICliente c) {
		this.cliente = c;
	}

	// Métodos del cliente

	public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
		Thread hilo;
		
		// Lanzamos la operación en otro hilo para no detener el servidor
		hilo = new Thread(new HiloActualizarVentanas(cliente, operacion, dato));
		hilo.start();
	}
	
	public void cerrarSesion() throws RemoteException {
		Thread hilo;
		
		// Lanzamos la operación en otro hilo para no detener el servidor
		hilo = new Thread(new HiloCerrarSesion(cliente));
		hilo.start();
	}

	public void cerrarSesionEliminacion() throws RemoteException {
		Thread hilo;
		
		// Lanzamos la operación en otro hilo para no detener el servidor
		hilo = new Thread(new HiloCerrarSesionEliminacion(cliente));
		hilo.start();
	}
	
	public void servidorInaccesible() throws RemoteException {
		Thread hilo;
		
		// Lanzamos la operación en otro hilo para no detener el servidor
		hilo = new Thread(new HiloServidorInaccesible(cliente));
		hilo.start();
	}
	
	/**
	 * Hilo utilizado para lanzar la operación cerrarSesion en un cliente.
	 */
	private class HiloCerrarSesion implements Runnable {
	
		private ICliente cliente;
		
		public HiloCerrarSesion(ICliente cliente) {
			this.cliente = cliente;
		}
		
		public void run() {
			try {
				cliente.cerrarSesion();
			} catch(Exception e) {
				// Aquí no se puede manejar la excepción
			}
		}
		
	}
	
	/**
	 * Hilo utilizado para lanzar la operación cerrarSesionEliminacion en un cliente.
	 */
	private class HiloCerrarSesionEliminacion implements Runnable {
	
		private ICliente cliente;
		
		public HiloCerrarSesionEliminacion(ICliente cliente) {
			this.cliente = cliente;
		}
		
		public void run() {
			try {
				cliente.cerrarSesionEliminacion();
			} catch(Exception e) {
				// Aquí no se puede manejar la excepción
			}
		}
		
	}

	/**
	 * Hilo utilizado para lanzar la operación actualizarVentanas en un cliente.
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
			} catch(Exception e) {
				// Aquí no se puede manejar la excepción
			}
		}
		
	}
	
	/**
	 * Hilo utilizado para lanzar la operación servidorInaccesible en un cliente.
	 */
	private class HiloServidorInaccesible implements Runnable {
	
		private ICliente cliente;
		
		public HiloServidorInaccesible(ICliente cliente) {
			this.cliente = cliente;
		}
		
		public void run() {
			try {
				cliente.servidorInaccesible();
			} catch(Exception e) {
				// Aquí no se puede manejar la excepción
			}
		}
		
	}

}
