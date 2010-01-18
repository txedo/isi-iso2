package dominio.control;

import java.sql.SQLException;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Pediatra;
import persistencia.FPBeneficiario;
import persistencia.FPEntradaLog;
import persistencia.FPTipoMedico;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de consultar, añadir, modificar y eliminar beneficiarios
 * en el sistema.
 */
public class GestorBeneficiarios {

	/* Metodo que devuelve un beneficiario, consultando por su dni.
	 * Se realiza esta operacion si la sesion tiene permisos suficientes */
	public static Beneficiario getBeneficiario(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {
		Beneficiario bene = null;
		EntradaLog entrada;
		
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		bene = FPBeneficiario.consultarPorNIF(dni);
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "read", "Se consultan los datos del beneficiario con NIF "+dni);
		FPEntradaLog.insertar(entrada);
		
		return bene;
	}
	
	public static Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {
		Beneficiario bene = null;
		EntradaLog entrada;

		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		bene = FPBeneficiario.consultarPorNSS(nss);
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "read", "Se consultan los datos del beneficiario con NSS "+nss);
		FPEntradaLog.insertar(entrada);

		return bene;
	}
	

	/* Metodo para registrar un nuevo beneficiario.
	 * Se realiza esta operacion si la sesion tiene permisos suficientes */
	public static void crear(long idSesion, Beneficiario beneficiario) throws SQLException, BeneficiarioYaExistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException {

		Medico medico = null;
		EntradaLog entrada;
		
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarBeneficiario);
		// Se consulta para comprobar si ese beneficiario ya existe.
		// Si existe, se lanza una excepcion.
		// Si no existe, se captura la BeneficiarioIncorrectoException y se comprueba que no se repita el NSS
		// Si no se repite el NSS, se registra el nuevo beneficiario
		try {
			FPBeneficiario.consultarPorNIF(beneficiario.getNif());
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "El beneficiario con NIF "+beneficiario.getNif()+" ya existe y no se puede crear");
			FPEntradaLog.insertar(entrada);
			throw new BeneficiarioYaExistenteException("El beneficiario con NIF "+beneficiario.getNif()+ " ya existe en la base de datos. No se puede registrar de nuevo.");
		}
		catch(BeneficiarioInexistenteException e){
			try {
				FPBeneficiario.consultarPorNSS(beneficiario.getNss());
				entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "El beneficiario con NIF "+beneficiario.getNif()+" no se puede registrar porque ya existe el NSS " + beneficiario.getNss()+ " en otro beneficiario");
				FPEntradaLog.insertar(entrada);
				throw new BeneficiarioYaExistenteException("El beneficiario con NIF "+beneficiario.getNif()+" no se puede registrar porque ya existe el NSS " + beneficiario.getNss()+ " en otro beneficiario");
			}
			catch(BeneficiarioInexistenteException bie) {
				// Se asigna un medico de cabecera o un pediatra, segun la edad
				// Se captura la excepcion por si no existe médicos registrados del tipo adecuado
				if (beneficiario.getEdad()<14)
					try {
						medico = FPTipoMedico.consultarTipoMedicoAleatorio(new Pediatra());
					} catch (UsuarioIncorrectoException uie) {
						entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "No se puede registrar el beneficiario con NIF " +beneficiario.getNif() + " porque no existe un pediatra en el sistema para asignárselo");
						FPEntradaLog.insertar(entrada);
						throw new SQLException("No se puede registrar el beneficiario con NIF " +beneficiario.getNif() + " porque no existe un pediatra en el sistema para asignárselo");
					}
				else
					try {
						medico = FPTipoMedico.consultarTipoMedicoAleatorio(new Cabecera());
					} catch (UsuarioIncorrectoException uie) {
						entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "No se puede registrar el beneficiario con NIF " +beneficiario.getNif() + " porque no existe un médico de cabecera en el sistema para asignárselo");
						FPEntradaLog.insertar(entrada);
						throw new SQLException("No se puede registrar el beneficiario con NIF " +beneficiario.getNif() + " porque no existe un médico de cabecera en el sistema para asignárselo");
					}
				beneficiario.setMedicoAsignado(medico);
				FPBeneficiario.insertar(beneficiario);
				entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "El beneficiario con NIF "+beneficiario.getNif()+" se ha registrado satisfactoriamente");
				FPEntradaLog.insertar(entrada);
			}
		}

	}
	
	/* Metodo para modificar los datos de un beneficiario.
	 * Se realiza esta operacion si la sesion tiene permisos suficientes */
	public static void modificar(long idSesion, Beneficiario beneficiario) throws OperacionIncorrectaException, SesionInvalidaException, BeneficiarioInexistenteException, SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		EntradaLog entrada;
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarBeneficiario);
		// Se consulta para comprobar si ese beneficiario ya existe.
		// Si no existe, se lanza una excepcion.
		// Si existe, se actualiza
		try {
			FPBeneficiario.consultarPorNIF(beneficiario.getNif());
			FPBeneficiario.modificar(beneficiario);
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "Se han actualizado los datos del beneficiario con NIF "+beneficiario.getNif());
			FPEntradaLog.insertar(entrada);
		}
		catch(BeneficiarioInexistenteException e){
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "El beneficiario con NIF "+beneficiario.getNif()+" no existe y no se puede actualizar");
			FPEntradaLog.insertar(entrada);
			throw e;
		}
	}
	
}
