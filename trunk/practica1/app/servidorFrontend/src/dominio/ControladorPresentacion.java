package dominio;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import persistencia.GestorConexionesBD;
import presentacion.JFServidorFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.ProxyAgenteRemoto;
import comunicaciones.ServidorFrontend;

public class ControladorPresentacion {
	
	private ServidorFrontend servidor;
	private ObservadorPresentacion observador;
	private ProxyAgenteRemoto proxy;
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
		// Establecemos conexión con el servidor de respaldo
		proxy = new ProxyAgenteRemoto();
		proxy.conectar(ipRespaldo);
		GestorConexionesBD.ponerConexion(proxy);
		// Creamos una conexión con la base de datos local
		basedatos = new ConexionBDFrontend();
		GestorConexionesBD.ponerConexion(basedatos);
		// Conectamos el servidor y lo ponemos a la escucha
		if(servidor == null) {
			servidor = new ServidorFrontend();
		}
		servidor.setControlador(this);
		servidor.conectar(ipFrontend);
		// Mostramos un mensaje indicando que el servidor está activo
    	observador.actualizarVentanas("Servidor iniciado.");
	}
	
	public void detenerServidor(String ipFrontend, String ipRespaldo) throws RemoteException, MalformedURLException, NotBoundException, SQLException {
		// Cerramos las conexiones con las BD y vaciamos la lista
		GestorConexionesBD.cerrarConexiones();
		GestorConexionesBD.quitarConexiones();
		// Desconectamos el servidor
		servidor.desconectar(ipFrontend);
		// Mostramos un mensaje indicando que el servidor está inactivo
		observador.actualizarVentanas("Servidor iniciado.");
	}

	public ObservadorPresentacion getObservador() {
		return observador;
	}

	public ServidorFrontend getServidor() {
		return servidor;
	}

}
