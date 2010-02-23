package dominio.control;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Vector;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Usuario;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import persistencia.UtilidadesPersistencia;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Clase encargada de consultar, a�adir, modificar y eliminar usuarios
 * (m�dicos, citadores y administradores) en el sistema.
 */
public class GestorUsuarios {

	// M�todo para consultar los datos de un usuario
	public static Usuario consultarUsuario(long idSesion, String dni) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Comprobamos los par�metros pasados
		if(dni == null) {
			throw new NullPointerException("El DNI del usuario buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarUsuario);
		
		// Obtenemos el usuario del sistema con el DNI indicado
		try {
			usuario = FPUsuario.consultar(dni);
		} catch(UsuarioIncorrectoException ex) {
			throw new UsuarioInexistenteException("No existe ning�n usuario con el DNI introducido.");
		}
		
		return usuario;
	}
	
	// M�todo para a�adir un nuevo usuario al sistema
	public static void crearUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		CentroSalud centro;
		Usuario usuarioReal;
		boolean existe;
		
		// Comprobamos los par�metros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a crear no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarUsuario);
		
		// Encriptamos la contrase�a del usuario (hacemos una
		// copia del usuario para no modificar el original)
		usuarioReal = (Usuario)usuario.clone();
		try {
			usuarioReal.setPassword(Encriptacion.encriptarPasswordSHA1(usuario.getPassword()));
		} catch(NoSuchAlgorithmException e) {
			throw new SQLException("No se puede encriptar la contrase�a del usuario.");
		}

		// Consultamos si ya existe otro usuario u otro
		// beneficiario con el mismo DNI
		existe = UtilidadesPersistencia.existeNIF(usuarioReal.getDni());
		if(existe) {
			throw new UsuarioYaExistenteException("Ya existe una persona en el sistema registrada con el DNI " + usuarioReal.getDni() + "."); 
		}
		
		// Consultamos si ya existe otro usuario con el mismo login
		try {
			FPUsuario.consultarPorLogin(usuarioReal.getLogin());
			throw new UsuarioYaExistenteException("El login " + usuarioReal.getLogin() + " ya existe en el sistema para otro usuario y no se puede utilizar de nuevo.");
		} catch(UsuarioIncorrectoException e) {
			// Lo normal es que se lance esta excepci�n
		}

		// Buscamos un centro de salud para el nuevo usuario; si se
		// lanza una excepci�n es porque no hay ning�n centro
		try {
			centro = FPCentroSalud.consultarAleatorio();
			usuarioReal.setCentroSalud(centro);
		} catch(CentroSaludInexistenteException e) {
			throw new SQLException("No se puede registrar el usuario porque no existe ning�n centro de salud en el sistema que se le pueda asignar.");
		}

		// A�adimos el usuario al sistema
		FPUsuario.insertar(usuarioReal);
	}
	
	// M�todo para modificar un usuario existente del sistema
	public static void modificarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuarioAntiguo, usuarioReal;
		
		// Comprobamos los par�metros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarUsuario);
		
		// Comprobamos si realmente existe el usuario que se quiere modificar
		try {
			usuarioAntiguo = FPUsuario.consultar(usuario.getDni());
		} catch(UsuarioIncorrectoException e) {
			throw new UsuarioInexistenteException(e.getMessage());
		}
		
		// Vemos si es necesario cambiar la contrase�a (siempre
		// hacemos una copia del usuario para no modificar el original)
		usuarioReal = (Usuario)usuario.clone();
		if(!usuario.getPassword().trim().equals("")) {
			// Encriptamos la nueva contrase�a del usuario
			try {
				usuarioReal.setPassword(Encriptacion.encriptarPasswordSHA1(usuario.getPassword()));
			} catch(NoSuchAlgorithmException e) {
				throw new SQLException("No se puede encriptar la contrase�a del usuario.");
			}
		} else {
			// Mantenemos la contrase�a antigua
			usuarioReal.setPassword(usuarioAntiguo.getPassword());
		}
		
		// Modificamos los datos del usuario
		FPUsuario.modificar(usuarioReal);
	}

	// M�todo para eliminar un usuario del sistema
	public static void eliminarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, DireccionInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException {
		Vector<Beneficiario> beneficiarios = null;
		Medico nuevoMedico;

		// Comprobamos los par�metros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a eliminar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarUsuario);
		
		// Comprobamos si realmente existe el usuario que se quiere borrar
		try {
			FPUsuario.consultar(usuario.getDni());
		} catch(UsuarioIncorrectoException e) {
			throw new UsuarioInexistenteException(e.getMessage());
		}	
		
		// Borramos los datos del usuario
		FPUsuario.eliminar(usuario);
		
		// Si se ha eliminado un m�dico, obtenemos su lista de beneficiarios para intentar asignar un nuevo m�dico
		if(usuario.getRol() == RolesUsuario.Medico) {
			beneficiarios = GestorBeneficiarios.consultarBeneficiariosMedico(idSesion, usuario.getDni());
		}
		
		if(usuario.getRol() == RolesUsuario.Medico) {
			for(Beneficiario beneficiario : beneficiarios) {
				nuevoMedico = GestorBeneficiarios.obtenerMedicoBeneficiario(beneficiario);
				if(nuevoMedico != null) {
					beneficiario.setMedicoAsignado(nuevoMedico);
					GestorBeneficiarios.modificarBeneficiario(idSesion, beneficiario);
				}
			}
		}
	}

	// M�todo para obtener la lista de centros de salud de los usuarios
	public static Vector<CentroSalud> consultarCentros(long idSesion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException {
		Vector<CentroSalud> centros;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCentros);
		
		// Obtenemos la lista de centros de salud
		centros = FPCentroSalud.consultarTodo();
		
		return centros;
	}

}
