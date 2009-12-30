package dominio;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import comunicaciones.ConexionBDRespaldo;
import comunicaciones.ConexionLogRespaldo;
import presentacion.JFServidorRespaldo;

/**
 * Controlador principal de la funcionalidad del servidor de respaldo.
 */
public class Controlador {

	private ConexionBDRespaldo conexionBD;
	private ConexionLogRespaldo conexionLog;
	private JFServidorRespaldo vent;

	public Controlador() {
		conexionBD = null;
	}
	
	public void mostrarVentana() {
		// Mostramos la ventana principal del servidor
		vent = new JFServidorRespaldo();
		vent.setControlador(this);
		vent.setVisible(true);
	}
	
	public void iniciarServidorRespaldo(String ipRespaldo) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
		// Activamos la clase remota para conectarse con la base de datos
		if(conexionBD == null) {
			conexionBD = new ConexionBDRespaldo();
		}
		conexionBD.conectar(ipRespaldo);
		// Activamos la clase remota para mostrar los mensajes del servidor
		if(conexionLog == null) {
			conexionLog = new ConexionLogRespaldo();
			conexionLog.ponerVentana(vent);
		}
		conexionLog.conectar(ipRespaldo);
	}
	
	public void detenerServidorRespaldo(String ipRespaldo) throws RemoteException, MalformedURLException, NotBoundException, SQLException {
		// Desactivamos las clases remotas del servidor de respaldo
		conexionBD.desconectar(ipRespaldo);
		conexionLog.desconectar(ipRespaldo);
	}
	
}
