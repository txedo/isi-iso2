package dominio.control;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import comunicaciones.ConfiguracionRespaldo;
import comunicaciones.RemotoServidorRespaldo;
import comunicaciones.UtilidadesComunicaciones;
import presentacion.JFServidorRespaldo;

/**
 * Controlador principal de la funcionalidad del servidor de respaldo.
 */
public class ControladorRespaldo {

	private RemotoServidorRespaldo remotoServidor;
	private JFServidorRespaldo ventana;
	private String ipServidor;
	private boolean servidorActivo;

	public ControladorRespaldo() {
		remotoServidor = null;
		servidorActivo = false;
		ipServidor = null;
		ventana = new JFServidorRespaldo(this);
	}
	
	public void mostrarVentana() {
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}
	
	public void ocultarVentana() {
		ventana.setVisible(false);
	}
	
	public void iniciarServidorRespaldo(ConfiguracionRespaldo configuracion) throws MalformedURLException, RemoteException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		SimpleDateFormat formatoFecha;

		// Obtenemos la IP de la máquina local
		ipServidor = UtilidadesComunicaciones.obtenerIPHost();
		
		// Indicamos a RMI que debe utilizar la IP obtenida como IP de este host
		// en las comunicaciones remotas; esta instrucción es necesaria porque
		// si el ordenador pertenece a más de una red, puede que RMI tome una IP
		// privada como IP del host y las comunicaciones entrantes no funcionen
		System.setProperty("java.rmi.server.hostname", ipServidor);
		
		// Configuramos y activamos la clase remota para
		// acceder al servidor de respaldo
		try {
			remotoServidor = RemotoServidorRespaldo.getServidor();
			((ServidorRespaldo)remotoServidor.getServidorExportado()).getConexionBD().getAgente().setIP(configuracion.getIPBDRespaldo());
			((ServidorRespaldo)remotoServidor.getServidorExportado()).getConexionBD().getAgente().setPuerto(configuracion.getPuertoBDRespaldo());
			((ServidorRespaldo)remotoServidor.getServidorExportado()).getConexionEstado().quitarVentanas();
			((ServidorRespaldo)remotoServidor.getServidorExportado()).getConexionEstado().ponerVentana(ventana);
			remotoServidor.activar(ipServidor, configuracion.getPuertoRespaldo());
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor de respaldo en la dirección IP " + ipServidor + " y el puerto " + configuracion.getPuertoRespaldo() + ".");
		}

		// Mostramos un mensaje indicando que el servidor está activo
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ventana.ponerMensaje(formatoFecha.format(new Date()) + ": === Servidor de respaldo iniciado ===");

		// El servidor está activo
		servidorActivo = true;
	}
	
	public void detenerServidorRespaldo(ConfiguracionRespaldo configuracion) throws RemoteException, MalformedURLException {
		SimpleDateFormat formatoFecha;
		
		// Al desconectar el servidor ignoramos todos los errores de conexión
		// y SQL para poder desconectar el servidor incluso si ha cambiado
		// la IP de la máquina del servidor de respaldo
		
		// Desactivamos las clases remotas del servidor de respaldo
		if(remotoServidor != null && ipServidor != null) {
			try {
				remotoServidor.desactivar(ipServidor, configuracion.getPuertoRespaldo());
			} catch(RemoteException e) {
			}
		}

		// Mostramos un mensaje indicando que el servidor está inactivo
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ventana.ponerMensaje(formatoFecha.format(new Date()) + ": === Servidor de respaldo detenido ===");
		
		// El servidor no está activo
		servidorActivo = false;
	}

	public JFServidorRespaldo getVentana() {
		return ventana;
	}
	
	public boolean isServidorActivo() {
		return servidorActivo;
	}
	
	public String getIPServidor() {
		return ipServidor;
	}

}
