package presentacion.auxiliar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Clase con m�todos est�ticos para mostrar cuadros de di�logo.
 */
public class Dialogos {
	
	public static void mostrarDialogoError(JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
	}
	
	public static boolean mostrarDialogoPregunta(JFrame ventanaPadre, String titulo, String mensaje) {
		return (JOptionPane.showConfirmDialog(ventanaPadre, mensaje, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION);
	}
	
}
