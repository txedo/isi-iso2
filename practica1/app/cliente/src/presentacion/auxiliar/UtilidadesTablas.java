package presentacion.auxiliar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.TipoMedico;

/**
 * Funciones auxiliares para manejar tablas de citas y beneficiarios.
 */
public class UtilidadesTablas {

	private static SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

	public static void crearTablaCitasBeneficiario(JTable tabla, int nfilas) {
		TableModelNoEditable modeloTabla;
		Vector<String> encabezado;

		// Inicializamos una nueva tabla de citas
		encabezado = new Vector<String>();
		encabezado.add("Día");
		encabezado.add("Hora");
		encabezado.add("Médico");
		encabezado.add("DNI Médico");
		encabezado.add("Tipo Médico");
		modeloTabla = new TableModelNoEditable(encabezado, nfilas);
		tabla.setModel(modeloTabla);
		tabla.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(2).setMinWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(4).setMinWidth(150);
		tabla.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(300);
	}
	
	public static void rellenarTablaCitasBeneficiario(JTable tabla, Vector<Cita> citas, Vector<Medico> medicosReales) {
		Date fecha;
		TipoMedico tipo;
		int fila, col;
		
		// Eliminamos la lista de citas pendientes
		((TableCellRendererCitas)tabla.getDefaultRenderer(Object.class)).getFilasDesactivadas().clear();
		
		for(fila = 0; fila < citas.size(); fila++) {
			col = 0;
			fecha = citas.get(fila).getFechaYHora();
			tipo = citas.get(fila).getMedico().getTipoMedico();
			tabla.setValueAt(formatoDia.format(fecha), fila, col++);			
			tabla.setValueAt(formatoHora.format(fecha), fila, col++);
			if(!citas.get(fila).getMedico().equals(medicosReales.get(fila))) {
				tabla.setValueAt(medicosReales.get(fila).getApellidos() + ", " + medicosReales.get(fila).getNombre() + " (sustituye a " + citas.get(fila).getMedico().getApellidos() + ", " + citas.get(fila).getMedico().getNombre() + ")", fila, col++);
				tabla.setValueAt(medicosReales.get(fila).getDni(), fila, col++);
			} else {
				tabla.setValueAt(citas.get(fila).getMedico().getApellidos() + ", " + citas.get(fila).getMedico().getNombre(), fila, col++);
				tabla.setValueAt(citas.get(fila).getMedico().getDni(), fila, col++);
			}
			if(tipo.getCategoria() == CategoriasMedico.Especialista) {
				tabla.setValueAt(tipo.getCategoria().name() + " (" + ((Especialista)tipo).getEspecialidad() + ")", fila, col++);
			} else {
				tabla.setValueAt(tipo.getCategoria().name(), fila, col++);
			}
		}
	}
	
	public static void rellenarTablaCitasBeneficiario(JTable tabla, Vector<Cita> citas, Vector<Cita> pendientes, Vector<Medico> medicosReales) {
		int fila;

		// Rellenamos la tabla con las citas
		rellenarTablaCitasBeneficiario(tabla, citas, medicosReales);
		
		// Guardamos una lista con las citas que deben desactivarse
		for(fila = 0; fila < citas.size(); fila++) {
			if(!pendientes.contains(citas.get(fila))) {
				// Eliminamos la lista de citas pendientes
				((TableCellRendererCitas)tabla.getDefaultRenderer(Object.class)).getFilasDesactivadas().add(fila);
			}
		}
	}
	
	public static void crearTablaCitasMedico(JTable tabla, int nfilas) {
		TableModelNoEditable modeloTabla;
		Vector<String> encabezado;

		// Inicializamos una nueva tabla de citas
		encabezado = new Vector<String>();
		encabezado.add("Día");
		encabezado.add("Hora");
		encabezado.add("Beneficiario");
		encabezado.add("NIF Beneficiario");
		modeloTabla = new TableModelNoEditable(encabezado, nfilas);
		tabla.setModel(modeloTabla);
		tabla.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(2).setMinWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(200);
	}
	
	public static void rellenarTablaCitasMedico(JTable tabla, Vector<Cita> citas) {
		Date fecha;
		int fila, col;
		
		// Eliminamos la lista de citas pendientes
		((TableCellRendererCitas)tabla.getDefaultRenderer(Object.class)).getFilasDesactivadas().clear();
		
		for(fila = 0; fila < citas.size(); fila++) {
			col = 0;
			fecha = citas.get(fila).getFechaYHora();
			tabla.setValueAt(formatoDia.format(fecha), fila, col++);			
			tabla.setValueAt(formatoHora.format(fecha), fila, col++);
			tabla.setValueAt(citas.get(fila).getBeneficiario().getApellidos() + ", " + citas.get(fila).getBeneficiario().getNombre(), fila, col++);
			tabla.setValueAt(citas.get(fila).getBeneficiario().getNif(), fila, col++);
		}
	}
	
	public static void rellenarTablaCitasMedico(JTable tabla, Vector<Cita> citas, Vector<Cita> pendientes) {
		int fila;

		// Rellenamos la tabla con las citas
		rellenarTablaCitasMedico(tabla, citas);
		
		// Guardamos una lista con las citas que deben desactivarse
		for(fila = 0; fila < citas.size(); fila++) {
			if(!pendientes.contains(citas.get(fila))) {
				// Eliminamos la lista de citas pendientes
				((TableCellRendererCitas)tabla.getDefaultRenderer(Object.class)).getFilasDesactivadas().add(fila);
			}
		}
	}
	
	public static void crearTablaBeneficiarios(JTable tabla, int nfilas) {
		TableModelNoEditable modeloTabla;
		Vector<String> encabezado;

		// Inicializamos la tabla de beneficiarios
		encabezado = new Vector<String>();
		encabezado.add("Beneficiario");
		encabezado.add("NIF");
		encabezado.add("Nuevo médico");
		encabezado.add("DNI Médico");
		modeloTabla = new TableModelNoEditable(encabezado, nfilas);
		tabla.setModel(modeloTabla);
		tabla.getTableHeader().getColumnModel().getColumn(0).setMinWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(2).setMinWidth(200);
		tabla.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(200);
	}
	
	public static void rellenarTablaBeneficiarios(JTable tabla, Vector<Beneficiario> beneficiarios) {
		int fila, col;
		
		fila = 0;
		for(Beneficiario beneficiario : beneficiarios) {
			col = 0;
			tabla.setValueAt(beneficiario.getApellidos() + ", " + beneficiario.getNombre(), fila, col++);			
			tabla.setValueAt(beneficiario.getNif(), fila, col++);
			tabla.setValueAt((beneficiario.getMedicoAsignado() == null) ? "(ninguno)" : (beneficiario.getMedicoAsignado().getApellidos() + ", " + beneficiario.getMedicoAsignado().getNombre()), fila, col++);
			tabla.setValueAt((beneficiario.getMedicoAsignado() == null) ? "" : beneficiario.getMedicoAsignado().getDni(), fila, col++);
			fila++;
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
