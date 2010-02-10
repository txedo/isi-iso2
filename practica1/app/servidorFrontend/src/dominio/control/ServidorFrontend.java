 package dominio.control;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import comunicaciones.GestorConexionesLog;
import comunicaciones.ICliente;
import comunicaciones.IServidorFrontend;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.ITiposMensajeLog;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.CentroSaludInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;
import excepciones.VolanteNoValidoException;

/**
 * Fachada del servidor frontend utilizada por el cliente que da acceso a
 * las clases gestoras que implementan la funcionalidad del sistema.
 */
public class ServidorFrontend implements IServidorFrontend {
	
	private Hashtable<Long, ICliente> clientesEscuchando = new Hashtable<Long, ICliente>();
	
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
    // M�todos del Gestor de Sesiones
    // ------------------------------
    
	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ISesion sesion;
		
		try {
			// Nos identificamos en el sistema con el login y la password
			sesion = GestorSesiones.identificar(login, password);
			/* 
			 * Si se ha cerrado la sesi�n y se ha vuelto a abrir, se elimina 
			 * el antiguo cliente que estaba a la escucha, para poder registrar al nuevo
			*/
			if (sesion.isModificada()) {
				//ICliente cliente = clientesEscuchando.get(GestorSesiones.getSesionAbierta().getId());
				//cliente.desactivar();
				clientesEscuchando.remove(GestorSesiones.getSesionAbierta().getId());			
				String mensajeLog = "Usuario '" + GestorSesiones.getSesion(sesion.getId()).getUsuario().getLogin() + "' desconectado.";
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, mensajeLog);
				GestorConexionesLog.actualizarClientesEscuchando(clientesEscuchando.size());
			}
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Usuario '" + login + "' autenticado.");
		} catch(RemoteException re) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error de conexi�n al autenticar el usuario '" + login + "': " + re.getLocalizedMessage());
			throw re;
		} catch(SQLException se) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error SQL al autenticar el usuario '" + login + "': " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error en el nombre de usuario o la contrase�a al autenticar el usuario '" + login + "': " + uie.getLocalizedMessage());
			throw uie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al autenticar un usuario: " + e.toString());
			throw e;
		}
		
		return sesion;
	}
	
	public void registrar(ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception {
		try {
			if (GestorSesiones.getSesion(idSesion)==null)
				throw new SesionNoIniciadaException("No se puede registrar en el servidor el cliente con la sesion "+idSesion+" porque no existe esa sesion");
			else {
				String url;
				url = "rmi://" + cliente.getDireccionIP() + ":" + String.valueOf(cliente.getPuerto()) + "/" + ICliente.NOMBRE_CLIENTE;
		        ICliente terminal = (ICliente)Naming.lookup(url);
		        clientesEscuchando.put(idSesion, terminal);
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "Nuevo cliente a la escucha. ID de sesion " + idSesion);
				GestorConexionesLog.actualizarClientesEscuchando(clientesEscuchando.size());
			}
		} catch (SesionNoIniciadaException snie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, snie.getMessage());
			throw snie;
		} catch (Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, e.getMessage());
			throw e;
		}
	}
	
	public void liberar(long idSesion) throws RemoteException, Exception {
		try {
			ISesion sesion = GestorSesiones.getSesion(idSesion);
			if (sesion == null) {
				throw new Exception("No se puede liberar en el servidor el cliente con la sesion "+idSesion+" porque no existe esa sesion");
			}
			else {
//TODO				ICliente cliente = clientesEscuchando.get(idSesion);
//TODO				cliente.desactivar();
				clientesEscuchando.remove(idSesion);
				String mensajeLog = "Usuario '" + GestorSesiones.getSesion(idSesion).getUsuario().getLogin() + "' desconectado.";
				GestorSesiones.liberar(idSesion);
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, mensajeLog);
				GestorConexionesLog.actualizarClientesEscuchando(clientesEscuchando.size());
			}
		} catch (Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_INFO, e.getMessage());
			throw e;
		}
	}

	// -----------------------------------
	// M�todos del Gestor de Beneficiarios
	// -----------------------------------
	
	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		String login;
		
		try {
			// Obtenemos el beneficiario con el NIF indicado
			beneficiario = GestorBeneficiarios.consultarBeneficiario(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el beneficiario con NIF " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar el beneficiario con NIF " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el m�dico asociado al beneficiario con NIF " + dni + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un beneficiario inexistente con NIF " + dni + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un beneficiario con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta del beneficiario con NIF " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar el beneficiario con NIF " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar un beneficiario por NIF: " + e.toString());
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
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar el beneficiario con NSS " + nss + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el m�dico asociado al beneficiario con NSS " + nss + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NSS " + nss + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un beneficiario inexistente con NSS " + nss + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un beneficiario con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta del beneficiario con NSS " + nss + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar el beneficiario con NSS " + nss + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar un beneficiario por NSS: " + e.toString());
			throw e;
		}
		
		return beneficiario;
	}

	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		String login;
		
		try {
			// A�adimos un nuevo beneficiario al sistema
			GestorBeneficiarios.crearBeneficiario(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Creado el beneficiario con NIF " + beneficiario.getNif() + " y NSS " + beneficiario.getNss() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL al crear el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a crear: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a crear: " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioYaExistenteException byee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al crear un beneficiario con NIF " + beneficiario.getNif() + " que ya existe: " + byee.getLocalizedMessage());
			throw byee;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al crear un beneficiario con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al realizar una operaci�n no permitida de creaci�n de un beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesi�n con id " + idSesion + " para crear un beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado al crear un beneficiario: " + e.toString());
			throw e;
		}
	}

	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		String login;
		
		try {
			// Modificamos un beneficiario existente del sistema
			GestorBeneficiarios.modificarBeneficiario(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Modificado el beneficiario con NIF " + beneficiario.getNif() + " y NSS " + beneficiario.getNss() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error SQL al modificar el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a modificar: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a modificar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al modificar un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al modificar un beneficiario con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al realizar una operaci�n no permitida de modificaci�n del beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error al comprobar la sesi�n con id " + idSesion + " para modificar el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error inesperado al modificar un beneficiario: " + e.toString());
			throw e;
		}
	}
	
	// -----------------------------
	// M�todos del Gestor de M�dicos
	// -----------------------------
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		Medico medico;
		String login;
		
		try {
			// Obtenemos el m�dico con el DNI indicado
			medico = GestorMedicos.consultarMedico(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el m�dico con DNI " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar el m�dico con DNI " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un m�dico inexistente con DNI " + dni + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del m�dico con DNI " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un m�dico con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta del m�dico con DNI " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar el m�dico con DNI " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar un m�dico: " + e.toString());
			throw e;
		}
		
		return medico;
	}
	
	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		String login;
		
		try {
			// A�adimos un nuevo m�dico al sistema
			GestorMedicos.crearMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Creado el m�dico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL al crear el m�dico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoYaExistenteException myee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al crear un m�dico con DNI " + medico.getDni() + " que ya existe: " + myee.getLocalizedMessage());
			throw myee;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el centro de salud del m�dico con DNI " + medico.getDni() + " que se iba a crear: " + csie.getLocalizedMessage());
			throw csie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al crear un m�dico con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al realizar una operaci�n no permitida de creaci�n de un m�dico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesi�n con id " + idSesion + " para crear el m�dico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado al crear un m�dico: " + e.toString());
			throw e;
		}
	}
	
	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Modificamos un m�dico existente del sistema
			GestorMedicos.modificarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Modificado el m�dico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error SQL al modificar el m�dico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al modificar un m�dico inexistente con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar el centro de salud del m�dico con DNI " + medico.getDni() + " que se iba a modificar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al modificar un m�dico con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al realizar una operaci�n no permitida de modificaci�n del m�dico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error al comprobar la sesi�n con id " + idSesion + " para modificar el m�dico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error inesperado al modificar un m�dico: " + e.toString());
			throw e;
		}
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Eliminamos un m�dico del sistema
			GestorMedicos.eliminarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Eliminado el m�dico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error SQL al eliminar el m�dico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al eliminar un m�dico inexistente con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar el centro de salud del m�dico con DNI " + medico.getDni() + " que se iba a eliminar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al eliminar un m�dico con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al realizar una operaci�n no permitida de eliminaci�n del m�dico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error al comprobar la sesi�n con id " + idSesion + " para eliminar el m�dico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error inesperado al eliminar un m�dico: " + e.toString());
			throw e;
		}
	}
	
	// ---------------------------
	// M�todos del Gestor de Citas
	// ---------------------------
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception {
		Cita cita;
		String login;
		
		try {
			// Pedimos una cita para un beneficiario y un m�dico dados
			cita = GestorCitas.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Solicitada una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL al pedir una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita para un m�dico inexistente con DNI " + idMedico + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + csie.getLocalizedMessage());
			throw csie;
		} catch(CitaNoValidaException cnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + cnve.getLocalizedMessage());
			throw cnve;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + " a una fecha y hora no v�lidas: " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al realizar una operaci�n no permitida de petici�n de una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesi�n con id " + idSesion + " para pedir una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado al solicitar una cita: " + e.toString());
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
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Solicitada una cita para el el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + csie.getLocalizedMessage());
			throw csie;
		} catch(VolanteNoValidoException vnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir de un volante inexistente o inv�lido con id " + idVolante + ": " + vnve.getLocalizedMessage());
			throw vnve;
		} catch(CitaNoValidaException cnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir de un volante ya utilizado con id " + idVolante + ": " + cnve.getLocalizedMessage());
			throw cnve;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + " a una fecha y hora no v�lidas: " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al pedir una cita a partir de un volante con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al realizar una operaci�n no permitida de petici�n de una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesi�n con id " + idSesion + " para pedir una cita para y el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado al solicitar una cita a partir de un volante: " + e.toString());
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
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar las citas del beneficiario con NIF " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar las citas de un beneficiario inexistente con NIF " + dni + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el m�dico asociado al beneficiario con NIF " + dni + " para el que se iban a consultar las citas: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + dni + " para el que se iban a consultar las citas: " + csie.getLocalizedMessage());
			throw csie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar las citas de un beneficiario con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta de las citas del beneficiario con NIF " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar las citas del beneficiario con NIF " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar las citas de un beneficiario: " + e.toString());
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
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error SQL al anular una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(CitaNoValidaException cnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al anular una cita inexistente del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + cnve.getLocalizedMessage());
			throw cnve;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al anular una cita con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al realizar una operaci�n no permitida de anulaci�n de una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error al comprobar la sesi�n con id " + idSesion + " para anular una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error inesperado al anular una cita de un beneficiario: " + e.toString());
			throw e;
		}
	}
	
	public long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception {
		long idVolante;
		String login;
		
		try {
			// Solicitamos un volante para un cierto beneficiario y m�dico
			idVolante = GestorCitas.emitirVolante(idSesion, beneficiario, emisor, destino);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Emitido un volante para el m�dico con DNI " + destino.getDni() + " y el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL al emitir un volante para el m�dico con DNI " + destino.getDni() + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al solicitar un volante para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al solicitar un volante para un m�dico inexistente con DNI " + emisor.getDni() + " o " + destino.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(VolanteNoValidoException vnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al solicitar un volante para un m�dico no especialista con DNI " + destino.getDni() + ": " + vnve.getLocalizedMessage());
			throw vnve;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a emitir un volante: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludInexistenteException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a emitir un volante: " + csie.getLocalizedMessage());
			throw csie;
		} catch(NullPointerException npe) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al solicitar un volante con datos no v�lidos: " + npe.getLocalizedMessage());
			throw npe;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al realizar una operaci�n no permitida de emisi�n de un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesi�n con id " + idSesion + " para emitir un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado al emitir un volante: " + e.toString());
			throw e;
		}
		
		return idVolante;
	}
		
	// ------------------------------
	// M�todos del Gestor de Mensajes
	// ------------------------------
	
	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, Exception {
		Object resultado;
		String login;
		
		resultado = null;
		switch((int)codigoMensaje) {
		
		case ICodigosMensajeAuxiliar.OPERACIONES_DISPONIBLES:
			try {
				resultado = GestorSesiones.operacionesDisponibles(idSesion);
			} catch(Exception e) {
				// TODO: Poner catch para las excepciones que se pueden lanzar
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al ejecutar una operaci�n auxiliar: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CONSULTAR_USUARIO:
			try {
				// Obtenemos el usuario con el DNI indicado
				resultado = GestorUsuarios.consultarUsuario(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el usuario con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar el usuario con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioInexistenteException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un usuario inexistente con DNI " + (String)informacion + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del usuario con DNI " + (String)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un usuario con DNI nulo: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta del usuario con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar el usuario con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar un usuario: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CREAR_USUARIO:
			try {
				// A�adimos un nuevo usuario al sistema
				GestorUsuarios.crearUsuario(idSesion, (Usuario)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Creado el usuario con DNI " + ((Usuario)informacion).getDni() + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error SQL al crear el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioYaExistenteException uyee) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al crear un usuario con DNI " + ((Usuario)informacion).getDni() + " que ya existe: " + uyee.getLocalizedMessage());
				throw uyee;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al recuperar el centro de salud del usuario con DNI " + ((Usuario)informacion).getDni() + " que se iba a crear: " + csie.getLocalizedMessage());
				throw csie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al crear un usuario nulo: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Error al realizar una operaci�n no permitida de creaci�n de un usuario con DNI " + ((Usuario)informacion).getDni() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error al comprobar la sesi�n con id " + idSesion + " para crear el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_CREATE, "Error inesperado al crear un usuario: " + e.toString());
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
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error SQL al modificar el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioInexistenteException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al modificar un usuario inexistente con DNI " + ((Usuario)informacion).getDni() + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al recuperar el centro de salud del usuario con DNI " + ((Usuario)informacion).getDni() + " que se iba a modificar: " + csie.getLocalizedMessage());
				throw csie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al modificar un usuario nulo: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_UPDATE, "Error al realizar una operaci�n no permitida de modificaci�n del usuario con DNI " + ((Usuario)informacion).getDni() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error al comprobar la sesi�n con id " + idSesion + " para modificar el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Error inesperado al modificar un usuario: " + e.toString());
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
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error SQL al eliminar el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(UsuarioInexistenteException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al eliminar un usuario inexistente con DNI " + ((Usuario)informacion).getDni() + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar el centro de salud del usuario con DNI " + ((Usuario)informacion).getDni() + " que se iba a eliminar: " + csie.getLocalizedMessage());
				throw csie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al eliminar un usuario nulo: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al realizar una operaci�n no permitida de eliminaci�n del usuario con DNI " + ((Usuario)informacion).getDni() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error al comprobar la sesi�n con id " + idSesion + " para eliminar el usuario con DNI " + ((Usuario)informacion).getDni() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error inesperado al eliminar un usuario: " + e.toString());
				throw e;
			}
			break;
			
		case ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO:
			try {
				// Eliminamos un beneficiario del sistema
				GestorBeneficiarios.eliminarBeneficiario(idSesion, (Beneficiario)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Eliminado el beneficiario con DNI " + ((Beneficiario)informacion).getNif() + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error SQL al eliminar el beneficiario con DNI " + ((Beneficiario)informacion).getNif() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al eliminar el beneficiario inexistente con DNI " + ((Beneficiario)informacion).getNif() + ": " + bie.getLocalizedMessage());
				throw bie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con DNI " + ((Beneficiario)informacion).getNif() + " que se iba a eliminar: " + csie.getLocalizedMessage());
				throw csie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al eliminar un beneficiario nulo: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al realizar una operaci�n no permitida de eliminaci�n del beneficiario con DNI " + ((Beneficiario)informacion).getNif() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error al comprobar la sesi�n con id " + idSesion + " para eliminar el beneficiario con DNI " + ((Beneficiario)informacion).getNif() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_DELETE, "Error inesperado al eliminar un beneficiario: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO:
			try {
				resultado = GestorMedicos.obtenerMedicos(idSesion, (CategoriasMedico)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultados los DNIs de los m�dicos de tipo " + ((CategoriasMedico)informacion).toString() + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar los DNIs de los m�dicos de tipo " + ((CategoriasMedico)informacion).toString() + ": " + se.getLocalizedMessage());
				throw se;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_DELETE, "Error al recuperar el centro de salud de un medico del tipo: " + ((CategoriasMedico)informacion) + ". " + csie.getLocalizedMessage());
				throw csie;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta de los DNIs de los m�dicos de tipo " + ((CategoriasMedico)informacion).toString() + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar los DNIs de los m�dicos de tipo " + ((CategoriasMedico)informacion).toString() + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar los DNIs de los m�dicos de tipo " + ((CategoriasMedico)informacion).toString() + ": " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS:
			try {
				// Consultamos a qu� horas puede trabajar el m�dico con el DNI indicado
				resultado = GestorCitas.consultarHorasCitas(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el horario del m�dico con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar el horario del m�dico con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(MedicoInexistenteException mie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar el horario de un m�dico inexistente con DNI " + (String)informacion + ": " + mie.getLocalizedMessage());
				throw mie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del m�dico con DNI " + (String)informacion + " para el que se iba a consultar el horario: " + csie.getLocalizedMessage());
				throw csie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar el horario de un m�dico con datos no v�lidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta del horario del m�dico con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar el horario del m�dico con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar el horario de un m�dico: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO:
			try {
				// Consultamos las citas del m�dico con el DNI indicado
				resultado = GestorCitas.consultarCitasMedico(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultadas las citas del m�dico con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar las citas del m�dico con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar uno de los beneficiarios que ten�a cita con el m�dico con DNI " + (String)informacion + " para el que se estaban consultando las citas: " + bie.getLocalizedMessage());
				throw bie;
			} catch(MedicoInexistenteException mie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar las citas de un m�dico inexistente con DNI " + (String)informacion + ": " + mie.getLocalizedMessage());
				throw mie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el m�dico con DNI " + (String)informacion + " para el que se iban a consultar las citas: " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del m�dico con DNI " + (String)informacion + " para el que se iban a consultar las citas: " + csie.getLocalizedMessage());
				throw csie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar las citas de un m�dico con datos no v�lidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta de las citas del m�dico con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar las citas del m�dico con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar las citas de un m�dico: " + e.toString());
				throw e;
			}
			break;
		
		case ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS:
			try {
				// Consultamos los d�as completos del m�dico con el DNI indicado
				resultado = GestorCitas.consultarDiasCompletos(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultados los d�as completos del m�dico con DNI " + (String)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar los d�as completos del m�dico con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar uno de los beneficiarios que ten�a cita con el m�dico con DNI " + (String)informacion + " para el que se estaban consultando los d�as completos: " + bie.getLocalizedMessage());
				throw bie;
			} catch(MedicoInexistenteException mie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar los d�as completos de un m�dico inexistente con DNI " + (String)informacion + ": " + mie.getLocalizedMessage());
				throw mie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el m�dico con DNI " + (String)informacion + " para el que se iban a consultar los d�as completos: " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud del m�dico con DNI " + (String)informacion + " para el que se iban a consultar los d�as completos: " + csie.getLocalizedMessage());
				throw csie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar los d�as completos de un m�dico con datos no v�lidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta de los d�as completos del m�dico con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar los d�as completos del m�dico con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar los di�s completos de un m�dico: " + e.toString());
				throw e;
			}
			break;
			
		case ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES:
			try {
				// Consultamos las citas del beneficiario con el DNI indicado
				Vector<Cita> citas = GestorCitas.consultarCitas(idSesion, (String)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultadas las citas pendientes del beneficiario con DNI " + (String)informacion + ".");
				// Devolvemos s�lo aquellas citas cuya fecha sea mayor o igual a la fecha actual
				Vector<Cita> citasPendientes = new Vector<Cita>();
				Date fechaActual = new Date();
				Date fechaCita;
				for (Cita c: citas) {
					fechaCita = c.getFechaYHora();
					// Si el a�o es mayor, la cita estar� pendiente
					if (fechaCita.getYear() > fechaActual.getYear())
						citasPendientes.add(c);
					// Si es el mismo a�o, pero en un mes posterior, la cita estar� pendiente
					else if ((fechaCita.getYear() == fechaActual.getYear()) && (fechaCita.getMonth() > fechaActual.getMonth()))
						citasPendientes.add(c);
					// Si es el mismo a�o y el mismo mes, la cita estar� pendiente en un d�a posterior al actual
					else if ((fechaCita.getYear() == fechaActual.getYear()) && (fechaCita.getMonth() == fechaActual.getMonth()) && (fechaCita.getDate() > fechaActual.getDate()))
						citasPendientes.add(c);
					// Si es el mismo a�o, mes y d�a, la cita estar� pendiente si no se ha pasado la hora
					else if ((fechaCita.getYear() == fechaActual.getYear()) && (fechaCita.getMonth() == fechaActual.getMonth()) && (fechaCita.getDate() == fechaActual.getDate()) && (fechaCita.getHours() > fechaActual.getHours()))
						citasPendientes.add(c);
					// Si es el mismo a�o, mes, dia y hora, la cita estar� pendiente si es en minutos posteriores a la fecha actual
					else if ((fechaCita.getYear() == fechaActual.getYear()) && (fechaCita.getMonth() == fechaActual.getMonth()) && (fechaCita.getDate() == fechaActual.getDate()) && (fechaCita.getHours() == fechaActual.getHours()) && (fechaCita.getMinutes() > fechaActual.getMinutes()))
						citasPendientes.add(c);	
				}
				resultado = citasPendientes;
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar las citas pendientes del beneficiario con DNI " + (String)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el beneficiario con DNI " + (String)informacion + " para el que se estaban consultando sus citas: " + bie.getLocalizedMessage());
				throw bie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el m�dico asociado al beneficiario con DNI " + (String)informacion + ". " + uie.getLocalizedMessage());
				throw uie;
			} catch(NullPointerException npe) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar las citas pendientes de un beneficiario con datos no v�lidos: " + npe.getLocalizedMessage());
				throw npe;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta de las citas pendientes del beneficiario con DNI " + (String)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar las citas pendientes del beneficiario con DNI " + (String)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar las citas de un beneficiario: " + e.toString());
				throw e;
			}
			break;
		case ICodigosMensajeAuxiliar.CONSULTAR_VOLANTE:
			try {
				// Consultamos un volante por id
				resultado = GestorCitas.consultarVolante(idSesion, (Long)informacion);
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Consultado el volante con id " + (Long)informacion + ".");
			} catch(SQLException se) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error SQL al consultar el volante con id " + (Long)informacion + ": " + se.getLocalizedMessage());
				throw se;
			} catch(VolanteNoValidoException vnve) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar un volante inexistente con id " + (Long)informacion + ": " + vnve.getLocalizedMessage());
				throw vnve;
			} catch(CitaNoValidaException cnve) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar la cita asociada al volante con id " + (Long)informacion + ": " + cnve.getLocalizedMessage());
				throw cnve;
			} catch(BeneficiarioInexistenteException bie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar el beneficiario asociado al volante con id " + (Long)informacion + ": " + bie.getLocalizedMessage());
				throw bie;
			} catch(UsuarioIncorrectoException uie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al consultar alguno de los m�dicos asociados al volante con id " + (Long)informacion + ": " + uie.getLocalizedMessage());
				throw uie;
			} catch(CentroSaludInexistenteException csie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al recuperar el centro de salud de alguno de los m�dicos asociados al volante con id " + (Long)informacion + ": " + csie.getLocalizedMessage());
				throw csie;
			} catch(OperacionIncorrectaException oie) {
				login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
				GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_READ, "Error al realizar una operaci�n no permitida de consulta del volante con id " + (Long)informacion + ": " + oie.getLocalizedMessage());
				throw oie;
			} catch(SesionInvalidaException sie) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error al comprobar la sesi�n con id " + idSesion + " para consultar el volante con id " + (Long)informacion + ": " + sie.getLocalizedMessage());
				throw sie;
			} catch(Exception e) {
				GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_READ, "Error inesperado al consultar un volante: " + e.toString());
				throw e;
			}
			break;
		}
		
		
			
		
		return resultado;
	}

	// M�todos a�n no implementados

	/*

	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException {
		GestorMedicos.modificarCalendario(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
	}
	*/

}
