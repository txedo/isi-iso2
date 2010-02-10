package dominio.control;

import java.sql.SQLException;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Pediatra;
import persistencia.FPBeneficiario;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.DireccionIncorrectaException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de consultar, a�adir y modificar beneficiarios en
 * el sistema.
 */
public class GestorBeneficiarios {

	private static final int EDAD_PEDIATRA = 14;
	
	// M�todo para obtener los datos de un beneficiario consultando por NIF
	public static Beneficiario consultarBeneficiario(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionIncorrectaException {
		Beneficiario beneficiario;
		
		// Comprobamos los par�metros pasados
		if(dni == null) {
			throw new NullPointerException("El NIF del beneficiario buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		
		// Obtenemos el beneficiario con el NIF indicado
		beneficiario = FPBeneficiario.consultarPorNIF(dni);
		
		// Comprobamos si el beneficiario tiene m�s de 14 a�os pero
		// a�n sigue con un pediatra asignado como m�dico
		comprobarMedicoBeneficiario(beneficiario);
		
		return beneficiario;
	}
	
	// M�todo para obtener los datos de un beneficiario consultando por NSS
	public static Beneficiario consultarBeneficiarioPorNSS(long idSesion, String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionIncorrectaException {
		Beneficiario beneficiario;

		// Comprobamos los par�metros pasados
		if(nss == null) {
			throw new NullPointerException("El NSS del beneficiario buscado no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		
		// Obtenemos el beneficiario con el NSS indicado
		beneficiario = FPBeneficiario.consultarPorNSS(nss);

		// Comprobamos si el beneficiario tiene m�s de 14 a�os pero
		// a�n sigue con un pediatra asignado como m�dico
		comprobarMedicoBeneficiario(beneficiario);

		return beneficiario;
	}
	
	// M�todo para registrar un nuevo beneficiario en el sistema
	public static void crearBeneficiario(long idSesion, Beneficiario beneficiario) throws SQLException, BeneficiarioYaExistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionIncorrectaException {
		Medico medico;
		
		// Comprobamos los par�metros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a crear no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarBeneficiario);
		
		// Consultamos si ya existe un usuario con el mismo NIF
		// que el beneficiario que se quiere crear en la base de
		// datos, y en ese caso se lanza un error
		try {
			FPUsuario.consultar(beneficiario.getNif());
			throw new BeneficiarioYaExistenteException("No se puede registrar el beneficiario porque ya existe un usuario en el sistema con el NIF " + beneficiario.getNif() + ".");
		} catch(UsuarioIncorrectoException e) {
			// Lo normal es que se lance esta excepci�n
		}
		
		// Consultamos si ya existe un beneficiario con el mismo NSS del
		// beneficiario que se quiere crear, y en ese caso se lanza un error
		try {
			FPBeneficiario.consultarPorNSS(beneficiario.getNss());
			throw new BeneficiarioYaExistenteException("No se puede registrar el beneficiario porque ya existe otro beneficiario en el sistema con el NSS " + beneficiario.getNss() + ".");
		} catch(BeneficiarioInexistenteException e) {
			// Lo normal es que se lance esta excepci�n
		}
		
		// Buscamos un m�dico de cabecera o pediatra para el nuevo
		// beneficiario seg�n su edad; si se lanza una excepci�n es
		// porque no existen m�dicos registrados del tipo adecuado
		if(beneficiario.getEdad() < EDAD_PEDIATRA) {
			try {
				medico = FPTipoMedico.consultarTipoMedicoAleatorio(new Pediatra());
				beneficiario.setMedicoAsignado(medico);
			} catch(UsuarioIncorrectoException e) {
				throw new UsuarioIncorrectoException("No se puede registrar el beneficiario porque no existe ning�n pediatra en el sistema que se le pueda asignar.");
			}
		} else {
			try {
				medico = FPTipoMedico.consultarTipoMedicoAleatorio(new Cabecera());
				beneficiario.setMedicoAsignado(medico);
			} catch(UsuarioIncorrectoException e) {
				throw new UsuarioIncorrectoException("No se puede registrar el beneficiario porque no existe ning�n m�dico de cabecera en el sistema que se le pueda asignar.");
			}
		}
		
		// A�adimos el beneficiario al sistema
		FPBeneficiario.insertar(beneficiario);
	}
	
	// M�todo para modificar un beneficiario existente en el sistema
	public static void modificarBeneficiario(long idSesion, Beneficiario beneficiario) throws OperacionIncorrectaException, SesionInvalidaException, BeneficiarioInexistenteException, SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, NullPointerException, DireccionIncorrectaException {
		// Comprobamos los par�metros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarBeneficiario);
		
		// Comprobamos si existe el beneficiario que se quiere modificar
		// y, en ese caso, lo modificamos
		FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		FPBeneficiario.modificar(beneficiario);
	}
	
	private static void comprobarMedicoBeneficiario(Beneficiario beneficiario) throws SQLException, CentroSaludIncorrectoException, UsuarioIncorrectoException, DireccionIncorrectaException {
		Medico medico;
		
		// Si el beneficiario tiene m�s de 14 a�os y ten�a asignado un
		// pediatra, se le asigna un m�dico de cabecera y se actualiza
		if(beneficiario.getEdad() >= EDAD_PEDIATRA && beneficiario.getMedicoAsignado().getTipoMedico().getCategoria() == CategoriasMedico.Pediatra) {
			try {
				medico = FPTipoMedico.consultarTipoMedicoAleatorio(new Cabecera());
				beneficiario.setMedicoAsignado(medico);
				FPBeneficiario.modificar(beneficiario);
			} catch(UsuarioIncorrectoException e) {
				throw new UsuarioIncorrectoException("No se puede actualizar el m�dico asignado al beneficiario porque no existe ning�n m�dico de cabecera en el sistema.");
			}
		}
	}
	
}
