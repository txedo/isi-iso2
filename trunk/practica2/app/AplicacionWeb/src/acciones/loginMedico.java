package acciones;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
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
 * Esta acción se encarga de identificar al médico que hace login, consultando los
 * beneficiarios que tiene asociado ese médico.
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
	
	public String execute() {
		ProxyServidorFrontend p;
		try {
			p = ProxyServidorFrontend.getProxy();
			p.conectar(IConexion.IP, IConexion.PUERTO);
			sesion = p.identificarUsuario(username, pass);
			medico = (Medico) p.getMedicoPorLogin(sesion.getId(), username);
			// Consultamos las especialidades
			for (Especialidades esp: Especialidades.values())
				especialidades.add(esp.name());
			// Consultamos los beneficiarios del médico
			beneficiarios = (Vector<Beneficiario>) p.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, medico.getNif());
			
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


	public Vector<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public Vector<String> getEspecialidades() {
		return especialidades;
	}



}
