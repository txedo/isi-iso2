package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import comunicaciones.GestorConexionesBD;
import dominio.Administrador;
import dominio.CentroSalud;
import dominio.Citador;
import dominio.Especialista;
import dominio.Medico;
import dominio.Cabecera;
import dominio.Pediatra;
import dominio.PeriodoTrabajo;
import dominio.TipoMedico;
import dominio.Usuario;
import dominio.Roles;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase dedicada a consultar y modificar usuarios en la base de datos.
 */
public class FPUsuario {
	
	private static final String TABLA_USUARIOS = "usuarios";

	private static final String COL_DNI = "dni";
	private static final String COL_LOGIN = "login";
	private static final String COL_PASSWORD = "password";
	private static final String COL_ROL = "rol";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_APELLIDOS = "apellidos";
	private static final String COL_ID_CENTRO = "id_centro";

	public static Usuario consultar(String dni) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<PeriodoTrabajo> calendario;
		CentroSalud centro;
		Usuario usuario = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS + " WHERE " + COL_DNI + " = ?", dni);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("No existe ningún usuario con el DNI " + dni);
		} else {
			// Creamos un usuario del tipo adecuado
			switch(Roles.values()[datos.getInt(COL_ROL)]) {
			case Citador:
				usuario = new Citador();
				break;
			case Administrador:
				usuario = new Administrador();
				break;
			case Medico:
				usuario = new Medico();
				break;
			}
			// Buscamos el centro del usuario
			centro = FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO));
			// Establecemos los datos del usuario
			usuario.setDni(datos.getString(COL_DNI));
			usuario.setLogin(datos.getString(COL_LOGIN));
			usuario.setPassword(datos.getString(COL_PASSWORD));			
			usuario.setNombre(datos.getString(COL_NOMBRE));
			usuario.setApellidos(datos.getString(COL_APELLIDOS));
			usuario.setCentroSalud(centro);
			// Establecemos datos adicionales de los usuarios
			if(usuario.getRol() == Roles.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarCalendario(usuario.getDni());
				((Medico)usuario).setCalendario(calendario);
			}
		}
		
		return usuario;
	}
	
	public static Usuario consultar(String login, String password) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<PeriodoTrabajo> calendario;
		CentroSalud centro;
		Usuario usuario = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS + " WHERE " + COL_LOGIN + " = ? AND " + COL_PASSWORD + " = ?", login, password);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El nombre de usuario o contraseña introducidos no son válidos");
		} else {	
			// Creamos un usuario del tipo adecuado
			switch(Roles.values()[datos.getInt(COL_ROL)]) {
			case Citador:
				usuario = new Citador();
				break;
			case Administrador:
				usuario = new Administrador();
				break;
			case Medico:
				usuario = new Medico();
				break;
			}
			// Buscamos el centro del usuario
			centro = FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO));
			// Establecemos los datos del usuario
			usuario.setDni(datos.getString(COL_DNI));
			usuario.setLogin(datos.getString(COL_LOGIN));
			usuario.setPassword(datos.getString(COL_PASSWORD));
			usuario.setNombre(datos.getString(COL_NOMBRE));
			usuario.setApellidos(datos.getString(COL_APELLIDOS));
			usuario.setCentroSalud(centro);
			// Establecemos datos adicionales de los usuarios
			if(usuario.getRol() == Roles.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarCalendario(usuario.getDni());
				((Medico)usuario).setCalendario(calendario);
			}
		}
		
		return usuario;
	}

	public static Usuario consultarAleatorio(Roles rol) throws SQLException,  UsuarioIncorrectoException, CentroSaludIncorrectoException{
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<PeriodoTrabajo> calendario;
		CentroSalud centro;
		Usuario usuario = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS + " WHERE " + COL_ROL + " = ?", rol.ordinal());
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("No hay ningun usuario con el rol " + rol + " en la base de datos");
		} else {	
			// Creamos un usuario del tipo adecuado
			switch(rol) {
			case Citador:
				usuario = new Citador();
				break;
			case Administrador:
				usuario = new Administrador();
				break;
			case Medico:
				usuario = new Medico();
				break;
			}
			// Buscamos el centro del usuario
			centro = FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO));
			// Establecemos los datos del usuario
			usuario.setDni(datos.getString(COL_DNI));
			usuario.setLogin(datos.getString(COL_LOGIN));
			usuario.setPassword(datos.getString(COL_PASSWORD));
			usuario.setNombre(datos.getString(COL_NOMBRE));
			usuario.setApellidos(datos.getString(COL_APELLIDOS));
			usuario.setCentroSalud(centro);
			// Establecemos datos adicionales de los usuarios
			if(usuario.getRol() == Roles.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarCalendario(usuario.getDni());
				((Medico)usuario).setCalendario(calendario);
			}
		}
		
		return usuario;
	}
	
	public static void insertar(Usuario usuario) throws SQLException {
		ComandoSQL comando;
		ArrayList<PeriodoTrabajo> calendario;

		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_USUARIOS + " (" + COL_DNI + ", " + COL_LOGIN + ", " + COL_PASSWORD + ", " + COL_ROL + ", " + COL_NOMBRE + ", " + COL_APELLIDOS + ", " + COL_ID_CENTRO + ") VALUES (?, ?, ?, ?, ?, ?, ?)",
		                                  usuario.getDni(), usuario.getLogin(), usuario.getPassword(), usuario.getRol().ordinal(), usuario.getNombre(), usuario.getApellidos(), usuario.getCentroSalud().getId());
		GestorConexionesBD.ejecutar(comando);
		
		// Si el usuario es un médico, insertamos su calendario y el tipo de medico que es
		if(usuario.getRol() == Roles.Medico) {
			calendario = ((Medico)usuario).getCalendario();
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.insertar(usuario.getDni(), periodo);
			}
			FPTipoMedico.insertar(usuario);
		}
	}
	
	public static void modificar(Usuario usuario) throws SQLException {
		ComandoSQL comando;
		ArrayList<PeriodoTrabajo> calendario;
		
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_USUARIOS + " SET " + COL_LOGIN + " = ?, "+ COL_PASSWORD + " = ?, " + COL_ROL + " = ?, " + COL_NOMBRE + " = ?, " + COL_APELLIDOS + " = ?, " + COL_ID_CENTRO + " = ? WHERE " + COL_DNI + " = ?",
		                                  usuario.getLogin(), usuario.getPassword(), usuario.getRol().ordinal(), usuario.getNombre(), usuario.getApellidos(), usuario.getCentroSalud().getId(), usuario.getDni());
		GestorConexionesBD.ejecutar(comando);
		
		// Si el usuario es un médico, borramos su calendario
		// antiguo e insertamos el nuevo
		if(usuario.getRol() == Roles.Medico) {
			calendario = FPPeriodoTrabajo.consultarCalendario(usuario.getDni());
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.eliminar(periodo);
			}
			calendario = ((Medico)usuario).getCalendario();
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.insertar(usuario.getDni(), periodo);
			}
		}
	}
	
	public static void eliminar(Usuario usuario) throws SQLException {
		ComandoSQL comando;
		ArrayList<PeriodoTrabajo> calendario;
		
		// Si el usuario es un médico, borramos su calendario
		//Al eliminar un usuario, si es medico, tb debe eliminarse de la tabla "TiposMedico"
		if(usuario.getRol() == Roles.Medico) {
			calendario = ((Medico)usuario).getCalendario();
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.eliminar(periodo);
			}
			FPTipoMedico.eliminar(usuario);
			
		}
		
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_USUARIOS + " WHERE " + COL_DNI + " = ?" , usuario.getDni());
		GestorConexionesBD.ejecutar(comando);
	}
	
}
