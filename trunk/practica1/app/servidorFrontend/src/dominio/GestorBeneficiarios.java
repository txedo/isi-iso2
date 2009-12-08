package dominio;

import java.sql.SQLException;

import persistencia.FPBeneficiario;
import persistencia.FPUsuario;
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
	public static Beneficiario getBeneficiario(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		Beneficiario bene = null;
		EntradaLog entrada;
		
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ConsultarBeneficiario);
		bene = FPBeneficiario.consultarPorNIF(dni);
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "read", "Se consultan los datos del beneficiario con NIF "+dni);
		entrada.insertar();
		
		return bene;
	}
	
	public static Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		Beneficiario bene = null;
		EntradaLog entrada;

		GestorSesiones.comprobarPermiso(idSesion, Operacion.ConsultarBeneficiario);
		bene = FPBeneficiario.consultarPorNSS(nss);
		entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "read", "Se consultan los datos del beneficiario con NSS "+nss);
		entrada.insertar();

		return bene;
	}
	

	/* Metodo para registrar un nuevo beneficiario.
	 * Se realiza esta operacion si la sesion tiene permisos suficientes */
	public static void crear(long idSesion, Beneficiario beneficiario) throws SQLException, BeneficiarioYaExistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, Exception {

		Medico medico = null;
		EntradaLog entrada;
		GestorSesiones.comprobarPermiso(idSesion, Operacion.RegistrarBeneficiario);
		// Se consulta para comprobar si ese beneficiario ya existe.
		// Si existe, se lanza una excepcion.
		// Si no existe, se captura la BeneficiarioIncorrectoException y se registra
		try {
			FPBeneficiario.consultarPorNIF(beneficiario.getNif());
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "El beneficiario con NIF "+beneficiario.getNif()+" ya existe y no se puede crear");
			entrada.insertar();
			throw new BeneficiarioYaExistenteException("El beneficiario con NIF "+beneficiario.getNif()+ " ya existe en la base de datos. No se puede registrar de nuevo.");
		}
		catch(BeneficiarioInexistenteException e){
			// Le asignamos un medico aleatorio al nuevo beneficiario
			medico = (Medico)FPUsuario.consultarAleatorio(Roles.Medico);
			beneficiario.setMedicoAsignado(medico);
			FPBeneficiario.insertar(beneficiario);
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "create", "El beneficiario con NIF "+beneficiario.getNif()+" se ha registrado satisfactoriamente");
			entrada.insertar();
		}

	}
	
	/* Metodo para modificar los datos de un beneficiario.
	 * Se realiza esta operacion si la sesion tiene permisos suficientes */
	public static void modificar(long idSesion, Beneficiario beneficiario) throws OperacionIncorrectaException, SesionInvalidaException, BeneficiarioInexistenteException, SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException{
		EntradaLog entrada;
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ModificarBeneficiario);
		// Se consulta para comprobar si ese beneficiario ya existe.
		// Si no existe, se lanza una excepcion.
		// Si existe, se actualiza
		try {
			FPBeneficiario.consultarPorNIF(beneficiario.getNif());
			FPBeneficiario.modificar(beneficiario);
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "Se han actualizado los datos del beneficiario con NIF "+beneficiario.getNif());
			entrada.insertar();
		}
		catch(BeneficiarioInexistenteException e){
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "update", "El beneficiario con NIF "+beneficiario.getNif()+" no existe y no se puede actualizar");
			entrada.insertar();
			throw e;
		}
	}
	
}
