package dominio;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;

public class GestorSesiones {
	// Tabla hash de sesiones. La clave es el idSesion y el valor es la Sesion con ese idSesion
	private static Hashtable<Long,Sesion> sesiones = new Hashtable<Long,Sesion>();
		
	public static void cerrarSesion (Sesion sesion) throws SQLException{
		sesiones.remove(sesion.getId());
		EntradaLog entrada = new EntradaLog(sesion.getUsuario().getLogin(),new Timestamp((new Date()).getTime()),"read","Se ha cerrado la sesion");
		entrada.insertar();
	}	
	
	public static ISesion identificar(String login, String password) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, Exception {
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
				if (sesionAbierta.getUsuario().getDni().equals(u.getDni()))
					encontrado=true;
			}
			// Si tenia una sesion iniciada, se cierra
			if (encontrado)
				cerrarSesion(sesionAbierta);
			
			// El identificador de sesion debe ser unico
			Random ran = new Random();
			ran.setSeed(System.currentTimeMillis()); 		
			do {
				idSesion = ran.nextLong();
			} while (sesiones.containsKey(idSesion));
			
			// Se crea la sesion, se inserta en la tabla de sesiones abiertas y se escribe el log
			s = new Sesion(idSesion,u);
			sesiones.put(idSesion, s);
			EntradaLog entrada = new EntradaLog(login,new Timestamp((new Date()).getTime()),"read","Se ha creado la sesion");
			entrada.insertar();
		}
		else {
			EntradaLog entrada = new EntradaLog(login,new Timestamp((new Date()).getTime()),"read","Intento de acceso al sistema fallido");
			entrada.insertar();
		}
		
		return (ISesion)s;
	}
	
	public static boolean comprobar(long idSesion, Operacion operacion) {
		boolean permitido = false;
		// Podrian hacerse comprobaciones si existe ese id. Se supone que si, porque primero se ha hecho el login
		Sesion s = sesiones.get(idSesion);
		 
		switch(operacion){
		case CrearUsuario:
			if (s.getRol()==Roles.Administrador.ordinal()) 
				permitido=true;
			break;
		case ModificarUsuario:
			if (s.getRol()==Roles.Administrador.ordinal()) 
				permitido=true;
			break;
		case EliminarUsuario:
			if (s.getRol()==Roles.Administrador.ordinal()) 
				permitido=true;
			break;
		case TramitarCita:
			if (s.getRol()==Roles.Administrador.ordinal() || s.getRol()==Roles.Citador.ordinal()) 
				permitido=true;
			break;
		case EliminarCita:
			if (s.getRol()==Roles.Administrador.ordinal() || s.getRol()==Roles.Citador.ordinal()) 
				permitido=true;
			break;
		case RegistrarBeneficiario:
			if (s.getRol()==Roles.Administrador.ordinal() || s.getRol()==Roles.Citador.ordinal()) 
				permitido=true;
			break;
		case ModificarCalendario:
			if (s.getRol()==Roles.Administrador.ordinal()) 
				permitido=true;
			break;
		case EstablecerSustituto:
			if (s.getRol()==Roles.Administrador.ordinal()) 
				permitido=true;
			break;
		}
		
		return permitido;
	}

	
	
}
