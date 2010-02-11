package dominio.control;

import java.sql.SQLException;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import persistencia.FPBeneficiario;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de consultar, añadir y modificar beneficiarios en
 * el sistema.
 */
public class GestorBeneficiarios {

	private static final int EDAD_PEDIATRA = 14;
	
	// Método para obtener los datos de un beneficiario consultando por NIF
	public static Beneficiario consultarBeneficiario(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Beneficiario beneficiario;
		
		// Comprobamos los parámetros pasados
		if(dni == null) {
			throw new NullPointerException("El NIF del beneficiario buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		
		// Obtenemos el beneficiario con el NIF indicado
		beneficiario = FPBeneficiario.consultarPorNIF(dni);
		
		// Comprobamos si el beneficiario tiene más de 14 años pero
		// aún sigue con un pediatra asignado como médico
		comprobarMedicoBeneficiario(beneficiario);
		Medico m = comprobarMedicoBeneficiario(beneficiario);
		// Si se ha cambiado el médico, se asigna el nuevo y se modifica
		if (!m.equals(beneficiario.getMedicoAsignado())) {
			beneficiario.setMedicoAsignado(m);
			FPBeneficiario.modificar(beneficiario);
		}
		
		return beneficiario;
	}
	
	// Método para obtener los datos de un beneficiario consultando por NSS
	public static Beneficiario consultarBeneficiarioPorNSS(long idSesion, String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Beneficiario beneficiario;

		// Comprobamos los parámetros pasados
		if(nss == null) {
			throw new NullPointerException("El NSS del beneficiario buscado no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		
		// Obtenemos el beneficiario con el NSS indicado
		beneficiario = FPBeneficiario.consultarPorNSS(nss);

		// Comprobamos si el beneficiario tiene más de 14 años pero
		// aún sigue con un pediatra asignado como médico
		Medico m = comprobarMedicoBeneficiario(beneficiario);
		// Si se ha cambiado el médico, se asigna el nuevo y se modifica
		if (!m.equals(beneficiario.getMedicoAsignado())) {
			beneficiario.setMedicoAsignado(m);
			FPBeneficiario.modificar(beneficiario);
		}

		return beneficiario;
	}
	
	// Método para registrar un nuevo beneficiario en el sistema
	public static void crearBeneficiario(long idSesion, Beneficiario beneficiario) throws SQLException, BeneficiarioYaExistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Medico medico;
		
		// Comprobamos los parámetros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a crear no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarBeneficiario);
		
		// Consultamos si ya existe un beneficiario con el mismo NIF 
		try {
			FPBeneficiario.consultarPorNIF(beneficiario.getNif());
			throw new BeneficiarioYaExistenteException("El beneficiario con el NIF " + beneficiario.getNif() + " ya se encuentra dado de alta en el sistema y no se puede registrar de nuevo.");
		} catch(BeneficiarioInexistenteException e) {
			// Lo normal es que se lance esta excepción
		}
		
		// Consultamos si ya existe un usuario con el mismo NIF que
		// el beneficiario que se quiere crear en la base de datos
		try {
			FPUsuario.consultar(beneficiario.getNif());
			throw new BeneficiarioYaExistenteException("No se puede registrar el beneficiario porque ya existe un usuario en el sistema con el NIF " + beneficiario.getNif() + ".");
		} catch(UsuarioIncorrectoException e) {
			// Lo normal es que se lance esta excepción
		}
		
		// Consultamos si ya existe un beneficiario con el mismo NSS
		try {
			FPBeneficiario.consultarPorNSS(beneficiario.getNss());
			throw new BeneficiarioYaExistenteException("El beneficiario con el NSS " + beneficiario.getNss() + " ya se encuentra dado de alta en el sistema y no se puede registrar de nuevo.");
		} catch(BeneficiarioInexistenteException e) {
			// Lo normal es que se lance esta excepción
		}
		
		// Buscamos un médico de cabecera o pediatra para el nuevo
		// beneficiario según su edad; si se lanza una excepción es
		// porque no existen médicos registrados del tipo adecuado
		if(beneficiario.getEdad() < EDAD_PEDIATRA) {
			try {
				medico = (Medico)FPUsuario.consultar(FPTipoMedico.consultarMedicoAleatorio(CategoriasMedico.Pediatra));
				beneficiario.setMedicoAsignado(medico);
			} catch(UsuarioIncorrectoException e) {
				throw new UsuarioIncorrectoException("No se puede registrar el beneficiario porque no existe ningún pediatra en el sistema que se le pueda asignar.");
			}
		} else {
			try {
				medico = (Medico)FPUsuario.consultar(FPTipoMedico.consultarMedicoAleatorio(CategoriasMedico.Cabecera));
				beneficiario.setMedicoAsignado(medico);
			} catch(UsuarioIncorrectoException e) {
				throw new UsuarioIncorrectoException("No se puede registrar el beneficiario porque no existe ningún médico de cabecera en el sistema que se le pueda asignar.");
			}
		}
		
		// Añadimos el beneficiario al sistema
		FPBeneficiario.insertar(beneficiario);
	}
	
	// Método para modificar un beneficiario existente en el sistema
	public static void modificarBeneficiario(long idSesion, Beneficiario beneficiario) throws OperacionIncorrectaException, SesionInvalidaException, BeneficiarioInexistenteException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		// Comprobamos los parámetros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarBeneficiario);
		
		// Comprobamos si existe el beneficiario que se quiere modificar
		FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		
		// Miramos si, al cambiar su fecha de nacimiento, hay que modificar su médico asignado
		Medico m = comprobarMedicoBeneficiario(beneficiario);
		// Si se ha cambiado el médico, se asigna el nuevo y se modifica
		if (!m.equals(beneficiario.getMedicoAsignado()))
			beneficiario.setMedicoAsignado(m);
		
		FPBeneficiario.modificar(beneficiario);
	}
	
	// Método para eliminar un beneficiario del sistema
	public static void eliminarBeneficiario(long idSesion, Beneficiario bene) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		// Comprobamos los parámetros pasados
		if(bene == null) {
			throw new NullPointerException("El beneficiario que se va a eliminar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarBeneficiario);
		
		// Comprobamos si realmente existe el usuario que se quiere borrar
		FPBeneficiario.consultarPorNIF(bene.getNif());
		
		// Borramos los datos del beneficiario
		FPBeneficiario.eliminar(bene);
	}
	
	private static Medico comprobarMedicoBeneficiario(Beneficiario beneficiario) throws SQLException, CentroSaludInexistenteException, DireccionInexistenteException, UsuarioIncorrectoException {
		Medico medico = beneficiario.getMedicoAsignado();
		
		// Si el beneficiario tiene más de 14 años y tenía asignado un
		// pediatra, se le busca un médico de cabecera
		if(beneficiario.getEdad() >= EDAD_PEDIATRA && beneficiario.getMedicoAsignado().getTipoMedico().getCategoria() == CategoriasMedico.Pediatra) {
			try {
				medico = (Medico)FPUsuario.consultar(FPTipoMedico.consultarMedicoAleatorio(CategoriasMedico.Cabecera));
			} catch(UsuarioIncorrectoException e) {
				throw new UsuarioIncorrectoException("No se puede cambiar el médico asignado al beneficiario porque no existe ningún médico de cabecera en el sistema.");
			}
		}
		return medico;
	}
	
}
