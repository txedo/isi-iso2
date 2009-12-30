package dominio;

import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
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
 * beneficiarios para acudir a la consulta de los m�dicos.
 */
public class GestorCitas {

	// M�todo para pedir una nueva cita para un cierto beneficiario y m�dico
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYhora, long duracion) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, MedicoInexistenteException, FechaNoValidaException, SesionInvalidaException, OperacionIncorrectaException {
		Cita cita;
		EntradaLog entrada;
		Usuario usuario;
		Medico medico;
		
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
		
		// TODO: �Un beneficiario s�lo deber�a pedir cita para su m�dico asignado?
		
		// Comprobamos que la fecha introducida sea v�lida para el medico dado
		medico = (Medico)usuario;
		if(!medico.fechaEnCalendario(fechaYhora, duracion)) {
			throw new FechaNoValidaException("La fecha, hora y duraci�n dadas no son v�lidas para concertar una cita con el m�dico con DNI " + idMedico);
		}
		
		// TODO: Falta por comoprobar si el m�dico no tiene ya una cita a esa hora
		
		// Si todo es v�lido, se crea la cita, se inserta y se devuelve
		cita = new Cita();
		cita.setBeneficiario(beneficiario);
		cita.setMedico(medico);
		cita.setDuracion(duracion);
		cita.setFechaYhora(fechaYhora);
		FPCita.insertar(cita);
		
		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "La cita del beneficiario con NIF " + beneficiario.getNif() + " con el medico con DNI " + idMedico + " se ha creado correctamente.");
		FPEntradaLog.insertar(entrada);
		
		return cita;
	}

	// M�todo para pedir una nueva cita para un cierto beneficiario a partir de un volante
	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYhora, long duracion) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, VolanteNoValidoException, FechaNoValidaException, SesionInvalidaException, OperacionIncorrectaException {
		Cita cita;
		EntradaLog entrada;
		Beneficiario bene;
		Medico medico;
		Volante volante;
		
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
		
		// Comprobamos que la fecha introducida sea v�lida para el medico dado
		medico = volante.getReceptor();
		if(!medico.fechaEnCalendario(fechaYhora, duracion)) {
			throw new FechaNoValidaException("La fecha, hora y duraci�n dadas no son v�lidas para concertar una cita con el m�dico con DNI " + medico.getDni());
		}
		
		// TODO: Falta por comoprobar si el m�dico no tiene ya una cita a esa hora
		
		// Si todo es v�lido, se crea la cita, se inserta y se devuelve
		cita = new Cita();
		cita.setBeneficiario(beneficiario);
		cita.setMedico(medico);
		cita.setDuracion(duracion);
		FPCita.insertar(cita);	
		
		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "La cita del beneficiario con NIF " + beneficiario.getNif() + " con el medico con DNI " + medico.getDni() + ", asociada al volante " + String.valueOf(idVolante) + ", se ha creado correctamente.");
		FPEntradaLog.insertar(entrada);
		
		return cita; 
	}

	// M�todo para obtener todas las citas de un beneficiario
	public static Vector<Cita> getCitas(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {
		Vector<Cita> citas;
		EntradaLog entrada;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ObtenerCitas);
		
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
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarCita);

		// TODO: Comprobar si la cita existe, y si no lanzar una CitaNoValidaException
		if(false) throw new CitaNoValidaException("---");
		
		// Eliminamos la cita de la base de datos
		FPCita.eliminar(cita);

		// A�adimos una entrada al log
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "Se ha eliminado la cita del beneficiario con DNI "+cita.getBeneficiario().getNif()+" con el medico "+cita.getMedico().getNombre() +" del dia "+cita.getFechaYhora());
		FPEntradaLog.insertar(entrada);
	}

}
