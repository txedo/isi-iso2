package dominio.conocimiento;

/**
 * Interfaz con las constantes que identifican las operaciones auxiliares
 * que se pueden solicitar al servidor front-end.
 */
public interface ICodigosMensajeAuxiliar {
	
	// Operaciones auxiliares del Gestor de Sesiones
	public final int OPERACIONES_DISPONIBLES = 1000;
	
	// Operaciones auxiliares del Gestor de Beneficiarios
	public final int ELIMINAR_BENEFICIARIO = 1100;

	// Operaciones auxiliares del Gestor de Usuarios
	public final int CREAR_USUARIO = 1200;
	public final int MODIFICAR_USUARIO = 1201;
	public final int ELIMINAR_USUARIO = 1202;
	public final int CONSULTAR_USUARIO = 1203;
	public final int CONSULTAR_PROPIO_USUARIO = 1204;
	public final int CONSULTAR_CENTROS = 1205;
	
	// Operaciones auxiliares del Gestor de Médicos
	public final int OBTENER_MEDICOS_TIPO = 1300;
	public final int CONSULTAR_HORARIO_MEDICO = 1301;
	public final int OBTENER_POSIBLES_SUSTITUTOS = 1302;
	public final int CONSULTAR_BENEFICIARIOS_MEDICO = 1303;
	public final int CONSULTAR_MEDICO_CITA = 1304;
	
	// Operaciones auxiliares del Gestor de Citas
	public final int CONSULTAR_HORAS_CITAS_MEDICO = 1400;
	public final int CONSULTAR_CITAS_MEDICO = 1401;
	public final int CONSULTAR_CITAS_PENDIENTES_MEDICO = 1402;
	public final int CONSULTAR_DIAS_COMPLETOS = 1403;
	public final int CONSULTAR_CITAS_PENDIENTES = 1404;
	public final int CONSULTAR_CITAS_FECHA_MEDICO = 1405;
	public final int CONSULTAR_CITAS_PROPIAS_MEDICO = 1406;
	public final int CONSULTAR_CITAS_PENDIENTES_PROPIAS_MEDICO = 1407;
	
	// Operaciones auxiliares del Gestor de Volantes
	public final int CONSULTAR_VOLANTE = 1500;
	
}
