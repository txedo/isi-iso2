package presentacion.auxiliar;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interfaz para el evento ventanaCerrada de la clase JFConfiguracion.
 */
public interface VentanaCerradaListener extends EventListener {

	public void ventanaCerrada(EventObject evt);
	
}
