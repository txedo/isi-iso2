package dominio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase que representa un usuario del sistema con rol de m�dico.
 */
public class Medico extends Usuario implements Serializable {

	private static final long serialVersionUID = -8629345838800810415L;

	private ArrayList<PeriodoTrabajo> calendario;
	
	public Medico(String dni, String login, String password, String nombre, String apellidos, CentroSalud centro) {
		super(dni, login, password, nombre, apellidos, centro);
		calendario = new ArrayList<PeriodoTrabajo>();
	}

	public Medico() {
		super(); 
	}
	
	public Rol getRol() {
		return Rol.Medico;
	}
	
	public ArrayList<PeriodoTrabajo> getCalendario() {
		return calendario;
	}

	public void setCalendario(ArrayList<PeriodoTrabajo> calendario) {
		this.calendario = calendario;
	}
	
	public boolean equals(Object o) {
		Medico m;
		boolean dev;
		int i;
		
		dev = false;
		if(o != null && o instanceof Medico) {
			m = (Medico)o;
			dev = super.equals(m);
			// Para que dos m�dicos sean iguales, deben tener el
			// mismo calendario (aunque los per�odos de trabajo
			// no tienen por qu� estar en el mismo orden)
			dev = dev && calendario.size() == m.getCalendario().size();
			for(i = 0; dev && i < calendario.size(); i++) {
				dev = dev && m.getCalendario().contains(calendario.get(i));
			}
			for(i = 0; dev && i < m.getCalendario().size(); i++) {
				dev = dev && calendario.contains(m.getCalendario().get(i));
			}
		}
		return dev;
	}
	
}
