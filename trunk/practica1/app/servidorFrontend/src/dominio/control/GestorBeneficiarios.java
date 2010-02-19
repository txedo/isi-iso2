package dominio.control;

import java.sql.SQLException;
import java.util.Vector;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import persistencia.FPBeneficiario;
import persistencia.FPUsuario;
import persistencia.UtilidadesPersistencia;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
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
	public static Beneficiario consultarBeneficiarioPorNIF(long idSesion, String dni) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Beneficiario beneficiario = null;
		Medico medico;
		
		// Comprobamos los par�metros pasados
		if(dni == null) {
			throw new NullPointerException("El NIF del beneficiario buscado no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		
		// Obtenemos el beneficiario con el NIF indicado
		beneficiario = FPBeneficiario.consultarPorNIF(dni);
				
		// Miramos si es necesario cambiar el m�dico asignado al beneficiario
		medico = obtenerMedicoBeneficiario(beneficiario);
		if(medico == null || !medico.equals(beneficiario.getMedicoAsignado())) {
			// Cambiamos el m�dico y lo guardamos en la base de datos
			beneficiario.setMedicoAsignado(medico);
			FPBeneficiario.modificar(beneficiario);
		}
		
		return beneficiario;
	}
	
	// M�todo para obtener los datos de un beneficiario consultando por NSS
	public static Beneficiario consultarBeneficiarioPorNSS(long idSesion, String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		Beneficiario beneficiario;
		Medico medico;

		// Comprobamos los par�metros pasados
		if(nss == null) {
			throw new NullPointerException("El NSS del beneficiario buscado no puede ser nulo.");
		}

		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiario);
		
		// Obtenemos el beneficiario con el NSS indicado
		beneficiario = FPBeneficiario.consultarPorNSS(nss);

		// Miramos si es necesario cambiar el m�dico asignado al beneficiario
		medico = obtenerMedicoBeneficiario(beneficiario);
		if(medico == null || !medico.equals(beneficiario.getMedicoAsignado())) {
			// Cambiamos el m�dico y lo guardamos en la base de datos
			beneficiario.setMedicoAsignado(medico);
			FPBeneficiario.modificar(beneficiario);
		}

		return beneficiario;
	}
	
	// M�todo para registrar un nuevo beneficiario en el sistema
	public static void crearBeneficiario(long idSesion, Beneficiario beneficiario) throws SQLException, BeneficiarioYaExistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, SesionInvalidaException, OperacionIncorrectaException, NullPointerException, DireccionInexistenteException {
		boolean existe;
		Medico medico;
		
		// Comprobamos los par�metros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a crear no puede ser nulo.");
		}
		if(beneficiario.getCentroSalud() == null) {
			throw new NullPointerException("El centro de salud del beneficiario que se va a crear no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.RegistrarBeneficiario);
		
		// Consultamos si ya existe otro usuario u otro
		// beneficiario en el sistema con el mismo DNI
		existe = UtilidadesPersistencia.existeNIF(beneficiario.getNif());
		if(existe) {
			throw new BeneficiarioYaExistenteException("Ya existe una persona en el sistema registrada con el NIF " + beneficiario.getNif() + "."); 
		}
		
		// Consultamos si ya existe un beneficiario con el mismo NSS
		try {
			FPBeneficiario.consultarPorNSS(beneficiario.getNss());
			throw new BeneficiarioYaExistenteException("El beneficiario con el NSS " + beneficiario.getNss() + " ya se encuentra dado de alta en el sistema y no se puede registrar de nuevo.");
		} catch(BeneficiarioInexistenteException e) {
			// Lo normal es que se lance esta excepci�n
		}

		// Intentamos asignar al beneficiario un m�dico del centro
		// elegido por �l; si no hay ning�n m�dico disponible, se lanza
		// una excepci�n para que el beneficiario pueda elegir otro centro
		beneficiario = (Beneficiario)beneficiario.clone();
		medico = obtenerMedicoBeneficiario(beneficiario);
		beneficiario.setMedicoAsignado(medico);
		if(medico == null) {
			throw new UsuarioIncorrectoException("No existe ning�n m�dico que se pueda asignar al beneficiario en el centro " + beneficiario.getCentroSalud().getNombre() + ".");
		}
		
		// A�adimos el beneficiario al sistema
		FPBeneficiario.insertar(beneficiario);
	}
	
	// M�todo para modificar un beneficiario existente en el sistema
	public static void modificarBeneficiario(long idSesion, Beneficiario beneficiario) throws OperacionIncorrectaException, SesionInvalidaException, BeneficiarioInexistenteException, SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Medico medico;
		
		// Comprobamos los par�metros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a modificar no puede ser nulo.");
		}
		if(beneficiario.getCentroSalud() == null) {
			throw new NullPointerException("El centro de salud del beneficiario que se va a modificar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ModificarBeneficiario);

		// Comprobamos si existe el beneficiario que se quiere modificar
		FPBeneficiario.consultarPorNIF(beneficiario.getNif());

		// Miramos si es necesario cambiar el m�dico asignado al beneficiario
		beneficiario = (Beneficiario)beneficiario.clone();
		medico = obtenerMedicoBeneficiario(beneficiario);
		if(medico == null) {
			throw new UsuarioIncorrectoException("No existe ning�n m�dico que se pueda asignar al beneficiario en el centro " + beneficiario.getCentroSalud().getNombre() + ".");
		}
		if(!medico.equals(beneficiario.getMedicoAsignado())) {
			// Cambiamos el m�dico antes de guardarlo en la base de datos
			beneficiario.setMedicoAsignado(medico);
		}

		// Modificamos los datos del beneficiario
		FPBeneficiario.modificar(beneficiario);
	}
	
	// M�todo para eliminar un beneficiario del sistema
	public static void eliminarBeneficiario(long idSesion, Beneficiario beneficiario) throws SesionInvalidaException, OperacionIncorrectaException, SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, NullPointerException {
		// Comprobamos los par�metros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario que se va a eliminar no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EliminarBeneficiario);
		
		// Comprobamos si realmente existe el usuario que se quiere borrar
		FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		
		// Borramos los datos del beneficiario
		FPBeneficiario.eliminar(beneficiario);
	}
	
	// M�todo que devuelve todos los beneficiarios que tiene un m�dico
	public static Vector<Beneficiario> consultarBeneficiariosMedico(long idSesion, String dniMedico) throws SQLException, SesionInvalidaException, OperacionIncorrectaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		Vector<Beneficiario> beneficiarios;
		Vector<String> nifs;
		
		// Comprobamos los par�metros pasados
		if(dniMedico == null) {
			throw new NullPointerException("El DNI del m�dico del que se quieren buscar sus beneficiarios no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarBeneficiariosMedico);
		
		// Obtenemos los NIFs de todos los beneficiarios asociados a ese m�dico
		nifs = FPBeneficiario.consultarBeneficiariosMedico(dniMedico);
		
		// Recuperamos los beneficiarios con los NIFs anteriores
		beneficiarios = new Vector<Beneficiario>();
		for(String nif : nifs) {
			beneficiarios.add(FPBeneficiario.consultarPorNIF(nif));
		}
		
		return beneficiarios;
	}
	
	// M�todo que devuelve el m�dico que se le debe asignar a un beneficiario
	// (es p�blico porque se utiliza desde otro gestor, no es accesible a los clientes)
	public static Medico obtenerMedicoBeneficiario(Beneficiario beneficiario) throws SQLException, CentroSaludInexistenteException, DireccionInexistenteException, UsuarioIncorrectoException {
		String nifMedico;
		Medico medico;

		// Por defecto, el beneficiario se quedar� con su m�dico
		medico = beneficiario.getMedicoAsignado();
		
		// Si el beneficiario tiene m�s de 14 a�os y ten�a asignado un
		// pediatra (o no tenia m�dico o pertenec�a a otro centro),
		// se le busca un nuevo m�dico de cabecera
		if(beneficiario.getEdad() >= EDAD_PEDIATRA) {
			if(beneficiario.getMedicoAsignado() == null
			 || !beneficiario.getCentroSalud().equals(beneficiario.getMedicoAsignado().getCentroSalud())
			 || beneficiario.getMedicoAsignado().getTipoMedico().getCategoria() == CategoriasMedico.Pediatra) {
				nifMedico = UtilidadesPersistencia.obtenerMedicoAleatorioTipoCentro(CategoriasMedico.Cabecera, beneficiario.getCentroSalud());
				if(nifMedico.equals("")) {
					medico = null;
				} else {
					medico = (Medico)FPUsuario.consultar(nifMedico);
				}
			}
		}
		
		// Si el beneficiario tiene menos de 14 a�os y ten�a asignado
		// un m�dico de cabecera (o no tenia m�dico), se le busca un pediatra 
		if(beneficiario.getEdad() < EDAD_PEDIATRA) {
			if(beneficiario.getMedicoAsignado() == null
			 || !beneficiario.getCentroSalud().equals(beneficiario.getMedicoAsignado().getCentroSalud())
			 || beneficiario.getMedicoAsignado().getTipoMedico().getCategoria() == CategoriasMedico.Cabecera) {
				nifMedico = UtilidadesPersistencia.obtenerMedicoAleatorioTipoCentro(CategoriasMedico.Pediatra, beneficiario.getCentroSalud());
				if(nifMedico.equals("")) {
					medico = null;
				} else {
					medico = (Medico)FPUsuario.consultar(nifMedico);
				}
			}
		}
		
		return medico;
	}
	
}
