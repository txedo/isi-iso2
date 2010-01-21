package presentacion;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Clase con métodos estáticos para mostrar cuadros de diálogo.
 */
public class Dialogos {
	
	public static void mostrarDialogoInformacion(JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void mostrarDialogoError(JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void mostrarDialogoAdvertencia(JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
	}
	
	public static boolean mostrarDialogoPregunta(JFrame ventanaPadre, String titulo, String mensaje) {
		return (JOptionPane.showConfirmDialog(ventanaPadre, mensaje, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION);
	}
	
}
