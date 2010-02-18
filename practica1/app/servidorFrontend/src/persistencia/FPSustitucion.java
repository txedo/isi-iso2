package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Medico;
import dominio.conocimiento.RolesUsuarios;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permite consultar e insertar sustituciones de médicos en la
 * base de datos.
 */
public class FPSustitucion {

	private static final String TABLA_SUSTITUCIONES = "sustituciones";
	
	private static final String COL_ID = "id";
	private static final String COL_DIA = "dia";
	private static final String COL_HORA_INICIO = "horaInicio";
	private static final String COL_HORA_FINAL = "horaFinal";
	private static final String COL_DNI_MEDICO = "dniMedico";
	private static final String COL_DNI_SUSTITUTO = "dniSustituto";
	
	public static Vector<Sustitucion> consultarPorSustituido(String dniMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Sustitucion> lista;
		Usuario medico, sustituto;
		Sustitucion sustitucion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_SUSTITUCIONES
				+ " WHERE " + COL_DNI_MEDICO + " = ?", dniMedico);
		datos = GestorConexionesBD.consultar(comando);
		
		// Obtenemos los datos del médico sustituido
		medico = FPUsuario.consultar(dniMedico);
		if(medico.getRol() != RolesUsuarios.Medico) {
			datos.close();
			throw new UsuarioIncorrectoException("No se pueden consultar las sustituciones del usuario con DNI " + String.valueOf(dniMedico) + " porque no es un médico.");
		}
		
		// Devolvemos la lista de sustituciones que tiene el médico
		lista = new Vector<Sustitucion>();
		while(datos.next()) {
			sustitucion = new Sustitucion();
			sustitucion.setId(datos.getInt(COL_ID));
			sustitucion.setDia(datos.getDate(COL_DIA));
			sustitucion.setHoraInicio(datos.getInt(COL_HORA_INICIO));
			sustitucion.setHoraFinal(datos.getInt(COL_HORA_FINAL));
			sustitucion.setMedico((Medico)medico);
			sustituto = FPUsuario.consultar(datos.getString(COL_DNI_SUSTITUTO));
			if(sustituto.getRol() != RolesUsuarios.Medico) {
				datos.close();
				throw new UsuarioIncorrectoException("Alguna de las sustituciones del médico con DNI " + String.valueOf(dniMedico) + " no tiene asociado un usuario con rol de médico.");
			}
			sustitucion.setSustituto((Medico)sustituto);
			lista.add(sustitucion);
		}
		datos.close();

		return lista;
	}
	
	public static Vector<Sustitucion> consultarPorSustituto(String dniMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Sustitucion> lista;
		Usuario medico, sustituto;
		Sustitucion sustitucion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_SUSTITUCIONES
				+ " WHERE " + COL_DNI_SUSTITUTO + " = ?", dniMedico);
		datos = GestorConexionesBD.consultar(comando);
		
		// Obtenemos los datos del médico sustituto
		sustituto = FPUsuario.consultar(dniMedico);
		if(sustituto.getRol() != RolesUsuarios.Medico) {
			datos.close();
			throw new UsuarioIncorrectoException("No se pueden consultar las sustituciones hechas por el usuario con DNI " + String.valueOf(dniMedico) + " porque no es un médico.");
		}
		
		// Devolvemos la lista de sustituciones que va a hacer el médico
		lista = new Vector<Sustitucion>();
		while(datos.next()) {
			sustitucion = new Sustitucion();
			sustitucion.setId(datos.getInt(COL_ID));
			sustitucion.setDia(datos.getDate(COL_DIA));
			sustitucion.setHoraInicio(datos.getInt(COL_HORA_INICIO));
			sustitucion.setHoraFinal(datos.getInt(COL_HORA_FINAL));
			medico = FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
			if(medico.getRol() != RolesUsuarios.Medico) {
				datos.close();
				throw new UsuarioIncorrectoException("Alguna de las sustituciones hechas por el médico con DNI " + String.valueOf(dniMedico) + " no tiene asociado un usuario con rol de médico.");
			}
			sustitucion.setMedico((Medico)medico);
			sustitucion.setSustituto((Medico)sustituto);
			lista.add(sustitucion);
		}
		datos.close();
		
		return lista;
	}
	
	public static void insertar(Sustitucion sustitucion) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_SUSTITUCIONES
				+ " (" + COL_DIA + ", " + COL_HORA_INICIO + ", " + COL_HORA_FINAL
				+ ", " + COL_DNI_MEDICO + ", " + COL_DNI_SUSTITUTO
				+ ") VALUES (?, ?, ?, ?, ?)",
				sustitucion.getDia(), sustitucion.getHoraInicio(),
				sustitucion.getHoraFinal(), sustitucion.getMedico().getDni(),
				sustitucion.getSustituto().getDni());
		GestorConexionesBD.ejecutar(comando);
		
		// Recuperamos el id autonumérico asignado a la nueva cita
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		sustitucion.setId(datos.getInt("LAST_INSERT_ID()"));
		datos.close();
	}
	
}
