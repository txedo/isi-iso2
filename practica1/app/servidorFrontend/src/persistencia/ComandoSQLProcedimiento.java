package persistencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase que representa una consulta o modificaci�n sobre una base de
 * datos escrita como un procedimiento almacenado.
 */
public class ComandoSQLProcedimiento extends ComandoSQL {

	public ComandoSQLProcedimiento(String sentencia, Object... parametros) {
		super(sentencia, parametros);
	}
	
	public PreparedStatement crearStatement(Connection bd) throws SQLException {
		CallableStatement callable;
		int i;
		
		// Devolvemos un procedimiento almacenado
		// para la base de datos especificada
		callable = bd.prepareCall(sentencia);
		for(i = 0; i < parametros.length; i++) {
			callable.setObject(i + 1, parametros[i]);
		}
		return callable;
	}

}