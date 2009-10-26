package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface IConexion {

	public ResultSet consultar(ComandoSQL sql) throws SQLException;
	
	public void ejecutar(ComandoSQL sql) throws SQLException;
	
//	public void commit();
	
//	public void rollback();
	
}
