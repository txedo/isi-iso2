package comunicaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistencia.ConsultaHibernate;

/**
 * Gestor que permite acceder y modificar de forma sincronizada varias
 * bases de datos (locales o remotas).
 */
public class GestorConexionesBD {

	private static ArrayList<IConexionBD> conexiones = new ArrayList<IConexionBD>();
	
	public static void ponerConexion(IConexionBD conexion) {
		if(!conexiones.contains(conexion)) {
			conexiones.add(conexion);
		}
	}

	public static void quitarConexiones() {
		conexiones.clear();
	}
	
	public static List<?> consultar(ConsultaHibernate consulta) throws SQLException {
		List<?> datos;
		
		// Para hacer una consulta utilizamos sólo la primera conexión
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones está vacía.");
		}
		try {
			datos = conexiones.get(0).consultar(consulta);
		} catch(Exception ex) {
			if(conexiones.get(0) instanceof ConexionBDFrontend) {
				throw new SQLException("Error en el acceso a la base de datos principal.", ex);
			} else if(conexiones.get(0) instanceof ProxyServidorRespaldo) {
				throw new SQLException("Error en el acceso a la base de datos secundaria.", ex);
			} else {
				throw new SQLException("Error en el acceso a las bases de datos.", ex);
			}
		}
		
		return datos;
	}
	
	public static void iniciarTransaccion() throws SQLException {
		// Iniciamos una transacción que puede estar formada
		// por más de una operación sobre la base de datos
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones está vacía.");
		}
		for(IConexionBD conexion : conexiones) {
			try {
				conexion.iniciarTransaccion();
			} catch(Exception ex) {
				if(conexion instanceof ConexionBDFrontend) {
					throw new SQLException("Error en el acceso a la base de datos principal.", ex);
				} else if(conexion instanceof ProxyServidorRespaldo) {
					throw new SQLException("Error en el acceso a la base de datos secundaria.", ex);
				} else {
					throw new SQLException("Error en el acceso a las bases de datos.", ex);
				}
			}
		}
	}
	
	public static void terminarTransaccion() throws SQLException {
		SQLException excepcion;
		boolean error;
		
		// Intentamos finalizar la última transacción iniciada
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones está vacía.");
		}
		error = false;
		excepcion = null;
		for(IConexionBD conexion : conexiones) {
			try {
				conexion.commit();
			} catch(Exception ex) {
				try {
					conexion.rollback();
				} catch(Exception ex2) {
				}
				error = true;
				if(conexion instanceof ConexionBDFrontend) {
					excepcion = new SQLException("Error en el acceso a la base de datos principal.", ex);
				} else if(conexion instanceof ProxyServidorRespaldo) {
					excepcion = new SQLException("Error en el acceso a la base de datos secundaria.", ex);
				} else {
					excepcion = new SQLException("Error en el acceso a las bases de datos.", ex);
				}
			}
		}
		if(error) {
			throw excepcion;
		}
	}
	
	public static Object insertar(Object objeto) throws SQLException {
		Object copia;
		
		// Insertamos el objeto en todas las conexiones, y nos quedamos
		// con la copia devuelta por la primera conexión
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones está vacía.");
		}
		copia = null;
		for(IConexionBD conexion : conexiones) {
			try {
				if(copia == null) {
					copia = conexion.insertar(objeto);
				} else {
					conexion.insertar(objeto);
				}
			} catch(Exception ex) {
				if(conexion instanceof ConexionBDFrontend) {
					throw new SQLException("Error en el acceso a la base de datos principal.", ex);
				} else if(conexion instanceof ProxyServidorRespaldo) {
					throw new SQLException("Error en el acceso a la base de datos secundaria.", ex);
				} else {
					throw new SQLException("Error en el acceso a las bases de datos.", ex);
				}
			}
		}
		
		return copia;
	}
	
	public static void actualizar(Object objeto) throws SQLException {
		// Actualizamos el objeto en todas las conexiones
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones está vacía.");
		}
		for(IConexionBD conexion : conexiones) {
			try {
				conexion.actualizar(objeto);
			} catch(Exception ex) {
				if(conexion instanceof ConexionBDFrontend) {
					throw new SQLException("Error en el acceso a la base de datos principal.", ex);
				} else if(conexion instanceof ProxyServidorRespaldo) {
					throw new SQLException("Error en el acceso a la base de datos secundaria.", ex);
				} else {
					throw new SQLException("Error en el acceso a las bases de datos.", ex);
				}
			}
		}
	}
	
	public static void eliminar(Object objeto) throws SQLException {
		// Eliminamos el objeto en todas las conexiones
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones está vacía.");
		}
		for(IConexionBD conexion : conexiones) {
			try {
				conexion.eliminar(objeto);
			} catch(Exception ex) {
				if(conexion instanceof ConexionBDFrontend) {
					throw new SQLException("Error en el acceso a la base de datos principal.", ex);
				} else if(conexion instanceof ProxyServidorRespaldo) {
					throw new SQLException("Error en el acceso a la base de datos secundaria.", ex);
				} else {
					throw new SQLException("Error en el acceso a las bases de datos.", ex);
				}
			}
		}
	}

	public static void borrarCache(Object objeto) throws SQLException {
		// Borramos el objeto de la caché de todas las conexiones
		if(conexiones.size() == 0) {
			throw new SQLException("La lista de conexiones está vacía.");
		}
		for(IConexionBD conexion : conexiones) {
			try {
				// Borramos el objeto
				conexion.borrarCache(objeto);
			} catch(Exception ex) {
				if(conexion instanceof ConexionBDFrontend) {
					throw new SQLException("Error en el acceso a la base de datos principal.", ex);
				} else if(conexion instanceof ProxyServidorRespaldo) {
					throw new SQLException("Error en el acceso a la base de datos secundaria.", ex);
				} else {
					throw new SQLException("Error en el acceso a las bases de datos.", ex);
				}
			}
		}
	}
	
}
