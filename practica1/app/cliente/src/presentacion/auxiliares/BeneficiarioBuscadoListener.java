package presentacion.auxiliares;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interfaz para el evento BeneficiarioBuscado de la clase JPBeneficiarioConsultar.
 */
public interface BeneficiarioBuscadoListener extends EventListener {

	public void beneficiarioBuscado(EventObject evt);
	
}
