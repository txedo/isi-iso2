package dominio;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
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
	
	public void iniciarServidorRespaldo(String ipBDRespaldo) throws MalformedURLException, RemoteException, NotBoundException, SQLException, UnknownHostException {
		String ipLocal;

		// Obtenemos la IP de la máquina local
		ipLocal = Inet4Address.getLocalHost().getHostAddress();
		
		// Activamos la clase remota para conectarse con la base de datos
		if(conexionBD == null) {
			conexionBD = new ConexionBDRespaldo();
			conexionBD.getAgente().setIP(ipBDRespaldo);
		}
		conexionBD.activar(ipLocal);
		// Activamos la clase remota para mostrar los mensajes del servidor
		if(conexionLog == null) {
			conexionLog = new ConexionLogRespaldo();
			conexionLog.ponerVentana(vent);
		}
		conexionLog.activar(ipLocal);
	}
	
	public void detenerServidorRespaldo() throws RemoteException, MalformedURLException, NotBoundException, SQLException, UnknownHostException {
		String ipLocal;

		// Obtenemos la IP de la máquina local
		ipLocal = Inet4Address.getLocalHost().getHostAddress();
		
		// Desactivamos las clases remotas del servidor de respaldo
		conexionBD.desactivar(ipLocal);
		conexionLog.desactivar(ipLocal);
	}
	
}
