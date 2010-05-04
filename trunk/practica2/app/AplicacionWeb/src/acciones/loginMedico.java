package acciones;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.IConexion;
import comunicaciones.ProxyServidorFrontend;

import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import excepciones.UsuarioIncorrectoException;

public class loginMedico extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5026160115822864726L;
	
	private Medico medico;
	private String login;
	private String password;
	
	public String execute() {
		ProxyServidorFrontend p;
		ISesion s;
		try {
			p = ProxyServidorFrontend.getProxy();
			p.conectar(IConexion.IP, IConexion.PUERTO);
			s = p.identificarUsuario(login, password);
			medico = (Medico) p.getMedicoPorLogin(s.getId(), login);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
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
	
	
	public Medico getMedico() {
		return medico;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
