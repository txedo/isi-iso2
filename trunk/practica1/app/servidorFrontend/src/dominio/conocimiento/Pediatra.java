package dominio.conocimiento;

import java.io.Serializable;

public class Pediatra extends TipoMedico implements Serializable {

	private static final long serialVersionUID = 5571521599727300482L;

	public Pediatra() {
		super();
	}
	
	public CategoriasMedico getCategoria() {
		return CategoriasMedico.Pediatra;
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
