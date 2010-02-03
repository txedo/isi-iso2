package comunicaciones;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import persistencia.ComandoSQL;
import comunicaciones.IConexionBD;

/**
 * Gestor que permite acceder de forma sincronizada a varias bases de datos
 * (locales o remotas).
 */
public class GestorConexionesBD {

	private static ArrayList<IConexionBD> conexiones = new ArrayList<IConexionBD>();
	
	public static void ponerConexion(IConexionBD conexion) {
		if(!conexiones.contains(conexion)) {
			conexiones.add(conexion);
		}
	}
	
	public static void quitarConexion(IConexionBD conexion) {
		if(conexiones.contains(conexion)) {
			conexiones.remove(conexion);
		}
	}

	public static void quitarConexiones() {
		conexiones.clear();
	}
	
	public static ResultSet consultar(ComandoSQL comando) throws SQLException {
		ResultSet datos;
		
		try {
			// Para hacer una consulta utilizamos la primera conexión
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones esta vacía");
			}
			datos = conexiones.get(0).consultar(comando);
		} catch(RemoteException ex) {
			throw new SQLException("Error en la conexión con una base de datos remota", ex);
		}
		return datos;
	}
	
	public static void ejecutar(ComandoSQL comando) throws SQLException {
		ArrayList<IConexionBD> conexionesUsadas;
		
		try {
			// Para hacer una modificación accedemos a todas las bases de
			// datos, y si alguna falla revertimos los cambios de las anteriores
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones esta vacía");
			}
			conexionesUsadas = new ArrayList<IConexionBD>();
			for(IConexionBD conexion : conexiones) {
				try {
					conexion.ejecutar(comando);
					conexionesUsadas.add(conexion);
				} catch(Exception ex) {
					// Deshacemos los cambios en las conexiones
					for(IConexionBD conexionUsada : conexionesUsadas) {
						conexionUsada.rollback();
					}
					throw new SQLException("Error en el acceso a las bases de datos", ex);
				}
			}
			// Aplicamos los cambios en todas las conexiones
			for(IConexionBD conexion : conexiones) {
				conexion.commit();
			}
		} catch(RemoteException ex) {
			throw new SQLException("Error en la conexión con una base de datos remota", ex);
		}
	}
	
	public static void cerrarConexiones() throws SQLException {
		try {
			// Cerramos todas las conexiones con bases de datos
			for(IConexionBD conexion : conexiones) {
				conexion.cerrar();
			}
		} catch(RemoteException ex) {
			throw new SQLException("Error en la conexión con una base de datos remota", ex);
		}
	}
	
}
