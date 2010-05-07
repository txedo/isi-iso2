package dominio.control;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import persistencia.ConsultaHibernate;

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
	
	public List<?> consultar(ConsultaHibernate consulta) throws RemoteException, SQLException {
		return basedatos.consultar(consulta);
	}

	public void iniciarTransaccion() throws RemoteException, SQLException {
		basedatos.iniciarTransaccion();
	}

	public Object insertar(Object objeto) throws RemoteException, SQLException {
		return basedatos.insertar(objeto);
	}

	public void actualizar(Object objeto) throws RemoteException, SQLException {
		basedatos.actualizar(objeto);
	}

	public void eliminar(Object objeto) throws RemoteException, SQLException {
		basedatos.eliminar(objeto);
	}

	public void borrarCache(Object objeto) throws RemoteException, SQLException {
		basedatos.borrarCache(objeto);
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
