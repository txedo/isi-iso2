package dominio.control;

import java.sql.SQLException;
import dominio.conocimiento.OperacionesAuxiliares;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Clase encargada de interpretar los mensajes auxiliares recibidos
 * y reenviarlos a los gestores que correspondan.
 */
public class GestorMensajes implements OperacionesAuxiliares {
	
	public static Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws SesionInvalidaException, SQLException, UsuarioYaExistenteException, OperacionIncorrectaException, CentroSaludIncorrectoException, UsuarioInexistenteException {
		Object res;
		
		res = null;
		switch((int)codigoMensaje) {
			case OPERACIONES_DISPONIBLES:
				res = GestorSesiones.operacionesDisponibles(idSesion);
				break;
			case CREAR_USUARIO:
				GestorUsuarios.crearUsuario(idSesion, (Usuario)informacion);
				break;
			case MODIFICAR_USUARIO:
				GestorUsuarios.modificarUsuario(idSesion, (Usuario)informacion);
				break;
			case ELIMINAR_USUARIO:
				GestorUsuarios.eliminarUsuario(idSesion, (Usuario)informacion);
				break;
		}
		return res;
	}
	
}
