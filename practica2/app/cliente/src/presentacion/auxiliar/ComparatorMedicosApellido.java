package presentacion.auxiliar;

import dominio.conocimiento.Medico;

/**
 * Clase que permite ordenar una lista de m�dicos por su apellido.
 */
@SuppressWarnings("unchecked")
public class ComparatorMedicosApellido implements java.util.Comparator {
	
	 public int compare(Object medico1, Object medico2) {
		return ((Medico)medico1).getApellidos().compareTo(((Medico)medico2).getApellidos());
	 }

}
