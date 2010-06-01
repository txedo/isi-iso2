package dominio.control;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import dominio.UtilidadesDominio;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.SesionUsuario;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.DireccionInexistenteException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;
import persistencia.FPBeneficiario;
import persistencia.FPCita;
import persistencia.FPSustitucion;
import persistencia.FPUsuario;
import persistencia.FPVolante;

/**
 * Clase encargada de solicitar, recuperar y eliminar citas que tienen los
 * beneficiarios para acudir a la consulta de los médicos.
 */
public class GestorCitas {

	// Método para obtener todas las citas de un beneficiario
	public static Vector<Cita> consultarCitas(long idSesion, String nif) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Vector<Cita> citas;
		
		// Comprobamos los parámetros pasados
		if(nif == null) {
			throw new NullPointerException("El NIF del beneficiario para el que se quieren buscar las citas no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasBeneficiario);

		// Comprobamos que exista el beneficiario
		FPBeneficiario.consultarPorNIF(nif);
		
		// Recuperamos las citas del beneficiario
		citas = FPCita.consultarPorBeneficiario(nif);
		
		return citas;
	}
	
	// Método para consultar una cita, conociendo su id
	public static Cita consultarCita(long idSesion, long idCita) throws SQLException, CitaNoValidaException, SesionInvalidaException, OperacionIncorrectaException {
		Cita cita;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasBeneficiario);
		
		// Recuperamos la cita del beneficiario
		cita = FPCita.consultar(idCita);
		
		return cita;
	}
	
	// Método para obtener todas las citas pendientes de un beneficiario
	public static Vector<Cita> consultarCitasPendientesBeneficiario(long idSesion, String nif) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Vector<Cita> citas, pendientes;
		Date fechaActual;
		
		// Comprobamos los parámetros pasados
		if(nif == null) {
			throw new NullPointerException("El NIF del beneficiario para el que se quieren buscar las citas no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasBeneficiario);

		// Comprobamos que exista el beneficiario
		FPBeneficiario.consultarPorNIF(nif);
		
		// Recuperamos las citas del beneficiario
		citas = FPCita.consultarPorBeneficiario(nif);
		
		// Nos quedamos con las citas posteriores a la fecha y hora actuales
		fechaActual = new Date();
		pendientes = new Vector<Cita>();
		for(Cita cita : citas) {
			if(cita.getFechaYHora().after(fechaActual)) {
				pendientes.add(cita);
			}
		}
		
		return pendientes;
	}
	
	// Método para obtener todas las citas de un médico
	@SuppressWarnings("deprecation")
	public static Vector<Cita> consultarCitasMedico(long idSesion, String nifMedico) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, NullPointerException {
		Vector<Cita> citas;
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del médico para el que se quieren buscar las citas puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasMedico);
		
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != Roles.Médico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Obtenemos las citas que ya tiene asignadas el médico
		citas = consultarCitasMedico(nifMedico);
		
		return citas;
	}
	
	// Método para obtener todas las citas pendientes de un médico
	public static Vector<Cita> consultarCitasPendientesMedico(long idSesion, String nifMedico) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, BeneficiarioInexistenteException, MedicoInexistenteException, NullPointerException {
		Vector<Cita> citas, pendientes;
		Date fechaActual;
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del médico para el que se quieren buscar las citas no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasMedico);

		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != Roles.Médico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Recuperamos las citas del médico
		citas = consultarCitasMedico(nifMedico);
		
		// Nos quedamos sólo con las citas posteriores a la fecha y hora actuales
		fechaActual = new Date();
		pendientes = new Vector<Cita>();
		for(Cita cita : citas) {
			if(cita.getFechaYHora().after(fechaActual)) {
				pendientes.add(cita);
			}
		}
		
