package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.EntradaLog;

/**
 * Clase que permite consultar e insertar entradas del log en la base de datos.
 */
public class FPEntradaLog {

	private static final String TABLA_ENTRADAS_LOG = "entradasLog";
	
	private static final String COL_USUARIO = "usuario";
	private static final String COL_FECHA = "fecha";
	private static final String COL_ACCION = "accion";
	private static final String COL_MENSAJE = "mensaje";
	
	public static Vector<EntradaLog> consultarLog() throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<EntradaLog> log;
		EntradaLog entrada;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_ENTRADAS_LOG);
		datos = GestorConexionesBD.consultar(comando);
		
		// Devolvemos la lista completa de entradas del log
		log = new Vector<EntradaLog>();
		while(datos.next()) {
			entrada = new EntradaLog();
			entrada.setUsuario(datos.getString(COL_USUARIO));
			entrada.setFecha(datos.getTimestamp(COL_FECHA));
			entrada.setAccion(datos.getString(COL_ACCION));
			entrada.setMensaje(datos.getString(COL_MENSAJE));
			log.add(entrada);
		}
		
		return log;
	}

	public static void insertar(EntradaLog entrada) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_ENTRADAS_LOG
				+ " (" + COL_USUARIO + ", " + COL_FECHA + ", " + COL_ACCION
				+ ", " + COL_MENSAJE + ") VALUES (?, ?, ?, ?)",
				entrada.getUsuario(), entrada.getFecha(),
				entrada.getAccion(), entrada.getMensaje());
		GestorConexionesBD.ejecutar(comando);
	}
	
}
