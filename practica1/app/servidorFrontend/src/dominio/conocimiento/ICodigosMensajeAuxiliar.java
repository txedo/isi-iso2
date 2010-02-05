package dominio.conocimiento;

/**
 * Interfaz con las constantes que identifican las operaciones auxiliares
 * que se pueden solicitar al servidor front-end.
 */
public interface ICodigosMensajeAuxiliar {
	
	public final int OPERACIONES_DISPONIBLES = 1000;
	
	// M�todos adicionales del Gestor de Usuarios
	public final int CREAR_USUARIO = 1001;
	public final int MODIFICAR_USUARIO = 1002;
	public final int ELIMINAR_USUARIO = 1003;
	public final int CONSULTAR_USUARIO = 1004;

	// M�todos adicionales del Gestor de Citas
	public final int CONSULTAR_HORAS_CITAS = 1006;
	public final int CONSULTAR_CITAS_MEDICO = 1005;
	public final int CONSULTAR_DIAS_COMPLETOS = 1007;

	public final int OBTENER_MEDICOS_TIPO = 1008;
		
}
