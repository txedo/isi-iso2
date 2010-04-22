package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Medico;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permite consultar, insertar y eliminar citas de la base de datos.
 */
public class FPCita {

	private static final String TABLA_CITAS = "citas";
	
	private static final String COL_ID = "id";
	private static final String COL_FECHA = "fecha";
	private static final String COL_DURACION = "duracion";
	private static final String COL_NIF_BENEFICIARIO = "nifBeneficiario";
	private static final String COL_NIF_MEDICO = "nifMedico";
	
	public static Cita consultar(long id) throws SQLException, CitaNoValidaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario beneficiario;
		Usuario medico;
		Cita cita;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CITAS
				+ " WHERE " + COL_ID + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe la cita
		if(datos.getRow() == 0) {
			datos.close();
			throw new CitaNoValidaException("No existe ninguna cita con el id " + String.valueOf(id) + ".");
		} else {
			// Establecemos los datos de la cita
			cita = new Cita();
			cita.setId(datos.getInt(COL_ID));
			cita.setFechaYHora(new Date(datos.getTimestamp(COL_FECHA).getTime()));
			cita.setDuracion(datos.getInt(COL_DURACION));
			beneficiario = FPBeneficiario.consultarPorNIF(datos.getString(COL_NIF_BENEFICIARIO));
			cita.setBeneficiario(beneficiario);
			medico = FPUsuario.consultar(datos.getString(COL_NIF_MEDICO));
			if(medico.getRol() != RolesUsuario.Médico) {
				datos.close();
				throw new UsuarioIncorrectoException("La cita con id " + String.valueOf(id) + " no tiene asociado un usuario con rol de médico.");
			}
			cita.setMedico((Medico)medico);
			datos.close();
		}
		
		return cita;
	}
	
	public static Cita consultar(Date fechaYHora, long duracion, String nifMedico, String nifBeneficiario) throws SQLException, CitaNoValidaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario beneficiario;
		Usuario medico;
		Cita cita;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CITAS
				+ " WHERE " + COL_FECHA + " = ? AND " + COL_DURACION + " = ? AND "
				+ COL_NIF_MEDICO + " = ? AND " + COL_NIF_BENEFICIARIO + " = ?",
				fechaYHora, duracion, nifMedico, nifBeneficiario);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe la cita 
		if(datos.getRow() == 0) {
			datos.close();
			throw new CitaNoValidaException("No existe ninguna cita con los datos indicados.");
		} else {
			// Establecemos los datos de la cita
			cita = new Cita();
			cita.setId(datos.getInt(COL_ID));
			cita.setFechaYHora(new Date(datos.getTimestamp(COL_FECHA).getTime()));
			cita.setDuracion(datos.getInt(COL_DURACION));
			beneficiario = FPBeneficiario.consultarPorNIF(datos.getString(COL_NIF_BENEFICIARIO));
			cita.setBeneficiario(beneficiario);
			medico = FPUsuario.consultar(datos.getString(COL_NIF_MEDICO));
			if(medico.getRol() != RolesUsuario.Médico) {
				datos.close();
				throw new UsuarioIncorrectoException("La cita con los datos indicados no tiene asociado un usuario con rol de médico.");
			}
			cita.setMedico((Medico)medico);
			datos.close();
		}
		
		return cita;
	}
	
	public static Vector<Cita> consultarPorBeneficiario(String nifBeneficiario) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Cita> citas;
		Beneficiario beneficiario;
		Usuario medico;
		Cita cita;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CITAS
				+ " WHERE " + COL_NIF_BENEFICIARIO + " = ? ORDER BY " + COL_FECHA
				+ " ASC", nifBeneficiario);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Obtenemos los datos del beneficiario
		beneficiario = FPBeneficiario.consultarPorNIF(nifBeneficiario);
		
		// Si no se obtienen datos, es porque el beneficiario no tiene citas,
		// en ese caso no lanzamos un error y devolvemos una lista vacía
		citas = new Vector<Cita>();
		if(datos.getRow() > 0) {
			// Recorremos todas las filas de la consulta
			do {
				// Establecemos los datos de la cita
				cita = new Cita();
				cita.setId(datos.getInt(COL_ID));
				cita.setFechaYHora(new Date(datos.getTimestamp(COL_FECHA).getTime()));
				cita.setDuracion(datos.getInt(COL_DURACION));
				cita.setBeneficiario(beneficiario);
				medico = FPUsuario.consultar(datos.getString(COL_NIF_MEDICO));
				if(medico.getRol() != RolesUsuario.Médico) {
					datos.close();
					throw new UsuarioIncorrectoException("Alguna de las citas del beneficiario con NIF " + nifBeneficiario + " no tiene asociado un usuario con rol de médico.");
				}
				cita.setMedico((Medico)medico);
				citas.add(cita);
			} while(datos.next());
			datos.close();
		}

		return citas;
	}

	public static Vector<Cita> consultarPorMedico(String nifMedico) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Cita> citas;
		Beneficiario beneficiario;
		Usuario medico;
		Cita cita;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CITAS
				+ " WHERE " + COL_NIF_MEDICO + " = ? ORDER BY " + COL_FECHA
				+ " ASC", nifMedico);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Obtenemos los datos del médico
		medico = FPUsuario.consultar(nifMedico);
		if(medico.getRol() != RolesUsuario.Médico) {
			datos.close();
			throw new UsuarioIncorrectoException("No se pueden consultar las citas del usuario con NIF " + String.valueOf(nifMedico) + " porque no es un médico.");
		}
		
		// Si no se obtienen datos, es porque el médico no tiene citas,
		// en ese caso no lanzamos un error y devolvemos una lista vacía
		citas = new Vector<Cita>();
		if(datos.getRow() > 0) {
			// Recorremos todas las filas de la consulta
			do {
				// Establecemos los datos de la cita
				cita = new Cita();
				cita.setId(datos.getInt(COL_ID));
				cita.setFechaYHora(new Date(datos.getTimestamp(COL_FECHA).getTime()));
				cita.setDuracion(datos.getInt(COL_DURACION));
				beneficiario = FPBeneficiario.consultarPorNIF(datos.getString(COL_NIF_BENEFICIARIO));
				cita.setBeneficiario(beneficiario);
				cita.setMedico((Medico)medico);
				citas.add(cita);
			} while(datos.next());
			datos.close();
		}

		return citas;
	}

	public static void insertar(Cita cita) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_CITAS
				+ " (" + COL_FECHA + ", " + COL_DURACION + ", " + COL_NIF_BENEFICIARIO
				+ ", " + COL_NIF_MEDICO + ") VALUES (?, ?, ?, ?)",
				cita.getFechaYHora(), cita.getDuracion(),
				cita.getBeneficiario().getNif(), cita.getMedico().getNif());
		GestorConexionesBD.ejecutar(comando);
		
		// Recuperamos el id autonumérico asignado a la nueva cita
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		cita.setId(datos.getInt("LAST_INSERT_ID()"));
		datos.close();
	}

	public static void eliminar(Cita cita) throws SQLException {
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_CITAS
				+ " WHERE " + COL_ID + " = ?", cita.getId());
		GestorConexionesBD.ejecutar(comando);
	}

}
