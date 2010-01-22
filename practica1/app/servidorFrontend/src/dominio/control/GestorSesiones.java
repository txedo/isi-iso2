package dominio.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Usuario;

import persistencia.FPEntradaLog;
import persistencia.FPUsuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase encargada de iniciar y finalizar sesiones con los clientes
 * del sistema.
 */
public class GestorSesiones {
	
	// Tabla hash de sesiones. La clave es el idSesion y el valor es la Sesion con ese idSesion
	private static Hashtable<Long, Sesion> sesiones = new Hashtable<Long, Sesion>();
		
	// Metodo para cerrar una sesion y borrarla de la tabla de sesiones abiertas
	public static void liberar(long idSesion) throws SQLException {
		EntradaLog entrada;
		entrada = new EntradaLog(sesiones.get(idSesion).getUsuario().getLogin(), "read", "Se ha cerrado la sesion cuyo id era " +idSesion);
		sesiones.remove(idSesion);
		FPEntradaLog.insertar(entrada);
	}	
	
	// Metodo para identificar un cliente y crear una sesion
	public static ISesion identificar(String login, String password) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, Exception {
		Enumeration<Sesion> sesionesAbiertas; 
		Sesion sesionAbierta = null;
		Sesion sesion = null;
		EntradaLog entrada;
		Usuario usuario;
		Random ran;
		boolean encontrado;
		long idSesion;
		
		try {
			
			// Comprobamos el login y el password del usuario
			usuario = FPUsuario.consultar(login, password);
			
			// Se comprueba si el usuario ya tenía una sesion iniciada
			sesionesAbiertas = sesiones.elements();
			encontrado = false;
			while(sesionesAbiertas.hasMoreElements() && !encontrado) {
				sesionAbierta = sesionesAbiertas.nextElement();
				if(sesionAbierta.getUsuario().getDni().equals(usuario.getDni())) {
					encontrado = true;
				}
			}
			// Si el usuario ya tenía una sesion iniciada, se cierra
			if(encontrado) {
				liberar(sesionAbierta.getId());
			}

			// Creamos un identificador único para la nueva sesión
			ran = new Random();
			ran.setSeed(System.currentTimeMillis());
			do {
				idSesion = ran.nextLong();
			} while (sesiones.containsKey(idSesion));

			// Se crea la sesion, se inserta en la tabla de sesiones
			// abiertas y se escribe el log
			sesion = new Sesion(idSesion, usuario);
			sesiones.put(idSesion, sesion);
			entrada = new EntradaLog(login, "read", "Se ha creado la sesion con id "+sesion.getId());
			FPEntradaLog.insertar(entrada);
			
		} catch(UsuarioIncorrectoException ex) {
			entrada = new EntradaLog(login, "read", "Intento de acceso al sistema fallido");
			FPEntradaLog.insertar(entrada);
			throw ex;
		}
		
		return (ISesion)sesion;
	}
	
	public static ArrayList<Operaciones> operacionesDisponibles (long idSesion) throws SesionInvalidaException {
		Sesion sesion;
		ArrayList<Operaciones> operaciones = new ArrayList<Operaciones>();
		
		sesion = sesiones.get(idSesion);
		if (sesion == null) {
			throw new SesionInvalidaException("El identificador de sesión es inválido");
		}
		
		// Se deben agregar al vector todas las operaciones declaradas en Operacion.java
		// Agregamos al vector las operaciones de todos los usuarios (administrador, citador y medico)
		operaciones.add(Operaciones.ConsultarBeneficiario);
		// Agregamos al vector las operaciones de citadores y administradores
		if (sesion.getRol() == Roles.Administrador.ordinal() || sesion.getRol() == Roles.Citador.ordinal()) {
			operaciones.add(Operaciones.TramitarCita);
			operaciones.add(Operaciones.AnularCita);
			operaciones.add(Operaciones.RegistrarBeneficiario);
			operaciones.add(Operaciones.ModificarBeneficiario);
		}
		// Agregamos al vector las operaciones de administradores
		if (sesion.getRol() == Roles.Administrador.ordinal()){
			operaciones.add(Operaciones.ConsultarUsuario);
			operaciones.add(Operaciones.CrearUsuario);
			operaciones.add(Operaciones.ModificarUsuario);
			operaciones.add(Operaciones.EliminarUsuario);
			operaciones.add(Operaciones.ModificarCalendario);
			operaciones.add(Operaciones.EstablecerSustituto);
		}
		// Agregamos al vector las operaciones de médicos
		if (sesion.getRol() == Roles.Medico.ordinal()){
			operaciones.add(Operaciones.EmitirVolante);
			operaciones.add(Operaciones.ConsultarMedicosTipo);
		}
		
		return operaciones;
	}
	
