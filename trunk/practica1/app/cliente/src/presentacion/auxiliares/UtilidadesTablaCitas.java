package presentacion.auxiliares;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.sun.corba.se.impl.encoding.CodeSetConversion.CTBConverter;

import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.TipoMedico;

public class UtilidadesTablaCitas {

	public static void crearTabla(JTable tblTablaCitas, int nfilas) {
		TableModelNoEditable modeloTabla;
		Vector<String> encabezado;

		// Inicializamos una nueva tabla de citas
		encabezado = new Vector<String>();
		encabezado.add("Día");
		encabezado.add("Hora");
		encabezado.add("Médico");
		encabezado.add("DNI Médico");
		encabezado.add("Tipo Médico");
		encabezado.add("Especialidad");
		modeloTabla = new TableModelNoEditable(encabezado, nfilas);
		tblTablaCitas.setModel(modeloTabla);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(2).setMinWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(200);
		tblTablaCitas.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(200);
	}
	
	public static void rellenarTabla(JTable tblTablaCitas, Vector<Cita> citas) {
		Date fecha;
		TipoMedico tipo;
		int fila, col;
		SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
		
		// Eliminamos la lista de citas pendientes
		((TableCellRendererCitas)tblTablaCitas.getDefaultRenderer(Object.class)).getFilasDesactivadas().clear();
		
		for(fila = 0; fila < citas.size(); fila++) {
			col = 0;
			fecha = citas.get(fila).getFechaYHora();
			tipo = citas.get(fila).getMedico().getTipoMedico();
			tblTablaCitas.setValueAt(formatoDia.format(fecha), fila, col++);			
			tblTablaCitas.setValueAt(formatoHora.format(fecha), fila, col++);
			tblTablaCitas.setValueAt(citas.get(fila).getMedico().getApellidos() + ", " + citas.get(fila).getMedico().getNombre(), fila, col++);
			tblTablaCitas.setValueAt(citas.get(fila).getMedico().getDni(), fila, col++);
			tblTablaCitas.setValueAt(tipo.getClass().getSimpleName(), fila, col++);
			if(tipo.getCategoria() == CategoriasMedico.Especialista) {
				tblTablaCitas.setValueAt(((Especialista)tipo).getEspecialidad(), fila, col++);
			} else {
				tblTablaCitas.setValueAt("", fila, col++);
			}
		}
	}
	
	public static void rellenarTabla(JTable tblTablaCitas, Vector<Cita> citas, Vector<Cita> pendientes) {
		int fila;

		// Rellenamos la tabla con las citas
		rellenarTabla(tblTablaCitas, citas);
		
		// Guardamos una lista con las citas que deben desactivarse
		for(fila = 0; fila < citas.size(); fila++) {
			if(!pendientes.contains(citas.get(fila))) {
				// Eliminamos la lista de citas pendientes
				((TableCellRendererCitas)tblTablaCitas.getDefaultRenderer(Object.class)).getFilasDesactivadas().add(fila);
			}
		}
	}
	
	public static void limpiarTabla(JTable tblTablaCitas) {
		DefaultTableModel modelo;
		
		modelo = (DefaultTableModel)tblTablaCitas.getModel();
		while(modelo.getRowCount() > 0){
			modelo.removeRow(0);
		}
	}
}
