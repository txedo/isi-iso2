package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permite consultar, insertar, modificar y eliminar usuarios
 * de la base de datos.
 */
public class FPUsuario {
	
	private static final String TABLA_USUARIOS = "usuarios";

	private static final String COL_NIF = "nif";
	private static final String COL_LOGIN = "login";
	private static final String COL_PASSWORD = "password";
	private static final String COL_ROL = "rol";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_APELLIDOS = "apellidos";
	private static final String COL_CORREO = "correo";
	private static final String COL_TELEFONO = "telefono";
	private static final String COL_MOVIL = "movil";
	private static final String COL_ID_CENTRO = "idCentro";

	public static Usuario consultar(String nif) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<PeriodoTrabajo> calendario;
		CentroSalud centro;
		Usuario usuario;
		TipoMedico tipo;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS
				+ " WHERE " + COL_NIF + " = ?", nif);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe el usuario
		if(datos.getRow() == 0) {
			datos.close();
			throw new UsuarioIncorrectoException("El usuario con NIF " + nif + " no se encuentra dado de alta en el sistema.");
		} else {
			// Creamos un usuario del tipo adecuado
			switch(RolesUsuario.values()[datos.getInt(COL_ROL)]) {
			case Citador:
				usuario = new Citador();
				break;
			case Administrador:
				usuario = new Administrador();
				break;
			case Medico:
				usuario = new Medico();
				break;
			default:
				datos.close();
				throw new UsuarioIncorrectoException("El tipo del usuario con NIF " + nif + " es inválido.");
			}
			// Establecemos los datos del usuario
			usuario.setNif(datos.getString(COL_NIF));
			usuario.setLogin(datos.getString(COL_LOGIN));
			usuario.setPassword(datos.getString(COL_PASSWORD));			
			usuario.setNombre(datos.getString(COL_NOMBRE));
			usuario.setApellidos(datos.getString(COL_APELLIDOS));
			usuario.setCorreo(datos.getString(COL_CORREO));
			usuario.setTelefono(datos.getString(COL_TELEFONO));
			usuario.setMovil(datos.getString(COL_MOVIL));
			centro = FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO));
			usuario.setCentroSalud(centro);
			// Establecemos datos adicionales de los médicos
			if(usuario.getRol() == RolesUsuario.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarHorario(usuario.getNif());
				((Medico)usuario).setCalendario(calendario);
				tipo = FPTipoMedico.consultar(usuario.getNif());
				((Medico)usuario).setTipoMedico(tipo);
			}
			datos.close();
		}
		
		return usuario;
	}
	
	public static Usuario consultar(String login, String password) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<PeriodoTrabajo> calendario;
		CentroSalud centro;
		Usuario usuario;
		TipoMedico tipo;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS
				+ " WHERE " + COL_LOGIN + " = ? AND " + COL_PASSWORD + " = ?",
				login, password);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe el usuario
		if(datos.getRow() == 0) {
			datos.close();
			throw new UsuarioIncorrectoException("El nombre de usuario o contraseña introducidos no son válidos.");
		} else {
			// Creamos un usuario del tipo adecuado
			switch(RolesUsuario.values()[datos.getInt(COL_ROL)]) {
			case Citador:
				usuario = new Citador();
				break;
			case Administrador:
				usuario = new Administrador();
				break;
			case Medico:
				usuario = new Medico();
				break;
			default:
				datos.close();
				throw new UsuarioIncorrectoException("El tipo del usuario con login " + login + " es inválido.");
			}
			// Establecemos los datos del usuario
			usuario.setNif(datos.getString(COL_NIF));
			usuario.setLogin(datos.getString(COL_LOGIN));
			usuario.setPassword(datos.getString(COL_PASSWORD));
			usuario.setNombre(datos.getString(COL_NOMBRE));
			usuario.setApellidos(datos.getString(COL_APELLIDOS));
			usuario.setCorreo(datos.getString(COL_CORREO));
			usuario.setTelefono(datos.getString(COL_TELEFONO));
			usuario.setMovil(datos.getString(COL_MOVIL));
			centro = FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO));
			usuario.setCentroSalud(centro);
			// Establecemos datos adicionales de los médicos
			if(usuario.getRol() == RolesUsuario.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarHorario(usuario.getNif());
				((Medico)usuario).setCalendario(calendario);
				tipo = FPTipoMedico.consultar(usuario.getNif());
				((Medico)usuario).setTipoMedico(tipo);
			}
			datos.close();
		}
		
		return usuario;
	}
	
	public static Usuario consultarPorLogin(String login) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Usuario usuario;
		String password;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS
				+ " WHERE " + COL_LOGIN + " = ?", login);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe el usuario
		if(datos.getRow() == 0) {
			datos.close();
			throw new UsuarioIncorrectoException("El nombre de usuario introducido no es válido.");
		} else {
			// Obtenemos la contraseña y llamamos al otro método
			password = datos.getString(COL_PASSWORD);
			datos.close();
			usuario = consultar(login, password);
		}
		
		return usuario;
	}
	
	public static void insertar(Usuario usuario) throws SQLException {
		Vector<PeriodoTrabajo> calendario;
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_USUARIOS
				+ " (" + COL_NIF + ", " + COL_LOGIN + ", " + COL_PASSWORD + ", "
				+ COL_ROL + ", " + COL_NOMBRE + ", " + COL_APELLIDOS + ", "
				+ COL_CORREO + ", " + COL_TELEFONO + ", " + COL_MOVIL + ", "
				+ COL_ID_CENTRO + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				usuario.getNif(), usuario.getLogin(), usuario.getPassword(),
				usuario.getRol().ordinal(), usuario.getNombre(),
				usuario.getApellidos(), usuario.getCorreo(), usuario.getTelefono(),
				usuario.getMovil(), usuario.getCentroSalud().getId());
		GestorConexionesBD.ejecutar(comando);
		
		// Insertamos datos adicionales de los médicos
		if(usuario.getRol() == RolesUsuario.Medico) {
			calendario = ((Medico)usuario).getCalendario();
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.insertar(usuario.getNif(), periodo);
			}
			FPTipoMedico.insertar(usuario.getNif(), ((Medico)usuario).getTipoMedico());
		}
	}
	
	public static void modificar(Usuario usuario) throws SQLException {
		Vector<PeriodoTrabajo> calendario;
		ComandoSQL comando;
		
		// Modificamos la base de datos
		// (el NIF no se puede cambiar)
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_USUARIOS + " SET "
				+ COL_LOGIN + " = ?, " + COL_PASSWORD + " = ?, " + COL_ROL + " = ?, "
				+ COL_NOMBRE + " = ?, " + COL_APELLIDOS + " = ?, " + COL_CORREO + " = ?, "
				+ COL_TELEFONO + " = ?, " + COL_MOVIL + " = ?, " + COL_ID_CENTRO
				+ " = ? WHERE " + COL_NIF + " = ?",
				usuario.getLogin(), usuario.getPassword(), usuario.getRol().ordinal(),
				usuario.getNombre(), usuario.getApellidos(), usuario.getCorreo(),
				usuario.getTelefono(), usuario.getMovil(), usuario.getCentroSalud().getId(),
				usuario.getNif());
		GestorConexionesBD.ejecutar(comando);
		
		// Modificamos datos adicionales de los médicos
		if(usuario.getRol() == RolesUsuario.Medico) {
			// Borramos el calendario antiguo del médico y añadimos el nuevo
			calendario = FPPeriodoTrabajo.consultarHorario(usuario.getNif());
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.eliminar(periodo);
			}
			calendario = ((Medico)usuario).getCalendario();
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.insertar(usuario.getNif(), periodo);
			}
			// Borramos el tipo de médico antiguo y añadimos el nuevo
			FPTipoMedico.eliminar(usuario.getNif());
			FPTipoMedico.insertar(usuario.getNif(), ((Medico)usuario).getTipoMedico());
		}
	}
	
	public static void eliminar(Usuario usuario) throws SQLException {
		ComandoSQL comando;
		Vector<PeriodoTrabajo> calendario;
		
		// Borramos datos adicionales de los médicos
		if(usuario.getRol() == RolesUsuario.Medico) {
			calendario = ((Medico)usuario).getCalendario();
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.eliminar(periodo);
			}
			FPTipoMedico.eliminar(usuario.getNif());
		}
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_USUARIOS
				+ " WHERE " + COL_NIF + " = ?", usuario.getNif());
		GestorConexionesBD.ejecutar(comando);
	}
	
}
