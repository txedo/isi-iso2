package dominio.conocimiento;

/**
 * Interfaz en la que se definen constantes utilizadas por el cliente
 * y el servidor.
 */
public interface IConstantes {
	
	// La duración de las citas debe ser divisible entre 60 para
	// que en una hora quepa un número entero de citas
	public static final int DURACION_CITA = 15;
	public static final int DIAS_LABORABLES = 5;
	public static final int HORA_INICIO_JORNADA = 9;
	public static final int HORA_FIN_JORNADA = 21;
}
