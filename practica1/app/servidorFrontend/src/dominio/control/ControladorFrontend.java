package dominio.control;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import presentacion.JFServidorFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConexionEstadoFrontend;
import comunicaciones.ConexionServidorFrontend;
import comunicaciones.GestorConexionesBD;
import comunicaciones.GestorConexionesEstado;
import comunicaciones.ProxyBDRespaldo;
import comunicaciones.ProxyEstadoRespaldo;

/**
 * Controlador principal de la funcionalidad del servidor front-end.
 */
public class ControladorFrontend {
	
	private ProxyBDRespaldo proxy;
	private ConexionServidorFrontend conexionServidor;
	private ConexionBDFrontend basedatos;
	private JFServidorFrontend vent;
	private boolean servidorActivo;

	public ControladorFrontend() {
		conexionServidor = null;
		servidorActivo = false;
	}
	
	public boolean getServidorActivo() {
		return servidorActivo;
	}
	
	public void mostrarVentana() {
		// Mostramos la ventana principal del servidor
		vent = new JFServidorFrontend();
		vent.setControladorPresentacion(this);
		vent.setVisible(true);
	}
	
	public void iniciarServidor(String ipBDPrincipal, String ipRespaldo) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException, SQLException {
		ConexionEstadoFrontend estadoFrontend;
		ProxyEstadoRespaldo estadoRespaldo;
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
		GestorConexionesEstado.quitarConexiones();
		
		// Comprobamos si se va a usar el servidor de respaldo
		respaldo = (ipRespaldo != null && !ipRespaldo.equals(""));

		// Creamos una conexión con la base de datos local
		try {
			basedatos = new ConexionBDFrontend();
			basedatos.getAgente().setIP(ipBDPrincipal);
			basedatos.abrir();
		} catch(SQLException e) {
			throw new SQLException("No se puede establecer una conexión con el servidor de la base de datos principal (" + ipBDPrincipal + ").");
		}
		GestorConexionesBD.ponerConexion(basedatos);
		
		// Establecemos conexión con la base de datos del servidor de respaldo
		if(respaldo) {
			try {
				proxy = new ProxyBDRespaldo();
				proxy.conectar(ipRespaldo);
				proxy.abrir();
			} catch(NotBoundException e) {
				throw new NotBoundException("No se puede conectar con el servidor de respaldo porque está desactivado (" + ipRespaldo + ").");
			} catch(RemoteException e) {
				throw new RemoteException("No se puede conectar con el servidor de respaldo (" + ipRespaldo + ").");
			} catch(SQLException e) {
				throw new SQLException("No se puede establecer una conexión con el servidor de base de datos de respaldo.");
			}
			GestorConexionesBD.ponerConexion(proxy);
		}
		
		// Añadimos la ventana del servidor frontend a la lista de logs
		estadoFrontend = new ConexionEstadoFrontend();
		estadoFrontend.ponerVentana(vent);
		GestorConexionesEstado.ponerConexion(estadoFrontend);
		// Establecemos conexión con la ventana del servidor de respaldo
		if(respaldo) {
			try {
				estadoRespaldo = new ProxyEstadoRespaldo();
				estadoRespaldo.conectar(ipRespaldo);
			} catch(NotBoundException e) {
				throw new NotBoundException("No se puede conectar con el servidor de respaldo porque está desactivado (" + ipRespaldo + ").");
			} catch(RemoteException e) {
				throw new RemoteException("No se puede conectar con el servidor de respaldo (" + ipRespaldo + ").");
			}
			GestorConexionesEstado.ponerConexion(estadoRespaldo);
		}
		
		// Creamos el servidor y lo ponemos a la escucha
		try {
			conexionServidor = ConexionServidorFrontend.getConexion();
			conexionServidor.activar(ipLocal);
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor front-end en la dirección IP " + ipLocal + ":" + ServidorFrontend.PUERTO_SERVIDOR + ".");
		}
		
		// Mostramos un mensaje indicando que el servidor está activo
		if(respaldo) {
			GestorConexionesEstado.ponerMensaje("=== Servidor iniciado ===");
		} else {
			GestorConexionesEstado.ponerMensaje("=== Servidor iniciado (servidor de respaldo deshabilitado) ===");
		}
		
		// El servidor está activo
		servidorActivo = true;
	}
	
	public void detenerServidor(String ipRespaldo) throws RemoteException, MalformedURLException, UnknownHostException, SQLException {
		// Cerramos las conexiones con las BD y vaciamos la lista
		// (ignoramos los errores que pudieran producirse)
		try {
			GestorConexionesBD.cerrarConexiones();
		} catch(SQLException e) {
		}
		GestorConexionesBD.quitarConexiones();
		
		// Desconectamos el servidor
		if(conexionServidor != null) {
			conexionServidor.desactivar(Inet4Address.getLocalHost().getHostAddress());
		}
		
		// Mostramos un mensaje indicando que el servidor está inactivo
		GestorConexionesEstado.ponerMensaje("=== Servidor detenido ===");
		
		// El servidor no está activo
		servidorActivo = false;
	}

}
