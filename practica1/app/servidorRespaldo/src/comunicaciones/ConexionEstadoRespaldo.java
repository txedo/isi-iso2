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
import presentacion.IVentanaEstado;

/**
 * Clase que exporta la instancia que será utilizada por el servidor
 * front-end para mostrar los mensajes generados en la ventana
 * principal del servidor de respaldo.
 */
public class ConexionEstadoRespaldo extends UnicastRemoteObject implements IConexionEstado {

	private static final long serialVersionUID = 8245499027365521538L;
	private final String NOMBRE_LOG = "servidorrespaldo";
	
	private ArrayList<IVentanaEstado> ventanas;
	
	public ConexionEstadoRespaldo() throws SQLException, RemoteException {
		super();
		// El constructor de 'UnicastRemoteObject' exporta automáticamente
		// este objeto; aquí cancelamos la exportación porque ya llamamos
		// manualmente a 'exportObject' en el método 'conectar'
		unexportObject(this, false);
		LocateRegistry.createRegistry(PUERTO_CONEXION_ESTADO);
		ventanas = new ArrayList<IVentanaEstado>();
	}

	public void activar(String ip) throws MalformedURLException, RemoteException, SQLException {
        exportObject(this, PUERTO_CONEXION_ESTADO);
        try {
            Naming.bind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION_ESTADO) + "/" + NOMBRE_LOG, this);
        } catch(AlreadyBoundException ex) {
            Naming.rebind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION_ESTADO) + "/" + NOMBRE_LOG, this);
        }
    }
		
	public void desactivar(String ip) throws RemoteException, MalformedURLException, NotBoundException {		
		unexportObject(this, false);
		Naming.unbind("rmi://" + ip + ":" + String.valueOf(PUERTO_CONEXION_ESTADO) + "/" + NOMBRE_LOG);
    }
	
	public void ponerVentana(IVentanaEstado ventana) {
		ventanas.add(ventana);
	}
	
	public void ponerMensaje(String mensaje) {
		for(IVentanaEstado ventana : ventanas) {
			ventana.actualizarTexto(mensaje);
		}
	}

	public void actualizarClientesEscuchando(int arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
}
