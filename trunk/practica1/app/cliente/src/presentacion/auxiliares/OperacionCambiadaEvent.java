package presentacion.auxiliares;

import java.util.EventObject;

/**
 * Datos para el evento OperacionCambiada de los paneles.
 */
public class OperacionCambiadaEvent extends EventObject {

	private static final long serialVersionUID = -2456189333950067886L;
	
	private OperacionesInterfaz operacion;
	
	public OperacionCambiadaEvent(Object source, OperacionesInterfaz operacion) {
		super(source);
		this.operacion = operacion;
	}
	
	public OperacionesInterfaz getOperacion() {
		return operacion;
	}
	
}
