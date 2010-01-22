package dominio.control;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import comunicaciones.ConexionBDRespaldo;
import comunicaciones.ConexionEstadoRespaldo;
import comunicaciones.IConexionBD;
import comunicaciones.IConexionEstado;
import presentacion.JFServidorRespaldo;

/**
 * Controlador principal de la funcionalidad del servidor de respaldo.
 */
public class ControladorRespaldo {

	private ConexionBDRespaldo conexionBD;
	private ConexionEstadoRespaldo conexionEstado;
	private JFServidorRespaldo vent;
	private boolean servidorActivo;

	public ControladorRespaldo() {
		conexionBD = null;
		conexionEstado = null;
		servidorActivo = false;
	}
	
	public boolean getServidorActivo() {
		return servidorActivo;
	}
	
	public void mostrarVentana() {
		// Mostramos la ventana principal del servidor
		vent = new JFServidorRespaldo();
		vent.setControlador(this);
		vent.setVisible(true);
	}
	
	public void ocultarVentana() {
		// Ocultamos la ventana principal del servidor
		vent.setVisible(false);
	}
	
	public void iniciarServidorRespaldo(String ipBDRespaldo) throws MalformedURLException, RemoteException, NotBoundException, SQLException, UnknownHostException {
		SimpleDateFormat formatoFecha;
		String ipLocal;

		// Obtenemos la IP de la máquina local
		ipLocal = Inet4Address.getLocalHost().getHostAddress();
		
		// Activamos la clase remota para conectarse con la base de datos
		try {
			conexionBD = ConexionBDRespaldo.getConexion();
			conexionBD.getAgente().setIP(ipBDRespaldo);
			conexionBD.activar(ipLocal);
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor de respaldo en la dirección IP " + ipLocal + ":" + IConexionBD.PUERTO_CONEXION_BD + ".");
		}

		// Activamos la clase remota para mostrar los mensajes del servidor
		try {
			conexionEstado = ConexionEstadoRespaldo.getConexion();
			conexionEstado.ponerVentana(vent);
			conexionEstado.activar(ipLocal);
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor de respaldo en la dirección IP " + ipLocal + ":" + IConexionEstado.PUERTO_CONEXION_ESTADO + ".");
		}
		
		// Mostramos un mensaje indicando que el servidor está activo
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		vent.ponerMensaje(formatoFecha.format(new Date()) + ": === Servidor de respaldo iniciado ===");

		// El servidor está activo
		servidorActivo = true;
	}
	
	public void detenerServidorRespaldo() throws RemoteException, MalformedURLException, SQLException, UnknownHostException {
		SimpleDateFormat formatoFecha;
		String ipLocal;

		// Obtenemos la IP de la máquina local
		ipLocal = Inet4Address.getLocalHost().getHostAddress();
		
		// Desactivamos las clases remotas del servidor de respaldo
		if(conexionBD != null) {
			conexionBD.desactivar(ipLocal);
		}
		if(conexionEstado != null) {
			conexionEstado.desactivar(ipLocal);
		}

		// Mostramos un mensaje indicando que el servidor está inactivo
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		vent.ponerMensaje(formatoFecha.format(new Date()) + ": === Servidor de respaldo detenido ===");
		
		// El servidor no está activo
		servidorActivo = false;
	}
	
}
