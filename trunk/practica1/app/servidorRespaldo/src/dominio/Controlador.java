package dominio;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import comunicaciones.ConexionBDRespaldo;
import presentacion.JFServidorRespaldo;

/**
 * Controlador principal de la funcionalidad del servidor de respaldo.
 */
public class Controlador {

	private ConexionBDRespaldo conexionBD;

	public Controlador() {
		conexionBD = null;
	}
	
	public void mostrarVentana() {
		JFServidorRespaldo vent;
		
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
	}
	
	public void detenerServidorRespaldo(String ipRespaldo) throws RemoteException, MalformedURLException, NotBoundException, SQLException {
		// Desactivamos la clase remota para conectarse con la base de datos
		conexionBD.desconectar(ipRespaldo);
	}
	
}