		return pendientes;
	}
	
	// Método para obtener todas las citas del médico que realiza la operación
	@SuppressWarnings("deprecation")
	public static Vector<Cita> consultarCitasPropiasMedico(long idSesion) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException {
		Vector<Cita> citas;
		Usuario usuario;
		Sesion sesion;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasPropiasMedico);

		// Obtenemos el usuario que ha ejecutado la operación a partir de su sesión
		sesion = GestorSesiones.getSesion(idSesion);
		if(sesion instanceof SesionUsuario) {
			usuario = ((SesionUsuario)sesion).getUsuario();
			if(usuario.getRol() != Roles.Médico) {
				throw new MedicoInexistenteException("Sólo los usuarios con rol de médico pueden consultar sus propias citas.");
			}
		} else {
			throw new MedicoInexistenteException("Sólo los usuarios con rol de médico pueden consultar sus propias citas.");
		}

		// Obtenemos las citas que tiene asignadas el médico
		citas = consultarCitasMedico(usuario.getNif());
		
		return citas;
	}
	
	// Método para obtener todas las citas pendientes de un médico
	public static Vector<Cita> consultarCitasPendientesPropiasMedico(long idSesion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, BeneficiarioInexistenteException, MedicoInexistenteException {
		Vector<Cita> citas, pendientes;
		Date fechaActual;
		Usuario usuario;
		Sesion sesion;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasPropiasMedico);

		// Obtenemos el usuario que ha ejecutado la operación a partir de su sesión
		sesion = GestorSesiones.getSesion(idSesion);
		if(sesion instanceof SesionUsuario) {
			usuario = ((SesionUsuario)sesion).getUsuario();
			if(usuario.getRol() != Roles.Médico) {
				throw new MedicoInexistenteException("Sólo los usuarios con rol de médico pueden consultar sus propias citas pendientes.");
			}
		} else {
			throw new MedicoInexistenteException("Sólo los usuarios con rol de médico pueden consultar sus propias citas pendientes.");
		}
		
		// Obtenemos las citas que tiene asignadas el médico
		citas = consultarCitasMedico(usuario.getNif());
		
		// Nos quedamos sólo con las citas posteriores a la fecha y hora actuales
		fechaActual = new Date();
		pendientes = new Vector<Cita>();
		for(Cita cita : citas) {
			if(cita.getFechaYHora().after(fechaActual)) {
				pendientes.add(cita);
			}
		}
		
		return pendientes;
	}
	
	// Método para obtener las citas de un médico en una fecha y rango de horas
	@SuppressWarnings("deprecation")
	public static Vector<Cita> consultarCitasFechaMedico(long idSesion, String nifMedico, Date dia, int horaDesde, int horaHasta) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException {
		Vector<Cita> citas, citasFecha;
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del médico para el que se quieren buscar las citas puede ser nulo.");
		}
		if(dia == null) {
			throw new NullPointerException("El día para el que se quieren buscar las fechas del médico no puede ser nulo.");			
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasMedico);
		
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != Roles.Médico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Recuperamos las citas del médico
		citas = FPCita.consultarPorMedico(nifMedico);
		
		// Nos quedamos con las citas del día indicado y cuya
		// hora está dentro del rango de horas especificado
		citasFecha = new Vector<Cita>();
		for(Cita cita : citas) {
			if(UtilidadesDominio.fechaIgual(dia, cita.getFechaYHora(), false)
			 && cita.citaEnHoras(horaDesde, horaHasta)) {
				citasFecha.add(cita);
			}
		}
		
		return citasFecha;
	}
	
	// Método para pedir una nueva cita para un cierto beneficiario y médico
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, MedicoInexistenteException, FechaNoValidaException, CitaNoValidaException, NullPointerException, DireccionInexistenteException {
		Vector<Cita> citas;
		Calendar hora;
		Usuario usuario;
		Medico medico;
		Cita cita;
		
		// Comprobamos los parámetros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que va a pedir cita no puede ser nulo.");
		}
		if(idMedico == null) {
			throw new NullPointerException("El médico para el que se va a pedir cita no puede ser nulo.");			
		}
		if(fechaYHora == null) {
			throw new NullPointerException("El día que se va a pedir cita no puede ser nulo.");			
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCita);
		
		// Comprobamos que exista el beneficiario
		beneficiario = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		if(beneficiario.getMedicoAsignado() == null) {
			throw new MedicoInexistenteException("El beneficiario que va a pedir cita no tiene asignado ningún médico.");
		}

		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(idMedico);
			if(usuario.getRol() != Roles.Médico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que el médico para el que se va a pedir cita
		// sea el médico que el beneficiario tiene asignado
		medico = (Medico)usuario;
		if(!medico.getNif().equals(beneficiario.getMedicoAsignado().getNif())) {
			throw new CitaNoValidaException("El médico con el que se desea pedir cita no se corresponde con el médico asignado al beneficiario con NIF " + beneficiario.getNif() + ".");
		}

		// Comprobamos que la fecha de la cita sea posterior a la actual
		if(UtilidadesDominio.fechaAnterior(fechaYHora, new Date(), true)) {
			throw new FechaNoValidaException("No se pueden solicitar citas para fechas anteriores a la actual.");
		}
		
		// Comprobamos que la hora de la cita sea múltiplo de
		// la duración, para que si las citas duran 15 minutos,
		// no se pueda pedir cita a las 19:38, por ejemplo
		hora = Calendar.getInstance();
		hora.setTime(fechaYHora);
		if((double)hora.get(Calendar.MINUTE) / IConstantes.DURACION_CITA != hora.get(Calendar.MINUTE) / IConstantes.DURACION_CITA) {
			throw new FechaNoValidaException("La hora de la cita no es válida porque sólo se pueden pedir citas en intervalos de " + IConstantes.DURACION_CITA + " minutos.");
		}

		// Comprobamos que, según el horario del médico, se pase
		// consulta a la fecha y hora introducidas
		if(!medico.fechaEnCalendario(fechaYHora, duracion)) {
			throw new FechaNoValidaException("La cita solicitada queda fuera del horario de trabajo del médico con NIF " + idMedico + ".");
		}
		
		// Recuperamos las citas del médico para comprobar que no
		// tiene ya una cita en esa misma fecha y hora
		citas = FPCita.consultarPorMedico(idMedico);	
		for(Cita c : citas) {
			if(UtilidadesDominio.fechaIgual(c.getFechaYHora(), fechaYHora, true)) {
				throw new CitaNoValidaException("El médico con NIF " + idMedico + " ya tiene una cita en la fecha y hora indicadas.");
			}
		}
		
		// Añadimos la cita con los datos pasados
		cita = new Cita();
		cita.setBeneficiario(beneficiario);
		cita.setMedico(medico);
		cita.setDuracion(duracion);
		cita.setFechaYHora(fechaYHora);
		FPCita.insertar(cita);
		
		return cita;
	}
	
	// Método para pedir una nueva cita para un cierto beneficiario a partir de un volante
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, VolanteNoValidoException, FechaNoValidaException, NullPointerException, CitaNoValidaException, DireccionInexistenteException, MedicoInexistenteException {
		Vector<Cita> citas;
		Calendar hora;
		Medico medico;
		Volante volante;
		Cita cita;
		
		// Comprobamos los parámetros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que va a pedir cita no puede ser nulo.");
		}
		if(fechaYHora == null) {
			throw new NullPointerException("El día que se va a pedir cita no puede ser nulo.");			
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCitaVolante);

		// Comprobamos que exista el beneficiario
		beneficiario = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		if(beneficiario.getMedicoAsignado() == null) {
			throw new MedicoInexistenteException("El beneficiario que va a pedir cita no tiene asignado ningún médico.");
		}

		// Obtenemos los datos del volante con el id indicado
		volante = FPVolante.consultar(idVolante);
		
		// Comprobamos que el volante no se haya utilizado ya
		if(volante.getCita() != null) {
			throw new VolanteNoValidoException("El volante con id " + String.valueOf(idVolante) + " ya se ha utilizado para tramitar una cita y no se puede usar de nuevo.");
		}
		
		// Comprobamos que el beneficiario que se pasa por parámetro y
		// el beneficiario que tiene asociado el volante sean los mismos
		if(!beneficiario.getNif().equals(volante.getBeneficiario().getNif())) {
			throw new VolanteNoValidoException("El beneficiario asociado al volante con id " + String.valueOf(idVolante) + " no coincide con el beneficiario que pide la cita.");
		}
		
		// Comprobamos que la fecha de la cita sea posterior a la actual
		if(UtilidadesDominio.fechaAnterior(fechaYHora, new Date(), true)) {
			throw new FechaNoValidaException("No se pueden solicitar citas para fechas anteriores a la actual.");
		}
		
		// Comprobamos que la hora de la cita sea múltiplo de
		// la duración, para que si las citas duran 15 minutos,
		// no se pueda pedir cita a las 19:38
		hora = Calendar.getInstance();
		hora.setTime(fechaYHora);
		if((double)hora.get(Calendar.MINUTE) / IConstantes.DURACION_CITA != hora.get(Calendar.MINUTE) / IConstantes.DURACION_CITA) {
			throw new FechaNoValidaException("La hora de la cita no es válida porque sólo se pueden pedir citas en intervalos de " + IConstantes.DURACION_CITA + " minutos.");
		}
		
		// Comprobamos que, según el horario del médico, se pase
		// consulta a la fecha y hora introducidas
		medico = volante.getReceptor();
		if(!medico.fechaEnCalendario(fechaYHora, duracion)) {
			throw new FechaNoValidaException("La cita solicitada queda fuera del horario de trabajo del médico con NIF " + medico.getNif() + ".");
		}
		
		// Recuperamos las citas del médico para comprobar que no
		// tiene ya una cita en esa misma fecha y hora
		citas = FPCita.consultarPorMedico(medico.getNif());	
		for(Cita c : citas) {
			if(UtilidadesDominio.fechaIgual(c.getFechaYHora(), fechaYHora, true)) {
				throw new CitaNoValidaException("El médico con NIF " + medico.getNif() + " ya tiene una cita en la fecha y hora indicadas.");
			}
		}
		
		// Añadimos la cita con los datos pasados
		cita = new Cita();
		cita.setBeneficiario(beneficiario);
		cita.setMedico(medico);
		cita.setDuracion(duracion);
		cita.setFechaYHora(fechaYHora);
		FPCita.insertar(cita);
		
		// Guardamos el id de la cita en el volante para que no
		// se pueda usar el mismo volante varias veces
		volante.setCita(cita);
		FPVolante.modificar(volante);
		
		return cita; 
	}
	
	// Método para eliminar una cita existente
	public static void anularCita(long idSesion, Cita cita) throws SQLException, CitaNoValidaException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludInexistenteException, NullPointerException, BeneficiarioInexistenteException, UsuarioIncorrectoException, DireccionInexistenteException {
		// Comprobamos los parámetros pasados
		if(cita == null) {
			throw new NullPointerException("La cita que se va a eliminar no puede ser nula.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.AnularCita);

		// Comprobamos si realmente existe la cita que se quiere
		// eliminar y a la vez obtenemos su identificador único
		try {
			cita = FPCita.consultar(cita.getFechaYHora(), cita.getDuracion(), cita.getMedico().getNif(), cita.getBeneficiario().getNif());
		} catch(CitaNoValidaException e) {
			throw new CitaNoValidaException("No existe ninguna cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + " para el médico con NIF " + cita.getMedico().getNif()+ " en el día " + cita.getFechaYHora().toString() + ".");
		}
		
		// Eliminamos la cita de la base de datos
		FPCita.eliminar(cita);
	}
	
	// Método para obtener las horas de las citas que tiene un médico en cada día
	public static Hashtable<Date, Vector<String>> consultarHorasCitasMedico(long idSesion, String nifMedico) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, MedicoInexistenteException, NullPointerException, DireccionInexistenteException {
		Hashtable<Date, Vector<String>> citasOcupadas;
		Vector<Cita> citas;
		Calendar cal;
		Date fecha, fechaDia;
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del médico para el que se quieren buscar las fechas de sus citas no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitasMedico);
		
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != Roles.Médico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Obtenemos las citas que ya tiene asignadas el médico
		citas = FPCita.consultarPorMedico(nifMedico);
		
		// Generamos una tabla que asigna a cada fecha las horas
		// de las citas que el médico ya tiene asignadas
		cal = Calendar.getInstance();
		citasOcupadas = new Hashtable<Date, Vector<String>>();
		for(Cita cita : citas) {
			fecha = cita.getFechaYHora();
			// Si la cita tiene una fecha pasada no se devuelve
			if(!UtilidadesDominio.fechaAnterior(fecha, new Date(), true)) {
				cal.setTime(fecha);
				fechaDia = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).getTime();
				// Inicializamos la lista de días si no se ha creado antes
				if(!citasOcupadas.containsKey(fechaDia)) {
					citasOcupadas.put(fechaDia, new Vector<String>());
				}
				// Añadimos la hora de la cita a la lista
				citasOcupadas.get(fechaDia).add(Cita.cadenaHoraCita(fecha));
			}
		}
		
		return citasOcupadas;
	}

	// Método para obtener los días en los que un médico podría pasar
	// consulta pero ya tiene citas en todas las horas posibles
	public static Vector<Date> consultarDiasCompletos(long idSesion, String nifMedico) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, CentroSaludInexistenteException, MedicoInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException, NullPointerException, DireccionInexistenteException {
		Hashtable<Date, Integer> citasPorFecha;
		Hashtable<DiaSemana, Integer> citasPorDiaSemana;
		Vector<Date> dias;
		Vector<String> horasCitas;
		Vector<Cita> citas;
		Calendar cal;
		Medico medico;
		Usuario usuario;

		// Comprobamos los parámetros pasados
		if(nifMedico == null) {
			throw new NullPointerException("El NIF del médico para el que se quieren calcular los días completos no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarMedico);
		
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(nifMedico);
			if(usuario.getRol() != Roles.Médico) {
				throw new MedicoInexistenteException("El NIF introducido no pertenece a un médico.");
			}
			medico = (Medico)usuario; 
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}

		// Recuperamos todas las citas del médico
		citas = FPCita.consultarPorMedico(nifMedico);

		// Calculamos cuántas citas tiene tramitadas el médico en cada fecha
		// (los fechas anteriores a la fecha actual se ignoran)
		cal = Calendar.getInstance();
		citasPorFecha = new Hashtable<Date, Integer>();
		for(Cita cita : citas) {
			cal.setTime(cita.getFechaYHora());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if(!UtilidadesDominio.fechaAnterior(cita.getFechaYHora(), new Date(), false)) {
				if(citasPorFecha.containsKey(cal.getTime())) {
					citasPorFecha.put(cal.getTime(), citasPorFecha.get(cal.getTime()) + 1);
				} else {
					citasPorFecha.put(cal.getTime(), 1);
				}
			}
		}
		
		// Calculamos cuál es el número máximo de citas que puede
		// tener el médico en cada día de la semana
		citasPorDiaSemana = new Hashtable<DiaSemana, Integer>();
		for(DiaSemana dia : DiaSemana.values()) {
			horasCitas = medico.horasCitas(dia, IConstantes.DURACION_CITA);
			citasPorDiaSemana.put(dia, horasCitas.size());
		}
		
		// A partir de las tablas anteriores, obtenemos las fechas en las
		// que el médico no puede pasar más citas de las que ya tiene asignadas
		dias = new Vector<Date>();
		for(Date dia : citasPorFecha.keySet()) {
			if(citasPorFecha.get(dia) == citasPorDiaSemana.get(UtilidadesDominio.diaFecha(dia))) {
				dias.add(dia);
			}
		}
		
		return dias;
	}

	private static Vector<Cita> consultarCitasMedico(String nifMedico) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		Vector<Sustitucion> sustituciones;
		Vector<Cita> citas, citas2, citasSust;
		
		// Obtenemos las citas que ya tiene asignadas el médico
		citas = FPCita.consultarPorMedico(nifMedico);
		
		// Quitamos las citas que no va a dar el médico por estar sustituido
		citasSust = new Vector<Cita>();
		sustituciones = FPSustitucion.consultarPorSustituido(nifMedico);
		for(Sustitucion sustitucion : sustituciones) {
			for(Cita cita : citas) {
				if(UtilidadesDominio.fechaIgual(cita.getFechaYHora(), sustitucion.getDia(), false)
				 && sustitucion.horaEnSustitucion(cita.getFechaYHora())) {
					citasSust.add(cita);
				}
			}
		}
		for(Cita cita : citasSust) {
			citas.remove(cita);
		}
		
		// Añadimos las citas que va a dar el médico por estar sustituyendo a otro
		citasSust = new Vector<Cita>();
		sustituciones = FPSustitucion.consultarPorSustituto(nifMedico);
		for(Sustitucion sustitucion : sustituciones) {
			citas2 = FPCita.consultarPorMedico(sustitucion.getMedico().getNif());
			for(Cita cita : citas2) {
				if(UtilidadesDominio.fechaIgual(cita.getFechaYHora(), sustitucion.getDia(), false)
				 && sustitucion.horaEnSustitucion(cita.getFechaYHora())) {
					citasSust.add(cita);
				}
			}
		}
		for(Cita cita : citasSust) {
			citas.add(cita);
		}
		
		return citas;
	}
	
}
