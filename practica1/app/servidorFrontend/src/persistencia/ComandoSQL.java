package persistencia;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase abstracta que representa una consulta o modificación sobre una
 * base de datos.
 */
public abstract class ComandoSQL implements Serializable {

	private static final long serialVersionUID = -3054901238912047586L;

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
	
	public abstract PreparedStatement crearStatement(Connection bd) throws SQLException;

}
