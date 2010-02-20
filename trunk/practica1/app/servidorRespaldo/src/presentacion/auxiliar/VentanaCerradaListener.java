package presentacion.auxiliar;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interfaz para el evento ventanaCerrada de la clase JFConfigRespaldo.
 */
public interface VentanaCerradaListener extends EventListener {

	public void ventanaCerrada(EventObject evt);
	
}
