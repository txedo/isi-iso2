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

	// Método para obtener los datos de un volante
	public static Volante consultarVolante(long idSesion, long idVolante) throws SQLException, SesionInvalidaException, OperacionIncorrectaException, VolanteNoValidoException, CitaNoValidaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		Volante volante;
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.ConsultarVolante);

		// Obtenemos el volante con el id indicado
		volante = FPVolante.consultar(idVolante);
		
		return volante;
	}
	
	// Método para emitir un volante para un beneficiario para un especialista
	public static Volante emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, SesionInvalidaException, OperacionIncorrectaException, VolanteNoValidoException, UsuarioIncorrectoException, CentroSaludInexistenteException, NullPointerException, DireccionInexistenteException {
		Calendar calend;
		Usuario usuario;
		Volante volante;
		
		// Comprobamos los parámetros pasados
		if(beneficiario == null) {
			throw new NullPointerException("El beneficiario de un volante no puede ser nulo.");
		}
		if(emisor == null) {
			throw new NullPointerException("El médico emisor de un volante no puede ser nulo.");
		}
		if(destino == null) {
			throw new NullPointerException("El médico receptor de un volante no puede ser nulo.");
		}
		
		// Comprobamos si se tienen permisos para realizar la operación
		GestorSesiones.comprobarPermiso(idSesion, Operaciones.EmitirVolante);
		
		// Comprobamos que exista el beneficiario
		beneficiario = FPBeneficiario.consultarPorNIF(beneficiario.getNif());
		if(beneficiario.getMedicoAsignado() == null) {
			throw new MedicoInexistenteException("El beneficiario al que se le va a emitir el volante no tiene asignado ningún médico.");
		}
		
		// Comprobamos que exista el médico emisor
		try {
			usuario = FPUsuario.consultar(emisor.getNif());
			if(usuario.getRol() != RolesUsuario.Médico) {
				throw new MedicoInexistenteException("El médico emisor no es un usuario del sistema con rol de médico.");
			}
			emisor = (Medico)usuario; 
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
		
		// Comprobamos que exista el médico receptor
		try {
			usuario = FPUsuario.consultar(destino.getNif());
			if(usuario.getRol() != RolesUsuario.Médico) {
				throw new MedicoInexistenteException("El médico receptor no es un usuario del sistema con rol de médico.");
			}
			destino = (Medico)usuario; 
		} catch(UsuarioIncorrectoException ex) {
			throw new MedicoInexistenteException(ex.getMessage());
		}
				
		// Comprobamos que el médico receptor sea un especialista
		if(destino.getTipoMedico().getCategoria() != CategoriasMedico.Especialista) {
			throw new VolanteNoValidoException("El médico receptor del volante no es un especialista.");
		}
		
		// Añadimos el volante y devolvemos su identificador
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
