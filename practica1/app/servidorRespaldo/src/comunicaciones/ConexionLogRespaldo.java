package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import presentacion.IVentanaLog;

/**
 * Clase que recibe los mensajes generados por el servidor y los
 * muestra en la ventana principal del servidor de respaldo.
 */
public class ConexionLogRespaldo extends UnicastRemoteObject implements IConexionLog {

	private static final long serialVersionUID = 8245499027365521538L;
	private final String NOMBRE_LOG = "servidorrespaldo";
	
	private ArrayList<IVentanaLog> ventanas;
	
	public ConexionLogRespaldo() throws SQLException, RemoteException {
		super();
		// El constructor de 'UnicastRemoteObject' exporta automáticamente
		// este objeto; aquí cancelamos la exportación porque ya llamamos
		// manualmente a 'exportObject' en el método 'conectar'
		unexportObject(this, false);
		LocateRegistry.createRegistry(PUERTO_CONEXION);
		ventanas = new ArrayList<IVentanaLog>();
	}

	public void activar(String ip) throws MalformedURLException, RemoteException, SQLException {
        exportObject(this, PUERTO_CONEXION);
        try {
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_LOG, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_LOG, this);
        }
    }
		
	public void desactivar(String ip) throws RemoteException, MalformedURLException, NotBoundException {		
		unexportObject(this, false);
		Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION) + "/" + NOMBRE_LOG);
    }
	
	public void ponerVentana(IVentanaLog ventana) {
		ventanas.add(ventana);
	}
	
	public void ponerMensaje(String mensaje) {
		for(IVentanaLog ventana : ventanas) {
			ventana.actualizarTexto(mensaje);
		}
	}
	
}
