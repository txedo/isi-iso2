package dominio;

import java.util.Calendar;
import java.util.Date;

import dominio.conocimiento.DiaSemana;

/**
 * Clase con métodos estáticos auxiliares utilizados en otras clases.
 */
public class UtilidadesDominio {

	public static DiaSemana diaFecha(Date fecha) {
		Calendar calend;
		DiaSemana dia;
		int diaNum;
		
		calend = Calendar.getInstance();
		calend.setTime(fecha);
		diaNum = calend.get(Calendar.DAY_OF_WEEK);
		switch(diaNum) {
			case Calendar.MONDAY:
				dia = DiaSemana.Lunes;
				break;
			case Calendar.TUESDAY:
				dia = DiaSemana.Martes;
				break;
			case Calendar.WEDNESDAY:
				dia = DiaSemana.Miercoles;
				break;
			case Calendar.THURSDAY:
				dia = DiaSemana.Jueves;
				break;
			case Calendar.FRIDAY:
				dia = DiaSemana.Viernes;
				break;
			default:
				// Este día de la semana no es válido
				dia = null;
				break;
		}
		
		return dia;
	}
	
	public static boolean fechaIgual(Date fecha1, Date fecha2, boolean considerarHora) {
		Calendar calend, calendSH;
		Date fecha1SH, fecha2SH;
		boolean dev;
		
		if(considerarHora) {
			// Comparamos directamente las dos fechas
			dev = fecha1.equals(fecha2);
		} else {
			// Nos quedamos sólo con la información del día
			calend = Calendar.getInstance();
			calendSH = Calendar.getInstance();
			calend.setTime(fecha1);
			calendSH.set(Calendar.YEAR, calend.get(Calendar.YEAR));
			calendSH.set(Calendar.MONTH, calend.get(Calendar.MONTH));
			calendSH.set(Calendar.DAY_OF_MONTH, calend.get(Calendar.DAY_OF_MONTH));
			fecha1SH = calendSH.getTime();
			calend.setTime(fecha2);
			calendSH.set(Calendar.YEAR, calend.get(Calendar.YEAR));
			calendSH.set(Calendar.MONTH, calend.get(Calendar.MONTH));
			calendSH.set(Calendar.DAY_OF_MONTH, calend.get(Calendar.DAY_OF_MONTH));
			fecha2SH = calendSH.getTime();
			// Comparamos las dos fechas
			dev = fecha1SH.equals(fecha2SH);
		}
		
		return dev;
	}
	
}
