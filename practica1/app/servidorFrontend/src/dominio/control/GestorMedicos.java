package dominio.control;

import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import persistencia.FPSustitucion;
import persistencia.FPTipoMedico;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuarios;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SustitucionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Clase encargada de consultar, a�adir, modificar y eliminar m�dicos
 * en el sistema y realizar otras operaciones espec�ficas de los m�dicos.
 */
public class GestorMedicos {

	// M�todo para consultar los datos de un m�dico
	public static Medico consultarMedico(long idSesion, String dni) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Comprobamos los par�metros pasados
		if(dni == null) {
			throw new NullPointerException("El DNI del m�dico buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Llamamos al m�todo equivalente para usuarios
		try {
			usuario = GestorUsuarios.consultarUsuario(idSesion, dni);
		} catch(UsuarioInexistenteException e) {
			throw new MedicoInexistenteException(e.getMessage());
		}
		
		// Nos aseguramos de que el usuario devuelto tenga el rol esperado
		if(usuario.getRol() != RolesUsuarios.Medico) {
			throw new MedicoInexistenteException("El DNI introducido no pertenece a un m�dico.");
		}

		return (Medico)usuario;
	}
	
	// M�todo para a�adir un nuevo m�dico al sistema
	public static void crearMedico(long idSesion, Medico medico) throws SQLException, MedicoYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		// Comprobamos los par�metros pasados
		if(medico == null) {
			throw new NullPointerException("El m�dico que se va a crear no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarMedico);
		
		// Llamamos al m�todo equivalente para usuarios
		try {
			GestorUsuarios.crearUsuario(idSesion, medico);
		} catch(UsuarioYaExistenteException ex) {
			throw new MedicoYaExistenteException(ex.getMessage());
		}
	}
	
	// M�todo para modificar un m�dico existente del sistema
	public static void modificarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		// Comprobamos los par�metros pasados
		if(medico == null) {
			throw new NullPointerException("El m�dico que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarMedico);
		
		// Llamamos al m�todo equivalente para usuarios
		try {
			GestorUsuarios.modificarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// M�todo para eliminar un m�dico del sistema
	public static void eliminarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		// Comprobamos los par�metros pasados
		if(medico == null) {
			throw new NullPointerException("El m�dico que se va a eliminar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarMedico);

		// Llamamos al m�todo equivalente para usuarios
		try {
			GestorUsuarios.eliminarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	//TODO:Los siguientes m�todos no se han revisado!
	
	public static void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Medico sustituto) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, SustitucionInvalidaException, UsuarioIncorrectoException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Vector<Sustitucion> sustituciones;
		Sustitucion sustitucion;
		
		// Comprobamos los par�metros pasados
		if(medico == null) {
			throw new NullPointerException("El m�dico que se va a sustituir no puede ser nulo.");
		}
		if(dias == null) {
			throw new NullPointerException("Los d�as en los que se va a realizar la sustituci�n no pueden ser nulos.");			
		}
		if(dias.contains(null)) {
			throw new NullPointerException("Los d�as en los que se va a realizar la sustituci�n no pueden ser nulos.");
		}
		if(sustituto == null) {
			throw new NullPointerException("El m�dico al que se le va a asignar una sustituci�n no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EstablecerSustituto);
		
		// TODO: �Se pueden sustituir m�dicos de todos los tipos?
		// TODO: �Se pueden sustituir m�dicos de un tipo con m�dicos de otro tipo?
		
		// Comprobamos que ni el m�dico sustituido ni el sustituto tiene que
		// hacer ya una sustituci�n alguno de los d�as solicitados
		sustituciones = FPSustitucion.consultarPorSustituto(sustituto.getDni());
		for(Sustitucion sust : sustituciones) {
			if(dias.contains(sust.getDia())) {
				throw new SustitucionInvalidaException("El m�dico sustituto ya va a hacer una sustituci�n en alguno de los d�as solicitados.");
			}
		}
		sustituciones = FPSustitucion.consultarPorSustituido(medico.getDni());
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
	
	// Este m�todo recupera todos los m�dicos que existan del tipo dado 
	public static Vector<Medico> obtenerMedicos(long idSesion, CategoriasMedico tipoMedico) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, NullPointerException, UsuarioInexistenteException {
		Vector<Medico> medicos = new Vector<Medico>();
		Vector<String> nifs = null;
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicosTipo);
		nifs = FPTipoMedico.consultarMedicos(tipoMedico);
		for (String nif: nifs)
			medicos.add((Medico)GestorUsuarios.consultarUsuario(idSesion, nif));
		return medicos;
	}
	
}
