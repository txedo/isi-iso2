package persistencia;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.EntradaLog;

/**
 * Clase que permite consultar e insertar entradas del log en la base de datos
 * utilizando Hibernate.
 */
public class FPEntradaLog {

	private static final String CLASE_ENTRADALOG = "EntradaLog";
	
	public static Vector<EntradaLog> consultarLog() throws SQLException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<EntradaLog> log;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_ENTRADALOG);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Devolvemos la lista completa de entradas del log
		log = new Vector<EntradaLog>();
		for(Object entrada : resultados) {
			log.add((EntradaLog)((EntradaLog)entrada).clone());
		}
		
		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}

		return log;
	}

	public static void insertar(EntradaLog entrada) throws SQLException {
		// Modificamos la base de datos
		try {
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.insertar(entrada.clone());
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}
	
}
