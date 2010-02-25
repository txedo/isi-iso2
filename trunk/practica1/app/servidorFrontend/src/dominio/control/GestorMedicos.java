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
 * Clase encargada de consultar, añadir, modificar y eliminar médicos
 * en el sistema y realizar otras operaciones específicas de los médicos.
 */
public class GestorMedicos {

	// Método para consultar los datos de un médico
	public static Medico consultarMedico(long idSesion, String nif) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(nif == null) {
			throw new NullPointerException("El NIF del médico buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Llamamos al método común para consultar usuarios y médicos
		try {
			usuario = GestorUsuarios.consultarUsuario(nif);
		} catch(UsuarioInexistenteException e) {
			throw new MedicoInexistenteException("No existe ningún médico con el NIF introducido.");
		}
		
		// Nos aseguramos de que el usuario devuelto tenga el rol esperado
		if(usuario.getRol() != RolesUsuario.Medico) {
			throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
		}

		return (Medico)usuario;
	}
	
	// Método para añadir un nuevo médico al sistema
	public static void crearMedico(long idSesion, Medico medico) throws SQLException, MedicoYaExistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a crear no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarMedico);
		
		// Llamamos al método común para crear usuarios y médicos
		try {
			GestorUsuarios.crearUsuario(medico);
		} catch(UsuarioYaExistenteException ex) {
			throw new MedicoYaExistenteException(ex.getMessage());
		}
	}
	
	// Método para modificar un médico existente del sistema
	public static void modificarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarMedico);
		
		// Llamamos al método común para consultar usuarios y médicos
		try {
			GestorUsuarios.modificarUsuario(medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// Método para eliminar un médico del sistema
	public static void eliminarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException {
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a eliminar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarMedico);

		// Llamamos al método común para eliminar usuarios y médicos
		try {
			GestorUsuarios.eliminarUsuario(medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// Método que devuelve las horas de cada día de la semana
	// en las que un médico puede pasar una cita
	public static Hashtable<DiaSemana, Vector<String>> consultarHorarioMedico(long idSesion, String nifMedico) throws SQLException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, NullPointerException, DireccionInexistenteException {
		Hashtable<DiaSemana, Vector<String>> horasDia;
		Usuario usuario;
		Medico medico;
		
		// Comprobamos los parámetros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del médico para el que se quiere consultar el horario no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Generamos la tabla que asocia día de la semana con horas de trabajo
		horasDia = new Hashtable<DiaSemana, Vector<String>>();
		for(DiaSemana dia : DiaSemana.values()) {
			horasDia.put(dia, medico.horasCitas(dia, IConstantes.DURACION_CITA));
		}
		
		return horasDia;
	}
	
	// Método que devuelve todos los médicos de un determinado tipo 
	public static Vector<Medico> consultarMedicosPorTipo(long idSesion, TipoMedico tipo) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, NullPointerException {
		Vector<Medico> medicos;
		Vector<String> nifs;
		
		// Comprobamos los parámetros pasados
		if(tipo == null) {
			throw new NullPointerException("El tipo de los médicos que se quieren buscar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicosTipo);
		
		// Obtenemos los NIFs de todos los médicos del tipo dado
		nifs = FPTipoMedico.consultarMedicos(tipo);
		
		// Recuperamos los médicos con los NIFs anteriores
		medicos = new Vector<Medico>();
		for(String nif : nifs) {
			medicos.add((Medico)FPUsuario.consultar(nif));
		}
		
		return medicos;
	}
	
	// Método que devuelve el médico que daría realmente una cita
	// teniendo en cuenta las sustituciones
	public static Medico consultarMedicoCita(long idSesion, String nifMedico, Date fechaYHora) throws NullPointerException, SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, MedicoInexistenteException, FechaNoValidaException {
		Vector<Sustitucion> sustituciones;
		Medico medico;
		Usuario usuario;
		boolean comprobar;
		
		// Comprobamos los parámetros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del médico para el que se pretende pedir cita no puede ser nulo.");
		}
		if(fechaYHora == null) {
			throw new NullPointerException("La hora en la que se pretende pedir cita no puede ser nula.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicoCita);
	
		// En principio dará la cita el médico previsto
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El médico para el que se pretende pedir cita no es un usuario del sistema con rol de médico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos si el médico realmente podría dar una cita en la fecha indicada
		if(!medico.fechaEnCalendario(fechaYHora, IConstantes.DURACION_CITA)) {
			throw new FechaNoValidaException("El médico con NIF " + nifMedico + " no trabaja en la fecha y horas indicadas.");
		}
		
		do {
			// Consultamos las sustituciones del médico
			comprobar = false;
			sustituciones = FPSustitucion.consultarPorSustituido(medico.getNif());
			for(Sustitucion sustitucion : sustituciones) {
				// Si alguien va a sustituir al médico en el día y hora
				// de la cita, nos quedamos con el médico sustituto
				if(!comprobar && UtilidadesDominio.fechaIgual(sustitucion.getDia(), fechaYHora, false)) {
					if(sustitucion.horaEnSustitucion(fechaYHora)) {
						medico = sustitucion.getSustituto();
						// Volvemos a repetir el bucle por si alguien va
						// a sustituir al médico sustituto
						comprobar = true;
					}
				}
			}
		} while(comprobar);
		
		return medico;
	}

}
