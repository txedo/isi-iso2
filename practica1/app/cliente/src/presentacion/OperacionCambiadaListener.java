package presentacion;

import java.util.EventListener;

/**
 * Interfaz para el evento OperacionCambiada de los paneles.
 */
public interface OperacionCambiadaListener extends EventListener {

	public void operacionCambiada(OperacionCambiadaEvent evt);
	
}
