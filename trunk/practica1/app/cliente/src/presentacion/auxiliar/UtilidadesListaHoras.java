package presentacion.auxiliar;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComboBox;


import dominio.conocimiento.DiaSemana;

public class UtilidadesListaHoras {
	
	@SuppressWarnings("deprecation")
	public static void obtenerListaHoras(JDateChooserCitas dtcDiaCita, Hashtable<DiaSemana, Vector<String>> horasCitas, Hashtable<Date, Vector<String>> citasOcupadas, JComboBox cmbHorasCitas) {
		Vector<String> horas;
		Vector<String> horasOcupadas;
		Date fecha;
		Calendar cal;
		int a�oAct, mesAct, diaAct;
		
		// Obtenemos la fecha de hoy
		cal = Calendar.getInstance();
		a�oAct = cal.get(Calendar.YEAR);
		mesAct = cal.get(Calendar.MONTH);
		diaAct = cal.get(Calendar.DAY_OF_MONTH);
		
		fecha = dtcDiaCita.getDate();
		if(fecha != null) {
			// Comprobamos si el d�a seleccionado es anterior a hoy
			cal.setTime(fecha);
			if(cal.get(Calendar.YEAR) < a�oAct
			 || (cal.get(Calendar.YEAR) == a�oAct && cal.get(Calendar.MONTH) < mesAct)
			 || (cal.get(Calendar.YEAR) == a�oAct && cal.get(Calendar.MONTH) == mesAct && cal.get(Calendar.DAY_OF_MONTH) < diaAct)) {
				desactivarListaHoras(cmbHorasCitas, "El d�a seleccionado no es v�lido");
			} else {
				// Obtenemos la lista de horas disponibles para
				// el d�a de la semana correspondiente
				horas = new Vector<String>();
				cal.setTime(fecha);
				switch(cal.get(Calendar.DAY_OF_WEEK)) {
				case Calendar.MONDAY:
					horas.addAll(horasCitas.get(DiaSemana.Lunes));
					break;
				case Calendar.TUESDAY:
					horas.addAll(horasCitas.get(DiaSemana.Martes));
					break;
				case Calendar.WEDNESDAY:
					horas.addAll(horasCitas.get(DiaSemana.Miercoles));
					break;
				case Calendar.THURSDAY:
					horas.addAll(horasCitas.get(DiaSemana.Jueves));
					break;
				case Calendar.FRIDAY:
					horas.addAll(horasCitas.get(DiaSemana.Viernes));
					break;
				default:
					// Los m�dicos no trabajan los fines de semana
					break;
				}
				// Si la lista no tiene ninguna hora, desactivamos
				// la selecci�n de hora para la cita
				if(horas.size() == 0) {
					desactivarListaHoras(cmbHorasCitas, "El d�a seleccionado no es laboral para el m�dico");
				} else {
					// Obtenemos las horas del d�a que el m�dico ya tiene ocupadas
					horasOcupadas = citasOcupadas.get(new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
					// Rellenamos la lista de horas
					rellenarListaHoras(cmbHorasCitas, horas, horasOcupadas);
				}
			}
		}
	}
	
	public static void rellenarListaHoras(JComboBox cmbHorasCitas, Vector<String> horas, Vector<String> horasOcupadas) {
		int i;
		
		// Actualizamos la lista de horas
		cmbHorasCitas.removeAllItems();
		if(horas != null) {
			for(String hora : horas) {
				if(horasOcupadas != null && horasOcupadas.contains(hora)) {
					cmbHorasCitas.addItem("<html><font color=\"#FF0000\">" + hora + "</font></html>");
				} else {
					cmbHorasCitas.addItem(hora);
				}
			}
		}
		
		// Seleccionamos la primera hora no ocupada
		if(horas != null && horas.size() > 0) {
			i = 0;
			while(i < horas.size() && ((String)cmbHorasCitas.getItemAt(i)).startsWith("<html>")) {
				i++;
			}
			if(i >= horas.size()) {
				cmbHorasCitas.setSelectedIndex(-1);
			} else {
				cmbHorasCitas.setSelectedIndex(i);
			}
		} else {
			cmbHorasCitas.setSelectedIndex(-1);
		}
		
		// Activamos el control
		cmbHorasCitas.setEnabled(true);
	}

	public static void desactivarListaHoras(JComboBox cmbHorasCitas, String mensaje) {
		cmbHorasCitas.removeAllItems();
		cmbHorasCitas.addItem(mensaje);
		cmbHorasCitas.setEnabled(false);
	}
	
}
