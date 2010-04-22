package acciones;

import com.opensymphony.xwork2.ActionSupport;

public class loginBeneficiario extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1830263215915906207L;
	
	private String Nss = "";
		
	public String execute() {
		return SUCCESS;
	}
	
	public void setNss(String Nss) {
		this.Nss= Nss;
	}

	public String getNss() {
		return Nss;
	}

}
