package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Direccion;
import excepciones.CentroSaludIncorrectoException;
import excepciones.DireccionIncorrectaException;

/**
 * Clase dedicada a consultar y modificar centros de salud en la base de datos.
 */
public class FPCentroSalud {

	private static final String TABLA_CENTROS = "centros";
	
	private static final String COL_ID = "id";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_DIRECCION = "direccion";
	
	public static CentroSalud consultar(int id) throws SQLException, CentroSaludIncorrectoException, DireccionIncorrectaException {
		ComandoSQL comando;
		ResultSet datos;
		CentroSalud centro = null;
		Direccion dir;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CENTROS + " WHERE " + COL_ID + "=?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new CentroSaludIncorrectoException("No existe ningún centro de salud con el id " + String.valueOf(id) + ".");
		} else {
			// Creamos el centro de salud
			centro = new CentroSalud();
			centro.setId(datos.getInt(COL_ID));
			centro.setNombre(datos.getString(COL_NOMBRE));
			dir = FPDireccion.consultar(datos.getInt(COL_DIRECCION));
			centro.setDireccion(dir);
		}
		
		return centro;
	}

	public static CentroSalud consultarAleatorio() throws SQLException, CentroSaludIncorrectoException, DireccionIncorrectaException {
		ArrayList<Integer> listaIds;
		ComandoSQL comando;
		ResultSet datos;
		CentroSalud centro = null;
		Random rnd;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT " + COL_ID + " FROM " + TABLA_CENTROS);
		datos = GestorConexionesBD.consultar(comando);
		
		// Obtenemos los ids de todos los centros
		listaIds = new ArrayList<Integer>();
		datos.next();
		if(datos.getRow() == 0) {
			throw new CentroSaludIncorrectoException("No existe ningún centro de salud en el sistema.");
		}
		do {
			listaIds.add(datos.getInt(COL_ID));
		} while(datos.next());

		// Devolvemos un centro aleatorio
		rnd = new Random(System.currentTimeMillis());
		centro = consultar(listaIds.get(rnd.nextInt(listaIds.size())));
		
		return centro;
	}
	
	public static void insertar(CentroSalud centro) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		
		// Insertamos primero la direccion del centro (si no existe ya en la base de datos esa dirección)
		try {
			FPDireccion.consultar(centro.getDireccion().getId());
		} catch(DireccionIncorrectaException e) {
			FPDireccion.insertar(centro.getDireccion());
		}
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_CENTROS + " (" + COL_NOMBRE + ", " + COL_DIRECCION + ") VALUES (?, ?)",
                                          centro.getNombre(), centro.getDireccion().getId());
		GestorConexionesBD.ejecutar(comando);
		
		// Cambiamos el id del nuevo centro de salud
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		centro.setId(datos.getInt("LAST_INSERT_ID()"));
	}

}
