package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.TipoMedico;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permite consultar, insertar y eliminar objetos que indican
 * de qu� tipo son los m�dicos de la base de datos.
 */
public class FPTipoMedico {
	
	private static final String TABLA_TIPOS_MEDICO = "tiposmedico";
	
	private static final String COL_DNI_MEDICO = "dniMedico";
	private static final String COL_TIPO = "tipo";
	private static final String COL_ESPECIALIDAD = "especialidad";
	
	public static TipoMedico consultar(String dniMedico) throws SQLException, UsuarioIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		TipoMedico tipo;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_TIPOS_MEDICO
				+ " WHERE " + COL_DNI_MEDICO + " = ?", dniMedico);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe el m�dico
		// o es un usuario con un rol diferente de m�dico
		if(datos.getRow() == 0) {
			datos.close();
			throw new UsuarioIncorrectoException("El m�dico con DNI " + dniMedico + " no se encuentra dado de alta en el sistema o no tiene asociado una categor�a de m�dico.");
		} else {
			// Establecemos los datos del tipo de m�dico
			switch(CategoriasMedico.values()[datos.getInt(COL_TIPO)]) {
			case Cabecera:
				tipo = new Cabecera();
				break;
			case Especialista:
				tipo = new Especialista(datos.getString(COL_ESPECIALIDAD));
				break;
			case Pediatra:
				tipo = new Pediatra();
				break;
			default:
				datos.close();
				throw new UsuarioIncorrectoException("La categor�a del m�dico con DNI " + dniMedico + " es inv�lida.");
			}
		}
		
		return tipo;
	}
	
	public static Vector<String> consultarMedicos(TipoMedico tipo) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<String> lista;
		
		// Consultamos la base de datos
		if(tipo.getCategoria() == CategoriasMedico.Especialista) {
			comando = new ComandoSQLSentencia("SELECT " + COL_DNI_MEDICO + " FROM "
					+ TABLA_TIPOS_MEDICO + " WHERE " + COL_TIPO + " = ? AND "
					+ COL_ESPECIALIDAD + " = ?", tipo.getCategoria().ordinal(),
					((Especialista)tipo).getEspecialidad());
		} else {
			comando = new ComandoSQLSentencia("SELECT " + COL_DNI_MEDICO + " FROM "
					+ TABLA_TIPOS_MEDICO + " WHERE " + COL_TIPO + " = ?",
					tipo.getCategoria().ordinal());
		}
		datos = GestorConexionesBD.consultar(comando);
		
		// Devolvemos la lista de m�dicos que son del tipo indicado
		lista = new Vector<String>();
		while(datos.next()) {
			lista.add(datos.getString(COL_DNI_MEDICO));
		}
		datos.close();

		return lista;
	}

	public static void insertar(String dniMedico, TipoMedico tipo) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		if(tipo.getCategoria() == CategoriasMedico.Especialista) {
			comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_TIPOS_MEDICO
					+ "(" + COL_DNI_MEDICO + ", " + COL_TIPO + ", " + COL_ESPECIALIDAD
					+ ") VALUES (?, ?, ?)", dniMedico, tipo.getCategoria().ordinal(),
					((Especialista)tipo).getEspecialidad());
		} else {
			comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_TIPOS_MEDICO
					+ "(" + COL_DNI_MEDICO + ", " + COL_TIPO + ") VALUES (?, ?)",
					dniMedico, tipo.getCategoria().ordinal());
		}
		GestorConexionesBD.ejecutar(comando);
	}
	
	public static void eliminar(String dniMedico) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_TIPOS_MEDICO
				+ " WHERE " + COL_DNI_MEDICO + " = ?", dniMedico);
		GestorConexionesBD.ejecutar(comando);
	}

}
