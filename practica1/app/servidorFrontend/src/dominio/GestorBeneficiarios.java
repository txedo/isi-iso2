package dominio;

import java.sql.SQLException;

import excepciones.BeneficiarioIncorrectoException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.UsuarioIncorrectoException;

public class GestorBeneficiarios {

	public Beneficiario getBeneficiario(long idSesion, String dni) throws OperacionIncorrectaException, SQLException, BeneficiarioIncorrectoException, UsuarioIncorrectoException, CentroSaludIncorrectoException{
		Beneficiario bene = null;
		if (GestorSesiones.comprobar(idSesion, Operacion.ConsultarBeneficiario))
			bene = Beneficiario.consultarPorNIF(dni);
		else
			throw new OperacionIncorrectaException("El rol " + Roles.values()[(int) GestorSesiones.getSesion(idSesion).getRol()] + "no puede consultar los datos de un beneficiario");
		return bene;
	}
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws OperacionIncorrectaException, SQLException, BeneficiarioIncorrectoException, UsuarioIncorrectoException, CentroSaludIncorrectoException{
		Beneficiario bene = null;
		if (GestorSesiones.comprobar(idSesion, Operacion.ConsultarBeneficiario))
			bene = Beneficiario.consultarPorNIF(nss);
		else
			throw new OperacionIncorrectaException("El rol " + Roles.values()[(int) GestorSesiones.getSesion(idSesion).getRol()] + "no puede consultar los datos de un beneficiario");
		return bene;
	}
	
	public void crear(long idSesion, Beneficiario beneficiario) throws OperacionIncorrectaException, SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException{
		Medico medico = null;
		if (GestorSesiones.comprobar(idSesion, Operacion.RegistrarBeneficiario))
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
			
		else
			throw new OperacionIncorrectaException("El rol " + Roles.values()[(int) GestorSesiones.getSesion(idSesion).getRol()] + "no puede registrar a un beneficiario");
	}
	
	public void modificar(long idSesion, Beneficiario beneficiario) throws OperacionIncorrectaException, SQLException{
		if (GestorSesiones.comprobar(idSesion, Operacion.ModificarBeneficiario))
			beneficiario.modificar();
		else
			throw new OperacionIncorrectaException("El rol " + Roles.values()[(int) GestorSesiones.getSesion(idSesion).getRol()] + "no puede modificar los datos de un beneficiario");
	}
	
	
}
