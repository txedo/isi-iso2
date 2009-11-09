package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import dominio.CentroSalud;
import excepciones.CentroSaludIncorrectoException;

/**
 * Clase dedicada a consultar y modificar centros de salud.
 */
public class FPCentroSalud {

	private static final String TABLA_CENTROS = "centros";
	private static final String COL_ID = "id";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_DIRECCION = "direccion";
	
	public static CentroSalud consultar(int id) throws SQLException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		CentroSalud centro = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CENTROS + " WHERE " + COL_ID + "=?", id);
		datos = GestorConexiones.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new CentroSaludIncorrectoException("El id del centro de salud no es válido");
		} else {
			// Creamos el centro de salud
			centro = new CentroSalud();
			centro.setId(datos.getInt(COL_ID));
			centro.setNombre(datos.getString(COL_NOMBRE));
			centro.setDireccion(datos.getString(COL_DIRECCION));
		}
		
		return centro;
	}

	public static CentroSalud consultarAleatorio() throws SQLException, CentroSaludIncorrectoException {
		ArrayList<Integer> listaIds;
		ComandoSQL comando;
		ResultSet datos;
		CentroSalud centro = null;
		Random rnd;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT " + COL_ID + " FROM " + TABLA_CENTROS);
		datos = GestorConexiones.consultar(comando);
		
		// Obtenemos los ids de todos los centros
		listaIds = new ArrayList<Integer>();
		while(datos.next()) {
			listaIds.add(datos.getInt(COL_ID));
		}
		if(datos.getRow() == 0) {
			throw new CentroSaludIncorrectoException("No existe ningún centro de salud");
		}
		
		// Devolvemos un centro aleatorio
		rnd = new Random(System.currentTimeMillis());
		centro = consultar(listaIds.get(rnd.nextInt(listaIds.size())));
		
		return centro;
	}

}
