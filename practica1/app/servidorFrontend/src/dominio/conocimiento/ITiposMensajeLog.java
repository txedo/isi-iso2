package dominio.conocimiento;

/**
 * Interfaz con los tipos de mensajes que puede generar el servidor.
 */
public interface ITiposMensajeLog {

	public static final String TIPO_CREATE = "create";
	public static final String TIPO_READ = "read";
	public static final String TIPO_UPDATE = "update";
	public static final String TIPO_DELETE = "delete";
	public static final String TIPO_OTRO = "read"; //TODO:¿cambiar en BD?
	
}
