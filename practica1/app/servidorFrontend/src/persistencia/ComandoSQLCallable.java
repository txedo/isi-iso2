package persistencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ComandoSQLCallable extends ComandoSQL {

	public ComandoSQLCallable(String sentencia, Object... parametros) {
		super(sentencia, parametros);
	}
	
	public Statement crearStatement(Connection bd) throws SQLException {
		CallableStatement callable;
		int i;
		
		callable = bd.prepareCall(sentencia);
		for(i = 0; i < parametros.length; i++) {
			callable.setObject(i + 1, parametros[i]);
		}
		return callable;
	}

}
