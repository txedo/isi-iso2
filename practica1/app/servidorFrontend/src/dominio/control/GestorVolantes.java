package dominio.control;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Calendar;

import persistencia.FPBeneficiario;
import persistencia.FPUsuario;
import persistencia.FPVolante;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.DireccionInexistenteException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Clase encargada de solicitar y recuperar volantes para que los
 * beneficiarios puedan pedir cita con los especialistas.
 */
public class GestorVolantes {

	// M�todo para obtener los datos de un volante
	public static Volante consultarVolante(long idSesion, long idVolante) throws SQLException, SesionInvalidaException, OperacionIncorrectaException, VolanteNoValidoException, CitaNoValidaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		Volante volante;
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarVolante);

		// Obtenemos el volante con el id indicado
		volante = FPVolante.consultar(idVolante);
		
		return volante;
	}
	
	// M�todo para emitir un volante para un beneficiario para un especialista
	public static Volante emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, SesionInvalidaException, OperacionIncorrectaException, VolanteNoValidoException, UsuarioIncorrectoException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Calendar calend;
		Usuario usuario;
		Volante volante;
		
		// Comprobamos los par�metros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario de un volante no puede ser nulo.");
		}
		if(emisor == null) {
			throw new NullPointerException("El m�dico emisor de un volante no puede ser nulo.");
		}
		if(destino == null) {
			throw new NullPointerException("El m�dico receptor de un volante no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EmitirVolante);
		
		// Comprobamos que exista el beneficiario
		beneficiario = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		if(beneficiario.getMedicoAsignado() == null) {
			throw new MedicoInexistenteException("El beneficiario al que se le va a emitir el volante no tiene asignado ning�n m�dico.");
		}
		
		// Comprobamos que exista el m�dico emisor
		try {
			usuario = FPUsuario.consultar(emisor.getNif());
			if(usuario.getRol() != RolesUsuario.M�dico) {
				throw new MedicoInexistenteException("El m�dico emisor no es un usuario del sistema con rol de m�dico.");
			}
			emisor = (Medico)usuario; 
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que exista el m�dico receptor
		try {
			usuario = FPUsuario.consultar(destino.getNif());
			if(usuario.getRol() != RolesUsuario.M�dico) {
				throw new MedicoInexistenteException("El m�dico receptor no es un usuario del sistema con rol de m�dico.");
			}
			destino = (Medico)usuario; 
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
				
		// Comprobamos que el m�dico receptor sea un especialista
		if(destino.getTipoMedico().getCategoria() != CategoriasMedico.Especialista) {
			throw new VolanteNoValidoException("El m�dico receptor del volante no es un especialista.");
		}
		
		// A�adimos el volante y devolvemos su identificador
		volante = new Volante();
		volante.setBeneficiario(beneficiario);
		volante.setEmisor(emisor);
		volante.setReceptor(destino);
		volante.setCita(null);
		calend = Calendar.getInstance();
		calend.add(Calendar.DAY_OF_MONTH, IConstantes.DIAS_CADUCIDAD_VOLANTE);
		volante.setFechaCaducidad(calend.getTime());
		FPVolante.insertar(volante);
		
		return volante;
	}
	
}
