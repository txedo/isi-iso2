package dominio.control;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import presentacion.JFServidorFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConexionLogFrontend;
import comunicaciones.GestorConexionesBD;
import comunicaciones.GestorConexionesLog;
import comunicaciones.ProxyBDRespaldo;
import comunicaciones.ProxyLogRespaldo;
import comunicaciones.ServidorFrontend;

/**
 * Controlador principal de la funcionalidad del servidor frontend.
 */
public class ControladorPrincipal {
	
	private ServidorFrontend servidor;
	private ProxyBDRespaldo proxy;
	private ConexionBDFrontend basedatos;
	private JFServidorFrontend vent;

	public ControladorPrincipal() {
		servidor = null;
	}
	
	public void mostrarVentana() {
		// Mostramos la ventana principal del servidor
		vent = new JFServidorFrontend();
		vent.setControladorPresentacion(this);
		vent.setVisible(true);
	}
	
	public void iniciarServidor(String ipFrontend, String ipRespaldo) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
		ConexionLogFrontend logsFrontend;
		ProxyLogRespaldo logsRespaldo;
		boolean respaldo;

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
		servidor.conectar(ipFrontend);
		// Mostramos un mensaje indicando que el servidor está activo
		if(respaldo) {
			GestorConexionesLog.ponerMensaje("=== Servidor iniciado ===");
		} else {
			GestorConexionesLog.ponerMensaje("=== Servidor iniciado (servidor de respaldo inhabilitado) ===");
		}
	}
	
	public void detenerServidor(String ipFrontend, String ipRespaldo) throws RemoteException, MalformedURLException, NotBoundException, SQLException {
		// Cerramos las conexiones con las BD y vaciamos la lista
		// (ignoramos los errores que pudieran producirse)
		try {
			GestorConexionesBD.cerrarConexiones();
		} catch(SQLException e) {
		}
		GestorConexionesBD.quitarConexiones();
		// Desconectamos el servidor
		servidor.desconectar(ipFrontend);
		// Mostramos un mensaje indicando que el servidor está inactivo
		GestorConexionesLog.ponerMensaje("=== Servidor detenido ===");
	}

}
