package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase de tipo de médico que representa un pediatra para los beneficiarios
 * de menos de 14 años.
 */
public class Pediatra extends TipoMedico implements Serializable {

	private static final long serialVersionUID = 5571521599727300482L;

	public Pediatra() {
		super();
	}
	
	public CategoriasMedico getCategoria() {
		return CategoriasMedico.Pediatra;
	}
	
	public Object clone() {
		return new Pediatra();
	}
	
	public boolean equals(Object o) {
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Pediatra) {
			dev = true; 
		}
		return dev;
	}
	
}
