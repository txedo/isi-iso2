package presentacion;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import dominio.conocimiento.Cita;

public class RenderTabla extends DefaultTableCellRenderer implements TableCellRenderer
{

    private Vector<Cita> historico;
    private Vector<Cita> pendientes;
    
    public RenderTabla(Vector<Cita> historico, Vector<Cita> pendientes) {
       this.historico = historico;
       this.pendientes = pendientes;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
       boolean isSelected, boolean hasFocus, int row, int column) {
        
    	Component comp = super.getTableCellRendererComponent(table,  value, isSelected, hasFocus, row, column);
    	    	
    	for (Cita c: historico) {
    		if (!pendientes.contains(c))
    			comp.setForeground(Color.CYAN);
    		else
    			comp.setForeground(Color.BLACK);
    	}
    	return comp;
    }

}

