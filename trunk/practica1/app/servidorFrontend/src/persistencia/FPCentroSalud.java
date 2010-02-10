package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Direccion;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;

/**
 * Clase que permite consultar e insertar centros de salud en la base de datos.
 */
public class FPCentroSalud {

	private static final String TABLA_CENTROS = "centros";
	
	private static final String COL_ID = "id";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_DIRECCION = "direccion";
	
	public static CentroSalud consultar(int id) throws SQLException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		CentroSalud centro;
		Direccion direccion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CENTROS
				+ " WHERE " + COL_ID + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el centro no existe
		if(datos.getRow() == 0) {
			throw new CentroSaludInexistenteException("No existe ningún centro de salud con el id " + String.valueOf(id) + ".");
		} else {
			// Establecemos los datos del centro de salud
			centro = new CentroSalud();
			centro.setId(datos.getInt(COL_ID));
			centro.setNombre(datos.getString(COL_NOMBRE));
			direccion = FPDireccion.consultar(datos.getInt(COL_DIRECCION));
			centro.setDireccion(direccion);
		}
		
		return centro;
	}

	public static CentroSalud consultarAleatorio() throws SQLException, CentroSaludInexistenteException, DireccionInexistenteException {
		ArrayList<Integer> listaIds;
		ComandoSQL comando;
		ResultSet datos;
		CentroSalud centro;
		Random rnd;
		int id;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT " + COL_ID + " FROM " + TABLA_CENTROS);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no hay ningún centro
		if(datos.getRow() == 0) {
			throw new CentroSaludInexistenteException("No existe ningún centro de salud en el sistema.");
		} else {
			// Obtenemos los ids de todos los centros registrados
			listaIds = new ArrayList<Integer>();
			do {
				listaIds.add(datos.getInt(COL_ID));
			} while(datos.next());
			// Devolvemos un centro aleatorio
			rnd = new Random(System.currentTimeMillis());
			id = rnd.nextInt(listaIds.size());
			centro = consultar(listaIds.get(id));
		}
		
		return centro;
	}
	
	public static void insertar(CentroSalud centro) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		
		// Insertamos primero la dirección del centro
		FPDireccion.insertar(centro.getDireccion());
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_CENTROS
				+ " (" + COL_NOMBRE + ", " + COL_DIRECCION + ") VALUES (?, ?)",
				centro.getNombre(), centro.getDireccion().getId());
		GestorConexionesBD.ejecutar(comando);
		
		// Recuperamos el id autonumérico asignado al nuevo centro de salud
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		centro.setId(datos.getInt("LAST_INSERT_ID()"));
	}

}
