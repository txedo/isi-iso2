package acciones;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.opensymphony.xwork2.ActionSupport;

import comunicaciones.IConexion;
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
	private ISesion sesion;
		
	public String execute() {
		ProxyServidorFrontend p;
		try {
			p = ProxyServidorFrontend.getProxy();
			p.conectar(IConexion.IP, IConexion.PUERTO);
			sesion = p.identificarBeneficiario(Nss);
			// Se obtiene el beneficiario a partir del NSS introducido
			beneficiario = p.getBeneficiarioPorNSS(sesion.getId(), Nss);
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

	public ISesion getSesion() {
		return sesion;
	}

}
