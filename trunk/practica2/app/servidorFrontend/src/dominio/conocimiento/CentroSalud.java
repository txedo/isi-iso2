package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase que representa un centro de salud en el que pueden trabajar los
 * usuarios (incluidos médicos) del sistema.
 */
public class CentroSalud implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 560876087738590995L;
	
	private int id;
	private String nombre;
	private String direccion;
	
	public CentroSalud() {
		id = -1;
		nombre = "";
		direccion = "";
	}
	
	public CentroSalud(String nombre, String direccion) {
		id = -1;
		this.nombre = nombre;
		this.direccion = direccion;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Object clone() {
		CentroSalud c;
		
		c = new CentroSalud(getNombre(), getDireccion());
		c.setId(getId());
		return c;
	}
	
	public boolean equals(Object o) {
		CentroSalud c;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof CentroSalud) {
			c = (CentroSalud)o;
			dev = getNombre().equals(c.getNombre()) && getDireccion().equals(c.getDireccion());
		}
		return dev;
	}
	
	public String toString() {
		return getId() + " " + getNombre() + " " + getDireccion();
	}
	
}
