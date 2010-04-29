package presentacion;

import javax.swing.JPanel;
import dominio.control.ControladorCliente;

/**
 * Panel base que contiene una referencia a la ventana principal del
 * cliente y al controlador que da acceso a las operaciones del servidor.
 */
public class JPBase extends JPanel {

	private static final long serialVersionUID = -8138352814844589253L;
	
	private JFPrincipal frame;
	private ControladorCliente controlador;
	
	public JPBase() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// con el Jigloo los formularios heredados de JPBase
	}
	
	public JPBase(JFPrincipal frame, ControladorCliente controlador) {
		this.frame = frame;
		this.controlador = controlador;
	}

	public JFPrincipal getFrame() {
		return frame;
	}

	public ControladorCliente getControlador() {
		return controlador;
	}
	
}
