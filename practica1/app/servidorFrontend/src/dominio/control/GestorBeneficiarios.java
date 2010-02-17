package dominio.control;

import java.sql.SQLException;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import persistencia.FPBeneficiario;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import persistencia.FuncionesPersistencia;
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
	public static Beneficiario consultarBeneficiarioPorNIF(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Beneficiario beneficiario = null;
		Medico medico;
		
		// Comprobamos los parámetros pasados
		if(dni == null) {
			throw new NullPointerException("El NIF del beneficiario buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		
		// Obtenemos el beneficiario con el NIF indicado
		beneficiario = FPBeneficiario.consultarPorNIF(dni);
				
		// Miramos si es necesario cambiar el médico asignado al beneficiario
		// (por si ya tiene más de 14 años pero sigue con un pediatra)
		medico = comprobarMedicoBeneficiario(beneficiario);
		if(!medico.equals(beneficiario.getMedicoAsignado())) {
			// Cambiamos el médico y lo guardamos en la base de datos
			beneficiario.setMedicoAsignado(medico);
			FPBeneficiario.modificar(beneficiario);
		}
		
		// Si el beneficiario no tiene médico asignado, se lanza una excepción
		if (beneficiario.getMedicoAsignado() == null)
			throw new UsuarioIncorrectoException("El beneficiario con NIF " + beneficiario.getNif() + " no tiene asignado médico.");
		return beneficiario;
	}
	
	// Método para obtener los datos de un beneficiario consultando por NSS
	public static Beneficiario consultarBeneficiarioPorNSS(long idSesion, String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Beneficiario beneficiario;
		Medico medico;

		// Comprobamos los parámetros pasados
		if(nss == null) {
			throw new NullPointerException("El NSS del beneficiario buscado no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		
		// Obtenemos el beneficiario con el NSS indicado
		beneficiario = FPBeneficiario.consultarPorNSS(nss);

		// Miramos si es necesario cambiar el médico asignado al beneficiario
		medico = comprobarMedicoBeneficiario(beneficiario);
		if(!medico.equals(beneficiario.getMedicoAsignado())) {
			// Cambiamos el médico y lo guardamos en la base de datos
			beneficiario.setMedicoAsignado(medico);
			FPBeneficiario.modificar(beneficiario);
		}

		return beneficiario;
	}
	
	// Método para registrar un nuevo beneficiario en el sistema
	public static void crearBeneficiario(long idSesion, Beneficiario beneficiario) throws SQLException, BeneficiarioYaExistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Medico medico;
		boolean existe;
		
		// Comprobamos los parámetros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a crear no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarBeneficiario);
		
		// Consultamos si ya existe otro usuario u otro
		// beneficiario con el mismo DNI
		existe = FuncionesPersistencia.existeNIF(beneficiario.getNif());
		if(existe) {
			throw new BeneficiarioYaExistenteException("Ya existe una persona en el sistema registrada con el NIF " + beneficiario.getNif() + "."); 
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
		Medico medico;
		
		// Comprobamos los parámetros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarBeneficiario);

		// Comprobamos si existe el beneficiario que se quiere modificar
		FPBeneficiario.consultarPorNIF(beneficiario.getNif());

		// Miramos si es necesario cambiar el médico asignado al beneficiario
		// (en caso de que se haya cambiado su fecha de nacimiento)
		medico = comprobarMedicoBeneficiario(beneficiario);
		if(!medico.equals(beneficiario.getMedicoAsignado())) {
			// Cambiamos el médico antes de guardarlo en la base de datos
			beneficiario.setMedicoAsignado(medico);
		}

		// Modificamos los datos del beneficiario
		FPBeneficiario.modificar(beneficiario);
	}
	
	// Método para eliminar un beneficiario del sistema
	public static void eliminarBeneficiario(long idSesion, Beneficiario bene) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, NullPointerException {
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
	
	public static void asignarMedico (long idSesion, Beneficiario beneficiario) throws SQLException, CentroSaludInexistenteException, DireccionInexistenteException, UsuarioIncorrectoException, NullPointerException, OperacionIncorrectaException, SesionInvalidaException, BeneficiarioInexistenteException {
		Medico medico;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarBeneficiario);
		
		// Buscamos un médico de cabecera o pediatra para el 
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
		// Actualizamos el beneficiario
		modificarBeneficiario(idSesion, beneficiario);
	}
	
	private static Medico comprobarMedicoBeneficiario(Beneficiario beneficiario) throws SQLException, CentroSaludInexistenteException, DireccionInexistenteException, UsuarioIncorrectoException {
		Medico medico;
		
		// Por defecto, el beneficiario se quedará con su médico
		medico = beneficiario.getMedicoAsignado();
		
		// Si el beneficiario tiene más de 14 años y tenía asignado un
		// pediatra (o no tenia médico asignado), se le busca un médico de cabecera
		if(beneficiario.getEdad() >= EDAD_PEDIATRA)
			if (beneficiario.getMedicoAsignado()==null || beneficiario.getMedicoAsignado().getTipoMedico().getCategoria() == CategoriasMedico.Pediatra) {
				try {
					medico = (Medico)FPUsuario.consultar(FPTipoMedico.consultarMedicoAleatorio(CategoriasMedico.Cabecera));
				} catch(UsuarioIncorrectoException e) {
					throw new UsuarioIncorrectoException("No se puede cambiar el médico asignado al beneficiario porque no existe ningún médico de cabecera en el sistema.");
				}
		}
		
		// Si el beneficiario tiene menos de 14 años y tenía asignado
		// un médico de cabecera (o no tenia médico), se le busca un pediatra 
		if(beneficiario.getEdad() < EDAD_PEDIATRA)
			if (beneficiario.getMedicoAsignado()==null || beneficiario.getMedicoAsignado().getTipoMedico().getCategoria() == CategoriasMedico.Cabecera) {
				try {
					medico = (Medico)FPUsuario.consultar(FPTipoMedico.consultarMedicoAleatorio(CategoriasMedico.Pediatra));
				} catch(UsuarioIncorrectoException e) {
					throw new UsuarioIncorrectoException("No se puede cambiar el médico asignado al beneficiario porque no existe ningún pediatra en el sistema.");
				}
		}
		return medico;
	}
	
}
