package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase que representa un centro de salud en el que pueden trabajan
 * los usuarios (incluidos médicos) del sistema.
 */
public class CentroSalud implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 560876087738590995L;
	
	private int id;
	private String nombre;
	private String direccion;
	
	public CentroSalud() {
	}
	
	public CentroSalud(String nombre, String direccion) {
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
		
		c = new CentroSalud(nombre, direccion);
		c.setId(id);
		return c;
	}
	
	public boolean equals(Object o) {
		CentroSalud c;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof CentroSalud) {
			c = (CentroSalud)o;
			dev = nombre.equals(c.getNombre()) && direccion.equals(c.getDireccion());
		}
		return dev;
	}
	
}
