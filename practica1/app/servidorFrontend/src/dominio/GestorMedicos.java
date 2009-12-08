package dominio;

import java.sql.SQLException;

import persistencia.FPUsuario;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de consultar, a�adir, modificar y eliminar m�dicos
 * en el sistema.
 */
public class GestorMedicos {

	public static Medico getMedico(long idSesion, String dni) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		Usuario usuario;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ConsultarMedico);
		
		// Obtenemos el usuario del sistema con el DNI indicado
		try {
			usuario = FPUsuario.consultar(dni);
		} catch(UsuarioIncorrectoException e) {
			// No existe un m�dico (ni un usuario) con ese DNI
			throw new MedicoInexistenteException("No existe ning�n usuario con el DNI introducido");
		}
		
		// Comprobamos si el usuario devuelto es realmente un m�dico
		if(usuario.getRol() != Roles.Medico) {
			throw new MedicoInexistenteException("El DNI introducido no pertenece a un m�dico");
		}
		
		return (Medico)usuario;
	}
	
	public static void crearMedico(long idSesion, Medico medico) throws SQLException, MedicoYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operacion.CrearUsuario);
		
		// Comprobamos si ya existe un usuario con el DNI del nuevo m�dico
		try {
			FPUsuario.consultar(medico.getDni());
			// Si no ha habido ning�n error, es porque el usuario ya existe
			throw new MedicoYaExistenteException("Ya existe un usuario con ese DNI");
		} catch(UsuarioIncorrectoException e) {
			// El usuario no existe
		}
		
		// Insertamos el nuevo m�dico en la base de datos
		FPUsuario.insertar(medico);
	}
	
	public static void modificarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ModificarUsuario);
		
		// Comprobamos si realmente existe el m�dico que se quiere modificar
		try {
			getMedico(idSesion, medico.getDni());
		} catch(MedicoInexistenteException e) {
			throw new MedicoInexistenteException("No se pueden modificar los datos de un m�dico antes de crearlo");
		}
		
		// Modificamos los datos del m�dico en la base de datos
		FPUsuario.modificar(medico);
	}

	public static void eliminarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operacion.EliminarUsuario);
		
		// Comprobamos si realmente existe el m�dico que se quiere borrar
		try {
			getMedico(idSesion, medico.getDni());
		} catch(MedicoInexistenteException e) {
			throw new MedicoInexistenteException("No se pueden eliminar los datos de un m�dico antes de crearlo");
		}
		
		// Borramos los datos del m�dico en la base de datos
		FPUsuario.eliminar(medico);
	}
	
}
