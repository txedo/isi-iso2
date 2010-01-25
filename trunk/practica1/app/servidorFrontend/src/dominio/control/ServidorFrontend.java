 package dominio.control;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import comunicaciones.GestorConexionesEstado;
import comunicaciones.ICliente;
import comunicaciones.IServidorFrontend;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CentroSaludIncorrectoException;
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
				GestorConexionesEstado.ponerMensaje(mensajeLog);
				GestorConexionesEstado.actualizarClientesEscuchando(clientesEscuchando.size());
			}
			GestorConexionesEstado.ponerMensaje("Usuario '" + login + "' autenticado.");
		} catch(RemoteException re) {
			GestorConexionesEstado.ponerMensaje("Error de conexi�n al autenticar el usuario '" + login + "': " + re.getLocalizedMessage());
			throw re;
		} catch(SQLException se) {
			GestorConexionesEstado.ponerMensaje("Error SQL al autenticar el usuario '" + login + "': " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			GestorConexionesEstado.ponerMensaje("Error en el nombre de usuario o la contrase�a al autenticar el usuario '" + login + "': " + uie.getLocalizedMessage());
			throw uie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al autenticar un usuario: " + e.toString());
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
				GestorConexionesEstado.ponerMensaje("Nuevo cliente a la escucha. ID de sesion " + idSesion);
				GestorConexionesEstado.actualizarClientesEscuchando(clientesEscuchando.size());
			}
		} catch (SesionNoIniciadaException snie) {
			GestorConexionesEstado.ponerMensaje(snie.getMessage());
			throw snie;
		} catch (Exception e) {
			GestorConexionesEstado.ponerMensaje(e.getMessage());
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
				GestorConexionesEstado.ponerMensaje(mensajeLog);
				GestorConexionesEstado.actualizarClientesEscuchando(clientesEscuchando.size());
			}
		} catch (Exception e) {
			GestorConexionesEstado.ponerMensaje(e.getMessage());
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
			beneficiario = GestorBeneficiarios.getBeneficiario(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Consultado el beneficiario con NIF " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al consultar el beneficiario con NIF " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el m�dico asociado al beneficiario con NIF " + dni + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al consultar un beneficiario inexistente con NIF " + dni + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de consulta del beneficiario con NIF " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para consultar el beneficiario con NIF " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al consultar un beneficiario por NIF: " + e.toString());
			throw e;
		}
		
		return beneficiario;
	}
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		String login;
		
		try {
			// Obtenemos el beneficiario con el NSS indicado
			beneficiario = GestorBeneficiarios.getBeneficiarioPorNSS(idSesion, nss);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Consultado el beneficiario con NSS " + nss + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al consultar el beneficiario con NSS " + nss + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el m�dico asociado al beneficiario con NSS " + nss + ": " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NSS " + nss + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al consultar un beneficiario inexistente con NSS " + nss + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de consulta del beneficiario con NSS " + nss + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para consultar el beneficiario con NSS " + nss + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al consultar un beneficiario por NSS: " + e.toString());
			throw e;
		}
		
		return beneficiario;
	}

	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		String login;
		
		try {
			// A�adimos un nuevo beneficiario al sistema
			GestorBeneficiarios.crear(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Creado el beneficiario con NIF " + beneficiario.getNif() + " y NSS " + beneficiario.getNss() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al crear el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a crear: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a crear: " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioYaExistenteException byee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al crear un beneficiario con NIF " + beneficiario.getNif() + " que ya existe: " + byee.getLocalizedMessage());
			throw byee;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de creaci�n de un beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para crear un beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al crear un beneficiario: " + e.toString());
			throw e;
		}
	}

	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		String login;
		
		try {
			// Modificamos un beneficiario existente del sistema
			GestorBeneficiarios.modificar(idSesion, beneficiario);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Modificado el beneficiario con NIF " + beneficiario.getNif() + " y NSS " + beneficiario.getNss() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al modificar el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a modificar: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " que se iba a modificar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al modificar un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de modificaci�n del beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para modificar el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al modificar un beneficiario: " + e.toString());
			throw e;
		}
	}
	
	// --------------------------------------
	// M�todos del Gestor de Usuarios/M�dicos
	// --------------------------------------
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		Medico medico;
		String login;
		
		try {
			// Obtenemos el m�dico con el DNI indicado
			medico = GestorUsuarios.getMedico(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Consultado el m�dico con DNI " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al consultar el m�dico con DNI " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al consultar un m�dico inexistente con DNI " + dni + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico con DNI " + dni + ": " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de consulta del m�dico con DNI " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para consultar el m�dico con DNI " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al consultar un m�dico: " + e.toString());
			throw e;
		}
		
		return medico;
	}
	
	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		String login;
		
		try {
			// A�adimos un nuevo m�dico al sistema
			GestorUsuarios.crearMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Creado el m�dico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al crear el m�dico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoYaExistenteException myee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al crear un m�dico con DNI " + medico.getDni() + " que ya existe: " + myee.getLocalizedMessage());
			throw myee;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico con DNI " + medico.getDni() + " que se iba a crear: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de creaci�n de un m�dico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para crear el m�dico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al crear un m�dico: " + e.toString());
			throw e;
		}
	}
	
	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Modificamos un m�dico existente del sistema
			GestorUsuarios.modificarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Modificado el m�dico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al modificar el m�dico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al modificar un m�dico inexistente con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico con DNI " + medico.getDni() + " que se iba a modificar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de modificaci�n del m�dico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para modificar el m�dico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al modificar un m�dico: " + e.toString());
			throw e;
		}
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		String login;
		
		try {
			// Eliminamos un m�dico del sistema
			GestorUsuarios.eliminarMedico(idSesion, medico);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Eliminado el m�dico con DNI " + medico.getDni() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al eliminar el m�dico con DNI " + medico.getDni() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al eliminar un m�dico inexistente con DNI " + medico.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico con DNI " + medico.getDni() + " que se iba a eliminar: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de eliminaci�n del m�dico con DNI " + medico.getDni() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para eliminar el m�dico con DNI " + medico.getDni() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al eliminar un m�dico: " + e.toString());
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
			GestorConexionesEstado.ponerMensaje(login, "Solicitada una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al pedir una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al pedir una cita para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al pedir una cita para un m�dico inexistente con DNI " + idMedico + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + csie.getLocalizedMessage());
			throw csie;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al pedir una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + " a una fecha y hora no v�lidas: " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de petici�n de una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para pedir una cita para el m�dico con DNI " + idMedico + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al solicitar una cita: " + e.toString());
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
			GestorConexionesEstado.ponerMensaje(login, "Solicitada una cita para el el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al pedir una cita para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a pedir una cita: " + csie.getLocalizedMessage());
			throw csie;
		} catch(VolanteNoValidoException vnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir de un volante con el id inexistente o inv�lido " + idVolante + ": " + vnve.getLocalizedMessage());
			throw vnve;
		} catch(FechaNoValidaException fnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al pedir una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + " a una fecha y hora no v�lidas: " + fnve.getLocalizedMessage());
			throw fnve;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de petici�n de una cita para el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para pedir una cita para y el beneficiario con NIF " + beneficiario.getNif() + " a partir del volante con id " + idVolante + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al solicitar una cita a partir de un volante: " + e.toString());
			throw e;
		}
		
		return cita;
	}
	
	public Vector<Cita> getCitas(long idSesion, String dni) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		Vector<Cita> citas;
		String login;
		
		try {
			// Obtenemos las citas que tiene un beneficiario
			citas = GestorCitas.getCitas(idSesion, dni);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Consultadas las citas del beneficiario con NIF " + dni + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al consultar las citas del beneficiario con NIF " + dni + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al consultar las citas de un beneficiario inexistente con NIF " + dni + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el m�dico asociado al beneficiario con NIF " + dni + " para el que se iban a consultar las citas: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + dni + " para el que se iban a consultar las citas: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de consulta de las citas del beneficiario con NIF " + dni + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para consultar las citas del beneficiario con NIF " + dni + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al consultar las citas de un beneficiario: " + e.toString());
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
			GestorConexionesEstado.ponerMensaje(login, "Anulada una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al anular una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(CitaNoValidaException cnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al anular una cita inexistente del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + cnve.getLocalizedMessage());
			throw cnve;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de anulaci�n de una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para anular una cita del beneficiario con NIF " + cita.getBeneficiario().getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al anular una cita de un beneficiario: " + e.toString());
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
			GestorConexionesEstado.ponerMensaje(login, "Emitido un volante para el m�dico con DNI " + destino.getDni() + " y el beneficiario con NIF " + beneficiario.getNif() + ".");
		} catch(SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al emitir un volante para el m�dico con DNI " + destino.getDni() + " y el beneficiario con NIF " + beneficiario.getNif() + ": " + se.getLocalizedMessage());
			throw se;
		} catch(BeneficiarioInexistenteException bie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al solicitar un volante para un beneficiario inexistente con NIF " + beneficiario.getNif() + ": " + bie.getLocalizedMessage());
			throw bie;
		} catch(MedicoInexistenteException mie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al solicitar un volante para un m�dico inexistente con DNI " + emisor.getDni() + " o " + destino.getDni() + ": " + mie.getLocalizedMessage());
			throw mie;
		} catch(VolanteNoValidoException vnve) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al solicitar un volante para un m�dico no especialista con DNI " + destino.getDni() + ": " + vnve.getLocalizedMessage());
			throw vnve;
		} catch(UsuarioIncorrectoException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a emitir un volante: " + uie.getLocalizedMessage());
			throw uie;
		} catch(CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al recuperar el centro de salud del m�dico asociado al beneficiario con NIF " + beneficiario.getNif() + " para el que se iba a emitir un volante: " + csie.getLocalizedMessage());
			throw csie;
		} catch(OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida de emisi�n de un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + oie.getLocalizedMessage());
			throw oie;
		} catch(SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion + " para emitir un volante para el beneficiario con NIF " + beneficiario.getNif() + ": " + sie.getLocalizedMessage());
			throw sie;
		} catch(Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado al emitir un volante: " + e.toString());
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
		GestorConexionesEstado.ponerMensaje("Usuario '" + GestorSesiones.getSesion(idSesion).getUsuario().getLogin() + "' ejecuta la operaci�n " + codigoMensaje);
		try {
			resultado = GestorMensajes.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
		} catch (SQLException se) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error SQL al ejecutar la operaci�n " + codigoMensaje);
			throw se;
		} catch (CentroSaludIncorrectoException csie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al ejecutar la operaci�n " + codigoMensaje + ". No se puede recuperar el centro de salud del usuario ");
			throw csie;
		} catch (UsuarioYaExistenteException uyee) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al ejecutar la operaci�n " + codigoMensaje + ". No se puede crear el usuario porque ya existe");
			throw uyee;
		} catch (UsuarioInexistenteException uie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al ejecutar la operaci�n " + codigoMensaje + ". No se puede recuperar el usuario porque no existe en la base de datos");
			throw uie;
		} catch (OperacionIncorrectaException oie) {
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesEstado.ponerMensaje(login, "Error al realizar una operaci�n no permitida " + codigoMensaje);
			throw oie;
		} catch (SesionInvalidaException sie) {
			GestorConexionesEstado.ponerMensaje("Error al comprobar la sesi�n con id " + idSesion 	+ " para la operaci�n " + codigoMensaje);
			throw sie;
		} catch (Exception e) {
			GestorConexionesEstado.ponerMensaje("Error inesperado: " + e.toString());
			throw e;
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
