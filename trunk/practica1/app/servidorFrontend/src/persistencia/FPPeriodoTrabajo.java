package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.PeriodoTrabajo;

/**
 * Clase que permite consultar, insertar, modificar y eliminar períodos
 * de trabajo de los médicos de la base de datos.
 */
public class FPPeriodoTrabajo {

	private static final String TABLA_PERIODOS = "periodosTrabajo";
	
	private static final String COL_ID = "id";
	private static final String COL_DNI_MEDICO = "dniMedico";
	private static final String COL_HORA_INICIO = "horaInicio";
	private static final String COL_HORA_FINAL = "horaFinal";
	private static final String COL_DIA = "dia";
	
	public static Vector<PeriodoTrabajo> consultarHorario(String dniMedico) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<PeriodoTrabajo> lista;
		PeriodoTrabajo periodo;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_PERIODOS
				+ " WHERE " + COL_DNI_MEDICO + " = ?", dniMedico);
		datos = GestorConexionesBD.consultar(comando);
		
		// Devolvemos la lista de períodos de trabajo del médico
		lista = new Vector<PeriodoTrabajo>();
		while(datos.next()) {
			periodo = new PeriodoTrabajo();
			periodo.setId(datos.getInt(COL_ID));
			periodo.setHoraInicio(datos.getInt(COL_HORA_INICIO));
			periodo.setHoraFinal(datos.getInt(COL_HORA_FINAL));
			periodo.setDia(DiaSemana.values()[datos.getInt(COL_DIA)]);
			lista.add(periodo);
		}
		datos.close();

		return lista;
	}
	
	public static void insertar(String dniMedico, PeriodoTrabajo periodo) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_PERIODOS
				+ " (" + COL_DNI_MEDICO + ", " + COL_HORA_INICIO + ", "
				+ COL_HORA_FINAL + ", " + COL_DIA + ") VALUES (?, ?, ?, ?)",
				dniMedico, periodo.getHoraInicio(), periodo.getHoraFinal(),
				periodo.getDia().ordinal());
		GestorConexionesBD.ejecutar(comando);
		
		// Recuperamos el id autonumérico asignado al nuevo período de trabajo
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		periodo.setId(datos.getInt("LAST_INSERT_ID()"));
		datos.close();
	}
	
	public static void modificar(PeriodoTrabajo periodo) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_PERIODOS + " SET "
				+ COL_HORA_INICIO + " = ?, "+ COL_HORA_FINAL + " = ?, "
				+ COL_DIA + " = ? WHERE " + COL_ID + " = ?",
				periodo.getHoraInicio(), periodo.getHoraFinal(),
				periodo.getDia().ordinal(), periodo.getId());
		GestorConexionesBD.ejecutar(comando);
	}
	
	public static void eliminar(PeriodoTrabajo periodo) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_PERIODOS +
				" WHERE " + COL_ID + " = " + periodo.getId());
		GestorConexionesBD.ejecutar(comando);
	}
	
}
