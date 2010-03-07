package presentacion.auxiliar;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interfaz para el evento beneficiarioBuscado.
 */
public interface BeneficiarioBuscadoListener extends EventListener {

	public void beneficiarioBuscado(EventObject evt);
	
}
