package dominio;

import java.io.Serializable;
import java.sql.SQLException;
import excepciones.CentroSaludIncorrectoException;
import persistencia.FPCentroSalud;

/**
 * Clase que representa un centro de salud en el que pueden trabajan
 * los usuarios (incluidos médicos) del sistema.
 */
public class CentroSalud implements Serializable {
	
	private int id;
	private String nombre;
	private String direccion;
	
	public CentroSalud() {
	}
	
	public CentroSalud(String nombre, String direccion) {
		this.nombre = nombre;
		this.direccion = direccion;
	}
	
	public static CentroSalud consultar(int id) throws SQLException, CentroSaludIncorrectoException {
		return FPCentroSalud.consultar(id);
	}

	public static CentroSalud consultarAleatorio() throws SQLException, CentroSaludIncorrectoException {
		return FPCentroSalud.consultarAleatorio();
	}

	public void insertar() throws SQLException {
		// Al insertar el centro le asignamos el id autonumérico
		id = FPCentroSalud.insertar(this);
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
