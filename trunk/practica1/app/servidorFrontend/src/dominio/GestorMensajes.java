package dominio;

import excepciones.SesionInvalidaException;

public class GestorMensajes implements OperacionesAuxiliares {
	public static Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws SesionInvalidaException {
		Object res = null;
		if (codigoMensaje == OPERACIONES_DISPONIBLES) { //operaciones disponibles
			res = GestorSesiones.operacionesDisponibles(idSesion);
		}
		return res;
	}
}
