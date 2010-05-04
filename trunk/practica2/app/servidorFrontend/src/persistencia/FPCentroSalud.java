package persistencia;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.CentroSalud;
import excepciones.CentroSaludInexistenteException;

/**
 * Clase que permite consultar e insertar centros de salud en la base de datos
 * utilizando Hibernate.
 */
public class FPCentroSalud {

	private static final String CLASE_CENTROS = "CentroSalud";
	
	private static final String ATRIB_ID = "id";
	
	public static CentroSalud consultar(int id) throws SQLException, CentroSaludInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		CentroSalud centro;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CENTROS + " WHERE "
				 + ATRIB_ID + " = ?", id);
		resultados = GestorConexionesBD.consultarHibernate(consulta);
		
		// Si no se obtienen datos, es porque el centro no existe
		if(resultados.size() == 0) {
			throw new CentroSaludInexistenteException("No existe ningún centro de salud con el id " + String.valueOf(id) + ".");
		} else {
			// Recuperamos el centro de salud leído
			centro = (CentroSalud)resultados.get(0);
		}
		
		return centro;
	}

	public static CentroSalud consultarAleatorio() throws SQLException, CentroSaludInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		CentroSalud centro;
		Random rnd;

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CENTROS);
		resultados = GestorConexionesBD.consultarHibernate(consulta);
		
		// Si no se obtienen datos, es porque no hay ningún centro
		if(resultados.size() == 0) {
			throw new CentroSaludInexistenteException("No existe ningún centro de salud en el sistema.");
		} else {
			// Devolvemos un centro aleatorio
			rnd = new Random(System.currentTimeMillis());
			centro = (CentroSalud)resultados.get(rnd.nextInt(resultados.size()));
		}
		
		return centro;
	}
	
	public static Vector<CentroSalud> consultarTodo() throws SQLException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<CentroSalud> lista;

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CENTROS);
		resultados = GestorConexionesBD.consultarHibernate(consulta);
		
		// Devolvemos la lista de centros de salud
		lista = new Vector<CentroSalud>();
		for(Object centro : resultados) {
			lista.add((CentroSalud)centro);
		}
		
		return lista;
	}
	
	public static void insertar(CentroSalud centro) throws SQLException {
		// Modificamos la base de datos (automáticamente se
		// establece el id autonumérico asignado al centro)
		GestorConexionesBD.insertarHibernate(centro);
	}

}
