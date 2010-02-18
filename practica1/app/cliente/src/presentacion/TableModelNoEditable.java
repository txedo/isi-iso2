package presentacion;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * Implementación del DefaultTableModel que impide que se puedan editar las celdas.
 */
public class TableModelNoEditable extends DefaultTableModel {

	private static final long serialVersionUID = 8714446933430487121L;

	public TableModelNoEditable(Vector<?> encabezado, int filas) {
		super(encabezado, filas);
	}
	
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
}
