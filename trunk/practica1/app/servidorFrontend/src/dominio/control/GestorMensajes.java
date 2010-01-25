package dominio.control;

import java.sql.SQLException;
import dominio.conocimiento.OperacionesAuxiliares;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Clase encargada de interpretar los mensajes auxiliares recibidos
 * y reenviarlos a los gestores que correspondan.
 */
public class GestorMensajes implements OperacionesAuxiliares {
	
	public static Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws SQLException, UsuarioInexistenteException, SesionInvalidaException, OperacionIncorrectaException, CentroSaludIncorrectoException, UsuarioYaExistenteException, MedicoInexistenteException, UsuarioIncorrectoException, BeneficiarioInexistenteException {
		Object res;
		
		res = null;
		switch((int)codigoMensaje) {
			case OPERACIONES_DISPONIBLES:
				res = GestorSesiones.operacionesDisponibles(idSesion);
				break;
			case CONSULTAR_USUARIO:
				res = GestorUsuarios.getUsuario(idSesion, (String)informacion);
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
			case OBTENER_MEDICOS_TIPO:
				res = GestorUsuarios.obtenerMedicos(idSesion, (String)informacion);
				break;
			case CONSULTAR_CITAS_MEDICO:
				res = GestorCitas.calcularCitasMedico(idSesion, (String)informacion);
				break;
		}
		return res;
	}
	
}