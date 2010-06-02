package dominio.control;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import presentacion.JFServidorFrontend;

import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConexionLogBD;
import comunicaciones.ConexionLogVentana;
import comunicaciones.ConfiguracionFrontend;
import comunicaciones.GestorConexionesBD;
import comunicaciones.GestorConexionesLog;
import comunicaciones.ProxyServidorRespaldo;
import comunicaciones.RemotoServidorFrontend;
import comunicaciones.UtilidadesComunicaciones;

import dominio.conocimiento.ITiposMensajeLog;

/**
 * Controlador principal de la funcionalidad del servidor front-end.
 */
public class ControladorFrontend {
	
	private ProxyServidorRespaldo proxy;
	private RemotoServidorFrontend remotoServidor;
	private ConexionBDFrontend basedatos;
	private JFServidorFrontend ventana;
	private String ipServidor;
	private boolean servidorActivo;

	public ControladorFrontend() {
		remotoServidor = null;
		servidorActivo = false;
		ipServidor = null;
		ventana = new JFServidorFrontend(this);
	}
		
	public void mostrarVentana() {
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}

	public void ocultarVentana() {
		ventana.setVisible(false);
	}

	public void iniciarServidor(ConfiguracionFrontend configuracion) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		ConexionLogBD logBD;
		ConexionLogVentana logFrontend;
		boolean ok;

		// Obtenemos la IP de la máquina local
		ipServidor = UtilidadesComunicaciones.obtenerIPHost();
		
		// Indicamos a RMI que debe utilizar la IP obtenida como IP de este host
		// en las comunicaciones remotas; esta instrucción es necesaria porque
		// si el ordenador pertenece a más de una red, puede que RMI tome una IP
		// privada como IP del host y las comunicaciones entrantes no funcionen
		System.setProperty("java.rmi.server.hostname", ipServidor);

		// Vaciamos las listas de conexiones
		GestorConexionesBD.quitarConexiones();
		GestorConexionesLog.quitarConexiones();
		
		// Creamos la conexión con la base de datos local mediante Hibernate
		basedatos = new ConexionBDFrontend();
		basedatos.setIP(configuracion.getIPBDPrincipal());
		basedatos.setPuerto(configuracion.getPuertoBDPrincipal());
		ok = basedatos.probarConexion();
		if(!ok) {
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
			} catch(NotBoundException e) {
				throw new NotBoundException("No se puede conectar con el servidor de respaldo porque está desactivado (IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()) + ").");
			} catch(RemoteException e) {
				throw new RemoteException("No se puede conectar con el servidor de respaldo (IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()) + ").");
			}
			GestorConexionesBD.ponerConexion(proxy);
			GestorConexionesLog.ponerConexion(proxy);
		}
		
		// Creamos el servidor y lo ponemos a la escucha
		try {
			remotoServidor = RemotoServidorFrontend.getServidor();
			remotoServidor.activar(ipServidor, configuracion.getPuertoFrontend());
		} catch(RemoteException e) {
			throw new RemoteException("No se puede poner a la escucha el servidor front-end en la dirección IP " + ipServidor + " y el puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ".");
		}
		
		// Mostramos un mensaje indicando que el servidor está activo
		if(configuracion.isRespaldoActivado()) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "=== Servidor iniciado ===");
		} else {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "=== Servidor iniciado (servidor de respaldo deshabilitado) ===");
		}
		GestorConexionesLog.actualizarClientesEscuchando(0);
		
		// El servidor está activo
		servidorActivo = true;
	}

	public void detenerServidor(ConfiguracionFrontend configuracion) throws RemoteException, MalformedURLException, UnknownHostException, SQLException {
		// Al desconectar el servidor ignoramos todos los errores de conexión
		// y SQL para poder desconectar el servidor incluso si no se tiene
		// acceso a los clientes que se habían registrado (p.ej., porque se ha
		// caído la red), el servidor de base de datos ha dejado de funcionar
		// o ha cambiado la IP de la máquina del servidor front-end
		
		// Generamos un mensaje indicando que el servidor está inactivo
		try {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "=== Servidor detenido ===");
		} catch(RemoteException e) {
		} catch(SQLException e) {
		}
		
		// Notificamos a los clientes que el servidor ha sido desconectado
		try {
			GestorSesiones.desconectarClientes();
		} catch(RemoteException e) {
		}
		
		// Vaciamos la lista de conexiones
		GestorConexionesBD.quitarConexiones();
		
		// Desconectamos el servidor
		if(remotoServidor != null) {
			try {
				remotoServidor.desactivar(ipServidor, configuracion.getPuertoFrontend());
			} catch(RemoteException e) {
			}
		}
		
		// El servidor no está activo
		servidorActivo = false;
	}
	
	public JFServidorFrontend getVentana() {
		return ventana;
	}
	
	public boolean isServidorActivo() {
		return servidorActivo;
	}

	public String getIPServidor() {
		return ipServidor;
	}
	
	public int getNumeroClientesConectados() {
		return GestorSesiones.getClientes().size();
	}

}
