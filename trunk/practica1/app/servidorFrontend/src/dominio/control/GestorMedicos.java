package dominio.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import persistencia.FPSustitucion;
import persistencia.FPTipoMedico;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
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
 * Clase encargada de consultar, añadir, modificar y eliminar médicos
 * en el sistema y realizar otras operaciones específicas de los médicos.
 */
public class GestorMedicos {

	// Método para consultar los datos de un médico
	public static Medico getMedico(long idSesion, String dni) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException, NullPointerException {
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(dni == null) {
			throw new NullPointerException("El DNI del médico buscado no puede ser nulo");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Llamamos al método equivalente para usuarios
		try {
			usuario = GestorUsuarios.getUsuario(idSesion, dni);
		} catch(UsuarioInexistenteException e) {
			throw new MedicoInexistenteException(e.getMessage());
		}
		
		// Nos aseguramos de que el usuario devuelto tenga el rol esperado
		if(usuario.getRol() != Roles.Medico) {
			throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico");
		}

		return (Medico)usuario;
	}
	
	// Método para añadir un nuevo médico al sistema
	public static void crearMedico(long idSesion, Medico medico) throws SQLException, MedicoYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException, NullPointerException {
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a crear no puede ser nulo");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarMedico);
		
		// Llamamos al método equivalente para usuarios
		try {
			GestorUsuarios.crearUsuario(idSesion, medico);
		} catch(UsuarioYaExistenteException ex) {
			throw new MedicoYaExistenteException(ex.getMessage());
		}
	}
	
	// Método para modificar un médico existente del sistema
	public static void modificarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException, NullPointerException {
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a modificar no puede ser nulo");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarMedico);
		
		// Llamamos al método equivalente para usuarios
		try {
			GestorUsuarios.modificarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// Método para eliminar un médico del sistema
	public static void eliminarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException, NullPointerException {
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a eliminar no puede ser nulo");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarMedico);

		// Llamamos al método equivalente para usuarios
		try {
			GestorUsuarios.eliminarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}

	//TODO:Los siguientes métodos no se han revisado!
	
	public static void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Medico sustituto) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, SustitucionInvalidaException, UsuarioIncorrectoException, CentroSaludIncorrectoException, NullPointerException {
		ArrayList<Sustitucion> sustituciones;
		Sustitucion sustitucion;
		
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a sustituir no puede ser nulo");
		}
		if(dias == null) {
			throw new NullPointerException("Los días en los que se va a realizar la sustitución no pueden ser nulos");			
		}
		if(sustituto == null) {
			throw new NullPointerException("El médico al que se le va a asignar una sustitución no puede ser nulo");
		}
		
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
	
	// TODO: ¿esto qué hace realmente?
	public static ArrayList<Medico> obtenerMedicos (long idSesion, String tipo) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, MedicoInexistenteException {
		ArrayList<Medico> medicos = null;
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicosTipo);
		medicos = FPTipoMedico.consultarTodo(tipo);
		return medicos;
	}
	
}
