package dominio.control;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import comunicaciones.RemotoServidorRespaldo;
import dominio.conocimiento.ConfiguracionRespaldo;
import presentacion.JFServidorRespaldo;

/**
 * Controlador principal de la funcionalidad del servidor de respaldo.
 */
public class ControladorRespaldo {

	private RemotoServidorRespaldo remotoServidor;
	private JFServidorRespaldo ventana;
	private boolean servidorActivo;

	public ControladorRespaldo() {
		remotoServidor = null;
		servidorActivo = false;
		ventana = new JFServidorRespaldo(this);
	}
	
	public JFServidorRespaldo getVentana() {
		return ventana;
	}
	
	public boolean isServidorActivo() {
		return servidorActivo;
	}
	
	public void mostrarVentana() {
		ventana.setLocationRelativeTo(null);
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
		
		// Configuramos y activamos la clase remota para
		// acceder al servidor de respaldo
		try {
			ServidorRespaldo.getServidor().getConexionBD().getAgente().setIP(configuracion.getIPBDRespaldo());
			ServidorRespaldo.getServidor().getConexionBD().getAgente().setPuerto(configuracion.getPuertoBDRespaldo());
			ServidorRespaldo.getServidor().getConexionEstado().ponerVentana(ventana);
			remotoServidor = RemotoServidorRespaldo.getServidor();
			remotoServidor.activar(ipLocal, configuracion.getPuertoRespaldo());
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
		if(remotoServidor != null) {
			remotoServidor.desactivar(ipLocal, configuracion.getPuertoRespaldo());
		}

		// Mostramos un mensaje indicando que el servidor está inactivo
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ventana.ponerMensaje(formatoFecha.format(new Date()) + ": === Servidor de respaldo detenido ===");
		
		// El servidor no está activo
		servidorActivo = false;
	}
	
}
