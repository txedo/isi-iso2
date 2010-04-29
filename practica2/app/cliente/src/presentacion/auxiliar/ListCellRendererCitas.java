package presentacion.auxiliar;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Implementación del DefaultListCellRenderer que permite marcar de
 * color rojo uno o varios elementos de una lista.
 */
public class ListCellRendererCitas extends DefaultListCellRenderer {

	private static final long serialVersionUID = 3413278179872404065L;

	private Vector<Object> elementosDesactivados;
	
	public ListCellRendererCitas() {
		super();
		elementosDesactivados = new Vector<Object>();
	}

	public Vector<Object> getElementosDesactivados() {
		return elementosDesactivados;
	}

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    	super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    	if(value != null && elementosDesactivados.contains(value)) {
    		setForeground(Color.RED);
    	} else {
    		setForeground(Color.BLACK);
    	}
    	return this;
    }
	
}
