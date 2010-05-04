package dominio.control;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Roles;
import dominio.conocimiento.SesionUsuario;
import dominio.conocimiento.Usuario;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPCita;
import persistencia.FPUsuario;
import persistencia.UtilidadesPersistencia;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Clase encargada de consultar, añadir, modificar y eliminar usuarios
 * (médicos, citadores y administradores) en el sistema.
 */
public class GestorUsuarios {

	// Método para consultar los datos de un usuario
	public static Usuario consultarUsuario(long idSesion, String nif) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(nif == null) {
			throw new NullPointerException("El NIF del usuario buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarUsuario);
		
		// Llamamos al método común para consultar usuarios y médicos
		usuario = consultarUsuario(nif);
		
		return usuario;
	}
	
	public static Usuario consultarUsuarioPorLogin(long idSesion, String login) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(login == null) {
			throw new NullPointerException("El login del usuario buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarPropioUsuario);
		
		// Llamamos al método común para consultar usuarios y médicos
		usuario = consultarUsuarioPorLogin(login);
		
		return usuario;
	}
	
	
	// Método para añadir un nuevo usuario al sistema
	public static Usuario crearUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuarioCreado;
		
		// Comprobamos los parámetros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a crear no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarUsuario);

		// Llamamos al método común para crear usuarios y médicos
		usuarioCreado = crearUsuario(usuario);
		
		return usuarioCreado;
	}
	
	// Método para modificar un usuario existente del sistema
	public static void modificarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException {
		// Comprobamos los parámetros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarUsuario);
		
		// Llamamos al método común para modificar usuarios y médicos
		modificarUsuario(usuario);
	}

	// Método para eliminar un usuario del sistema
	public static void eliminarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, DireccionInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException, SesionNoIniciadaException {
		// Comprobamos los parámetros pasados
		if(usuario == null) {
			throw new NullPointerException("El usuario que se va a eliminar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarUsuario);
		
		// Llamamos al método común para eliminar usuarios y médicos
		eliminarUsuario(usuario);
	}
	
	public static boolean correspondeNIFUsuario (String nif) throws SQLException {
		return FPUsuario.correspondeNIFUsuario(nif);
	}

	// Método para obtener los datos del usuario que ha iniciado sesión
	public static Usuario consultarPropioUsuario(long idSesion) throws SesionInvalidaException, OperacionIncorrectaException {
		Usuario usuario;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarPropioUsuario);
		
		// Obtenemos el usuario que ha ejecutado la operación a partir de su sesión
		usuario = ((SesionUsuario)GestorSesiones.getSesion(idSesion)).getUsuario();

		return usuario;
	}
	
	// Método para obtener la lista de centros de salud de los usuarios
	public static Vector<CentroSalud> consultarCentros(long idSesion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException {
		Vector<CentroSalud> centros;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCentros);
		
		// Obtenemos la lista de centros de salud
		centros = FPCentroSalud.consultarTodo();
		
		return centros;
	}

	// Método para consultar los datos de un usuario
	// (es público porque se utiliza desde otro gestor, no es accesible a los clientes)
	public static Usuario consultarUsuario(String nif) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Obtenemos el usuario del sistema con el NIF indicado
		try {
			usuario = FPUsuario.consultar(nif);
		} catch(UsuarioIncorrectoException ex) {
			throw new UsuarioInexistenteException("No existe ningún usuario con el NIF introducido.");
		}
		
		return usuario;
	}
	
	// Método para consultar los datos de un usuario a partir de su login (que es único en la base de datos)
	// (es público porque se utiliza desde otro gestor, no es accesible a los clientes)
	public static Usuario consultarUsuarioPorLogin(String login) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Obtenemos el usuario del sistema con el login indicado
		try {
			usuario = FPUsuario.consultarPorLogin(login);
		} catch(UsuarioIncorrectoException ex) {
			throw new UsuarioInexistenteException("No existe ningún usuario con el login introducido.");
		}
		
		return usuario;
	}
	
	// Método para añadir un nuevo usuario al sistema
	// (es público porque se utiliza desde otro gestor, no es accesible a los clientes)
	public static Usuario crearUsuario(Usuario usuario) throws SQLException, UsuarioYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		CentroSalud centro;
		Usuario usuarioReal;
		boolean existe;
		
		// Encriptamos la contraseña del usuario (hacemos una
		// copia del usuario para no modificar el original)
		usuarioReal = (Usuario)usuario.clone();
		try {
			usuarioReal.setPassword(UtilidadesDominio.encriptarPasswordSHA1(usuario.getPassword()));
		} catch(NoSuchAlgorithmException e) {
			throw new SQLException("No se puede encriptar la contraseña del usuario.");
		}

		// Consultamos si ya existe otro usuario u otro
		// beneficiario con el mismo NIF
		existe = UtilidadesPersistencia.existeNIF(usuarioReal.getNif());
		if(existe) {
			throw new UsuarioYaExistenteException("Ya existe una persona en el sistema registrada con el NIF " + usuarioReal.getNif() + "."); 
		}
		
