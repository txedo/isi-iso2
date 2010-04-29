package dominio.control;

import java.rmi.RemoteException;
import comunicaciones.ICliente;

/**
 * Clase utilizada por el servidor front-end que implementa las
 * operaciones que se pueden solicitar a los clientes.
 */
public class Cliente implements ICliente {

	private ControladorCliente controlador;
	
	public Cliente() {
	}
	
	public ControladorCliente getControlador() {
		return controlador;
	}

	public void setControlador(ControladorCliente controlador) {
		this.controlador = controlador;
	}
	
	public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
		controlador.actualizarVentanaPrincipal(operacion, dato);
	}
	
	public void cerrarSesion() throws RemoteException {
		controlador.forzarCierreSesionDuplicada();
	}
	
	public void cerrarSesionEliminacion() throws RemoteException {
		controlador.forzarCierreSesionEliminacion();
	}

	public void servidorInaccesible() throws RemoteException {
		controlador.forzarCierreServidorDesconectado();
	}

}
