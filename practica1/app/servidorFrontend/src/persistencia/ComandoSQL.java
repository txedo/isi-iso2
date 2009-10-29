package persistencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ComandoSQL {

	protected String sentencia;
	protected Object[] parametros;
	
	public ComandoSQL(String sentencia, Object... parametros) {
		this.sentencia = sentencia;
		this.parametros = parametros;
	}

	public String getSentencia() {
		return sentencia;
	}

	public Object[] getParametros() {
		return parametros;
	}
	
	public abstract Statement crearStatement(Connection bd) throws SQLException;

}
