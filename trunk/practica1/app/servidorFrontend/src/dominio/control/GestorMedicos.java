package dominio.control;

import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import persistencia.FPSustitucion;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuarios;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Utilidades;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.FechaNoValidaException;
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
	public static Medico consultarMedico(long idSesion, String dni) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(dni == null) {
			throw new NullPointerException("El DNI del médico buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Llamamos al método equivalente para usuarios
		try {
			usuario = GestorUsuarios.consultarUsuario(idSesion, dni);
		} catch(UsuarioInexistenteException e) {
			throw new MedicoInexistenteException(e.getMessage());
		}
		
		// Nos aseguramos de que el usuario devuelto tenga el rol esperado
		if(usuario.getRol() != RolesUsuarios.Medico) {
			throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico.");
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
		
		// Llamamos al método equivalente para usuarios
		try {
			GestorUsuarios.crearUsuario(idSesion, medico);
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
		
		// Llamamos al método equivalente para usuarios
		try {
			GestorUsuarios.modificarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// Método para eliminar un médico del sistema
	public static void eliminarMedico(long idSesion, Medico medico) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a eliminar no puede ser nulo.");
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
	
	// Método que devuelve las horas de cada día de la semana
	// en las que un médico puede pasar una cita
	public static Hashtable<DiaSemana, Vector<String>> consultarHorarioMedico(long idSesion, String dniMedico) throws SQLException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, NullPointerException, DireccionInexistenteException {
		Hashtable<DiaSemana, Vector<String>> horasDia;
		Usuario usuario;
		Medico medico;
		
		// Comprobamos los parámetros pasados
		if(dniMedico == null) {
			throw new NullPointerException("El DNI del médico para el que se quiere consultar el horario no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(dniMedico);
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico.");
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
	public static Vector<Medico> consultarMedicosPorTipo(long idSesion, CategoriasMedico tipoMedico) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		Vector<Medico> medicos;
		Vector<String> nifs;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicosTipo);
		
		// Obtenemos los NIFs de todos los médicos del tipo dado
		nifs = FPTipoMedico.consultarMedicos(tipoMedico);
		
		// Recuperamos los médicos con los NIFs anteriores
		medicos = new Vector<Medico>();
		for(String nif : nifs) {
			medicos.add((Medico)FPUsuario.consultar(nif));
		}
		
		return medicos;
	}
	
	public static Vector<Medico> obtenerPosiblesSustitutos(long idSesion, String dniMedico, Date dia, int horaDesde, int horaHasta) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, CentroSaludInexistenteException, DireccionInexistenteException, MedicoInexistenteException, UsuarioIncorrectoException, NullPointerException, FechaNoValidaException {
		Vector<Medico> sustitutos;
		Vector<String> horasCitas, horasSust;
		Vector<Sustitucion> sustituciones;
		Vector<String> dnis;
		Usuario usuario;
		Medico medico, medicoSust;
		boolean ok;
		
		// Comprobamos los parámetros pasados
		if(dniMedico == null) {
			throw new NullPointerException("El DNI del médico para el que se quiere buscar un sustituto no puede ser nulo.");
		}
		if(dia == null) {
			throw new NullPointerException("El día para el que se quiere buscar un sustituto a un médico no puede ser nulo.");			
		}

		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarSustitutosPosibles);

		// Recuperamos los datos del médico
		try {
			usuario = FPUsuario.consultar(dniMedico);
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que los datos de la sustitución sean válidos
		if(Utilidades.fechaAnterior(dia, new Date(), false)) {
			throw new FechaNoValidaException("No se pueden buscar sustitutos para días anteriores al actual.");
		}
		if(horaDesde < 0 || horaDesde > 23) {
			throw new FechaNoValidaException("La hora inicial de la sustitución no es válida.");
		}
		if(horaHasta < 0 || horaHasta > 23) {
			throw new FechaNoValidaException("La hora final de la sustitución no es válida.");
		}
		if(horaHasta == horaDesde || (horaHasta < horaDesde && horaHasta != 0)) {
			throw new FechaNoValidaException("La hora final de la sustitución no es superior a la inicial.");
		}
		
		// Obtenemos las horas de las citas que se van a tener que sustituir
		horasCitas = medico.horasCitas(Utilidades.diaFecha(dia), horaDesde, horaHasta, IConstantes.DURACION_CITA);
		if(horasCitas.size() == 0) {
			throw new FechaNoValidaException("El médico con DNI " + dniMedico + " no trabaja en la fecha y horas indicadas.");
		}
		
		// Obtenemos los médicos del sistema que son del mismo tipo
		// que el médico pasado como parámetro, para limitar un
		// poco la búsqueda de sustitutos
		dnis = FPTipoMedico.consultarMedicos(medico.getTipoMedico().getCategoria());
		
		// Nos quedamos con los médicos encontrados que realmente pueden
		// hacer una sustitución en la fecha y hora dadas
		sustitutos = new Vector<Medico>();
		for(String dni : dnis) {
			
			// El médico sustituto debe ser exactamente del mismo tipo (p.ej.
			// si son especialistas deben serlo de la misma especialidad) y
			// trabajar en el mismo centro que el médico a sustituir
			medicoSust = (Medico)FPUsuario.consultar(dni);
			if(medicoSust.getCentroSalud().equals(medico.getCentroSalud())
			   && medicoSust.getTipoMedico().equals(medico.getTipoMedico())) {
				
				// Comprobamos que este médico no esté ya siendo sustituido
				// en las horas en las que se debe hacer la sustitución
				ok = true;
				sustituciones = FPSustitucion.consultarPorSustituido(medicoSust.getDni());
				for(Sustitucion sustitucion : sustituciones) {
					if(Utilidades.fechaIgual(dia, sustitucion.getDia(), false)) {
						if(sustitucion.horaEnSustitucion(horaDesde, horaHasta)) {
							// Este médico no está disponible para
							// hacer la sustitución
							ok = false;
						}
					}
				}

				if(ok) {
					
					// Obtenemos las horas en los que el médico sustituto
					// tiene que pasar sus citas en el día indicado
					horasSust = medicoSust.horasCitas(Utilidades.diaFecha(dia), horaDesde, horaHasta, IConstantes.DURACION_CITA);
					// Añadimos a la lista de horas aquellas en las que el médico
					// también tiene que trabajar por sustituir a otro médico
					sustituciones = FPSustitucion.consultarPorSustituto(medicoSust.getDni());
					for(Sustitucion sustitucion : sustituciones) {
						if(Utilidades.fechaIgual(dia, sustitucion.getDia(), false)) {
							horasSust.addAll(sustitucion.getMedico().horasCitas(Utilidades.diaFecha(dia), sustitucion.getHoraInicio(), sustitucion.getHoraFinal(), IConstantes.DURACION_CITA));
						}
					}
	
					// Vemos si el médico sustituto no tiene trabajo
					// en las horas que se deben sustituir
					ok = true;
					for(String hora : horasCitas) {
						if(horasSust.contains(hora)) {
							// Este médico no puede ser el sustituto
							// porque se solapan los horarios
							ok = false;
						}
					}
					
					// Este médico es un sustituto válido
					if(ok) {
						sustitutos.add(medicoSust);
					}
					
				}
				
			}
		}
		
		return sustitutos;
	}
	
	//TODO:Los siguientes métodos no se han revisado!
	
	public static void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Medico sustituto) throws SQLException, MedicoInexistenteException, SesionInvalidaException, OperacionIncorrectaException, SustitucionInvalidaException, UsuarioIncorrectoException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Vector<Sustitucion> sustituciones;
		Sustitucion sustitucion;
		
		// Comprobamos los parámetros pasados
		if(medico == null) {
			throw new NullPointerException("El médico que se va a sustituir no puede ser nulo.");
		}
		if(dias == null) {
			throw new NullPointerException("Los días en los que se va a realizar la sustitución no pueden ser nulos.");			
		}
		if(dias.contains(null)) {
			throw new NullPointerException("Los días en los que se va a realizar la sustitución no pueden ser nulos.");
		}
		if(sustituto == null) {
			throw new NullPointerException("El médico al que se le va a asignar una sustitución no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EstablecerSustituto);
		
		// TODO: ¿Se pueden sustituir médicos de todos los tipos?
		// TODO: ¿Se pueden sustituir médicos de un tipo con médicos de otro tipo?
		
		// Comprobamos que ni el médico sustituido ni el sustituto tiene que
		// hacer ya una sustitución alguno de los días solicitados
		sustituciones = FPSustitucion.consultarPorSustituto(sustituto.getDni());
		for(Sustitucion sust : sustituciones) {
			if(dias.contains(sust.getDia())) {
				throw new SustitucionInvalidaException("El médico sustituto ya va a hacer una sustitución en alguno de los días solicitados.");
			}
		}
		sustituciones = FPSustitucion.consultarPorSustituido(medico.getDni());
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
	
}
