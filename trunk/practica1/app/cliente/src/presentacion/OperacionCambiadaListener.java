package presentacion;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interfaz para el evento OperacionCambiada de la clase JPOperaciones.
 */
public interface OperacionCambiadaListener extends EventListener {

	public void operacionCambiada(EventObject evt);
	
}
