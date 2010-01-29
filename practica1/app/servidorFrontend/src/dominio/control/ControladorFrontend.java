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
import dominio.conocimiento.ConfiguracionFrontend;

/**
 * Controlador principal de la funcionalidad del servidor front-end.
 */
public class ControladorFrontend {
	
	private ProxyBDRespaldo proxy;
	private ConexionServidorFrontend conexionServidor;
	private ConexionBDFrontend basedatos;
	private JFServidorFrontend ventana;
	private boolean servidorActivo;

	public ControladorFrontend() {
		conexionServidor = null;
		servidorActivo = false;
		ventana = new JFServidorFrontend(this);
	}
	
	public JFServidorFrontend getVentana() {
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

	public void iniciarServidor(ConfiguracionFrontend configuracion) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException, SQLException {
		ConexionEstadoFrontend estadoFrontend;
		ProxyEstadoRespaldo estadoRespaldo;
		String ipLocal;

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
		
		// Creamos una conexión con la base de datos local
		try {
			basedatos = new ConexionBDFrontend();
			basedatos.getAgente().setIP(configuracion.getIPBDPrincipal());
			basedatos.getAgente().setPuerto(configuracion.getPuertoBDPrincipal());
			basedatos.abrir();
		} catch(SQLException e) {
			throw new SQLException("No se puede establecer una conexión con el servidor de la base de datos principal (IP " + configuracion.getIPBDPrincipal() + ", puerto " + String.valueOf(configuracion.getPuertoBDPrincipal()) + ").");
		}
		GestorConexionesBD.ponerConexion(basedatos);
		
		// Establecemos conexión con la base de datos del servidor de respaldo
		if(configuracion.isRespaldoActivado()) {
			try {
				proxy = new ProxyBDRespaldo();
				proxy.conectar(configuracion.getIPRespaldo(), configuracion.getPuertoRespaldo());
				proxy.abrir();
			} catch(NotBoundException e) {
				throw new NotBoundException("No se puede conectar con el servidor de respaldo porque está desactivado (IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()) + ").");
			} catch(RemoteException e) {
				throw new RemoteException("No se puede conectar con el servidor de respaldo (IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()) + ").");
			} catch(SQLException e) {
				throw new SQLException("No se puede establecer una conexión con el servidor de la base de datos de respaldo.");
			}
			GestorConexionesBD.ponerConexion(proxy);
		}
		
		// Añadimos la ventana del servidor frontend a la lista de logs
		estadoFrontend = new ConexionEstadoFrontend();
		estadoFrontend.ponerVentana(ventana);
		GestorConexionesEstado.ponerConexion(estadoFrontend);
		// Establecemos conexión con la ventana del servidor de respaldo
		if(configuracion.isRespaldoActivado()) {
			try {
				estadoRespaldo = new ProxyEstadoRespaldo();
				estadoRespaldo.conectar(configuracion.getIPRespaldo(), configuracion.getPuertoRespaldo() + 1);
			} catch(NotBoundException e) {
				throw new NotBoundException("No se puede conectar con el servidor de respaldo porque está desactivado (IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()) + ").");
			} catch(RemoteException e) {
				throw new RemoteException("No se puede conectar con el servidor de respaldo (IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()) + ").");
			}
			GestorConexionesEstado.ponerConexion(estadoRespaldo);
		}
		
		// Creamos el servidor y lo ponemos a la escucha
		try {
			conexionServidor = ConexionServidorFrontend.getConexion();
			conexionServidor.activar(ipLocal, configuracion.getPuertoFrontend());
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor front-end en la dirección IP " + ipLocal + " y el puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ".");
		}
		
		// Mostramos un mensaje indicando que el servidor está activo
		if(configuracion.isRespaldoActivado()) {
			GestorConexionesEstado.ponerMensaje("=== Servidor iniciado ===");
		} else {
			GestorConexionesEstado.ponerMensaje("=== Servidor iniciado (servidor de respaldo deshabilitado) ===");
		}
		
		// El servidor está activo
		servidorActivo = true;
	}
	
	public void detenerServidor(ConfiguracionFrontend configuracion) throws RemoteException, MalformedURLException, UnknownHostException, SQLException {
		// Cerramos las conexiones con las BD y vaciamos la lista
		// (ignoramos los errores que pudieran producirse)
		try {
			GestorConexionesBD.cerrarConexiones();
		} catch(SQLException e) {
		}
		GestorConexionesBD.quitarConexiones();
		
		// Desconectamos el servidor
		if(conexionServidor != null) {
			conexionServidor.desactivar(Inet4Address.getLocalHost().getHostAddress(), configuracion.getPuertoFrontend());
		}
		
		// Mostramos un mensaje indicando que el servidor está inactivo
		GestorConexionesEstado.ponerMensaje("=== Servidor detenido ===");
		
		// El servidor no está activo
		servidorActivo = false;
	}

}
