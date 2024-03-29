package dominio.control;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import comunicaciones.ICliente;
import comunicaciones.ProxyCliente;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.SesionBeneficiario;
import dominio.conocimiento.SesionUsuario;
import dominio.conocimiento.Usuario;
import persistencia.FPBeneficiario;
import persistencia.FPUsuario;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de iniciar y finalizar sesiones con los clientes del
 * sistema, as� como de comunicarse con los clientes registrados cuando hay
 * un cambio en el servidor.
 */
public class GestorSesiones {
	
	// Tabla que asocia el identificador de cada sesi�n con la propia sesi�n
	private static Hashtable<Long, Sesion> sesiones = new Hashtable<Long, Sesion>();

	// Tabla que asocia el identificador de cada sesi�n con el cliente
	private static Hashtable<Long, ICliente> clientes = new Hashtable<Long, ICliente>();

	// Metodo para identificar un usuario y crear una sesion
	public static ISesion identificarUsuario(String login, String password) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, RemoteException, Exception {
		Enumeration<Sesion> sesionesAbiertas; 
		Sesion sesion, sesionAbierta;
		ICliente cliente;
		Usuario usuario;
		Random rnd;
		String passwordEncriptada;
		boolean encontrado;
		long idSesion;
		
		// Comprobamos los par�metros pasados
		if(login == null) {
			throw new NullPointerException("El nombre de usuario no puede ser nulo.");
		}
		if(password == null) {
			throw new NullPointerException("La contrase�a no puede ser nula.");
		}
		
		// Encriptamos la contrase�a del usuario
		try {
			passwordEncriptada = UtilidadesDominio.encriptarPasswordSHA1(password);			
		} catch(NoSuchAlgorithmException e) {
			throw new SQLException("No se puede encriptar la contrase�a del usuario.");
		}
		// Comprobamos el login y la contrase�a encriptada del usuario
		usuario = FPUsuario.consultar(login, passwordEncriptada);

		// Comprobamos si el usuario ya ten�a una sesi�n iniciada
		sesionesAbiertas = sesiones.elements();
		sesionAbierta = null;
		encontrado = false;
		while(sesionesAbiertas.hasMoreElements() && !encontrado) {
			sesionAbierta = sesionesAbiertas.nextElement();
			if(sesionAbierta instanceof SesionUsuario
			 && ((SesionUsuario)sesionAbierta).getUsuario().getNif().equals(usuario.getNif())) {
				encontrado = true;
			}
		}
		
		// Si el usuario ya ten�a una sesion iniciada, se cierra
		if(encontrado) {
			cliente = clientes.get(sesionAbierta.getId());
			// El cliente devuelto puede ser null si nunca se llam� al
			// m�todo registrar (por ejemplo, en las pruebas del sistema)
			if(cliente != null) {
				try {
					// Forzamos a que el cliente antiguo salga del sistema
					cliente.cerrarSesion();
				} catch(RemoteException e) {
					// Ignoramos la excepci�n
				}
			}
			ServidorFrontend.getServidor().liberar(sesionAbierta.getId());
		}

		// Creamos un identificador �nico para la nueva sesi�n
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		do {
			idSesion = Math.abs(rnd.nextLong());
		} while(sesiones.containsKey(idSesion));

		// Creamos la sesi�n y la guardamos en la tabla de sesiones
		sesion = new SesionUsuario(idSesion, usuario);
		sesiones.put(idSesion, sesion);
		
		return (ISesion)sesion;
	}
	
	public static ISesion identificarBeneficiario(String nss) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, RemoteException, Exception {
		Enumeration<Sesion> sesionesAbiertas; 
		Sesion sesion, sesionAbierta;
		ICliente cliente;
		Beneficiario bene;
		Random rnd;
		boolean encontrado;
		long idSesion;
		
		// Comprobamos los par�metros pasados
		if(nss == null) {
			throw new NullPointerException("El NSS de un beneficiario no puede ser nulo.");		
		}
		
		// Consultamos el beneficiario
		bene = FPBeneficiario.consultarPorNSS(nss);

		// Comprobamos si el beneficiario ya ten�a una sesi�n iniciada
		sesionesAbiertas = sesiones.elements();
		sesionAbierta = null;
		encontrado = false;
		while(sesionesAbiertas.hasMoreElements() && !encontrado) {
			sesionAbierta = sesionesAbiertas.nextElement();
			if(sesionAbierta instanceof SesionBeneficiario
			 && ((SesionBeneficiario)sesionAbierta).getBeneficiario().getNif().equals(bene.getNif())) {
				encontrado = true;
			}
		}
		
		// Si el beneficiario ya ten�a una sesion iniciada, se cierra
		if(encontrado) {
			cliente = clientes.get(sesionAbierta.getId());
			// El cliente devuelto puede ser null si nunca se llam� al
			// m�todo registrar (por ejemplo, en las pruebas del sistema)
			if(cliente != null) {
				try {
					// Forzamos a que el cliente antiguo salga del sistema
					cliente.cerrarSesion();
				} catch(RemoteException e) {
					// Ignoramos la excepci�n
				}
			}
			ServidorFrontend.getServidor().liberar(sesionAbierta.getId());
		}

		// Creamos un identificador �nico para la nueva sesi�n
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		do {
			idSesion = Math.abs(rnd.nextLong());
		} while(sesiones.containsKey(idSesion));

		// Creamos la sesi�n y la guardamos en la tabla de sesiones
		sesion = new SesionBeneficiario(idSesion, bene);
		sesiones.put(idSesion, sesion);
		
		return (ISesion)sesion;
	}
	
