package comunicaciones;

import java.util.ArrayList;
import presentacion.IVentanaLog;

/**
 * Clase que recibe los mensajes generados por el servidor y los
 * muestra en la ventana principal del servidor frontend.
 */
public class ConexionLogFrontend implements IConexionLog {

	private ArrayList<IVentanaLog> ventanas;
	
	public ConexionLogFrontend() {
		ventanas = new ArrayList<IVentanaLog>();
	}
	
	public void ponerVentana(IVentanaLog ventana) {
		ventanas.add(ventana);
	}
	
	public void ponerMensaje(String mensaje) {
		for(IVentanaLog ventana : ventanas) {
			ventana.actualizarTexto(mensaje);
		}
	}
	
}
