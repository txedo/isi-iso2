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
 * Clase encargada de consultar, a�adir, modificar y eliminar usuarios
 * (m�dicos, citadores y administradores) en el sistema.
 */
public class GestorUsuarios {

	// M�todo para consultar los datos de un usuario
	public static Usuario getUsuario(long idSesion, String dni) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		Usuario usuario;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarUsuario);
		
		// Obtenemos el usuario del sistema con el DNI indicado
		try {
			usuario = FPUsuario.consultar(dni);
		} catch(UsuarioIncorrectoException ex) {
			throw new UsuarioInexistenteException("No existe ning�n usuario con el DNI introducido");
		}

		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "read", "Se han consultado los datos del usuario con DNI " + dni.toString() + ".");
		FPEntradaLog.insertar(entrada);
		
		return usuario;
	}
	
	// M�todo para a�adir un nuevo usuario al sistema
	public static void crearUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		CentroSalud centro;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.CrearUsuario);
		
		// Comprobamos si ya existe un usuario con el DNI especificado
		try {
			FPUsuario.consultar(usuario.getDni());
			// No se puede crear un usuario que ya existe
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "No se puede crear el usuario con DNI " + usuario.getDni() + " porque ya existe en el sistema.");
			FPEntradaLog.insertar(entrada);
			throw new UsuarioYaExistenteException("Ya existe un usuario con ese DNI");
		} catch(UsuarioIncorrectoException e) {
			// No hay ning�n usuario con ese DNI
		}
		
		// Insertamos el nuevo usuario en la base de datos, asign�ndole un centro de salud aleatorio. 
		// Si no hay ning�n centro, se lanza una excepci�n.
		try {
			centro = FPCentroSalud.consultarAleatorio();
		}
		catch (CentroSaludIncorrectoException ex) {
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "No se puede registrar el usuario con NIF " + usuario.getDni() + " porque no existe un centro de salud en el sistema para asign�rselo");
			FPEntradaLog.insertar(entrada);
			throw new SQLException("No se puede registrar el usuario con NIF " + usuario.getDni() + " porque no existe un centro de salud en el sistema para asign�rselo");
		}
		usuario.setCentroSalud(centro);
		FPUsuario.insertar(usuario);
		
		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "El usuario con DNI " + usuario.getDni() + " se ha creado correctamente.");
		FPEntradaLog.insertar(entrada);
	}
	
	// M�todo para modificar un usuario existente del sistema
	public static void modificarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
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
		
		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "Los datos del usuario con DNI " + usuario.getDni() + " se han modificado correctamente.");
		FPEntradaLog.insertar(entrada);
	}

	// M�todo para eliminar un usuario del sistema
	public static void eliminarUsuario(long idSesion, Usuario usuario) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
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

		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "El usuario con DNI " + usuario.getDni() + " se ha eliminado correctamente.");
		FPEntradaLog.insertar(entrada);
	}

	// Los siguientes m�todos son espec�ficos para usuarios con rol de m�dico,
	// por lo que no sirven para citadores y administradores. Se han creado
	// para ser llamados desde la interfaz del servidor frontend, donde no hay
	// m�todos para manipular usuarios, sino s�lo m�dicos.
	
	// M�todo para consultar los datos de un m�dico
	public static Medico getMedico(long idSesion, String dni) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		Usuario usuario;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		try {
			usuario = getUsuario(idSesion, dni);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos si el usuario devuelto tiene el rol esperado
		if(usuario.getRol() != Roles.Medico) {
			throw new MedicoInexistenteException("El DNI introducido no pertenece a un m�dico");
		}

		return (Medico)usuario;
	}
	
	// M�todo para a�adir un nuevo m�dico al sistema
	public static void crearMedico(long idSesion, Medico medico) throws SQLException, MedicoYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarMedico);
		
		try {
			crearUsuario(idSesion, medico);
		} catch(UsuarioYaExistenteException ex) {
			throw new MedicoYaExistenteException(ex.getMessage());
		}
	}
	
	// M�todo para modificar un m�dico existente del sistema
	public static void modificarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarMedico);
		
		try {
			modificarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// M�todo para eliminar un m�dico del sistema
	public static void eliminarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException {
		// Comprobamos si se tienen permisos para realizar la operaci�n
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
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EstablecerSustituto);
		
		// TODO: �Se pueden sustituir m�dicos de todos los tipos?
		// TODO: �Se pueden sustituir m�dicos de un tipo con m�dicos de otro tipo?
		
		// Comprobamos que ni el m�dico sustituido ni el sustituto tiene que
		// hacer ya una sustituci�n alguno de los d�as solicitados
		sustituciones = FPSustitucion.consultarSustituto(sustituto.getDni());
		for(Sustitucion sust : sustituciones) {
			if(dias.contains(sust.getDia())) {
				throw new SustitucionInvalidaException("El m�dico sustituto ya va a hacer una sustituci�n en alguno de los d�as solicitados.");
			}
		}
		sustituciones = FPSustitucion.consultarMedico(medico.getDni());
		for(Sustitucion sust : sustituciones) {
			if(dias.contains(sust.getDia())) {
				throw new SustitucionInvalidaException("El m�dico sustituido ten�a que hacer una sustituci�n en alguno de los d�as solicitados.");
			}
		}
		
		// Comprobamos que el horario del m�dico y el sustituto sean
		// diferentes para todos los d�as solicitados
		for(Date dia : dias) {
			if(!medico.calendariosDiferentes(sustituto, dia)) {
				throw new SustitucionInvalidaException("Los calendarios del m�dico sustituto y del sustituido tienen horas en com�n en alguno de los d�as solicitados.");
			}
		}
		
		// A�adimos las sustituciones a la base de datos
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
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicosTipo);
		medicos = FPTipoMedico.consultarTodo(tipo);
		return medicos;
	}
		
}
