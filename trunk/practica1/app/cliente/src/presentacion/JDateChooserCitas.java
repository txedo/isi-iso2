package presentacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

import dominio.conocimiento.DiaSemana;

/**
 * Implementación del control JDateChooser que permite desactivar
 * días concretos o días de la semana en el calendario desplegable.
 */
public class JDateChooserCitas extends JDateChooser {
		
	private static final long serialVersionUID = 4524665595655741215L;

	public JDateChooserCitas() {
		super();
		// Eliminamos la configuración del calendario antiguo
		popup.remove(jcalendar);
		// Creamos y configuramos el calendario nuevo
		jcalendar = new JCalendarCitas();
		jcalendar.getDayChooser().addPropertyChangeListener("day", this);
		jcalendar.getDayChooser().setAlwaysFireDayProperty(true);
		popup.add(jcalendar);
	}
	
	@SuppressWarnings("deprecation")
	public void ponerFechaDesactivada(Date fecha) {
		ArrayList<Date> fechas;
		Calendar cal;
		
		fechas = ((JDayChooserCitas)((JCalendarCitas)jcalendar).getDayChooser()).getFechasDesactivadas();
		cal = Calendar.getInstance();
		cal.setTime(fecha);
		fechas.add(new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
	}
	
	@SuppressWarnings("deprecation")
	public void quitarFechaDesactivada(Date fecha) {
		ArrayList<Date> fechas;
		Calendar cal;
		
		fechas = ((JDayChooserCitas)((JCalendarCitas)jcalendar).getDayChooser()).getFechasDesactivadas();
		cal = Calendar.getInstance();
		cal.setTime(fecha);
		fechas.remove(new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
	}
	
	public void quitarFechasDesactivadas() {
		ArrayList<Date> fechas;
		
		fechas = ((JDayChooserCitas)((JCalendarCitas)jcalendar).getDayChooser()).getFechasDesactivadas();
		fechas.clear();
	}
	
	public void ponerDiaSemanaDesactivado(DiaSemana dia) {
		ArrayList<DiaSemana> dias;
		
		dias = ((JDayChooserCitas)((JCalendarCitas)jcalendar).getDayChooser()).getDiasSemanaDesactivados();
		dias.add(dia);
	}

	public void quitarDiaSemanaDesactivado(DiaSemana dia) {
		ArrayList<DiaSemana> dias;
		
		dias = ((JDayChooserCitas)((JCalendarCitas)jcalendar).getDayChooser()).getDiasSemanaDesactivados();
		dias.remove(dia);
	}

	public void quitarDiasSemanaDesactivados() {
		ArrayList<DiaSemana> dias;
		
		dias = ((JDayChooserCitas)((JCalendarCitas)jcalendar).getDayChooser()).getDiasSemanaDesactivados();
		dias.clear();
	}

}
