package dominio.control;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import comunicaciones.IConstantes;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;
import persistencia.FPBeneficiario;
import persistencia.FPCita;
import persistencia.FPEntradaLog;
import persistencia.FPPeriodoTrabajo;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import persistencia.FPVolante;

/**
 * Clase encargada de solicitar, recuperar y eliminar citas que tienen los
 * beneficiarios para acudir a la consulta de los m�dicos.
 */
public class GestorCitas implements IConstantes {

	// M�todo para pedir una nueva cita para un cierto beneficiario y m�dico
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, MedicoInexistenteException, FechaNoValidaException, CitaNoValidaException, Exception {
		Vector<Cita> citas;
		Calendar hora;
		Usuario usuario;
		Medico medico;
		Cita cita;
		
		// Comprobamos los par�metros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que va a pedir cita no puede ser nulo");
		}
		if(idMedico == null) {
			throw new NullPointerException("El m�dico para el que se va a pedir cita no puede ser nulo");			
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCita);
		
		// Comprobamos que exista el beneficiario
		FPBeneficiario.consultarPorNIF(beneficiario.getNif());

		// Comprobamos que exista el m�dico
		try {
			usuario = FPUsuario.consultar(idMedico);
			if(usuario.getRol() != Roles.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un m�dico");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que el m�dico para el que se va a pedir cita
		// sea el m�dico que el beneficiario tiene asignado
		medico = (Medico)usuario;
		if(!medico.getDni().equals(beneficiario.getMedicoAsignado().getDni())) {
			throw new CitaNoValidaException("El m�dico con el que se desea pedir cita no se corresponde con el m�dico asignado al beneficiario con NIF " + beneficiario.getNif());
		}

		// Comprobamos que la hora de la cita sea m�ltiplo de
		// la duraci�n, para que si las citas duran 15 minutos,
		// no se pueda pedir cita a las 19:38
		hora = Calendar.getInstance();
		hora.setTime(fechaYHora);
		if((int)hora.get(Calendar.MINUTE) / DURACION_CITA != hora.get(Calendar.MINUTE) / DURACION_CITA) {
			throw new FechaNoValidaException("La hora de la cita no es v�lida porque s�lo se pueden pedir citas en intervalos de " + DURACION_CITA + "minutos");
		}

		// Comprobamos que, seg�n el horario del m�dico, se pase
		// consulta a la fecha y hora introducidas
		if(!medico.fechaEnCalendario(fechaYHora, duracion)) {
			throw new FechaNoValidaException("La cita solicitada queda fuera del horario de trabajo del m�dico con DNI " + idMedico);
		}
		
		// Recuperamos las citas del m�dico para comprobar que no
		// tiene ya una cita en esa misma fecha y hora
		citas = FPCita.consultarPorMedico(idMedico);	
		for(Cita c : citas) {
			if(c.getFechaYhora().equals(fechaYHora)) {
				throw new FechaNoValidaException("El m�dico con DNI " + idMedico + " ya tiene una cita en la fecha y hora indicadas");
			}
		}
		
		// A�adimos la cita con los datos pasados
		cita = new Cita();
		cita.setBeneficiario(beneficiario);
		cita.setMedico(medico);
		cita.setDuracion(duracion);
		cita.setFechaYHora(fechaYHora);
		FPCita.insertar(cita);
		
		return cita;
	}
	
	// TODO: Los siguientes m�todos no est�n revisados
	
	// M�todo para pedir una nueva cita para un cierto beneficiario a partir de un volante
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYhora, long duracion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, VolanteNoValidoException, FechaNoValidaException {
		Cita cita;
		EntradaLog entrada;
		Beneficiario bene;
		Medico medico;
		Volante volante;
		Vector<Cita> citas;
		
		// Comprobamos los par�metros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que va a pedir cita no puede ser nulo");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCita);
		
		// Obtenemos los datos del volante con el id indicado
		volante = FPVolante.consultar(idVolante);
		
		// Se comprueba que exista el beneficiario que se pasa por parametro
		bene = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
				
		// Se comprueba que el beneficiario que se pasa por par�metro y
		// el que tiene asociado el volante sean los mismos
		if(!bene.equals(volante.getBeneficiario())) {
			throw new VolanteNoValidoException("El beneficiario asociado al volante con id " + String.valueOf(idVolante) + " no coincide con el beneficiario que pide la cita");
		}
		
		// Comprobamos que la fecha introducida sea v�lida para el medico receptor
		medico = volante.getReceptor();
		if(!medico.fechaEnCalendario(fechaYhora, duracion)) {
			throw new FechaNoValidaException("La fecha, hora y duraci�n dadas no son v�lidas para concertar una cita con el m�dico con DNI " + medico.getDni());
		}
		
		// Recuperamos las citas del m�dico para comprobar si no tiene ya una cita en esa fecha y hora
		citas = FPCita.consultarPorMedico(medico.getDni());	
		for (Cita c : citas) {
			if (c.getFechaYhora().equals(fechaYhora))
				throw new FechaNoValidaException("La fecha, hora y duraci�n dadas no son v�lidas para concertar una cita con el m�dico con DNI " + medico.getDni());
		}
		
		// Si todo es v�lido, se crea la cita, se inserta y se devuelve
		cita = new Cita();
		cita.setBeneficiario(beneficiario);
		cita.setMedico(medico);
		cita.setDuracion(duracion);
		cita.setFechaYHora(fechaYhora);
		FPCita.insertar(cita);	
		
		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "La cita del beneficiario con NIF " + beneficiario.getNif() + " con el medico con DNI " + medico.getDni() + ", asociada al volante " + String.valueOf(idVolante) + ", se ha creado correctamente.");
		FPEntradaLog.insertar(entrada);
		
		return cita; 
	}
	
