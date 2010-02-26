package presentacion.auxiliar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Implementaci�n del TableCellRendererCitas que permite marcar de
 * color azul una o varias filas de una tabla.
 */
public class TableCellRendererCitas extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 3560345438754355960L;
	
    private Vector<Integer> filasDesactivadas;

    public TableCellRendererCitas() {
    	super();
    	filasDesactivadas = new Vector<Integer>();
    }
    
    public Vector<Integer> getFilasDesactivadas() {
    	return filasDesactivadas;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	if(filasDesactivadas.contains(row)) {
    		if(isSelected) {
        		setForeground(Color.WHITE);
    		} else {
        		setForeground(Color.BLUE);
    		}
    		setFont(getFont().deriveFont(Font.ITALIC));
    	} else {
    		if(isSelected) {
        		setForeground(Color.WHITE);
    		} else {
        		setForeground(Color.BLACK);
    		}
    	}
    	return this;
    }

}
