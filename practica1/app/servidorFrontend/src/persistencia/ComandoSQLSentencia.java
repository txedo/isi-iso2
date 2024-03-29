package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase que representa una consulta o modificación sobre una base de
 * datos escrita en forma de sentencia SQL.
 */
public class ComandoSQLSentencia extends ComandoSQL {

	private static final long serialVersionUID = 3132897363533160582L;

	public ComandoSQLSentencia(String sentencia, Object... parametros) {
		super(sentencia, parametros);
	}
	
	public PreparedStatement crearStatement(Connection bd) throws SQLException {
		PreparedStatement prepared;
		int i;
		
		// Devolvemos una sentencia SQL para
		// la base de datos especificada
		prepared = bd.prepareStatement(sentencia);
		for(i = 0; i < parametros.length; i++) {
			prepared.setObject(i + 1, parametros[i]);
		}
		return prepared;
	}

}
