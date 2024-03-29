package acciones;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.ServidorFrontend;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.ISesion;
import excepciones.BeneficiarioInexistenteException;

/**
 * Acci�n ejecutada cuando un beneficiario quiere iniciar sesi�n en
 * la aplicaci�n web.
 */
public class loginBeneficiario extends ActionSupport {
	
	private static final long serialVersionUID = 1830263215915906207L;
	
	private String nss;
	private Beneficiario beneficiario;
	private ISesion sesion;
		
	public String execute() throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		ServidorFrontend servidor;
	
		// Iniciamos sesi�n con el NSS introducido
		servidor = ServidorFrontend.getServidor();
		sesion = servidor.identificarBeneficiario(nss);
		// Obtenemos los datos del beneficiario
		beneficiario = servidor.consultarBeneficiarioPorNSS(sesion.getId(), nss);

		return SUCCESS;
	}
	
	public String getNss() {
		return nss;
	}

	public void setNss(String nss) {
		this.nss = nss;
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public ISesion getSesion() {
		return sesion;
	}

}
