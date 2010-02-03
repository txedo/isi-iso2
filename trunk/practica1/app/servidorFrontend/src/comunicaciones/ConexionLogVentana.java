package comunicaciones;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import presentacion.IVentanaEstado;

/**
 * Clase que recibe los mensajes generados por el servidor y los
 * muestra en una ventana de estado.
 */
public class ConexionLogVentana implements IConexionLog {

	private ArrayList<IVentanaEstado> ventanas;
	private SimpleDateFormat formatoFecha;
	
	public ConexionLogVentana() {
		ventanas = new ArrayList<IVentanaEstado>();
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	public void ponerVentana(IVentanaEstado ventana) {
		ventanas.add(ventana);
	}
	
	// Métodos del log del servidor
	
	public void ponerMensaje(String tipoMensaje, String mensaje) {
		String fecha;
		
		fecha = formatoFecha.format(new Date());
		for(IVentanaEstado ventana : ventanas) {
			ventana.ponerMensaje(fecha + ": " + mensaje);
		}
	}
	
	public void ponerMensaje(String usuario, String tipoMensaje, String mensaje) {
		String fecha;
		
		fecha = formatoFecha.format(new Date());
		for(IVentanaEstado ventana : ventanas) {
			ventana.ponerMensaje(fecha + " (" + usuario + "): " + mensaje);
		}
	}

	public void actualizarClientesEscuchando(int numeroClientes) {
		for(IVentanaEstado ventana : ventanas) {
			ventana.actualizarClientesEscuchando(numeroClientes);
		}
	}
	
}
