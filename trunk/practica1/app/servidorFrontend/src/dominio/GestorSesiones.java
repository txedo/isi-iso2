package dominio;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

public class GestorSesiones {
	// Tabla hash de sesiones. La clave es el idSesion y el valor es la Sesion con ese idSesion
	private static Hashtable<Long,Sesion> sesiones = new Hashtable<Long,Sesion>();
		
	public static void cerrarSesion (Sesion sesion){
		sesiones.remove(sesion.getId());
	}	
	
	public static ISesion identificar(String login, String password) throws SQLException, UsuarioIncorrectoException {
		long idSesion;
		Sesion s = null;
		boolean encontrado=false;
		Sesion sesionAbierta = null;
		
		// Se consulta el login y password. Si el usuario existe, se crea la sesion
		Usuario u = Usuario.consultar(login, password);		
		if (u!=null){
			// Se comprueba si el usuario ya tenia una sesion iniciada
			Enumeration<Sesion> sesionesAbiertas = sesiones.elements();
			while(sesionesAbiertas.hasMoreElements() && !encontrado) {
				sesionAbierta=sesionesAbiertas.nextElement();			
				if (sesionAbierta.getUsuario().getDni() == u.getDni())
					encontrado=true;
			}
			// Si tenia una sesion iniciada, se cierra
			if (encontrado)
				cerrarSesion(sesionAbierta);
			
			/* 
			 * 
			 * Cambiar a una forma de generar id unicos 
			 * 
			 * 
			 */
			Random ran = new Random();
			// � Cambiar la semilla del random ? 
			
			// El identificador de sesion debe ser unico
			do {
				idSesion = ran.nextLong();
			} while (sesiones.containsKey(idSesion));
			
			// Se crea la sesion, se inserta en la tabla de sesiones abiertas
			s = new Sesion(idSesion,u);
			sesiones.put(idSesion, s);
		}
		//else
			// Se podria poner una excepcion UsuarioIncorrecto para que en la interfaz se mostrase un error
			// Aunque tambien se puede saber consultando si la ISesion es NULL, porque si lo es, es que no se ha creado la sesion en el IF
		
		return (ISesion)s;
	}
	
	public static boolean comprobar(long idSesion, Operacion operacion) {
		boolean permitido = false;
		// Podrian hacerse comprobaciones si existe ese id. Se supone que si, porque primero se ha hecho el login
		Sesion s = sesiones.get(idSesion);
		 
		switch(operacion){
		case CrearUsuario:
			if (s.getRol()==) permitido=true;
			break;
		case EliminarUsuario:
			if (s.getRol()==) permitido=true;
			break;
		case TramitarCita:
			if (s.getRol()==) permitido=true;
			break;
		case EliminarCita:
			if (s.getRol()==) permitido=true;
			break;
		case RegistrarBeneficiario:
			if (s.getRol()==) permitido=true;
			break;
		}
		
		return permitido;
	}

	
	
}
