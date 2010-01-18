package presentacion;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dominio.control.ControladorPrincipal;

/**
 * Panel base que contiene una referencia a un JFrame y al controlador
 * que da acceso a las operaciones del servidor.
 */
public class JPBase extends JPanel {

	private static final long serialVersionUID = -8138352814844589253L;
	
	private ControladorPrincipal controlador;
	private JFrame frame;
	
	public void setControlador(ControladorPrincipal controlador) {
		this.controlador = controlador;
	}
	
	public ControladorPrincipal getControlador() {
		return controlador;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
		
	public JFrame getFrame() {
		return frame;
	}
	
}
