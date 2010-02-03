package comunicaciones;

import java.sql.SQLException;
import dominio.conocimiento.EntradaLog;
import persistencia.FPEntradaLog;

/**
 * Clase que recibe los mensajes generados por el servidor y los
 * muestra en una ventana de estado.
 */
public class ConexionLogBD implements IConexionLog {

	public ConexionLogBD() {
	}
	
	// Métodos del log del servidor
	
	public void ponerMensaje(String tipoMensaje, String mensaje) throws SQLException {
		EntradaLog entrada;
		
		entrada = new EntradaLog("---", tipoMensaje, mensaje);
		FPEntradaLog.insertar(entrada);
	}
	
	public void ponerMensaje(String usuario, String tipoMensaje, String mensaje) throws SQLException {
		EntradaLog entrada;
		
		entrada = new EntradaLog(usuario, tipoMensaje, mensaje);
		FPEntradaLog.insertar(entrada);
	}

	public void actualizarClientesEscuchando(int numeroClientes) {
		// Estos mensajes no se guardan en la base de datos
	}

}
