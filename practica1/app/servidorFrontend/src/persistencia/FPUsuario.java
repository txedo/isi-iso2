package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import dominio.Usuario;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase dedicada a consultar y modificar usuarios en la base de datos.
 */
public class FPUsuario {

	private static final String COL_DNI = "dni";
	private static final String COL_LOGIN = "login";
	private static final String COL_PASSWORD = "password";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_APELLIDOS = "apellidos";
	private static final String COL_ID_CENTRO = "id_centro";
	
	public static Usuario consultar(int dni) throws SQLException, UsuarioIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Usuario usuario = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM Usuarios WHERE " + COL_DNI + "=?", dni);
		datos = GestorConexiones.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El DNI introducido no es válido");
		} else {
			// Creamos el usuario
			usuario = new Usuario();
			usuario.setDni(datos.getString(COL_DNI));
			usuario.setLogin(datos.getString(COL_LOGIN));
			usuario.setPassword(datos.getString(COL_PASSWORD));
			usuario.setNombre(datos.getString(COL_NOMBRE));
			usuario.setApellidos(datos.getString(COL_APELLIDOS));
		}
		return usuario;
	}
	
	public static Usuario consultar(String login, String password) throws SQLException, UsuarioIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Usuario usuario = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM Usuarios WHERE " + COL_LOGIN + "=? AND " + COL_PASSWORD + "=?", login, password);
		datos = GestorConexiones.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El nombre de usuario o contraseña introducidos no son válidos");
		} else {		
			// Creamos el usuario
			usuario = new Usuario();
			usuario.setDni(datos.getString(COL_DNI));
			usuario.setLogin(datos.getString(COL_LOGIN));
			usuario.setPassword(datos.getString(COL_PASSWORD));
			usuario.setNombre(datos.getString(COL_NOMBRE));
			usuario.setApellidos(datos.getString(COL_APELLIDOS));
		}
		return usuario;
	}
	
}
