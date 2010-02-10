package dominio.control;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuarios;
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
import persistencia.FPUsuario;
import persistencia.FPVolante;

/**
 * Clase encargada de solicitar, recuperar y eliminar citas que tienen los
 * beneficiarios para acudir a la consulta de los médicos.
 */
public class GestorCitas {

	// Método para obtener todas las citas de un beneficiario
	public static Vector<Cita> consultarCitas(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Vector<Cita> citas;
		
		// Comprobamos los parámetros pasados
		if(dni == null) {
			throw new NullPointerException("El NIF del beneficiario para el que se quieren buscar las citas no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitas);

		// Comprobamos que exista el beneficiario
		FPBeneficiario.consultarPorNIF(dni);
		
		// Recuperamos las citas del beneficiario
		citas = FPCita.consultarPorBeneficiario(dni);
		
		return citas;
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

		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(idMedico);
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico.");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que el médico para el que se va a pedir cita
		// sea el médico que el beneficiario tiene asignado
		medico = (Medico)usuario;
		if(!medico.getDni().equals(beneficiario.getMedicoAsignado().getDni())) {
			throw new CitaNoValidaException("El médico con el que se desea pedir cita no se corresponde con el médico asignado al beneficiario con NIF " + beneficiario.getNif() + ".");
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
		if(!medico.fechaEnCalendario(fechaYHora, duracion)) {
			throw new FechaNoValidaException("La cita solicitada queda fuera del horario de trabajo del médico con DNI " + idMedico + ".");
		}
		
		// Recuperamos las citas del médico para comprobar que no
		// tiene ya una cita en esa misma fecha y hora
		citas = FPCita.consultarPorMedico(idMedico);	
		for(Cita c : citas) {
			if(c.getFechaYHora().equals(fechaYHora)) {
				throw new FechaNoValidaException("El médico con DNI " + idMedico + " ya tiene una cita en la fecha y hora indicadas.");
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
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, VolanteNoValidoException, FechaNoValidaException, NullPointerException, CitaNoValidaException, DireccionInexistenteException {
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
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCita);

		// Comprobamos que exista el beneficiario
		beneficiario = FPBeneficiario.consultarPorNIF(beneficiario.getNif());

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
			throw new FechaNoValidaException("La cita solicitada queda fuera del horario de trabajo del médico con DNI " + medico.getDni() + ".");
		}
		
		// Recuperamos las citas del médico para comprobar que no
		// tiene ya una cita en esa misma fecha y hora
		citas = FPCita.consultarPorMedico(medico.getDni());	
		for(Cita c : citas) {
			if(c.getFechaYHora().equals(fechaYHora)) {
				throw new FechaNoValidaException("El médico con DNI " + medico.getDni() + " ya tiene una cita en la fecha y hora indicadas.");
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
			cita = FPCita.consultar(cita.getFechaYHora(), cita.getDuracion(), cita.getBeneficiario().getNif(), cita.getMedico().getDni());
		} catch(CitaNoValidaException e) {
			throw new CitaNoValidaException("No existe ninguna cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + " para el médico con DNI " + cita.getMedico().getDni()+ " en el día " + cita.getFechaYHora().toString() + ".");
		}
		
		// Eliminamos la cita de la base de datos
		FPCita.eliminar(cita);
	}

	// Método para emitir un volante para un beneficiario para un especialista
	public static long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, SesionInvalidaException, OperacionIncorrectaException, VolanteNoValidoException, UsuarioIncorrectoException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Usuario usuario;
		Volante volante;
		
		// Comprobamos los parámetros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario de un volante no puede ser nulo.");
		}
		if(emisor == null) {
			throw new NullPointerException("El médico emisor de un volante no puede ser nulo.");
		}
		if(destino == null) {
			throw new NullPointerException("El médico receptor de un volante no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EmitirVolante);
		
		// Comprobamos que exista el beneficiario
		beneficiario = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		
		// Comprobamos que exista el médico emisor
		try {
			usuario = FPUsuario.consultar(emisor.getDni());
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El médico emisor no es un usuario del sistema con rol de médico.");
			}
			emisor = (Medico)usuario; 
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que exista el médico receptor
		try {
			usuario = FPUsuario.consultar(destino.getDni());
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El médico receptor no es un usuario del sistema con rol de médico.");
			}
			destino = (Medico)usuario; 
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
				
		// Comprobamos que el médico receptor sea un especialista
		if(destino.getTipoMedico().getCategoria() != CategoriasMedico.Especialista) {
			throw new VolanteNoValidoException("El médico receptor del volante no es un especialista.");
		}
		
		// Añadimos el volante y devolvemos su identificador
		volante = new Volante();
		volante.setBeneficiario(beneficiario);
		volante.setEmisor(emisor);
		volante.setReceptor(destino);
		volante.setCita(null);
		FPVolante.insertar(volante);
		
		return volante.getId();
	}

	// Método para obtener los datos de un volante
	public static Volante consultarVolante(long idSesion, long idVolante) throws SQLException, SesionInvalidaException, OperacionIncorrectaException, VolanteNoValidoException, CitaNoValidaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		Volante volante;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarVolante);

		// Obtenemos el volante con el id indicado
		volante = FPVolante.consultar(idVolante);
		
		return volante;
	}
	
	// Método que devuelve las horas de cada día de la semana
	// en las que un médico puede pasar una cita
	public static Hashtable<DiaSemana, Vector<String>> consultarHorasCitas(long idSesion, String dniMedico) throws SQLException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, MedicoInexistenteException, NullPointerException, DireccionInexistenteException {
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
	
	// Método para obtener las citas que tiene un médico en cada día
	@SuppressWarnings("deprecation")
	public static Hashtable<Date, Vector<String>> consultarCitasMedico(long idSesion, String dniMedico) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, MedicoInexistenteException, NullPointerException, DireccionInexistenteException {
		Hashtable<Date, Vector<String>> citasOcupadas;
		Vector<Cita> citas;
		Calendar cal;
		Date fecha, fechaDia;
		Usuario usuario;
		
		// Comprobamos los parámetros pasados
		if(dniMedico == null) {
			throw new NullPointerException("El DNI del médico para el que se quieren buscar las citas puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitas);
		
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(dniMedico);
			if(usuario.getRol() != RolesUsuarios.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico.");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Obtenemos las citas que ya tiene asignadas el médico
		citas = FPCita.consultarPorMedico(dniMedico);
		
		// Generamos una tabla que asigna a cada fecha las horas
		// de las citas que el médico ya tiene asignadas
		cal = Calendar.getInstance();
		citasOcupadas = new Hashtable<Date, Vector<String>>();
		for(Cita cita : citas) {
			fecha = cita.getFechaYHora();
			cal.setTime(fecha);
			fechaDia = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			// Inicializamos la lista de días si no se ha creado antes
			if(!citasOcupadas.containsKey(fechaDia)) {
				citasOcupadas.put(fechaDia, new Vector<String>());
			}
			// Añadimos la hora de la cita a la lista
			citasOcupadas.get(fechaDia).add(Cita.cadenaHoraCita(fecha));
		}
		
		return citasOcupadas;
	}

	// Método para obtener los días en los que un médico podría pasar
	// consulta pero ya tiene citas en todas las horas posibles
	public static Vector<Date> consultarDiasCompletos(long idSesion, String dniMedico) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, CentroSaludInexistenteException, MedicoInexistenteException, BeneficiarioInexistenteException, UsuarioIncorrectoException, NullPointerException, DireccionInexistenteException {
		Hashtable<Date, Integer> citasPorFecha;
		Hashtable<DiaSemana, Integer> citasPorDiaSemana;
		Vector<Date> dias;
		Vector<String> horasCitas;
		Vector<Cita> citas;
		Calendar cal;
		Medico medico;
		Usuario usuario;
		int añoAct, mesAct, diaAct;

		// Comprobamos los parámetros pasados
		if(dniMedico == null) {
			throw new NullPointerException("El DNI del médico para el que se quieren calcular los días completos no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.CalcularDiasCompletosMedico);
		
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

		// Recuperamos todas las citas del médico
		citas = FPCita.consultarPorMedico(dniMedico);

		// Obtenemos la fecha actual
		cal = Calendar.getInstance();
		cal.setTime(new Date());
		añoAct = cal.get(Calendar.YEAR);
		mesAct = cal.get(Calendar.MONTH);
		diaAct = cal.get(Calendar.DAY_OF_MONTH);
		
		// Calculamos cuántas citas tiene tramitadas el médico en cada fecha
		// (los fechas anteriores a la fecha actual se ignoran)
		cal = Calendar.getInstance();
		citasPorFecha = new Hashtable<Date, Integer>();
		for(Cita cita : citas) {
			cal.setTime(cita.getFechaYHora());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			if(cal.get(Calendar.YEAR) < añoAct
			 || (cal.get(Calendar.YEAR) == añoAct && cal.get(Calendar.MONTH) < mesAct)
			 || (cal.get(Calendar.YEAR) == añoAct && cal.get(Calendar.MONTH) == mesAct && cal.get(Calendar.DAY_OF_MONTH) < diaAct)) {
				// La cita es de una fecha anterior a la actual
			} else {
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
			cal.setTime(dia);
			switch(cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				if(citasPorFecha.get(dia) == citasPorDiaSemana.get(DiaSemana.Lunes)) {
					dias.add(dia);
				}
				break;
			case Calendar.TUESDAY:
				if(citasPorFecha.get(dia) == citasPorDiaSemana.get(DiaSemana.Martes)) {
					dias.add(dia);
				}
				break;
			case Calendar.WEDNESDAY:
				if(citasPorFecha.get(dia) == citasPorDiaSemana.get(DiaSemana.Miercoles)) {
					dias.add(dia);
				}
				break;
			case Calendar.THURSDAY:
				if(citasPorFecha.get(dia) == citasPorDiaSemana.get(DiaSemana.Jueves)) {
					dias.add(dia);
				}
				break;
			case Calendar.FRIDAY:
				if(citasPorFecha.get(dia) == citasPorDiaSemana.get(DiaSemana.Viernes)) {
					dias.add(dia);
				}
				break;
			}
		}
		
		return dias;
	}

}
