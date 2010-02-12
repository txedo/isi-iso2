package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
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
 * de qué tipo son los médicos de la base de datos.
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
		
		// Si no se obtienen datos, es porque no existe el médico
		// o es un usuario con un rol diferente de médico
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El médico con DNI " + dniMedico + " no se encuentra dado de alta en el sistema o no tiene asociado una categoría de médico.");
		} else {
			// Establecemos los datos del tipo de médico
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
				throw new UsuarioIncorrectoException("La categoría del médico con DNI " + dniMedico + " es inválida.");
			}
		}
		
		return tipo;
	}

	public static Vector<String> consultarMedicos(CategoriasMedico tipo) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<String> lista;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT dniMedico FROM " + TABLA_TIPOS_MEDICO
				+ " WHERE " + COL_TIPO + " = ?", tipo.ordinal());
		datos = GestorConexionesBD.consultar(comando);
		
		// Devolvemos la lista de médicos que son del tipo indicado
		lista = new Vector<String>();
		while(datos.next()) {
			lista.add(datos.getString(COL_DNI_MEDICO));
		}

		return lista;
	}
	
	public static String consultarMedicoAleatorio(CategoriasMedico tipo) throws SQLException, UsuarioIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<String> listaDNIs;
		Random rnd;
		String dni;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_TIPOS_MEDICO
				+ " WHERE " + COL_TIPO + " = ?", tipo.ordinal());
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no hay ningún médico
		// en el sistema de la categoría indicada
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("No hay ningún médico de tipo " + tipo.name() + " registrado en el sistema.");
		} else {
			// Obtenemos los DNIs de todos los médicos del tipo indicado
			listaDNIs = new ArrayList<String>();
			do {
				listaDNIs.add(datos.getString(COL_DNI_MEDICO));
			} while(datos.next());
			// Devolvemos un DNI aleatorio
			rnd = new Random(System.currentTimeMillis());
			dni = listaDNIs.get(rnd.nextInt(listaDNIs.size()));
		}
		
		return dni;
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
