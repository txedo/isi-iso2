package acciones;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.IConexion;
import comunicaciones.ProxyServidorFrontend;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.Especialista;
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
	private Vector<Especialista> especialistas;
	private int idEspecialidad = -1;
	// Estos valores booleanos indican si se han cargado los especialistas y si se ha emitido el volante.
	// Necesarios para refrescar una u otra parte de la página darVolante.jsp
	private boolean especialistasCargados = false;
	private boolean volanteEmitido = false;
	
	public String execute() {
		ProxyServidorFrontend p;
		try {
			// Si no se ha introducido una especialidad en la página "darVolante.jsp", es porque es la primera vez que se carga.
			// Es decir, esto se ejecuta cuando se loguea el médico.
			p = ProxyServidorFrontend.getProxy();
			p.conectar(IConexion.IP, IConexion.PUERTO);
			if (idEspecialidad==-1) {
				sesion = p.identificarUsuario(username, pass);
				medico = (Medico) p.getMedicoPorLogin(sesion.getId(), username);
				for (Especialidades esp: Especialidades.values())
					especialidades.add(esp.name());
					
			}
			// Si no es la primera vez que se llama esta acción (ya se ha logueado el médico, éste estará en el HTTPSession
			else {
				Map<String, Object> parametros = ActionContext.getContext().getSession();
				sesion = (ISesion) parametros.get("SesionFrontend");
				medico = (Medico) parametros.get("Medico");
				// Además, cuando se seleccione una especialidad y se vuelva a llamar a esta acción, se consultan los especialistas de esa especialidad
				especialistas = (Vector<Especialista>) p.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, new Especialista(Especialidades.values()[idEspecialidad].name()));
				especialistasCargados = true;
			}
			// Consultamos los beneficiarios del médico (se hace siempre, para evitar que al recargar la página se produzca un NullPointerException)
			// Es decir, esta acción debe volver a la página "darVolante.jsp" para que funcione como AJAX, pero parece ser que se pierden los valores
			// que ya se habían cargado antes y se deben colocar de nuevo en la valueStack
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
	
	public Vector<Especialista> getEspecialistas() {
		return especialistas;
	}


	public void setIdEspecialidad(int especialidad) {
		this.idEspecialidad = especialidad;
	}


	public boolean getEspecialistasCargados() {
		return especialistasCargados;
	}


	public boolean getVolanteEmitido() {
		return volanteEmitido;
	}


	public Vector<String> getEspecialidades() {
		return especialidades;
	}



}
