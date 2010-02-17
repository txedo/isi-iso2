package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.CentroSalud;

/**
 * Clase con métodos estáticos auxiliares para acceder a la base de datos.
 */
public class FuncionesPersistencia {

	private static final String TABLA_USUARIOS = "usuarios";
	private static final String TABLA_BENEFICIARIOS = "beneficiarios";
	private static final String TABLA_TIPOS_MEDICO = "tiposmedico";

	private static final String COL_USUARIOS_NIF = "nif";
	private static final String COL_BENEFICIARIOS_NIF = "nif";
	private static final String COL_DNI_MEDICO = "dniMedico";
	private static final String COL_TIPO_MEDICO = "tipo";
	private static final String COL_CENTRO = "idCentro";
	
	public static boolean existeNIF(String nif) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		boolean existe;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT " + COL_USUARIOS_NIF + " FROM "
				+ TABLA_USUARIOS + " WHERE " + COL_USUARIOS_NIF + " = ? UNION SELECT "
				+ COL_BENEFICIARIOS_NIF + " FROM " + TABLA_BENEFICIARIOS + " WHERE "
				+ COL_BENEFICIARIOS_NIF + " = ?", nif, nif);
		datos = GestorConexionesBD.consultar(comando);
		
		// Si no hay datos es porque no hay ningún usuario/beneficiario
		// que tenga el NIF buscado
		existe = (datos.next());
		
		return existe;
	}
	
	public static String getMedicoTipoAsociadoCentro (CategoriasMedico tipoMedico, CentroSalud centro) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		String nif = "";
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT " + COL_DNI_MEDICO + " FROM "
				+ TABLA_TIPOS_MEDICO + ", " + TABLA_USUARIOS + " WHERE " + 
				COL_DNI_MEDICO + " = " + COL_USUARIOS_NIF + " AND " + COL_TIPO_MEDICO + " = ?" +
				" AND " + COL_CENTRO + " = ? ORDER BY RAND() LIMIT 1", tipoMedico.ordinal(), centro.getId()); 
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Devolvemos el nif de ese tipo de médico, obtenido aleatoriamente
		if (datos.getRow() != 0)
			nif = datos.getString(COL_DNI_MEDICO);
		
		return nif;
	}
}
