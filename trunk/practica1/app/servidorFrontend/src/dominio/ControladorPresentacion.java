package dominio;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import persistencia.GestorConexionesBD;
import presentacion.JFServidorFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.ProxyBDRespaldo;
import comunicaciones.ServidorFrontend;

/**
 * Controlador principal de la funcionalidad del servidor frontend.
 */
public class ControladorPresentacion {
	
	private ServidorFrontend servidor;
	private ObservadorPresentacion observador;
	private ProxyBDRespaldo proxy;
	private ConexionBDFrontend basedatos;

	public ControladorPresentacion() {
		observador = new ObservadorPresentacion();
		servidor = null;
	}
	
	public void mostrarVentana() {
		JFServidorFrontend vent;
		
		// Mostramos la ventana principal del servidor
		vent = new JFServidorFrontend();
		vent.setControladorPresentacion(this);
		vent.setVisible(true);
		observador.add(vent);
	}
	
	public void iniciarServidor(String ipFrontend, String ipRespaldo) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
		boolean respaldo;

		// Cerramos las conexiones que pudiera haber abiertas
		// (ignoramos los errores que pudieran producirse)
		try {
			GestorConexionesBD.cerrarConexiones();
		} catch(SQLException e) {
		}
		GestorConexionesBD.quitarConexiones();
		// Creamos una conexión con la base de datos local
		basedatos = new ConexionBDFrontend();
		basedatos.abrir();
		GestorConexionesBD.ponerConexion(basedatos);
		// Establecemos conexión con el servidor de respaldo
		respaldo = (ipRespaldo != null && !ipRespaldo.equals(""));
		if(respaldo) {
			proxy = new ProxyBDRespaldo();
			proxy.conectar(ipRespaldo);
			proxy.abrir();
			GestorConexionesBD.ponerConexion(proxy);
		}
		// Conectamos el servidor y lo ponemos a la escucha
		if(servidor == null) {
			servidor = new ServidorFrontend();
		}
		servidor.setControlador(this);
		servidor.conectar(ipFrontend);
		// Mostramos un mensaje indicando que el servidor está activo
		if(respaldo) {
			observador.actualizarVentanas("Servidor iniciado.");
		} else {
			observador.actualizarVentanas("Servidor iniciado (servidor de respaldo inhabilitado).");
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
		observador.actualizarVentanas("Servidor detenido.");
	}

	public ObservadorPresentacion getObservador() {
		return observador;
	}

}
