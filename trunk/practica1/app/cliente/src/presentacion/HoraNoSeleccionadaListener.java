package presentacion;

import java.util.EventListener;
import java.util.EventObject;

public interface HoraNoSeleccionadaListener extends EventListener {

	public void horaNoSeleccionada (EventObject evt);
}