package dominio;

import java.sql.SQLException;
import excepciones.BeneficiarioIncorrectoException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de consultar, añadir, modificar y eliminar beneficiarios
 * en el sistema.
 */
public class GestorBeneficiarios {

	public Beneficiario getBeneficiario(long idSesion, String dni) throws SQLException, BeneficiarioIncorrectoException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		Beneficiario bene = null;
		
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ConsultarBeneficiario);
		bene = Beneficiario.consultarPorNIF(dni);
		
		return bene;
	}
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws SQLException, BeneficiarioIncorrectoException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		Beneficiario bene = null;
		
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ConsultarBeneficiario);
		bene = Beneficiario.consultarPorNIF(nss);

		return bene;
	}
	
	public void crear(long idSesion, Beneficiario beneficiario) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		Medico medico = null;
		
		GestorSesiones.comprobarPermiso(idSesion, Operacion.RegistrarBeneficiario);
		// Si es un nuevo beneficiario, se le asigna un medico aleatorio
		// Si ya existe, hay un fallo 
		try {
			Beneficiario.consultarPorNIF(beneficiario.getNif());
			throw new SQLException("El beneficiario con NIF "+beneficiario.getNif()+ " ya existe en la base de datos. No se puede registrar de nuevo.");
		}
		catch(BeneficiarioIncorrectoException e){
			// Le asignamos un medico
			medico = (Medico) Usuario.consultarAleatorio(Roles.Medico);
			beneficiario.setMedicoAsignado(medico);
			beneficiario.insertar();
		}
	}
	
	public void modificar(long idSesion, Beneficiario beneficiario) throws SQLException, SesionInvalidaException, OperacionIncorrectaException, Exception {
		GestorSesiones.comprobarPermiso(idSesion, Operacion.ModificarBeneficiario);
		beneficiario.modificar();
	}
	
}
