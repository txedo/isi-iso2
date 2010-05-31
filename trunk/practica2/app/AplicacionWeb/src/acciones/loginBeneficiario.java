package acciones;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.opensymphony.xwork2.ActionSupport;

import comunicaciones.IConexion;
import comunicaciones.ProxyServidorFrontend;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.ISesion;
import excepciones.BeneficiarioInexistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Acción ejecutada cuando un beneficiario quiere iniciar sesión en
 * la aplicación web.
 */
public class loginBeneficiario extends ActionSupport {
	
	private static final long serialVersionUID = 1830263215915906207L;
	
	private String nss;
	private Beneficiario beneficiario;
	private ISesion sesion;
		
	public String execute() throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ProxyServidorFrontend servidor;
		
		try {
			// Establecemos conexión con el servidor front-end 
			servidor = ProxyServidorFrontend.getProxy();
			servidor.conectar(IConexion.IP, IConexion.PUERTO);
			// Iniciamos sesión con el NSS introducido
			sesion = servidor.identificarBeneficiario(nss);
			// Obtenemos los datos del beneficiario
			beneficiario = servidor.getBeneficiarioPorNSS(sesion.getId(), nss);
		} catch(RemoteException e) {
			throw e;
		} catch(SQLException e) {
			throw e;
		} catch(BeneficiarioInexistenteException e) {
			throw e;
		}

		return SUCCESS;
	}
	
	public void setNss(String nss) {
		this.nss = nss;
	}

	public String getNss() {
		return nss;
	}
	
	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public ISesion getSesion() {
		return sesion;
	}

}