	public static long emitirVolante(long idSesion, Beneficiario b, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, SesionInvalidaException, OperacionIncorrectaException, VolanteNoValidoException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		Usuario usuario;
		Volante volante;
		Medico medicoAsignado = null;
		boolean cambioMedico = false;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EmitirVolante);
		
		// Se comprueba que existe el beneficiario dado por par�metro
		FPBeneficiario.consultarPorNIF(b.getNif());
		
		// Se comprueba si el beneficiario tiene m�s de 14 a�os. En ese caso, si ten�a asignado un pediatra, se le asigna un m�dico de cabecera
		if (b.getEdad() >= 14) {
			if (b.getMedicoAsignado().getTipoMedico() instanceof Pediatra) {
				medicoAsignado = FPTipoMedico.consultarTipoMedicoAleatorio(new Cabecera());
				b.setMedicoAsignado(medicoAsignado);
				cambioMedico = true;
			}
		}
		
		// Se comprueba que existen los m�dicos, tanto emisor como receptor
		// No haria falta comprobar que los parametros introducidos sean medicos,
		// pues se obliga poniendo que su tipo sea de la clase "Medico"
		try {
			if (!cambioMedico)
				usuario = FPUsuario.consultar(emisor.getDni());
			else
				usuario = medicoAsignado;
			if(usuario.getRol() != Roles.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un m�dico");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		try {
			usuario = FPUsuario.consultar(destino.getDni());
			if(usuario.getRol() != Roles.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un m�dico");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que el medico receptor sea un especialista
		if (!(destino.getTipoMedico() instanceof Especialista))
			throw new VolanteNoValidoException("Solo se puede emitir un volante para acudir a un especialista");
		
		// Si todo es correcto, se crea el volante, se escribe en el log y se devuelve el identificador del volante
		volante = new Volante();
		volante.setBeneficiario(b);
		if (!cambioMedico)
			volante.setEmisor(emisor);
		else
			volante.setEmisor(medicoAsignado);
		volante.setReceptor(destino);
		FPVolante.insertar(volante);
		
		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se ha emitido un volante para el beneficiario "+volante.getBeneficiario().getNif()+", emitido por el medico "+volante.getEmisor().getNombre() +" para el especialista "+volante.getReceptor().getNombre());
		FPEntradaLog.insertar(entrada);
		
		// Si se ha cambiado el m�dico, actualizamos el beneficiario en la base de datos
		if (cambioMedico) {
			FPBeneficiario.modificar(b);
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "Se ha modificado el m�dico asignado al beneficiario con NIF " + b.getNif() + ". Pasa a tener un m�dico de cabecera, cuyo NIF es " + medicoAsignado.getDni() +".");
			FPEntradaLog.insertar(entrada);
		}
		
		return volante.getId();
	}

	// M�todo para obtener todas las citas de un beneficiario
	public static Vector<Cita> getCitas(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {
		Vector<Cita> citas;
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitas);
		
		// Obtenemos las citas del beneficiario
		citas = FPCita.consultarPorBeneficiario(dni);
		
		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se han consultado las citas del beneficiario con DNI "+ dni.toString() + ".");
		FPEntradaLog.insertar(entrada);
		
		return citas;
	}

	// M�todo para eliminar una cita existente
	public static void anularCita(long idSesion, Cita cita) throws SQLException, CitaNoValidaException, SesionInvalidaException, OperacionIncorrectaException {
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.AnularCita);

		// Se comprueba si la cita existe
		if (!FPCita.existe(cita))
			throw new CitaNoValidaException("No existe la cita del beneficiario " + cita.getBeneficiario().getNif()+ " con el medico " + cita.getMedico().getDni()+ " para poder anularla");
		
		// Eliminamos la cita de la base de datos
		FPCita.eliminar(cita);

		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "Se ha eliminado la cita del beneficiario con DNI "+cita.getBeneficiario().getNif()+" con el medico "+cita.getMedico().getNombre() +" del dia "+cita.getFechaYhora());
		FPEntradaLog.insertar(entrada);
	}
	
	public static Object[] calcularCitasMedico (long idSesion, String dniMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, BeneficiarioInexistenteException {
		/* 
		 * Se devuelve una matriz con dos elementos, de diferente naturaleza.
		 * El primer elemento es una tabla donde se indica los d�as en los que un m�dico trabaja, 
		 * junto con todas las horas posibles para dar cita esos d�as, seg�n la jornada de dicho m�dico.
		 * El segundo elemento es una tabla donde se indica la fecha donde ya hay asignadas citas, 
		 * junto con todas las horas ya ocupadas de esa fecha.
		 */
		Object [] informacion = new Object [2];
		
		Hashtable<DiaSemana, ArrayList<String>> horasPosiblesMedico = new Hashtable<DiaSemana,ArrayList<String>>();
		Hashtable<String, ArrayList<String>> citasOcupadasMedico = new Hashtable<String, ArrayList<String>>();
		
		ArrayList<String> horasTrabajo;
		ArrayList<String> horasOcupadas;
		ArrayList<PeriodoTrabajo> calendario;
		EntradaLog entrada;
		Vector <Cita> citas;
		int intervalos, hora;
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		Date fecha;
		String dia, minutos;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitas);
		
		// Obtenemos las citas que ya tiene asignadas el m�dico
		citas = FPCita.consultarPorMedico(dniMedico);
		
		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se han consultado las citas del m�dico con DNI "+ dniMedico + ".");
		FPEntradaLog.insertar(entrada);
		
		// Obtenemos el calendario del medico
		calendario = FPPeriodoTrabajo.consultarCalendario(dniMedico);
		
		/* 
		 * Con este bucle obtenemos una tabla donde se indican las horas de trabajo disponibles
		 * para cada uno de los dias de trabajo del m�dico.
		 * Tambi�n obtenemos las horas que ya est�n ocupadas esos d�as de trabajo
		 */
		for(PeriodoTrabajo p: calendario) {
			horasTrabajo = new ArrayList<String> ();
			hora = 0;
			intervalos = (int) ((((p.getHoraFinal() - p.getHoraInicio()) * 60) / DURACION_CITA) - 1);
			
			// Primera hora en la que se podr�a pedir cita
			horasTrabajo.add(p.getHoraInicio()+":00");
			
			for (int i=(int) DURACION_CITA; i <= (intervalos*DURACION_CITA); i += DURACION_CITA) {
				if ((i % 60) == 0) {
					hora += 1;
					horasTrabajo.add((p.getHoraInicio()+hora)+":00");
				}
				else
					horasTrabajo.add((p.getHoraInicio()+hora)+":"+(i%60));
			}
			horasPosiblesMedico.put(p.getDia(), horasTrabajo);
		}
		
		// Con este bucle obtenemos los dias y horas en los que el m�dico ya tiene citas asignadas		
		for (Cita c: citas) {
			horasOcupadas = new ArrayList<String> ();
			fecha = c.getFechaYhora();
			dia = formatoDeFecha.format(fecha);
			
			// Si para ese d�a aun no se han incluido las horas ocupadas, se a�aden
			if (citasOcupadasMedico.get(dia) == null) {
				if (fecha.getMinutes() == 0)
					minutos = "00";
				else
					minutos = Integer.toString(fecha.getMinutes());
				horasOcupadas.add(fecha.getHours()+":"+minutos);
				citasOcupadasMedico.put(dia, horasOcupadas);
				
			}
			// Si ya existian horas ocupadas para ese d�a, se a�ade la nueva hora
			else {
				if (fecha.getMinutes() == 0)
					minutos = "00";
				else
					minutos = Integer.toString(fecha.getMinutes());
				citasOcupadasMedico.get(dia).add(fecha.getHours()+":"+minutos);
			}
		}		
		
		informacion[0] = horasPosiblesMedico;
		informacion[1] = citasOcupadasMedico;
		return informacion;
	}	
}
