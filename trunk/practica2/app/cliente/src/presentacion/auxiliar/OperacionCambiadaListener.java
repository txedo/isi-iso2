package presentacion.auxiliar;

import java.util.EventListener;

/**
 * Interfaz para el evento operacionCambiada de los paneles.
 */
public interface OperacionCambiadaListener extends EventListener {

	public void operacionCambiada(OperacionCambiadaEvent evt);
	
}