	// M�todo para registrar un nuevo cliente en el sistema
	public static void registrar(long idSesion, ICliente cliente) throws SesionNoIniciadaException, RemoteException {
		ProxyCliente proxyCliente;
		Sesion sesion;
		
		// Comprobamos los par�metros pasados
		if(cliente == null) {
			throw new NullPointerException("El cliente que se quiere registrar no puede ser nulo.");
		}
		
		// Comprobamos si la sesi�n es v�lida
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionNoIniciadaException("El identificador de la sesi�n es inv�lido.");
		}

		// Creamos un proxy asociado con el cliente y lo a�adimos a la lista de clientes conectados
		proxyCliente = new ProxyCliente();
		proxyCliente.asociar(cliente);
		clientes.put(idSesion, proxyCliente);		
	}
	
	// Metodo para cerrar una sesi�n y liberar el cliente registrado
	public static void liberar(long idSesion) throws SesionNoIniciadaException {
		Sesion sesion;
		
		// Comprobamos si la sesi�n es v�lida
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionNoIniciadaException("El identificador de la sesi�n es inv�lido.");
		}

		// Quitamos la sesi�n y el cliente
		sesiones.remove(idSesion);
		clientes.remove(idSesion);
	}	
	
	// M�todo que devuelve las operaciones que puede realizar el usuario con el servidor
	public static Vector<Operaciones> operacionesDisponibles(long idSesion) throws SesionInvalidaException {
		Vector<Operaciones> operaciones;
		Sesion sesion;
		
		// Comprobamos si la sesi�n es v�lida
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionInvalidaException("El identificador de la sesi�n es inv�lido.");
		}
		
		// Agregamos las operaciones permitidas para todos los usuarios
		operaciones = new Vector<Operaciones>();
		operaciones.add(Operaciones.ConsultarPropioUsuario);
		operaciones.add(Operaciones.ConsultarBeneficiario);
		operaciones.add(Operaciones.ConsultarCentros);
		operaciones.add(Operaciones.ConsultarVolante);
		operaciones.add(Operaciones.ConsultarMedico);
		operaciones.add(Operaciones.CorrespondeNIFUsuario);

		// Agregamos las operaciones permitidas para los beneficiarios
		if(sesion.getRol() == Roles.Beneficiario.ordinal()) {
			operaciones.add(Operaciones.ConsultarCitasBeneficiario);
			operaciones.add(Operaciones.AnularCita);
			operaciones.add(Operaciones.TramitarCita);
			operaciones.add(Operaciones.TramitarCitaVolante);
			operaciones.add(Operaciones.ConsultarCitasMedico);
			operaciones.add(Operaciones.ConsultarMedicoCita);
		}
		
		// Agregamos las operaciones permitidas para citadores y administradores
		if(sesion.getRol() == Roles.Administrador.ordinal() || sesion.getRol() == Roles.Citador.ordinal()) {	
			operaciones.add(Operaciones.RegistrarBeneficiario);
			operaciones.add(Operaciones.ModificarBeneficiario);
			operaciones.add(Operaciones.EliminarBeneficiario);
			operaciones.add(Operaciones.ConsultarMedicoCita);
			operaciones.add(Operaciones.ConsultarCitasBeneficiario);
			operaciones.add(Operaciones.ConsultarCitasMedico);
			operaciones.add(Operaciones.TramitarCita);
			operaciones.add(Operaciones.TramitarCitaVolante);
			operaciones.add(Operaciones.AnularCita);
		}
		
		// Agregamos las operaciones permitidas para administradores
		if(sesion.getRol() == Roles.Administrador.ordinal()) {
			operaciones.add(Operaciones.ConsultarUsuario);
			operaciones.add(Operaciones.RegistrarUsuario);
			operaciones.add(Operaciones.ModificarUsuario);
			operaciones.add(Operaciones.EliminarUsuario);
			operaciones.add(Operaciones.RegistrarMedico);
			operaciones.add(Operaciones.ModificarMedico);
			operaciones.add(Operaciones.EliminarMedico);
			operaciones.add(Operaciones.ConsultarMedicosTipo);
			operaciones.add(Operaciones.ConsultarSustitutosPosibles);
			operaciones.add(Operaciones.EstablecerSustituto);
			operaciones.add(Operaciones.ConsultarBeneficiariosMedico);
			operaciones.add(Operaciones.ConsultarPropioMedico);
		}
		
		// Agregamos las operaciones permitidas para m�dicos
		if(sesion.getRol() == Roles.M�dico.ordinal()) {
			operaciones.add(Operaciones.ConsultarMedicosTipo);
			operaciones.add(Operaciones.EmitirVolante);
			operaciones.add(Operaciones.ConsultarCitasPropiasMedico);
			operaciones.add(Operaciones.ConsultarPropioMedico);
			operaciones.add(Operaciones.ConsultarBeneficiariosMedico);
		}
		
		return operaciones;
	}
	
	// M�todo utilizado por otros gestores para comprobar los permisos de un usuario
	public static void comprobarPermiso(long idSesion, Operaciones operacion) throws SesionInvalidaException, OperacionIncorrectaException {
		Vector<Operaciones> operaciones;
		Sesion sesion;
		
		// Comprobamos si la sesi�n es v�lida
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionInvalidaException("El identificador de la sesi�n es inv�lido.");
		}

		// Obtenemos la lista de operaciones disponibles para el usuario
		operaciones = operacionesDisponibles(idSesion);

		// Comprobamos si se tienen permisos para realizar la operaci�n
		if(!operaciones.contains(operacion)) {
			throw new OperacionIncorrectaException("El rol " + Roles.values()[(int)sesion.getRol()] + " no tiene permiso para realizar la operaci�n " + operacion.toString() + ".");
		}
	}

	// M�todo que fuerza la desconexi�n de todos los clientes conectados al servidor
	public static void desconectarClientes() throws RemoteException {
		try {
			// Cerramos todos los clientes que hay conectados al servidor
			for(Long id : clientes.keySet()) {
				// Notificamos al cliente que el servidor ha sido desconectado
				clientes.get(id).servidorInaccesible();
			}
		} finally {
			// Reseteamos la tabla de sesiones y clientes (lo hacemos aqu�,
			// y no iteraci�n a iteraci�n en el bucle anterior, para evitar
			// el problema de tablas mutantes)
			sesiones = new Hashtable<Long, Sesion>();
			clientes = new Hashtable<Long, ICliente>();
		}
	}
	
	// M�todo que actualiza el estado de los clientes conectados al servidor
	public static void actualizarClientes(long idSesion, int operacion, Object dato) throws RemoteException {
		// Avisamos a todos los clientes (menos el que lanz� la operaci�n)
		// de que ha ocurrido una inserci�n, modificaci�n o eliminaci�n
		// de un objeto y puede que haya que refrescar alguna ventana
		for(Long id : clientes.keySet()) {
			if(id != idSesion) {
				clientes.get(id).actualizarVentanas(operacion, dato);
			}
		}
	}
	
	// M�todo para cerrar la sesi�n de un usuario
	public static void cerrarSesionUsuario(String nifUsuario) throws Exception {
		Enumeration<Sesion> sesionesAbiertas;
		Sesion sesion, sesionUsuario;

		// Buscamos el cliente asociado al usuario
		sesionUsuario = null;
		sesionesAbiertas = sesiones.elements();
		while(sesionesAbiertas.hasMoreElements() && sesionUsuario == null) {
			sesion = sesionesAbiertas.nextElement();
			if(sesion instanceof SesionUsuario
			 && ((SesionUsuario)sesion).getUsuario().getNif().equals(nifUsuario)) {
				sesionUsuario = sesion;
			}
		}
		
		if(sesionUsuario != null) {
			// Si el cliente se hab�a registrado en el sistema, lo desconectamos
			if(clientes.get(sesionUsuario.getId()) != null) {
				clientes.get(sesionUsuario.getId()).cerrarSesionEliminacion();	
			}
			// Liberamos el cliente
			ServidorFrontend.getServidor().liberar(sesionUsuario.getId());
		}
	}
	
	public static Hashtable<Long, ICliente> getClientes() {
		return clientes;
	}
	
	public static Sesion getSesion(long idSesion) {
		return sesiones.get(idSesion);
	}

}
