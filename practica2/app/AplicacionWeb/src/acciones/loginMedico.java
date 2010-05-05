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
	private String username;
	private String pass;
	private ISesion sesion;
	
	public String execute() {
		ProxyServidorFrontend p;
		try {
			p = ProxyServidorFrontend.getProxy();
			p.conectar(IConexion.IP, IConexion.PUERTO);
			sesion = p.identificarUsuario(username, pass);
			medico = (Medico) p.getMedicoPorLogin(sesion.getId(), username);
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

	public void setUsername(String login) {
		this.username = login;
	}

	public void setPass(String password) {
		this.pass = password;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}


	public ISesion getSesion() {
		return sesion;
	}


}
