package comunicaciones;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import persistencia.ConsultaHibernate;
import persistencia.HibernateSessionFactory;

/**
 * Clase intermedia para acceder a la base de datos principal utilizando
 * Hibernate.
 */
public class ConexionBDFrontend implements IConexionBD {

	private String ip;
	private int puerto;
	
	public ConexionBDFrontend() {	 
	}
	
	public void setIP(String ip) {
		this.ip = ip;
		actualizarURL();
	}
	
	public void setPuerto(int puerto) {
		this.puerto = puerto;
		actualizarURL();
	}
	
	@SuppressWarnings("deprecation")
	public boolean probarConexion() {
		Session sesion;
		boolean ok;
		
		try {
			// Creamos una sesión de Hibernate y vemos si la
			// conexión JDBC creada internamente es válida
			sesion = HibernateSessionFactory.getSession();
			ok = sesion.connection().isValid(1000);
		} catch(SQLException ex) {
			ok = false;
		} catch(HibernateException ex) {
			ok = false;
		}
		return ok;
	}
	
	private void actualizarURL() {
		HibernateSessionFactory.setDatabaseURL("jdbc:mysql://" + ip + ":" + String.valueOf(puerto) + "/bdssca");
	}
	
	// Métodos de acceso a la base de datos
	
	public List<?> consultar(ConsultaHibernate consulta) throws RemoteException, SQLException {
		List<?> datosLeidos;
		
		try {
			HibernateSessionFactory.getSession().beginTransaction();
			datosLeidos = consulta.crearQuery(HibernateSessionFactory.getSession()).list();
		} catch(HibernateException ex) {
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}

		return datosLeidos;
	}

	public void iniciarTransaccion() throws RemoteException, SQLException {
		try {
			HibernateSessionFactory.getSession().beginTransaction();
		} catch(HibernateException ex) {
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}

	public Object insertar(Object objeto) throws RemoteException, SQLException {
		try {
			HibernateSessionFactory.getSession().save(objeto);
		} catch(HibernateException ex) {
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
		return objeto;
	}

	public void actualizar(Object objeto) throws RemoteException, SQLException {
		try {
			HibernateSessionFactory.getSession().update(objeto);
		} catch(HibernateException ex) {
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}

	public void eliminar(Object objeto) throws RemoteException, SQLException {
		try {
			HibernateSessionFactory.getSession().delete(objeto);
		} catch(HibernateException ex) {
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}

	public void borrarCache(Object objeto) throws RemoteException, SQLException {
		try {
			HibernateSessionFactory.getSession().evict(objeto);
		} catch(HibernateException ex) {
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}

	public void commit() throws RemoteException, SQLException {
		try {
			HibernateSessionFactory.getSession().getTransaction().commit();
		} catch(HibernateException ex) {
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}

	public void rollback() throws RemoteException, SQLException {
		try {
			HibernateSessionFactory.getSession().getTransaction().rollback();
			HibernateSessionFactory.closeSession();
		} catch(HibernateException ex) {
			throw new SQLException(ex.getLocalizedMessage(), ex);
		}
	}

}
