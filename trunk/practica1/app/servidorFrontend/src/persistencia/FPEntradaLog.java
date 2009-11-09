package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import dominio.EntradaLog;

/**
 * Clase dedicada a consultar y modificar entradas del log en la base de datos.
 */
public class FPEntradaLog {

	private static final String TABLA_LOG = "entradasLog";
	
	private static final String COL_USUARIO = "usuario";
	private static final String COL_FECHA = "fecha";
	private static final String COL_ACCION = "accion";
	private static final String COL_MENSAJE = "mensaje";
	
	public static ArrayList<EntradaLog> consultarLog() throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<EntradaLog> log;
		EntradaLog entrada;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_LOG);
		datos = GestorConexiones.consultar(comando);
		
		// Devolvemos la lista completa de entradas del log
		log = new ArrayList<EntradaLog>();
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
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_LOG + " (" + COL_USUARIO + ", " + COL_FECHA + ", " + COL_ACCION + ", " + COL_MENSAJE + ") "
				                        + "VALUES (?,?,?,?)", entrada.getUsuario(), entrada.getFecha(), entrada.getAccion(), entrada.getMensaje());
		GestorConexiones.ejecutar(comando);
	}
	
}
