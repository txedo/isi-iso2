package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.CentroSalud;
import excepciones.CentroSaludInexistenteException;

/**
 * Clase que permite consultar e insertar centros de salud en la base de datos.
 */
public class FPCentroSalud {

	private static final String TABLA_CENTROS = "centros";
	
	private static final String COL_ID = "id";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_DIRECCION = "direccion";
	
	public static CentroSalud consultar(int id) throws SQLException, CentroSaludInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		CentroSalud centro;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CENTROS
				+ " WHERE " + COL_ID + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el centro no existe
		if(datos.getRow() == 0) {
			datos.close();
			throw new CentroSaludInexistenteException("No existe ningún centro de salud con el id " + String.valueOf(id) + ".");
		} else {
			// Establecemos los datos del centro de salud
			centro = new CentroSalud();
			centro.setId(datos.getInt(COL_ID));
			centro.setNombre(datos.getString(COL_NOMBRE));
			centro.setDireccion(datos.getString(COL_DIRECCION));
			datos.close();
		}
		
		return centro;
	}

	public static CentroSalud consultarAleatorio() throws SQLException, CentroSaludInexistenteException {
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
			datos.close();
			throw new CentroSaludInexistenteException("No existe ningún centro de salud en el sistema.");
		} else {
			// Obtenemos los ids de todos los centros registrados
			listaIds = new ArrayList<Integer>();
			do {
				listaIds.add(datos.getInt(COL_ID));
			} while(datos.next());
			datos.close();
			// Devolvemos un centro aleatorio
			rnd = new Random(System.currentTimeMillis());
			id = rnd.nextInt(listaIds.size());
			centro = consultar(listaIds.get(id));
		}
		
		return centro;
	}
	
	public static Vector<CentroSalud> consultarTodo() throws SQLException {
		Vector<CentroSalud> lista;
		ComandoSQL comando;
		ResultSet datos;
		CentroSalud centro;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CENTROS);
		datos = GestorConexionesBD.consultar(comando);
		
		// Devolvemos la lista de centros de salud
		lista = new Vector<CentroSalud>();
		while(datos.next()) {
			centro = new CentroSalud();
			centro.setId(datos.getInt(COL_ID));
			centro.setNombre(datos.getString(COL_NOMBRE));
			centro.setDireccion(datos.getString(COL_DIRECCION));
			lista.add(centro);
		}
		datos.close();
		
		return lista;
	}
	
	public static void insertar(CentroSalud centro) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
				
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_CENTROS
				+ " (" + COL_NOMBRE + ", " + COL_DIRECCION + ") VALUES (?, ?)",
				centro.getNombre(), centro.getDireccion());
		GestorConexionesBD.ejecutar(comando);
		
		// Recuperamos el id autonumérico asignado al nuevo centro de salud
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		centro.setId(datos.getInt("LAST_INSERT_ID()"));
		datos.close();
	}

}
