package comunicaciones;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import persistencia.ConsultaHibernate;
import persistencia.ComandoSQL;
import persistencia.HibernateSessionFactory;

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
	
	public static void cerrarConexiones() throws SQLException {
		try {
			// Cerramos todas las conexiones con bases de datos
			for(IConexionBD conexion : conexiones) {
				conexion.cerrar();
			}
		} catch(RemoteException ex) {
			throw new SQLException("Error en la conexión con una base de datos remota.", ex);
		}
	}
	
	public static ResultSet consultar(ComandoSQL comando) throws SQLException {
		ResultSet datos;
		
		try {
			// Para hacer una consulta utilizamos la primera conexión
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones está vacía.");
			}
			datos = conexiones.get(0).consultar(comando);
		} catch(RemoteException ex) {
			throw new SQLException("Error en la conexión con una base de datos remota.", ex);
		}
		return datos;
	}
	
	public static List<?> consultarHibernate(ConsultaHibernate comando) throws SQLException {
		List<?> datos;
		Session sesion;
		
		sesion = null;
		try {
			// Para hacer una consulta utilizamos la primera conexión
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones está vacía.");
			}
			sesion = HibernateSessionFactory.getSession();
			sesion.beginTransaction();
			datos = comando.crearQuery(sesion).list();
			sesion.getTransaction().commit();
		} catch(HibernateException ex) {
			sesion.getTransaction().rollback();
			sesion.close();
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
		
		return datos;
	}
	
	public static void ejecutar(ComandoSQL comando) throws SQLException {
		ArrayList<IConexionBD> conexionesUsadas;
		
		try {
			// Para hacer una modificación accedemos a todas las bases de
			// datos, y si alguna falla revertimos los cambios de las anteriores
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones está vacía.");
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
					if(conexion instanceof ConexionBDFrontend) {
						throw new SQLException("Error en el acceso a la base de datos principal.", ex);
					} else if(conexion instanceof ProxyServidorRespaldo) {
						throw new SQLException("Error en el acceso a la base de datos secundaria.", ex);
					} else {
						throw new SQLException("Error en el acceso a las bases de datos.", ex);
					}
				}
			}
			// Aplicamos los cambios en todas las conexiones
			for(IConexionBD conexion : conexiones) {
				conexion.commit();
			}
		} catch(RemoteException ex) {
			throw new SQLException("Error en la conexión con una base de datos remota.", ex);
		}
	}
	
	public static void insertarHibernate(Object objeto) throws SQLException {
		Session sesion;
		
		sesion = null;
		try {
			// Por el momento sólo se utiliza Hibernate con la primera conexión
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones está vacía.");
			}
			// Almacenamos el objeto pasado como parámetro
			sesion = HibernateSessionFactory.getSession();
			sesion.beginTransaction();
			sesion.save(objeto);
			sesion.getTransaction().commit();
		} catch(HibernateException ex) {
			// Si se produce un error, hay que cerrar la sesión
			sesion.getTransaction().rollback();
			sesion.close();
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}
	
	public static void actualizarHibernate(Object objeto) throws SQLException, HibernateException {
		Session sesion;
		
		sesion = null;
		try {
			// Por el momento sólo se utiliza Hibernate con la primera conexión
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones está vacía.");
			}
			// Actualizamos el objeto pasado como parámetro
			sesion = HibernateSessionFactory.getSession();
			sesion.beginTransaction();
			sesion.update(objeto);
			sesion.getTransaction().commit();
		} catch(HibernateException ex) {
			// Si se produce un error, hay que cerrar la sesión
			sesion.getTransaction().rollback();
			sesion.close();
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}
	
	public static void borrarHibernate(Object objeto) throws SQLException, HibernateException {
		Session sesion;
		
		sesion = null;
		try {
			// Por el momento sólo se utiliza Hibernate con la primera conexión
			if(conexiones.size() == 0) {
				throw new SQLException("La lista de conexiones está vacía.");
			}
			// Borramos el objeto pasado como parámetro
			sesion = HibernateSessionFactory.getSession();
			sesion.beginTransaction();
			sesion.delete(objeto);
			sesion.getTransaction().commit();
		} catch(HibernateException ex) {
			// Si se produce un error, hay que cerrar la sesión
			sesion.getTransaction().rollback();
			sesion.close();
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}
	
}