		// Consultamos si ya existe otro usuario con el mismo login
		try {
			FPUsuario.consultarPorLogin(usuarioReal.getLogin());
			throw new UsuarioYaExistenteException("El login " + usuarioReal.getLogin() + " ya existe en el sistema para otro usuario y no se puede utilizar de nuevo.");
		} catch(UsuarioIncorrectoException e) {
			// Lo normal es que se lance esta excepción
		}

		// Buscamos un centro de salud para el nuevo usuario; si se
		// lanza una excepción es porque no hay ningún centro
		try {
			centro = FPCentroSalud.consultarAleatorio();
			usuarioReal.setCentroSalud(centro);
		} catch(CentroSaludInexistenteException e) {
			throw new SQLException("No se puede registrar el usuario porque no existe ningún centro de salud en el sistema que se le pueda asignar.");
		}

		// Añadimos el usuario al sistema
		FPUsuario.insertar(usuarioReal);
		
		return usuarioReal;
	}
	
	// Método para modificar un usuario existente del sistema
	// (es público porque se utiliza desde otro gestor, no es accesible a los clientes)
	public static void modificarUsuario(Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException {
		Vector<Cita> citas;
		Usuario usuarioAntiguo, usuarioReal;
		boolean afectada;
		
		// Comprobamos si realmente existe el usuario que se quiere modificar
		try {
			usuarioAntiguo = FPUsuario.consultar(usuario.getNif());
		} catch(UsuarioIncorrectoException e) {
			throw new UsuarioInexistenteException(e.getMessage());
		}
		
		// Vemos si es necesario cambiar la contraseña (siempre
		// hacemos una copia del usuario para no modificar el original)
		usuarioReal = (Usuario)usuario.clone();
		if(!usuario.getPassword().trim().equals("")) {
			// Encriptamos la nueva contraseña del usuario
			try {
				usuarioReal.setPassword(UtilidadesDominio.encriptarPasswordSHA1(usuario.getPassword()));
			} catch(NoSuchAlgorithmException e) {
				throw new SQLException("No se puede encriptar la contraseña del usuario.");
			}
		} else {
			// Mantenemos la contraseña antigua
			usuarioReal.setPassword(usuarioAntiguo.getPassword());
		}
		
		// Modificamos los datos del usuario
		FPUsuario.modificar(usuarioReal);
		
		// Si el usuario modificado es un médico, eliminamos aquellas citas
		// pendientes que ahora no quedan dentro del nuevo horario del médico
		if(usuarioReal.getRol() == Roles.Médico) {
			citas = FPCita.consultarPorMedico(usuario.getNif());
			for(Cita cita : citas) {
				if(cita.getFechaYHora().after(new Date())) {
					afectada = true;
					for(PeriodoTrabajo periodo : ((Medico)usuarioReal).getCalendario()) {
						if(UtilidadesDominio.diaFecha(cita.getFechaYHora()) == periodo.getDia()
						 && cita.citaEnHoras(periodo.getHoraInicio(), periodo.getHoraFinal())) {
							// La cita queda dentro del nuevo horario
							afectada = false;
						}
					}
					if(afectada) {
						// La cita no queda dentro del nuevo horario
						FPCita.eliminar(cita);
					}
				}
			}
		}
	}

	// Método para eliminar un usuario del sistema
	// (es público porque se utiliza desde otro gestor, no es accesible a los clientes)
	public static void eliminarUsuario(Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, DireccionInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException {
		Vector<Beneficiario> beneficiarios = null;
		Vector<String> nifs;
		Medico nuevoMedico;

		// Comprobamos si realmente existe el usuario que se quiere borrar
		try {
			FPUsuario.consultar(usuario.getNif());
		} catch(UsuarioIncorrectoException e) {
			throw new UsuarioInexistenteException(e.getMessage());
		}	
		
		// Si se va a eliminar un médico, obtenemos su lista de
		// beneficiarios para asignarle después un médico diferente
		if(usuario.getRol() == Roles.Médico) {
			nifs = FPBeneficiario.consultarBeneficiariosMedico(usuario.getNif());
			beneficiarios = new Vector<Beneficiario>();
			for(String nif : nifs) {
				beneficiarios.add(FPBeneficiario.consultarPorNIF(nif));
			}
		}

		// Borramos los datos del usuario
		FPUsuario.eliminar(usuario);

		// Intentamos asignar nuevos médicos a los beneficiarios
		if(usuario.getRol() == Roles.Médico) {
			for(Beneficiario beneficiario : beneficiarios) {
				beneficiario.setMedicoAsignado(null);
				nuevoMedico = GestorBeneficiarios.obtenerMedicoBeneficiario(beneficiario);
				if(nuevoMedico != null) {
					beneficiario.setMedicoAsignado(nuevoMedico);
					FPBeneficiario.modificar(beneficiario);
				}
			}
		}
	}

}
