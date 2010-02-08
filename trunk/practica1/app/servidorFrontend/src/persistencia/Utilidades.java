package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import comunicaciones.GestorConexionesBD;

public class Utilidades {

	private static final String TABLA_USUARIOS = "usuarios";
	private static final String TABLA_BENEFICIARIOS = "beneficiarios";
	
	private static final String COL_DNI = "nif";
	
	public static ArrayList<String> getDNIs() throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<String> dnis = new ArrayList<String>();
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT " + COL_DNI + " FROM " + TABLA_USUARIOS + " UNION SELECT " + COL_DNI + " FROM " + TABLA_BENEFICIARIOS); 
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		if(datos.getRow() != 0) {
			do {
				dnis.add(datos.getString(COL_DNI));
			} while(datos.next());
		}
		return dnis;
	}
	
}
