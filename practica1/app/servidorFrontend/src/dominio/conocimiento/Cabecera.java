package dominio.conocimiento;

import java.io.Serializable;

public class Cabecera extends TipoMedico implements Serializable {

	private static final long serialVersionUID = -6691515760167451968L;

	public Cabecera() {
		super();
	}
	
	public CategoriasMedico getCategoria() {
		return CategoriasMedico.Cabecera;
	}
	
}
