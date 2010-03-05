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
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Usuario;
import persistencia.FPUsuario;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de iniciar y finalizar sesiones con los clientes
 * del sistema.
 */
public class GestorSesiones {
	
	// Tabla que asocia el identificador de cada sesión con la propia sesión
	private static Hashtable<Long, Sesion> sesiones = new Hashtable<Long, Sesion>();

	// Tabla que asocia el identificador de cada sesión con el cliente
	private static Hashtable<Long, ICliente> clientes = new Hashtable<Long, ICliente>();

	// Metodo para identificar un cliente y crear una sesion
	public static ISesion identificar(String login, String password) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException, RemoteException, Exception {
		Enumeration<Sesion> sesionesAbiertas; 
		Sesion sesion, sesionAbierta;
		ICliente cliente;
		Usuario usuario;
		Random rnd;
		String passwordEncriptada;
		boolean encontrado;
		long idSesion;
		
		// Comprobamos los parámetros pasados
		if(login == null) {
			throw new NullPointerException("El nombre de usuario no puede ser nulo.");
		}
		if(password == null) {
			throw new NullPointerException("La contraseña no puede ser nula.");
		}
		
		// Encriptamos la contraseña del usuario
		try {
			passwordEncriptada = UtilidadesDominio.encriptarPasswordSHA1(password);			
		} catch(NoSuchAlgorithmException e) {
			throw new SQLException("No se puede encriptar la contraseña del usuario.");
		}
		// Comprobamos el login y la contraseña encriptada del usuario
		usuario = FPUsuario.consultar(login, passwordEncriptada);

		// Comprobamos si el usuario ya tenía una sesión iniciada
		sesionesAbiertas = sesiones.elements();
		sesionAbierta = null;
		encontrado = false;
		while(sesionesAbiertas.hasMoreElements() && !encontrado) {
			sesionAbierta = sesionesAbiertas.nextElement();
			if(sesionAbierta.getUsuario().getNif().equals(usuario.getNif())) {
				encontrado = true;
			}
		}
		
		// Si el usuario ya tenía una sesion iniciada, se cierra
		if(encontrado) {
			cliente = clientes.get(sesionAbierta.getId());
			// El cliente devuelto puede ser null si nunca se llamó al
			// método registrar (por ejemplo, en las pruebas del sistema)
			if(cliente != null) {
				try {
					// Forzamos a que el cliente antiguo salga del sistema
					cliente.cerrarSesion();
				} catch(RemoteException e) {
					// Ignoramos la excepción
				}
			}
			ServidorFrontend.getServidor().liberar(sesionAbierta.getId());
		}

		// Creamos un identificador único para la nueva sesión
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		do {
			idSesion = Math.abs(rnd.nextLong());
		} while(sesiones.containsKey(idSesion));

		// Creamos la sesión y la guardamos en la tabla de sesiones
		sesion = new Sesion(idSesion, usuario);
		sesiones.put(idSesion, sesion);
		
		return (ISesion)sesion;
	}
	
	// Método para registrar un nuevo cliente en el sistema
	public static void registrar(long idSesion, ICliente cliente) throws SesionNoIniciadaException, RemoteException {
		ProxyCliente proxyCliente;
		Sesion sesion;
		
		// Comprobamos los parámetros pasados
		if(cliente == null) {
			throw new NullPointerException("El cliente que se quiere registrar no puede ser nulo.");
		}
		
		// Comprobamos si la sesión es válida
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionNoIniciadaException("El identificador de la sesión es inválido.");
		}

