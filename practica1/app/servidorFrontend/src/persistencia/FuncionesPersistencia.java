package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

/**
 * Clase con métodos estáticos auxiliares para acceder a la base de datos.
 */
public class FuncionesPersistencia {

	private static final String TABLA_USUARIOS = "usuarios";
	private static final String TABLA_BENEFICIARIOS = "beneficiarios";

	private static final String COL_USUARIOS_NIF = "nif";
	private static final String COL_BENEFICIARIOS_NIF = "nif";
	private static final String COL_DNI_MEDICO = "dniMedico";
	
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

	public static Vector<String> getBeneficiariosMedico(String dniMedico) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<String> nifsBeneficiarios;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT " + COL_BENEFICIARIOS_NIF + " FROM "
				+ TABLA_BENEFICIARIOS + " WHERE " + COL_DNI_MEDICO + " = ? ", dniMedico);
		datos = GestorConexionesBD.consultar(comando);
		
		
		nifsBeneficiarios = new Vector<String>();
		while (datos.next()) {
			nifsBeneficiarios.add(datos.getString(COL_BENEFICIARIOS_NIF));
		}
		return nifsBeneficiarios;
	}
}
