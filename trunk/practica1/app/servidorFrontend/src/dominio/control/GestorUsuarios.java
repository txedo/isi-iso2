package dominio.control;

import java.sql.SQLException;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Usuario;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Clase encargada de consultar, añadir, modificar y eliminar usuarios
 * (médicos, citadores y administradores) en el sistema.
 */
public class GestorUsuarios {

	// Método para consultar los datos de un usuario
	public static Usuario consultarUsuario(long idSesion, String dni) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException, NullPointerException {
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(dni == null) {
			throw new NullPointerException("El DNI del usuario buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarUsuario);
		
		// Obtenemos el usuario del sistema con el DNI indicado
		try {
			usuario = FPUsuario.consultar(dni);
		} catch(UsuarioIncorrectoException ex) {
			throw new UsuarioInexistenteException("No existe ningún usuario con el DNI introducido.");
		}
		
		return usuario;
	}
	
	// Método para añadir un nuevo usuario al sistema
	public static void crearUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException, NullPointerException {
		CentroSalud centro;
		
		// Comprobamos los parámetros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a crear no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.CrearUsuario);
		
		// Consultamos si ya existe un usuario con el mismo DNI del
		// usuario que se quiere crear, y en ese caso se lanza un error
		try {
			FPUsuario.consultar(usuario.getDni());
			throw new UsuarioYaExistenteException("El usuario con DNI " + usuario.getDni() + " ya existe en el sistema y no se puede registrar de nuevo.");
		} catch(UsuarioIncorrectoException e) {
			// Lo normal es que se lance esta excepción
		}
		
		// Buscamos un centro de salud para el nuevo usuario; si se
		// lanza una excepción es porque no hay ningún centro
		try {
			centro = FPCentroSalud.consultarAleatorio();
			usuario.setCentroSalud(centro);
		} catch(CentroSaludIncorrectoException e) {
			throw new SQLException("No se puede registrar el usuario porque no existe ningún centro de salud en el sistema que se le pueda asignar.");
		}

		// Añadimos el usuario al sistema
		FPUsuario.insertar(usuario);
	}
	
	// Método para modificar un usuario existente del sistema
	public static void modificarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException, NullPointerException {
		// Comprobamos los parámetros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarUsuario);
		
		// Comprobamos si realmente existe el usuario que se quiere modificar
		try {
			FPUsuario.consultar(usuario.getDni());
		} catch(UsuarioIncorrectoException e) {
			throw new UsuarioInexistenteException(e.getMessage());
		}
		
		// Modificamos los datos del usuario
		FPUsuario.modificar(usuario);
	}

	// Método para eliminar un usuario del sistema
	public static void eliminarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		// Comprobamos los parámetros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a eliminar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarUsuario);
		
		// Comprobamos si realmente existe el usuario que se quiere borrar
		try {
			FPUsuario.consultar(usuario.getDni());
		} catch(UsuarioIncorrectoException e) {
			throw new UsuarioInexistenteException(e.getMessage());
		}
		
		// Borramos los datos del usuario
		FPUsuario.eliminar(usuario);
	}
		
}
