package presentacion.auxiliar;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interfaz para el evento horaSeleccionada.
 */
public interface HoraSeleccionadaListener extends EventListener {

	public void horaSeleccionada(EventObject evt);
	
}
