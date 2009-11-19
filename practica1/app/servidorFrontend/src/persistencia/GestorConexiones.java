package persistencia;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Gestor que permite tener sincronizadas varias bases de datos.
 */
public class GestorConexiones {

	private static ArrayList<IConexion> conexiones = new ArrayList<IConexion>();
	
	public static void ponerConexion(IConexion conexion) {
		if(!conexiones.contains(conexion)) {
			conexiones.add(conexion);
		}
	}
	
	public static void quitarConexion(IConexion conexion) {
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
		ArrayList<IConexion> conexionesUsadas;
		
		try {
			// Para hacer una modificación accedemos a todas las bases de
			// datos, y si alguna falla revertimos los cambios de las anteriores
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones esta vacía");
			}
			conexionesUsadas = new ArrayList<IConexion>();
			for(IConexion conexion : conexiones) {
				try {
					conexion.ejecutar(comando);
					conexionesUsadas.add(conexion);
				} catch(Exception ex) {
					// Deshacemos los cambios en las conexiones
					for(IConexion conexionUsada : conexionesUsadas) {
						conexionUsada.rollback();
					}
					throw new SQLException("Error en el acceso a las bases de datos", ex);
				}
			}
			// Aplicamos los cambios en todas las conexiones
			for(IConexion conexion : conexiones) {
				conexion.commit();
			}
		} catch(RemoteException ex) {
			throw new SQLException("Error en la conexión con una base de datos remota", ex);
		}
	}
	
	public static void cerrarConexiones() throws SQLException {
		try {
			// Cerramos todas las conexiones con bases de datos
			for(IConexion conexion : conexiones) {
				conexion.cerrar();
			}
		} catch(RemoteException ex) {
			throw new SQLException("Error en la conexión con una base de datos remota", ex);
		}
	}
	
}
