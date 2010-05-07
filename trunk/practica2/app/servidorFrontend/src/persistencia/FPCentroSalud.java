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

	private static final String CLASE_CENTROSALUD = "CentroSalud";
	
	private static final String ATRIB_ID = "id";
	
	public static CentroSalud consultar(int id) throws SQLException, CentroSaludInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		CentroSalud centro;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CENTROSALUD + " WHERE "
				 + ATRIB_ID + " = ?", id);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el centro no existe
		if(resultados.size() == 0) {
			throw new CentroSaludInexistenteException("No existe ning�n centro de salud con el id " + String.valueOf(id) + ".");
		} else {
			// Recuperamos el centro de salud le�do
			centro = (CentroSalud)((CentroSalud)resultados.get(0)).clone();
			// Borramos los objetos le�dos de la cach�
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return centro;
	}

	public static CentroSalud consultarAleatorio() throws SQLException, CentroSaludInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		CentroSalud centro;
		Random rnd;

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CENTROSALUD);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque no hay ning�n centro
		if(resultados.size() == 0) {
			throw new CentroSaludInexistenteException("No existe ning�n centro de salud en el sistema.");
		} else {
			// Devolvemos un centro aleatorio
			rnd = new Random(System.currentTimeMillis());
			centro = (CentroSalud)((CentroSalud)resultados.get(rnd.nextInt(resultados.size()))).clone();
			// Borramos los objetos le�dos de la cach�
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return centro;
	}
	
	public static Vector<CentroSalud> consultarTodo() throws SQLException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<CentroSalud> lista;

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CENTROSALUD);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Devolvemos la lista de centros de salud
		lista = new Vector<CentroSalud>();
		for(Object centro : resultados) {
			lista.add((CentroSalud)((CentroSalud)centro).clone());
		}
		
		// Borramos los objetos le�dos de la cach�
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}

		return lista;
	}
	
	public static void insertar(CentroSalud centro) throws SQLException {
		CentroSalud centroNuevo;
		
		// Modificamos la base de datos y copiamos el id asignado
		try {
			GestorConexionesBD.iniciarTransaccion();
			centroNuevo = (CentroSalud)GestorConexionesBD.insertar(centro.clone());
			centro.setId(centroNuevo.getId());
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}

}
