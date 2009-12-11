package persistencia;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import dominio.Beneficiario;
import dominio.Cita;
import dominio.Medico;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;

public class FPCita {
	
	/**
	 * Clase dedicada a consultar y modificar citas en la base de datos.
	 */

	private static final String TABLA_CITAS = "citas";
	private static final String COL_FECHA = "fecha";
	private static final String COL_DURACION = "duracion";
	private static final String COL_VOLANTE = "idVolante";
	private static final String COL_DNI_BENEFICIARIO = "dniBeneficiario";
	private static final String COL_DNI_MEDICO = "dniMedico";
	
	
	public static Vector<Cita> consultarTodo(String dniBeneficiario) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Cita> citas = new Vector<Cita>();
		Date fecha;
		int duracion, idVolante;
		Beneficiario bene = null;
		Medico med = null;
		Cita cita = null;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM "
				+ TABLA_CITAS + " WHERE " + COL_DNI_BENEFICIARIO + " = ?", dniBeneficiario);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Si no se obtienen datos, hay que distinguir si es porque no existe el beneficiario o porque no tiene ninguna cita
		if (datos.getRow() == 0) {
			try {
				bene = FPBeneficiario.consultarPorNIF(dniBeneficiario);
			} catch (BeneficiarioInexistenteException e) {
				throw e;
			} catch (UsuarioIncorrectoException e) {
				throw e;
			} catch (CentroSaludIncorrectoException e) {
				throw e;
			}
			
		} else {
			// Se recorren las filas, para ir guardando todas las citas que pueda tener ese beneficiario
			while (datos.next()){
				fecha = new Date(datos.getTimestamp(COL_FECHA).getTime());
				duracion = datos.getInt(COL_DURACION);
				idVolante = datos.getInt(COL_VOLANTE);
				bene = FPBeneficiario.consultarPorNIF(datos.getString(COL_DNI_BENEFICIARIO));
				med = (Medico) FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
				cita = new Cita(fecha, duracion, bene, med);
				citas.add(cita);
			}
		}

		return citas;
	}
	

	public static Vector<Cita> consultarCitasMedico(String dniMedico) {


	}

	public static void insertar(Cita c) {
		throw new UnsupportedOperationException();
	}

	public static void eliminar(Cita c) {
		throw new UnsupportedOperationException();
	}

}
