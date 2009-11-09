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
	
	private static final String COL_DNI = "dni";
	private static final String COL_LOGIN = "login";
	private static final String COL_PASSWORD = "password";
	private static final String COL_ROL = "rol";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_APELLIDOS = "apellidos";
	private static final String COL_ID_CENTRO_USUARIO = "id_centro";
	private static final String COL_ID_CENTRO = "id";

	
	public static Usuario consultar(int dni) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Usuario usuario = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM Usuarios,Centros WHERE " + COL_DNI + " = ? AND " + COL_ID_CENTRO_USUARIO + " = " + COL_ID_CENTRO, dni);
		datos = GestorConexiones.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El DNI introducido no es v�lido");
		} else {
			switch (Roles.values()[datos.getInt(COL_ROL)]){
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
			// Creamos el usuario y su centro asociado
			CentroSalud centro = FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO));
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
		Usuario usuario = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM  Usuarios,Centros WHERE " + COL_LOGIN + "=? AND " + COL_PASSWORD + "=?", login, password);
		datos = GestorConexiones.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("El nombre de usuario o contrase�a introducidos no son v�lidos");
		} else {	
			switch (Roles.values()[datos.getInt(COL_ROL)]){
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
			// Creamos el usuario
			CentroSalud centro = FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO));
			usuario.setDni(datos.getString(COL_DNI));
			usuario.setLogin(datos.getString(COL_LOGIN));
			usuario.setPassword(datos.getString(COL_PASSWORD));
			usuario.setNombre(datos.getString(COL_NOMBRE));
			usuario.setApellidos(datos.getString(COL_APELLIDOS));
			usuario.setCentroSalud(centro);
		}
		return usuario;
	}
	
}
