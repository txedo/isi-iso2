package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.Direccion;
import excepciones.DireccionIncorrectaException;

public class FPDireccion {

	private static final String TABLA_DIRECCION = "direcciones";
	private static final String COL_ID = "id";
	private static final String COL_DOMICILIO = "domicilio";
	private static final String COL_NUMERO = "numero";
	private static final String COL_PISO = "piso";
	private static final String COL_PUERTA = "puerta";
	private static final String COL_CIUDAD = "ciudad";
	private static final String COL_PROVINCIA = "provincia";
	private static final String COL_CP = "cp";
	
	public static Direccion consultar (int id) throws SQLException, DireccionIncorrectaException {
		ComandoSQL comando;
		ResultSet datos;
		Direccion dir = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_DIRECCION + " WHERE " + COL_ID + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque esa dirección no existe
		if(datos.getRow() == 0) {
			throw new DireccionIncorrectaException("No existe la dirección que se intenta buscar, cuyo identificador es el " + id);
		} else {		
			// Establecemos la direccion
			dir = new Direccion();
			dir.setDomicilio(datos.getString(COL_DOMICILIO));
			dir.setNumero(datos.getString(COL_NUMERO));
			dir.setPiso(datos.getString(COL_PISO));
			dir.setPuerta(datos.getString(COL_PUERTA));
			dir.setCiudad(datos.getString(COL_CIUDAD));
			dir.setProvincia(datos.getString(COL_PROVINCIA));
			dir.setCp(datos.getInt(COL_CP));
			dir.setId(datos.getInt(COL_ID));
		}
		return dir;
	}
	
	public static void insertar (Direccion dir) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_DIRECCION
			+ " (" + COL_DOMICILIO + ", " + COL_NUMERO + ", " + COL_PISO + ", " + 
			COL_PUERTA + ", " + COL_CIUDAD + ", " + COL_PROVINCIA + ", " + COL_CP + ") VALUES (?, ?, ?, ?, ?, ?, ?)",
			dir.getDomicilio(), dir.getNumero(), dir.getPiso(), dir.getPuerta(), dir.getCiudad(), dir.getProvincia(), dir.getCp());
		GestorConexionesBD.ejecutar(comando);
		
		// Cambiamos el id de la nueva direccion insertada
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		dir.setId(datos.getInt("LAST_INSERT_ID()"));
	}
	
	public static void modificar (Direccion dir) throws SQLException {
		ComandoSQL comando;
		
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_DIRECCION + " SET " +
			COL_DOMICILIO + "=?, " + COL_NUMERO + "=?, " + COL_PISO + "=?, " + 
			COL_PUERTA + "=?, " + COL_CIUDAD + "=?, " + COL_PROVINCIA + "=?, " + COL_CP + "=? WHERE " + COL_ID + " =?", 
			dir.getDomicilio(), dir.getNumero(), dir.getPiso(), dir.getPuerta(), dir.getCiudad(), dir.getProvincia(), dir.getCp(), dir.getId());
		GestorConexionesBD.ejecutar(comando);
	}
	
	public static void eliminar (Direccion dir) throws SQLException {
		ComandoSQL comando;
		
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_DIRECCION + " WHERE " + COL_ID + " = " + dir.getId());
		GestorConexionesBD.ejecutar(comando);
	}
}
