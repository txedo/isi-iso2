package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.CitaNoValidaException;
import excepciones.DireccionIncorrectaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase dedicada a consultar y modificar citas en la base de datos.
 */
public class FPCita {

	private static final String TABLA_CITAS = "citas";
	
	private static final String COL_ID = "id";
	private static final String COL_FECHA = "fecha";
	private static final String COL_DURACION = "duracion";
	private static final String COL_NIF_BENEFICIARIO = "nifBeneficiario";
	private static final String COL_DNI_MEDICO = "dniMedico";
	
	public static Cita consultar(long id) throws SQLException, CitaNoValidaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, DireccionIncorrectaException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario bene;
		Cita cita;
		GregorianCalendar fechaAux;
		Timestamp fechaTimeStamp;
		Date fecha;
		int duracion;
		Medico med;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_CITAS + " WHERE " + COL_ID + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe la cita 
		if(datos.getRow() == 0) {
			throw new CitaNoValidaException("No existe ninguna cita con el id " + String.valueOf(id) + ".");
		} else {
			// Establecemos los datos de la cita
			fechaTimeStamp = datos.getTimestamp(COL_FECHA);
			fechaAux = new GregorianCalendar(fechaTimeStamp.getYear()+1900, fechaTimeStamp.getMonth(), fechaTimeStamp.getDate(), fechaTimeStamp.getHours(), fechaTimeStamp.getMinutes());
			fecha = fechaAux.getTime();
			duracion = datos.getInt(COL_DURACION);
			med = (Medico) FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
			bene = (Beneficiario) FPBeneficiario.consultarPorNIF(datos.getString(COL_NIF_BENEFICIARIO));
			cita = new Cita(fecha, duracion, bene, med);
		}
		return cita;
	}
	
	public static Vector<Cita> consultarPorBeneficiario(String dniBeneficiario) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, DireccionIncorrectaException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Cita> citas = new Vector<Cita>();
		Date fecha;
		int duracion;
		Beneficiario bene = null;
		Medico med = null;
		Cita cita = null;
		GregorianCalendar fechaAux;
		Timestamp fechaTimeStamp;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM "
				+ TABLA_CITAS + " WHERE " + COL_NIF_BENEFICIARIO + " = ?", dniBeneficiario);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Se obtiene el beneficiario
		bene = FPBeneficiario.consultarPorNIF(dniBeneficiario);
		
		// Si no se obtienen datos, y lo anterior no produjo excepcion, se devuelve el vector vacio porque no hay ctas
		if (datos.getRow() > 0) {
			// Se recorren las filas, para ir guardando todas las citas que pueda tener ese beneficiario
			do {
				fechaTimeStamp = datos.getTimestamp(COL_FECHA);
				fechaAux = new GregorianCalendar(fechaTimeStamp.getYear()+1900, fechaTimeStamp.getMonth(), fechaTimeStamp.getDate(), fechaTimeStamp.getHours(), fechaTimeStamp.getMinutes());
				fecha = fechaAux.getTime();
				duracion = datos.getInt(COL_DURACION);
				//idVolante = datos.getInt(COL_VOLANTE);
				med = (Medico) FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
				cita = new Cita(fecha, duracion, bene, med);
				citas.add(cita);
			}while(datos.next());
		}

		return citas;
	}
	

	public static Vector<Cita> consultarPorMedico(String dniMedico) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, DireccionIncorrectaException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Cita> citas = new Vector<Cita>();
		Date fecha;
		int duracion;
		Beneficiario bene = null;
		Medico med = null;
		Cita cita = null;
		GregorianCalendar fechaAux;
		Timestamp fechaTimeStamp;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM "
				+ TABLA_CITAS + " WHERE " + COL_DNI_MEDICO + " = ?", dniMedico);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Se obtiene el medico
		med = (Medico) FPUsuario.consultar(dniMedico);
		
		// Si no se obtienen datos, y lo anterior no produjo excepcion, se devuelve el vector vacio porque no hay ctas
		if (datos.getRow() > 0) {
			// Se recorren las filas, para ir guardando todas las citas que pueda tener ese médico
			do {
				// Se recupera la fecha de la base de datos como TimeStamp y se convierte a GregorianCalendar, componente a componente
				// Se tiene que hacer asi para no perder la hora, que es importante para comparar luego las horas de las citas
				// Al año se suma 1900
				fechaTimeStamp = datos.getTimestamp(COL_FECHA);
				fechaAux = new GregorianCalendar(fechaTimeStamp.getYear()+1900, fechaTimeStamp.getMonth(), fechaTimeStamp.getDate(), fechaTimeStamp.getHours(), fechaTimeStamp.getMinutes());
				fecha = fechaAux.getTime();
				duracion = datos.getInt(COL_DURACION);
				bene = FPBeneficiario.consultarPorNIF(datos.getString(COL_NIF_BENEFICIARIO));
				cita = new Cita(fecha, duracion, bene, med);
				citas.add(cita);
			}while(datos.next());
		}

		return citas;
	}
	
	// Este metodo comprueba si existe una cita dada en la base de datos
	public static boolean existe(Cita c) throws SQLException{
		ComandoSQL comando;
		ResultSet datos;
		boolean existe = true;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM "
				+ TABLA_CITAS + " WHERE " + COL_FECHA + " = ? AND " + COL_DURACION + " = ? AND " + COL_DNI_MEDICO + " = ? AND " + COL_NIF_BENEFICIARIO + " = ?", c.getFechaYHora(), c.getDuracion(), c.getMedico().getDni(), c.getBeneficiario().getNif());
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque la cita no existe
		if (datos.getRow() == 0) 
			existe = false;
		
		return existe;
	}

	public static void insertar(Cita cita) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_CITAS
				+ " (" + COL_FECHA + ", " + COL_DURACION + ", " + COL_NIF_BENEFICIARIO
				+ ", " + COL_DNI_MEDICO + ") VALUES (?, ?, ?, ?)",
				cita.getFechaYHora(), cita.getDuracion(), cita.getBeneficiario().getNif(), cita.getMedico().getDni());
		GestorConexionesBD.ejecutar(comando);
		
		// Cambiamos el id de la nueva cita
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		cita.setId(datos.getInt("LAST_INSERT_ID()"));
	}

	public static void eliminar(Cita cita) throws SQLException {
		ComandoSQL comando;

		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_CITAS
				+ " WHERE " + COL_FECHA + " = ? AND " + COL_DURACION + " = ? AND "
				+ COL_NIF_BENEFICIARIO + " = ? AND " + COL_DNI_MEDICO + " = ?",
				cita.getFechaYHora(), cita.getDuracion(), cita.getBeneficiario().getNif(), cita.getMedico().getDni());
		GestorConexionesBD.ejecutar(comando);
	}

}
