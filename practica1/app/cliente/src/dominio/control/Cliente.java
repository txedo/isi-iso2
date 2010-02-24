package dominio.control;

import java.rmi.RemoteException;
import comunicaciones.ICliente;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.ICodigosOperacionesCliente;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;

/**
 * Clase utilizada por el servidor front-end que implementa las
 * operaciones que se pueden solicitar a los clientes.
 */
public class Cliente implements ICliente {

	private ControladorCliente controlador;
	private String direccionIP;
	private int puerto;
	
	public Cliente() {
	}
	
	public Cliente(String direccionIP, int puerto) {
		this.direccionIP = direccionIP;
		this.puerto = puerto;
	}
	
	public String getDireccionIP() throws RemoteException {
		return direccionIP;
	}
	
	public void setDireccionIP(String direccionIP) throws RemoteException {
		this.direccionIP = direccionIP;
	}
	
	public int getPuerto() throws RemoteException {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	public ControladorCliente getControlador() {
		return controlador;
	}
	
	public void setControlador(ControladorCliente controlador) {
		this.controlador = controlador;
	}
 
	public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
		switch(operacion) {
		case ICodigosOperacionesCliente.INSERTAR:
			if(dato instanceof Cita) {
				controlador.citaRegistrada((Cita)dato);
			} else if(dato instanceof Sustitucion) {
				controlador.sustitucionRegistrada((Sustitucion)dato);
			}
			break;
		case ICodigosOperacionesCliente.MODIFICAR:
			if(dato instanceof Beneficiario) {
				controlador.beneficiarioActualizado((Beneficiario)dato);
			} else if(dato instanceof Usuario) {
				controlador.usuarioActualizado((Usuario)dato);
			}
			break;
		case ICodigosOperacionesCliente.ELIMINAR:
			if(dato instanceof Beneficiario) {
				controlador.beneficiarioEliminado((Beneficiario)dato);
			} else if(dato instanceof Usuario) {
				controlador.usuarioEliminado((Usuario)dato);
			} else if(dato instanceof Cita) {
				controlador.citaAnulada((Cita)dato);
			}
			break;
		}
	}
	
	public void cerrarSesion() throws RemoteException {
		controlador.forzarCierreSesionDuplicada();
	}

	public void servidorInaccesible() throws RemoteException {
		controlador.forzarCierreServidorDesconectado();
	}

}
