package acciones;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.ServidorFrontend;

import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import excepciones.UsuarioIncorrectoException;

/**
 * Acci�n ejecutada cuando un m�dico quiere iniciar sesi�n en
 * la aplicaci�n web.
 */
public class loginMedico extends ActionSupport {

	private static final long serialVersionUID = 5026160115822864726L;
	
	private Medico medico;
	private String username;
	private String pass;
	private ISesion sesion;
	
	@SuppressWarnings("unchecked")
	public String execute() throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ServidorFrontend servidor;

		try {
			// Iniciamos sesi�n con el usuario y la contrase�a introducidos
			servidor = ServidorFrontend.getServidor();
			sesion = servidor.identificarUsuario(username, pass);
			// Obtenemos los datos del m�dico
			medico = (Medico)servidor.consultarMedicoPorLogin(sesion.getId(), username);
		} catch (RemoteException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (UsuarioIncorrectoException e) {
			throw e;
		}

		return SUCCESS;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public ISesion getSesion() {
		return sesion;
	}

}
