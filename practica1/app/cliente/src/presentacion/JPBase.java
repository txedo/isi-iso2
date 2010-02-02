package presentacion;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dominio.control.ControladorCliente;

/**
 * Panel base que contiene una referencia a un JFrame y al controlador
 * que da acceso a las operaciones del servidor.
 */
public class JPBase extends JPanel {

	private static final long serialVersionUID = -8138352814844589253L;
	
	private JFrame frame;
	private ControladorCliente controlador;
	
	public JPBase(JFrame frame, ControladorCliente controlador) {
		this.frame = frame;
		this.controlador = controlador;
	}

	public JFrame getFrame() {
		return frame;
	}

	public ControladorCliente getControlador() {
		return controlador;
	}
	
}
