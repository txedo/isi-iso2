package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ComandoSQLPrepared extends ComandoSQL {

	public ComandoSQLPrepared(String sentencia, Object... parametros) {
		super(sentencia, parametros);
	}
	
	public Statement crearStatement(Connection bd) throws SQLException {
		PreparedStatement prepared;
		int i;
		
		prepared = bd.prepareStatement(sentencia);
		for(i = 0; i < parametros.length; i++) {
			prepared.setObject(i + 1, parametros[i]);
		}
		return prepared;
	}

}
