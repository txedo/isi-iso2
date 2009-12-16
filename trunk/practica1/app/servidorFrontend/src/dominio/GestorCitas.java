package dominio;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Vector;

import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
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

public class GestorCitas {

	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYhora, long duracion) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, MedicoInexistenteException, FechaNoValidaException {
		Cita c = new Cita();
		Beneficiario bene = null;
		Medico med = null; 
		// Se comprueba que exista el beneficiario
		bene = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		// Se comprueba que exista el medico
		try {
			med = (Medico) FPUsuario.consultar(idMedico);
		}catch (UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Se comprueba que la fecha introducida sea valida para el medico dado
		if (!med.fechaEnCalendario(fechaYhora, duracion))
			throw new FechaNoValidaException("La fecha, hora y duracion dadas no son validas para concertar una cita con el medico con DNI "+med.getDni());
		
		// Si todo es valido, se actualiza la cita, se inserta y se devuelve
		c.setBeneficiario(beneficiario);
		c.setMedico(med);
		c.setDuracion(duracion);
		c.setFechaYhora(fechaYhora);
		FPCita.insertar(c);
		// Creamos la entrada del log
		EntradaLog entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se almacena la cita del beneficiario con DNI "+ beneficiario.getNif() + " con el medico con DNI "+med.getDni()+".");
		FPEntradaLog.insertar(entrada);
		
		return c;
	}

	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYhora, long duracion) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, VolanteNoValidoException, FechaNoValidaException {
		Cita c = new Cita();
		Beneficiario bene = null;
		Volante vol = null;
		
		// Se comprueba que exista el volante en la base de datos
		vol = FPVolante.consultarPorID(idVolante);
		
		// Se comprueba que exista el beneficiario que se pasa por parametro
		bene = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		// Se comprueba que el beneficiario que se pasa por parametro y el que esta en el volante sean los mismos
		if (!bene.equals(vol.getBeneficiario()))
			throw new VolanteNoValidoException("El beneficiario incluido en el volante no coincide con el beneficiario que pide cita");
		
		// Se comprueba que la fecha introducida sea valida para el medico dado
		if (!vol.getReceptor().fechaEnCalendario(fechaYhora, duracion))
			throw new FechaNoValidaException("La fecha, hora y duracion dadas no son validas para concertar una cita con el medico con DNI "+vol.getReceptor().getDni());
		
		// Si todo es valido, se actualiza la cita, se inserta y se devuelve
		c.setBeneficiario(beneficiario);
		c.setMedico(vol.getReceptor());
		c.setDuracion(duracion);
		FPCita.insertar(c);	
		EntradaLog entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se almacena la cita del beneficiario con DNI "+ beneficiario.getNif() + " con el medico con DNI "+vol.getReceptor().getDni()+".");
		FPEntradaLog.insertar(entrada);
		return c; 
	}

	public static Vector<Cita> getCitas(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ObtenerCitas);
		// A�adimos una entrada al log
		EntradaLog entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "Se han consultado las citas del beneficiario con DNI "+ dni.toString() + ".");
		FPEntradaLog.insertar(entrada);
		return FPCita.consultarTodo(dni);
	}

	public static void anularCita(long idSesion, Cita cita) throws SQLException, SesionInvalidaException, OperacionIncorrectaException {
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operacion.EliminarCita);
		// A�adimos una entrada al log
		EntradaLog entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "Se ha eliminado la cita del beneficiario con DNI "+cita.getBeneficiario().getNif()+" con el medico "+cita.getMedico().getNombre() +" del dia "+cita.getFechaYhora());
		FPEntradaLog.insertar(entrada);
		FPCita.eliminar(cita);
	}

}
