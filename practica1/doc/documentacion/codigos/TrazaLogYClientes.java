public class GestorConexionesLog {
		public static void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws ... {
		for(IConexionLog log : conexiones) {
			log.ponerMensaje(usuario, tipoMensaje, mensaje);
		}
	}
}
public class GestorSesiones {
	public static void actualizarClientes(long idSesion, int operacion, Object dato) throws ... {
		// Avisamos a todos los clientes (menos el que lanzó la operación)
		for(Long id : clientes.keySet()) {
			if(id != idSesion) {
				clientes.get(id).actualizarVentanas(operacion, dato);
			}
		}
	}
}