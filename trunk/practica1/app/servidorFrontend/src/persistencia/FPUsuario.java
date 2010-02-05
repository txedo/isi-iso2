package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.RolesUsuarios;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
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
	private static final String COL_ID_CENTRO = "idCentro";

	public static Usuario consultar(String dni) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<PeriodoTrabajo> calendario;
		CentroSalud centro;
		Usuario usuario = null;
		TipoMedico tipo;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS + " WHERE " + COL_DNI + " = ?", dni);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El usuario con DNI " + dni + " no se encuentra dado de alta en el sistema.");
		} else {
			// Creamos un usuario del tipo adecuado
			switch(RolesUsuarios.values()[datos.getInt(COL_ROL)]) {
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
			// Establecemos datos adicionales de los médicos
			if(usuario.getRol() == RolesUsuarios.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarCalendario(usuario.getDni());
				((Medico)usuario).setCalendario(calendario);
				tipo = FPTipoMedico.consultar(((Medico)usuario).getDni());
				((Medico)usuario).setTipoMedico(tipo);
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
		TipoMedico tipo;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS + " WHERE " + COL_LOGIN + " = ? AND " + COL_PASSWORD + " = PASSWORD(?)", login, password);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El nombre de usuario o contraseña introducidos no son válidos.");
		} else {	
			// Creamos un usuario del tipo adecuado
			switch(RolesUsuarios.values()[datos.getInt(COL_ROL)]) {
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
			// Establecemos datos adicionales de los médicos
			if(usuario.getRol() == RolesUsuarios.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarCalendario(usuario.getDni());
				((Medico)usuario).setCalendario(calendario);
				tipo = FPTipoMedico.consultar(((Medico)usuario).getDni());
				((Medico)usuario).setTipoMedico(tipo);
			}
		}
		
		return usuario;
	}

/*TODO:Estos métodos todavía no se utilizan
	public static Usuario consultarAleatorio(RolesUsuarios rol) throws SQLException,  UsuarioIncorrectoException, CentroSaludIncorrectoException{
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
			throw new UsuarioIncorrectoException("No hay ningún usuario con el rol " + rol + " en la base de datos.");
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
			if(usuario.getRol() == RolesUsuarios.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarCalendario(usuario.getDni());
				((Medico)usuario).setCalendario(calendario);
			}
		}
		
		return usuario;
	}
	
	public static ArrayList<Usuario> consultarTodo(RolesUsuarios rol) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<Usuario> usuarios;
		ArrayList<PeriodoTrabajo> calendario;
		CentroSalud centro;
		Usuario usuario = null;
		TipoMedico tipo;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS + " WHERE " + COL_ROL + " = ?", rol.ordinal());
		datos = GestorConexionesBD.consultar(comando);
		
		// Recorremos la lista de usuarios con el rol indicado
		usuarios = new ArrayList<Usuario>();
		while(datos.next()) {
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
			if(usuario.getRol() == RolesUsuarios.Medico) {
				// Obtenemos el calendario del médico
				calendario = FPPeriodoTrabajo.consultarCalendario(usuario.getDni());
				((Medico)usuario).setCalendario(calendario);
				tipo = FPTipoMedico.consultar(((Medico)usuario).getDni());
				((Medico)usuario).setTipoMedico(tipo);
			}
			// Añadimos el usuario a la lista que se va a devolver
			usuarios.add(usuario);
		}
		
		return usuarios;
	}*/
	
	public static void insertar(Usuario usuario) throws SQLException {
		ArrayList<PeriodoTrabajo> calendario;
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_USUARIOS + " (" + COL_DNI + ", " + COL_LOGIN + ", " + COL_PASSWORD + ", " + COL_ROL + ", " + COL_NOMBRE + ", " + COL_APELLIDOS + ", " + COL_ID_CENTRO + ") VALUES (?, ?, PASSWORD(?), ?, ?, ?, ?)",
		                                  usuario.getDni(), usuario.getLogin(), usuario.getPassword(), usuario.getRol().ordinal(), usuario.getNombre(), usuario.getApellidos(), usuario.getCentroSalud().getId());
		GestorConexionesBD.ejecutar(comando);
		
		// Insertamos datos adicionales de los médicos
		if(usuario.getRol() == RolesUsuarios.Medico) {
			calendario = ((Medico)usuario).getCalendario();
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.insertar(usuario.getDni(), periodo);
			}
			FPTipoMedico.insertar(usuario);
		}
	}
	
	public static void modificar(Usuario usuario) throws SQLException {
		ArrayList<PeriodoTrabajo> calendario;
		ComandoSQL comando;
		
		// Modificamos la base de datos
		// (el DNI no se puede cambiar)
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_USUARIOS + " SET " + COL_LOGIN + " = ?, "+ COL_PASSWORD + " = PASSWORD(?), " + COL_ROL + " = ?, " + COL_NOMBRE + " = ?, " + COL_APELLIDOS + " = ?, " + COL_ID_CENTRO + " = ? WHERE " + COL_DNI + " = ?",
		                                  usuario.getLogin(), usuario.getPassword(), usuario.getRol().ordinal(), usuario.getNombre(), usuario.getApellidos(), usuario.getCentroSalud().getId(), usuario.getDni());
		GestorConexionesBD.ejecutar(comando);
		
		// Modificamos datos adicionales de los médicos
		if(usuario.getRol() == RolesUsuarios.Medico) {
			// Borramos el calendario antiguo del médico y añadimos el nuevo
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
		
		// Borramos datos adicionales de los médicos
		if(usuario.getRol() == RolesUsuarios.Medico) {
			calendario = ((Medico)usuario).getCalendario();
			for(PeriodoTrabajo periodo : calendario) {
				FPPeriodoTrabajo.eliminar(periodo);
			}
			FPTipoMedico.eliminar(usuario);
		}
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_USUARIOS + " WHERE " + COL_DNI + " = ?", usuario.getDni());
		GestorConexionesBD.ejecutar(comando);
	}
	
}
