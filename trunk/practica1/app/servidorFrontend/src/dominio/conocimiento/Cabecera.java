package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase de tipo de m�dico que representa un m�dico de cabecera para los
 * beneficiarios de 14 o m�s a�os.
 */
public class Cabecera extends TipoMedico implements Serializable {

	private static final long serialVersionUID = -6691515760167451968L;

	public Cabecera() {
		super();
	}
	
	public CategoriasMedico getCategoria() {
		return CategoriasMedico.Cabecera;
	}
	
	public Object clone() {
		return new Cabecera();
	}
	
	public boolean equals(Object o) {
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Cabecera) {
			dev = true; 
		}
		return dev;
	}
		
}
