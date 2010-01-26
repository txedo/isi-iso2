package dominio.control;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * beneficiarios para acudir a la consulta de los médicos.
 */
public class GestorCitas implements IConstantes {

	// Método para pedir una nueva cita para un cierto beneficiario y médico
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYhora, long duracion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, MedicoInexistenteException, FechaNoValidaException, Exception {
		Cita cita;
		EntradaLog entrada;
		Usuario usuario;
		Medico medico = null;
		Medico medicoAsignado = null;
		Vector<Cita> citas;
		boolean cambioMedico = false;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCita);
		
		// Comprobamos que exista el beneficiario
		FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		
		// Se comprueba si el beneficiario tiene más de 14 años. En ese caso, si tenía asignado un pediatra, se le asigna un médico de cabecera
		if (beneficiario.getEdad() >= 14) {
			if (beneficiario.getMedicoAsignado().getTipoMedico() instanceof Pediatra) {
				medicoAsignado = FPTipoMedico.consultarTipoMedicoAleatorio(new Cabecera());
				beneficiario.setMedicoAsignado(medicoAsignado);
				cambioMedico = true;
			}
		}
			
		// Comprobamos que exista el médico (el que se pasa como argumento si no se cambia de médico, o el recién asignado)
		try {
			if (!cambioMedico)
				usuario = FPUsuario.consultar(idMedico);
			else
				usuario = medicoAsignado;
			
			if(usuario.getRol() != Roles.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que el DNI introducido del medico corresponda al medico que el beneficiario tiene asignado
		if (!((Medico)usuario).equals(beneficiario.getMedicoAsignado()))
			throw new Exception("El médico con el que se desea pedir cita no corresponde con el médico asignado al beneficiario con DNI " + beneficiario.getNif());
		
		// Comprobamos que la fecha introducida sea válida para el medico dado
		medico = (Medico)usuario;
		if(!medico.fechaEnCalendario(fechaYhora, duracion)) {
			throw new FechaNoValidaException("La fecha, hora y duración dadas no son válidas para concertar una cita con el médico con DNI " + idMedico);
		}
		
		// Recuperamos las citas del médico para comprobar si no tiene ya una cita en esa fecha y hora
		citas = FPCita.consultarPorMedico(idMedico);	
		for (Cita c : citas) {
			if (c.getFechaYhora().equals(fechaYhora))
				throw new FechaNoValidaException("La fecha, hora y duración dadas no son válidas para concertar una cita con el médico con DNI " + idMedico);
		}
		
		// Si todo es válido, se crea la cita, se inserta y se devuelve
		cita = new Cita();
		cita.setBeneficiario(beneficiario);
		cita.setMedico(medico);
		cita.setDuracion(duracion);
		cita.setFechaYhora(fechaYhora);
		FPCita.insertar(cita);
		
		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "La cita del beneficiario con NIF " + beneficiario.getNif() + " con el medico con DNI " + idMedico + " se ha creado correctamente.");
		FPEntradaLog.insertar(entrada);
		
		// Si se ha cambiado el médico, actualizamos el beneficiario en la base de datos
		if (cambioMedico) {
			FPBeneficiario.modificar(beneficiario);
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "Se ha modificado el médico asignado al beneficiario con NIF " + beneficiario.getNif() + ". Pasa a tener un médico de cabecera, cuyo NIF es " + medicoAsignado.getDni() +".");
			FPEntradaLog.insertar(entrada);
		}
		
