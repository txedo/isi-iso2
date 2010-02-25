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
 * Clase encargada de buscar y asignar sustitutos a los m�dicos.
 */
public class GestorSustituciones {

	// M�todo que devuelve una lista de posibles sustitutos para un m�dico en una fecha
	public static Vector<Medico> obtenerPosiblesSustitutos(long idSesion, String nifMedico, Date dia, int horaDesde, int horaHasta) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, CentroSaludInexistenteException, DireccionInexistenteException, MedicoInexistenteException, UsuarioIncorrectoException, NullPointerException, FechaNoValidaException {
		Vector<Medico> sustitutos;
		Vector<String> horasCitas, horasSust;
		Vector<Sustitucion> sustituciones;
		Vector<String> nifs;
		Usuario usuario;
		Medico medico, medicoSust;
		boolean ok;
		
		// Comprobamos los par�metros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del m�dico para el que se quiere buscar un sustituto no puede ser nulo.");
		}
		if(dia == null) {
			throw new NullPointerException("El d�a para el que se quiere buscar un sustituto a un m�dico no puede ser nulo.");			
		}

		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarSustitutosPosibles);

		// Recuperamos los datos del m�dico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un m�dico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que los datos de la sustituci�n sean v�lidos
		if(UtilidadesDominio.fechaAnterior(dia, new Date(), false)) {
			throw new FechaNoValidaException("No se pueden buscar sustitutos para d�as anteriores al actual.");
		}
		if(horaDesde < IConstantes.HORA_INICIO_JORNADA || horaDesde > IConstantes.HORA_FIN_JORNADA) {
			throw new FechaNoValidaException("La hora inicial de la sustituci�n no es v�lida.");
		}
		if(horaHasta < IConstantes.HORA_INICIO_JORNADA || horaHasta > IConstantes.HORA_FIN_JORNADA) {
			throw new FechaNoValidaException("La hora final de la sustituci�n no es v�lida.");
		}
		if(horaHasta <= horaDesde) {
			throw new FechaNoValidaException("La hora final de la sustituci�n no es superior a la inicial.");
		}
		
		// Obtenemos las horas de las citas que se van a tener que sustituir
		horasCitas = horasTrabajo(medico, dia, horaDesde, horaHasta);
		if(horasCitas.size() == 0) {
			throw new FechaNoValidaException("El m�dico que se quiere sustituir no trabaja en la fecha y horas indicadas.");
		}
		
		// Comprobamos que no haya ya alguien sustituyendo al m�dico
		// en todo o parte del rango de horas indicado
		sustituciones = FPSustitucion.consultarPorSustituido(medico.getNif());
		for(Sustitucion sustitucion : sustituciones) {
			if(UtilidadesDominio.fechaIgual(dia, sustitucion.getDia(), false)
			 && sustitucion.horaEnSustitucion(horaDesde, horaHasta)) {
				throw new FechaNoValidaException("El m�dico que se quiere sustituir ya est� siendo sustituido por otro m�dico en todas o algunas de las horas indicadas.");
			}
		}
		
		// Comprobamos que el m�dico no est� sustituyendo a otro
		// m�dico en todo o parte del rango de horas indicado
		sustituciones = FPSustitucion.consultarPorSustituto(medico.getNif());
		for(Sustitucion sustitucion : sustituciones) {
			if(UtilidadesDominio.fechaIgual(dia, sustitucion.getDia(), false)
			 && sustitucion.horaEnSustitucion(horaDesde, horaHasta)) {
				throw new FechaNoValidaException("El m�dico que se quiere sustituir tiene una sustituci�n asignada en todas o algunas de las horas indicadas.");
			}
		}
		
		// Obtenemos los m�dicos del sistema que son del mismo tipo
		// que el m�dico pasado como par�metro, para limitar un
		// poco la b�squeda de sustitutos
		nifs = FPTipoMedico.consultarMedicos(medico.getTipoMedico());
		
		// Nos quedamos con los m�dicos encontrados que realmente pueden
		// hacer una sustituci�n en la fecha y hora dadas
		sustitutos = new Vector<Medico>();
		for(String nif : nifs) {
			
			// El m�dico sustituto debe ser exactamente del mismo tipo (p.ej.
			// si son especialistas deben serlo de la misma especialidad) y
			// trabajar en el mismo centro que el m�dico a sustituir
			medicoSust = (Medico)FPUsuario.consultar(nif);
			if(!medicoSust.equals(medico)
			   && medicoSust.getCentroSalud().equals(medico.getCentroSalud())
			   && medicoSust.getTipoMedico().equals(medico.getTipoMedico())) {
				
				// Comprobamos que este m�dico no est� ya siendo sustituido
				// en las horas en las que se debe hacer la sustituci�n
				ok = true;
				sustituciones = FPSustitucion.consultarPorSustituido(medicoSust.getNif());
				for(Sustitucion sustitucion : sustituciones) {
					if(UtilidadesDominio.fechaIgual(dia, sustitucion.getDia(), false)
					 && sustitucion.horaEnSustitucion(horaDesde, horaHasta)) {
						// Este m�dico no est� disponible para
						// hacer la sustituci�n
						ok = false;
					}
				}

				if(ok) {
					
					// Obtenemos las horas en los que el m�dico sustituto
					// tiene ya trabajo asignado (incluyendo las horas
					// de las sustituciones)
					horasSust = horasTrabajo(medicoSust, dia, horaDesde, horaHasta);
	
					// Vemos si el m�dico sustituto no tiene trabajo
					// en las horas que se deben sustituir
					ok = true;
					for(String hora : horasCitas) {
						if(horasSust.contains(hora)) {
							// Este m�dico no puede ser el sustituto
							// porque se solapan los horarios
							ok = false;
						}
					}
					
					// Este m�dico es un sustituto v�lido
					if(ok) {
						sustitutos.add(medicoSust);
					}
					
				}
				
			}
		}
		
		return sustitutos;
	}
	
	// M�todo que asigna un sustituto a un m�dico en una determinada fecha
	public static Vector<Sustitucion> establecerSustituto(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws NullPointerException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, SQLException, CentroSaludInexistenteException, DireccionInexistenteException, UsuarioIncorrectoException, FechaNoValidaException, SustitucionInvalidaException {
		Vector<Sustitucion> sustituciones;
		Vector<Medico> medicos;
		Usuario usuario;
		Medico sustitutoReal;
		Calendar calend;
		Sustitucion sustitucion;
		int horaDesdeN, horaHastaN;
		
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
		if(horaDesde == null) {
			throw new NullPointerException("La hora de inicio de la sustituci�n no puede ser nula.");
		}
		if(horaHasta == null) {
			throw new NullPointerException("La hora de final de la sustituci�n no puede ser nula.");
		}
		if(sustituto == null) {
			throw new NullPointerException("El m�dico que va a realizar la sustituci�n no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EstablecerSustituto);
	
		// Comprobamos que existan los m�dicos sustituto y sustituido
		try {
			usuario = FPUsuario.consultar(medico.getNif());
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El m�dico sustituido no es un usuario del sistema con rol de m�dico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		try {
			usuario = FPUsuario.consultar(sustituto.getNif());
			if(usuario.getRol() != RolesUsuario.Medico) {
				throw new MedicoInexistenteException("El m�dico sustituto no es un usuario del sistema con rol de m�dico.");
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
		
		// Vemos si el m�dico sustituto propuesto es uno de los
		// que devuelve el m�todo 'obtenerPosiblesSustitutos'
		// (dentro de este m�todo se comprobar� que la fecha y
		// las horas de la sustituci�n son v�lidas)
		for(Date dia : dias) {
			medicos = obtenerPosiblesSustitutos(idSesion, medico.getNif(), dia, horaDesdeN, horaHastaN);
			if(!medicos.contains(sustituto)) {
				throw new SustitucionInvalidaException("El m�dico sustituto propuesto no puede realizar la sustituci�n del d�a " + dia.toString() + ".");
			}
		}
		
		// Si el m�dico puede hacer la sustituci�n todos los d�as,
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
		
		// Obtenemos las horas en los que el m�dico sustituto
		// tiene que pasar sus citas en el d�a indicado
		horasCitas = medico.horasCitas(UtilidadesDominio.diaFecha(dia), horaDesde, horaHasta, IConstantes.DURACION_CITA);
		// A�adimos a la lista de horas aquellas en las que el m�dico
		// tambi�n tiene que trabajar por sustituir a otro m�dico,
		// y esto lo hacemos recursivamente por si ese m�dico estaba
		// sustituyendo a otro m�dico diferente
		sustituciones = FPSustitucion.consultarPorSustituto(medico.getNif());
		for(Sustitucion sustitucion : sustituciones) {
			if(UtilidadesDominio.fechaIgual(dia, sustitucion.getDia(), false)) {
				horasCitas.addAll(horasTrabajo(sustitucion.getMedico(), dia, horaDesde, horaHasta));
			}
		}
		
		return horasCitas;
	}
	
}
