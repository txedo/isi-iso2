package dominio;

import excepciones.SesionInvalidaException;

public class GestorMensajes {
	public static Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws SesionInvalidaException {
		Object res = null;
		if (codigoMensaje == 1000) { //operaciones disponibles
			res = GestorSesiones.operacionesDisponibles(idSesion);
		}
		return res;
	}
}
