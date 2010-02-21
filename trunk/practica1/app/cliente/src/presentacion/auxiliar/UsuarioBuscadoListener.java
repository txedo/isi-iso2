package presentacion.auxiliar;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interfaz para el evento UsuarioBuscado de la clase JPUsuarioConsultar.
 */
public interface UsuarioBuscadoListener extends EventListener {

	public void usuarioBuscado(EventObject evt);
	
}
