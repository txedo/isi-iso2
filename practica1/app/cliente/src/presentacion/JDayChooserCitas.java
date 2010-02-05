package presentacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.toedter.calendar.JDayChooser;

import dominio.conocimiento.DiaSemana;

/**
 * Implementación del JDayChooser utilizada por el control JDateChooserCitas.
 */
public class JDayChooserCitas extends JDayChooser {

	private static final long serialVersionUID = -1259211645327948405L;

	private ArrayList<Date> fechasDesactivadas;
	private ArrayList<DiaSemana> diasSemanaDesactivados;
	
	public JDayChooserCitas() {
		this(false);
	}
	
	public JDayChooserCitas(boolean weekOfYearVisible) {
		super(weekOfYearVisible);
		fechasDesactivadas = new ArrayList<Date>();
		diasSemanaDesactivados = new ArrayList<DiaSemana>();
	}
	
	public ArrayList<Date> getFechasDesactivadas() {
		return fechasDesactivadas;
	}
	
	public ArrayList<DiaSemana> getDiasSemanaDesactivados() {
		return diasSemanaDesactivados;
	}
	
	@SuppressWarnings("deprecation")
	protected void drawDays() {
		Calendar cal;
		boolean activado;
		int i;
		
		// Llamamos a la función que crea todos los botones de los días
		super.drawDays();
		// Desactivamos los días que correspondan
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		for(i = 0; i < days.length; i++) {
			if(days[i].getText().length() > 0 && Character.isDigit(days[i].getText().charAt(0))) {
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(days[i].getText()));
				// Obtenemos el estado del botón (puede estar ya desactivado
				// si se corresponde con un día fuera del rango del JDateChooser) 
				activado = days[i].isEnabled();
				if(activado && diasSemanaDesactivados != null) {
					// Comprobamos si hay que deshabilitar el día de la semana
					switch(cal.get(Calendar.DAY_OF_WEEK)) {
					case Calendar.MONDAY:
						activado = !diasSemanaDesactivados.contains(DiaSemana.Lunes);
						break;
					case Calendar.TUESDAY:
						activado = !diasSemanaDesactivados.contains(DiaSemana.Martes);
						break;
					case Calendar.WEDNESDAY:
						activado = !diasSemanaDesactivados.contains(DiaSemana.Miercoles);
						break;
					case Calendar.THURSDAY:
						activado = !diasSemanaDesactivados.contains(DiaSemana.Jueves);
						break;
					case Calendar.FRIDAY:
						activado = !diasSemanaDesactivados.contains(DiaSemana.Viernes);
						break;
					case Calendar.SATURDAY:
						activado = !diasSemanaDesactivados.contains(DiaSemana.Sabado);
						break;
					case Calendar.SUNDAY:
						activado = !diasSemanaDesactivados.contains(DiaSemana.Domingo);
						break;
					}
				}
				if(activado && fechasDesactivadas != null) {
					// Comprobamos si hay que deshabilitar la fecha concreta
					activado = !fechasDesactivadas.contains(new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
				}
				days[i].setEnabled(activado);
			}
		}
	}
	
}
