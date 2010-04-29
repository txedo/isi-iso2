package presentacion.auxiliar;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interfaz para el evento horaNoSeleccionada.
 */
public interface HoraNoSeleccionadaListener extends EventListener {

	public void horaNoSeleccionada(EventObject evt);
	
}
