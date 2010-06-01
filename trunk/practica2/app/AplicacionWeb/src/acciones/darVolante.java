package acciones;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Vector;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.IConexion;
import comunicaciones.ProxyServidorFrontend;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;

public class darVolante extends ActionSupport {

	private static final long serialVersionUID = 8606059954495046094L;
	private Vector<String> especialidades;
	private Vector<Beneficiario> beneficiarios;
	

	public String execute () throws RemoteException, Exception {
		// Establecemos conexión con el servidor front-end 
		ProxyServidorFrontend servidor = ProxyServidorFrontend.getProxy();
		servidor.conectar(IConexion.IP, IConexion.PUERTO);
		// Consultamos las especialidades
		especialidades = new Vector<String>();
		for(Especialidades esp: Especialidades.values()) {
			especialidades.add(esp.name());
		}
		Map<String, Object> parametros = ActionContext.getContext().getSession();
        ISesion sesion = (ISesion) parametros.get("SesionFrontend");
		Medico medico = (Medico) parametros.get("Medico");
		// Consultamos los beneficiarios del médico
		beneficiarios = (Vector<Beneficiario>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, medico.getNif());
		return SUCCESS;
	}
	
	public Vector<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public Vector<String> getEspecialidades() {
		return especialidades;
	}
	
}
