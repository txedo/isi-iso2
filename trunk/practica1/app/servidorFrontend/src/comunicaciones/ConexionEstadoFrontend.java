package comunicaciones;

import java.util.ArrayList;
import presentacion.IVentanaEstado;

/**
 * Clase que recibe los mensajes generados por el servidor y los
 * muestra en la ventana principal del servidor frontend.
 */
public class ConexionEstadoFrontend implements IConexionEstado {

	private ArrayList<IVentanaEstado> ventanas;
	
	public ConexionEstadoFrontend() {
		ventanas = new ArrayList<IVentanaEstado>();
	}
	
	public void ponerVentana(IVentanaEstado ventana) {
		ventanas.add(ventana);
	}
	
	// Métodos de las ventanas
	
	public void ponerMensaje(String mensaje) {
		for(IVentanaEstado ventana : ventanas) {
			ventana.actualizarTexto(mensaje);
		}
	}
	
}
