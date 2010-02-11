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
	
	private static final String COL_NIF_BENEFICIARIO = "nifBeneficiario";
	private static final String COL_DOMICILIO = "domicilio";
	private static final String COL_NUMERO = "numero";
	private static final String COL_PISO = "piso";
	private static final String COL_PUERTA = "puerta";
	private static final String COL_CIUDAD = "ciudad";
	private static final String COL_PROVINCIA = "provincia";
	private static final String COL_CP = "cp";
	
	public static Direccion consultar(String nifBeneficiario) throws SQLException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Direccion direccion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_DIRECCIONES
				+ " WHERE " + COL_NIF_BENEFICIARIO + " = ?", nifBeneficiario);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe la dirección
		if(datos.getRow() == 0) {
			throw new DireccionInexistenteException("El beneficiario con NIF " + nifBeneficiario + " no tiene registrada una dirección.");
		} else {		
			// Establecemos los datos de la dirección
			direccion = new Direccion();
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
	
	public static void insertar(String nifBeneficiario, Direccion direccion) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_DIRECCIONES
				+ " (" + COL_NIF_BENEFICIARIO + ", " + COL_DOMICILIO
				+ ", "	+ COL_NUMERO + ", " + COL_PISO + ", " + COL_PUERTA
				+ ", " + COL_CIUDAD + ", " + COL_PROVINCIA + ", " + COL_CP
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
				nifBeneficiario, direccion.getDomicilio(), direccion.getNumero(),
				direccion.getPiso(), direccion.getPuerta(), direccion.getCiudad(),
				direccion.getProvincia(), direccion.getCP());
		GestorConexionesBD.ejecutar(comando);
	}
	
	public static void modificar(String nifBeneficiario, Direccion direccion) throws SQLException {
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_DIRECCIONES + " SET "
				+ COL_DOMICILIO + " = ?, " + COL_NUMERO + " = ?, " + COL_PISO + " = ?, "
				+ COL_PUERTA + " = ?, " + COL_CIUDAD + " = ?, " + COL_PROVINCIA + " = ?, "
				+ COL_CP + " = ? WHERE " + COL_NIF_BENEFICIARIO + " = ?", 
				direccion.getDomicilio(), direccion.getNumero(), direccion.getPiso(),
				direccion.getPuerta(), direccion.getCiudad(), direccion.getProvincia(),
				direccion.getCP(), nifBeneficiario);
		GestorConexionesBD.ejecutar(comando);
	}
	
	public static void eliminar(String nifBeneficiario) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_DIRECCIONES
				+ " WHERE " + COL_NIF_BENEFICIARIO + " = ?", nifBeneficiario);
		GestorConexionesBD.ejecutar(comando);
	}
	
}
