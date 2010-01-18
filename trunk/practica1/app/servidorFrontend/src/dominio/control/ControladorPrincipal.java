package dominio.control;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import presentacion.JFServidorFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConexionLogFrontend;
import comunicaciones.ConexionServidorFrontEnd;
import comunicaciones.GestorConexionesBD;
import comunicaciones.GestorConexionesLog;
import comunicaciones.ProxyBDRespaldo;
import comunicaciones.ProxyLogRespaldo;

/**
 * Controlador principal de la funcionalidad del servidor frontend.
 */
public class ControladorPrincipal {
	
	private ServidorFrontend servidor;
	private ProxyBDRespaldo proxy;
	private ConexionServidorFrontEnd conexionServidor;
	private ConexionBDFrontend basedatos;
	private JFServidorFrontend vent;

	public ControladorPrincipal() {
		servidor = null;
		conexionServidor = null; 
	}
	
	public void mostrarVentana() {
		// Mostramos la ventana principal del servidor
		vent = new JFServidorFrontend();
		vent.setControladorPresentacion(this);
		vent.setVisible(true);
	}
	
	public void iniciarServidor(String ipBDPrincipal, String ipRespaldo) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException, SQLException {
		ConexionLogFrontend logsFrontend;
		ProxyLogRespaldo logsRespaldo;
		String ipLocal;
		boolean respaldo;

		// Obtenemos la IP de la máquina local
		ipLocal = Inet4Address.getLocalHost().getHostAddress();
		
		// Cerramos las conexiones que pudiera haber abiertas
		// (ignoramos los errores que pudieran producirse)
		try {
			GestorConexionesBD.cerrarConexiones();
		} catch(SQLException e) {
		}
		GestorConexionesBD.quitarConexiones();
		GestorConexionesLog.quitarConexiones();
		
		// Comprobamos si se va a usar el servidor de respaldo
		respaldo = (ipRespaldo != null && !ipRespaldo.equals(""));

		// Creamos una conexión con la base de datos local
		basedatos = new ConexionBDFrontend();
		basedatos.getAgente().setIP(ipBDPrincipal);
		basedatos.abrir();
		GestorConexionesBD.ponerConexion(basedatos);
		// Establecemos conexión con la base de datos del servidor de respaldo
		if(respaldo) {
			proxy = new ProxyBDRespaldo();
			proxy.conectar(ipRespaldo);
			proxy.abrir();
			GestorConexionesBD.ponerConexion(proxy);
		}
		
		// Añadimos la ventana del servidor frontend a la lista de logs
		logsFrontend = new ConexionLogFrontend();
		logsFrontend.ponerVentana(vent);
		GestorConexionesLog.ponerConexion(logsFrontend);
		// Establecemos conexión con la ventana del servidor de respaldo
		if(respaldo) {
			logsRespaldo = new ProxyLogRespaldo();
			logsRespaldo.conectar(ipRespaldo);
			GestorConexionesLog.ponerConexion(logsRespaldo);
		}
		
		// Conectamos el servidor y lo ponemos a la escucha
		if(servidor == null) {
			servidor = new ServidorFrontend();
		}
		if(conexionServidor == null)
			conexionServidor = new ConexionServidorFrontEnd();
		
		conexionServidor.activar(ipLocal);
		// Mostramos un mensaje indicando que el servidor está activo
		if(respaldo) {
			GestorConexionesLog.ponerMensaje("=== Servidor iniciado ===");
		} else {
			GestorConexionesLog.ponerMensaje("=== Servidor iniciado (servidor de respaldo inhabilitado) ===");
		}
	}
	
	public void detenerServidor(String ipRespaldo) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException, SQLException {
		// Cerramos las conexiones con las BD y vaciamos la lista
		// (ignoramos los errores que pudieran producirse)
		try {
			GestorConexionesBD.cerrarConexiones();
		} catch(SQLException e) {
		}
		GestorConexionesBD.quitarConexiones();
		// Desconectamos el servidor
		conexionServidor.desactivar(Inet4Address.getLocalHost().getHostAddress());
		// Mostramos un mensaje indicando que el servidor está inactivo
		GestorConexionesLog.ponerMensaje("=== Servidor detenido ===");
	}

}
