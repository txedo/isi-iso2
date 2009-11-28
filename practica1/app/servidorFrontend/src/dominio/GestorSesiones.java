package dominio;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;

public class GestorSesiones {
	// Tabla hash de sesiones. La clave es el idSesion y el valor es la Sesion con ese idSesion
	private static Hashtable<Long,Sesion> sesiones = new Hashtable<Long,Sesion>();
		
	public static void cerrarSesion(Sesion sesion) throws SQLException {
		EntradaLog entrada;
		
		sesiones.remove(sesion.getId());
		entrada = new EntradaLog(sesion.getUsuario().getLogin(), "read", "Se ha cerrado la sesion");
		entrada.insertar();
	}	
	
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
			usuario = Usuario.consultar(login, password);
			
			// Se comprueba si el usuario ya ten�a una sesion iniciada
			sesionesAbiertas = sesiones.elements();
			encontrado = false;
			while(sesionesAbiertas.hasMoreElements() && !encontrado) {
				sesionAbierta = sesionesAbiertas.nextElement();
				if(sesionAbierta.getUsuario().getDni().equals(usuario.getDni())) {
					encontrado = true;
				}
			}
			// Si el usuario ya ten�a una sesion iniciada, se cierra
			if(encontrado) {
				cerrarSesion(sesionAbierta);
			}

			// Creamos un identificador �nico para la nueva sesi�n
			ran = new Random();
			ran.setSeed(System.currentTimeMillis());
			do {
				idSesion = ran.nextLong();
			} while (sesiones.containsKey(idSesion));

			// Se crea la sesion, se inserta en la tabla de sesiones
			// abiertas y se escribe el log
			sesion = new Sesion(idSesion, usuario);
			sesiones.put(idSesion, sesion);
			entrada = new EntradaLog(login, "read", "Se ha creado la sesion");
			entrada.insertar();
			
		} catch(UsuarioIncorrectoException ex) {
			entrada = new EntradaLog(login, "read", "Intento de acceso al sistema fallido");
			entrada.insertar();
			throw ex;
		}
		
		return (ISesion)sesion;
	}
	
	public static void comprobarPermiso(long idSesion, Operacion operacion) throws SesionInvalidaException, OperacionIncorrectaException {
		Sesion sesion;
		boolean permitido;

		// Obtenemos la sesi�n para el id indicado y comprobamos si existe
		// (en teor�a s�, porque primero el usuario ha tenido que hacer login)
		sesion = sesiones.get(idSesion);
		if(sesion == null) {
			throw new SesionInvalidaException("El identificador de sesi�n es inv�lido");
		}
		
		// Vemos cu�l es la operaci�n solicitada
		switch(operacion) {
		case CrearUsuario:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal());
			break;
		case ModificarUsuario:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal()); 
			break;
		case EliminarUsuario:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal()); 
			break;
		case TramitarCita:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal() || sesion.getRol() == Roles.Citador.ordinal()); 
			break;
		case EliminarCita:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal() || sesion.getRol() == Roles.Citador.ordinal()); 
			break;
		case RegistrarBeneficiario:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal() || sesion.getRol() == Roles.Citador.ordinal()); 
			break;
		case ModificarBeneficiario:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal() || sesion.getRol() == Roles.Citador.ordinal()); 
			break;
		case ModificarCalendario:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal()); 
			break;
		case EstablecerSustituto:
			permitido = (sesion.getRol() == Roles.Administrador.ordinal());
			break;
		case ConsultarMedico:
		case ConsultarBeneficiario:
			// Estas operaciones siempre est�n permitidas
			permitido = true;
			break;
		default:
			permitido = false;
		}
		
		// Comprobamos si se tienen permisos para realizar la operaci�n
		if(!permitido) {
			throw new OperacionIncorrectaException("El rol " + Roles.values()[(int)sesion.getRol()] + " no puede realizar la operaci�n solicitada");
		}
	}

	public static Sesion getSesion(long idSesion) {
		return sesiones.get(idSesion);
	}
	
}
