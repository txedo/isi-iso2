package presentacion.auxiliar;

import java.util.EventListener;
import java.util.EventObject;

public interface HoraSeleccionadaListener extends EventListener {

	public void horaSeleccionada (EventObject evt);
}