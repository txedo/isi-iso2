package dominio.control;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import persistencia.FPSustitucion;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import dominio.UtilidadesDominio;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.IMedico;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SustitucionInvalidaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de buscar y asignar sustitutos a los médicos.
 */
public class GestorSustituciones {

	// Método que devuelve una lista de posibles sustitutos para un médico en una fecha
	public static Vector<Medico> obtenerPosiblesSustitutos(long idSesion, String nifMedico, Date dia, int horaDesde, int horaHasta) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, CentroSaludInexistenteException, DireccionInexistenteException, MedicoInexistenteException, UsuarioIncorrectoException, NullPointerException, FechaNoValidaException {
		Vector<Medico> sustitutos;
		Vector<String> horasCitas, horasSust;
		Vector<Sustitucion> sustituciones;
		Vector<String> nifs;
		Usuario usuario;
		Medico medico, medicoSust;
		boolean ok;
		
		// Comprobamos los parámetros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del médico para el que se quiere buscar un sustituto no puede ser nulo.");
		}
		if(dia == null) {
			throw new NullPointerException("El día para el que se quiere buscar un sustituto a un médico no puede ser nulo.");			
		}

		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarSustitutosPosibles);

		// Recuperamos los datos del médico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que los datos de la sustitución sean válidos
		if(UtilidadesDominio.fechaAnterior(dia, new Date(), false)) {
			throw new FechaNoValidaException("No se pueden buscar sustitutos para días anteriores al actual.");
		}
		if(horaDesde < IConstantes.HORA_INICIO_JORNADA || horaDesde > IConstantes.HORA_FIN_JORNADA) {
			throw new FechaNoValidaException("La hora inicial de la sustitución no es válida.");
		}
		if(horaHasta < IConstantes.HORA_INICIO_JORNADA || horaHasta > IConstantes.HORA_FIN_JORNADA) {
			throw new FechaNoValidaException("La hora final de la sustitución no es válida.");
		}
		if(horaHasta <= horaDesde) {
			throw new FechaNoValidaException("La hora final de la sustitución no es superior a la inicial.");
		}
		
		// Obtenemos las horas de las citas que se van a tener que sustituir
		horasCitas = horasTrabajo(medico, dia, horaDesde, horaHasta);
		if(horasCitas.size() == 0) {
			throw new FechaNoValidaException("El médico que se quiere sustituir no trabaja en la fecha y horas indicadas.");
		}
		
		// Comprobamos que no haya ya alguien sustituyendo al médico
		// en todo o parte del rango de horas indicado
		sustituciones = FPSustitucion.consultarPorSustituido(medico.getNif());
		for(Sustitucion sustitucion : sustituciones) {
			if(UtilidadesDominio.fechaIgual(dia, sustitucion.getDia(), false)
			 && sustitucion.horaEnSustitucion(horaDesde, horaHasta)) {
				throw new FechaNoValidaException("El médico que se quiere sustituir ya está siendo sustituido por otro médico en todas o algunas de las horas indicadas.");
			}
		}
		
		// Comprobamos que el médico no esté sustituyendo a otro
		// médico en todo o parte del rango de horas indicado
		sustituciones = FPSustitucion.consultarPorSustituto(medico.getNif());
		for(Sustitucion sustitucion : sustituciones) {
			if(UtilidadesDominio.fechaIgual(dia, sustitucion.getDia(), false)
			 && sustitucion.horaEnSustitucion(horaDesde, horaHasta)) {
				throw new FechaNoValidaException("El médico que se quiere sustituir tiene una sustitución asignada en todas o algunas de las horas indicadas.");
			}
		}
		
		// Obtenemos los médicos del sistema que son del mismo tipo
		// que el médico pasado como parámetro, para limitar un
		// poco la búsqueda de sustitutos
		nifs = FPTipoMedico.consultarMedicos(medico.getTipoMedico());
		
		// Nos quedamos con los médicos encontrados que realmente pueden
		// hacer una sustitución en la fecha y hora dadas
		sustitutos = new Vector<Medico>();
		for(String nif : nifs) {
			
			// El médico sustituto debe ser exactamente del mismo tipo (p.ej.
			// si son especialistas deben serlo de la misma especialidad) y
			// trabajar en el mismo centro que el médico a sustituir
			medicoSust = (Medico)FPUsuario.consultar(nif);
			if(!medicoSust.equals(medico)
			   && medicoSust.getCentroSalud().equals(medico.getCentroSalud())
			   && medicoSust.getTipoMedico().equals(medico.getTipoMedico())) {
				
				// Comprobamos que este médico no esté ya siendo sustituido
				// en las horas en las que se debe hacer la sustitución
				ok = true;
				sustituciones = FPSustitucion.consultarPorSustituido(medicoSust.getNif());
				for(Sustitucion sustitucion : sustituciones) {
					if(UtilidadesDominio.fechaIgual(dia, sustitucion.getDia(), false)
					 && sustitucion.horaEnSustitucion(horaDesde, horaHasta)) {
						// Este médico no está disponible para
						// hacer la sustitución
						ok = false;
					}
				}

				if(ok) {
					
					// Obtenemos las horas en los que el médico sustituto
					// tiene ya trabajo asignado (incluyendo las horas
					// de las sustituciones)
					horasSust = horasTrabajo(medicoSust, dia, horaDesde, horaHasta);
	
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
	
	// Método que asigna un sustituto a un médico en una determinada fecha
	public static Vector<Sustitucion> establecerSustituto(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws NullPointerException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, SQLException, CentroSaludInexistenteException, DireccionInexistenteException, UsuarioIncorrectoException, FechaNoValidaException, SustitucionInvalidaException {
		Vector<Sustitucion> sustituciones;
		Vector<Medico> medicos;
		Usuario usuario;
		Medico sustitutoReal;
		Calendar calend;
		Sustitucion sustitucion;
		int horaDesdeN, horaHastaN;
		
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
		if(horaDesde == null) {
			throw new NullPointerException("La hora de inicio de la sustitución no puede ser nula.");
		}
		if(horaHasta == null) {
			throw new NullPointerException("La hora de final de la sustitución no puede ser nula.");
		}
		if(sustituto == null) {
			throw new NullPointerException("El médico que va a realizar la sustitución no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EstablecerSustituto);
	
		// Comprobamos que existan los médicos sustituto y sustituido
		try {
			usuario = FPUsuario.consultar(medico.getNif());
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El médico sustituido no es un usuario del sistema con rol de médico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		try {
			usuario = FPUsuario.consultar(sustituto.getNif());
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El médico sustituto no es un usuario del sistema con rol de médico.");
			}
			sustitutoReal = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Extraemos la hora de inicio y de final
		calend = Calendar.getInstance();
		calend.setTime(horaDesde);
		horaDesdeN = calend.get(Calendar.HOUR_OF_DAY);
		calend.setTime(horaHasta);
		horaHastaN = calend.get(Calendar.HOUR_OF_DAY);
		
		// Vemos si el médico sustituto propuesto es uno de los
		// que devuelve el método 'obtenerPosiblesSustitutos'
		// (dentro de este método se comprobará que la fecha y
		// las horas de la sustitución son válidas)
		for(Date dia : dias) {
			medicos = obtenerPosiblesSustitutos(idSesion, medico.getNif(), dia, horaDesdeN, horaHastaN);
			if(!medicos.contains(sustituto)) {
				throw new SustitucionInvalidaException("El médico sustituto propuesto no puede realizar la sustitución del día " + dia.toString() + ".");
			}
		}
		
		// Si el médico puede hacer la sustitución todos los días,
		// generamos y guardamos las sustituciones en la base de datos
		sustituciones = new Vector<Sustitucion>();
		for(Date dia : dias) {
			sustitucion = new Sustitucion();
			sustitucion.setDia(dia);
			sustitucion.setHoraInicio(horaDesdeN);
			sustitucion.setHoraFinal(horaHastaN);
			sustitucion.setMedico(medico);
			sustitucion.setSustituto(sustitutoReal);
			FPSustitucion.insertar(sustitucion);
			sustituciones.add(sustitucion);
		}
		
		return sustituciones;
	}
	
	private static Vector<String> horasTrabajo(Medico medico, Date dia, int horaDesde, int horaHasta) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		Vector<Sustitucion> sustituciones;
		Vector<String> horasCitas;
		
		// Obtenemos las horas en los que el médico sustituto
		// tiene que pasar sus citas en el día indicado
		horasCitas = medico.horasCitas(UtilidadesDominio.diaFecha(dia), horaDesde, horaHasta, IConstantes.DURACION_CITA);
		// Añadimos a la lista de horas aquellas en las que el médico
		// también tiene que trabajar por sustituir a otro médico,
		// y esto lo hacemos recursivamente por si ese médico estaba
		// sustituyendo a otro médico diferente
		sustituciones = FPSustitucion.consultarPorSustituto(medico.getNif());
		for(Sustitucion sustitucion : sustituciones) {
			if(UtilidadesDominio.fechaIgual(dia, sustitucion.getDia(), false)) {
				horasCitas.addAll(horasTrabajo(sustitucion.getMedico(), dia, horaDesde, horaHasta));
			}
		}
		
		return horasCitas;
	}
	
}
