package presentacion;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ListCellRendererCitas extends DefaultCellEditor implements ListCellRenderer {

	private static final long serialVersionUID = 6302949166400690545L;
	
	private ArrayList<String> horas;
	
	public ListCellRendererCitas(ArrayList<String> horas) {
		super(new JComboBox());
		this.horas = horas;
	}
	
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = new JLabel(value.toString());
		if (horas.contains(value.toString())) {
			label.setForeground(Color.RED);
			//label.setEnabled(false);
			//label.setFocusable(false);
		}
		else
			label.setForeground(Color.BLACK);
        return label;
    }
    
}
