package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Sustitucion;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase dedicada a consultar y modificar sustituciones de médicos en
 * la base de datos.
 */
public class FPSustitucion {

	private static final String TABLA_SUSTITUCIONES = "sustituciones";
	
	private static final String COL_ID = "id";
	private static final String COL_DIA = "dia";
	private static final String COL_HORA_INICIO = "horaInicio";
	private static final String COL_HORA_FINAL = "horaFinal";
	private static final String COL_DNI_MEDICO = "dniMedico";
	private static final String COL_DNI_SUSTITUTO = "dniSustituto";
	
	public static ArrayList<Sustitucion> consultarMedico(String dniMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Medico medico, sustituto;
		ArrayList<Sustitucion> lista;
		Sustitucion sustitucion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_SUSTITUCIONES + " WHERE " + COL_DNI_MEDICO + " = ?", dniMedico);
		datos = GestorConexionesBD.consultar(comando);
		
		// Devolvemos la lista de días que el médico va a ser sustituido
		lista = new ArrayList<Sustitucion>();
		while(datos.next()) {
			sustitucion = new Sustitucion();
			medico = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
			sustituto = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_SUSTITUTO));
			sustitucion.setId(datos.getInt(COL_ID));
			sustitucion.setDia(datos.getDate(COL_DIA));
			sustitucion.setHoraInicio(datos.getInt(COL_HORA_INICIO));
			sustitucion.setHoraFinal(datos.getInt(COL_HORA_FINAL));
			sustitucion.setMedico(medico);
			sustitucion.setSustituto(sustituto);
			lista.add(sustitucion);
		}
		
		return lista;
	}
	
	public static ArrayList<Sustitucion> consultarSustituto(String dniSustituto) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Medico medico, sustituto;
		ArrayList<Sustitucion> lista;
		Sustitucion sustitucion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_SUSTITUCIONES + " WHERE " + COL_DNI_SUSTITUTO + " = ?", dniSustituto);
		datos = GestorConexionesBD.consultar(comando);
		
		// Devolvemos la lista de días que el médico va a hacer una sustitución
		lista = new ArrayList<Sustitucion>();
		while(datos.next()) {
			sustitucion = new Sustitucion();
			medico = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
			sustituto = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_SUSTITUTO));
			sustitucion.setId(datos.getInt(COL_ID));
			sustitucion.setDia(datos.getDate(COL_DIA));
			sustitucion.setHoraInicio(datos.getInt(COL_HORA_INICIO));
			sustitucion.setHoraFinal(datos.getInt(COL_HORA_FINAL));
			sustitucion.setMedico(medico);
			sustitucion.setSustituto(sustituto);
			lista.add(sustitucion);
		}
		
		return lista;
	}
	
	public static void insertar(Sustitucion sustitucion) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_SUSTITUCIONES + " (" + COL_DIA + ", " + COL_HORA_INICIO + ", " + COL_HORA_FINAL + ", " + COL_DNI_MEDICO + ", " + COL_DNI_SUSTITUTO + ") VALUES (?, ?, ?, ?, ?)",
		                                  sustitucion.getDia(), sustitucion.getHoraInicio(), sustitucion.getHoraFinal(), sustitucion.getMedico().getDni(), sustitucion.getSustituto().getDni());
		GestorConexionesBD.ejecutar(comando);
		
		// Cambiamos el id de la nueva sustitución
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		sustitucion.setId(datos.getInt("LAST_INSERT_ID()"));
	}
	
}
