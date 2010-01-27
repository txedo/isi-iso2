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
import dominio.conocimiento.ConfiguracionRespaldo;
import presentacion.JFServidorRespaldo;

/**
 * Controlador principal de la funcionalidad del servidor de respaldo.
 */
public class ControladorRespaldo {

	private ConexionBDRespaldo conexionBD;
	private ConexionEstadoRespaldo conexionEstado;
	private JFServidorRespaldo ventana;
	private boolean servidorActivo;

	public ControladorRespaldo() {
		conexionBD = null;
		conexionEstado = null;
		servidorActivo = false;
		ventana = new JFServidorRespaldo(this);
	}
	
	public JFServidorRespaldo getVentana() {
		return ventana;
	}
	
	public boolean getServidorActivo() {
		return servidorActivo;
	}
	
	public void mostrarVentana() {
		ventana.setVisible(true);
	}
	
	public void ocultarVentana() {
		ventana.setVisible(false);
	}
	
	public void iniciarServidorRespaldo(ConfiguracionRespaldo configuracion) throws MalformedURLException, RemoteException, NotBoundException, SQLException, UnknownHostException {
		SimpleDateFormat formatoFecha;
		String ipLocal;

		// Obtenemos la IP de la máquina local
		ipLocal = Inet4Address.getLocalHost().getHostAddress();
		
		// Activamos la clase remota para conectarse con la base de datos
		try {
			conexionBD = ConexionBDRespaldo.getConexion();
			conexionBD.getAgente().setIP(configuracion.getIPBDRespaldo());
			conexionBD.getAgente().setPuerto(configuracion.getPuertoBDRespaldo());
			conexionBD.activar(ipLocal, configuracion.getPuertoRespaldo());
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor de respaldo en la dirección IP " + ipLocal + " y el puerto " + configuracion.getPuertoRespaldo() + ".");
		}

		// Activamos la clase remota para mostrar los mensajes del servidor
		try {
			conexionEstado = ConexionEstadoRespaldo.getConexion();
			conexionEstado.ponerVentana(ventana);
			conexionEstado.activar(ipLocal, configuracion.getPuertoRespaldo() + 1);
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor de respaldo en la dirección IP " + ipLocal + " y el puerto " + configuracion.getPuertoRespaldo() + ".");
		}
		
		// Mostramos un mensaje indicando que el servidor está activo
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ventana.ponerMensaje(formatoFecha.format(new Date()) + ": === Servidor de respaldo iniciado ===");

		// El servidor está activo
		servidorActivo = true;
	}
	
	public void detenerServidorRespaldo(ConfiguracionRespaldo configuracion) throws RemoteException, MalformedURLException, SQLException, UnknownHostException {
		SimpleDateFormat formatoFecha;
		String ipLocal;

		// Obtenemos la IP de la máquina local
		ipLocal = Inet4Address.getLocalHost().getHostAddress();
		
		// Desactivamos las clases remotas del servidor de respaldo
		if(conexionBD != null) {
			conexionBD.desactivar(ipLocal, configuracion.getPuertoRespaldo());
		}
		if(conexionEstado != null) {
			conexionEstado.desactivar(ipLocal, configuracion.getPuertoRespaldo());
		}

		// Mostramos un mensaje indicando que el servidor está inactivo
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ventana.ponerMensaje(formatoFecha.format(new Date()) + ": === Servidor de respaldo detenido ===");
		
		// El servidor no está activo
		servidorActivo = false;
	}
	
}
