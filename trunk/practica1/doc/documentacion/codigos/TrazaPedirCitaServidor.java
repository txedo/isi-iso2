public class GestorCitas {
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws ... {
		// Comprobamos los parámetros pasados
		// (...)
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCita);
		// Comprobamos que exista el beneficiario
		beneficiario = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		if(beneficiario.getMedicoAsignado() == null) {
			throw new MedicoInexistenteException(...);
		}
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(idMedico);
			if(usuario.getRol() != RolesUsuario.Médico) {
				throw new MedicoInexistenteException(...);
			}
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(...);
		}
		// Comprobamos que el médico para el que se va a pedir cita
		// sea el médico que el beneficiario tiene asignado
		medico = (Medico)usuario;
		if(!medico.getNif().equals(beneficiario.getMedicoAsignado().getNif())) {
			throw new CitaNoValidaException(...);
		}
		// Comprobamos que la fecha de la cita sea posterior a la actual
		if(UtilidadesDominio.fechaAnterior(fechaYHora, new Date(), true)) {
			throw new FechaNoValidaException(...);
		}
		// Comprobamos que la hora de la cita sea múltiplo de
		// la duración, para que si las citas duran 15 minutos,
		// no se pueda pedir cita a las 19:38, por ejemplo
		hora = Calendar.getInstance();
		hora.setTime(fechaYHora);
		if((double)hora.get(Calendar.MINUTE) / IConstantes.DURACION_CITA != hora.get(Calendar.MINUTE) / IConstantes.DURACION_CITA) {
			throw new FechaNoValidaException(...);
		}
		// Comprobamos que, según el horario del médico, se pase
		// consulta a la fecha y hora introducidas
		if(!medico.fechaEnCalendario(fechaYHora, duracion)) {
			throw new FechaNoValidaException(...);
		}
		// Recuperamos las citas del médico para comprobar que no
		// tiene ya una cita en esa misma fecha y hora
		citas = FPCita.consultarPorMedico(idMedico);	
		for(Cita c : citas) {
			if(c.getFechaYHora().equals(fechaYHora)) {
				throw new CitaNoValidaException(...);
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
}