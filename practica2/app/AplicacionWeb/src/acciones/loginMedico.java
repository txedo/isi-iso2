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

/* 
 * Esta acci�n se encarga de identificar al m�dico que hace login, consultando los
 * beneficiarios que tiene asociado ese m�dico.
 */
public class loginMedico extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5026160115822864726L;
	
	private Medico medico;
	private String username;
	private String pass;
	private ISesion sesion;
	private Vector<String> especialidades = new Vector<String>();
	private Vector<Beneficiario> beneficiarios;
	
	public String execute() throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ProxyServidorFrontend p;
		try {
			p = ProxyServidorFrontend.getProxy();
			p.conectar(IConexion.IP, IConexion.PUERTO);
			sesion = p.identificarUsuario(username, pass);
			medico = (Medico) p.getMedicoPorLogin(sesion.getId(), username);
			// Consultamos las especialidades
			for (Especialidades esp: Especialidades.values())
				especialidades.add(esp.name());
			// Consultamos los beneficiarios del m�dico
			beneficiarios = (Vector<Beneficiario>) p.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, medico.getNif());
			return SUCCESS;
		} catch (RemoteException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (UsuarioIncorrectoException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
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


	public Vector<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public Vector<String> getEspecialidades() {
		return especialidades;
	}



}
