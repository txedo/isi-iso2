package dominio.control;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import comunicaciones.GestorConexionesLog;
import comunicaciones.ICliente;
import comunicaciones.IServidorFrontend;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ICodigosOperacionesCliente;
import dominio.conocimiento.IMedico;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.ITiposMensajeLog;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.DireccionInexistenteException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.SustitucionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;
import excepciones.VolanteNoValidoException;

/**
 * Fachada del servidor frontend utilizada por el cliente que da acceso a
 * las clases gestoras que implementan la funcionalidad del sistema.
 */
public class ServidorFrontend implements IServidorFrontend {

	private static ServidorFrontend instancia;
	
	protected ServidorFrontend() {
	}
	
	public static ServidorFrontend getServidor() {
		if(instancia == null) {
			instancia = new ServidorFrontend();
		}
		return instancia;
	}
	
    // ------------------------------
    // Métodos de gestión de sesiones
    // ------------------------------
    
	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ISesion sesion;
		
		try {
			// Nos identificamos en el sistema con el login y la password
			sesion = GestorSesiones.identificar(login, password);
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Usuario '" + login + "' autenticado.");
		} catch(SQLException se) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error SQL mientras se autenticaba el usuario '" + login + "': " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al recuperar el usuario '" + login + "' que se estaba autenticando: " + uie.getLocalizedMessage());
			throw uie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se autenticaba un usuario: " + e.toString());
			throw e;
		}
		
		return sesion;
	}
	
	public void registrar(ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception {
		String login;
		
		try {
			// Registramos el cliente en el sistema
			GestorSesiones.registrar(idSesion, cliente);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_INFO, "Registrado el cliente con id de sesión " + idSesion + ".");
			GestorConexionesLog.actualizarClientesEscuchando(GestorSesiones.getClientes().size());
		} catch(SesionNoIniciadaException snie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "Error al comprobar la sesión con id " + idSesion + " para registrar un cliente en el sistema: " + snie.getLocalizedMessage());
			throw snie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "Error inesperado mientras se registraba un cliente en el sistema: " + e.toString());
			throw e;
		}
	}
	
	public void liberar(long idSesion) throws RemoteException, Exception {
		String login;
		
		try {
			// Liberamos la sesión del cliente
			if(GestorSesiones.getSesion(idSesion) != null) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			} else {
				login = "";
			}
			GestorSesiones.liberar(idSesion);
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "Liberado el cliente con id de sesión " + idSesion + ".");
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "Usuario '" + login + "' desconectado.");
			GestorConexionesLog.actualizarClientesEscuchando(GestorSesiones.getClientes().size());
		} catch(SesionNoIniciadaException snie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "Error al comprobar la sesión con id " + idSesion + " para liberar un cliente en el sistema: " + snie.getLocalizedMessage());
			throw snie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "Error inesperado mientras se liberaba un cliente en el sistema: " + e.toString());
			throw e;
		}
	}

	// -----------------------------------
	// Métodos de gestión de beneficiarios
	// -----------------------------------
	
	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		String login;
		
		try {
			// Obtenemos el beneficiario con el NIF indicado
			beneficiario = GestorBeneficiarios.consultarBeneficiarioPorNIF(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el beneficiario con NIF " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaba el beneficiario con NIF " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaba el beneficiario con NIF " + dni + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaba el beneficiario con NIF " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaba el beneficiario con NIF " + dni + ": " + die.getLocalizedMessage());
			throw die;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un beneficiario mientras se consultaba el beneficiario con NIF " + dni + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar un beneficiario con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta del beneficiario con NIF " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar el beneficiario con NIF " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaba un beneficiario por su NIF: " + e.toString());
			throw e;
		}
		
		return beneficiario;
	}
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		String login;
		
		try {
			// Obtenemos el beneficiario con el NSS indicado
			beneficiario = GestorBeneficiarios.consultarBeneficiarioPorNSS(idSesion, nss);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el beneficiario con NSS " + nss + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaba el beneficiario con NSS " + nss + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaba el beneficiario con NSS " + nss + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaba el beneficiario con NSS " + nss + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaba el beneficiario con NSS " + nss + ": " + die.getLocalizedMessage());
			throw die;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un beneficiario mientras se consultaba el beneficiario con NSS " + nss + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar un beneficiario con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta del beneficiario con NSS " + nss + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar el beneficiario con NSS " + nss + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaba un beneficiario por NSS: " + e.toString());
			throw e;
		}
		
		return beneficiario;
	}

	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		String login;
		
		try {
			// Añadimos un nuevo beneficiario al sistema
			GestorBeneficiarios.crearBeneficiario(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Creado el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL mientras se creaba el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un usuario mientras se creaba el beneficiario con NIF " + beneficiario.getNif() + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un centro de salud mientras se creaba el beneficiario con NIF " + beneficiario.getNif() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar una dirección mientras se creaba el beneficiario con NIF " + beneficiario.getNif() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(BeneficiarioYaExistenteException byee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al registrar el beneficiario que se estaba creando con NIF " + beneficiario.getNif() + ": " + byee.getLocalizedMessage());
			throw byee;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar crear un beneficiario con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar realizar una operación no permitida de creación del beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesión con id " + idSesion + " para crear el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado mientras se creaba un beneficiario: " + e.toString());
			throw e;
		}
	}

	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		String login;
		
		try {
			// Modificamos un beneficiario existente del sistema
			GestorBeneficiarios.modificarBeneficiario(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Modificado el beneficiario con NIF " + beneficiario.getNif() + ".");
			GestorSesiones.actualizarClientes(idSesion, ICodigosOperacionesCliente.MODIFICAR, beneficiario);
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error SQL mientras se modificaba el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar un usuario mientras se modificaba el beneficiario con NIF " + beneficiario.getNif() + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar un centro de salud mientras se modificaba el beneficiario con NIF " + beneficiario.getNif() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar una dirección mientras se modificaba el beneficiario con NIF " + beneficiario.getNif() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar un beneficiario mientras se modificaba el beneficiario con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al intentar modificar un beneficiario con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al intentar realizar una operación no permitida de modificación del beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error al comprobar la sesión con id " + idSesion + " para modificar el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error inesperado mientras se modificaba un beneficiario: " + e.toString());
			throw e;
		}
	}
	
	// -----------------------------
	// Métodos de gestión de médicos
	// -----------------------------
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		Medico medico;
		String login;
		
		try {
			// Obtenemos el médico con el DNI indicado
			medico = GestorMedicos.consultarMedico(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el médico con DNI " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaba el médico con DNI " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un médico mientras se consultaba el médico con DNI " + dni + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaba el médico con DNI " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaba el médico con DNI " + dni + ": " + die.getLocalizedMessage());
			throw die;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar un médico con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta del médico con DNI " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar el médico con DNI " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaba un médico: " + e.toString());
			throw e;
		}
		
		return medico;
	}
	
	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		String login;
		
		try {
			// Añadimos un nuevo médico al sistema
			GestorMedicos.crearMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Creado el médico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL mientras se creaba el médico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoYaExistenteException myee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al registrar el médico que se estaba creando con DNI " + medico.getDni() + ": " + myee.getLocalizedMessage());
			throw myee;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un centro de salud mientras se creaba el médico con DNI " + medico.getDni() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar una dirección mientras se creaba el médico con DNI " + medico.getDni() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar crear un médico con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar realizar una operación no permitida de creación del médico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesión con id " + idSesion + " para crear el médico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado mientras se creaba un médico: " + e.toString());
			throw e;
		}
	}
	
	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Modificamos un médico existente del sistema
			GestorMedicos.modificarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Modificado el médico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error SQL mientras se modificaba el médico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al consultar un médico mientras se modificaba el médico con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar un centro de salud mientras se modificaba el médico con DNI " + medico.getDni() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar una dirección mientras se modificaba el médico con DNI " + medico.getDni() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al intentar modificar un médico con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al intentar realizar una operación no permitida de modificación del médico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error al comprobar la sesión con id " + idSesion + " para modificar el médico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error inesperado mientras se modificaba un médico: " + e.toString());
			throw e;
		}
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Eliminamos un médico del sistema
			GestorMedicos.eliminarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Eliminado el médico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error SQL mientras se eliminaba el médico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un médico mientras se eliminaba el médico con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un centro de salud mientras se eliminaba el médico con DNI " + medico.getDni() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar una dirección mientras se eliminaba el médico con DNI " + medico.getDni() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al intentar eliminar un médico con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al intentar realizar una operación no permitida de eliminación del médico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error al comprobar la sesión con id " + idSesion + " para eliminar el médico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error inesperado mientras se eliminaba un médico: " + e.toString());
			throw e;
		}
	}
	
	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Asignamos un sustituto a un médico
			GestorMedicos.establecerSustituto(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Asignada una sustitución del médico con DNI " + sustituto.getDni() + " al médico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL mientras se asignaba una sustitución al médico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un médico mientras se asignaba una sustitución al médico con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un centro de salud mientras se asignaba una sustitución al médico con DNI " + medico.getDni() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar una dirección mientras se asignaba una sustitución al médico con DNI " + medico.getDni() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un usuario mientras se asignaba una sustitución al médico con DNI " + medico.getDni() + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al analizar la fecha de los días para los que se estaba asignando un sustituto al médico con DNI " + medico.getDni() + ": " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(SustitucionInvalidaException sie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al analizar los datos de la sustitución que se estaba asignando al médico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar asignar una sustitución a un médico con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar realizar una operación no permitida de asignación de una sustitución al médico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesión con id " + idSesion + " para asignar una sustitución al médico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado mientras se asignaba una sustitución a un médico: " + e.toString());
			throw e;
		}
	}

	// ---------------------------
	// Métodos de gestión de citas
	// ---------------------------
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception {
		Cita cita;
		String login;
		
		try {
			// Pedimos una cita para un beneficiario y un médico dados
			cita = GestorCitas.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Tramitada una cita para el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un beneficiario mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un médico mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un usuario mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un centro de salud mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar una dirección mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(CitaNoValidaException cnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al analizar los datos de la cita que se estaba tramitando para el beneficiario con NIF " + beneficiario.getNif() + ": " + cnve.getLocalizedMessage());
			throw cnve;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al analizar la fecha de la cita que se estaba tramitando para el beneficiario con NIF " + beneficiario.getNif() + ": " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar tramitar una cita con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar realizar una operación no permitida de tramitación de una cita para el beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesión con id " + idSesion + " para tramitar una cita para el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado mientras se tramitaba una cita: " + e.toString());
			throw e;
		}
		
		return cita;
	}
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, VolanteNoValidoException, SQLException, Exception {
		Cita cita;
		String login;
		
		try {
			// Pedimos una cita para un beneficiario a partir de un volante
			cita = GestorCitas.pedirCita(idSesion, beneficiario, idVolante, fechaYHora, duracion);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Tramitada una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un beneficiario mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un usuario mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un centro de salud mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar una dirección mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + die.getLocalizedMessage());
			throw die;
		} catch(VolanteNoValidoException vnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un volante o analizar sus datos mientras se tramitaba una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + vnve.getLocalizedMessage());
			throw vnve;
		} catch(CitaNoValidaException cnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al analizar los datos de la cita que se estaba tramitando para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + cnve.getLocalizedMessage());
			throw cnve;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al analizar la fecha de la cita que se estaba tramitando para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar tramitar una cita a partir de un volante con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar realizar una operación no permitida de tramitación de una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesión con id " + idSesion + " para tramitar una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado mientras se tramitaba una cita a partir de un volante: " + e.toString());
			throw e;
		}
		
		return cita;
	}
	
	public Vector<Cita> getCitas(long idSesion, String dni) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		Vector<Cita> citas;
		String login;
		
		try {
			// Obtenemos las citas que tiene un beneficiario
			citas = GestorCitas.consultarCitas(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultadas las citas del beneficiario con NIF " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaban las citas del beneficiario con NIF " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un beneficiario mientras se consultaban las citas del beneficiario con NIF " + dni + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaban las citas del beneficiario con NIF " + dni + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaban las citas del beneficiario con NIF " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaban las citas del beneficiario con NIF " + dni + ": " + die.getLocalizedMessage());
			throw die;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar las citas de un beneficiario con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de las citas del beneficiario con NIF " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar las citas del beneficiario con NIF " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaban las citas de un beneficiario: " + e.toString());
			throw e;
		}
		
		return citas;
	}
	
	public void anularCita(long idSesion, Cita cita) throws RemoteException, CitaNoValidaException, SQLException, Exception {
		String login;
		
		try {
			// Eliminamos una cita existente
			GestorCitas.anularCita(idSesion, cita);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Anulada una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error SQL mientras se anulaba una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(CitaNoValidaException cnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar una cita mientras se anulaba una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + cnve.getLocalizedMessage());
			throw cnve;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un beneficiario mientras se anulaba una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un usuario mientras se anulaba una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un centro de salud mientras se anulaba una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar una dirección mientras se anulaba una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al intentar anular una cita con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al intentar realizar una operación no permitida de anulación de una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error al comprobar la sesión con id " + idSesion + " para anular una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error inesperado mientras se anulaba una cita de un beneficiario: " + e.toString());
			throw e;
		}
	}
	
	// ------------------------------
	// Métodos de gestión de volantes
	// ------------------------------
	
	public long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception {
		long idVolante;
		String login;
		
		try {
			// Solicitamos un volante para un cierto beneficiario y médico
			idVolante = GestorVolantes.emitirVolante(idSesion, beneficiario, emisor, destino);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Emitido un volante para el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL mientras se emitía un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un beneficiario mientras se emitía un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un médico mientras se emitía un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(VolanteNoValidoException vnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al analizar los datos del volante que se estaba emitiendo para el beneficiario con NIF " + beneficiario.getNif() + ": " + vnve.getLocalizedMessage());
			throw vnve;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un usuario mientras se emitía un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un centro de salud mientras se emitía un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(DireccionInexistenteException die) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar una dirección mientras se emitía un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + die.getLocalizedMessage());
			throw die;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar emitir un volante con datos no válidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar realizar una operación no permitida de emisión de un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesión con id " + idSesion + " para emitir un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado mientras se emitía un volante: " + e.toString());
			throw e;
		}
		
		return idVolante;
	}
		
	// ---------------
	// Método auxiliar
	// ---------------
	
	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, Exception {
		Object resultado;
		String login;
		
		resultado = null;
		switch((int)codigoMensaje) {
	
		// Métodos auxiliares de gestión de sesiones
		
		case ICodigosMensajeAuxiliar.OPERACIONES_DISPONIBLES:
			try {
				resultado = GestorSesiones.operacionesDisponibles(idSesion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultadas las operaciones disponibles.");
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar las operaciones disponibles: " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar las operaciones disponibles: " + e.toString());
				throw e;
			}
			break;
			
		// Métodos auxiliares de gestión de beneficiarios
			
		case ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO:
			try {
				// Eliminamos un beneficiario del sistema
				GestorBeneficiarios.eliminarBeneficiario(idSesion, (Beneficiario)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Eliminado el beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ".");
				GestorSesiones.actualizarClientes(idSesion, ICodigosOperacionesCliente.ELIMINAR, (Beneficiario)informacion);
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error SQL mientras se eliminaba el beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un beneficiario mientras se eliminaba el beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + bie.getLocalizedMessage());
				throw bie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un usuario mientras se eliminaba el beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un centro de salud mientras se eliminaba el beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar una dirección mientras se eliminaba el beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al intentar eliminar un beneficiario con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al intentar realizar una operación no permitida de eliminación del beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error al comprobar la sesión con id " + idSesion + " para eliminar el beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error inesperado mientras se eliminaba un beneficiario: " + e.toString());
				throw e;
			}
			break;

		case ICodigosMensajeAuxiliar.ASIGNAR_MEDICO_BENEFICIARIO:
			try {
				resultado = GestorBeneficiarios.comprobarMedicoBeneficiario((Beneficiario)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Asignado un nuevo médico al beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error SQL al asignar un nuevo médico al beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar un usuario al asignar un nuevo médico al beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar un centro de salud al asignar un nuevo médico al beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar una dirección al asignar un nuevo médico al beneficiario con NIF " + ((Beneficiario)informacion).getNif() + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al intentar asignar un nuevo médico a un beneficiario con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error inesperado mientras se asignaba un nuevo médico a un beneficiario: " + e.toString());
				throw e;
			}
			break;

		// Métodos auxiliares de gestión de usuarios
						
		case ICodigosMensajeAuxiliar.CONSULTAR_USUARIO:
			try {
				// Obtenemos el usuario con el DNI indicado
				resultado = GestorUsuarios.consultarUsuario(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el usuario con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaba el usuario con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioInexistenteException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaba el usuario con DNI " + (String)informacion + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaba el usuario con DNI " + (String)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaba el usuario con DNI " + (String)informacion + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar un usuario con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta del usuario con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar el usuario con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaba un usuario: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CREAR_USUARIO:
			try {
				// Añadimos un nuevo usuario al sistema
				GestorUsuarios.crearUsuario(idSesion, (Usuario)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Creado el usuario con DNI " + ((Usuario)informacion).getDni() + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL mientras se creaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioYaExistenteException uyee) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al registrar el usuario que se estaba creando con DNI " + ((Usuario)informacion).getDni() + ": " + uyee.getLocalizedMessage());
				throw uyee;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar un centro de salud mientras se creaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar una dirección mientras se creaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al intentar crear un usuario con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al realizar una operación no permitida de creación del usuario con DNI " + ((Usuario)informacion).getDni() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesión con id " + idSesion + " para crear el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado mientras se creaba un usuario: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.MODIFICAR_USUARIO:
			try {
				// Modificamos un usuario existente del sistema
				GestorUsuarios.modificarUsuario(idSesion, (Usuario)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Modificado el usuario con DNI " + ((Usuario)informacion).getDni() + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error SQL mientras se modificaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioInexistenteException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar un usuario mientras se modificaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar un centro de salud mientras se modificaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar una dirección mientras se modificaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al intentar modificar un usuario con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al intentar realizar una operación no permitida de modificación del usuario con DNI " + ((Usuario)informacion).getDni() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error al comprobar la sesión con id " + idSesion + " para modificar el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error inesperado mientras se modificaba un usuario: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.ELIMINAR_USUARIO:
			try {
				// Eliminamos un usuario del sistema
				GestorUsuarios.eliminarUsuario(idSesion, (Usuario)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Eliminado el usuario con DNI " + ((Usuario)informacion).getDni() + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error SQL mientras se eliminaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioInexistenteException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un usuario mientras se eliminaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar un centro de salud mientras se eliminaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar una dirección mientras se eliminaba el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al intentar eliminar un usuario con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al intentar realizar una operación no permitida de eliminación del usuario con DNI " + ((Usuario)informacion).getDni() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error al comprobar la sesión con id " + idSesion + " para eliminar el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error inesperado mientras se eliminaba un usuario: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CONSULTAR_CENTROS:
			try {
				// Consultamos la lista de centros de salud
				resultado = GestorUsuarios.consultarCentros(idSesion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultada la lista de centros de salud.");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaba la lista de centros de salud: " + se.getLocalizedMessage());
				throw se;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de la lista de centros de salud: " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar la lista de centros de salud: " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar la lista de centros de salud: " + e.toString());
				throw e;
			}
			break;

		// Métodos auxiliares de gestión de médicos
			
		case ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO:
			try {
				// Toma un numero variable de argumentos porque el tipo "Especialista", va acompañado de su especialidad
				Object [] parametros = (Object[])informacion;
				resultado = GestorMedicos.consultarMedicosPorTipo(idSesion, parametros);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultados los médicos de tipo " + ((CategoriasMedico)parametros[0]).toString() + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaban los médicos de tipo " + ((CategoriasMedico)((Object[])informacion)[0]).toString() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaban los médicos de tipo " + ((CategoriasMedico)((Object[])informacion)[0]).toString() + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaban los médicos de tipo " + ((CategoriasMedico)((Object[])informacion)[0]).toString() + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaban los médicos de tipo " + ((CategoriasMedico)((Object[])informacion)[0]).toString() + ": " + die.getLocalizedMessage());
				throw die;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de los médicos de tipo " + ((CategoriasMedico)((Object[])informacion)[0]).toString() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar los médicos de tipo " + ((CategoriasMedico)((Object[])informacion)[0]).toString() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaban los médicos de un cierto tipo: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO:
			try {
				// Consultamos a qué horas puede trabajar el médico con el DNI indicado
				resultado = GestorMedicos.consultarHorarioMedico(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el horario del médico con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaba el horario del médico con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(MedicoInexistenteException mie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un médico mientras se consultaba el horario del médico con DNI " + (String)informacion + ": " + mie.getLocalizedMessage());
				throw mie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaba el horario del médico con DNI " + (String)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaba el horario del médico con DNI " + (String)informacion + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar el horario de un médico con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta del horario del médico con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar el horario del médico con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaba el horario de un médico: " + e.toString());
				throw e;
			}
			break;
			
		case ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO:
			try {
				resultado = GestorBeneficiarios.getBeneficiariosMedico(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultados los beneficiarios asignados al médico con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaban los beneficiarios asignados al médico con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del médico con DNI " + (String)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección de un beneficiario asignado al médico con DNI " + (String)informacion + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar los beneficiarios asignados al un médico con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de los beneficiarios asignados al médico con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar los beneficiarios asignados al médico con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaban los beneficiarios asignados a un médico: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS:
			try {
				// Obtenemos los médicos posibles para una sustitución
				resultado = GestorMedicos.obtenerPosiblesSustitutos(idSesion, (String)((Object[])informacion)[0], (Date)((Object[])informacion)[1], (Integer)((Object[])informacion)[2], (Integer)((Object[])informacion)[3]);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultados los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaban los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ": " + se.getLocalizedMessage());
				throw se;
			} catch(MedicoInexistenteException mie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un médico mientras se consultaban los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ": " + mie.getLocalizedMessage());
				throw mie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaban los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaban los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaban los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ": " + die.getLocalizedMessage());
				throw die;
			} catch(FechaNoValidaException fnve) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al analizar la fecha del día para el que se estaban consultando los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ": " + fnve.getLocalizedMessage());
				throw fnve;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar los médicos que pueden sustituir a otro médico con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar los médicos que pueden sustituir al médico con DNI " + (String)((Object[])informacion)[0] + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaban los médicos que pueden sustituir a otro médico: " + e.toString());
				throw e;
			}
			break;
			
		// Métodos auxiliares de gestión de citas
		
		case ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO:
			try {
				// Consultamos las citas del médico con el DNI indicado
				resultado = GestorCitas.consultarHorasCitasMedico(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultadas las fechas de las citas del médico con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaban las fechas de las citas del médico con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un beneficiario mientras se consultaban las las fechas de las citas del médico con DNI " + (String)informacion + ": " + bie.getLocalizedMessage());
				throw bie;
			} catch(MedicoInexistenteException mie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un médico mientras se consultaban las las fechas de las citas del médico con DNI " + (String)informacion + ": " + mie.getLocalizedMessage());
				throw mie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaban las las fechas de las citas del médico con DNI " + (String)informacion + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaban las las fechas de las citas del médico con DNI " + (String)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaban las las fechas de las citas del médico con DNI " + (String)informacion + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar las las fechas de las citas de un médico con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de las las fechas de las citas del médico con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar las las fechas de las citas del médico con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaban las las fechas de las citas de un médico: " + e.toString());
				throw e;
			}
			break;
			
		case ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO:
			try {
				// Consultamos las citas del médico con el DNI indicado
				resultado = GestorCitas.consultarCitasMedico(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultadas las citas del médico con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaban las citas del médico con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un beneficiario mientras se consultaban las citas del médico con DNI " + (String)informacion + ": " + bie.getLocalizedMessage());
				throw bie;
			} catch(MedicoInexistenteException mie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un médico mientras se consultaban las citas del médico con DNI " + (String)informacion + ": " + mie.getLocalizedMessage());
				throw mie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaban las citas del médico con DNI " + (String)informacion + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaban las citas del médico con DNI " + (String)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaban las citas del médico con DNI " + (String)informacion + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar las citas de un médico con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de las citas del médico con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar las citas del médico con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaban las citas de un médico: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS:
			try {
				// Consultamos los días completos del médico con el DNI indicado
				resultado = GestorCitas.consultarDiasCompletos(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultados los días completos del médico con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaban los días completos del médico con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un beneficiario mientras se consultaban los días completos del médico con DNI " + (String)informacion + ": " + bie.getLocalizedMessage());
				throw bie;
			} catch(MedicoInexistenteException mie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un médico mientras se consultaban los días completos del médico con DNI " + (String)informacion + ": " + mie.getLocalizedMessage());
				throw mie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaban los días completos del médico con DNI " + (String)informacion + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaban los días completos del médico con DNI " + (String)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaban los días completos del médico con DNI " + (String)informacion + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar los días completos de un médico con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de los días completos del médico con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar los días completos del médico con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaban los diás completos de un médico: " + e.toString());
				throw e;
			}
			break;
			
		case ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES:
			try {
				// Obtenemos las citas pendientes que tiene un beneficiario
				resultado = GestorCitas.consultarCitasPendientes(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultadas las citas pendientes del beneficiario con NIF " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaban las citas pendientes del beneficiario con NIF " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un beneficiario mientras se consultaban las citas pendientes del beneficiario con NIF " + (String)informacion + ": " + bie.getLocalizedMessage());
				throw bie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaban las citas pendientes del beneficiario con NIF " + (String)informacion + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaban las citas pendientes del beneficiario con NIF " + (String)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaban las citas pendientes del beneficiario con NIF " + (String)informacion + ": " + die.getLocalizedMessage());
				throw die;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar consultar las citas pendientes de un beneficiario con datos no válidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta de las citas pendientes del beneficiario con NIF " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar las citas pendientes del beneficiario con NIF " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado mientras se consultaban las citas pendientes de un beneficiario: " + e.toString());
				throw e;
			}
			break;
		
		// Métodos auxiliares de gestión de volantes
			
		case ICodigosMensajeAuxiliar.CONSULTAR_VOLANTE:
			try {
				// Consultamos un volante por id
				resultado = GestorVolantes.consultarVolante(idSesion, (Long)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el volante con id " + (Long)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL mientras se consultaba el volante con id " + (Long)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(VolanteNoValidoException vnve) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un volante mientras se consultaba el volante con id " + (Long)informacion + ": " + vnve.getLocalizedMessage());
				throw vnve;
			} catch(CitaNoValidaException cnve) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una cita mientras se consultaba el volante con id " + (Long)informacion + ": " + cnve.getLocalizedMessage());
				throw cnve;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un beneficiario mientras se consultaba el volante con id " + (Long)informacion + ": " + bie.getLocalizedMessage());
				throw bie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un usuario mientras se consultaba el volante con id " + (Long)informacion + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar un centro de salud mientras se consultaba el volante con id " + (Long)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(DireccionInexistenteException die) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar una dirección mientras se consultaba el volante con id " + (Long)informacion + ": " + die.getLocalizedMessage());
				throw die;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al intentar realizar una operación no permitida de consulta del volante con id " + (Long)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesión con id " + idSesion + " para consultar el volante con id " + (Long)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar un volante: " + e.toString());
				throw e;
			}
			break;
		
		}

		return resultado;
	}

}
