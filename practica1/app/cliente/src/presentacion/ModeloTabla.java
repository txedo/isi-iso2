package presentacion;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class ModeloTabla extends DefaultTableModel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5758860904066349581L;

	public ModeloTabla(Vector encabezado, int filas) {
        super(encabezado,filas);
    }
   
}

