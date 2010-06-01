package acciones;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.ServidorFrontend;

import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import excepciones.UsuarioIncorrectoException;

/**
 * Acción ejecutada cuando un médico quiere iniciar sesión en
 * la aplicación web.
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

			// Iniciamos sesión con el usuario y la contraseña introducidos
			servidor = ServidorFrontend.getServidor();
			sesion = servidor.identificarUsuario(username, pass);
			// Obtenemos los datos del médico
			medico = (Medico)servidor.consultarMedicoPorLogin(sesion.getId(), username);
		

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