		return cita;
	}

	// Método para pedir una nueva cita para un cierto beneficiario a partir de un volante
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYhora, long duracion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, VolanteNoValidoException, FechaNoValidaException {
		Cita cita;
		EntradaLog entrada;
		Beneficiario bene;
		Medico medico;
		Volante volante;
		Vector<Cita> citas;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCita);
		
		// Obtenemos los datos del volante con el id indicado
		volante = FPVolante.consultar(idVolante);
		
		// Se comprueba que exista el beneficiario que se pasa por parametro
		bene = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
				
		// Se comprueba que el beneficiario que se pasa por parámetro y
		// el que tiene asociado el volante sean los mismos
		if(!bene.equals(volante.getBeneficiario())) {
			throw new VolanteNoValidoException("El beneficiario asociado al volante con id " + String.valueOf(idVolante) + " no coincide con el beneficiario que pide la cita");
		}
		
		// Comprobamos que la fecha introducida sea válida para el medico receptor
		medico = volante.getReceptor();
		if(!medico.fechaEnCalendario(fechaYhora, duracion)) {
			throw new FechaNoValidaException("La fecha, hora y duración dadas no son válidas para concertar una cita con el médico con DNI " + medico.getDni());
		}
		
		// Recuperamos las citas del médico para comprobar si no tiene ya una cita en esa fecha y hora
		citas = FPCita.consultarPorMedico(medico.getDni());	
		for (Cita c : citas) {
			if (c.getFechaYhora().equals(fechaYhora))
				throw new FechaNoValidaException("La fecha, hora y duración dadas no son válidas para concertar una cita con el médico con DNI " + medico.getDni());
		}
		
		// Si todo es válido, se crea la cita, se inserta y se devuelve
		cita = new Cita();
		cita.setBeneficiario(beneficiario);
		cita.setMedico(medico);
		cita.setDuracion(duracion);
		cita.setFechaYhora(fechaYhora);
		FPCita.insertar(cita);	
		
		// Añadimos una entrada al log
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
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EmitirVolante);
		
		// Se comprueba que existe el beneficiario dado por parámetro
		FPBeneficiario.consultarPorNIF(b.getNif());
		
		// Se comprueba si el beneficiario tiene más de 14 años. En ese caso, si tenía asignado un pediatra, se le asigna un médico de cabecera
		if (b.getEdad() >= 14) {
			if (b.getMedicoAsignado().getTipoMedico() instanceof Pediatra) {
				medicoAsignado = FPTipoMedico.consultarTipoMedicoAleatorio(new Cabecera());
				b.setMedicoAsignado(medicoAsignado);
				cambioMedico = true;
			}
		}
		
		// Se comprueba que existen los médicos, tanto emisor como receptor
		// No haria falta comprobar que los parametros introducidos sean medicos,
		// pues se obliga poniendo que su tipo sea de la clase "Medico"
		try {
			if (!cambioMedico)
				usuario = FPUsuario.consultar(emisor.getDni());
			else
				usuario = medicoAsignado;
			if(usuario.getRol() != Roles.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico");
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		try {
			usuario = FPUsuario.consultar(destino.getDni());
			if(usuario.getRol() != Roles.Medico) {
				throw new MedicoInexistenteException("El DNI introducido no pertenece a un médico");
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
		
		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se ha emitido un volante para el beneficiario "+volante.getBeneficiario().getNif()+", emitido por el medico "+volante.getEmisor().getNombre() +" para el especialista "+volante.getReceptor().getNombre());
		FPEntradaLog.insertar(entrada);
		
		// Si se ha cambiado el médico, actualizamos el beneficiario en la base de datos
		if (cambioMedico) {
			FPBeneficiario.modificar(b);
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "Se ha modificado el médico asignado al beneficiario con NIF " + b.getNif() + ". Pasa a tener un médico de cabecera, cuyo NIF es " + medicoAsignado.getDni() +".");
			FPEntradaLog.insertar(entrada);
		}
		
		return volante.getId();
	}

	// Método para obtener todas las citas de un beneficiario
	public static Vector<Cita> getCitas(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {
		Vector<Cita> citas;
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitas);
		
		// Obtenemos las citas del beneficiario
		citas = FPCita.consultarPorBeneficiario(dni);
		
		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se han consultado las citas del beneficiario con DNI "+ dni.toString() + ".");
		FPEntradaLog.insertar(entrada);
		
		return citas;
	}

	// Método para eliminar una cita existente
	public static void anularCita(long idSesion, Cita cita) throws SQLException, CitaNoValidaException, SesionInvalidaException, OperacionIncorrectaException {
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.AnularCita);

		// Se comprueba si la cita existe
		if (!FPCita.existe(cita))
			throw new CitaNoValidaException("No existe la cita del beneficiario " + cita.getBeneficiario().getNif()+ " con el medico " + cita.getMedico().getDni()+ " para poder anularla");
		
		// Eliminamos la cita de la base de datos
		FPCita.eliminar(cita);

		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "Se ha eliminado la cita del beneficiario con DNI "+cita.getBeneficiario().getNif()+" con el medico "+cita.getMedico().getNombre() +" del dia "+cita.getFechaYhora());
		FPEntradaLog.insertar(entrada);
	}
	
	public static Object[] calcularCitasMedico (long idSesion, String dniMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, BeneficiarioInexistenteException {
		/* 
		 * Se devuelve una matriz con dos elementos, de diferente naturaleza.
		 * El primer elemento es una tabla donde se indica los días en los que un médico trabaja, 
		 * junto con todas las horas posibles para dar cita esos días, según la jornada de dicho médico.
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
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarCitas);
		
		// Obtenemos las citas que ya tiene asignadas el médico
		citas = FPCita.consultarPorMedico(dniMedico);
		
		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se han consultado las citas del médico con DNI "+ dniMedico + ".");
		FPEntradaLog.insertar(entrada);
		
		// Obtenemos el calendario del medico
		calendario = FPPeriodoTrabajo.consultarCalendario(dniMedico);
		
		/* 
		 * Con este bucle obtenemos una tabla donde se indican las horas de trabajo disponibles
		 * para cada uno de los dias de trabajo del médico.
		 * También obtenemos las horas que ya están ocupadas esos días de trabajo
		 */
		for(PeriodoTrabajo p: calendario) {
			horasTrabajo = new ArrayList<String> ();
			hora = 0;
			intervalos = (int) ((((p.getHoraFinal() - p.getHoraInicio()) * 60) / DURACION_CITA) - 1);
			
			// Primera hora en la que se podría pedir cita
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
		
		// Con este bucle obtenemos los dias y horas en los que el médico ya tiene citas asignadas		
		for (Cita c: citas) {
			horasOcupadas = new ArrayList<String> ();
			fecha = c.getFechaYhora();
			dia = formatoDeFecha.format(fecha);
			
			// Si para ese día aun no se han incluido las horas ocupadas, se añaden
			if (citasOcupadasMedico.get(dia) == null) {
				if (fecha.getMinutes() == 0)
					minutos = "00";
				else
					minutos = Integer.toString(fecha.getMinutes());
				horasOcupadas.add(fecha.getHours()+":"+minutos);
				citasOcupadasMedico.put(dia, horasOcupadas);
				
			}
			// Si ya existian horas ocupadas para ese día, se añade la nueva hora
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
