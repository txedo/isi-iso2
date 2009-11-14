package dominio;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import excepciones.CentroSaludIncorrectoException;
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
				cerrarSesion(sesionAbierta);
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
			entrada = new EntradaLog(login, "read", "Se ha creado la sesion");
			entrada.insertar();
			
		} catch(UsuarioIncorrectoException ex) {
			entrada = new EntradaLog(login, "read", "Intento de acceso al sistema fallido");
			entrada.insertar();
			throw ex;
		}
		
		return (ISesion)sesion;
	}
	
	public static boolean comprobar(long idSesion, Operacion operacion) {
		boolean permitido = false;
		// Podrian hacerse comprobaciones si existe ese id. Se supone que si, porque primero se ha hecho el login
		Sesion s = sesiones.get(idSesion);
		 
		switch(operacion) {
		case CrearUsuario:
			permitido = (s.getRol() == Roles.Administrador.ordinal());
			break;
		case ModificarUsuario:
			permitido = (s.getRol() == Roles.Administrador.ordinal()); 
			break;
		case EliminarUsuario:
			permitido = (s.getRol() == Roles.Administrador.ordinal()); 
			break;
		case TramitarCita:
			permitido = (s.getRol() == Roles.Administrador.ordinal() || s.getRol() == Roles.Citador.ordinal()); 
			break;
		case EliminarCita:
			permitido = (s.getRol() == Roles.Administrador.ordinal() || s.getRol() == Roles.Citador.ordinal()); 
			break;
		case RegistrarBeneficiario:
			permitido = (s.getRol() == Roles.Administrador.ordinal() || s.getRol() == Roles.Citador.ordinal()); 
			break;
		case ModificarCalendario:
			permitido = (s.getRol() == Roles.Administrador.ordinal()); 
			break;
		case EstablecerSustituto:
			permitido = (s.getRol() == Roles.Administrador.ordinal());
			break;
		}
		
		return permitido;
	}
	
}
