package dominio.conocimiento;

/**
 * Interfaz implementada por los objetos Sesion.
 */
public interface ISesion {
	
	public boolean cambioSesion = false;
	
	public long getId();
	
	public long getRol();
	
	public boolean isModificada();
	
}
