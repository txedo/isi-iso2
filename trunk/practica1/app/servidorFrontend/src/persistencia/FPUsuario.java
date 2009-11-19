package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import dominio.Administrador;
import dominio.CentroSalud;
import dominio.Citador;
import dominio.Medico;
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
		CentroSalud centro;
		Usuario usuario = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS + " WHERE " + COL_DNI + " = ?", dni);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El DNI introducido no es válido");
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
		}
		
		return usuario;
	}
	
	public static Usuario consultar(String login, String password) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
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
		}
		
		return usuario;
	}
	
	public static void insertar(Usuario usuario) throws SQLException {
		ComandoSQL comando;

		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_USUARIOS + " (" + COL_DNI + ", " + COL_LOGIN + ", " + COL_PASSWORD + ", " + COL_ROL + ", " + COL_NOMBRE + ", " + COL_APELLIDOS + ", " + COL_ID_CENTRO + ") VALUES (?, ?, ?, ?, ?, ?, ?)",
		                                  usuario.getDni(), usuario.getLogin(), usuario.getPassword(), usuario.getRol().ordinal(), usuario.getNombre(), usuario.getApellidos(), usuario.getCentroSalud().getId());
		GestorConexionesBD.ejecutar(comando);	
	}
	
	public static void actualizar(Usuario usuario) throws SQLException {
		ComandoSQL comando;
		
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_USUARIOS + " SET " + COL_LOGIN + " = ?, "+ COL_PASSWORD + " = ?, " + COL_ROL + " = ?, " + COL_NOMBRE + " = ?, " + COL_APELLIDOS + " = ?, " + COL_ID_CENTRO + " = ? WHERE " + COL_DNI + " = ?",
		                                  usuario.getLogin(), usuario.getPassword(), usuario.getRol().ordinal(), usuario.getNombre(), usuario.getApellidos(), usuario.getCentroSalud().getId(), usuario.getDni());
		GestorConexionesBD.ejecutar(comando);	
	}
	
	public static void eliminar(Usuario usuario) throws SQLException {
		ComandoSQL comando;
		
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_USUARIOS + " WHERE " + COL_DNI + " = ?" , usuario.getDni());
		GestorConexionesBD.ejecutar(comando);
	}
	
}
