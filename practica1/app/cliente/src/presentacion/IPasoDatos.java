package presentacion;

import java.util.Vector;

import dominio.conocimiento.PeriodoTrabajo;

// Interfaz para comunicar datos entre JFCalendarioLaboral, JPUsuarioConsultar y JPUsuarioRegistrar
public interface IPasoDatos {
	
	public void setPeriodos(Vector<PeriodoTrabajo> p);

}
