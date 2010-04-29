package acciones;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.ProxyServidorFrontend;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.ISesion;
import excepciones.UsuarioIncorrectoException;

public class loginBeneficiario extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1830263215915906207L;
	
	private String Nss = "";
	private Beneficiario beneficiario;
		
	public String execute() {
		ProxyServidorFrontend p = new ProxyServidorFrontend();
		ISesion s;
		try {
			// TODO: hacer que el porxy sea singleton y se conecte solo la primera vez que se necesite
			// Esa vez será en el loginBeneficiario o en el loginMedico
			p.conectar("127.0.0.1", 2995);
			// TODO: cambiar la forma de autenticarse
			s = p.identificar("admin", "admin123");
			// Se obtiene el beneficiario a partir del NSS introducido
			beneficiario = p.getBeneficiarioPorNSS(s.getId(), Nss);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UsuarioIncorrectoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public void setNss(String Nss) {
		this.Nss= Nss;
	}

	public String getNss() {
		return Nss;
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

}
