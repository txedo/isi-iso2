package dominio.control;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import presentacion.JFServidorFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConexionLogBD;
import comunicaciones.ConexionLogVentana;
import comunicaciones.RemotoServidorFrontend;
import comunicaciones.GestorConexionesBD;
import comunicaciones.GestorConexionesLog;
import comunicaciones.ProxyServidorRespaldo;
import dominio.conocimiento.ConfiguracionFrontend;
import dominio.conocimiento.ITiposMensajeLog;

/**
 * Controlador principal de la funcionalidad del servidor front-end.
 */
public class ControladorFrontend {
	
	private ProxyServidorRespaldo proxy;
	private RemotoServidorFrontend remotoServidor;
	private ConexionBDFrontend basedatos;
	private JFServidorFrontend ventana;
	private boolean servidorActivo;

	public ControladorFrontend() {
		remotoServidor = null;
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
		ConexionLogBD logBD;
		ConexionLogVentana logFrontend;
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
		GestorConexionesLog.quitarConexiones();
		
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
		
		// Añadimos las conexiones que mostrarán los mensaje del servidor
		// en su ventana principal y los guardará en la base de datos
		logFrontend = new ConexionLogVentana();
		logFrontend.ponerVentana(ventana);
		logBD = new ConexionLogBD();
		GestorConexionesLog.ponerConexion(logFrontend);
		GestorConexionesLog.ponerConexion(logBD);
		
		// Establecemos conexión con el servidor de respaldo
		if(configuracion.isRespaldoActivado()) {
			try {
				proxy = new ProxyServidorRespaldo();
				proxy.conectar(configuracion.getIPRespaldo(), configuracion.getPuertoRespaldo());
				// Abrimos la base de datos
				proxy.abrir();
			} catch(NotBoundException e) {
				throw new NotBoundException("No se puede conectar con el servidor de respaldo porque está desactivado (IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()) + ").");
			} catch(RemoteException e) {
				throw new RemoteException("No se puede conectar con el servidor de respaldo (IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()) + ").");
			} catch(SQLException e) {
				throw new SQLException("No se puede establecer una conexión con el servidor de la base de datos de respaldo.");
			}
			GestorConexionesBD.ponerConexion(proxy);
			GestorConexionesLog.ponerConexion(proxy);
		}
		
		// Creamos el servidor y lo ponemos a la escucha
		try {
			remotoServidor = RemotoServidorFrontend.getServidor();
			remotoServidor.activar(ipLocal, configuracion.getPuertoFrontend());
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor front-end en la dirección IP " + ipLocal + " y el puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ".");
		}
		
		// Mostramos un mensaje indicando que el servidor está activo
		if(configuracion.isRespaldoActivado()) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "=== Servidor iniciado ===");
		} else {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "=== Servidor iniciado (servidor de respaldo deshabilitado) ===");
		}
		
		// El servidor está activo
		servidorActivo = true;
	}
	
	public int getNumeroClientesConectados () {
		return GestorSesiones.getClientes().size();
	}
	
	public void detenerServidor(ConfiguracionFrontend configuracion) throws RemoteException, MalformedURLException, UnknownHostException, SQLException {
		// Generamos un mensaje indicando que el servidor está inactivo
		// (ignoramos los errores que pudieran producirse)
		try {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "=== Servidor detenido ===");
		} catch(RemoteException e) {
		} catch(SQLException e) {
		}
		// Notificamos a los cliente que el servidor ha sido desconectado
		GestorSesiones.desconectarClientes();

		// Cerramos las conexiones con las BD y vaciamos la lista
		// (ignoramos los errores que pudieran producirse)
		try {
			GestorConexionesBD.cerrarConexiones();
			GestorConexionesBD.quitarConexiones();
		} catch(SQLException e) {
		}
		
		// Desconectamos el servidor
		if(remotoServidor != null) {
			remotoServidor.desactivar(Inet4Address.getLocalHost().getHostAddress(), configuracion.getPuertoFrontend());
		}
				
		// El servidor no está activo
		servidorActivo = false;
	}

}
