package dominio.control;

import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import persistencia.FPSustitucion;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import dominio.UtilidadesDominio;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Clase encargada de consultar, a�adir, modificar y eliminar m�dicos
 * en el sistema y realizar otras operaciones espec�ficas de los m�dicos.
 */
public class GestorMedicos {

	// M�todo para consultar los datos de un m�dico
	public static Medico consultarMedico(long idSesion, String nif) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Comprobamos los par�metros pasados
		if(nif == null) {
			throw new NullPointerException("El NIF del m�dico buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Llamamos al m�todo com�n para consultar usuarios y m�dicos
		try {
			usuario = GestorUsuarios.consultarUsuario(nif);
		} catch(UsuarioInexistenteException e) {
			throw new MedicoInexistenteException("No existe ning�n m�dico con el NIF introducido.");
		}
		
		// Nos aseguramos de que el usuario devuelto tenga el rol esperado
		if(usuario.getRol() != RolesUsuario.Medico) {
			throw new MedicoInexistenteException("El NIF introducido no pertenece a un m�dico.");
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
		
		// Llamamos al m�todo com�n para crear usuarios y m�dicos
		try {
			GestorUsuarios.crearUsuario(medico);
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
		
		// Llamamos al m�todo com�n para consultar usuarios y m�dicos
		try {
			GestorUsuarios.modificarUsuario(medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// M�todo para eliminar un m�dico del sistema
	public static void eliminarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException {
		// Comprobamos los par�metros pasados
		if(medico == null) {
			throw new NullPointerException("El m�dico que se va a eliminar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarMedico);

		// Llamamos al m�todo com�n para eliminar usuarios y m�dicos
		try {
			GestorUsuarios.eliminarUsuario(medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// M�todo que devuelve las horas de cada d�a de la semana
	// en las que un m�dico puede pasar una cita
	public static Hashtable<DiaSemana, Vector<String>> consultarHorarioMedico(long idSesion, String nifMedico) throws SQLException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, NullPointerException, DireccionInexistenteException {
		Hashtable<DiaSemana, Vector<String>> horasDia;
		Usuario usuario;
		Medico medico;
		
		// Comprobamos los par�metros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del m�dico para el que se quiere consultar el horario no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Comprobamos que exista el m�dico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un m�dico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Generamos la tabla que asocia d�a de la semana con horas de trabajo
		horasDia = new Hashtable<DiaSemana, Vector<String>>();
		for(DiaSemana dia : DiaSemana.values()) {
			horasDia.put(dia, medico.horasCitas(dia, IConstantes.DURACION_CITA));
		}
		
		return horasDia;
	}
	
	// M�todo que devuelve todos los m�dicos de un determinado tipo 
	public static Vector<Medico> consultarMedicosPorTipo(long idSesion, TipoMedico tipo) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, NullPointerException {
		Vector<Medico> medicos;
		Vector<String> nifs;
		
		// Comprobamos los par�metros pasados
		if(tipo == null) {
			throw new NullPointerException("El tipo de los m�dicos que se quieren buscar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicosTipo);
		
		// Obtenemos los NIFs de todos los m�dicos del tipo dado
		nifs = FPTipoMedico.consultarMedicos(tipo);
		
		// Recuperamos los m�dicos con los NIFs anteriores
		medicos = new Vector<Medico>();
		for(String nif : nifs) {
			medicos.add((Medico)FPUsuario.consultar(nif));
		}
		
		return medicos;
	}
	
	// M�todo que devuelve el m�dico que dar�a realmente una cita
	// teniendo en cuenta las sustituciones
	public static Medico consultarMedicoCita(long idSesion, String nifMedico, Date fechaYHora) throws NullPointerException, SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, MedicoInexistenteException, FechaNoValidaException {
		Vector<Sustitucion> sustituciones;
		Medico medico;
		Usuario usuario;
		boolean comprobar;
		
		// Comprobamos los par�metros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del m�dico para el que se pretende pedir cita no puede ser nulo.");
		}
		if(fechaYHora == null) {
			throw new NullPointerException("La hora en la que se pretende pedir cita no puede ser nula.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicoCita);
	
		// En principio dar� la cita el m�dico previsto
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El m�dico para el que se pretende pedir cita no es un usuario del sistema con rol de m�dico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos si el m�dico realmente podr�a dar una cita en la fecha indicada
		if(!medico.fechaEnCalendario(fechaYHora, IConstantes.DURACION_CITA)) {
			throw new FechaNoValidaException("El m�dico con NIF " + nifMedico + " no trabaja en la fecha y horas indicadas.");
		}
		
		do {
			// Consultamos las sustituciones del m�dico
			comprobar = false;
			sustituciones = FPSustitucion.consultarPorSustituido(medico.getNif());
			for(Sustitucion sustitucion : sustituciones) {
				// Si alguien va a sustituir al m�dico en el d�a y hora
				// de la cita, nos quedamos con el m�dico sustituto
				if(!comprobar && UtilidadesDominio.fechaIgual(sustitucion.getDia(), fechaYHora, false)) {
					if(sustitucion.horaEnSustitucion(fechaYHora)) {
						medico = sustitucion.getSustituto();
						// Volvemos a repetir el bucle por si alguien va
						// a sustituir al m�dico sustituto
						comprobar = true;
					}
				}
			}
		} while(comprobar);
		
		return medico;
	}

}
