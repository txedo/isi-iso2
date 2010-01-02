package dominio.control;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
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
import persistencia.FPUsuario;
import persistencia.FPVolante;

/**
 * Clase encargada de solicitar, recuperar y eliminar citas que tienen los
 * beneficiarios para acudir a la consulta de los médicos.
 */
public class GestorCitas {

	// Método para pedir una nueva cita para un cierto beneficiario y médico
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYhora, long duracion) throws Exception {
		Cita cita;
		EntradaLog entrada;
		Usuario usuario;
		Medico medico;
		Vector<Cita> citas;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.TramitarCita);
		
		// Comprobamos que exista el beneficiario
		FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		
		// Comprobamos que exista el médico
		try {
			usuario = FPUsuario.consultar(idMedico);
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
		
		return cita;
	}

	// Método para pedir una nueva cita para un cierto beneficiario a partir de un volante
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYhora, long duracion) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, VolanteNoValidoException, FechaNoValidaException, SesionInvalidaException, OperacionIncorrectaException {
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
		
		// Comprobamos que la fecha introducida sea válida para el medico dado
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
		FPCita.insertar(cita);	
		
		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "La cita del beneficiario con NIF " + beneficiario.getNif() + " con el medico con DNI " + medico.getDni() + ", asociada al volante " + String.valueOf(idVolante) + ", se ha creado correctamente.");
		FPEntradaLog.insertar(entrada);
		
		return cita; 
	}

	// Método para obtener todas las citas de un beneficiario
	public static Vector<Cita> getCitas(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {
		Vector<Cita> citas;
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ObtenerCitas);
		
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
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarCita);

		// Se comprueba si la cita existe
		if (!FPCita.existe(cita))
			throw new CitaNoValidaException("No existe la cita del beneficiario " + cita.getBeneficiario().getNif()+ " con el medico " + cita.getMedico().getDni()+ " para poder anularla");
		
		// Eliminamos la cita de la base de datos
		FPCita.eliminar(cita);

		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "Se ha eliminado la cita del beneficiario con DNI "+cita.getBeneficiario().getNif()+" con el medico "+cita.getMedico().getNombre() +" del dia "+cita.getFechaYhora());
		FPEntradaLog.insertar(entrada);
	}

	public static long emitirVolante(long idSesion, Beneficiario b, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, SesionInvalidaException, OperacionIncorrectaException, VolanteNoValidoException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		Usuario usuario;
		Volante volante;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarCita);
		
		// Se comprueba que existe el beneficiario dado por parámetro
		FPBeneficiario.consultarPorNIF(b.getNif());
		
		// Se comprueba que existen los médicos, tanto emisor como receptor
		try {
			usuario = FPUsuario.consultar(emisor.getDni());
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
		volante.setEmisor(emisor);
		volante.setReceptor(destino);
		FPVolante.insertar(volante);
		
		// Añadimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se ha emitido un volante para el beneficiario "+volante.getBeneficiario().getNif()+", emitido por el medico "+volante.getEmisor().getNombre() +" para el especialista "+volante.getReceptor().getNombre());
		FPEntradaLog.insertar(entrada);
		
		return volante.getId();
	}
}
