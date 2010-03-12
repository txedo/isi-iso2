package dominio.control;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.ComandoSQL;
import comunicaciones.ConexionBDRespaldo;
import comunicaciones.ConexionLogVentana;
import comunicaciones.IServidorRespaldo;

/**
 * Fachada del servidor de respaldo utilizada por el servidor front-end
 * que da acceso a la funcionalidad del servidor.
 */
public class ServidorRespaldo implements IServidorRespaldo {
	
	private static ServidorRespaldo instancia;

	private ConexionBDRespaldo basedatos;
	private ConexionLogVentana log;

	protected ServidorRespaldo() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		basedatos = new ConexionBDRespaldo();
		log = new ConexionLogVentana();
	}
	
	public static ServidorRespaldo getServidor() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if(instancia == null) {
			instancia = new ServidorRespaldo();
		}
		return instancia;
	}
	
	public ConexionBDRespaldo getConexionBD() {
		return basedatos;
	}
	
	public ConexionLogVentana getConexionEstado() {
		return log;
	}
	
	// Métodos de acceso a la base de datos
	
	public void abrir() throws RemoteException, SQLException {
		basedatos.abrir();
	}
	
	public void cerrar() throws RemoteException, SQLException {
		basedatos.cerrar();
	}
	
	public ResultSet consultar(ComandoSQL comando) throws RemoteException, SQLException {
		return basedatos.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws RemoteException, SQLException {
		basedatos.ejecutar(comando);
	}

	public void commit() throws RemoteException, SQLException {
		basedatos.commit();
	}

	public void rollback() throws RemoteException, SQLException {
		basedatos.rollback();
	}

	// Métodos del log del servidor

	public void ponerMensaje(String tipoMensaje, String mensaje) throws RemoteException {
		log.ponerMensaje(tipoMensaje, mensaje);
	}

	public void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws RemoteException {
		log.ponerMensaje(usuario, tipoMensaje, mensaje);
	}

	public void actualizarClientesEscuchando(int numeroClientes) throws RemoteException {
		log.actualizarClientesEscuchando(numeroClientes);
	}
	
}
