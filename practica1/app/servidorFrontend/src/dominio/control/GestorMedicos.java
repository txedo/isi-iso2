package dominio.control;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import persistencia.FPBeneficiario;
import persistencia.FPSustitucion;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import persistencia.FuncionesPersistencia;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.IMedico;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuarios;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Utilidades;
import excepciones.BeneficiarioInexistenteException;
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

		// Todo lo relacionado con eliminar un m�dico (como reasignar
		// nuevos m�dicos a los beneficiarios) no se debe hacer aqu�,
		// sino en eliminarUsuario, porque si se llama a eliminarUsuario
		// con un m�dico no se ejecutar�a el mismo c�digo !!!
		
		// Llamamos al m�todo equivalente para usuarios
		try {
			GestorUsuarios.eliminarUsuario(idSesion, medico);
		} catch(UsuarioInexistenteException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
	}
	
	// M�todo que devuelve las horas de cada d�a de la semana
	// en las que un m�dico puede pasar una cita
	public static Hashtable<DiaSemana, Vector<String>> consultarHorarioMedico(long idSesion, String dniMedico) throws SQLException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, NullPointerException, DireccionInexistenteException {
		Hashtable<DiaSemana, Vector<String>> horasDia;
		Usuario usuario;
		Medico medico;
		
		// Comprobamos los par�metros pasados
		if(dniMedico == null) {
			throw new NullPointerException("El DNI del m�dico para el que se quiere consultar el horario no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Comprobamos que exista el m�dico
		try {
			usuario = FPUsuario.consultar(dniMedico);
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un m�dico.");
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
	public static Vector<Medico> consultarMedicosPorTipo(long idSesion, Object... informacion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		Vector<Medico> medicos;
		Vector<String> nifs;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedicosTipo);
		
		// Obtenemos los NIFs de todos los m�dicos del tipo dado
		nifs = FPTipoMedico.consultarMedicos(informacion);
		
		// Recuperamos los m�dicos con los NIFs anteriores
		medicos = new Vector<Medico>();
		for(String nif : nifs) {
			medicos.add((Medico)FPUsuario.consultar(nif));
		}
		
		return medicos;
	}
	
	// M�todo que devuelve una lista de posibles sustitutos para un m�dico en una fecha
	public static Vector<Medico> obtenerPosiblesSustitutos(long idSesion, String dniMedico, Date dia, int horaDesde, int horaHasta) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, CentroSaludInexistenteException, DireccionInexistenteException, MedicoInexistenteException, UsuarioIncorrectoException, NullPointerException, FechaNoValidaException {
		Vector<Medico> sustitutos;
		Vector<String> horasCitas, horasSust;
		Vector<Sustitucion> sustituciones;
		Vector<String> dnis;
		Usuario usuario;
		Medico medico, medicoSust;
		boolean ok;
		
		// Comprobamos los par�metros pasados
		if(dniMedico == null) {
			throw new NullPointerException("El DNI del m�dico para el que se quiere buscar un sustituto no puede ser nulo.");
		}
		if(dia == null) {
			throw new NullPointerException("El d�a para el que se quiere buscar un sustituto a un m�dico no puede ser nulo.");			
		}

		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarSustitutosPosibles);

		// Recuperamos los datos del m�dico
		try {
			usuario = FPUsuario.consultar(dniMedico);
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un m�dico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que los datos de la sustituci�n sean v�lidos
		if(Utilidades.fechaAnterior(dia, new Date(), false)) {
			throw new FechaNoValidaException("No se pueden buscar sustitutos para d�as anteriores al actual.");
		}
		if(horaDesde < 0 || horaDesde > 23) {
			throw new FechaNoValidaException("La hora inicial de la sustituci�n no es v�lida.");
		}
		if(horaHasta < 0 || horaHasta > 23) {
			throw new FechaNoValidaException("La hora final de la sustituci�n no es v�lida.");
		}
		if(horaHasta == horaDesde || (horaHasta < horaDesde && horaHasta != 0)) {
			throw new FechaNoValidaException("La hora final de la sustituci�n no es superior a la inicial.");
		}
		
		// Obtenemos las horas de las citas que se van a tener que sustituir
		horasCitas = medico.horasCitas(Utilidades.diaFecha(dia), horaDesde, horaHasta, IConstantes.DURACION_CITA);
		if(horasCitas.size() == 0) {
			throw new FechaNoValidaException("El m�dico que se quiere sustituir no trabaja en la fecha y horas indicadas.");
		}
		
		// Obtenemos los m�dicos del sistema que son del mismo tipo
		// que el m�dico pasado como par�metro, para limitar un
		// poco la b�squeda de sustitutos
		dnis = FPTipoMedico.consultarMedicos(medico.getTipoMedico().getCategoria());
		
		// Nos quedamos con los m�dicos encontrados que realmente pueden
		// hacer una sustituci�n en la fecha y hora dadas
		sustitutos = new Vector<Medico>();
		for(String dni : dnis) {
			
			// El m�dico sustituto debe ser exactamente del mismo tipo (p.ej.
			// si son especialistas deben serlo de la misma especialidad) y
			// trabajar en el mismo centro que el m�dico a sustituir
			medicoSust = (Medico)FPUsuario.consultar(dni);
			if(medicoSust.getCentroSalud().equals(medico.getCentroSalud())
			   && medicoSust.getTipoMedico().equals(medico.getTipoMedico())) {
				
				// Comprobamos que este m�dico no est� ya siendo sustituido
				// en las horas en las que se debe hacer la sustituci�n
				ok = true;
				sustituciones = FPSustitucion.consultarPorSustituido(medicoSust.getDni());
				for(Sustitucion sustitucion : sustituciones) {
					if(Utilidades.fechaIgual(dia, sustitucion.getDia(), false)) {
						if(sustitucion.horaEnSustitucion(horaDesde, horaHasta)) {
							// Este m�dico no est� disponible para
							// hacer la sustituci�n
							ok = false;
						}
					}
				}

				if(ok) {
					
					// Obtenemos las horas en los que el m�dico sustituto
					// tiene que pasar sus citas en el d�a indicado
					horasSust = medicoSust.horasCitas(Utilidades.diaFecha(dia), horaDesde, horaHasta, IConstantes.DURACION_CITA);
					// A�adimos a la lista de horas aquellas en las que el m�dico
					// tambi�n tiene que trabajar por sustituir a otro m�dico
					sustituciones = FPSustitucion.consultarPorSustituto(medicoSust.getDni());
					for(Sustitucion sustitucion : sustituciones) {
						if(Utilidades.fechaIgual(dia, sustitucion.getDia(), false)) {
							horasSust.addAll(sustitucion.getMedico().horasCitas(Utilidades.diaFecha(dia), sustitucion.getHoraInicio(), sustitucion.getHoraFinal(), IConstantes.DURACION_CITA));
						}
					}
	
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
	public static void establecerSustituto(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws NullPointerException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, SQLException, CentroSaludInexistenteException, DireccionInexistenteException, UsuarioIncorrectoException, FechaNoValidaException, SustitucionInvalidaException {
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
			usuario = FPUsuario.consultar(medico.getDni());
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El m�dico sustituido no es un usuario del sistema con rol de m�dico.");
			}
			medico = (Medico)usuario;
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		try {
			usuario = FPUsuario.consultar(sustituto.getDni());
			if(usuario.getRol() != RolesUsuarios.Medico) {
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
		for(Date dia : dias) {
			medicos = obtenerPosiblesSustitutos(idSesion, medico.getDni(), dia, horaDesdeN, horaHastaN);
			if(!medicos.contains(sustituto)) {
				throw new SustitucionInvalidaException("El m�dico sustituto propuesto no puede realizar la sustituci�n del d�a " + dia.toString() + ".");
			}
		}
		
		// Si el m�dico puede hacer la sustituci�n todos los d�as,
		// generamos y guardamos las sustituciones en la base de datos
		for(Date dia : dias) {
			sustitucion = new Sustitucion();
			sustitucion.setDia(dia);
			sustitucion.setHoraInicio(horaDesdeN);
			sustitucion.setHoraFinal(horaHastaN);
			sustitucion.setMedico(medico);
			sustitucion.setSustituto(sustitutoReal);
			FPSustitucion.insertar(sustitucion);
		}
	}
	
}