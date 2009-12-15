package dominio;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Vector;

import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;

import persistencia.FPBeneficiario;
import persistencia.FPCita;
import persistencia.FPEntradaLog;
import persistencia.FPUsuario;

public class GestorCitas {

	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYhora, long duracion) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, MedicoInexistenteException {
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
		// ********** Algo para comprobar si la fecha es valida o no *************
		
		// Si todo es valido, se actualiza la cita y se devuelve
		c.setBeneficiario(beneficiario);
		c.setMedico(med);
		c.setDuracion(duracion);
		c.setFechaYhora(fechaYhora);
		
		return c;
	}

	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		Cita c = new Cita();
		Beneficiario bene = null;
		Medico med = null; 
		// Se comprueba que exista el beneficiario
		bene = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		
		// ********** Algo para comprobar si la fecha es valida o no *************
		
		// Si todo es valido, se actualiza la cita y se devuelve
		c.setBeneficiario(beneficiario);
		c.setMedico(med);
		c.setDuracion(duracion);
		// ****** ¿Que se hace con el volante *****?
		
		
		return c; 
	}

	public static Vector<Cita> getCitas(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ObtenerCitas);
		// Añadimos una entrada al log
		EntradaLog entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "read", "Se han consultado las citas del beneficiario con DNI "+ dni.toString() + ".");
		FPEntradaLog.insertar(entrada);
		return FPCita.consultarTodo(dni);
	}

	public static void anularCita(long idSesion, Cita cita) throws SQLException, SesionInvalidaException, OperacionIncorrectaException {
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operacion.EliminarCita);
		// Añadimos una entrada al log
		EntradaLog entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "delete", "Se ha eliminado la cita del beneficiario con DNI "+cita.getBeneficiario().getNif()+" con el medico "+cita.getMedico().getNombre() +" del dia "+cita.getFechaYhora());
		FPEntradaLog.insertar(entrada);
		FPCita.eliminar(cita);
	}

}
