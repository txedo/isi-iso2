package dominio.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import persistencia.FPCentroSalud;
import persistencia.FPEntradaLog;
import persistencia.FPSustitucion;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SustitucionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Clase encargada de consultar, añadir, modificar y eliminar usuarios
 * (médicos, citadores y administradores) en el sistema.
 */
public class GestorUsuarios {

	// Método para consultar los datos de un usuario
	public static Usuario getUsuario(long idSesion, String dni) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		Usuario usuario;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarUsuario);
		
		// Obtenemos el usuario del sistema con el DNI indicado
		try {
			usuario = FPUsuario.consultar(dni);
		} catch(UsuarioIncorrectoException ex) {
			throw new UsuarioInexistenteException("No existe ningún usuario con el DNI introducido");
		}

		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "read", "Se han consultado los datos del usuario con DNI " + dni.toString() + ".");
		FPEntradaLog.insertar(entrada);
		
		return usuario;
	}
	
	// Método para añadir un nuevo usuario al sistema
	public static void crearUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		CentroSalud centro;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.CrearUsuario);
		
		// Comprobamos si ya existe un usuario con el DNI especificado
		try {
			FPUsuario.consultar(usuario.getDni());
			// No se puede crear un usuario que ya existe
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "No se puede crear el usuario con DNI " + usuario.getDni() + " porque ya existe en el sistema.");
			FPEntradaLog.insertar(entrada);
			throw new UsuarioYaExistenteException("Ya existe un usuario con ese DNI");
		} catch(UsuarioIncorrectoException e) {
			// No hay ningún usuario con ese DNI
		}
		
		// Insertamos el nuevo usuario en la base de datos, asignándole un centro de salud aleatorio. 
		// Si no hay ningún centro, se lanza una excepción.
		try {
			centro = FPCentroSalud.consultarAleatorio();
		}
		catch (CentroSaludIncorrectoException ex) {
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "No se puede registrar el usuario con NIF " + usuario.getDni() + " porque no existe un centro de salud en el sistema para asignárselo");
			FPEntradaLog.insertar(entrada);
			throw new SQLException("No se puede registrar el usuario con NIF " + usuario.getDni() + " porque no existe un centro de salud en el sistema para asignárselo");
		}
		usuario.setCentroSalud(centro);
		FPUsuario.insertar(usuario);
		
		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "El usuario con DNI " + usuario.getDni() + " se ha creado correctamente.");
		FPEntradaLog.insertar(entrada);
	}
	
	// Método para modificar un usuario existente del sistema
	public static void modificarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarUsuario);
		
		// Comprobamos si realmente existe el usuario que se quiere modificar
		try {
			FPUsuario.consultar(usuario.getDni());
		} catch(UsuarioIncorrectoException e) {
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "No se pueden modificar los datos del usuario con DNI " + usuario.getDni() + " porque no existe.");
			FPEntradaLog.insertar(entrada);
			throw new UsuarioInexistenteException("No se pueden modificar los datos de un usuario antes de crearlo");
		}
		
		// Modificamos los datos del usuario en la base de datos
		FPUsuario.modificar(usuario);
		
		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "Los datos del usuario con DNI " + usuario.getDni() + " se han modificado correctamente.");
		FPEntradaLog.insertar(entrada);
	}

	// Método para eliminar un usuario del sistema
	public static void eliminarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarUsuario);
		
		// Comprobamos si realmente existe el usuario que se quiere borrar
		try {
			FPUsuario.consultar(usuario.getDni());
		} catch(UsuarioIncorrectoException e) {
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "No se puede eliminar el usuario con DNI " + usuario.getDni() + " porque no existe.");
			FPEntradaLog.insertar(entrada);
			throw new UsuarioInexistenteException("No se pueden eliminar los datos de un usuario antes de crearlo");
		}
		
		// Borramos los datos del usuario en la base de datos
		FPUsuario.eliminar(usuario);

		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "El usuario con DNI " + usuario.getDni() + " se ha eliminado correctamente.");
		FPEntradaLog.insertar(entrada);
	}

	// Los siguientes métodos son específicos para usuarios con rol de médico,
	// por lo que no sirven para citadores y administradores. Se han creado
	// para ser llamados desde la interfaz del servidor frontend, donde no hay
	// métodos para manipular usuarios, sino sólo médicos.
	
	// Método para consultar los datos de un médico
	public static Medico getMedico(long idSesion, String dni) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		Usuario usuario;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		try {
			usuario = getUsuario(idSesion, dni);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos si el usuario devuelto tiene el rol esperado
		if(usuario.getRol() != Roles.Medico) {
			throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico");
		}

		return (Medico)usuario;
	}
	
	// Método para añadir un nuevo médico al sistema
	public static void crearMedico(long idSesion, Medico medico) throws SQLException, MedicoYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarMedico);
		
		try {
			crearUsuario(idSesion, medico);
		} catch(UsuarioYaExistenteException ex) {
			throw new MedicoYaExistenteException(ex.getMessage());
		}
	}
	
	// Método para modificar un médico existente del sistema
	public static void modificarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarMedico);
		
		try {
			modificarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// Método para eliminar un médico del sistema
	public static void eliminarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarMedico);

		try {
			eliminarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	public static void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Medico sustituto) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, SustitucionInvalidaException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ArrayList<Sustitucion> sustituciones;
		Sustitucion sustitucion;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EstablecerSustituto);
		
		// TODO: ¿Se pueden sustituir médicos de todos los tipos?
		// TODO: ¿Se pueden sustituir médicos de un tipo con médicos de otro tipo?
		
		// Comprobamos que ni el médico sustituido ni el sustituto tiene que
		// hacer ya una sustitución alguno de los días solicitados
		sustituciones = FPSustitucion.consultarSustituto(sustituto.getDni());
		for(Sustitucion sust : sustituciones) {
			if(dias.contains(sust.getDia())) {
				throw new SustitucionInvalidaException("El médico sustituto ya va a hacer una sustitución en alguno de los días solicitados.");
			}
		}
		sustituciones = FPSustitucion.consultarMedico(medico.getDni());
		for(Sustitucion sust : sustituciones) {
			if(dias.contains(sust.getDia())) {
				throw new SustitucionInvalidaException("El médico sustituido tenía que hacer una sustitución en alguno de los días solicitados.");
			}
		}
		
		// Comprobamos que el horario del médico y el sustituto sean
		// diferentes para todos los días solicitados
		for(Date dia : dias) {
			if(!medico.calendariosDiferentes(sustituto, dia)) {
				throw new SustitucionInvalidaException("Los calendarios del médico sustituto y del sustituido tienen horas en común en alguno de los días solicitados.");
			}
		}
		
		// Añadimos las sustituciones a la base de datos
		for(Date dia : dias) {
			sustitucion = new Sustitucion();
			sustitucion.setDia(dia);
			sustitucion.setMedico(medico);
			sustitucion.setSustituto(sustituto);
			FPSustitucion.insertar(sustitucion);
		}
	}
	
	public static ArrayList<Medico> obtenerMedicos (long idSesion, String tipo) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, MedicoInexistenteException {
		ArrayList<Medico> medicos = null;
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicosTipo);
		medicos = FPTipoMedico.consultarTodo(tipo);
		return medicos;
	}
		
}
