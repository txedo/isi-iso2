package acciones;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Vector;

import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.IConexion;
import comunicaciones.ProxyServidorFrontend;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
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
	private Vector<String> especialidades;
	private Vector<Beneficiario> beneficiarios;
	
	@SuppressWarnings("unchecked")
	public String execute() throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ProxyServidorFrontend servidor;

		try {
			// Establecemos conexión con el servidor front-end 
			servidor = ProxyServidorFrontend.getProxy();
			servidor.conectar(IConexion.IP, IConexion.PUERTO);
			// Iniciamos sesión con el usuario y la contraseña introducidos
			sesion = servidor.identificarUsuario(username, pass);
			// Obtenemos los datos del médico
			medico = (Medico)servidor.getMedicoPorLogin(sesion.getId(), username);
			// Consultamos las especialidades
			especialidades = new Vector<String>();
			for(Especialidades esp: Especialidades.values()) {
				especialidades.add(esp.name());
			}
			// Consultamos los beneficiarios del médico
			beneficiarios = (Vector<Beneficiario>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, medico.getNif());
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

	public Vector<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public Vector<String> getEspecialidades() {
		return especialidades;
	}

}
