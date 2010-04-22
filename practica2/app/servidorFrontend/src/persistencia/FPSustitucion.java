package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Medico;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permite consultar e insertar sustituciones de m�dicos en la
 * base de datos.
 */
public class FPSustitucion {

	private static final String TABLA_SUSTITUCIONES = "sustituciones";
	
	private static final String COL_ID = "id";
	private static final String COL_DIA = "dia";
	private static final String COL_HORA_INICIO = "horaInicio";
	private static final String COL_HORA_FINAL = "horaFinal";
	private static final String COL_NIF_MEDICO = "nifMedico";
	private static final String COL_NIF_SUSTITUTO = "nifSustituto";
	
	public static Vector<Sustitucion> consultarPorSustituido(String nifMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Sustitucion> lista;
		Usuario medico, sustituto;
		Sustitucion sustitucion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_SUSTITUCIONES
				+ " WHERE " + COL_NIF_MEDICO + " = ?", nifMedico);
		datos = GestorConexionesBD.consultar(comando);
		
		// Obtenemos los datos del m�dico sustituido
		medico = FPUsuario.consultar(nifMedico);
		if(medico.getRol() != RolesUsuario.M�dico) {
			datos.close();
			throw new UsuarioIncorrectoException("No se pueden consultar las sustituciones del usuario con NIF " + String.valueOf(nifMedico) + " porque no es un m�dico.");
		}
		
		// Devolvemos la lista de sustituciones que tiene el m�dico
		lista = new Vector<Sustitucion>();
		while(datos.next()) {
			sustitucion = new Sustitucion();
			sustitucion.setId(datos.getInt(COL_ID));
			sustitucion.setDia(datos.getDate(COL_DIA));
			sustitucion.setHoraInicio(datos.getInt(COL_HORA_INICIO));
			sustitucion.setHoraFinal(datos.getInt(COL_HORA_FINAL));
			sustitucion.setMedico((Medico)medico);
			sustituto = FPUsuario.consultar(datos.getString(COL_NIF_SUSTITUTO));
			if(sustituto.getRol() != RolesUsuario.M�dico) {
				datos.close();
				throw new UsuarioIncorrectoException("Alguna de las sustituciones del m�dico con NIF " + String.valueOf(nifMedico) + " no tiene asociado un usuario con rol de m�dico.");
			}
			sustitucion.setSustituto((Medico)sustituto);
			lista.add(sustitucion);
		}
		datos.close();

		return lista;
	}
	
	public static Vector<Sustitucion> consultarPorSustituto(String nifMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<Sustitucion> lista;
		Usuario medico, sustituto;
		Sustitucion sustitucion;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_SUSTITUCIONES
				+ " WHERE " + COL_NIF_SUSTITUTO + " = ?", nifMedico);
		datos = GestorConexionesBD.consultar(comando);
		
		// Obtenemos los datos del m�dico sustituto
		sustituto = FPUsuario.consultar(nifMedico);
		if(sustituto.getRol() != RolesUsuario.M�dico) {
			datos.close();
			throw new UsuarioIncorrectoException("No se pueden consultar las sustituciones hechas por el usuario con NIF " + String.valueOf(nifMedico) + " porque no es un m�dico.");
		}
		
		// Devolvemos la lista de sustituciones que va a hacer el m�dico
		lista = new Vector<Sustitucion>();
		while(datos.next()) {
			sustitucion = new Sustitucion();
			sustitucion.setId(datos.getInt(COL_ID));
			sustitucion.setDia(datos.getDate(COL_DIA));
			sustitucion.setHoraInicio(datos.getInt(COL_HORA_INICIO));
			sustitucion.setHoraFinal(datos.getInt(COL_HORA_FINAL));
			medico = FPUsuario.consultar(datos.getString(COL_NIF_MEDICO));
			if(medico.getRol() != RolesUsuario.M�dico) {
				datos.close();
				throw new UsuarioIncorrectoException("Alguna de las sustituciones hechas por el m�dico con NIF " + String.valueOf(nifMedico) + " no tiene asociado un usuario con rol de m�dico.");
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
				+ ", " + COL_NIF_MEDICO + ", " + COL_NIF_SUSTITUTO
				+ ") VALUES (?, ?, ?, ?, ?)",
				sustitucion.getDia(), sustitucion.getHoraInicio(),
				sustitucion.getHoraFinal(), sustitucion.getMedico().getNif(),
				sustitucion.getSustituto().getNif());
		GestorConexionesBD.ejecutar(comando);
		
		// Recuperamos el id autonum�rico asignado a la nueva cita
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		sustitucion.setId(datos.getInt("LAST_INSERT_ID()"));
		datos.close();
	}
	
}