	public static void comprobarPermiso(long idSesion, Operaciones operacion) throws SesionInvalidaException, OperacionIncorrectaException, SQLException {
		Sesion sesion;
		boolean permitido;
		EntradaLog entrada;
		boolean esAdministrador = false;
		boolean esCitador = false;
		boolean esMedico = false;
		
		// Obtenemos la sesión para el id indicado y comprobamos si existe
		// (en teoría sí, porque primero el usuario ha tenido que hacer login)
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionInvalidaException("El identificador de sesión es inválido");
		}
		
		esAdministrador = (sesion.getRol() == Roles.Administrador.ordinal());
		esCitador = (sesion.getRol() == Roles.Citador.ordinal());
		esMedico = (sesion.getRol() == Roles.Medico.ordinal());
		
		// TODO modificar este switch para que analice el arraylist devuelto por operacionesDisponibles()
		// Vemos cuál es la operación solicitada
		switch(operacion) {
		case CrearUsuario:
			permitido = esAdministrador;
			break;
		case ModificarUsuario:
			permitido = esAdministrador; 
			break;
		case EliminarUsuario:
			permitido = esAdministrador; 
			break;
		case TramitarCita:
			permitido = esAdministrador || esCitador;
			break;
		case AnularCita:
			permitido = esAdministrador || esCitador;
			break;
		case ObtenerCitas:
			permitido = esAdministrador || esCitador;
			break;
		case EmitirVolante:
			permitido = esMedico;
			break;
		case RegistrarBeneficiario:
			permitido = esAdministrador || esCitador;
			break;
		case ModificarBeneficiario:
			permitido = esAdministrador || esCitador;
			break;
		case ConsultarBeneficiario:
			permitido = true;
			break;
		case ConsultarMedico:
			permitido = esAdministrador || esCitador;
			break;
		case RegistrarMedico:
			permitido = esAdministrador ;
			break;
		case ModificarMedico:
			permitido = esAdministrador ;
			break;
		case EliminarMedico:
			permitido = esAdministrador ;
			break;
		case ModificarCalendario:
			permitido = esAdministrador;
			break;
		case EstablecerSustituto:
			permitido = esAdministrador;
			break;
		case ConsultarUsuario:
			// Estas operaciones siempre están permitidas
			permitido = true;
			break;
		case ConsultarMedicosTipo:
			// Estas operaciones siempre están permitidas
			permitido = esMedico;
			break;
		default:
			permitido = false;
			break;
		}

		// Comprobamos si se tienen permisos para realizar la operación
		if(!permitido) {
			entrada = new EntradaLog(GestorSesiones.getSesion(idSesion).getUsuario().getLogin(), "read", "No tiene permiso para ejecutar la operacion " + operacion.toString());
			FPEntradaLog.insertar(entrada);
			throw new OperacionIncorrectaException("El rol " + Roles.values()[(int)sesion.getRol()] + " no puede realizar la operación " + operacion.toString());
		}
	}

	public static Sesion getSesion(long idSesion) {
		return sesiones.get(idSesion);
	}
	
}
