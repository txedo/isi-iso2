package dominio.conocimiento;

/**
 * Interfaz con las constantes que identifican las operaciones auxiliares
 * que se pueden solicitar al servidor front-end.
 */
public interface ICodigosMensajeAuxiliar {
	
	// Operaciones auxiliares del Gestor de Sesiones
	public final int OPERACIONES_DISPONIBLES = 1000;
	
	// Operaciones auxiliares del Gestor de Beneficiarios
	public final int ELIMINAR_BENEFICIARIO = 1001;
	public final int ASIGNAR_MEDICO_BENEFICIARIO = 1002;

	// Operaciones auxiliares del Gestor de Usuarios
	public final int CREAR_USUARIO = 1003;
	public final int MODIFICAR_USUARIO = 1004;
	public final int ELIMINAR_USUARIO = 1005;
	public final int CONSULTAR_USUARIO = 1006;
	
	// Operaciones auxiliares del Gestor de Médicos
	public final int OBTENER_MEDICOS_TIPO = 1007;
	public final int CONSULTAR_HORARIO_MEDICO = 1008;
	public final int OBTENER_POSIBLES_SUSTITUTOS = 1009;
	public final int CONSULTAR_BENEFICIARIOS_MEDICO = 1010;
	
	// Operaciones auxiliares del Gestor de Citas
	public final int CONSULTAR_HORAS_CITAS_MEDICO = 1011;
	public final int CONSULTAR_CITAS_MEDICO = 1012;
	public final int CONSULTAR_DIAS_COMPLETOS = 1013;
	public final int CONSULTAR_CITAS_PENDIENTES = 1014;
	
	// Operaciones auxiliares del Gestor de Volantes
	public final int CONSULTAR_VOLANTE = 1015;
	
}
