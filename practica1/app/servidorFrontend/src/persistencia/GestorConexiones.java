package persistencia;

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
	
	public static ResultSet consultar(ComandoSQL comando) throws SQLException {
		// Para hacer una consulta utilizamos la primera conexi�n
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones esta vac�a");
		}
		return conexiones.get(0).consultar(comando);
	}
	
	public static void ejecutar(ComandoSQL comando) throws SQLException {
		ArrayList<IConexion> conexionesUsadas;
		
		// Para hacer una modificaci�n accedemos a todas las bases de
		// datos, y si alguna falla revertimos los cambios de las anteriores
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones esta vac�a");
		}
		conexionesUsadas = new ArrayList<IConexion>();
		for(IConexion conexion : conexiones) {
			try {
				conexion.ejecutar(comando);
				conexionesUsadas.add(conexion);
			} catch(SQLException ex) {
				// Deshacemos los cambios en las conexiones
				for(IConexion conexionUsada : conexionesUsadas) {
					conexionUsada.rollback();
				}
				throw ex;
			}
		}
		// Aplicamos los cambios en todas las conexiones
		for(IConexion conexion : conexiones) {
			conexion.rollback();
		}
	}
	
	public static void cerrarConexiones() throws SQLException {
		// Cerramos todas las conexiones con bases de datos
		for(IConexion conexion : conexiones) {
			conexion.cerrar();
		}
	}
	
}