		// Creamos un proxy asociado con el cliente y lo añadimos a la lista de clientes conectados
		proxyCliente = new ProxyCliente();
		proxyCliente.asociar(cliente);
		clientes.put(idSesion, proxyCliente);		
	}
	
	// Metodo para cerrar una sesión y liberar el cliente registrado
	public static void liberar(long idSesion) throws SesionNoIniciadaException {
		Sesion sesion;
		
		// Comprobamos si la sesión es válida
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionNoIniciadaException("El identificador de la sesión es inválido.");
		}

		// Quitamos la sesión y el cliente
		sesiones.remove(idSesion);
		clientes.remove(idSesion);
	}	
	
	// Método que devuelve las operaciones que puede realizar el usuario con el servidor
	public static Vector<Operaciones> operacionesDisponibles(long idSesion) throws SesionInvalidaException {
		Vector<Operaciones> operaciones;
		Sesion sesion;
		
		// Comprobamos si la sesión es válida
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionInvalidaException("El identificador de la sesión es inválido.");
		}
		
		// Agregamos las operaciones permitidas para todos los usuarios
		operaciones = new Vector<Operaciones>();
		operaciones.add(Operaciones.ConsultarPropioUsuario);
		operaciones.add(Operaciones.ConsultarBeneficiario);
		operaciones.add(Operaciones.ConsultarCentros);
		operaciones.add(Operaciones.ConsultarVolante);
		
		// Agregamos las operaciones permitidas para citadores y administradores
		if(sesion.getRol() == RolesUsuario.Administrador.ordinal() || sesion.getRol() == RolesUsuario.Citador.ordinal()) {
			operaciones.add(Operaciones.RegistrarBeneficiario);
			operaciones.add(Operaciones.ModificarBeneficiario);
			operaciones.add(Operaciones.EliminarBeneficiario);
			operaciones.add(Operaciones.ConsultarMedico);
			operaciones.add(Operaciones.ConsultarMedicoCita);
			operaciones.add(Operaciones.ConsultarCitasBeneficiario);
			operaciones.add(Operaciones.ConsultarCitasMedico);
			operaciones.add(Operaciones.TramitarCita);
			operaciones.add(Operaciones.TramitarCitaVolante);
			operaciones.add(Operaciones.AnularCita);
		}
		
		// Agregamos las operaciones permitidas para administradores
		if(sesion.getRol() == RolesUsuario.Administrador.ordinal()) {
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
		}
		
		// Agregamos las operaciones permitidas para médicos
		if(sesion.getRol() == RolesUsuario.Medico.ordinal()) {
			operaciones.add(Operaciones.ConsultarMedicosTipo);
			operaciones.add(Operaciones.EmitirVolante);
			operaciones.add(Operaciones.ConsultarCitasPropiasMedico);
		}
		
		return operaciones;
	}
	
	// Método utilizado por otros gestores para comprobar los permisos de un usuario
	public static void comprobarPermiso(long idSesion, Operaciones operacion) throws SesionInvalidaException, OperacionIncorrectaException {
		Vector<Operaciones> operaciones;
		Sesion sesion;
		
		// Comprobamos si la sesión es válida
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionInvalidaException("El identificador de la sesión es inválido.");
		}

		// Obtenemos la lista de operaciones disponibles para el usuario
		operaciones = operacionesDisponibles(idSesion);

		// Comprobamos si se tienen permisos para realizar la operación
		if(!operaciones.contains(operacion)) {
			throw new OperacionIncorrectaException("El rol " + RolesUsuario.values()[(int)sesion.getRol()] + " no tiene permiso para realizar la operación " + operacion.toString() + ".");
		}
	}

	// Método que fuerza la desconexión de todos los clientes conectados al servidor
	public static void desconectarClientes() throws RemoteException {
		try {
			// Cerramos todos los clientes que hay conectados al servidor
			for(Long id : clientes.keySet()) {
				// Notificamos al cliente que el servidor ha sido desconectado
				clientes.get(id).servidorInaccesible();
			}
		} finally {
			// Reseteamos la tabla de sesiones y clientes (lo hacemos aquí,
			// y no iteración a iteración en el bucle anterior, para evitar
			// el problema de tablas mutantes)
			sesiones = new Hashtable<Long, Sesion>();
			clientes = new Hashtable<Long, ICliente>();
		}
	}
	
	// Método que actualiza el estado de los clientes conectados al servidor
	public static void actualizarClientes(long idSesion, int operacion, Object dato) throws RemoteException {
		// Avisamos a todos los clientes (menos el que lanzó la operación)
		// de que ha ocurrido una inserción, modificación o eliminación
		// de un objeto y puede que haya que refrescar alguna ventana
		for(Long id : clientes.keySet()) {
			if(id != idSesion) {
				clientes.get(id).actualizarVentanas(operacion, dato);
			}
		}
	}
	
	public static void cerrarSesionCliente(String nifUsuario) throws Exception {
		Enumeration<Sesion> sesionesAbiertas;
		Sesion sesion, sesionUsuario;

		// Buscamos el cliente asociado al usuario
		sesionUsuario = null;
		sesionesAbiertas = sesiones.elements();
		while(sesionesAbiertas.hasMoreElements() && sesionUsuario == null) {
			sesion = sesionesAbiertas.nextElement();
			if(sesion.getUsuario().getNif().equals(nifUsuario)) {
				sesionUsuario = sesion;
			}
		}
		
		if(sesionUsuario != null) {
			// Si el cliente se había registrado en el sistema, lo desconectamos
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
