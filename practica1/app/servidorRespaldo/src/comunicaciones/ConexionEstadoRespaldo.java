package comunicaciones;

import java.util.ArrayList;
import presentacion.IVentanaEstado;

/**
 * Clase que recibe los mensajes generados por el servidor y los
 * muestra en la ventana principal del servidor de respaldo.
 */
public class ConexionEstadoRespaldo implements IConexionEstado {

	private ArrayList<IVentanaEstado> ventanas;
	
	public ConexionEstadoRespaldo() {
		ventanas = new ArrayList<IVentanaEstado>();
	}
	
	public void ponerVentana(IVentanaEstado ventana) {
		ventanas.add(ventana);
	}
	
	// M�todos de las ventanas
	
	public void ponerMensaje(String mensaje) {
		for(IVentanaEstado ventana : ventanas) {
			ventana.ponerMensaje(mensaje);
		}
	}

	public void actualizarClientesEscuchando(int numeroClientes) {
		for(IVentanaEstado ventana : ventanas) {
			ventana.actualizarClientesEscuchando(numeroClientes);
		}
	}
	
}
