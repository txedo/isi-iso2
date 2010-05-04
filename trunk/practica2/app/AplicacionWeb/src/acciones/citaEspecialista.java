package acciones;

import com.opensymphony.xwork2.ActionSupport;

public class citaEspecialista extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4741351353645398459L;
	
	private long nVolante;
	
	public String execute() {
		return SUCCESS;
	}

	public long getNVolante() {
		return nVolante;
	}

	public void setNVolante(long volante) {
		nVolante = volante;
	}

}
