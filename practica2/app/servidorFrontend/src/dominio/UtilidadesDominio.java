package dominio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import dominio.conocimiento.DiaSemana;

/**
 * Clase con métodos estáticos auxiliares utilizados en otras clases.
 */
public class UtilidadesDominio {

	public static String encriptarPasswordSHA1(String password) throws NoSuchAlgorithmException {
		MessageDigest clave;
		StringBuffer bufClave;
		byte[] bytesClave;
		int valor;
		int i;

		// Algoritmo de encriptación extraído de la web:
		// http://www.rgagnon.com/javadetails/java-0400.html
		
		// Generamos la clave encriptada con el algoritmo SHA-1
		clave = java.security.MessageDigest.getInstance("SHA-1");
		clave.reset();
		clave.update(password.getBytes());
		bytesClave = clave.digest();

		// Convertimos la clave en una cadena hexadecimal
		bufClave = new StringBuffer(bytesClave.length * 2);
		for (i = 0; i < bytesClave.length; i++) {
			valor = bytesClave[i] & 0xff;
			if (valor < 16) {
				bufClave.append('0');
			}
			bufClave.append(Integer.toHexString(valor));
		}

		return bufClave.toString();
	}

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
	
	public static boolean fechaAnterior(Date fecha1, Date fecha2, boolean considerarHora) {
		Calendar calend, calendSH;
		Date fecha1SH, fecha2SH;
		boolean dev;
		
		if(considerarHora) {
			// Comparamos directamente las dos fechas
			dev = fecha1.before(fecha2);
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
			dev = fecha1SH.before(fecha2SH);
		}
		
		return dev;
	}
	
	public static boolean fechaIgual(Date fecha1, Date fecha2, boolean considerarHora) {
		Calendar calend, calendSH;
		Date fecha1SH, fecha2SH;
		boolean dev;
		
		if(considerarHora) {
			// Comparamos directamente las dos fechas
			dev = fecha1.getTime() == fecha2.getTime();
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
			dev = fecha1SH.getTime() == fecha2SH.getTime();
		}
		
		return dev;
	}
	
}
