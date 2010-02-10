package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Direccion;
import excepciones.DireccionInexistenteException;

/**
 * Clase que permite consultar, insertar, modificar y eliminar direcciones
 * de beneficiarios en la base de datos.
 */
public class FPDireccion {

	private static final String TABLA_DIRECCIONES = "direcciones";
	
	private static final String COL_ID = "id";
	private static final String COL_DOMICILIO = "domicilio";
	private static final String COL_NUMERO = "numero";
	private static final String COL_PISO = "piso";
	private static final String COL_PUERTA = "puerta";
	private static final String COL_CIUDAD = "ciudad";
	private static final String COL_PROVINCIA = "provincia";
	private static final String COL_CP = "cp";
	
	public static Direccion consultar(long id) throws SQLException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Direccion direccion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_DIRECCIONES
				+ " WHERE " + COL_ID + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe la dirección
		if(datos.getRow() == 0) {
			throw new DireccionInexistenteException("No existe ninguna dirección con el id " + String.valueOf(id) + ".");
		} else {		
			// Establecemos los datos de la dirección
			direccion = new Direccion();
			direccion.setId(datos.getInt(COL_ID));
			direccion.setDomicilio(datos.getString(COL_DOMICILIO));
			direccion.setNumero(datos.getString(COL_NUMERO));
			direccion.setPiso(datos.getString(COL_PISO));
			direccion.setPuerta(datos.getString(COL_PUERTA));
			direccion.setCiudad(datos.getString(COL_CIUDAD));
			direccion.setProvincia(datos.getString(COL_PROVINCIA));
			direccion.setCP(datos.getInt(COL_CP));
		}
		return direccion;
	}
	
	public static void insertar(Direccion direccion) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_DIRECCIONES
				+ " (" + COL_DOMICILIO + ", " + COL_NUMERO + ", " + COL_PISO
				+ ", " + COL_PUERTA + ", " + COL_CIUDAD + ", " + COL_PROVINCIA
				+ ", " + COL_CP + ") VALUES (?, ?, ?, ?, ?, ?, ?)",
				direccion.getDomicilio(), direccion.getNumero(), direccion.getPiso(),
				direccion.getPuerta(), direccion.getCiudad(), direccion.getProvincia(),
				direccion.getCP());
		GestorConexionesBD.ejecutar(comando);
		
		// Recuperamos el id autonumérico asignado a la nueva dirección
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		direccion.setId(datos.getInt("LAST_INSERT_ID()"));
	}
	
	public static void modificar(Direccion direccion) throws SQLException {
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_DIRECCIONES + " SET "
				+ COL_DOMICILIO + " = ?, " + COL_NUMERO + " = ?, " + COL_PISO + " = ?, "
				+ COL_PUERTA + " = ?, " + COL_CIUDAD + " = ?, " + COL_PROVINCIA + " = ?, "
				+ COL_CP + " = ? WHERE " + COL_ID + " = ?", 
				direccion.getDomicilio(), direccion.getNumero(), direccion.getPiso(),
				direccion.getPuerta(), direccion.getCiudad(), direccion.getProvincia(),
				direccion.getCP(), direccion.getId());
		GestorConexionesBD.ejecutar(comando);
	}
	
	public static void eliminar(Direccion direccion) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_DIRECCIONES
				+ " WHERE " + COL_ID + " = ?", direccion.getId());
		GestorConexionesBD.ejecutar(comando);
	}
	
}
