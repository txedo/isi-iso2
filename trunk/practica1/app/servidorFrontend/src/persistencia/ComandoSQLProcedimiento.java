package persistencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase que representa una consulta o modificación sobre una base de
 * datos escrita en forma de procedimiento almacenado.
 */
public class ComandoSQLProcedimiento extends ComandoSQL {

	private static final long serialVersionUID = -6249425775295737777L;

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
